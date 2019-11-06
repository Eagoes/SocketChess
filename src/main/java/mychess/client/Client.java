package mychess.client;

import mychess.util.Internet;
import mychess.util.ReadProperties;

import javax.swing.*;

public class Client {
    private JFrame loginFrame;
    private JFrame clientFrame;
    private Internet chessInternet;
    private Internet funcInternet;
    public Client() {
        ReadProperties.read();
        chessInternet = new Internet(ReadProperties.IP, Integer.parseInt(ReadProperties.PORT));
        funcInternet = new Internet(ReadProperties.IP, Integer.parseInt(ReadProperties.FUNCTIONPORT));
        loginFrame = new LoginFrame(funcInternet);
        clientFrame = new ChessClientFrame(chessInternet, funcInternet, loginFrame.getName());
    }

    public static void main(String args[]) {
        System.out.println("Client start ...");
        Client client = new Client();
    }
}
