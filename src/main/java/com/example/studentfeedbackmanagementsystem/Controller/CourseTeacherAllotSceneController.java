package com.example.studentfeedbackmanagementsystem.Controller;

import com.example.studentfeedbackmanagementsystem.Entity.Course;
import com.example.studentfeedbackmanagementsystem.Entity.Teacher;
import com.example.studentfeedbackmanagementsystem.Misc.Constants;
import com.example.studentfeedbackmanagementsystem.Misc.Helpers;
import com.example.studentfeedbackmanagementsystem.Repository.CoursesRepository;
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
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseTeacherAllotSceneController {

    Map<String, String> allCourses = new HashMap<>();
    Map<String, String> allTeachers = new HashMap<>();

    @FXML
    ComboBox<String> teacherInp;

    @FXML
    ComboBox<String> courseInp;

    @FXML
    Slider semesterInp;

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

    public void onAllotCourses(ActionEvent event) throws SQLException, IOException {
        String teacher = Helpers.getKey(allTeachers, teacherInp.getValue());
        String course = Helpers.getKey(allCourses, courseInp.getValue());
        int sem = (int) semesterInp.getValue();
        boolean courseRegisteredForDeptt = new DepartmentRepository().getAllottedCoursesForSemester(new TeacherRepository().getTeacherById(teacher).getDepttId(), sem).contains(new CoursesRepository().getCourseById(course));
        System.out.println(courseRegisteredForDeptt);
        if (!courseRegisteredForDeptt) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Course " + courseInp.getValue() + " not registered for this semester/department!");
            error.showAndWait();
            return;
        }
        boolean success = new TeacherRepository().validateCourseAssignment(teacher, course, sem);
        Alert alert;
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Success!");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error!");
        }
        alert.showAndWait();
        exit(event);
    }

    public void render(List<Teacher> teachers, List<Course> courses) {
        ObservableList<String> listTeachers = FXCollections.observableArrayList();
        ObservableList<String> listCourses = FXCollections.observableArrayList();
        teachers.forEach(teacher -> {
            allTeachers.put(teacher.getTeacherId(), teacher.getTeacherName());
            listTeachers.add(teacher.getTeacherName());
        });
        courses.forEach(course -> {
            allCourses.put(course.getCourseCode(), course.getCourseName());
            listCourses.add(course.getCourseName());
        });
        teacherInp.setItems(listTeachers);
        courseInp.setItems(listCourses);
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
}
