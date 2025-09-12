package de.holube.ether.generators.color;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.List;

/**
 * A factory that generates colors by selecting from a predefined list of colors based on a noise value.
 * The noise value is expected to be in the range [-1.0, 1.0].
 */
@RequiredArgsConstructor
public class ManySharpRangeColorFactory implements RangeColorFactory {

    private final List<Color> colors;

    @Override
    public int create(double value) {
        // Normalize the value to the range [0, 1]
        double normalizedValue = (value + 1.0) / 2.0;
        int index = (int) (normalizedValue * (colors.size()));
        index = Math.max(0, Math.min(index, colors.size() - 1)); // Clamp index to valid range

        return colors.get(index).getRGB();
    }

}
