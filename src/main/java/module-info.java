module com.example.studentfeedbackmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires lombok;

    opens com.example.studentfeedbackmanagementsystem to javafx.fxml;
    exports com.example.studentfeedbackmanagementsystem;
    exports com.example.studentfeedbackmanagementsystem.Controller;
    exports com.example.studentfeedbackmanagementsystem.Entity;
    exports com.example.studentfeedbackmanagementsystem.DAO;
    opens com.example.studentfeedbackmanagementsystem.Controller to javafx.fxml;
    exports com.example.studentfeedbackmanagementsystem.Misc;
    opens com.example.studentfeedbackmanagementsystem.Misc to javafx.fxml;
}