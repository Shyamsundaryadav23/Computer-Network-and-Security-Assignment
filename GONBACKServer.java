import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GONBACKServer {

    // === Shared Scanner for entire program ===
    // Using one Scanner for System.in to prevent input stream closure issues
    private static final Scanner sc = new Scanner(System.in);

    // === Function to handle Go-Back-N ARQ sending logic ===
    public static void runServer() {
        ServerSocket serverSocket = null;   // Server socket to listen for client connections
        DataInputStream dis = null;         // Input stream to read data from client
        DataOutputStream dos = null;        // Output stream to send data to client

        try {
            // === Step 1: Get packet data from user ===
            System.out.print("Enter number of packets to send: ");
            int count = sc.nextInt();        // Number of packets to send
            int[] a = new int[count];        // Array to store packet data

            // Take packet values as input
            for (int i = 0; i < count; i++) {
                System.out.print("Enter data for packet " + (i + 1) + ": ");
                a[i] = sc.nextInt();         // Read packet data
            }

            // === Step 2: Setup server socket and wait for client ===
            serverSocket = new ServerSocket(8011); // Open server socket on port 8011
            System.out.println("Waiting for connection...");
            Socket client = serverSocket.accept();  // Accept client connection
            System.out.println("Connected to client!");

            // Initialize data streams for communication
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

            // === Step 3: Send total number of packets to client ===
            dos.write(count);                // Send number of packets to client
            dos.flush();

            // === Step 4: Send each packet's data ===
            for (int value : a) {
                dos.write(value);            // Send packet data one by one
                dos.flush();
            }

            // === Step 5: Wait for retransmission request from client ===
            int k = dis.read();              // Read lost packet index from client
            System.out.println("Client requested retransmission for packet index: " + k);

            // Resend requested packet
            dos.write(a[k]);
            dos.flush();

        } catch (IOException e) {
            // Catch any network or I/O related exceptions
            System.out.println("Error: " + e);
        } finally {
            // === Step 6: Close all resources properly ===
            try {
                if (dis != null) dis.close();           // Close input stream
                if (dos != null) dos.close();           // Close output stream
                if (serverSocket != null) serverSocket.close(); // Close server socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // === Main Menu ===
    public static void main(String[] args) {
        int choice;

        // Menu loop allows multiple sessions
        while (true) {
            System.out.println("\n===== GO-BACK-N ARQ SERVER MENU =====");
            System.out.println("1. Start Server");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            // Prevents crash if no valid input
            if (!sc.hasNextInt()) {
                System.out.println("No valid input. Exiting...");
                break;
            }

            choice = sc.nextInt(); // Read user choice

            // Menu options using enhanced switch
            switch (choice) {
                case 1 -> runServer(); // Start server function
                case 2 -> {
                    System.out.println("Exiting Server... Goodbye!");
                    return; // Exit program
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
