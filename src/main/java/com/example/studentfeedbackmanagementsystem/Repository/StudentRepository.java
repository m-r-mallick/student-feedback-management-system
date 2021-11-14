package com.example.studentfeedbackmanagementsystem.Repository;

import com.example.studentfeedbackmanagementsystem.Entity.Student;
import com.example.studentfeedbackmanagementsystem.Misc.Auth;
import com.example.studentfeedbackmanagementsystem.Misc.SQLQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class StudentRepository {

    private static Connection conn;

    public StudentRepository() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Auth.dbName, Auth.username, Auth.password);
        System.out.println("Connected...");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(SQLQueries.CREATE_STUDENT_TABLE_QUERY);
    }

    public boolean saveStudent(Student student) throws SQLException {
        String query = "INSERT INTO students (student_id, student_name, semester, deptt_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, student.getRollNo());
        pstmt.setString(2, student.getName());
        pstmt.setInt(3, student.getSemester());
        pstmt.setString(4, student.getDepttId());
        int res = pstmt.executeUpdate();
        return res > 0;
    }

    public boolean isStudentPresentInDB(Student student) throws SQLException {
        String query = "SELECT * FROM students WHERE student_id = '" + student.getRollNo() + "'";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next();
    }

    public Alert addStudentToDb(Student student) throws SQLException {
        Alert result;
        if (isStudentPresentInDB(student)) {
            result = new Alert(Alert.AlertType.ERROR);
            result.setContentText("Student already present in records!");
            return result;
        }
        saveStudent(student);
        result = new Alert(Alert.AlertType.INFORMATION);
        result.setContentText("Student added to database!");
        return result;
    }

    public ObservableList<Student> getStudents() throws SQLException {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String query = "SELECT * FROM students";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            Student student = new Student(resultSet.getString(1), resultSet.getString(2), resultSet.getString(4), resultSet.getInt(3));
            students.add(student);
        }
        return students;
    }

    public Alert deleteStudent(String student) throws SQLException {
        Alert alert;
        String query = "DELETE FROM students WHERE student_id = '" + student + "'";
        Statement stmt = conn.createStatement();
        int res = stmt.executeUpdate(query);
        if (res > 0) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Student Deleted!");
            return alert;
        }
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Something went wrong, please try again!");
        return alert;
    }

    public boolean updateStudent(String rno, Student student) throws SQLException {
        String query = "UPDATE students SET student_name = ?, semester = ?, deptt_id = ? WHERE student_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, student.getName());
        pstmt.setInt(2, student.getSemester());
        pstmt.setString(3, student.getDepttId());
        pstmt.setString(4, rno);
        int res = pstmt.executeUpdate();
        return res > 0;
    }

    public Student getStudentById(String student_id) throws SQLException {
        String query = "SELECT * FROM students WHERE student_id = '" + student_id + "'";
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            System.out.println("now inside condition");
            return new Student(rs.getString(1), rs.getString(2), rs.getString(4), rs.getInt(3));
        }
        return null;
    }
}
