// A simple bank client application in Java
import java.io.*;
import java.net.*;

public class BankClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Send client name
            System.out.print("Enter your name: ");
            String name = userInput.readLine();
            out.println(name);

            // Send PIN
            System.out.print("Enter your PIN: ");
            String pin = userInput.readLine();
            out.println(pin);

            // Read welcome message
            System.out.println(in.readLine());

            while (true) {
                // Print menu
                String serverLine;
                while ((serverLine = in.readLine()) != null && !serverLine.equals("Enter your choice:")) {
                    System.out.println(serverLine);
                }
                System.out.print("> ");
                String choice = userInput.readLine();
                out.println(choice);

                switch (choice) {
                    case "1":
                    case "2":
                        System.out.println(in.readLine()); // Enter amount prompt
                        int amount = Integer.parseInt(userInput.readLine());
                        out.println(amount);
                        System.out.println(in.readLine()); // Transaction result
                        break;
                    case "3":
                    case "4":
                        System.out.println(in.readLine());
                        if (choice.equals("4")) return; // Exit
                        break;
                    default:
                        System.out.println(in.readLine());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
