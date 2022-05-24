module com.example.itmoprojectsmartwaiter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.itmoprojectsmartwaiter to javafx.fxml;
    exports com.example.itmoprojectsmartwaiter;
}