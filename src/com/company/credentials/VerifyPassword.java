package com.company.credentials;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * VerifyPassword checks if passwords match
 */
public abstract class VerifyPassword {
    /**
     * Compare two paswwords(one encrypted and the other not) and tell if they match
     * @param password - the non encrypted password
     * @param encryptedPassword - the encrypted password
     * @return true if they match, false otherwise
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static boolean comparePasswords(String password, String encryptedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] tokens = encryptedPassword.split(":");
        int iterations = Integer.parseInt(tokens[0]);
        byte[] salt = toBytes(tokens[1]);
        byte[] hash = toBytes(tokens[2]);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length*8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();

        int diff = hash.length ^ testHash.length;  // ^ represents binary xor operator
        for(int i=0; i<hash.length && i<testHash.length; i++){
            diff |= hash[i] ^ testHash[i];    // | represents bit-wise or operator
        }
        return diff==0;
    }

    /**
     * Transform a string representation into bytes representation
     * @param string
     * @return
     */
    private static byte[] toBytes(String string){
        byte[] bytes = new byte[string.length()/2];
        for(int i=0;i<bytes.length;i++){
            bytes[i] = (byte)Integer.parseInt(string.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }

    /**
     *Tells if two passwords match
     * @param password - the non encrypted password
     * @param encryptedPassword - the encrypted password
     * @return true if they match, false otherwise
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean match(String password, String encryptedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return comparePasswords(password, encryptedPassword);
    }
}
