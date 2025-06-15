package com.ylo.ylo_gradebook_v1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

import java.io.IOException;

public class MainWindow {

    // The main content pane where views will be loaded
    @FXML
    public AnchorPane contentPane;

    @FXML
    public void initialize() {
        try {
            loadLoginView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads the login view into the content pane
    public void loadLoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
        Parent loginView = loader.load();

        contentPane.getChildren().clear();
        contentPane.getChildren().add(loginView);

        AnchorPane.setTopAnchor(loginView, 0.0);
        AnchorPane.setBottomAnchor(loginView, 0.0);
        AnchorPane.setLeftAnchor(loginView, 0.0);
        AnchorPane.setRightAnchor(loginView, 0.0);


    }
}
