// Java Program: Menu-Driven Implementation of RSA Algorithm
import java.math.*;
import java.util.*;

public class RSA {

    static int p, q, n, z, d = 0, e;
    static BigInteger N, C;
    static boolean keysGenerated = false;
    static Scanner sc = new Scanner(System.in);

    // Function to compute GCD
    static int gcd(int a, int b) {
        if (a == 0)
            return b;
        else
            return gcd(b % a, a);
    }

    // Function to generate keys
    static void generateKeys() {
        System.out.print("Enter first prime number (p): ");
        p = sc.nextInt();
        System.out.print("Enter second prime number (q): ");
        q = sc.nextInt();

        n = p * q;
        z = (p - 1) * (q - 1);

        for (e = 2; e < z; e++) {
            if (gcd(e, z) == 1)
                break;
        }

        for (int i = 0; i <= 9; i++) {
            int x = 1 + (i * z);
            if (x % e == 0) {
                d = x / e;
                break;
            }
        }

        N = BigInteger.valueOf(n);
        keysGenerated = true;

        System.out.println("\n=== RSA Keys Generated ===");
        System.out.println("p = " + p + ", q = " + q);
        System.out.println("n = " + n);
        System.out.println("z = " + z);
        System.out.println("Public Key (e, n): (" + e + ", " + n + ")");
        System.out.println("Private Key (d, n): (" + d + ", " + n + ")");
    }

    // Function to encrypt message
    static BigInteger encrypt(int msg) {
        if (!keysGenerated) {
            System.out.println("Generate keys first!");
            return BigInteger.ZERO;
        }
        double c = (Math.pow(msg, e)) % n;
        C = BigDecimal.valueOf(c).toBigInteger();
        System.out.println("Encrypted message (C): " + C);
        return C;
    }

    // Function to decrypt message
    static BigInteger decrypt(BigInteger C) {
        if (!keysGenerated) {
            System.out.println("Generate keys first!");
            return BigInteger.ZERO;
        }
        BigInteger msgback = (C.pow(d)).mod(N);
        System.out.println("Decrypted message: " + msgback);
        return msgback;
    }

    // Main menu
    public static void main(String[] args) {
        int choice;
        BigInteger cipher = BigInteger.ZERO;

        while (true) {
            System.out.println("\n===== RSA ALGORITHM MENU =====");
            System.out.println("1. Generate Keys");
            System.out.println("2. Encrypt Message");
            System.out.println("3. Decrypt Message");
            System.out.println("4. Display Public & Private Keys");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    generateKeys();
                    break;

                case 2:
                    if (!keysGenerated) {
                        System.out.println("Please generate keys first!");
                        break;
                    }
                    System.out.print("Enter message (integer): ");
                    int msg = sc.nextInt();
                    cipher = encrypt(msg);
                    break;

                case 3:
                    if (!keysGenerated) {
                        System.out.println("Please generate keys first!");
                        break;
                    }
                    if (cipher.equals(BigInteger.ZERO)) {
                        System.out.println("No message has been encrypted yet!");
                        break;
                    }
                    decrypt(cipher);
                    break;

                case 4:
                    if (!keysGenerated) {
                        System.out.println("Keys not generated yet!");
                        break;
                    }
                    System.out.println("\nPublic Key (e, n): (" + e + ", " + n + ")");
                    System.out.println("Private Key (d, n): (" + d + ", " + n + ")");
                    break;

                case 5:
                    System.out.println("Exiting... Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
