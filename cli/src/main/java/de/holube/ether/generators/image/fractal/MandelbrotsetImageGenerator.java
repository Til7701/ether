package de.holube.ether.generators.image.fractal;

import de.holube.ether.generators.color.RangeColorFactory;
import de.holube.ether.generators.image.ImageGenerator;
import de.holube.ether.generators.image.ImageGeneratorResult;
import lombok.RequiredArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class MandelbrotsetImageGenerator implements ImageGenerator {

    private final int imageWidth;
    private final int imageHeight;
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    private final int maxIterations;
    private final RangeColorFactory inColorFactory;
    private final RangeColorFactory outColorFactory;

    @Override
    public ImageGeneratorResult generate() {
        Mandelbrotset mandelbrotset = new Mandelbrotset(maxIterations);
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        IntStream.range(0, imageWidth)
                .parallel()
                .forEach(x -> {
                    for (int y = 0; y < imageHeight; y++) {
                        double real = xMin + (xMax - xMin) * x / imageWidth;
                        double imaginary = yMin + (yMax - yMin) * y / imageHeight;

                        FractalResult result = mandelbrotset.calculate(real, imaginary);

                        int color;
                        if (result.isInSet()) {
                            double sumOfSquares = result.sumOfSquares();
                            color = inColorFactory.create(0, 1.25, sumOfSquares);
                        } else {
                            int iterations = result.iterations();
                            color = outColorFactory.create(0, maxIterations, iterations);
                        }

                        image.setRGB(x, y, color);
                    }
                });

        return new ImageGeneratorResult(image);
    }

}
