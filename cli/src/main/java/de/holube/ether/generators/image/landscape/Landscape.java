package de.holube.ether.generators.image.landscape;

class Landscape {

    private final double[][] height;
    private final double[][] waterLayer;

    Landscape(int width, int height) {
        this.height = new double[width][height];
        waterLayer = new double[width][height];
    }

    Cell get(int i, int j) {
        return new Cell(
                height[i][j],
                waterLayer[i][j]
        );
    }

    record Cell(
            double terrainHeight,
            double waterLayer
    ) {
    }

    void setHeight(Mapper mapper) {
        double[][] heights = this.height;
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < heights.length; i++) {
            double[] heightRow = heights[i];
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < heightRow.length; j++) {
                double height = heightRow[j];
                double water = waterLayerRow[j];
                Mapper.Result result = mapper.map(i, j, height, water);
                heightRow[j] = result.newHeight();
            }
        }
    }

    void scale(double min, double max) {
        final double targetDiff = max - min;
        MinMax minMax = minMaxTotal();
        final double totalDiff = minMax.max() - minMax.min();

        double[][] heights = this.height;
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < heights.length; i++) {
            double[] heightRow = heights[i];
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < heightRow.length; j++) {
                double height = heightRow[j];
                double water = waterLayerRow[j];

                double positiveHeight = height - minMax.min();
                double positiveWater = water - minMax.min();

                double terrainRatio = positiveHeight / totalDiff;
                double waterRatio = positiveWater / totalDiff;

                heightRow[j] = min + (terrainRatio * targetDiff);
                waterLayerRow[j] = min + (waterRatio * targetDiff);
            }
        }
    }

    MinMax minMaxTotal() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        double[][] heights = this.height;
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < heights.length; i++) {
            double[] heightRow = heights[i];
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < heightRow.length; j++) {
                double height = heightRow[j];
                double water = waterLayerRow[j];
                double totalHeight = height + water;
                if (min > totalHeight) min = totalHeight;
                if (max < totalHeight) max = totalHeight;
            }
        }

        return new MinMax(min, max);
    }

    record MinMax(double min, double max) {
    }

    @FunctionalInterface
    interface Mapper {

        Result map(int i, int j, double height, double water);

        record Result(
                double newHeight
        ) {
        }

    }

}
