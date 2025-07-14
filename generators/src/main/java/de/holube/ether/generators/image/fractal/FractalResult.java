package de.holube.ether.generators.image.fractal;

public record FractalResult(
        boolean isInSet,
        int iterations,
        double sumOfSquares
) {
}
