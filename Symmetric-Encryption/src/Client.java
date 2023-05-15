import javax.crypto.Cipher;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Client {

    public String userName;
    public String serialNumber;
    public String macAdress;
    public String diskSerialNumber;
    public String motherBoardSerialNumber;
    public PublicKey publicKey;
    public byte[] encryptedData, hashedData, signature;

    public String full_content;

    Client(){
        userName = "Yigit";
        serialNumber = "1234-5678-9012";
        getMacAdress();
        getSerialNumbers();
        getPublicKey();
        full_content = userName + "$" + serialNumber + "$" + macAdress + "$" + diskSerialNumber + "$" + motherBoardSerialNumber;

    }
    public void getMacAdress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareAddress = ni.getHardwareAddress();
            String[] hexadecimal = new String[hardwareAddress.length];
            for (int i = 0; i < hardwareAddress.length; i++) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }
            macAdress = String.join("-", hexadecimal);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getSerialNumbers() {
        Runtime runtime = Runtime.getRuntime();

        String[] getDiskSN = {"wmic", "diskdrive", "get", "serialnumber"};
        String[] getMotherBoardSN = {"wmic", "baseboard", "get", "serialnumber"};
        try{
            Process process = runtime.exec(getDiskSN);
            String chain = null;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String serialNumber = null;
                while ((serialNumber = bufferedReader.readLine()) != null) {
                    if (serialNumber.trim().length() > 0) {
                        chain = serialNumber;
                    }
                }
                diskSerialNumber = chain.trim();
            }

            process = runtime.exec(getMotherBoardSN);
            chain = null;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String serialNumber = null;
                while ((serialNumber = bufferedReader.readLine()) != null) {
                    if (serialNumber.trim().length() > 0) {
                        chain = serialNumber;
                    }
                }
                motherBoardSerialNumber = chain.trim();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getPublicKey() {
        PublicKey key = null;
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get("keys/public.key"));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            key = kf.generatePublic(spec);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        publicKey = key;
    }

    public void send_to_LicenseManager(byte[] encryptedData, LicenseManager licenseManager) {
        licenseManager.setEncryptedData(encryptedData);
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public boolean verify() {
        boolean equal = false;
        try {
            Signature signature_verify = Signature.getInstance("SHA256withRSA");
            signature_verify.initVerify(publicKey);
            signature_verify.update(hashedData);
            return signature_verify.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return equal;
    }

    public byte[] encrypt(String plainText, PublicKey publicKey) {
        byte[] cipherText = null;
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherText = encryptCipher.doFinal(plainText.getBytes("UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public byte[] hash(String data) {
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

    public boolean isLicenseFileBroken(){
        hashedData = hash(full_content);
        signature = readLicenseFile();
        if (verify()) {
            return false;
        } else {
            return true;
        }
    }
    public void createLicense() throws IOException {
        OutputStream os = new FileOutputStream("license.txt");
        os.write(signature);
        os.close();
    }

    public void __PROCESS__(LicenseManager licenseManager) throws IOException{

        System.out.println("Client started...");
        System.out.println("My Mac: " + macAdress);
        System.out.println("My DiskID: " + diskSerialNumber);
        System.out.println("My Motherboard ID: " + motherBoardSerialNumber);
        System.out.println("LicenseManager service started...");

        System.out.println("Client -- Raw Licence Text: " + full_content);

        //ENCRYPTION
        encryptedData = encrypt(full_content, publicKey);
        System.out.println("Client -- Encryted License Text: " + Base64.getEncoder().encodeToString(encryptedData));

        //HASHING
        hashedData = hash(full_content);
        System.out.println(
                "Client -- MD5 License Text: " + Base64.getEncoder().encodeToString(hashedData));

        System.out.println("Server -- Server is being requested...");

        //SENT TO LICENSE MANAGER
        send_to_LicenseManager(encryptedData,licenseManager);
        System.out.println(
                "Server -- Incoming Encryted Text: " + Base64.getEncoder().encodeToString(licenseManager.encryptedData));

        licenseManager.setDecryptedData(licenseManager.decryptMessage(licenseManager.encryptedData));
        System.out.println("Server -- Decrypted Text: " + licenseManager.decryptedData);

        licenseManager.setHashedData(licenseManager.hash(licenseManager.decryptedData));
        System.out.println("Server -- MD5 Plain License Text: "
                + Base64.getEncoder().encodeToString(licenseManager.hashedData));

        licenseManager.setSignature(licenseManager.sign(licenseManager.hashedData));
        System.out.println(
                "Server -- Digital Signature: " + Base64.getEncoder().encodeToString(licenseManager.signature));

        licenseManager.send_to_Client(licenseManager.signature,this);

        if(verify()){
            createLicense();
            System.out.println("Client -- Succeed. The License file content is secured and signed by the server");
        }
        else {
            System.out.println("Client -- verification failed");
        }
    }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Client client = new Client();
        LicenseManager licenseManager = new LicenseManager();

        File licenseFile = new File("license.txt");

        if (licenseFile.isFile()) {
            if(client.isLicenseFileBroken()){
                System.out.println("The license file has been broken!!");
                client.__PROCESS__(licenseManager);
            }
            else{
                System.out.println("Succeed. The license is correct.");
            }
        }
        else{
            client.__PROCESS__(licenseManager);
        }
    }

    public byte[] readLicenseFile() {
        File license_file = new File("license.txt");
        FileInputStream fis;
        byte[] bArray = new byte[(int) license_file.length()];
        try {
            fis = new FileInputStream(license_file);
            fis.read(bArray);
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bArray;
    }

}