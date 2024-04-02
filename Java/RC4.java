import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RC4 {
    private byte[] S;  // The array used for the permutation
    private int i;     // The index used for permutation
    private int j;     // The index used for permutation

    // Constructor to initialize RC4 with the given key
    public RC4(byte[] key) {
        initialize(key);
    }

    // Initialize the RC4 cipher with the given key
    private void initialize(byte[] key) {
        S = new byte[256];      // Initialize the permutation array
        byte[] T = new byte[256];  // Temporary array for key scheduling
        int keyLength = key.length;

        // Initialize permutation array S and T with key
        for (int i = 0; i < 256; i++) {
            S[i] = (byte) i;       // Fill S with values 0 to 255
            T[i] = key[i % keyLength];  // Repeat key if shorter than 256 bytes
        }

        int j = 0;
        // Permute the array S based on the key
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + T[i]) & 0xFF;  // Update index j based on S and T
            swap(S, i, j);  // Swap values at indices i and j
        }

        // Reset indices i and j for encryption/decryption
        this.i = 0;
        this.j = 0;
    }

    // Method to encrypt plaintext using RC4 algorithm
    public byte[] encrypt(byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        for (int k = 0; k < plaintext.length; k++) {
            i = (i + 1) & 0xFF;   // Update index i
            j = (j + S[i]) & 0xFF;  // Update index j based on S
            swap(S, i, j);   // Swap values at indices i and j
            int t = (S[i] + S[j]) & 0xFF;  // Calculate permutation value
            ciphertext[k] = (byte) (plaintext[k] ^ S[t]);  // XOR plaintext with permutation value
        }
        return ciphertext;
    }

    // Method to decrypt ciphertext (same as encrypting with RC4)
    public byte[] decrypt(byte[] ciphertext) {
        return encrypt(ciphertext); // RC4 decryption is the same as encryption
    }

    // Helper method to swap values in array
    private void swap(byte[] array, int i, int j) {
        byte temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Main method for user interaction
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter 'e' or 'E' to encrypt, 'd' or 'D' to decrypt: ");
        String choice = scanner.nextLine().toLowerCase(); // Convert input to lowercase

        if (choice.equals("e")) {  // Encryption process
            System.out.print("Enter a plaintext: ");
            String plaintext = scanner.nextLine();
            System.out.print("Enter a key: ");
            String key = scanner.nextLine();

            RC4 rc4 = new RC4(key.getBytes(StandardCharsets.UTF_8));  // Initialize RC4 with key
            byte[] encrypted = rc4.encrypt(plaintext.getBytes(StandardCharsets.UTF_8));  // Encrypt plaintext

            // Print plaintext, encrypted message, and ciphertext
            System.out.println("Plaintext: " + plaintext);
            System.out.println("Encrypted Message: " + new String(encrypted, StandardCharsets.UTF_8));
            System.out.print("Cipher text: ");
            for (byte b : encrypted) {
                System.out.print(String.format("%02X", b));  // Print ciphertext in hexadecimal format
            }
        } else if (choice.equals("d")) {  // Decryption process
            System.out.print("Enter a ciphertext: ");
            String ciphertextHex = scanner.nextLine();
            System.out.print("Enter a key: ");
            String key = scanner.nextLine();

            RC4 rc4 = new RC4(key.getBytes(StandardCharsets.UTF_8));  // Initialize RC4 with key
            byte[] decrypted = rc4.decrypt(hexStringToByteArray(ciphertextHex));  // Decrypt ciphertext

            // Print decrypted message
            System.out.println("Decrypted Message: " + new String(decrypted, StandardCharsets.UTF_8));
        } else {
            System.out.println("Invalid choice.");
        }

        scanner.close();
    }

    // Helper method to convert hexadecimal string to byte array
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
