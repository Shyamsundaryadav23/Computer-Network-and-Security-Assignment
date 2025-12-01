import java.util.*;

class PlayfairCipher {

    // ---------- Utility Functions ----------

    // Convert to lowercase
    static void toLowerCase(StringBuilder plain) {
        for (int i = 0; i < plain.length(); i++) {
            if (plain.charAt(i) >= 'A' && plain.charAt(i) <= 'Z') {
                plain.setCharAt(i, (char) (plain.charAt(i) + 32));
            }
        }
    }

    // Remove spaces
    static void removeSpaces(StringBuilder plain) {
        for (int i = 0; i < plain.length(); i++) {
            if (plain.charAt(i) == ' ') {
                plain.deleteCharAt(i);
                i--;
            }
        }
    }

    // Generate 5x5 key table
    static void generateKeyTable(StringBuilder key, char[][] keyT) {
        boolean[] present = new boolean[26];
        toLowerCase(key);
        removeSpaces(key);

        // Treat 'j' as 'i'
        present['j' - 'a'] = true;

        int row = 0, col = 0;

        // Add key characters first
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!present[ch - 'a']) {
                keyT[row][col++] = ch;
                present[ch - 'a'] = true;
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }
        }

        // Fill remaining letters
        for (char ch = 'a'; ch <= 'z'; ch++) {
            if (!present[ch - 'a']) {
                keyT[row][col++] = ch;
                present[ch - 'a'] = true;
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    // Search for letters in the key table
    static void search(char[][] keyT, char a, char b, int[] pos) {
        if (a == 'j') a = 'i';
        if (b == 'j') b = 'i';

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyT[i][j] == a) {
                    pos[0] = i;
                    pos[1] = j;
                }
                if (keyT[i][j] == b) {
                    pos[2] = i;
                    pos[3] = j;
                }
            }
        }
    }

    // Prepare plaintext (insert 'x' between duplicates, pad with 'z' if needed)
    static void prepare(StringBuilder str) {
        toLowerCase(str);
        removeSpaces(str);

        for (int i = 0; i < str.length() - 1; i += 2) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                str.insert(i + 1, 'x');
            }
        }

        if (str.length() % 2 != 0) {
            str.append('z');
        }
    }

    // Encrypt the plaintext
    static void encrypt(StringBuilder str, char[][] keyT) {
        int[] pos = new int[4];
        for (int i = 0; i < str.length(); i += 2) {
            search(keyT, str.charAt(i), str.charAt(i + 1), pos);

            // Same row
            if (pos[0] == pos[2]) {
                str.setCharAt(i, keyT[pos[0]][(pos[1] + 1) % 5]);
                str.setCharAt(i + 1, keyT[pos[0]][(pos[3] + 1) % 5]);
            }
            // Same column
            else if (pos[1] == pos[3]) {
                str.setCharAt(i, keyT[(pos[0] + 1) % 5][pos[1]]);
                str.setCharAt(i + 1, keyT[(pos[2] + 1) % 5][pos[1]]);
            }
            // Rectangle swap
            else {
                str.setCharAt(i, keyT[pos[0]][pos[3]]);
                str.setCharAt(i + 1, keyT[pos[2]][pos[1]]);
            }
        }
    }

    // Decrypt the ciphertext
    static void decrypt(StringBuilder str, char[][] keyT) {
        int[] pos = new int[4];
        for (int i = 0; i < str.length(); i += 2) {
            search(keyT, str.charAt(i), str.charAt(i + 1), pos);

            // Same row
            if (pos[0] == pos[2]) {
                str.setCharAt(i, keyT[pos[0]][(pos[1] - 1 + 5) % 5]);
                str.setCharAt(i + 1, keyT[pos[0]][(pos[3] - 1 + 5) % 5]);
            }
            // Same column
            else if (pos[1] == pos[3]) {
                str.setCharAt(i, keyT[(pos[0] - 1 + 5) % 5][pos[1]]);
                str.setCharAt(i + 1, keyT[(pos[2] - 1 + 5) % 5][pos[1]]);
            }
            // Rectangle swap
            else {
                str.setCharAt(i, keyT[pos[0]][pos[3]]);
                str.setCharAt(i + 1, keyT[pos[2]][pos[1]]);
            }
        }
    }

    // Wrapper to perform encryption
    public static StringBuilder encryptByPlayfairCipher(StringBuilder str, StringBuilder key) {
        char[][] keyT = new char[5][5];
        generateKeyTable(key, keyT);
        prepare(str);
        encrypt(str, keyT);
        return str;
    }

    // Wrapper to perform decryption
    public static StringBuilder decryptByPlayfairCipher(StringBuilder str, StringBuilder key) {
        char[][] keyT = new char[5][5];
        generateKeyTable(key, keyT);
        decrypt(str, keyT);
        return str;
    }

    // Print the key table (optional helper)
    static void printKeyTable(char[][] keyT) {
        System.out.println("\nGenerated 5x5 Key Table:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(keyT[i][j] + " ");
            }
            System.out.println();
        }
    }
     public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder key = new StringBuilder();
        char[][] keyTable = new char[5][5];
        boolean keySet = false;

        while (true) {
            System.out.println("\n===== PLAYFAIR CIPHER MENU =====");
            System.out.println("1. Enter Key");
            System.out.println("2. Encrypt Message");
            System.out.println("3. Decrypt Message");
            System.out.println("4. Show Key Table");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter key: ");
                    key = new StringBuilder(sc.nextLine());
                    generateKeyTable(key, keyTable);
                    keySet = true;
                    System.out.println("Key table generated successfully!");
                    break;

                case 2:
                    if (!keySet) {
                        System.out.println("Please enter a key first!");
                        break;
                    }
                    System.out.print("Enter plaintext: ");
                    StringBuilder plain = new StringBuilder(sc.nextLine());
                    StringBuilder cipher = encryptByPlayfairCipher(new StringBuilder(plain), key);
                    System.out.println("Encrypted Text: " + cipher);
                    break;

                case 3:
                    if (!keySet) {
                        System.out.println("Please enter a key first!");
                        break;
                    }
                    System.out.print("Enter ciphertext: ");
                    StringBuilder cipherText = new StringBuilder(sc.nextLine());
                    StringBuilder decrypted = decryptByPlayfairCipher(new StringBuilder(cipherText), key);
                    System.out.println("Decrypted Text: " + decrypted);
                    break;

                case 4:
                    if (!keySet) {
                        System.out.println("Please enter a key first!");
                        break;
                    }
                    printKeyTable(keyTable);
                    break;

                case 5:
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}

