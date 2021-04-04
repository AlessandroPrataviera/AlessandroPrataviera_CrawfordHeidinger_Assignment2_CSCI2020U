package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


public class Controller {
    @FXML
    private ListView<String> serverView;
    @FXML
    private ListView<String> clientView;

    @FXML
    public void initialize() throws IOException {
        getFiles();

        File files = new File("resources/local");
        for (File file : Objects.requireNonNull(files.listFiles())) {
            clientView.getItems().add(file.getName());
        }
    }

    private void getFiles() throws IOException {
        connect();
        Main.out.println("DIR");
        String files = Main.in.readLine();
        String[] filesSeparated = files.split(", ");
        serverView.getItems().addAll(filesSeparated);
        //disconnect();
    }


    public void uploadFiles() throws IOException {
        //connect();

        String fileName = clientView.getSelectionModel().getSelectedItem();
        Main.out.println("UPLOAD " + fileName);
        if (fileName != null) {
            Main.out.println("UPLOAD " + fileName);
        }
        List<String> content = Files.readAllLines(Paths.get("resources/local/" + fileName));
        for (String line : content) {
            Main.out.println(line);
        }
        Main.out.println("\\Z");
        //disconnect();
    }

    public void downloadFiles() {
        //connect();
        String fileName = serverView.getSelectionModel().getSelectedItem();
        if (fileName != null) {
            Main.out.println("DOWNLOAD " + fileName);
        }
        try {
            FileWriter writer = new FileWriter("resources/local/" + fileName);
            String line = Main.in.readLine();
            while (!line.equals("\\Z")) {
                writer.write(line + '\n');
                line = Main.in.readLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //disconnect();
    }

    public void connect() {
        try {
            Main.socket = new Socket("localhost", 8080);
            Main.in = new BufferedReader(new InputStreamReader(Main.socket.getInputStream()));
            Main.out = new PrintWriter(Main.socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
        Main.socket.close();
        Main.in.close();
        Main.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exit() {
        System.exit(0);
    }
}
