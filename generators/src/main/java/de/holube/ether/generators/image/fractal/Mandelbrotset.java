package de.holube.ether.generators.image.fractal;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.complex.Complex;

@RequiredArgsConstructor
public class Mandelbrotset implements Fractal {

    private final int maxIterations;

    @Override
    public FractalResult calculate(Complex c) {
        Complex z = Complex.ZERO;
        int iterations = 0;

        while (z.abs() <= 2 && iterations < maxIterations) {
            z = z.multiply(z).add(c);
            iterations++;
        }

        double sumOfSquares = z.getReal() * z.getReal() + z.getImaginary() * z.getImaginary();
        double innerGradiant = z.divide(sumOfSquares).subtract(c.divide(sumOfSquares)).abs();

        return new FractalResult(z.abs() <= 2, iterations, innerGradiant);
    }

}
