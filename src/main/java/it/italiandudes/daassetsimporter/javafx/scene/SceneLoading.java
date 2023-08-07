package it.italiandudes.daassetsimporter.javafx.scene;

import it.italiandudes.daassetsimporter.DAAssetsImporter;
import it.italiandudes.daassetsimporter.javafx.JFXDefs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

@SuppressWarnings("unused")
public final class SceneLoading {

    //Scene Generator
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(DAAssetsImporter.Defs.Resources.get(JFXDefs.Resource.FXML.FXML_LOADING)));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}