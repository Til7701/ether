package de.holube.ether.generators.color;

import lombok.RequiredArgsConstructor;

import java.awt.*;

@RequiredArgsConstructor
public class DualNoiseColorFactory implements NoiseColorFactory {

    private final Color color1;
    private final Color color2;

    @Override
    public int create(double noise) {
        noise = (noise + 1.0) / 2.0; // Normalize noise to range [0, 1]

        // Interpolate between color1 and color2 based on noise value
        int red = (int) (color1.getRed() * (1 - noise) + color2.getRed() * noise);
        int green = (int) (color1.getGreen() * (1 - noise) + color2.getGreen() * noise);
        int blue = (int) (color1.getBlue() * (1 - noise) + color2.getBlue() * noise);

        return (red << 16) | (green << 8) | blue;
    }

}
