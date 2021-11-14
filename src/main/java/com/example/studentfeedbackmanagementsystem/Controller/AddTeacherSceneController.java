package com.example.studentfeedbackmanagementsystem.Controller;

import com.example.studentfeedbackmanagementsystem.Entity.Department;
import com.example.studentfeedbackmanagementsystem.Entity.Teacher;
import com.example.studentfeedbackmanagementsystem.Misc.Constants;
import com.example.studentfeedbackmanagementsystem.Misc.Helpers;
import com.example.studentfeedbackmanagementsystem.Repository.DepartmentRepository;
import com.example.studentfeedbackmanagementsystem.Repository.TeacherRepository;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTeacherSceneController {

    private static DepartmentRepository departmentRepository;

    private static TeacherRepository teacherRepository;

    Map<String, String> departments = new HashMap<>();

    @FXML
    TextField teacherNameInp, teacherCodeInp;

    @FXML
    ComboBox<String> departmentInp;

    public void onAddTeacherToDb(ActionEvent event) throws SQLException {
        teacherRepository = new TeacherRepository();
        String name = teacherNameInp.getText().trim();
        String id = teacherCodeInp.getText().trim();
        String depttId = Helpers.getKey(departments, departmentInp.getValue());
        if (name.equals("") || id.equals("") || depttId == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty fields!");
            alert.showAndWait();
            return;
        }
        Teacher teacher = new Teacher(id, name, depttId, null, null);
        Alert res = teacherRepository.saveTeacher(teacher);
        res.showAndWait();
    }

    public void render(List<Department> deps) {
        ObservableList<String> list = FXCollections.observableArrayList();
        deps.forEach(dep -> {
            departments.put(dep.getDepttId(), dep.getDepttName());
            list.add(dep.getDepttName());
        });
        departmentInp.setItems(list);
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
