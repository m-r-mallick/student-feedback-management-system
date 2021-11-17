package com.example.studentfeedbackmanagementsystem.DAO;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCourses {

    private String courseId;
    private String courseName;
    private String depttId;
    private String depttName;
    private Integer semester;

}
