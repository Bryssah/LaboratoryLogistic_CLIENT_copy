/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.security;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/**
 * This class will take care of cryp and decryp the users passwords using RSA mode.
 *
 * @author Garikoitz
 */
public class Cryptography {

    /**
     * This method hash the password for save in de database.
     *
     * @param password without hash.
     * @return the hashed password.
     */
    public static String getHashedPassword(String password) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-256");

            byte[] bytes = md.digest(password.getBytes());

            password = bytes.toString();

            return password;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        } finally {
            return password;

        }
    }

    /**
     * Cryp the password.
     *
     * @param password the password to cryp.
     * @return the crypted password.
     */
    /*public static byte[] cifrarTexto(String password) {
        byte[] encodedMessage = null;

        try {
            byte fileKey[] = fileReader("lazydevelopers/security/keys/LaboratoryLogisticP.key");

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(fileKey);

            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            encodedMessage = cipher.doFinal(password.getBytes());

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return encodedMessage;

        }

    }*/

    /**
     * Decryp the password.
     *
     * @param password encrypted password.
     * @return decrypted password.
     */
    public static byte[] descifrarTexto(byte[] password) {
        byte[] decodedMessage = null;

        try {
           
            byte fileKey[] = getPrivateFileKey();
            System.out.println("cogiendo fich" + fileKey.toString());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PKCS8EncodedKeySpec KeySpec = new PKCS8EncodedKeySpec(fileKey);

            PrivateKey key = keyFactory.generatePrivate(KeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            cipher.init(Cipher.DECRYPT_MODE, key);

            decodedMessage = cipher.doFinal(password);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return decodedMessage;

        }

    }
    
    /*public static byte[] hexStringToByteArray(String s){
        Integer len = s.length();
        byte data[] = new byte[len/2];
        for(int i = 0; i<len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) 
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }*/
    
    public static byte[] getPrivateFileKey() throws IOException {
        byte[] publicKeyBytes;
        try (InputStream inputStream = Cryptography.class.getClassLoader()
                .getResourceAsStream("lazydevelopers/security/keys/LaboratoryLogisticPrivate.key")) {
            publicKeyBytes = new byte[inputStream.available()];
            inputStream.read(publicKeyBytes);
        }
        return publicKeyBytes;
    }

}
