module com.example.java_final_project_javafx {
    requires java.net.http;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.text;

    opens com.example.java_final_project_javafx to javafx.fxml;
    exports com.example.java_final_project_javafx;
}