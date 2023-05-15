import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class CipherService {
    public static ArrayList<String> __INPUT_ARRAY_FOR_ENCRYPT__;
    public static byte[] __INPUT_ARRAY_FOR_DECRYPT__;

    public static void  __DES_CBC_ENCRYPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(String eightByteWord : __INPUT_ARRAY_FOR_ENCRYPT__){
            byte[] cipherBlock = DES.encryptCBC(eightByteWord.getBytes(StandardCharsets.ISO_8859_1),key,IV);
            fos.write(cipherBlock);
            IV = cipherBlock.clone();
        }
    }
    public static void  __DES_OFB_ENCRYPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(String eightByteWord : __INPUT_ARRAY_FOR_ENCRYPT__){
            byte[][] result = DES.encryptOFB(eightByteWord.getBytes(StandardCharsets.ISO_8859_1),key,IV);
            fos.write(result[0]);
            IV = result[1];
        }
    }
    public static void  __DES_CFB_ENCRYPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(String eightByteWord : __INPUT_ARRAY_FOR_ENCRYPT__){
            byte[] cipherBlock = DES.encryptCFB(eightByteWord.getBytes(StandardCharsets.ISO_8859_1),key,IV);
            fos.write(cipherBlock);
            IV = cipherBlock;
        }
    }
    public static void  __DES_CTR_ENCRYPT__(FileOutputStream fos, byte[] key, String nonce) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(String eightByteWord : __INPUT_ARRAY_FOR_ENCRYPT__){
            byte[] cipherBlock = DES.encryptCTR(eightByteWord.getBytes(StandardCharsets.ISO_8859_1),key,nonce);
            fos.write(cipherBlock);
        }
    }

    /*--------------------------DECRYPT-----------------------*/
    public static void  __DES_CBC_DECYRPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(int i=0;i<__INPUT_ARRAY_FOR_DECRYPT__.length;i+=8){
            byte[] temp = Arrays.copyOfRange(__INPUT_ARRAY_FOR_DECRYPT__,i,i+8);
            byte[][] result = DES.decryptCBC(temp,key,IV);
            IV = result[1];
            fos.write(result[0]);
        }
    }
    public static void  __DES_OFB_DECYRPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(int i=0;i<__INPUT_ARRAY_FOR_DECRYPT__.length;i+=8){
            byte[] temp = Arrays.copyOfRange(__INPUT_ARRAY_FOR_DECRYPT__,i,i+8);
            byte[][] result = DES.decryptOFB(temp,key,IV);
            IV = result[1];
            fos.write(result[0]);
        }
    }
    public static void  __DES_CFB_DECYRPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(int i=0;i<__INPUT_ARRAY_FOR_DECRYPT__.length;i+=8){
            byte[] temp = Arrays.copyOfRange(__INPUT_ARRAY_FOR_DECRYPT__,i,i+8);
            byte[][] result = DES.decryptCFB(temp,key,IV);
            fos.write(result[0]);
            IV = result[1];
        }
    }
    public static void  __DES_CTR_DECYRPT__(FileOutputStream fos, byte[] key, String nonce) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(int i=0;i<__INPUT_ARRAY_FOR_DECRYPT__.length;i+=8){
            byte[] temp = Arrays.copyOfRange(__INPUT_ARRAY_FOR_DECRYPT__,i,i+8);
            byte[] plainBlock = DES.decryptCTR(temp,key,nonce);
            fos.write(plainBlock);
        }
    }
    /*----------------------------------- Thriple DES ---------------------------------*/

    public static void  __ThripleDES_CBC_ENCRYPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(String eightByteWord : __INPUT_ARRAY_FOR_ENCRYPT__){
            byte[] cipherBlock = TripleDES.encryptCBC(eightByteWord.getBytes(StandardCharsets.ISO_8859_1),key,IV);
            fos.write(cipherBlock);
            IV = cipherBlock.clone();
        }
    }
    public static void  __ThripleDES_OFB_ENCRYPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(String eightByteWord : __INPUT_ARRAY_FOR_ENCRYPT__){
            byte[][] result = TripleDES.encryptOFB(eightByteWord.getBytes(StandardCharsets.ISO_8859_1),key,IV);
            fos.write(result[0]);
            IV = result[1];
        }
    }
    public static void  __ThripleDES_CFB_ENCRYPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(String eightByteWord : __INPUT_ARRAY_FOR_ENCRYPT__){
            byte[] cipherBlock = TripleDES.encryptCFB(eightByteWord.getBytes(StandardCharsets.ISO_8859_1),key,IV);
            fos.write(cipherBlock);
            IV = cipherBlock;
        }
    }
    public static void  __ThripleDES_CTR_ENCRYPT__(FileOutputStream fos, byte[] key, String nonce) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(String eightByteWord : __INPUT_ARRAY_FOR_ENCRYPT__){
            byte[] cipherBlock = TripleDES.encryptCTR(eightByteWord.getBytes(StandardCharsets.ISO_8859_1),key,nonce);
            fos.write(cipherBlock);
        }
    }

    /*--------------------------DECRYPT-----------------------*/
    public static void  __ThripleDES_CBC_DECYRPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(int i=0;i<__INPUT_ARRAY_FOR_DECRYPT__.length;i+=8){
            byte[] temp = Arrays.copyOfRange(__INPUT_ARRAY_FOR_DECRYPT__,i,i+8);
            byte[][] result = TripleDES.decryptCBC(temp,key,IV);
            IV = result[1];
            fos.write(result[0]);
        }
    }
    public static void  __ThripleDES_OFB_DECYRPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(int i=0;i<__INPUT_ARRAY_FOR_DECRYPT__.length;i+=8){
            byte[] temp = Arrays.copyOfRange(__INPUT_ARRAY_FOR_DECRYPT__,i,i+8);
            byte[][] result = TripleDES.decryptOFB(temp,key,IV);
            IV = result[1];
            fos.write(result[0]);
        }
    }
    public static void  __ThripleDES_CFB_DECYRPT__(FileOutputStream fos, byte[] key, byte[] IV) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(int i=0;i<__INPUT_ARRAY_FOR_DECRYPT__.length;i+=8){
            byte[] temp = Arrays.copyOfRange(__INPUT_ARRAY_FOR_DECRYPT__,i,i+8);
            byte[][] result = TripleDES.decryptCFB(temp,key,IV);
            fos.write(result[0]);
            IV = result[1];
        }
    }
    public static void  __ThripleDES_CTR_DECYRPT__(FileOutputStream fos, byte[] key, String nonce) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        for(int i=0;i<__INPUT_ARRAY_FOR_DECRYPT__.length;i+=8){
            byte[] temp = Arrays.copyOfRange(__INPUT_ARRAY_FOR_DECRYPT__,i,i+8);
            byte[] plainBlock = TripleDES.decryptCTR(temp,key,nonce);
            fos.write(plainBlock);
        }
    }
}
