package de.holube.ether.generators.color;

/**
 * A color factory that generates grayscale colors based on a value in the range [-1, 1].
 * The value is mapped to a grayscale color where -1 corresponds to black and 1 corresponds to white.
 */
public class GrayscaleRangeColorFactory implements RangeColorFactory {

    @Override
    public int create(double value) {
        int r = (int) ((value + 1) * 127.5);
        int g = (int) ((value + 1) * 127.5);
        int b = (int) ((value + 1) * 127.5);
        return (r << 16) | (g << 8) | b;
    }

}
