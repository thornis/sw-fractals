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

    /**
     * Creates a new instamce.
     *
     * @param maxIters maximum iteraction
     * @param cr       the CR value
     * @param ci       the CI value
     */

    public JuliaSet(Integer maxIters, String cr, String ci) {
        this("0.0", "0.0", "4.0", cr, ci, maxIters);
    }

    private JuliaSet(String x, String y, String width, String cr, String ci, Integer maxIters) {
        super(x, y, width, maxIters);
        this.cr = new BigDecimal(cr.trim());
        this.ci = new BigDecimal(ci.trim());
    }

    @Override
    public String getName() {
        return "Julia set [" + cr + ',' + ci + ']';
    }

    @Override
    public String getDescription() {
        return "<html><p><b>Julia set</b> is defined by the following iteration:</p>" + //
                "<p></p><p><b>z<sub>n+1</sub> = z<sub>n</sub> * z<sub>n</sub> + c</b></p>" + //
                "</html>";
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
