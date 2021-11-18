package com.example.studentfeedbackmanagementsystem.Controller;

import com.example.studentfeedbackmanagementsystem.Entity.Student;
import com.example.studentfeedbackmanagementsystem.Misc.Constants;
import com.example.studentfeedbackmanagementsystem.Misc.Helpers;
import com.example.studentfeedbackmanagementsystem.Repository.CoursesRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FeedbackSceneController {

    private CoursesRepository coursesRepository;

    static HashMap<String, String> courses;
    static String[] subs;
    static Student student;

    @FXML
    FlowPane ratingsPane;

    @FXML
    ComboBox<String> sub1, sub2, sub3, sub4, sub5;

    @FXML
    Rating rate1, rate2, rate3, rate4, rate5;

    @FXML
    Label studentLabel;

    public void backToHomeScene(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/homeScene.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        HomeSceneController homeSceneController = fxmlLoader.getController();
        homeSceneController.render(Constants.homeScreenImgUrl);
        stage.setScene(scene);
        stage.show();
    }

    //TODO
    public void onClickSave(ActionEvent e) throws InterruptedException, IOException, SQLException {
        HashMap<String, Double> data = new HashMap<>();
        HashMap<String, Integer> res = new HashMap<>();
        if (checkDuplicate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Duplicate Values!");
            alert.setContentText("Please re-check your input values...");
            alert.showAndWait();
            return;
        }
        data.put(sub1.getValue(), rate1.getRating());
        data.put(sub2.getValue(), rate2.getRating());
        data.put(sub3.getValue(), rate3.getRating());
        data.put(sub4.getValue(), rate4.getRating());
        data.put(sub5.getValue(), rate5.getRating());
        data.forEach((k, v) -> {
            if (k != null) {
                System.out.println("Subject : " + k + ", Rating : " + v);
                String cId = Helpers.getKey(courses, k);
                res.put(cId, v.intValue());
            }
        });
        List<String> attemptedRating = new ArrayList<>();
        for (String i : res.keySet()) {
            if (!coursesRepository.getCoursesPerStudent(student.getRollNo()).contains(i)) {
                attemptedRating.add(i);
            }
        }
        if (coursesRepository.getCoursesPerStudent(student.getRollNo()).size() + attemptedRating.size() <= 5) {
            boolean saveSuccess = coursesRepository.saveRatings(res, student);
            if (saveSuccess) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Success!");
                alert.showAndWait();
            }
            System.out.println("Logging Out...");
            Thread.sleep(500);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Attempted rating for more than 5 courses!");
            alert.showAndWait();
        }
        logout(e);

    }

    public void render(Student student) throws SQLException {
        coursesRepository = new CoursesRepository();
        FeedbackSceneController.student = student;
        System.out.println("Enrolled in " + coursesRepository.getCoursesPerStudent(student.getRollNo()).size() + " Subjects");
        courses = coursesRepository.getCoursesPerDepttPerSem(student.getDepttId(), student.getSemester());
        subs = courses.values().toArray(new String[0]);
        Arrays.sort(subs);
        sub1.setItems(FXCollections.observableArrayList(subs));
        sub2.setItems(FXCollections.observableArrayList(subs));
        sub3.setItems(FXCollections.observableArrayList(subs));
        sub4.setItems(FXCollections.observableArrayList(subs));
        sub5.setItems(FXCollections.observableArrayList(subs));
        studentLabel.setText(student.getName() + "\n" + student.getRollNo() + "\n" + student.getDepttId());
    }

    public boolean checkDuplicate() {
        return sub1.getValue() != null && (sub1.getValue().equals(sub2.getValue()) || sub1.getValue().equals(sub3.getValue()) || sub1.getValue().equals(sub4.getValue()) || sub1.getValue().equals(sub5.getValue()))
                || sub2.getValue() != null && (sub2.getValue().equals(sub3.getValue()) || sub2.getValue().equals(sub4.getValue()) || sub2.getValue().equals(sub5.getValue()))
                || sub3.getValue() != null && (sub3.getValue().equals(sub4.getValue()) || sub3.getValue().equals(sub5.getValue()))
                || sub4.getValue() != null && (sub4.getValue().equals(sub5.getValue()));
    }

    public void logout(ActionEvent event) throws IOException {
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
