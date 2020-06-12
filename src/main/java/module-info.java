module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jave.core;

    opens org.example.controller to javafx.fxml;
    opens org.example.model to javafx.base;
    exports org.example;
}