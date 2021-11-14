package com.example.studentfeedbackmanagementsystem.Misc;

public class SQLQueries {
    public static final String CREATE_COURSE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS courses
            (course_id varchar(20),
            course_name varchar(255) UNIQUE,
            course_ratings decimal(2,1) DEFAULT 0.0,
            num_of_feedbacks int(11) DEFAULT 0,
            PRIMARY KEY (course_id))
            """;
    public static final String CREATE_STUDENT_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS students
            (student_id varchar(20),
            student_name varchar(255) NOT NULL,
            semester int(11),
            deptt_id varchar(20) NOT NULL,
            PRIMARY KEY (student_id),
            FOREIGN KEY (deptt_id) REFERENCES departments(deptt_id) ON DELETE CASCADE ON UPDATE CASCADE)
            """;
    public static final String CREATE_SCR_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS student_course_ratings
            (course_id varchar(20),
            student_id varchar(20),
            teacher_id varchar(20),
            deptt_id varchar(20),
            ratings int,
            PRIMARY KEY (course_id, student_id),
            FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (deptt_id) REFERENCES departments(deptt_id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE ON UPDATE CASCADE)
            """;
    public static final String CREATE_TEACHER_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS teachers
            (teacher_id varchar(20),
            teacher_name varchar(255) NOT NULL,
            deptt_id varchar(20),
            ratings decimal(2, 1) DEFAULT 0.0,
            num_of_feedbacks int DEFAULT 0,
            PRIMARY KEY (teacher_id),
            FOREIGN KEY (deptt_id) REFERENCES departments(deptt_id) ON DELETE CASCADE ON UPDATE CASCADE)
            """;
    public static final String CREATE_CTS_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS course_teachers
            (course_id varchar(20),
            teacher_id varchar(20),
            semester int,
            FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE ON UPDATE CASCADE,
            UNIQUE ct_key(course_id, teacher_id),
            UNIQUE ts_key(teacher_id, semester),
            UNIQUE cts_key(course_id, teacher_id, semester))
            """;
    public static final String CREATE_DEPTT_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS departments
            (deptt_id varchar(20),
            deptt_name varchar(255) NOT NULL UNIQUE,
            PRIMARY KEY (deptt_id))
            """;
    public static final String CREATE_CDS_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS department_courses
            (course_id varchar(20),
            deptt_id varchar(20),
            semester int,
            FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (deptt_id) REFERENCES departments(deptt_id) ON DELETE CASCADE ON UPDATE CASCADE,
            UNIQUE cds_key(course_id, deptt_id, semester),
            UNIQUE cd_key(course_id, deptt_id))
            """;

    public SQLQueries() {
        System.out.println("Creating tables...");
    }
}
