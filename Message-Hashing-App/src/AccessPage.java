import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class AccessPage {
    public void showWindow(JFrame frame, ArrayList<Message> messages) {
        JFrame accessPage = new JFrame("Message View");

        // codename
        JLabel msgCodename = new JLabel("Message Codename");
        msgCodename.setBounds(25, 50, 150, 25);
        JTextField msgCodenameTA = new JTextField();
        msgCodenameTA.setBounds(175, 50, 175, 25);

        // password
        JLabel msgPassword = new JLabel("Message Password");
        msgPassword.setBounds(25, 100, 150, 25);
        JPasswordField msgPasswordTA = new JPasswordField();
        msgPasswordTA.setBounds(175, 100, 175, 25);


        // username
        JLabel username = new JLabel("Username");
        username.setBounds(25, 200, 150, 25);
        JTextField usernameTA = new JTextField();
        usernameTA.setBounds(175, 200, 175, 25);

        JLabel note = new JLabel("Passwords of existings users are same as their usernames.");
        JLabel note2 = new JLabel(" Such as Mert's password is Mert and Yiğit's password is Yiğit");
        note.setBounds(25, 150, 500, 25);
        note2.setBounds(25, 175, 500, 25);

        // user password
        JLabel userPassword = new JLabel("User Password");
        userPassword.setBounds(25, 250, 150, 25);
        JPasswordField userPasswordTA = new JPasswordField();
        userPasswordTA.setBounds(175, 250, 175, 25);
        JCheckBox passwordCheckbox = new JCheckBox();
        passwordCheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    userPasswordTA.setEchoChar('*');
                } else {
                    userPasswordTA.setEchoChar((char) 0);
                }
            }
        });
        passwordCheckbox.setBounds(175, 275, 25, 25);
        JLabel showPassword = new JLabel("Show Password");
        showPassword.setBounds(200, 275, 150, 25);

        // view button
        JButton view = new JButton("View");
        view.setBounds(75, 325, 90, 35);

        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageID = msgCodenameTA.getText();
                String messagePassword = SHA256Hasher.hashIt(String.valueOf(msgPasswordTA.getPassword()));
                String username = usernameTA.getText();
                String password = SHA256Hasher.hashIt(String.valueOf(userPasswordTA.getPassword()));

                boolean flag = false;
                for (Message message : messages) {
                    if (message.messageID.equals(messageID) && message.password.equals(messagePassword)) {
                        if (message.receiver.getUsername().equals(username) && password.equals(message.receiver.getPassword())) {
                            accessPage.setVisible(false);
                            ViewPage viewPage = new ViewPage();
                            viewPage.showWindow(accessPage, message.content);
                            flag = true;
                        }
                    }
                }
                if (!flag) {
                    JOptionPane.showMessageDialog(accessPage, "Error");
                }
            }
        });
        // else

        // reset button
        JButton reset = new JButton("Reset");
        reset.setBounds(225, 325, 90, 35);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accessPage.dispose();
                AccessPage resetAccessPage = new AccessPage();
                resetAccessPage.showWindow(frame, messages);
            }
        });

        // home button
        JButton home = new JButton("Home");
        home.setBounds(150, 375, 90, 35);
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accessPage.dispose();
                frame.setVisible(true);
            }
        });

        // adding to the frame
        accessPage.add(msgCodename);
        accessPage.add(msgCodenameTA);
        accessPage.add(msgPassword);
        accessPage.add(msgPasswordTA);
        accessPage.add(username);
        accessPage.add(usernameTA);
        accessPage.add(userPassword);
        accessPage.add(userPasswordTA);
        accessPage.add(note);
        accessPage.add(note2);
        accessPage.add(passwordCheckbox);
        accessPage.add(showPassword);
        accessPage.add(view);
        accessPage.add(reset);
        accessPage.add(home);

        accessPage.setSize(450,500);//450 width and 500 height
        accessPage.setLayout(null);//using no layout managers
        accessPage.setVisible(true);//making the frame visible
        accessPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
