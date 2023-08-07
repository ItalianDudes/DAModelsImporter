package it.italiandudes.damodelsimporter.javafx.controller;

import it.italiandudes.damodelsimporter.DAModelsImporter;
import it.italiandudes.damodelsimporter.enums.CategoryName;
import it.italiandudes.damodelsimporter.javafx.Client;
import it.italiandudes.damodelsimporter.javafx.alert.ErrorAlert;
import it.italiandudes.damodelsimporter.javafx.alert.InformationAlert;
import it.italiandudes.damodelsimporter.javafx.scene.SceneLoading;
import it.italiandudes.idl.common.FileHandler;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Predicate;

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
        Client.getStage().setResizable(false);
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
    @FXML
    private void handleOnDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
    }
    @FXML
    private void handleOnDragDropped(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            String path = event.getDragboard().getFiles().get(0).getAbsolutePath();
            ((TextField) event.getSource()).setText(path);
            event.setDropCompleted(true);
        } else {
            event.setDropCompleted(false);
        }
    }
    @FXML
    private void openGameDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Dungeon Alchemist Directory");
        directoryChooser.setInitialDirectory(new File(DAModelsImporter.Defs.JAR_POSITION).getParentFile());
        File gameDirectory;
        try {
            gameDirectory = directoryChooser.showDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            gameDirectory = directoryChooser.showDialog(Client.getStage().getScene().getWindow());
        }
        if(gameDirectory!=null && gameDirectory.exists() && gameDirectory.isDirectory()) {
            textFieldDAPath.setText(gameDirectory.getAbsolutePath());
        }
    }
    @FXML
    private void openModelFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .glb Model File");
        for (String extension : DAModelsImporter.Defs.MODEL_EXTENSIONS) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension, "*."+extension));
        }
        fileChooser.setInitialDirectory(new File(DAModelsImporter.Defs.JAR_POSITION).getParentFile());
        File modelFile;
        try {
            modelFile = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            modelFile = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if(modelFile!=null && modelFile.exists() && modelFile.isFile()) {
            textFieldModelPath.setText(modelFile.getAbsolutePath());
        }
    }
    @FXML
    private void importModel() {
        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());

        Service<Void> importModelService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {

                        File modelsDirectory = new File(textFieldDAPath.getText() + File.separator + "Dungeon Alchemist_Data" + File.separator + "StreamingAssets" + File.separator + "Models");
                        if (!modelsDirectory.exists() || !modelsDirectory.isDirectory()) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "INPUT ERROR", "The path to the Dungeon Alchemist installation directory isn't right or it doesn't have the proper directory structure");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        File modelFile = new File(textFieldModelPath.getText());
                        if (!modelFile.exists() || !modelFile.isFile() || Arrays.stream(DAModelsImporter.Defs.MODEL_EXTENSIONS).noneMatch(Predicate.isEqual(FileHandler.getFileExtension(modelFile)))) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "INPUT ERROR", "The path to the model doesn't exist, doesn't take to a file or the extension is not supported");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        String modelName = textFieldModelName.getText();
                        if (modelName.replace(" ", "").isEmpty()) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "INPUT ERROR", "The model name can't be empty");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        double scale;
                        try {
                            scale = Double.parseDouble(textFieldScale.getText());
                            if (scale <= 0) throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "INPUT ERROR", "The scale must be a decimal number greater than 0");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        File customModelsDirectory = new File(modelsDirectory.getAbsolutePath() + File.separator + DAModelsImporter.Defs.CUSTOM_MODELS_DIRECTORY_NAME);
                        if (!customModelsDirectory.exists()) {
                            //noinspection ResultOfMethodCallIgnored
                            customModelsDirectory.mkdirs();
                        }

                        File[] customModelDirectories = customModelsDirectory.listFiles(File::isDirectory);

                        int directoryNumber = 0;
                        if (customModelDirectories != null) {
                            directoryNumber = customModelDirectories.length;
                        }

                        File newModelDirectory = new File(customModelsDirectory.getAbsolutePath() + File.separator + directoryNumber);
                        if (newModelDirectory.exists()) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "IMPORT ERROR", "A model with this name already exists");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        File newModelFile = new File(newModelDirectory.getAbsolutePath() + File.separator + "model." + FileHandler.getFileExtension(modelFile));

                        try {
                            FileUtils.copyFile(modelFile, newModelFile);
                        } catch (IOException e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "IMPORT ERROR", "An error has occurred during model copy");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        Scanner fileReader;
                        try {
                            fileReader = new Scanner(new File(modelsDirectory.getAbsolutePath() + File.separator + DAModelsImporter.Defs.JSON_MODELS_FILE_NAME), "UTF-8");
                        } catch (FileNotFoundException e) {
                            Logger.log(e);
                            try {
                                FileUtils.deleteDirectory(newModelDirectory);
                            } catch (IOException ex) {
                                Logger.log(ex);
                            }
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "IMPORT ERROR", "Json models file not found");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        StringBuilder modelsFileBuilder = new StringBuilder();
                        StringBuilder newModelBuilder = new StringBuilder();
                        String buffer;
                        while (fileReader.hasNext()) {
                            buffer = fileReader.nextLine();
                            if (buffer.replace(" ", "").equals("]")) {
                                newModelBuilder.append("    {\n");
                                newModelBuilder.append("      \"name\":\"").append(modelName).append("\",\n");
                                newModelBuilder.append("      \"keywords\":[],\n");
                                newModelBuilder.append("      \"categoryName\":\"").append(comboBoxCategoryName.getSelectionModel().getSelectedItem()).append("\",\n");
                                newModelBuilder.append("      \"path\":\"").append(DAModelsImporter.Defs.CUSTOM_MODELS_DIRECTORY_NAME + "/").append(directoryNumber).append("/").append("model.").append(FileHandler.getFileExtension(modelFile)).append("\",\n");
                                newModelBuilder.append("      \"template\":\"Token 1x1\",\n");
                                newModelBuilder.append("      \"scale\":").append(scale).append(",\n");
                                newModelBuilder.append("      \"verticalOffset\":0.0,\n");
                                newModelBuilder.append("      \"config\":null\n");
                                newModelBuilder.append("    },\n]");
                                modelsFileBuilder.append(newModelBuilder);
                            } else {
                                modelsFileBuilder.append(buffer).append('\n');
                            }
                        }
                        fileReader.close();

                        try {
                            FileWriter fileWriter = new FileWriter(modelsDirectory.getAbsolutePath() + File.separator + DAModelsImporter.Defs.JSON_MODELS_FILE_NAME);
                            fileWriter.append(modelsFileBuilder.toString());
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException e) {
                            Logger.log(e);
                            try {
                                FileUtils.deleteDirectory(newModelDirectory);
                            } catch (IOException ex) {
                                Logger.log(ex);
                            }
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "IMPORT ERROR", "An error has occurred during json model overwriting");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        Platform.runLater(() -> {
                            new InformationAlert("SUCCESS", "IMPORT SUCCESS", "Model imported successfully!");
                            Client.getStage().setScene(thisScene);
                        });

                        return null;
                    }
                };
            }
        };
        importModelService.start();
    }

}
