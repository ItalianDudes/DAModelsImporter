package it.italiandudes.damodelsimporter;

import it.italiandudes.damodelsimporter.javafx.Client;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@SuppressWarnings("unused")
public final class DAModelsImporter {

    // Main Method
    public static void main(String[] args) {

        // Initializing the logger
        try {
            Logger.init();
        } catch (IOException e) {
            Logger.log("An error has occurred during Logger initialization, exit...");
        }

        // Configure the shutdown hooks
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));

        // Start the client
        Client.start(args);

    }

    // Defs
    public static final class Defs {

        // Jar App Position
        public static final String JAR_POSITION = new File(DAModelsImporter.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();

        // Resources Location
        public static final class Resources {
            //Resource Getter
            public static URL get(@NotNull final String resourceConst) {
                return Objects.requireNonNull(DAModelsImporter.class.getResource(resourceConst));
            }

            public static InputStream getAsStream(@NotNull final String resourceConst) {
                return Objects.requireNonNull(DAModelsImporter.class.getResourceAsStream(resourceConst));
            }
        }

        // Model Extensions
        public static final String[] MODEL_EXTENSIONS = new String[]{
                "glb"
        };

        // Custom Models Directory
        public static final String CUSTOM_MODELS_DIRECTORY_NAME = "Custom_Models";

        // JSON Models File
        public static final String JSON_MODELS_FILE_NAME = "models.json";

        // JSON
        public static final class JSON {

            // Object Type Identifier
            public static final String JSON_OBJECT_TYPE = "object-type";

            public static final class ObjectType {
                public static final int SIMPLE_MESSAGE = 1;
            }

            public static final class SimpleMessage {
                public static final String FIELD_TITLE = "title";
                public static final String FIELD_MESSAGE = "message";
            }
        }
    }

}
