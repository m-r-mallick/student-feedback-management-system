package com.example.studentfeedbackmanagementsystem.Repository;

import com.example.studentfeedbackmanagementsystem.DAO.DepartmentCourses;
import com.example.studentfeedbackmanagementsystem.Entity.Course;
import com.example.studentfeedbackmanagementsystem.Entity.Department;
import com.example.studentfeedbackmanagementsystem.Entity.Teacher;
import com.example.studentfeedbackmanagementsystem.Misc.Auth;
import com.example.studentfeedbackmanagementsystem.Misc.SQLQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepository {

    private static Connection conn;

    public DepartmentRepository() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Auth.dbName, Auth.username, Auth.password);
        new StudentRepository();
        new CoursesRepository();
        System.out.println("Connected...");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(SQLQueries.CREATE_DEPTT_TABLE_QUERY);
    }

    public List<Department> getAllDepartments() throws SQLException {
        List<Department> list = new ArrayList<>();
        String query = "SELECT * FROM departments";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            Department department =
                    new Department(resultSet.getString(1), resultSet.getString(2));
            list.add(department);
        }
        return list;
    }

    public Alert saveDepartment(Department department) throws SQLException {
        Alert alert;
        String query = "INSERT INTO departments (deptt_id, deptt_name) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, department.getDepttId());
        pstmt.setString(2, department.getDepttName());
        int res = pstmt.executeUpdate();
        if (res > 0) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Department saved!");
            return alert;
        }
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Something went wrong, please try again!");
        return alert;
    }

    public Alert allotCoursesToDeptt(String deptt, List<String> courses, int sem) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(SQLQueries.CREATE_CDS_TABLE_QUERY);
        preparedStatement.executeUpdate();
        Alert alert;
        int count = 0;
        String query = "INSERT INTO department_courses (course_id, deptt_id, semester) VALUES (?, ?, ?)";
        for (String course : courses) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, course);
            pstmt.setString(2, deptt);
            pstmt.setInt(3, sem);
            int res = pstmt.executeUpdate();
            if (res > 0) {
                count++;
            }
        }
        if (count != courses.size()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error while inserting some of the records!");
            return alert;
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Successfully allotted courses!");
        return alert;
    }

    public List<Course> getAllottedCoursesForSemester(String depttId, int sem) {
        List<Course> list = new ArrayList<>();
        String query = "SELECT course_id FROM department_courses WHERE deptt_id = ? AND semester = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, depttId);
            pstmt.setInt(2, sem);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                System.out.println(id);
                Course course = new CoursesRepository().getCourseById(id);
                if (course != null) {
                    System.out.println(course);
                    list.add(course);
                }
            }
            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Teacher getTeacherForCourseInDepartment(String depttId, String courseId) throws SQLException {
        String query = "SELECT teacher_id FROM course_teachers WHERE course_id = '" + courseId + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Teacher teacher = new TeacherRepository().getTeacherById(rs.getString(1));
            if (teacher.getDepttId().equals(depttId)) {
                return teacher;
            }
        }
        return null;
    }

    public ObservableList<DepartmentCourses> getAllDepttCourses() throws SQLException {
        ObservableList<DepartmentCourses> departmentCourses = FXCollections.observableArrayList();
        String query = "SELECT courses.course_id, courses.course_name, departments.deptt_id, departments.deptt_name, department_courses.semester FROM department_courses JOIN courses JOIN departments WHERE department_courses.course_id = courses.course_id AND department_courses.deptt_id = departments.deptt_id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            DepartmentCourses departmentCourse = new DepartmentCourses(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            departmentCourses.add(departmentCourse);
        }
        return departmentCourses;
    }
}
