package com.example.studentfeedbackmanagementsystem.Controller;

import com.example.studentfeedbackmanagementsystem.Entity.Department;
import com.example.studentfeedbackmanagementsystem.Entity.Student;
import com.example.studentfeedbackmanagementsystem.Misc.Constants;
import com.example.studentfeedbackmanagementsystem.Misc.Helpers;
import com.example.studentfeedbackmanagementsystem.Repository.StudentRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateStudentSceneController {

    Map<String, String> allDeptts = new HashMap<>();

    @FXML
    TextField studentRnoInp;

    @FXML
    TextField studentNewNameInp;

    @FXML
    ComboBox<String> depttInp;

    @FXML
    Slider semesterInp;

    public void onUpdateStudentInDb(ActionEvent event) throws SQLException {
        Alert alert;
        if (studentNewNameInp.getText().trim().equals("") || studentRnoInp.getText().trim().equals("")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty Fields!");
            alert.showAndWait();
            return;
        }
        String rno = studentRnoInp.getText().trim();
        String newName = studentNewNameInp.getText().trim();
        Integer newSemester = (int) semesterInp.getValue();
        Student student = new Student(rno, newName, Helpers.getKey(allDeptts, depttInp.getValue()), newSemester);
        StudentRepository studentRepository = new StudentRepository();
        boolean res = studentRepository.updateStudent(rno, student);
        if (res) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Student Updated!");
            alert.showAndWait();
            return;
        }
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Something went wrong, please try again!");
        alert.showAndWait();
    }

    public void render(List<Department> depts) {
        ObservableList<String> list = FXCollections.observableArrayList();
        depts.forEach(dept -> {
            allDeptts.put(dept.getDepttId(), dept.getDepttName());
            list.add(dept.getDepttName());
        });
        depttInp.setItems(list);
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
