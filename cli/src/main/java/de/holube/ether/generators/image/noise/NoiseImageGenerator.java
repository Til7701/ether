package de.holube.ether.generators.image.noise;

import de.holube.ether.generators.color.RangeColorFactory;
import de.holube.ether.generators.image.ImageGenerator;
import de.holube.ether.generators.image.ImageGeneratorResult;
import lombok.RequiredArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class NoiseImageGenerator implements ImageGenerator {

    private final int width;
    private final int height;
    private final long seed;
    private final double scale;
    private final RangeColorFactory colorFactory;

    @Override
    public ImageGeneratorResult generate() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        IntStream.range(0, width)
                .parallel()
                .forEach(x -> IntStream.range(0, height)
                        .forEach(y -> shader(x, y, image))
                );

        return new ImageGeneratorResult(image);
    }

    private void shader(int x, int y, BufferedImage image) {
        double n = OpenSimplex2S.noise2(
                seed,
                x * scale,
                y * scale
        );
        int color = colorFactory.create(n);
        image.setRGB(x, y, color);
    }

}
