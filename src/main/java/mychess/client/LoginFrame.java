package mychess.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mychess.util.Internet;
import mychess.entity.Message;
import mychess.entity.NormalMessage;
import mychess.util.ReadProperties;

public class LoginFrame extends JFrame{
    private int count=0;
    private JButton loginButton;// login button
    private JButton forgetPasswordButton;// forget password button
    private JLabel label;
    private JTextField nameField;// username
    private JPasswordField passwordField;// password
    private JLabel adminLabel;
    private JLabel passwordLabel;
    private Internet internet;
    static String remotehost = "localhost";
    private Lock lock;

    public LoginFrame(Internet outer_internet) {

        internet = outer_internet;
        Font font =new Font("ºÚÌå", Font.PLAIN, 20);
        label = new JLabel();

        adminLabel=new JLabel("Username");
        adminLabel.setBounds(20, 50, 60, 50);
        adminLabel.setFont(font);

        passwordLabel=new JLabel("Password");
        passwordLabel.setBounds(20, 120, 60, 50);
        passwordLabel.setFont(font);

        loginButton=new JButton("Login");
        //TODO:change to loginbutton
        loginButton.setBounds(90, 250, 100, 50);
        loginButton.setFont(font);

        forgetPasswordButton=new JButton("Quit");
        forgetPasswordButton.setBounds(250, 250, 100, 50);
        forgetPasswordButton.setFont(font);

        //username input frame
        nameField=new JTextField();
        nameField.setBounds(150, 50, 250, 50);
        nameField.setFont(font);

        passwordField=new JPasswordField();//password input frame
        passwordField.setBounds(150, 120, 250, 50);
        passwordField.setFont(font);
        passwordField.setEchoChar('*');

        label.add(nameField);
        label.add(passwordField);
        label.add(adminLabel);
        label.add(passwordLabel);
        label.add(loginButton);
        label.add(forgetPasswordButton);

        lock = new ReentrantLock();

		loginButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
                synchronized (loginButton) {
                    String userName=nameField.getText();
                    String password=String.valueOf(passwordField.getPassword());
                    NormalMessage message = new NormalMessage();
                    message.setAttach("Login");
                    internet.writeMessage(message);

                    NormalMessage nameMessage = new NormalMessage();
                    nameMessage.setAttach(userName);
                    internet.writeMessage(nameMessage);

                    NormalMessage passwordMessage = new NormalMessage();
                    passwordMessage.setAttach(password);
                    internet.writeMessage(passwordMessage);

                    Message myMessage = internet.readMessage();
                    if (((NormalMessage)myMessage).getAttach().equals("True")) {
                        loginButton.notify();
                    }
                }
            }
		});

        this.add(label);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(300,400);
        this.setSize(450, 400);
        this.setVisible(true);

        synchronized (loginButton) {
            try {
                loginButton.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setVisible(false);
    }

    public String getName() { return nameField.getText(); }

}
