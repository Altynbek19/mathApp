module org.example.mathapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.mathapp to javafx.fxml;
    exports org.example.mathapp;
}