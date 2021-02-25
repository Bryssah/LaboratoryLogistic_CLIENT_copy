/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ResourceBundle;
import javax.crypto.Cipher;

/**
 * This class will take care of cryp and decryp the users passwords using RSA mode.
 *
 * @author Garikoitz
 */
public class CryptographyClient {

    private static final String properties ="lazydevelopers.config.clientProperties";
    private static final String path = ResourceBundle.getBundle(properties).getString("path");
    
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
    public static byte[] cifrarTexto(String password) {
        byte[] encodedMessage = null;

        try {
            byte fileKey[] = getPublicFileKey();

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

    }
    
    /**
     * 
     * @param resumen
     * @return 
     */
    public static String toHexadecimal(byte[] resumen) {
        String HEX = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1) {
                HEX += "0";
            }
            HEX += h;
        }
        return HEX.toUpperCase();
    }
    
    /**
     * 
     * @param s
     * @return 
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
  
    /**
     * 
     * @return
     * @throws IOException 
     */
    public static byte[] getPublicFileKey() throws IOException {

        InputStream keyfis = CryptographyClient.class.getClassLoader()
                .getResourceAsStream(path);
       
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        // read bytes from the input stream and store them in buffer
        while ((len = keyfis.read(buffer)) != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len);
        }
        keyfis.close();
        return os.toByteArray();
    }

}
