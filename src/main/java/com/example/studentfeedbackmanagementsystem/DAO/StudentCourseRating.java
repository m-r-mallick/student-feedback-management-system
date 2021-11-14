package com.example.studentfeedbackmanagementsystem.DAO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StudentCourseRating {
    private String courseCode;
    private String rollNo;
    private String teacherId;
    private String departmentId;
    private Integer rating;
}
