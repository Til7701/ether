package de.holube.ether.viz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class AbstractApplication extends Application {

    protected static final Color HEADER_COLOR = Color.color(0.05, 0.05, 0.05);
    protected static final Color BACKGROUND_COLOR = Color.color(0.1, 0.1, 0.1);

    protected static Stage stage;
    protected BorderPane root;

    protected void setupStage() {
        stage.initStyle(StageStyle.EXTENDED);

        HeaderBar headerBar = new HeaderBar();
        Label titleLabel = new Label();
        titleLabel.textProperty().bind(stage.titleProperty());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        HeaderBar.setDragType(titleLabel, HeaderDragType.DRAGGABLE_SUBTREE);
        headerBar.setCenter(titleLabel);
        headerBar.setBackground(new Background(new BackgroundFill(HEADER_COLOR, null, null)));
        root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, null, null)));
        root.setTop(headerBar);

        Scene scene = new Scene(root);
        scene.setFill(BACKGROUND_COLOR);
        stage.setScene(scene);
    }

}
