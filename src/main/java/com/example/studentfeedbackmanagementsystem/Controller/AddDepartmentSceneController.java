package com.example.studentfeedbackmanagementsystem.Controller;

import com.example.studentfeedbackmanagementsystem.Entity.Department;
import com.example.studentfeedbackmanagementsystem.Misc.Constants;
import com.example.studentfeedbackmanagementsystem.Repository.DepartmentRepository;
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

public class AddDepartmentSceneController {

    private static DepartmentRepository departmentRepository;

    @FXML
    TextField depttNameInp, depttCodeInp;

    public void onAddDepartmentToDb(ActionEvent event) throws SQLException {
        departmentRepository = new DepartmentRepository();
        String name = depttNameInp.getText().trim();
        String id = depttCodeInp.getText().trim();
        if (name.equals("") || id.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty fields!");
            alert.showAndWait();
            return;
        }
        Department department = new Department(id, name);
        Alert res = departmentRepository.saveDepartment(department);
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
