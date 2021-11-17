package com.example.studentfeedbackmanagementsystem.DAO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CourseTeachers {

    private String courseId;
    private String courseName;
    private String courseTeacherId;
    private String courseTeacherName;

}
