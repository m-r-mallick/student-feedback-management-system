package com.example.studentfeedbackmanagementsystem.Controller;

import com.example.studentfeedbackmanagementsystem.Entity.Course;
import com.example.studentfeedbackmanagementsystem.Misc.Constants;
import com.example.studentfeedbackmanagementsystem.Repository.CoursesRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AddCourseSceneController {

    private static CoursesRepository coursesRepository;

    @FXML
    TextField courseNameInp, courseCodeInp;

    public void onAddCourseToDb(ActionEvent event) throws SQLException {
        coursesRepository = new CoursesRepository();
        if (courseNameInp.getText().trim().equals("") || courseCodeInp.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty Fields!");
            alert.showAndWait();
            return;
        }
        Course course = new Course(courseCodeInp.getText().trim(), courseNameInp.getText().trim());
        Alert result = coursesRepository.addCourseToDb(course);
        result.showAndWait();
    }

    public void backToHomeScene(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/homeScene.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        HomeSceneController homeSceneController = fxmlLoader.getController();
        homeSceneController.reRender(Constants.homeScreenImgUrl);
        stage.setScene(scene);
        stage.show();
    }
}
