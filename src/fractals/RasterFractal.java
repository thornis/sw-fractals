package fractals;

import java.math.BigDecimal;

public abstract class RasterFractal implements Fractal {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal width;
    private BigDecimal angle;
    private Integer maxIters;

    public RasterFractal(BigDecimal x, BigDecimal y, BigDecimal width, BigDecimal angle, Integer maxIters) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.maxIters = maxIters;
        this.angle = angle;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public BigDecimal getAngle() {
        return angle;
    }

    public BigDecimal getCosAngle() {
        return FractalMath.cos(angle);
    }

    public BigDecimal getSinAngle() {
        return FractalMath.sin(angle);
    }

    public Integer getAngleInDegrees() {
        return Integer.valueOf((int) (angle.doubleValue() * 180 / Math.PI));
    }

    public Integer getMaxIters() {
        return maxIters;
    }

}
