import java.net.*;
import java.util.Scanner;

public class DNSL {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== DNS Lookup Program ===");

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Enter Host Name (Forward Lookup)");
            System.out.println("2. Enter IP Address (Reverse Lookup)");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1:
                        // Forward lookup: Hostname → IP
                        System.out.print("Enter host name: ");
                        String hostName = scanner.nextLine();

                        InetAddress forwardAddress = InetAddress.getByName(hostName);
                        System.out.println("IP Address: " + forwardAddress.getHostAddress());
                        System.out.println("Host Name: " + forwardAddress.getHostName());
                        System.out.println("Full Info: " + forwardAddress.toString());
                        break;

                    case 2:
                        // Reverse lookup: IP → Hostname
                        System.out.print("Enter IP address: ");
                        String ipAddress = scanner.nextLine();

                        InetAddress reverseAddress = InetAddress.getByName(ipAddress);
                        System.out.println("Host Name: " + reverseAddress.getHostName());
                        System.out.println("IP Address: " + reverseAddress.getHostAddress());
                        System.out.println("Full Info: " + reverseAddress.toString());
                        break;

                    case 3:
                        running = false;
                        System.out.println("Exiting program. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                        break;
                }
            } catch (UnknownHostException e) {
                System.out.println("Error: Could not resolve the host/IP.");
            }
        }

        scanner.close();
    }
}
