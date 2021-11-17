package com.example.studentfeedbackmanagementsystem.Misc;

import com.example.studentfeedbackmanagementsystem.DAO.CourseTeachers;
import com.example.studentfeedbackmanagementsystem.DAO.DepartmentCourses;
import com.example.studentfeedbackmanagementsystem.DAO.StudentCourseRating;
import com.example.studentfeedbackmanagementsystem.Entity.Course;
import com.example.studentfeedbackmanagementsystem.Entity.Department;
import com.example.studentfeedbackmanagementsystem.Entity.Student;
import com.example.studentfeedbackmanagementsystem.Entity.Teacher;

import java.util.Map;

public class Helpers {
    public static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (K key: map.keySet())
        {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }

    public static String formattedCourse(Course course) {
        return "Course Code : " + course.getCourseCode() + "\n" + "Course Name : " + course.getCourseName() + "\n";
    }

    public static String formattedStudent(Student student) {
        return "Roll No. : " + student.getRollNo() + "\n" + "Name : " + student.getName() + "\n" + "Semester : " + student.getSemester() + "\n";
    }

    public static String formattedSCR(StudentCourseRating studentCourseRating) {
        return "Subject Code : " + studentCourseRating.getCourseCode() + "\n" + "Student RNo. : " + AESUtils.encrypt(studentCourseRating.getRollNo(), Auth.SECRET_KEY) + "\n" + "Teacher Id : " + studentCourseRating.getTeacherId() + "\n" + "Department Id : " + studentCourseRating.getDepartmentId() + "\n" + "Rating : " + studentCourseRating.getRating() + "\n";
    }

    public static String formattedTeacher(Teacher teacher) {
        return "Teacher Id : " + teacher.getTeacherId() + "\n" + "Name : " + teacher.getTeacherName() + "\n" + "Department : " + teacher.getDepttId() + "\n";
    }

    public static String formattedDepartment(Department department) {
        return "Id : " + department.getDepttId() + "\n" + "Name : " + department.getDepttName() + "\n";
    }

    public static String formattedCT(CourseTeachers courseTeacher) {
        return "Course Id : " + courseTeacher.getCourseId() + "\n" + "Course Name : " + courseTeacher.getCourseName() + "\n" + "Teacher Id : " + courseTeacher.getCourseTeacherId() + "\n" + "Teacher Name : " + courseTeacher.getCourseTeacherName() + "\n";
    }

    public static String formattedDC(DepartmentCourses departmentCourse) {
        return "Course Id : " + departmentCourse.getCourseId() + "\n" + "Course Name : " + departmentCourse.getCourseName() + "\n" + "Deptt. Id : " + departmentCourse.getDepttId() + "\n" + "Department : " + departmentCourse.getDepttName() + "\n" + "Semester : " + departmentCourse.getSemester() + "\n";
    }
}

