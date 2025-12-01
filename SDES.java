import java.util.*;

public class SDES {

    // --- DES Tables (same as before, shortened comments for brevity) ---
    private static final int[] IP = {58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,
            62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,
            57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,
            61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};

    private static final int[] E = {32,1,2,3,4,5,4,5,6,7,8,9,
            8,9,10,11,12,13,12,13,14,15,16,17,
            16,17,18,19,20,21,20,21,22,23,24,25,
            24,25,26,27,28,29,28,29,30,31,32,1};

    private static final int[] P = {16,7,20,21,29,12,28,17,
            1,15,23,26,5,18,31,10,
            2,8,24,14,32,27,3,9,
            19,13,30,6,22,11,4,25};

    private static final int[] FP = {40,8,48,16,56,24,64,32,
            39,7,47,15,55,23,63,31,
            38,6,46,14,54,22,62,30,
            37,5,45,13,53,21,61,29,
            36,4,44,12,52,20,60,28,
            35,3,43,11,51,19,59,27,
            34,2,42,10,50,18,58,26,
            33,1,41,9,49,17,57,25};

    private static final int[][][] S_BOXES = {
        {{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
         {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
         {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
         {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}},
        {{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
         {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
         {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
         {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}},
        {{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
         {13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
         {13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
         {1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}},
        {{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
         {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
         {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
         {3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}},
        {{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
         {14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
         {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
         {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}},
        {{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
         {10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
         {9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
         {4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}},
        {{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
         {13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
         {1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
         {6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}},
        {{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
         {1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
         {7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
         {2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}}
    };

    private static final int[] PC1 = {57,49,41,33,25,17,9,
            1,58,50,42,34,26,18,
            10,2,59,51,43,35,27,
            19,11,3,60,52,44,36,
            63,55,47,39,31,23,15,
            7,62,54,46,38,30,22,
            14,6,61,53,45,37,29,
            21,13,5,28,20,12,4};

    private static final int[] PC2 = {14,17,11,24,1,5,
            3,28,15,6,21,10,
            23,19,12,4,26,8,
            16,7,27,20,13,2,
            41,52,31,37,47,55,
            30,40,51,45,33,48,
            44,49,39,56,34,53,
            46,42,50,36,29,32};

    private static final int[] SHIFTS = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

    // ---------- Helper Functions ----------

    private static long permutation(long block, int[] table) {
        long result = 0;
        for (int i = 0; i < table.length; i++) {
            result <<= 1;
            result |= (block >> (64 - table[i])) & 0x01;
        }
        return result;
    }

    private static long initialPermutation(long block) {
        return permutation(block, IP);
    }

    private static long finalPermutation(long block) {
        return permutation(block, FP);
    }

    private static long expansion(long halfBlock) {
        return permutation(halfBlock, E);
    }

    private static int permuteP(int block) {
        return (int) permutation(block & 0xFFFFFFFFL, P);
    }

    private static long[] generateSubkeys(long key) {
        long[] subkeys = new long[16];
        key = permutation(key, PC1);
        int C = (int) ((key >> 28) & 0x0FFFFFFF);
        int D = (int) (key & 0x0FFFFFFF);

        for (int round = 0; round < 16; round++) {
            C = ((C << SHIFTS[round]) | (C >> (28 - SHIFTS[round]))) & 0x0FFFFFFF;
            D = ((D << SHIFTS[round]) | (D >> (28 - SHIFTS[round]))) & 0x0FFFFFFF;
            long CD = ((long) C << 28) | D;
            subkeys[round] = permutation(CD, PC2);
        }
        return subkeys;
    }

    private static int sBoxSubstitution(long block) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            int sBoxInput = (int) ((block >> (42 - 6 * i)) & 0x3F);
            int row = ((sBoxInput & 0x20) >> 4) | (sBoxInput & 0x01);
            int col = (sBoxInput >> 1) & 0x0F;
            result = (result << 4) | S_BOXES[i][row][col];
        }
        return result;
    }

    private static long desFunction(long block, long[] subkeys, boolean isEncrypt) {
        block = initialPermutation(block);
        int L = (int) ((block >> 32) & 0xFFFFFFFF);
        int R = (int) (block & 0xFFFFFFFF);

        for (int round = 0; round < 16; round++) {
            int tempR = R;
            long expandedR = expansion(R);
            expandedR ^= isEncrypt ? subkeys[round] : subkeys[15 - round];
            int sOut = sBoxSubstitution(expandedR);
            int pOut = permuteP(sOut);
            R = L ^ pOut;
            L = tempR;
        }

        long preOutput = ((long) R << 32) | (L & 0xFFFFFFFFL);
        return finalPermutation(preOutput);
    }

    public static long encrypt(long plaintext, long key) {
        return desFunction(plaintext, generateSubkeys(key), true);
    }

    public static long decrypt(long ciphertext, long key) {
        return desFunction(ciphertext, generateSubkeys(key), false);
    }

    // ---------- Menu-Driven Main ----------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long plaintext = 0, key = 0, ciphertext = 0;
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== DES Encryption/Decryption Menu =====");
            System.out.println("1. Enter Plaintext and Key");
            System.out.println("2. Encrypt");
            System.out.println("3. Decrypt");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter 64-bit plaintext (in hex, e.g., 0123456789ABCDEF): 0x");
                    plaintext = Long.parseUnsignedLong(sc.next(), 16);
                    System.out.print("Enter 64-bit key (in hex, e.g., 133457799BBCDFF1): 0x");
                    key = Long.parseUnsignedLong(sc.next(), 16);
                    System.out.println("Plaintext and key stored successfully!");
                    break;

                case 2:
                    ciphertext = encrypt(plaintext, key);
                    System.out.printf("Ciphertext: 0x%016X\n", ciphertext);
                    break;

                case 3:
                    long decrypted = decrypt(ciphertext, key);
                    System.out.printf("Decrypted Text: 0x%016X\n", decrypted);
                    if (decrypted == plaintext)
                        System.out.println("Decryption successful!");
                    else
                        System.out.println("Decryption failed!");
                    break;

                case 4:
                    exit = true;
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }

        sc.close();
    }
}
