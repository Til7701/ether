package de.holube.ether.generators.image.fractal;

import org.apache.commons.math3.complex.Complex;

public interface Fractal {

    FractalResult calculate(Complex input);

}
