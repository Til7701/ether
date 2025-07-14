package de.holube.ether.generators.color;

import lombok.RequiredArgsConstructor;

import java.awt.*;

/**
 * A factory that generates colors based on the hue in the HSB color model.
 * The hue is calculated from the value, which is expected to be in the range [-1.0, 1.0].
 * Saturation and brightness are set to maximum (1.0).
 */
@RequiredArgsConstructor
public class HueRangeColorFactory implements RangeColorFactory {

    private final double saturation;
    private final double brightness;

    @Override
    public int create(double value) {
        float hue = (float) ((value + 1.0) / 2.0);
        return Color.HSBtoRGB(hue, (float) saturation, (float) brightness);
    }

}
