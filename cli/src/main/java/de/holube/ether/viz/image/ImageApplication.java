package de.holube.ether.viz.image;

import de.holube.ether.viz.AbstractApplication;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;

public class ImageApplication extends AbstractApplication {

    private static ImageAppData data;

    public static void setup(ImageAppData data) {
        ImageApplication.data = data;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        setupStage();

        ImageView imageView = new ImageView(getImage());
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(data.image().getWidth());
        imageView.setFitHeight(data.image().getHeight());
        root.setCenter(imageView);

        stage.setTitle(data.title());
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
