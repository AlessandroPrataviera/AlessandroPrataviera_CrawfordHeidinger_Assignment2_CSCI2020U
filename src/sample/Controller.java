package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


public class Controller {
    @FXML
    public TextArea textArea;
    @FXML
    private ListView<String> serverView;
    @FXML
    private ListView<String> clientView;

    @FXML
    public void initialize() throws IOException {          //set up files in ListViews
        getFiles();
        File files = new File("resources/local");//read the local files from folder
        for (File file : Objects.requireNonNull(files.listFiles())) {
            clientView.getItems().add(file.getName());     //add files to ListView
        }
    }

    private void getFiles() throws IOException {           //get file names from server then put in ListView
        connect();
        Main.out.println("DIR");                           //DIR command to get file names
        String files = Main.in.readLine();
        String[] filesSeparated = files.split(", ");//separate file names
        serverView.getItems().addAll(filesSeparated);
        disconnect();
    }


    public void uploadFiles() throws IOException {         //upload file from local to server
        connect();
        String fileName = clientView.getSelectionModel().getSelectedItem();
        if (fileName != null) {
            Main.out.println("UPLOAD " + fileName);       //put content from file into list then sent to server
            List<String> content = Files.readAllLines(Paths.get("resources/local/" + fileName));
            for (String line : content) {
                Main.out.println(line);
            }
            Main.out.println("\\Z");                      //terminate using \Z
        }
        disconnect();
        refresh();
    }

    public void downloadFiles() {                        //download file from server
        connect();
        String fileName = serverView.getSelectionModel().getSelectedItem();
        if (fileName != null) {
            Main.out.println("DOWNLOAD " + fileName);   //use DOWNLOAD command then send
            try {
                FileWriter writer = new FileWriter("resources/local/" + fileName);
                String line;                           //write into file until \Z
                while (!(line = Main.in.readLine()).equals("\\Z")) {
                    writer.write(line + '\n');
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        disconnect();
        refresh();
    }

    public void connect() {                           //connect client to server
        try {
            Main.socket = new Socket("localhost", 8080);
            Main.in = new BufferedReader(new InputStreamReader(Main.socket.getInputStream()));
            Main.out = new PrintWriter(Main.socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {                                        //disconnect client from server
        Main.socket.close();
        Main.in.close();
        Main.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {                          //refresh ListViews with up to date files
        connect();
        try {
            clientView.getItems().removeAll(clientView.getItems()); // remove items from ListViews
            serverView.getItems().removeAll(serverView.getItems());
            initialize();   // add items back
        } catch (IOException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void exit() {                             //exit program
        try {
            Main.in.close();
            Main.out.close();
            Main.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void viewServerFile() {                   //put content of selected server file into TextArea
        connect();
        String fileName;
        if ((fileName = serverView.getSelectionModel().getSelectedItem()) != null){
            try {
                Main.out.println("DOWNLOAD " + fileName);
                StringBuilder content = new StringBuilder(fileName + ":\n");
                String line;
                while (!(line = Main.in.readLine()).equals("\\Z")) {
                    content.append(line).append('\n');
                }
                textArea.setText(content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        disconnect();
    }

    public void viewClientFile() {                   //put content of selected local file into TextArea
        try {
            String fileName;
            if ((fileName = clientView.getSelectionModel().getSelectedItem()) != null) {
                textArea.setText("");
                BufferedReader reader = new BufferedReader(new FileReader("resources/local/" + fileName));
                String line;
                StringBuilder allText = new StringBuilder(fileName + ": \n");
                while ((line = reader.readLine()) != null) {
                    allText.append(line).append('\n');
                }
                textArea.setText(allText.toString());
            }
        } catch (IOException e) {
             e.printStackTrace();
        }
    }
}
