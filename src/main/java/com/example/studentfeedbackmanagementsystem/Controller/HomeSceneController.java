package com.example.studentfeedbackmanagementsystem.Controller;

import com.example.studentfeedbackmanagementsystem.DAO.CourseTeachers;
import com.example.studentfeedbackmanagementsystem.DAO.DepartmentCourses;
import com.example.studentfeedbackmanagementsystem.DAO.StudentCourseRating;
import com.example.studentfeedbackmanagementsystem.Entity.Course;
import com.example.studentfeedbackmanagementsystem.Entity.Department;
import com.example.studentfeedbackmanagementsystem.Entity.Student;
import com.example.studentfeedbackmanagementsystem.Entity.Teacher;
import com.example.studentfeedbackmanagementsystem.Misc.Helpers;
import com.example.studentfeedbackmanagementsystem.Repository.CoursesRepository;
import com.example.studentfeedbackmanagementsystem.Repository.DepartmentRepository;
import com.example.studentfeedbackmanagementsystem.Repository.StudentRepository;
import com.example.studentfeedbackmanagementsystem.Repository.TeacherRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HomeSceneController {

    Map<String, String> allDepts = new HashMap<>();

    @FXML
    Accordion sideBarAccordion;

    @FXML
    TextField rollNoInput;

    @FXML
    TextField nameInput;

    @FXML
    TextField semesterInput;

    @FXML
    Pane hsDisplay;

    @FXML
    Label miniDisplay;

    @FXML
    TitledPane adminZone, studentZone;

    @FXML
    ComboBox<String> depttInp;

    public void onConnectToDB(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/feedbackScene.fxml"));
        Parent root = loader.load();
        FeedbackSceneController feedbackSceneController = loader.getController();
        try {
            Student student = new Student(rollNoInput.getText(), nameInput.getText(), Helpers.getKey(allDepts, depttInp.getValue()), Integer.valueOf(semesterInput.getText()));
            feedbackSceneController.render(student);
        } catch (Exception exception) {
            exception.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong Input, please try again!");
            alert.showAndWait();
            return;
        }
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onAddCourse(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/addCourseScene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onAddStudent(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/addStudentScene.fxml"));
        Parent root = loader.load();
        AddStudentSceneController addStudentSceneController = loader.getController();
        addStudentSceneController.render(new DepartmentRepository().getAllDepartments());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onViewCoursesClick() throws SQLException {
        if (hsDisplay.getChildren().size() > 0) {
            hsDisplay.getChildren().clear();
        }

        CoursesRepository coursesRepository = new CoursesRepository();
        TableView<Course> table = new TableView<>();
        ObservableList<Course> courses = coursesRepository.getCourses();

        TableColumn codeCol = new TableColumn("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        table.setItems(courses);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            sideBarAccordion.setExpandedPane(studentZone);
            miniDisplay.setText(Helpers.formattedCourse(newValue));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(codeCol, nameCol);
        table.prefHeightProperty().bind(hsDisplay.heightProperty());
        table.prefWidthProperty().bind(hsDisplay.widthProperty());

        hsDisplay.getChildren().addAll(table);
    }

    public void onViewStudentsClick() throws SQLException {
        if (hsDisplay.getChildren().size() > 0) {
            hsDisplay.getChildren().clear();
        }

        StudentRepository studentRepository = new StudentRepository();
        TableView<Student> table = new TableView<>();
        ObservableList<Student> students = studentRepository.getStudents();

        TableColumn rnoCol = new TableColumn("Roll No.");
        rnoCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        TableColumn nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn semCol = new TableColumn<>("Semester");
        semCol.setCellValueFactory(new PropertyValueFactory<>("semester"));

        table.setItems(students);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            sideBarAccordion.setExpandedPane(studentZone);
            miniDisplay.setText(Helpers.formattedStudent(newValue));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(rnoCol, nameCol, semCol);
        table.prefHeightProperty().bind(hsDisplay.heightProperty());
        table.prefWidthProperty().bind(hsDisplay.widthProperty());

        hsDisplay.getChildren().addAll(table);
    }

    public void onCourseRatingsClick(ActionEvent event) throws SQLException {
        if (hsDisplay.getChildren().size() > 0) {
            hsDisplay.getChildren().clear();
        }

        CoursesRepository coursesRepository = new CoursesRepository();
        TableView<StudentCourseRating> table = new TableView<>();
        ObservableList<StudentCourseRating> scrData = coursesRepository.getSCRData();

        TableColumn codeCol = new TableColumn("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        TableColumn rnoCol = new TableColumn("Roll No.");
        rnoCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        TableColumn teacherCol = new TableColumn("Teacher ID");
        teacherCol.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
        TableColumn depttCol = new TableColumn("Department Id");
        depttCol.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        TableColumn ratingCol = new TableColumn("Ratings");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        table.setItems(scrData);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            sideBarAccordion.setExpandedPane(studentZone);
            miniDisplay.setText(Helpers.formattedSCR(newValue));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(codeCol, rnoCol, teacherCol, depttCol, ratingCol);
        table.prefHeightProperty().bind(hsDisplay.heightProperty());
        table.prefWidthProperty().bind(hsDisplay.widthProperty());

        hsDisplay.getChildren().addAll(table);
    }

    public void onDeleteCourse(ActionEvent event) throws SQLException {
        CoursesRepository coursesRepository = new CoursesRepository();
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #993333;");
        vbox.setSpacing(20);
        TextField textField = new TextField();
        textField.setPrefHeight(20);
        textField.setPrefWidth(vbox.getPrefWidth());
        textField.setPromptText("Enter Course ID");
        Button delBtn = new Button();
        delBtn.setText("Delete Course");
        delBtn.setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Confirm delete course?");
            alert.showAndWait();
            if (alert.getResult() != ButtonType.OK) {
                return;
            }
            String course = textField.getText().trim();
            System.out.println("Deleting course " + course + "...");
            try {
                Alert res = coursesRepository.deleteCourse(course);
                res.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        vbox.setPrefWidth(hsDisplay.getPrefWidth());
        vbox.setPrefHeight(hsDisplay.getPrefHeight());
        vbox.getChildren().addAll(textField, delBtn);

        vbox.setPadding(new Insets(((hsDisplay.getPrefHeight() / 2) - (textField.getPrefHeight() + delBtn.getPrefHeight()) / 2) - 10, 50, 50, 50));

        hsDisplay.getChildren().clear();
        hsDisplay.getChildren().addAll(vbox);
    }

    public void onDeleteStudent(ActionEvent event) throws SQLException {
        StudentRepository studentRepository = new StudentRepository();
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #993333;");
        vbox.setSpacing(20);
        TextField textField = new TextField();
        textField.setPrefHeight(20);
        textField.setPrefWidth(vbox.getPrefWidth());
        textField.setPromptText("Enter Student UID");
        Button delBtn = new Button();
        delBtn.setText("Delete Student");
        delBtn.setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Confirm delete student?");
            alert.showAndWait();
            if (alert.getResult() != ButtonType.OK) {
                return;
            }
            String student = textField.getText().trim();
            System.out.println("Deleting student " + student + " ...");
            try {
                Alert res = studentRepository.deleteStudent(student);
                res.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        vbox.setPrefWidth(hsDisplay.getPrefWidth());
        vbox.setPrefHeight(hsDisplay.getPrefHeight());
        vbox.getChildren().addAll(textField, delBtn);

        vbox.setPadding(new Insets(((hsDisplay.getPrefHeight() / 2) - (textField.getPrefHeight() + delBtn.getPrefHeight()) / 2) - 10, 50, 50, 50));

        hsDisplay.getChildren().clear();
        hsDisplay.getChildren().addAll(vbox);
    }

    public void onUpdateCourse(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/updateCourseScene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onUpdateStudent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/updateStudentScene.fxml"));
        Parent root = loader.load();
        UpdateStudentSceneController updateStudentSceneController = loader.getController();
        try {
            updateStudentSceneController.render(new DepartmentRepository().getAllDepartments());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onViewTeachersRatingsLessThanVal(ActionEvent event) {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #993333;");
        vbox.setSpacing(20);
        TextField textField = new TextField();
        textField.setPrefHeight(20);
        textField.setPrefWidth(vbox.getPrefWidth());
        textField.setPromptText("Enter Rating (upper bound)");
        Button delBtn = new Button();
        delBtn.setText("Get Teachers");
        delBtn.setOnMouseClicked(e -> {
            hsDisplay.getChildren().clear();
            try {
                int ubVal = Integer.parseInt(textField.getText());
                TableView<Teacher> table = new TableView<>();
                ObservableList<Teacher> teachers = new TeacherRepository().getTeachersWithUB(ubVal);

                TableColumn codeCol = new TableColumn("Code");
                codeCol.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
                TableColumn nameCol = new TableColumn("Name");
                nameCol.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
                TableColumn deptCol = new TableColumn("Deptt.");
                deptCol.setCellValueFactory(new PropertyValueFactory<>("depttId"));
                TableColumn ratingsCol = new TableColumn("Ratings");
                ratingsCol.setCellValueFactory(new PropertyValueFactory<>("ratings"));
                TableColumn nofCol = new TableColumn("No. of feedbacks");
                nofCol.setCellValueFactory(new PropertyValueFactory<>("numOfFeedbacks"));

                table.setItems(teachers);
                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                    sideBarAccordion.setExpandedPane(studentZone);
                    miniDisplay.setText(Helpers.formattedTeacher(newValue));
                });
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                table.getColumns().addAll(codeCol, nameCol, deptCol, ratingsCol, nofCol);
                table.prefHeightProperty().bind(hsDisplay.heightProperty());
                table.prefWidthProperty().bind(hsDisplay.widthProperty());

                hsDisplay.getChildren().clear();
                hsDisplay.getChildren().addAll(table);
            } catch (Exception exception) {
                Alert errAlert = new Alert(Alert.AlertType.ERROR);
                errAlert.setContentText("Wrong input, please try again!");
                errAlert.showAndWait();
                exception.printStackTrace();
            }
        });

        vbox.setPrefWidth(hsDisplay.getPrefWidth());
        vbox.setPrefHeight(hsDisplay.getPrefHeight());
        vbox.getChildren().addAll(textField, delBtn);

        vbox.setPadding(new Insets(((hsDisplay.getPrefHeight() / 2) - (textField.getPrefHeight() + delBtn.getPrefHeight()) / 2) - 10, 50, 50, 50));

        hsDisplay.getChildren().clear();
        hsDisplay.getChildren().addAll(vbox);
    }

    public void onViewTeachersRatingsMoreThanVal(ActionEvent event) {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #993333;");
        vbox.setSpacing(20);
        TextField textField = new TextField();
        textField.setPrefHeight(20);
        textField.setPrefWidth(vbox.getPrefWidth());
        textField.setPromptText("Enter Rating (upper bound)");
        Button delBtn = new Button();
        delBtn.setText("Get Teachers");
        delBtn.setOnMouseClicked(e -> {
            hsDisplay.getChildren().clear();
            try {
                int lbVal = Integer.parseInt(textField.getText());
                TableView<Teacher> table = new TableView<>();
                ObservableList<Teacher> teachers = new TeacherRepository().getTeachersWithLB(lbVal);

                TableColumn codeCol = new TableColumn("Code");
                codeCol.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
                TableColumn nameCol = new TableColumn("Name");
                nameCol.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
                TableColumn deptCol = new TableColumn("Deptt.");
                deptCol.setCellValueFactory(new PropertyValueFactory<>("depttId"));
                TableColumn ratingsCol = new TableColumn("Ratings");
                ratingsCol.setCellValueFactory(new PropertyValueFactory<>("ratings"));
                TableColumn nofCol = new TableColumn("No. of feedbacks");
                nofCol.setCellValueFactory(new PropertyValueFactory<>("numOfFeedbacks"));

                table.setItems(teachers);
                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                    sideBarAccordion.setExpandedPane(studentZone);
                    miniDisplay.setText(Helpers.formattedTeacher(newValue));
                });
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                table.getColumns().addAll(codeCol, nameCol, deptCol, ratingsCol, nofCol);
                table.prefHeightProperty().bind(hsDisplay.heightProperty());
                table.prefWidthProperty().bind(hsDisplay.widthProperty());

                hsDisplay.getChildren().clear();
                hsDisplay.getChildren().addAll(table);
            } catch (Exception exception) {
                Alert errAlert = new Alert(Alert.AlertType.ERROR);
                errAlert.setContentText("Wrong input, please try again!");
                errAlert.showAndWait();
                exception.printStackTrace();
            }
        });

        vbox.setPrefWidth(hsDisplay.getPrefWidth());
        vbox.setPrefHeight(hsDisplay.getPrefHeight());
        vbox.getChildren().addAll(textField, delBtn);

        vbox.setPadding(new Insets(((hsDisplay.getPrefHeight() / 2) - (textField.getPrefHeight() + delBtn.getPrefHeight()) / 2) - 10, 50, 50, 50));

        hsDisplay.getChildren().clear();
        hsDisplay.getChildren().addAll(vbox);
    }

    public void render(String imgUrl) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(imgUrl));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(hsDisplay.getPrefHeight());
        imageView.setFitWidth(hsDisplay.getPrefWidth());
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            new DepartmentRepository().getAllDepartments().forEach(dept -> {
                allDepts.put(dept.getDepttId(), dept.getDepttName());
                list.add(dept.getDepttName());
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        depttInp.setItems(list);
        hsDisplay.getChildren().addAll(imageView);
        sideBarAccordion.setExpandedPane(studentZone);
    }

    public void reRender(String imgUrl) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(imgUrl));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(hsDisplay.getPrefHeight());
        imageView.setFitWidth(hsDisplay.getPrefWidth());
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            new DepartmentRepository().getAllDepartments().forEach(dept -> {
                allDepts.put(dept.getDepttId(), dept.getDepttName());
                list.add(dept.getDepttName());
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        depttInp.setItems(list);
        hsDisplay.getChildren().addAll(imageView);
        sideBarAccordion.setExpandedPane(adminZone);
    }

    public void onAddDepartment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/addDepartmentScene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onAddTeacher(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/addTeacherScene.fxml"));
        Parent root = loader.load();
        AddTeacherSceneController addTeacherSceneController = loader.getController();
        DepartmentRepository departmentRepository = new DepartmentRepository();
        addTeacherSceneController.render(departmentRepository.getAllDepartments());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickCourseTeacherAllotment(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/courseTeacherAllotScene.fxml"));
        Parent root = loader.load();
        CourseTeacherAllotSceneController courseTeacherAllotSceneController = loader.getController();
        TeacherRepository teacherRepository = new TeacherRepository();
        CoursesRepository coursesRepository = new CoursesRepository();
        courseTeacherAllotSceneController.render(teacherRepository.getAllTeachers(), coursesRepository.getCourses());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickCourseDeptAllotment(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentfeedbackmanagementsystem/courseDeptAllotScene.fxml"));
        Parent root = loader.load();
        CourseDeptAllotSceneController courseDeptAllotSceneController = loader.getController();
        DepartmentRepository departmentRepository = new DepartmentRepository();
        CoursesRepository coursesRepository = new CoursesRepository();
        courseDeptAllotSceneController.render(coursesRepository.getCourses(), departmentRepository.getAllDepartments());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onViewTeachers(ActionEvent event) throws SQLException {
        if (hsDisplay.getChildren().size() > 0) {
            hsDisplay.getChildren().clear();
        }

        TeacherRepository teacherRepository = new TeacherRepository();
        TableView<Teacher> table = new TableView<>();
        ObservableList<Teacher> teachers = FXCollections.observableArrayList(teacherRepository.getAllTeachers());

        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
        TableColumn nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        TableColumn deptCol = new TableColumn<>("Department-ID");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("depttId"));
        TableColumn ratingsCol = new TableColumn<>("Ratings");
        ratingsCol.setCellValueFactory(new PropertyValueFactory<>("ratings"));
        TableColumn nofCol = new TableColumn<>("No. Of Feedbacks");
        nofCol.setCellValueFactory(new PropertyValueFactory<>("numOfFeedbacks"));

        table.setItems(teachers);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            sideBarAccordion.setExpandedPane(studentZone);
            miniDisplay.setText(Helpers.formattedTeacher(newValue));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(idCol, nameCol, deptCol, ratingsCol, nofCol);
        table.prefHeightProperty().bind(hsDisplay.heightProperty());
        table.prefWidthProperty().bind(hsDisplay.widthProperty());

        hsDisplay.getChildren().addAll(table);
    }

    public void onViewDepartments(ActionEvent event) throws SQLException {
        if (hsDisplay.getChildren().size() > 0) {
            hsDisplay.getChildren().clear();
        }

        DepartmentRepository departmentRepository = new DepartmentRepository();
        TableView<Department> table = new TableView<>();
        ObservableList<Department> departments = FXCollections.observableArrayList(departmentRepository.getAllDepartments());

        TableColumn idCol = new TableColumn("Deptt. ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("depttId"));
        TableColumn nameCol = new TableColumn<>("Deptt. Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("depttName"));

        table.setItems(departments);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            sideBarAccordion.setExpandedPane(studentZone);
            miniDisplay.setText(Helpers.formattedDepartment(newValue));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(idCol, nameCol);
        table.prefHeightProperty().bind(hsDisplay.heightProperty());
        table.prefWidthProperty().bind(hsDisplay.widthProperty());

        hsDisplay.getChildren().addAll(table);
    }

    public void onViewCourseTeachers(ActionEvent event) throws SQLException {
        if (hsDisplay.getChildren().size() > 0) {
            hsDisplay.getChildren().clear();
        }
        TableView<CourseTeachers> table = new TableView<>();
        ObservableList<CourseTeachers> courseTeachers = new CoursesRepository().getAllCourseTeachers();

        TableColumn cidCol = new TableColumn("Course Id");
        cidCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        TableColumn cnameCol = new TableColumn<>("Course Name");
        cnameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        TableColumn tidCol = new TableColumn<>("Teacher Id");
        tidCol.setCellValueFactory(new PropertyValueFactory<>("courseTeacherId"));
        TableColumn tnameCol = new TableColumn("Teacher Name");
        tnameCol.setCellValueFactory(new PropertyValueFactory<>("courseTeacherName"));

        table.setItems(courseTeachers);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            sideBarAccordion.setExpandedPane(studentZone);
            miniDisplay.setText(Helpers.formattedCT(newValue));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(cidCol, cnameCol, tidCol, tnameCol);
        table.prefHeightProperty().bind(hsDisplay.heightProperty());
        table.prefWidthProperty().bind(hsDisplay.widthProperty());

        hsDisplay.getChildren().addAll(table);
    }

    public void onViewDepartmentsCourses(ActionEvent event) throws SQLException {
        if (hsDisplay.getChildren().size() > 0) {
            hsDisplay.getChildren().clear();
        }
        TableView<DepartmentCourses> table = new TableView<>();
        ObservableList<DepartmentCourses> departmentCourses = new DepartmentRepository().getAllDepttCourses();

        TableColumn cidCol = new TableColumn("Course Id");
        cidCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        TableColumn cnameCol = new TableColumn<>("Course Name");
        cnameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        TableColumn didCol = new TableColumn<>("Deptt. Id");
        didCol.setCellValueFactory(new PropertyValueFactory<>("depttId"));
        TableColumn dnameCol = new TableColumn("Department");
        dnameCol.setCellValueFactory(new PropertyValueFactory<>("depttName"));
        TableColumn semCol = new TableColumn("Semester");
        semCol.setCellValueFactory(new PropertyValueFactory<>("semester"));

        table.setItems(departmentCourses);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            sideBarAccordion.setExpandedPane(studentZone);
            miniDisplay.setText(Helpers.formattedDC(newValue));
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(cidCol, cnameCol, didCol, dnameCol, semCol);
        table.prefHeightProperty().bind(hsDisplay.heightProperty());
        table.prefWidthProperty().bind(hsDisplay.widthProperty());

        hsDisplay.getChildren().addAll(table);
    }
}

