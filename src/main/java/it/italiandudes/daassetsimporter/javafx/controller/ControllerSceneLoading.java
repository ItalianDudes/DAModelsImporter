package it.italiandudes.daassetsimporter.javafx.controller;

import it.italiandudes.daassetsimporter.javafx.Client;
import javafx.fxml.FXML;

@SuppressWarnings("unused")
public final class ControllerSceneLoading {

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
    }
}