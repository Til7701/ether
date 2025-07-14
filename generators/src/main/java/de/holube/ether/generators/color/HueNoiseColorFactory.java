package de.holube.ether.generators.color;

import java.awt.*;

public class HueNoiseColorFactory implements NoiseColorFactory {

    @Override
    public int create(double noise) {
        float hue = (float) ((noise + 1.0) / 2.0);
        float saturation = 1.0f;
        float brightness = 1.0f;

        return Color.HSBtoRGB(hue, saturation, brightness);
    }

}
