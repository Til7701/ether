package de.holube.ether.generators.color;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.List;

/**
 * A factory that generates colors by interpolating between two specified colors based on a noise value.
 * The noise value is expected to be in the range [-1.0, 1.0].
 */
@RequiredArgsConstructor
public class ManyRangeColorFactory implements RangeColorFactory {

    private final List<Color> colors;

    @Override
    public int create(double value) {
        value = (value + 1.0) / 2.0; // Normalize value to [0, 1]

        if (value <= 0) return colors.getFirst().getRGB();
        if (value >= 1) return colors.getLast().getRGB();

        double scaled = value * (colors.size() - 1);
        int index = (int) Math.floor(scaled);
        double fraction = scaled - index;

        Color c1 = colors.get(index);
        Color c2 = colors.get(index + 1);

        int r = (int) (c1.getRed() + fraction * (c2.getRed() - c1.getRed()));
        int g = (int) (c1.getGreen() + fraction * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue() + fraction * (c2.getBlue() - c1.getBlue()));

        return (r << 16) | (g << 8) | b;
    }

}
