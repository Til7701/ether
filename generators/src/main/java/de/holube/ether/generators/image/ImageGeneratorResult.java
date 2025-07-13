package de.holube.ether.generators.image;

import de.holube.ether.generators.GeneratorResult;

import java.awt.image.BufferedImage;

public record ImageGeneratorResult(
        BufferedImage result
) implements GeneratorResult<BufferedImage> {
}
