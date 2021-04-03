package sample;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ClientHandler implements Runnable{
    private final HashMap<String, File> fileHashMap;
    private Socket client;
    private boolean running;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientHandler(Socket socket) throws IOException {
        this.client = socket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);

        File files = new File("./resources/server");
        fileHashMap = new HashMap<>();
        for (File file : Objects.requireNonNull(files.listFiles())) {
            fileHashMap.put(file.getName(), file);
        }
        running = true;
    }

    @Override
    public void run() {
        try {
            while (running) {
                String command = in.readLine();
                handleCommand(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleCommand (String command) {
        String[] separated = command.split(" ");
        switch (separated[0]) {
            case "UPLOAD":
                upload(separated[1]);
                break;
            case "DOWNLOAD":
                download(separated[1]);
                break;
            case "DIR":
                dir();
                break;
            default:
                System.out.println("Invalid command");
                running = false;
                break;
        }
    }

    private void dir() {
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
        System.out.println("[SERVER] " + fileName + " has been downloaded");
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
}
