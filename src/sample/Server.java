package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server{

    private static ArrayList<ClientHandler> clients;
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);
    private final ServerSocket server;

    public Server() throws IOException {
        clients = new ArrayList<>();
        server = new ServerSocket(8080);
    }

    public void run() throws IOException {
        while (true) {
            System.out.println("[SERVER] Listening for connection");
            Socket client = server.accept();
            System.out.println("[SERVER] has connected to client");
            ClientHandler clientThread = new ClientHandler(client);
            clients.add(clientThread);
            pool.execute(clientThread);
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
