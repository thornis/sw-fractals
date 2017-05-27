package fractals.impl;

import fractals.RasterFractal;

import java.math.BigDecimal;

/**
 * Mandelbrot set implementation.
 */

public class MandelbrotSet extends RasterFractal {

    private static final class Renderer extends StandardRasterRenderer<MandelbrotSet> {

        private Renderer(Class<?> type) {
            super(type);
        }

        @Override
        protected final int getPointDouble(final MandelbrotSet fractal, final double x, final double y, final int maxIters) {
            int iter = 0;
            double zr = x;
            double zi = y;
            double zrsq = zr * zr;
            double zisq = zi * zi;

            while (zrsq + zisq < 4.0 && iter < maxIters) {
                zi = 2 * zr * zi + y;
                zr = zrsq - zisq + x;
                zrsq = zr * zr;
                zisq = zi * zi;
                iter++;
            }

            return iter;
        }

        @Override
        protected final int getPointFloat(final MandelbrotSet fractal, final float x, final float y, final int maxIters) {
            int iter = 0;
            float zr = x;
            float zi = y;
            float zrsq = zr * zr;
            float zisq = zi * zi;

            while (zrsq + zisq < 4.0f && iter < maxIters) {
                zi = 2 * zr * zi + y;
                zr = zrsq - zisq + x;
                zrsq = zr * zr;
                zisq = zi * zi;
                iter++;
            }

            return iter;
        }
    }

    /**
     * Creates a new instamce.
     *
     * @param x        the x coordinate
     * @param y        the x coordinate
     * @param width    width
     * @param maxIters maximum iterations
     */

    public MandelbrotSet(BigDecimal x, BigDecimal y, BigDecimal width, BigDecimal angle, Integer maxIters) {
        super(x, y, width, angle, maxIters);
    }

    @Override
    public StandardRasterRenderer<MandelbrotSet> getRenderer() {
        if (getWidth().doubleValue() < 0.025) {
            return new Renderer(Double.TYPE);
        } else {
            return new Renderer(Float.TYPE);
        }
    }

}
