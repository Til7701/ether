package de.holube.ether.generators.image.noise;

import de.holube.ether.generators.image.ImageGenerator;
import de.holube.ether.generators.image.ImageGeneratorResult;
import lombok.RequiredArgsConstructor;

import java.awt.image.BufferedImage;

@RequiredArgsConstructor
public class NoiseImageGenerator implements ImageGenerator {

    private static final double NOISE_SCALE = 0.01;

    private final long seed;
    private final int width;
    private final int height;

    @Override
    public ImageGeneratorResult generate() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double n = OpenSimplex2S.noise2(
                        seed,
                        x * NOISE_SCALE,
                        y * NOISE_SCALE
                );
                System.out.println("Noise value at (" + x + ", " + y + "): " + n);
                int color = (int) (n * 0x000F00);
                image.setRGB(x, y, color);
            }
        }

        return new ImageGeneratorResult(image);
    }

}
