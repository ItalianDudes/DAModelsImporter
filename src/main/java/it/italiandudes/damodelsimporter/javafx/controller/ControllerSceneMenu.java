package it.italiandudes.damodelsimporter.javafx.controller;

import it.italiandudes.damodelsimporter.enums.CategoryName;
import it.italiandudes.damodelsimporter.javafx.Client;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

@SuppressWarnings("unused")
public final class ControllerSceneMenu {

    //Graphic Elements
    @FXML private TextField textFieldDAPath;
    @FXML private TextField textFieldModelPath;
    @FXML private ComboBox<String> comboBoxCategoryName;
    @FXML private TextField textFieldModelName;
    @FXML private TextField textFieldScale;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        comboBoxCategoryName.setItems(FXCollections.observableList(CategoryName.CATEGORY_NAMES));
        comboBoxCategoryName.getSelectionModel().selectFirst();
        comboBoxCategoryName.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {

            // Get the arrow button of the combo-box
            StackPane arrowButton = (StackPane) comboBoxCategoryName.lookup(".arrow-button");
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setBackground(Background.EMPTY);
                    if (empty || item == null) {
                        setText("");
                    } else {
                        setText(item);
                    }

                    // Set the background of the arrow also
                    if (arrowButton != null)
                        arrowButton.setBackground(getBackground());
                }
            };
        }, comboBoxCategoryName.valueProperty()));
    }

    // EDT

}
