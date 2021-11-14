package com.example.studentfeedbackmanagementsystem.Repository;

import com.example.studentfeedbackmanagementsystem.DAO.StudentCourseRating;
import com.example.studentfeedbackmanagementsystem.Entity.Course;
import com.example.studentfeedbackmanagementsystem.Entity.Student;
import com.example.studentfeedbackmanagementsystem.Misc.Auth;
import com.example.studentfeedbackmanagementsystem.Misc.SQLQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.HashMap;

public class CoursesRepository {

    private static Connection conn;

    private static StudentRepository studentRepository;

    public CoursesRepository() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Auth.dbName, Auth.username, Auth.password);
        studentRepository = new StudentRepository();
        System.out.println("Connected...");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(SQLQueries.CREATE_COURSE_TABLE_QUERY);
    }

    public HashMap<String, String> getAllCourses() throws SQLException {
        HashMap<String, String> courses = new HashMap<>();
        String query = "SELECT course_id, course_name FROM courses";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("course_id") + " - " + resultSet.getString("course_name"));
            courses.put(resultSet.getString("course_id"), resultSet.getString("course_name"));
        }
        return courses;
    }

    public boolean saveRatings(HashMap<String, Integer> ratings, Student student) {
        try {
            System.out.println(ratings);
            if (!studentRepository.isStudentPresentInDB(student)) {
                if (studentRepository.saveStudent(student)) {
                    System.out.println("Student added...");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Something went wrong, please try again!");
                    alert.showAndWait();
                    System.out.println("Error in adding student...");
                }
            }
            Statement stmt = conn.createStatement();
            int res = stmt.executeUpdate(SQLQueries.CREATE_SCR_TABLE_QUERY);
            if (res > 0) {
                System.out.println("Creating Table...");
            }
            ratings.forEach((k, v) -> {
                String query = "INSERT INTO student_course_ratings (course_id, student_id, teacher_id, deptt_id, ratings) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt;
                try {
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, k);
                    pstmt.setString(2, student.getRollNo());
                    pstmt.setString(3, new DepartmentRepository().getTeacherForCourseInDepartment(student.getDepttId(), k).getTeacherId());
                    pstmt.setString(4, student.getDepttId());
                    pstmt.setInt(5, v);
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        System.out.println("Feedback Added!");
                    }
                } catch (SQLException e) {
                    //TODO: Research for any possible way to include a condition check based on typeof(SQLException)
                    String newQuery = "SELECT * FROM student_course_ratings WHERE student_id = '" + student.getRollNo() + "' AND course_id = '" + k + "'";
                    try {
                        Statement newStmt = conn.createStatement();
                        ResultSet rs = newStmt.executeQuery(newQuery);
                        if (rs.next()) {
                            newQuery = "UPDATE student_course_ratings SET ratings = ? WHERE student_id = ? AND course_id = ?";
                            PreparedStatement newPstmt = conn.prepareStatement(newQuery);
                            newPstmt.setInt(1, v);
                            newPstmt.setString(2, student.getRollNo());
                            newPstmt.setString(3, k);
                            int result = newPstmt.executeUpdate();
                            if (result > 0) {
                                System.out.println("Feedback Updated!");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            new TeacherRepository().rateTeacher(ratings, student.getRollNo());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Alert addCourseToDb(Course course) throws SQLException {
        Alert result;
        String query = "SELECT * FROM courses WHERE course_id = '" + course.getCourseCode() + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            result = new Alert(Alert.AlertType.ERROR);
            result.setContentText("Course code already exists!");
            return result;
        }
        query = "INSERT INTO courses (course_id, course_name) VALUES (?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, course.getCourseCode());
        preparedStatement.setString(2, course.getCourseName());
        int res = preparedStatement.executeUpdate();
        if (res > 0) {
            result = new Alert(Alert.AlertType.INFORMATION);
            result.setContentText("Course added!");
            return result;
        }
        return new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again!");
    }

    public ObservableList<Course> getCourses() throws SQLException {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String query = "SELECT * FROM courses";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            Course course = new Course(resultSet.getString(1), resultSet.getString(2));
            courses.add(course);
        }
        return courses;
    }

    public ObservableList<String> getCoursesPerStudent(String uid) throws SQLException {
        ObservableList<String> courses = FXCollections.observableArrayList();
        String query = "SELECT course_id FROM student_course_ratings WHERE student_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, uid);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            courses.add(resultSet.getString(1));
        }
        return courses;
    }

    public ObservableList<StudentCourseRating> getSCRData() throws SQLException {
        ObservableList<StudentCourseRating> scrList = FXCollections.observableArrayList();
        String query = "SELECT * FROM student_course_ratings";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            StudentCourseRating scr = new StudentCourseRating();
            scr.setCourseCode(resultSet.getString(1));
            scr.setRollNo(resultSet.getString(2));
            scr.setTeacherId(resultSet.getString(3));
            scr.setDepartmentId(resultSet.getString(4));
            scr.setRating(resultSet.getInt(5));
            scrList.add(scr);
        }
        return scrList;
    }

    public Alert deleteCourse(String course) throws SQLException {
        Alert alert;
        String query = "DELETE FROM courses WHERE course_id = '" + course + "'";
        Statement stmt = conn.createStatement();
        int res = stmt.executeUpdate(query);
        if (res > 0) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Course Deleted!");
            return alert;
        }
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Something went wrong, please try again!");
        return alert;
    }

    public Alert updateCourse(String code, String cname) throws SQLException {
        Alert alert;
        String query = "UPDATE courses SET course_name = ? WHERE course_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, cname);
        pstmt.setString(2, code);
        int res = pstmt.executeUpdate();
        if (res == 0) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No course found for code " + code);
            return alert;
        }
        if (res > 0) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Course details updated!");
            return alert;
        }
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Something went wrong!");
        return alert;
    }

    public ObservableList<Course> getCoursesWithUB(int ubVal) throws SQLException {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String query = "SELECT * FROM courses WHERE course_ratings < " + ubVal;
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            Course course = new Course(resultSet.getString(1), resultSet.getString(2));
            courses.add(course);
        }
        return courses;
    }

    public ObservableList<Course> getCoursesWithLB(int lbVal) throws SQLException {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String query = "SELECT * FROM courses WHERE course_ratings >= " + lbVal;
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            Course course = new Course(resultSet.getString(1), resultSet.getString(2));
            courses.add(course);
        }
        return courses;
    }

    public Course getCourseById(String id) throws SQLException {
        String query = "SELECT * FROM courses WHERE course_id = '" + id + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return new Course(rs.getString(1), rs.getString(2));
        }
        return null;
    }

    public HashMap<String, String> getCoursesPerDepttPerSem(String depttId, Integer semester) throws SQLException {
        HashMap<String, String> courses = new HashMap<>();
        String query = "select courses.course_id, courses.course_name from department_courses join courses where department_courses.deptt_id = ? and department_courses.course_id = courses.course_id and semester = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, depttId);
        pstmt.setInt(2, semester);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            System.out.println("Courses for this semester : " + resultSet.getString("course_id") + " - " + resultSet.getString("course_name"));
            courses.put(resultSet.getString("course_id"), resultSet.getString("course_name"));
        }
        return courses;
    }
}
