package de.holube.ether.generators.color;

public class GrayscaleNoiseColorFactory implements NoiseColorFactory {

    @Override
    public int create(double noise) {
        int r = (int) ((noise + 1) * 127.5);
        int g = (int) ((noise + 1) * 127.5);
        int b = (int) ((noise + 1) * 127.5);
        return (r << 16) | (g << 8) | b;
    }

}
