package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Main extends Application {
    static public Socket socket;
    static public BufferedReader in;
    static public PrintWriter out;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Assignment 2");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }

    @Override
    public void stop() {
        try {
            out.println("SHUTDOWN");
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
