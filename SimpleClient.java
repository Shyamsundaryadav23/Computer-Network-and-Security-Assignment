// SimpleClient.java : A simple client program with continuous communication.
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class SimpleClient {
    public static void main(String args[]) throws IOException {
        // Open connection to server at port 1589
        Socket socket = new Socket("localhost", 1589);
        System.out.println("Connected to server.");

        // Streams for communication
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);

        String msgIn = "", msgOut = "";

        // Communication loop
        while (true) {
            System.out.print("Client: ");
            msgOut = sc.nextLine();
            dos.writeUTF(msgOut);
            dos.flush();

            if (msgOut.equalsIgnoreCase("stop")) {
                System.out.println("Client stopped the chat.");
                break;
            }

            msgIn = dis.readUTF();
            System.out.println("Server: " + msgIn);

            if (msgIn.equalsIgnoreCase("stop")) {
                System.out.println("Server stopped the chat.");
                break;
            }
        }

        // Cleanup
        dis.close();
        dos.close();
        socket.close();
        sc.close();
    }
}
