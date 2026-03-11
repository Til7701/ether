package de.holube.ether.viz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class AbstractApplication extends Application {

    protected static final Color BACKGROUND_COLOR = Color.color(0.1, 0.1, 0.1);

    protected Stage stage;
    protected BorderPane root;

    protected void setupStage() {
        root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, null, null)));

        Scene scene = new Scene(root);
        scene.setFill(BACKGROUND_COLOR);
        stage.setScene(scene);
    }

}
