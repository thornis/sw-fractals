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
    public MandelbrotSet getFractal() {
        return new MandelbrotSet(getX().toString(), getY().toString(), getWidth().toString(), getMaxIters());
    }

}
