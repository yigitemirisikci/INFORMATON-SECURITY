import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TripleDES {
    public static int __COUNTER__ = 0;

    private static byte[] xor_op(byte[] input1, byte[] input2) {
        byte[] rArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            rArr[i] = (byte) (input1[i] ^ input2[i]);
        }
        return rArr;
    }

    private static byte[] xor(final byte[] input, final byte[] secret) {
        // 8 bit 1 bytes 64 bit 8 bytes
        final byte[] output = new byte[input.length];
        if (secret.length == 0) {
            throw new IllegalArgumentException("empty security key");
        }
        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            ++spos;
            if (spos >= secret.length) {
                spos = 0;
            }
        }
        return output;
    }

    public static byte[] encryptCBC(byte[] plain, byte[] key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] xor_result = xor_op(plain, IV);

        SecretKeySpec secret_key = new SecretKeySpec(key, "TripleDES");
        Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        return cipher.doFinal(xor_result);
    }

    public static byte[][] decryptCBC(byte[] cipherText, byte[] key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secret_key = new SecretKeySpec(key, "TripleDES");
        Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secret_key);

        byte[] will_be_XORed = cipher.doFinal(cipherText);
        byte[][] rArr = {xor_op(will_be_XORed, IV), cipherText};
        return rArr;
    }

    public static byte[][] encryptOFB(byte[] plain, byte[] key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secret_key = new SecretKeySpec(key, "TripleDES");
        Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        byte[] will_be_XORed = cipher.doFinal(IV);
        byte[] cipherBlock = xor_op(plain, will_be_XORed);
        byte[][] result = {cipherBlock, will_be_XORed};
        return result;
    }

    public static byte[][] decryptOFB(byte[] cipherText, byte[] key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secret_key = new SecretKeySpec(key, "TripleDES");
        Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        byte[] will_be_XORed = cipher.doFinal(IV);
        byte[] plainBlock = xor_op(will_be_XORed, cipherText);
        byte[][] result = {plainBlock, will_be_XORed};
        return result;
    }

    public static byte[] encryptCFB(byte[] plainBlock, byte[] key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secret_key = new SecretKeySpec(key, "TripleDES");
        Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        byte[] will_be_XORed = cipher.doFinal(IV);
        byte[] cipherBlock = xor_op(will_be_XORed,plainBlock);

        return cipherBlock;
    }

    public static byte[][] decryptCFB(byte[] cipherBlock, byte[] key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secret_key = new SecretKeySpec(key, "TripleDES");
        Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        byte[] will_be_XORed = cipher.doFinal(IV);
        byte[] plainBlock = xor_op(will_be_XORed,cipherBlock);
        byte[][] result= {plainBlock,cipherBlock};
        return result;
    }

    public static byte[] encryptCTR(byte[] plain, byte[] key, String nonce) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        if (__COUNTER__ >= 10) {
            __COUNTER__ -= 10;
        }

        String nonce_counter = nonce + __COUNTER__;
        __COUNTER__++;

        SecretKeySpec secret_key = new SecretKeySpec(key, "TripleDES");
        Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        byte[] will_be_XORed = cipher.doFinal(nonce_counter.getBytes(StandardCharsets.ISO_8859_1));
        byte[] cipherBlock = xor(plain, will_be_XORed);
        return cipherBlock;
    }


    public static byte[] decryptCTR(byte[] cipherBlock, byte[] key, String nonce) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        if (__COUNTER__ >= 10) {
            __COUNTER__ -= 10;
        }
        String nonce_counter = nonce + __COUNTER__;
        __COUNTER__++;

        SecretKeySpec secret_key = new SecretKeySpec(key, "TripleDES");
        Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        byte[] will_be_XORed = cipher.doFinal(nonce_counter.getBytes(StandardCharsets.ISO_8859_1));
        byte[] plainBlock = xor(cipherBlock, will_be_XORed);
        return plainBlock;
    }

}

