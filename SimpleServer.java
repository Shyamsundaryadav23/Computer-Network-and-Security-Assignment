// SimpleServer.java : A simple server program with continuous communication.
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class SimpleServer {
    public static void main(String args[]) throws IOException {
        // Register service on port 1589
        ServerSocket serverSocket = new ServerSocket(1589);
        System.out.println("Server started. Waiting for client...");

        Socket socket = serverSocket.accept(); // Wait and accept a connection
        System.out.println("Client connected.");

        // Streams for communication
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);

        String msgIn = "", msgOut = "";

        // Communication loop
        while (true) {
            msgIn = dis.readUTF();
            System.out.println("Client: " + msgIn);
            if (msgIn.equalsIgnoreCase("stop")) {
                System.out.println("Client stopped the chat.");
                break;
            }

            System.out.print("Server: ");
            msgOut = sc.nextLine();
            dos.writeUTF(msgOut);
            dos.flush();

            if (msgOut.equalsIgnoreCase("stop")) {
                System.out.println("Server stopped the chat.");
                break;
            }
        }

        // Cleanup
        dis.close();
        dos.close();
        socket.close();
        serverSocket.close();
        sc.close();
    }
}
