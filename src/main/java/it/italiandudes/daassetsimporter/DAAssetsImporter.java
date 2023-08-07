package it.italiandudes.daassetsimporter;

import it.italiandudes.daassetsimporter.javafx.Client;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@SuppressWarnings("unused")
public final class DAAssetsImporter {

    // Attributes
    public static final JSONParser JSON_PARSER = new JSONParser();

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
        public static final String JAR_POSITION = new File(DAAssetsImporter.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();

        // Resources Location
        public static final class Resources {
            //Resource Getter
            public static URL get(@NotNull final String resourceConst) {
                return Objects.requireNonNull(DAAssetsImporter.class.getResource(resourceConst));
            }

            public static InputStream getAsStream(@NotNull final String resourceConst) {
                return Objects.requireNonNull(DAAssetsImporter.class.getResourceAsStream(resourceConst));
            }
        }

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
