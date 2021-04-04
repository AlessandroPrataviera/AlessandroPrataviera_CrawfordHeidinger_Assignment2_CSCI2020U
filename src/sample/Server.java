package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server{
    private static ArrayList<ClientConnectionHandler> clients;
    private static final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final ServerSocket server;

    public Server() throws IOException {        // create ArrayList for clients and ServerSocket
        clients = new ArrayList<>();
        server = new ServerSocket(8080);
    }

    public void run() throws IOException {      // loop looking for connections to clients
        while (true) {
            System.out.println("[SERVER] Listening for connection");
            Socket client = server.accept();
            System.out.println("[SERVER] has connected to client " + client.getInetAddress().toString());
            ClientConnectionHandler clientThread = new ClientConnectionHandler(client);
            clients.add(clientThread);
            clients.removeIf(socket -> !socket.isRunning());    // remove client from list if disconnected
            pool.execute(clientThread);                         // start clients thread

        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
