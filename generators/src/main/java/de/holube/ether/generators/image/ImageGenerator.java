package de.holube.ether.generators.image;

import de.holube.ether.generators.Generator;

import java.awt.image.BufferedImage;

public interface ImageGenerator extends Generator<BufferedImage> {

    ImageGeneratorResult generate();

}
