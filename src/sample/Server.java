package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Server{
    private HashMap<String, File> fileHashMap;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Socket client = null;

    public Server() {
        File files = new File("./resources/server");
        fileHashMap = new HashMap<>();
        for (File file : Objects.requireNonNull(files.listFiles())) {
            fileHashMap.put(file.getName(), file);
        }
    }

    public void run() {
        if (client == null) {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                System.out.println("[SERVER] listening for connection");
                client = serverSocket.accept();
                System.out.println("[SERVER] has connected to client");
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String line = "";
        while (!line.equals("SHUTDOWN")) {
            try {
                assert in != null;
                line = in.readLine();
                System.out.println(line);
                String[] separatedLine = line.split(" ");
                switch (separatedLine[0]) {
                    case "UPLOAD":
                        upload(separatedLine[1]);
                        break;
                    case "DOWNLOAD":
                        download(separatedLine[1]);
                        break;
                    case "DIR":
                        dir();
                        break;
                    default:
                        System.out.println("Invalid command");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //return list of contents in shared folder then disconnect client
    private void dir() throws IOException {
        StringBuilder fileNames = new StringBuilder();
        for (var file : fileHashMap.keySet()) {
            fileNames.append(file).append(", ");
        }
        out.println(fileNames);
    }

    private void download(String fileName) {
        try {
            List<String> content = Files.readAllLines(Paths.get("resources/server/" + fileName));
            for (String line : content) {
                out.println(line);
            }
            out.println("\\Z");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER]" + fileName + "has been downloaded");
    }

    private void upload(String fileName) {
        try {
            FileWriter writer = new FileWriter("resources/server/" + fileName);
            String line = in.readLine();
            line = in.readLine();
            while (!line.equals("\\Z")) {
                writer.write(line + '\n');
                line = in.readLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER] " + fileName + " has been uploaded");
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
