package fractals.impl;

import fractals.RasterFractalBuilder;

import java.math.BigDecimal;

public class MandelbrotSetBuilder extends RasterFractalBuilder<MandelbrotSet> {

    @Override
    public void init() {
        setX(new BigDecimal("-0.5"));
        setY(new BigDecimal("0.0"));
        setWidth(new BigDecimal("3.0"));
        setAngle(BigDecimal.ZERO);
        setMaxIters(100);
    }

    @Override
    public String getDescription() {
        return "<html><p><b>Mandelbrot set</b> is defined by the following iteration:</p>" + //
                "<p></p><p><b>z<sub>n+1</sub> = z<sub>n</sub> * z<sub>n</sub> + c</b></p>" + //
                "</html>";
    }

    @Override
    public MandelbrotSet getFractal() {
        return new MandelbrotSet(getX(), getY(), getWidth(), getAngle(), getMaxIters());
    }



}
