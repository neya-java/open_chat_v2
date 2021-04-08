package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static ServerSocket server;
    private static Socket socket;

    private static final int PORT = 8197;
    private List<ClientHandler> client;
    private AuthService authService;

    public Server() {
        client = new CopyOnWriteArrayList<>();
        authService = new SimpleAuthService();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started");

            while (true) {
                socket = server.accept();
                System.out.println(socket.getLocalSocketAddress());
                System.out.println("Client connect: " + socket.getRemoteSocketAddress());
                new ClientHandler(this,socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg){
        String message = String.format("%s : %s", sender.getNickname(), msg);
        for (ClientHandler c : client) {
            c.sendMsg(message);
            System.out.println(c);
        }
    }

    public void userMsg(String nickname, String user, String msg){
        String message = String.format("%s : %s", nickname, msg);
        for (ClientHandler c : client) {
            System.out.println(user + "::::::::::" + c.getNickname());
            if (user.equals(c.getNickname())) {
                c.sendMsg(message);
//              //System.out.println(c);
            }
        }
    }

    public void subscribe(ClientHandler clientHandler){
        client.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler){
        client.remove(clientHandler);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
