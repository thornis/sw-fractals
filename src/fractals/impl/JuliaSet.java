package fractals.impl;

import fractals.RasterFractal;
import fractals.util.ComplexNumber;

import java.math.BigDecimal;

/**
 * Julia set implementation.
 */

public class JuliaSet extends RasterFractal {
    private BigDecimal cr;
    private BigDecimal ci;

    private static final class Renderer extends StandardRasterRenderer<JuliaSet> {
        private int exp;

        private Renderer(int exp) {
            super(Double.TYPE);
            this.exp = exp;
        }

        @Override
        protected final int getPointDouble(final JuliaSet fractal, final double x, final double y, final int maxIters) {
            ComplexNumber z = new ComplexNumber(x, y);
            ComplexNumber c = new ComplexNumber(fractal.getCr(), fractal.getCi());
            int iter = 0;
            while (z.absSqr() < 4.0 && iter++ < maxIters) {
                z.sqr(exp);
                z.add(c);
            }
            return iter;
        }

    }

    public JuliaSet(BigDecimal x, BigDecimal y, BigDecimal width, BigDecimal angle, BigDecimal cr, BigDecimal ci, Integer maxIters) {
        super(x, y, width, angle, maxIters);
        this.cr = cr;
        this.ci = ci;
    }

    public BigDecimal getCr() {
        return cr;
    }

    public BigDecimal getCi() {
        return ci;
    }

    @Override
    public StandardRasterRenderer<JuliaSet> getRenderer() {
        return new Renderer(2);
    }

}
