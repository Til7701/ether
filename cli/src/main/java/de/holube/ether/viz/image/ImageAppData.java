package de.holube.ether.viz.image;

import java.awt.image.BufferedImage;

public record ImageAppData(
        String title,
        BufferedImage image
) {
}
