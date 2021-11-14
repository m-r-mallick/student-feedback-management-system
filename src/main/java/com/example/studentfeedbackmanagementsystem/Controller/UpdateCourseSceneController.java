package com.example.studentfeedbackmanagementsystem.Controller;

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

public class UpdateCourseSceneController {

    @FXML
    TextField courseCodeInp;

    @FXML
    TextField courseNameInp;

    public void onUpdateCourseInDb(ActionEvent event) throws SQLException {
        CoursesRepository coursesRepository = new CoursesRepository();
        String code = courseCodeInp.getText().trim();
        String cname = courseNameInp.getText().trim();
        if (code.equals("") || cname.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty Fields!");
            alert.showAndWait();
            return;
        }
        Alert res = coursesRepository.updateCourse(code, cname);
        res.showAndWait();
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
