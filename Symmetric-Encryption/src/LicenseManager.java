import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class LicenseManager {
    public PublicKey publicKey;
    private PrivateKey privateKey;
    public byte[] encryptedData, hashedData, signature;
    public String decryptedData;

    LicenseManager() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        getPublicKey();
        getPrivateKey();
    }
    public String decryptMessage(byte[] cipherText) {
        String plain = "";
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] result = decryptCipher.doFinal(cipherText);
            plain = new String(result, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plain;
    }

    public byte[] sign(byte[] hashedData) {
        byte[] signedData = null;
        try {
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(privateKey);
            privateSignature.update(hashedData);
            signedData = privateSignature.sign();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return signedData;
    }
    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public void setHashedData(byte[] hashedData) {
        this.hashedData = hashedData;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public void setDecryptedData(String decryptedData) {
        this.decryptedData = decryptedData;
    }

    public void getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get("keys/public.key"));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey key = kf.generatePublic(spec);
        publicKey = key;
    }

    public void getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get("keys/private.key"));

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey key = kf.generatePrivate(spec);

        privateKey = key;

    }

    public void send_to_Client(byte[] signature, Client client){
        client.setSignature(signature);
    }

    public  byte[] hash(String data) {
        byte[] hashedValue = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF8"));
            hashedValue = messageDigest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashedValue;
    }
}
