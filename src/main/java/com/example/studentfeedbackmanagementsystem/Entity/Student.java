package com.example.studentfeedbackmanagementsystem.Entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student {

    private String rollNo;
    private String name;
    private String depttId;
    private Integer semester;

}
