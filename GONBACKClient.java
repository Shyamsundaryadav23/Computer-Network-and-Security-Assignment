import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GONBACKClient {

    // === Shared Scanner for entire program ===
    private static final Scanner sc = new Scanner(System.in);

    // === Function to handle Go-Back-N ARQ client logic ===
    public static void runClient() {
        try {
            // === Step 1: Establish connection with the server ===
            InetAddress addr = InetAddress.getByName("localhost");  // Server address
            Socket connection = new Socket(addr, 8011);             // Connect to server on port 8011
            System.out.println("Connected to Server.");

            // Create data streams for input and output
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            DataInputStream in = new DataInputStream(connection.getInputStream());

            // === Step 2: Receive number of packets from server ===
            int p = in.read();                 // Number of packets sent by server
            System.out.println("Number of frames received: " + p);

            int[] v = new int[p];              // Array to hold received packets

            // === Step 3: Receive each packet from server ===
            for (int i = 0; i < p; i++) {
                v[i] = in.read();              // Read packet data
                System.out.println("Received: " + v[i]);
            }

            // === Step 4: Simulate packet loss (for testing retransmission) ===
            System.out.print("Enter index (0-" + (p - 1) + ") of packet to mark lost: ");
            int lostIndex = sc.nextInt();      // Get index of lost packet
            v[lostIndex] = -1;                 // Mark it as lost (-1 represents missing frame)

            // === Step 5: Display current received frames ===
            for (int i = 0; i < p; i++) {
                System.out.println("Frame[" + i + "]: " + v[i]);
            }

            // === Step 6: Send retransmission request to server ===
            out.write(lostIndex);              // Request the lost packet by index
            out.flush();

            // === Step 7: Receive retransmitted packet ===
            v[lostIndex] = in.read();          // Receive retransmitted data
            System.out.println("Retransmitted packet received: " + v[lostIndex]);

            // === Step 8: Close connection ===
            System.out.println("Closing connection...");
            connection.close();                // Close socket connection

        } catch (Exception e) {
            // Handle errors like connection failure or invalid input
            System.out.println("Error: " + e);
        }
    }

    // === Main Menu ===
    public static void main(String[] args) {
        int choice;

        while (true) {
            System.out.println("\n===== GO-BACK-N ARQ CLIENT MENU =====");
            System.out.println("1. Connect to Server");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            // Prevents crash if invalid input is given
            if (!sc.hasNextInt()) {
                System.out.println("No valid input. Exiting...");
                break;
            }

            choice = sc.nextInt(); // Read user menu choice

            // Handle menu choices using enhanced switch
            switch (choice) {
                case 1 -> runClient(); // Run client logic
                case 2 -> {
                    System.out.println("Exiting Client... Goodbye!");
                    return; // Exit program
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
