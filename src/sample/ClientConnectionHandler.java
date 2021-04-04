package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ClientConnectionHandler implements Runnable{
    private static HashMap<String, File> fileHashMap = new HashMap<>();
    private final Socket client;
    private boolean running;
    private final BufferedReader in;
    private final PrintWriter out;
    private final InetAddress ip;

    public ClientConnectionHandler(Socket socket) throws IOException {  // set up socket, in, out, files and ip address
        this.client = socket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);

        fileHashMap = getFileHashMap();
        running = true;
        ip = socket.getInetAddress();
    }

    private HashMap<String, File> getFileHashMap() {                    // returns files in server repo as HashMap
        final HashMap<String, File> fileHashMap;
        File files = new File("./resources/server");
        fileHashMap = new HashMap<>();
        for (File file : Objects.requireNonNull(files.listFiles())) {
            fileHashMap.put(file.getName(), file);
        }
        return fileHashMap;
    }

    @Override
    public void run() {                                                 // get and handle command from client
        try {
            handleCommand(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleCommand (String command) throws IOException {    // handle command from client
        String[] separated = command.split(" ");    // split command to get all arguments
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
                running = false;
                break;
        }
    }

    private void dir() {                                                // return file names in repo then disconnect client
        fileHashMap = getFileHashMap();
        StringBuilder fileNames = new StringBuilder();
        for (var file : fileHashMap.keySet()) {
            fileNames.append(file).append(", ");
        }
        out.println(fileNames);
        disconnect();
    }

    private void download(String fileName) {                            // send file from server to client
        try {
            List<String> content = Files.readAllLines(Paths.get(String.valueOf(fileHashMap.get(fileName))));
            for (String line : content) {                               // sent content of each line then end with \Z
                out.println(line);
            }
            out.println("\\Z");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER] " + fileName + " has been downloaded to " + ip);
        disconnect();
    }

    private void upload(String fileName) {                              // send content of file from client to server
        try {
            FileWriter writer = new FileWriter("resources/server/" + fileName);
            String line = in.readLine();
            line = line.startsWith("UPLOAD") ? in.readLine() : line;    // readline() if command to not write command into file
            while (!line.equals("\\Z")) {           // write content to repo
                writer.write(line + '\n');
                line = in.readLine();
            }
            writer.close();
            File file = new File ("resources/server/" + fileName);
            fileHashMap.put(fileName, file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER] " + fileName + " has been uploaded from " + ip);
        disconnect();
    }

    private void disconnect() {                                         // disconnect client from server
        try {
            client.close();
            in.close();
            out.close();
            System.out.println("[SERVER] Disconnected from client " + ip.toString());
            running = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {                                        // returns whether the client is connected
        return running;
    }
}
