package org.banking.models;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


public class Hashing {

    private static final byte[] salt = Base64.getDecoder().decode("i+JPQ0iR9I29iuTc/45jvA==");

    public static void main(String[] args) {

        String password = "xeL$V3ji#";
        String hashedPassword = "vBh7A2zRNulKGSdhb9mwahHIbgzjWcMEPv3fNOcwqPc=";

        System.out.println("Password: " + password);
        System.out.println("Salt (Base64): " + Base64.getEncoder().encodeToString(salt));
        System.out.println("Hashed Password (Base64): " + hashedPassword);

        System.out.println(verifyPassword(hashedPassword, password));

    }

    public static String hashPassword(String password) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyPassword(String storedHash, String password) {
        return hashPassword(password).equals(storedHash);
    }
}
