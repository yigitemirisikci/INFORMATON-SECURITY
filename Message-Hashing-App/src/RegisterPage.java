import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterPage {
    private File mess;

    public void showWindow(JFrame frame, HashMap<String, User> utO, ArrayList<Message> messages) {
        JFrame registerPage = new JFrame("Register Form");

        // auth username*
        JLabel authUsername = new JLabel("Auth. username*");
        authUsername.setBounds(25, 50, 150, 25);
        String[] users = {"Mert", "Arda", "Yiğit", "Halil", "Alp", "Serhat", "Haşim"};
        JComboBox<String> usersDropdownMenu = new JComboBox<>(users);
        usersDropdownMenu.setBounds(185, 50, 150, 25);

        // Password*
        JLabel password = new JLabel("Password*");
        password.setBounds(25, 100, 150, 25);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(185, 100, 150, 25);

        // Confirm password*
        JLabel cPassword = new JLabel("Confirm Password*");
        cPassword.setBounds(350, 100, 150, 25);
        JPasswordField cPasswordField = new JPasswordField();
        cPasswordField.setBounds(475, 100, 150, 25);

        // Message codename*
        JLabel msgCodename = new JLabel("Message codename*");
        msgCodename.setBounds(25, 150, 150, 25);
        JTextField msgCodenameField = new JTextField();
        msgCodenameField.setBounds(185, 150, 150, 25);

        // Text pane
        JLabel text = new JLabel("ENTER YOUR MESSAGE*");
        text.setBounds(25, 225, 200, 25);
        JTextPane textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBounds(250, 225, 375, 150);

        // Create
        JButton createMessage = new JButton("Create Message");
        createMessage.setBounds(185, 425, 90, 35);
        createMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(String.valueOf(cPasswordField.getPassword()))) {
                    boolean isValid = true;
                    for (Message message : messages) {
                        if (message.messageID.equals(msgCodenameField.getText())) {
                            isValid = false;
                        }
                    }
                    if (isValid) {
                        String hashedPassword = SHA256Hasher.hashIt(String.valueOf(passwordField.getPassword()));
                        Message message = new Message(msgCodenameField.getText(), textPane.getText(), hashedPassword, utO.get(String.valueOf(usersDropdownMenu.getSelectedItem())));
                        messages.add(message);

                        mess = new File("tempMsgs2");
                        try {
                            PrintWriter pw = new PrintWriter(mess);
                            for (Message message1 : messages) {
                                pw.print(message1.messageID + "\t" + message1.content + "\t" + message1.password + "\t" + message1.receiver.getUsername() + "\n");
                            }
                            pw.close();

                            try {
                                File messagesEncrypted = new File("messagesEncrypted.data");
                                File inputFile = new File("tempMsgs2");
                                EncryptDecryptFile.encrypt(Main.key, inputFile, messagesEncrypted);

                                Files.deleteIfExists(inputFile.toPath());
                            } catch (CryptoException | IOException em) {
                                em.printStackTrace();
                            }

                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(registerPage, "Success");
                    }
                    else {
                        JOptionPane.showMessageDialog(registerPage, "Error");
                    }
                } else {
                    JOptionPane.showMessageDialog(registerPage, "Error");
                }
            }
        });

        // Home
        JButton home = new JButton("HOME");
        home.setBounds(375, 425, 90, 35);
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerPage.dispose();
                frame.setVisible(true);
            }
        });

        // adding to the frame
        registerPage.add(authUsername);
        registerPage.add(usersDropdownMenu);
        registerPage.add(password);
        registerPage.add(passwordField);
        registerPage.add(cPassword);
        registerPage.add(cPasswordField);
        registerPage.add(msgCodename);
        registerPage.add(msgCodenameField);
        registerPage.add(text);
        registerPage.add(scrollPane);
        registerPage.add(createMessage);
        registerPage.add(home);

        registerPage.setSize(700,550);//400 width and 500 height
        registerPage.setLayout(null);//using no layout managers
        registerPage.setVisible(true);//making the frame visible
        registerPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setMess(File mess) {
        this.mess = mess;
    }

    public File getMess() {
        return mess;
    }
}
