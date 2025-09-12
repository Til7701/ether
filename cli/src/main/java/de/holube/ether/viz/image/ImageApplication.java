package de.holube.ether.viz.image;

import de.holube.ether.viz.AbstractApplication;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;

public class ImageApplication extends AbstractApplication {

    private static ImageAppData data;

    private static Stage stage;

    public static void setup(ImageAppData data) {
        ImageApplication.data = data;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        ImageView imageView = new ImageView(getImage());
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(data.image().getWidth());
        imageView.setFitHeight(data.image().getHeight());
        Pane root = new Pane();
        // center image in page
        imageView.layoutXProperty().bind(root.widthProperty().subtract(imageView.fitWidthProperty()).divide(2));
        imageView.layoutYProperty().bind(root.heightProperty().subtract(imageView.fitHeightProperty()).divide(2));
        root.getChildren().add(imageView);
        Scene scene = new Scene(root);
        scene.setFill(BACKGROUND_COLOR);

        stage.setTitle(data.title());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private static Image getImage() {
        BufferedImage bf = data.image();
        WritableImage wr = new WritableImage(bf.getWidth(), bf.getHeight());
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < bf.getWidth(); x++) {
            for (int y = 0; y < bf.getHeight(); y++) {
                pw.setArgb(x, y, bf.getRGB(x, y));
            }
        }
        return wr;
    }

    public static void launchWindow() {
        Application.launch();
    }

}
