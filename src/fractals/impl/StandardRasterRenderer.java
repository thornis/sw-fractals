package fractals.impl;

import fractals.RasterFractal;
import fractals.RasterRenderer;
import fractals.RenderingContext;

public abstract class StandardRasterRenderer<F extends RasterFractal> extends RasterRenderer<F> {
    private Class<?> type;

    protected StandardRasterRenderer(Class<?> type) {
        this.type = type;
    }

    @Override
    protected String getPrecision() {
        return type.getSimpleName();
    }

    @Override
    protected void renderBlock(final F fractal, final RenderingContext context) {
        if (type == Float.TYPE) {
            renderBlockFloat(fractal, context);
        } else {
            renderBlockDouble(fractal, context);
        }
    }

    protected void renderBlockDouble(final F fractal, final RenderingContext context) {
        final int[] data = context.getData();

        // prefix p = physical (viewport) coordinates
        final int pyStart = context.getClipY();
        final int pyEnd = context.getClipY() + context.getClipHeight();
        final int pxStart = context.getClipX();
        final int pxEnd = context.getClipX() + context.getClipWidth();
        final int pWidth = context.getWidth();
        final int pHeight = context.getHeight();
        final int skipi = pWidth - context.getClipWidth();

        // prefix l = logical coordinates before rotation
        final double lxCenter = fractal.getX().doubleValue();
        final double lyCenter = fractal.getY().doubleValue();
        final double lWidth = fractal.getWidth().doubleValue();
        final double lxStart = lWidth * pxStart / pWidth - lWidth / 2.0;
        final double lHeight = lWidth * pHeight / pWidth;
        final double lyStart = lHeight * pyStart / pHeight - lHeight / 2.0;

        // prefix r = logical coordinates including rotation
        final double sinAngle = Math.sin(fractal.getAngle().doubleValue());
        final double cosAngle = Math.cos(fractal.getAngle().doubleValue());
        final double rxStep = lWidth / pWidth * cosAngle;
        final double ryStep = lWidth / pWidth * sinAngle;
        final double rxStepLine = lHeight / pHeight * -sinAngle;
        final double ryStepLine = lHeight / pHeight * cosAngle;

        final int maxIters = fractal.getMaxIters();
        int totalIters = 0;
        int i = pyStart * pWidth + pxStart;
        double rxLine = lxCenter + lxStart * cosAngle - lyStart * sinAngle;
        double ryLine = lyCenter + lyStart * cosAngle + lxStart * sinAngle;

        for (int py = pyStart; py < pyEnd; py++) {
            double rx = rxLine;
            double ry = ryLine;
            for (int px = pxStart; px < pxEnd; px++) {
                int value = getPointDouble(fractal, rx, ry, maxIters);
                totalIters += value;
                data[i++] = value;
                rx += rxStep;
                ry += ryStep;
            }
            i += skipi;
            rxLine += rxStepLine;
            ryLine += ryStepLine;
        }

        context.addTotalIterations(totalIters);
    }

    protected abstract int getPointDouble(final F fractal, final double x, final double y, final int maxIters);

    protected void renderBlockFloat(final F fractal, final RenderingContext context) {
        final int[] data = context.getData();

        // prefix p = physical (viewport) coordinates
        final int pyStart = context.getClipY();
        final int pyEnd = context.getClipY() + context.getClipHeight();
        final int pxStart = context.getClipX();
        final int pxEnd = context.getClipX() + context.getClipWidth();
        final int pWidth = context.getWidth();
        final int pHeight = context.getHeight();
        final int skipi = pWidth - context.getClipWidth();

        // prefix l = logical coordinates before rotation
        final float lxCenter = fractal.getX().floatValue();
        final float lyCenter = fractal.getY().floatValue();
        final float lWidth = fractal.getWidth().floatValue();
        final float lxStart = lWidth * pxStart / pWidth - lWidth / 2.0f;
        final float lHeight = lWidth * pHeight / pWidth;
        final float lyStart = lHeight * pyStart / pHeight - lHeight / 2.0f;

        // prefix r = logical coordinates including rotation
        final float sinAngle = (float) Math.sin(fractal.getAngle().floatValue());
        final float cosAngle = (float) Math.cos(fractal.getAngle().floatValue());
        final float rxStep = lWidth / pWidth * cosAngle;
        final float ryStep = lWidth / pWidth * sinAngle;
        final float rxStepLine = lHeight / pHeight * -sinAngle;
        final float ryStepLine = lHeight / pHeight * cosAngle;

        final int maxIters = fractal.getMaxIters();
        int totalIters = 0;
        int i = pyStart * pWidth + pxStart;
        float rxLine = lxCenter + lxStart * cosAngle - lyStart * sinAngle;
        float ryLine = lyCenter + lyStart * cosAngle + lxStart * sinAngle;

        for (int py = pyStart; py < pyEnd; py++) {
            float rx = rxLine;
            float ry = ryLine;
            for (int px = pxStart; px < pxEnd; px++) {
                int value = getPointFloat(fractal, rx, ry, maxIters);
                totalIters += value;
                data[i++] = value;
                rx += rxStep;
                ry += ryStep;
            }
            i += skipi;
            rxLine += rxStepLine;
            ryLine += ryStepLine;
        }

        context.addTotalIterations(totalIters);
    }

    protected int getPointFloat(final F fractal, float x, float y, int maxIters) {
        return getPointDouble(fractal, x, y, maxIters);
    }

}
