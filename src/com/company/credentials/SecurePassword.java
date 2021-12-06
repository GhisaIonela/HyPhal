package com.company.credentials;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Class responsible with passwords security
 */
public abstract class SecurePassword {

    /**
     * Generate a hash for password
     * @param password - the password
     * @return - a string representation for hased password with the following form
     *           number of iterations:salt:hash
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars_psw = password.toCharArray();
        byte[] salt = generateSalt();
        PBEKeySpec pbeKey_spec = new PBEKeySpec(chars_psw, salt, iterations, 64*8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(pbeKey_spec).getEncoded();
        return iterations + ":" + to_string(salt) + ":" + to_string(hash);
    }

    /**
     * Generate a salt
     * @return - the salt
     * @throws NoSuchAlgorithmException
     */
    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    /**
     * Get a string representation from a byte array
     * @param array - the byte array
     * @return - the string representation
     */
    private static String to_string(byte[] array){
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * Generate an encrypted version for a password
     * @param password - the password to encrypt
     * @return - the encrypted password
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String generateEncryptedPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generatePasswordHash(password);
    }
}
