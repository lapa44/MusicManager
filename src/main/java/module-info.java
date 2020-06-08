module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.example.controller to javafx.fxml;
    opens org.example.model to javafx.base;
    exports org.example;
}