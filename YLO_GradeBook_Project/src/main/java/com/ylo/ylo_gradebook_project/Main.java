package com.ylo.ylo_gradebook_project;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //Run Method for the LoginWindowController
    @Override
    public void start(Stage primaryStage) {

        WindowManager.openLoginWindow(primaryStage);
    }
}