module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jave.core;
    requires org.slf4j;

    opens org.example.controller to javafx.fxml;
    opens org.example.model to javafx.base;
    exports org.example;
}