module com.library {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires mysql.connector.j;
    requires fontawesomefx;
    requires javafx.media;
    requires java.net.http;
    requires java.smartcardio;
    requires org.json;
    requires com.jfoenix;
    requires com.google.gson;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;

    opens com.library to javafx.fxml;
    exports com.library;
    exports com.library.admin;
    opens com.library.admin to javafx.fxml;
    exports com.library.Controller;
    opens com.library.Controller to javafx.fxml;
}