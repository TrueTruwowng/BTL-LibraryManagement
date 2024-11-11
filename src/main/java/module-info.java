module com.library {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires fontawesomefx;
    requires java.sql;
    requires java.desktop;
    requires java.smartcardio;

    opens com.library to javafx.fxml;
    exports com.library;
}