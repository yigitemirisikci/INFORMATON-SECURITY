import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;

public class Main {
    static String key;
    public static void main(String[] args) {
        key = "Xn2r5u8x";

        File usersEncrypted = new File("usersEncrypted.data");
        File tempUsers = new File("tempUsers");

        File messagesEncrypted = new File("messagesEncrypted.data");
        File tempMsgs = new File("tempMsgs");


        HashMap<String, User> usernameToObject = new HashMap<>();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>();

        try {
            // decrypt users file
            EncryptDecryptFile.decrypt(key, usersEncrypted, tempUsers);

            // decrypt messages file
            EncryptDecryptFile.decrypt(key, messagesEncrypted, tempMsgs);

            // extract decrypted users info
            Scanner sc1 = new Scanner(tempUsers);
            while (sc1.hasNextLine()) {
                String line = sc1.nextLine();
                String[] split = line.split(" ");
                User user = new User(split[0], split[1]);
                users.add(user);
                if (!usernameToObject.containsKey(split[0])) {
                    usernameToObject.put(split[0], user);
                }
            }

            // extract decrypted messages info
            Scanner sc2 = new Scanner(tempMsgs);
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine();
                String[] split = line.split("\t");

                Message message = new Message(split[0], split[1], split[2], usernameToObject.get(split[3]));
                messages.add(message);
            }

            sc1.close();
            sc2.close();
            Files.deleteIfExists(tempUsers.toPath());




            Files.deleteIfExists(tempMsgs.toPath());
        } catch (CryptoException | IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        // Creating GUI
        JFrame f = new JFrame("Message Box!");//creating instance of JFrame

        JLabel welcomeText = new JLabel("Welcome to MessageBox!", SwingConstants.CENTER);
        welcomeText.setBounds(0, 0, 400, 100);
        welcomeText.setFont(new Font("Calibri", Font.BOLD, 20));

        JButton access = new JButton("Access");//creating instance of JButton
        access.setBounds(150,100,100, 40);//x axis, y axis, width, height
        access.addActionListener(new ActionListener() {
            // Access Page
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                AccessPage accessPage = new AccessPage();
                accessPage.showWindow(f, messages);
            }
        });

        JButton leaveAMsg = new JButton("Leave a message");
        leaveAMsg.setBounds(100, 200, 200, 40);
        leaveAMsg.addActionListener(new ActionListener() {
            // Register page
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                RegisterPage registerPage = new RegisterPage();
                registerPage.showWindow(f, usernameToObject, messages);
            }
        });

        JLabel credits = new JLabel("Abdullah Mert Dinçer       Yiğit Emir Işıkçı", SwingConstants.CENTER);
        credits.setBounds(0, 340, 400, 100);
        credits.setFont(new Font("Calibri", Font.PLAIN, 12));

        f.add(welcomeText);
        f.add(access);//adding button in JFrame
        f.add(leaveAMsg);
        f.add(credits);

        f.setSize(400,500);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
