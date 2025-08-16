package de.holube.ether.commons.math;

public record ComplexNumber(
        double real,
        double imaginary
) {

    private static final ComplexNumber ZERO = of(0.0, 0.0);

    public ComplexNumber {
        if (Double.isNaN(real) || Double.isNaN(imaginary)) {
            throw new IllegalArgumentException("Real and imaginary parts must not be NaN");
        }
    }

    public static ComplexNumber zero() {
        return ZERO;
    }

    public static ComplexNumber of(double real, double imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber add(ComplexNumber other) {
        return of(this.real + other.real, this.imaginary + other.imaginary);
    }

    public ComplexNumber sub(ComplexNumber other) {
        return of(this.real - other.real, this.imaginary - other.imaginary);
    }

    public ComplexNumber multiply(ComplexNumber other) {
        return of(
                this.real * other.real - this.imaginary * other.imaginary,
                this.real * other.imaginary + this.imaginary * other.real
        );
    }

    public ComplexNumber divide(ComplexNumber other) {
        double denominator = other.real * other.real + other.imaginary * other.imaginary;
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return of(
                (this.real * other.real + this.imaginary * other.imaginary) / denominator,
                (this.imaginary * other.real - this.real * other.imaginary) / denominator
        );
    }

    public ComplexNumber divide(double scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return of(this.real / scalar, this.imaginary / scalar);
    }

    public double abs() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

}
