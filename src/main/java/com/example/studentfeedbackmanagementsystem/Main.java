package com.example.studentfeedbackmanagementsystem;

import com.example.studentfeedbackmanagementsystem.Controller.HomeSceneController;
import com.example.studentfeedbackmanagementsystem.Misc.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homeScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HomeSceneController homeSceneController = fxmlLoader.getController();
        homeSceneController.render(Constants.homeScreenImgUrl);
        stage.setResizable(false);
        try {
            Image icon = new Image(new FileInputStream(Constants.appIconImgUrl));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Error in fetching icon...");
        }
        stage.setTitle("Student Feedback Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}