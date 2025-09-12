package de.holube.ether.generators.image.fractal;

import de.holube.ether.commons.math.ComplexNumber;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Mandelbrotset implements Fractal {

    private final int maxIterations;

    @Override
    public FractalResult calculate(double real, double imaginary) {
        ComplexNumber c = ComplexNumber.of(real, imaginary);
        ComplexNumber z = ComplexNumber.zero();
        int iterations = 0;

        while (z.abs() <= 2 && iterations < maxIterations) {
            z = z.multiply(z).add(c);
            iterations++;
        }

        double sumOfSquares = z.real() * z.real() + z.imaginary() * z.imaginary();
        double innerGradiant = z.divide(sumOfSquares).sub(c.divide(sumOfSquares)).abs();

        return new FractalResult(z.abs() <= 2, iterations, innerGradiant);
    }

}
