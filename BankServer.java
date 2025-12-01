// A simple multi-threaded bank server application in Java
import java.io.*;
import java.net.*;

public class BankServer {
    public static void main(String[] args) {
        Bank bank = new Bank(); // shared for all clients

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is running and waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket, bank).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// ------------------- Bank class -------------------
class Bank {
    private int total = 3000; // shared balance

    public synchronized String withdraw(String name, int amount) {
        if (total >= amount) {
            total -= amount;
            return amount + " withdrawn by " + name + "\nBalance: " + total;
        } else {
            return "Insufficient balance. Current balance: " + total;
        }
    }

    public synchronized String deposit(String name, int amount) {
        total += amount;
        return amount + " deposited by " + name + ". Balance: " + total;
    }

    public synchronized int getBalance() {
        return total;
    }
}

// ------------------- ClientHandler class -------------------
class ClientHandler extends Thread {
    private Socket clientSocket;
    private Bank bank;
    private BufferedReader in;
    private PrintWriter out;
    private final int PIN = 454545; // fixed PIN for simplicity

    public ClientHandler(Socket socket, Bank bank) {
        this.clientSocket = socket;
        this.bank = bank;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Get client name and PIN
            String name = in.readLine();
            int pin = Integer.parseInt(in.readLine());
            if (pin != PIN) {
                out.println("Invalid PIN. Connection closing.");
                clientSocket.close();
                return;
            }
            out.println("Welcome " + name + " to the Bank! Login successful.");

            while (true) {
                out.println("----- MENU -----");
                out.println("1. Withdraw");
                out.println("2. Deposit");
                out.println("3. Check Balance");
                out.println("4. Exit");
                out.println("Enter your choice:");

                String choice = in.readLine();

                switch (choice) {
                    case "1":
                        out.println("Enter amount to withdraw:");
                        int withdrawal = Integer.parseInt(in.readLine());
                        out.println(bank.withdraw(name, withdrawal));
                        break;
                    case "2":
                        out.println("Enter amount to deposit:");
                        int deposit = Integer.parseInt(in.readLine());
                        out.println(bank.deposit(name, deposit));
                        break;
                    case "3":
                        out.println("Your current balance is: " + bank.getBalance());
                        break;
                    case "4":
                        out.println("Thank you for banking with us. Goodbye!");
                        clientSocket.close();
                        return;
                    default:
                        out.println("Invalid choice! Try again.");
                }
            }

        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }
}
