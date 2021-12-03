package com.company.credentials;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class VerifyPassword {
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

    private static byte[] toBytes(String string){
        byte[] bytes = new byte[string.length()/2];
        for(int i=0;i<bytes.length;i++){
            bytes[i] = (byte)Integer.parseInt(string.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }

    public static boolean match(String password, String encryptedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return comparePasswords(password, encryptedPassword);
    }
}
