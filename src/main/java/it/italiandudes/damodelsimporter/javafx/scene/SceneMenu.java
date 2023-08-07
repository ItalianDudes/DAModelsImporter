package it.italiandudes.damodelsimporter.javafx.scene;

import it.italiandudes.damodelsimporter.DAModelsImporter;
import it.italiandudes.damodelsimporter.javafx.JFXDefs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

@SuppressWarnings("unused")
public final class SceneMenu {

    //Scene Generator
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(DAModelsImporter.Defs.Resources.get(JFXDefs.Resource.FXML.FXML_MENU)));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}
