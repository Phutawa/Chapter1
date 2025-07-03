module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens se233.chapter1 to javafx.fxml;
    exports se233.chapter1;
}