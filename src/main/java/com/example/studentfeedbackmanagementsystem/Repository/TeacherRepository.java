package com.example.studentfeedbackmanagementsystem.Repository;

import com.example.studentfeedbackmanagementsystem.Entity.Student;
import com.example.studentfeedbackmanagementsystem.Entity.Teacher;
import com.example.studentfeedbackmanagementsystem.Misc.Auth;
import com.example.studentfeedbackmanagementsystem.Misc.SQLQueries;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeacherRepository {

    private static Connection conn;

    public TeacherRepository() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Auth.dbName, Auth.username, Auth.password);
        new StudentRepository();
        new CoursesRepository();
        System.out.println("Connected...");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(SQLQueries.CREATE_TEACHER_TABLE_QUERY);
    }

    public Alert saveTeacher(Teacher teacher) throws SQLException {
        Alert alert;
        String query = "INSERT INTO teachers (teacher_id, teacher_name, deptt_id) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, teacher.getTeacherId());
        pstmt.setString(2, teacher.getTeacherName());
        pstmt.setString(3, teacher.getDepttId());
        int res = pstmt.executeUpdate();
        if (res > 0) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Teacher details saved!");
            return alert;
        }
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Something went wrong, please try again!");
        return alert;
    }

    public List<Teacher> getAllTeachers() throws SQLException {
        List<Teacher> list = new ArrayList<>();
        String query = "SELECT * FROM teachers";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            Teacher teacher =
                    new Teacher(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDouble(4), resultSet.getInt(5));
            list.add(teacher);
        }
        return list;
    }

    public boolean validateCourseAssignment(String teacher, String course, int sem) {
        int res = 0;
        int insertRes = 0;
        String query;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQLQueries.CREATE_CTS_TABLE_QUERY);
            query = "SELECT * FROM department_courses WHERE course_id = ? AND deptt_id = ? AND semester = ?";
            PreparedStatement pstmt1 = conn.prepareStatement(query);
            pstmt1.setString(1, course);
            pstmt1.setString(2, getTeacherById(teacher).getDepttId());
            pstmt1.setInt(3, sem);
            boolean cdsExists = pstmt1.executeQuery().next();
            if (!cdsExists) {
                query = "INSERT INTO department_courses (course_id, deptt_id, semester) VALUES (?, ?, ?)";
                PreparedStatement pstmt2 = conn.prepareStatement(query);
                pstmt2.setString(1, course);
                pstmt2.setString(2, getTeacherById(teacher).getDepttId());
                pstmt2.setInt(3, sem);
                insertRes = pstmt2.executeUpdate();
            }
            if (insertRes > 0 || cdsExists) {
                query = "INSERT INTO course_teachers (course_id, teacher_id, semester) VALUES (?, ?, ?)";
                PreparedStatement pstmt3 = conn.prepareStatement(query);
                pstmt3.setString(1, course);
                pstmt3.setString(2, teacher);
                pstmt3.setInt(3, sem);
                res = pstmt3.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res > 0;
    }

    public Teacher getTeacherById (String id) throws SQLException {
        String query = "SELECT * FROM teachers WHERE teacher_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new Teacher(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5));
        }
        return null;
    }

    public void rateTeacher(HashMap<String, Integer> ratings, String studentId) throws SQLException {
        Student student = new StudentRepository().getStudentById(studentId);
        List<Teacher> teachers = new ArrayList<>();

        ratings.keySet().forEach(course -> {
            try {
                teachers.add(new DepartmentRepository().getTeacherForCourseInDepartment(student.getDepttId(), course));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        teachers.forEach(teacher -> {
            int rating = 0, count = 0;
            String query = "SELECT ratings FROM student_course_ratings WHERE teacher_id = '" + teacher.getTeacherId() + "'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    int currRating = rs.getInt(1);
                    count++;
                    rating += currRating;
                }
                double finalAvgRating = (double) rating / count;
                query = "UPDATE teachers SET ratings = ?, num_of_feedbacks = ? WHERE teacher_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setDouble(1, finalAvgRating);
                pstmt.setInt(2, count);
                pstmt.setString(3, teacher.getTeacherId());
                int res = pstmt.executeUpdate();
                if (res > 0) {
                    System.out.println("Success");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
