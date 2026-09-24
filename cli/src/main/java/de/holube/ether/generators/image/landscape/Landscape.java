package de.holube.ether.generators.image.landscape;

public class Landscape {

    private final double[][] heights;
    private final double[][] water;

    public Landscape(int width, int height) {
        heights = new double[width][height];
        water = new double[width][height];
    }

    public void setHeights(Mapper mapper) {

    }

    public void normalizeHeights(double[][] heights, double min, double max) {
        for (int i = 0; i < heights.length; i++) {
            int rowLength = heights[i].length;
            for (int j = 0; j < rowLength; j++) {
                double height = heights[i][j];
                // TODO
            }
        }
    }

    @FunctionalInterface
    public interface Mapper {

        Result map(int i, int j, double height, double water);

        record Result(
                double newHeight
        ) {
        }

    }

}
