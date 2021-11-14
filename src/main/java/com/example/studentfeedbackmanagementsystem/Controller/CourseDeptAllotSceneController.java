package com.example.studentfeedbackmanagementsystem.Controller;

import com.example.studentfeedbackmanagementsystem.Entity.Course;
import com.example.studentfeedbackmanagementsystem.Entity.Department;
import com.example.studentfeedbackmanagementsystem.Misc.Constants;
import com.example.studentfeedbackmanagementsystem.Misc.Helpers;
import com.example.studentfeedbackmanagementsystem.Repository.DepartmentRepository;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class CourseDeptAllotSceneController {

    Map<String, String> allCourses = new HashMap<>();
    Map<String, String> allDepartments = new HashMap<>();

    @FXML
    ComboBox<String> depttInp;

    @FXML
    ComboBox<String> courseInp1, courseInp2, courseInp3, courseInp4, courseInp5;

    @FXML
    Slider semesterInp;

    public void onAllotCourses(ActionEvent event) throws SQLException, IOException {
        String deptt = Helpers.getKey(allDepartments, depttInp.getValue());
        List<String> courses = getInpCourses();
        int sem = (int) semesterInp.getValue();
        if (getAllottedCoursesForSem(deptt, sem).size() > 5) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setContentText("Reached max registration limit per semester!");
            err.showAndWait();
            return;
        }
        DepartmentRepository departmentRepository = new DepartmentRepository();
        Alert res = departmentRepository.allotCoursesToDeptt(deptt, courses, sem);
        res.showAndWait();
        exit(event);
    }

    public List<String> getInpCourses() {
        List<String> list = new ArrayList<>();
        List<ComboBox> inps = Arrays.asList(courseInp1, courseInp2, courseInp3, courseInp4, courseInp5);
        for (var inp: inps) {
            if (inp.getValue() != null && !list.contains((String) inp.getValue())) {
                list.add(Helpers.getKey(allCourses, (String) inp.getValue()));
            }
        }
        return list;
    }

    public List<Course> getAllottedCoursesForSem(String depttId, int sem) throws SQLException {
        return new DepartmentRepository().getAllottedCoursesForSemester(depttId, sem);
    }

    public void render(List<Course> courses, List<Department> departments) {
        ObservableList<String> listCourses = FXCollections.observableArrayList();
        ObservableList<String> listDeptts = FXCollections.observableArrayList();

        courses.forEach(course -> {
            allCourses.put(course.getCourseCode(), course.getCourseName());
            listCourses.add(course.getCourseName());
        });
        departments.forEach(department -> {
            allDepartments.put(department.getDepttId(), department.getDepttName());
            listDeptts.add(department.getDepttName());
        });
        depttInp.setItems(listDeptts);
        courseInp1.setItems(listCourses);
        courseInp2.setItems(listCourses);
        courseInp3.setItems(listCourses);
        courseInp4.setItems(listCourses);
        courseInp5.setItems(listCourses);
    }
    public void exit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/homeScene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        HomeSceneController homeSceneController = loader.getController();
        homeSceneController.render(Constants.homeScreenImgUrl);
        stage.setScene(scene);
        stage.show();
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
