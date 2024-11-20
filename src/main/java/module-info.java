module com.library {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires mysql.connector.j;
    requires fontawesomefx;
    requires javafx.media;
    requires java.net.http;
    requires java.smartcardio;
    requires org.json;
    requires com.jfoenix;
    requires com.google.gson;
    requires java.sql;

    opens com.library to javafx.fxml;
    exports com.library;
    exports com.library.admin;
    opens com.library.admin to javafx.fxml;
}