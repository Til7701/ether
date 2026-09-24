package de.holube.ether.generators.image.landscape;

import java.util.Random;

class Landscape {

    private static final double RAIN_DROP_PER_PIXEL = 0.01;
    private static final double RAIN_DROP_SIZE = 0.001;
    private static final double VERSICKER_SIZE = 0.0005;

    private final int width;
    private final int height;

    private final double[][] terrainHeight;
    private final double[][] waterLayer;

    private final Random random;

    Landscape(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.terrainHeight = new double[width][height];
        this.waterLayer = new double[width][height];
        random = new Random(seed);
    }

    Cell get(int i, int j) {
        return new Cell(
                terrainHeight[i][j],
                waterLayer[i][j]
        );
    }

    record Cell(
            double terrainHeight,
            double waterLayer
    ) {
    }

    void setTerrainHeight(Mapper mapper) {
        double[][] terrainHeight = this.terrainHeight;
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < terrainHeight.length; i++) {
            double[] terrainHeightRow = terrainHeight[i];
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < terrainHeightRow.length; j++) {
                double terrain = terrainHeightRow[j];
                double water = waterLayerRow[j];
                Mapper.Result result = mapper.map(i, j, terrain, water);
                terrainHeightRow[j] = result.newHeight();
            }
        }
    }

    void scale(double min, double max) {
        final double targetDiff = max - min;
        MinMax minMax = minMaxHeight();
        final double totalDiff = minMax.max() - minMax.min();

        double[][] terrainHeight = this.terrainHeight;
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < terrainHeight.length; i++) {
            double[] terrainHeightRow = terrainHeight[i];
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < terrainHeightRow.length; j++) {
                double terrain = terrainHeightRow[j];
                double water = waterLayerRow[j];

                double positiveHeight = terrain - minMax.min();
                double positiveWater = water - minMax.min();

                double terrainRatio = positiveHeight / totalDiff;
                double waterRatio = positiveWater / totalDiff;

                terrainHeightRow[j] = min + (terrainRatio * targetDiff);
                waterLayerRow[j] = min + (waterRatio * targetDiff);
            }
        }
    }

    MinMax minMaxHeight() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double[] terrainHeightRow : this.terrainHeight) {
            for (double terrain : terrainHeightRow) {
                if (min > terrain) min = terrain;
                if (max < terrain) max = terrain;
            }
        }

        return new MinMax(min, max);
    }

    void fillWaterBelow(double waterLevel) {
        double[][] terrainHeight = this.terrainHeight;
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < terrainHeight.length; i++) {
            double[] terrainHeightRow = terrainHeight[i];
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < terrainHeightRow.length; j++) {
                double terrain = terrainHeightRow[j];
                if (terrain < waterLevel) {
                    waterLayerRow[j] = waterLevel - terrain;
                }
            }
        }
    }

    void fancify(long iterations) {
        long drops = (long) (((long) width * height) * RAIN_DROP_PER_PIXEL);
        for (long i = 0; i < iterations; i++) {
            rain(drops);
            erode();
            equalizeWater();
            versicker();
        }
    }

    private void rain(long drops) {
        for (long i = 0; i < drops; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            waterLayer[x][y] += RAIN_DROP_SIZE;
        }
    }

    private void erode() {
        double[][] terrainHeight = this.terrainHeight;
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < terrainHeight.length; i++) {
            double[] terrainHeightRow = terrainHeight[i];
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < terrainHeightRow.length; j++) {
                double terrain = terrainHeightRow[j];
                double water = waterLayerRow[j];

                if (water > 0) {
                    // TODO
                }
            }
        }
    }

    private void equalizeWater() {
        double[][] terrainHeight = this.terrainHeight;
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < terrainHeight.length; i++) {
            double[] terrainHeightRow = terrainHeight[i];
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < terrainHeightRow.length; j++) {
                double terrain = terrainHeightRow[j];
                double water = waterLayerRow[j];

                // TODO
            }
        }
    }

    private void versicker() {
        double[][] waterLayer = this.waterLayer;
        for (int i = 0; i < terrainHeight.length; i++) {
            double[] waterLayerRow = waterLayer[i];
            for (int j = 0; j < waterLayerRow.length; j++) {
                waterLayerRow[j] -= VERSICKER_SIZE;
                if (waterLayerRow[j] < 0) {
                    waterLayerRow[j] = 0;
                }
            }
        }
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
