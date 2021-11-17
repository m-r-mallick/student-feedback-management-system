#student-feedback-management-system
A student-teacher feedback management system for college/university students

#Technology Stack Used
→ Java 17<br/>
→ JavaFX 16<br/>
→ MySQL 8.0 (also compatible with MariaDB 10.5.12)<br/>
→ Additional libraries - JFoenix | ControlsFX<br/>

#Want to use this project?
Add below code block to package - com/example/studentfeedbackmanagementsystem/Misc_<br/>
```java
package com.example.studentfeedbackmanagementsystem.Misc;

public class Auth {

    public static final String dbName = <insert_database_name>;
    public static final String username = <insert_database_username>;
    public static final String password = <insert_database_password>;

    public static final String SECRET_KEY = "JavaStillTheKing";

}
```