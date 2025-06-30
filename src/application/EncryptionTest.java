/**
 * <p> EncryptionTest Class </p>
 *
 * <p> Description: This class contains unit tests for testing the functionality of the 
 * EncryptionManager class, specifically the encryption and decryption methods. The tests 
 * ensure that encryption and decryption work correctly with valid inputs, handle errors 
 * for invalid Base64 strings, and properly address incorrect decryption keys. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of the test suite for the EncryptionManager class.
 */


package application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EncryptionTest {

    // Test encryption and decryption
    @Test
    void testEncryptDecrypt() {
        // Arrange
        String originalText = "Hello, Custom Encryption!";
        String key = EncryptionManager.getKey();

        // Encrypt the original text
        String encryptedText = EncryptionManager.encrypt(originalText, key);

        // Assert: Ensure that the encrypted text is not the same as the original
        assertNotEquals(originalText, encryptedText, "Encrypted text should not be the same as the original");

        //Decrypt the encrypted text
        String decryptedText = EncryptionManager.decrypt(encryptedText, key);

        //Ensure that the decrypted text matches the original
        assertEquals(originalText, decryptedText, "Decrypted text should match the original text");
    }

    // Test decryption with an invalid Base64 string
    @Test
    void testDecryptWithInvalidBase64() {
        // Arrange
        String invalidBase64 = "InvalidBase64==";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            EncryptionManager.decrypt(invalidBase64, EncryptionManager.getKey());
        });

        // Check the exception message
        assertEquals("Invalid Base64 string provided for decryption.", thrown.getMessage());
    }

    // Test decryption with incorrect key
    @Test
    void testDecryptWithIncorrectKey() {
        // Arrange
        String originalText = "Hello, Custom Encryption!";
        String key = EncryptionManager.getKey();
        String incorrectKey = "WrongKey456";

        // Encrypt the original text with the correct key
        String encryptedText = EncryptionManager.encrypt(originalText, key);

        // Try to decrypt with an incorrect key
        String decryptedText = EncryptionManager.decrypt(encryptedText, incorrectKey);

        // Ensure that the decrypted text with the incorrect key is not the same as the original
        assertNotEquals(originalText, decryptedText, "Decrypted text with the wrong key should not match the original");
    }
}
