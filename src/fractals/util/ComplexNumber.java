package fractals.util;

import java.math.BigDecimal;

public final class ComplexNumber {
    private double re;
    private double im;

    public ComplexNumber(BigDecimal re, BigDecimal im) {
        this(re.doubleValue(), im.doubleValue());
    }

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public double abs() {
        return Math.hypot(re, im);
    }

    public double absSqr() {
        return re * re + im * im;
    }

    public void add(ComplexNumber b) {
        re += b.re;
        im += b.im;
    }

    public void add(double re, double im) {
        this.re += re;
        this.im += im;
    }

    public void sqr() {
        double real = re * re - im * im;
        im = re * im + im * re;
        re = real;
    }

    public void sqr(int exp) {
        double bre = re;
        double bim = im;
        for (int i = 0; i < exp - 1; i++) {
            mul(bre, bim);
        }
    }

    public void mul(ComplexNumber b) {
        double real = re * b.re - im * b.im;
        double imag = re * b.im + im * b.re;
        re = real;
        im = imag;
    }

    public void mul(double bre, double bim) {
        double real = re * bre - im * bim;
        im = re * bim + im * bre;
        re = real;
    }

    public void div(double dre, double dim) {
        double r = (re * dre + im * dim) / (dre * dre + dim * dim);
        double i = (im * dre - re * dim) / (dre * dre + dim * dim);
        re = r;
        im = i;
    }

}
