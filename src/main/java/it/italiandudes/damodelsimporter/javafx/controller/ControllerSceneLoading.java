package it.italiandudes.damodelsimporter.javafx.controller;

import it.italiandudes.damodelsimporter.javafx.Client;
import javafx.fxml.FXML;

@SuppressWarnings("unused")
public final class ControllerSceneLoading {

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
    }
}