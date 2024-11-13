module com.library {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires fontawesomefx;
    requires javafx.media;
    requires org.json;
    requires java.smartcardio;

    opens com.library to javafx.fxml;
    exports com.library;
}