import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewPage {
    public void showWindow(JFrame frame, String messageContent) {
        JFrame viewPage = new JFrame("Message");

        // message pane
        JTextPane message = new JTextPane();
        message.setEditable(false);
        JScrollPane scrollMessage = new JScrollPane(message);
        message.setText(messageContent);
        scrollMessage.setBounds(65, 40, 250, 250);

        // return button
        JButton home = new JButton("Return");
        home.setBounds(149, 350, 90, 35);
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPage.dispose();
                frame.setVisible(true);
            }
        });

        // adding to the frame
        viewPage.add(scrollMessage);
        viewPage.add(home);

        viewPage.setSize(400,500);//400 width and 500 height
        viewPage.setLayout(null);//using no layout managers
        viewPage.setVisible(true);//making the frame visible
        viewPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
