//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.library;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LibraryApplication extends Application {
    public LibraryApplication() {
    }

    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("Dashboard-view.fxml"));
            Scene scene = new Scene((Parent)fxmlLoader.load(), 913, 600);
            stage.setTitle("Library Application");
            stage.setScene(scene);
            stage.show();
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
