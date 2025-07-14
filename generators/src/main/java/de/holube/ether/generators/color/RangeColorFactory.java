package de.holube.ether.generators.color;

/**
 * A factory interface for creating colors based on a double value.
 * This is typically used for generating colors in a range, such as for noise generation.
 */
public interface RangeColorFactory extends ColorFactory {

    /**
     * Creates a color based on the provided double value.
     * The value is expected to be in a range that the factory can interpret,
     * normally between -1.0 and 1.0, but this can vary based on the implementation.
     *
     * @param value the double value used to determine the color
     * @return an integer representing the color in RGB format
     */
    int create(double value);

    /**
     * Creates a color based on a range defined by minimum and maximum values,
     * and a specific value within that range.
     * The value is normalized to the range [-1.0, 1.0] before being passed to the create method.
     *
     * @param min   the minimum value of the range
     * @param max   the maximum value of the range
     * @param value the specific value within the range
     * @return an integer representing the color in RGB format
     */
    default int create(double min, double max, double value) {
        double normalizedValue = (value - min) / (max - min);
        return create(normalizedValue * 2.0 - 1.0);
    }

}
