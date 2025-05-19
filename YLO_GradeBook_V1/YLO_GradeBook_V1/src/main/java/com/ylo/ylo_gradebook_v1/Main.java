package com.ylo.ylo_gradebook_v1;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ViewLoadingManager.openMainWindow(primaryStage);
    }

}