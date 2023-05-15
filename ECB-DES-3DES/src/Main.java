import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.io.*;

class Main {

    public static void main(String[] args) throws Exception {
        String enc_or_dec = args[0];
        String input_file = args[2];
        String output_file = args[4];
        String algo = args[5];
        String algo_mode = args[6];
        String key_file = args[7];


        /* ------------------- Taking Input ----------------------*/

        ArrayList<String> input_array = IOservice.getInput(input_file);
        byte[] input_array2 = IOservice.readByBytes(input_file);
        CipherService.__INPUT_ARRAY_FOR_ENCRYPT__ = input_array;
        CipherService.__INPUT_ARRAY_FOR_DECRYPT__ = input_array2;

        /* ----------------------Taking Key Input -------------------*/

        ArrayList<String> keyArray = IOservice.getKey(key_file);

        byte[] byte_arr_key = keyArray.get(0).getBytes(StandardCharsets.ISO_8859_1);
        byte[] byte_arr_key_IV = keyArray.get(1).getBytes(StandardCharsets.ISO_8859_1);
        String key_nonce = keyArray.get(2);

        /* -----------------------------------------*/
        FileOutputStream fos = new FileOutputStream(output_file);


        Instant start = Instant.now();

        if(algo.equals("DES")){
            byte[] IV = Arrays.copyOfRange(byte_arr_key_IV, 0, 8);
            byte[] key = Arrays.copyOfRange(byte_arr_key, 0, 8);
            String nonce = key_nonce.substring(0,7);

            if (enc_or_dec.equals("-e")) {

                if (algo_mode.equals("CBC")) {
                    CipherService.__DES_CBC_ENCRYPT__(fos,key,IV);
                }
                if(algo_mode.equals("OFB")){
                    CipherService.__DES_OFB_ENCRYPT__(fos,key,IV);
                }
                if(algo_mode.equals("CTR")){
                    CipherService.__DES_CTR_ENCRYPT__(fos,key,nonce);
                }
                if(algo_mode.equals("CFB")){
                    CipherService.__DES_CFB_ENCRYPT__(fos,key,IV);
                }
            }
            if (enc_or_dec.equals("-d")) {

                if (algo_mode.equals("CBC")) {
                    CipherService.__DES_CBC_DECYRPT__(fos,key,IV);
                }
                if(algo_mode.equals("OFB")){
                    CipherService.__DES_OFB_DECYRPT__(fos,key,IV);
                }
                if(algo_mode.equals("CFB")){
                    CipherService.__DES_CFB_DECYRPT__(fos,key,IV);
                }
                if(algo_mode.equals("CTR")){
                    CipherService.__DES_CTR_DECYRPT__(fos,key,nonce);
                }
            }
        }

        if(algo.equals("3DES")){
            byte[] IV = Arrays.copyOfRange(byte_arr_key_IV, 0, 8);
            byte[] key = Arrays.copyOfRange(byte_arr_key, 0, 24);
            String nonce = key_nonce.substring(0,7);
            if (enc_or_dec.equals("-e")) {

                if (algo_mode.equals("CBC")) {
                    CipherService.__ThripleDES_CBC_ENCRYPT__(fos,key,IV);
                }
                if(algo_mode.equals("OFB")){
                    CipherService.__ThripleDES_OFB_ENCRYPT__(fos,key,IV);
                }
                if(algo_mode.equals("CFB")){
                    CipherService.__ThripleDES_CFB_ENCRYPT__(fos,key,IV);
                }
                if(algo_mode.equals("CTR")){
                    CipherService.__ThripleDES_CTR_ENCRYPT__(fos,key,nonce);
                }
            }
            if (enc_or_dec.equals("-d")) {

                if (algo_mode.equals("CBC")) {
                    CipherService.__ThripleDES_CBC_DECYRPT__(fos,key,IV);
                }
                if(algo_mode.equals("OFB")){
                    CipherService.__ThripleDES_OFB_DECYRPT__(fos,key,IV);
                }
                if(algo_mode.equals("CFB")){
                    CipherService.__ThripleDES_CFB_DECYRPT__(fos,key,IV);
                }
                if(algo_mode.equals("CTR")){
                    CipherService.__ThripleDES_CTR_DECYRPT__(fos,key,nonce);
                }
            }
        }
        fos.close();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        FileWriter __LOG__ = new FileWriter("run.log", true);
        __LOG__.write(input_file + " "+ output_file);
        __LOG__.write(enc_or_dec.equals("e") ? " enc " : " dec ");
        __LOG__.write(algo +" "+algo_mode +" "+timeElapsed.toMillis());
        __LOG__.write("\n");
        __LOG__.close();
    }
}