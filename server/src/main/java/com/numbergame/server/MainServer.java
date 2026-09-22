package com.numbergame.server;

import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        System.out.println(">>> Server Game Tim So dang khoi dong tren port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(">>> Server san sang nhan ket noi (TCP Socket)!");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Co Client ket noi: " + clientSocket.getRemoteSocketAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}