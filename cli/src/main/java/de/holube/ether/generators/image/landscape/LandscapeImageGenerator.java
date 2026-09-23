package de.holube.ether.generators.image.landscape;

import de.holube.ether.commons.noise.OpenSimplex2S;
import de.holube.ether.generators.image.ImageGenerator;
import de.holube.ether.generators.image.ImageGeneratorResult;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class LandscapeImageGenerator implements ImageGenerator {

    private final int width;
    private final int height;
    private final long seed;
    private final double scale;
    private final int levels;
    private final double multi;

    @Override
    public ImageGeneratorResult generate() {
        double[][] heights = new double[width][height];
        MinMax minMax = sampleNoiseHeight(heights);
        normalizeHeights(heights, minMax.min(), minMax.max());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        IntStream.range(0, width)
                .parallel()
                .forEach(x -> IntStream.range(0, height)
                        .forEach(y -> {
                            double height = heights[x][y];
                            int color = createColor(height);
                            image.setRGB(x, y, color);
                        })
                );

        return new ImageGeneratorResult(image);
    }

    private MinMax sampleNoiseHeight(double[][] heights) {
        double min = 0;
        double max = 0;
        for (int i = 0; i < heights.length; i++) {
            int rowLength = heights[i].length;
            for (int j = 0; j < rowLength; j++) {
                double h = sampleHeight(i, j);
                if (h < min) min = h;
                else if (max < h) max = h;
                heights[i][j] = h;
            }
        }
        return new MinMax(min, max);
    }

    private record MinMax(double min, double max) {
    }

    private double sampleHeight(int x, int y) {
        double n = OpenSimplex2S.noise2(
                seed,
                x * scale,
                y * scale
        );

        for (int i = 1; i < levels; i++) {
            double levelMulti = multi * i;
            double value = OpenSimplex2S.noise2(
                    seed,
                    x * scale * levelMulti,
                    y * scale * levelMulti
            );
            n += value * levelMulti;
        }

        return n;
    }

    private void normalizeHeights(double[][] heights, double min, double max) {
        for (int i = 0; i < heights.length; i++) {
            int rowLength = heights[i].length;
            for (int j = 0; j < rowLength; j++) {
                double height = heights[i][j];
                
            }
        }
    }

    private int createColor(double height) {
        if (height < 0)
            return oceanColor(height);
        return terrainColor(height);
    }

    private int oceanColor(double height) {
        double clamped = Math.clamp(height, -1, 0);
        double positive = -clamped;
        return 255 - (int) (200.0 * positive);
    }

    private int terrainColor(double height) {
        double clamped = Math.clamp(height, 0, Double.MAX_VALUE);
        if (clamped < 0.1) {
            return new Color(255, 205, 134).getRGB();
        } else if (clamped < 0.95) {
            return 0;
        } else {
            return Color.WHITE.getRGB();
        }
    }

}
