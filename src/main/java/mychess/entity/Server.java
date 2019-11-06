package mychess.entity;


import mychess.util.ReadProperties;

public class Server {
    private ChessServer chessServer;
    private FunctionServer functionServer;
    public Server() {
        new Thread() {
            public void run() {
                chessServer = new ChessServer();
            }
        }.start();

        new Thread() {
            public void run() {
                functionServer = new FunctionServer();
            }
        }.start();
    }
    public static void main(String args[]) {
        ReadProperties.read();
        System.out.println("server start...");
        new Server();
    }
}
