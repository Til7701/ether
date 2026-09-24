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
        Landscape landscape = new Landscape(width, height, seed);
        landscape.setTerrainHeight((i, j, _, _) -> new Landscape.Mapper.Result(sampleHeight(i, j)));
        landscape.scale(0.0, 1.5);
        landscape.fillWaterBelow(0.2);
        landscape.fancify(10);
        landscape.scale(0.0, 1.0);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        IntStream.range(0, width)
                .parallel()
                .forEach(x -> IntStream.range(0, height)
                        .forEach(y -> {
                            Landscape.Cell cell = landscape.get(x, y);
                            int color = createBlackWhiteColor(cell);
                            image.setRGB(x, y, color);
                        })
                );

        return new ImageGeneratorResult(image);
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

    private int createBlackWhiteColor(Landscape.Cell cell) {
        int value = Math.abs((int) (cell.terrainHeight() * 255.0));
        if (value > 255)
            value = 255;
        else if (value < 0)
            value = 0;
        int blue = value;
        if (cell.waterLayer() > 0.1) {
            value = 0;
            blue = 255;
        }
        return ((value & 0xFF) << 16)
                | ((value & 0xFF) << 8)
                | (blue & 0xFF);
    }

    private int createColourfulColor(Landscape.Cell cell) {
        if (cell.waterLayer() > 0)
            return oceanColor(cell);
        return terrainColor(cell);
    }

    private int oceanColor(Landscape.Cell cell) {
        double clamped = Math.clamp(cell.waterLayer(), -1, 0);
        double positive = -clamped;
        return 255 - (int) (200.0 * positive);
    }

    private int terrainColor(Landscape.Cell cell) {
        double clamped = Math.clamp(cell.terrainHeight(), 0, Double.MAX_VALUE);
        if (clamped < 0.1) {
            return new Color(255, 205, 134).getRGB();
        } else if (clamped < 0.95) {
            return 0;
        } else {
            return Color.WHITE.getRGB();
        }
    }

}
