package com.example.studentfeedbackmanagementsystem.Entity;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Teacher {

    private String teacherId;
    private String teacherName;
    private String depttId;
    private Double ratings;
    private Integer numOfFeedbacks;

}
