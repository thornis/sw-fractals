package fractals;

import fractals.Parameter.Value;

import java.awt.image.Raster;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class RasterFractalBuilder<F extends RasterFractal> extends AbstractFractalBuilder<F> {
    private static final Integer AUTO_MAX_ITERS = Integer.MIN_VALUE;

    // state
    @Parameter.Marker(order = "100", description = "Coordinate X")
    private BigDecimal x;
    @Parameter.Marker(order = "101", description = "Coordinate Y")
    private BigDecimal y;
    @Parameter.Marker(order = "102", description = "Width")
    private BigDecimal width;
    @Parameter.Marker(order = "103", description = "Angle (rad)")
    private BigDecimal angle;
    @Parameter.Marker(order = "105", description = "Max iters")
    private Integer maxIters;

    // shared
    private List<UserTransition<RasterFractalBuilder<RasterFractal>>> allTransitions;
    private CommonTransitions<RasterFractalBuilder<RasterFractal>> commonTransitions;

    private static final class MoveRelativeX extends SingleValueTransition<RasterFractalBuilder<RasterFractal>> {

        private MoveRelativeX(BigDecimal value) {
            super(value);
        }

        @Override
        public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getSame(BigDecimal amount) {
            return new MoveRelativeX(amount);
        }

        @Override
        public void run(RasterFractalBuilder<RasterFractal> fractal, View view) {
            MathContext mctx = Env.instance().getMathContext();
            BigDecimal moveAbsoluteX = getValue().multiply(fractal.getWidth(), mctx);
            BigDecimal newX = fractal.getX().add(moveAbsoluteX.multiply(fractal.getCosAngle(), mctx), mctx);
            BigDecimal newY = fractal.getY().add(moveAbsoluteX.multiply(fractal.getSinAngle(), mctx), mctx);
            fractal.setX(newX);
            fractal.setY(newY);
        }

    }

    private static final class MoveRelativeY extends SingleValueTransition<RasterFractalBuilder<RasterFractal>> {

        private MoveRelativeY(BigDecimal value) {
            super(value);
        }

        @Override
        public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getSame(BigDecimal amount) {
            return new MoveRelativeY(amount);
        }

        @Override
        public void run(RasterFractalBuilder<RasterFractal> fractal, View view) {
            MathContext mctx = Env.instance().getMathContext();
            BigDecimal viewWidth = FractalMath.getBigDecimal(view.getWidth());
            BigDecimal viewHeight = FractalMath.getBigDecimal(view.getHeight());
            BigDecimal height = fractal.getWidth().multiply(viewHeight, mctx).divide(viewWidth, mctx);
            BigDecimal moveAbsoluteY = getValue().multiply(height, mctx);
            fractal.setX(fractal.getX().subtract(moveAbsoluteY.multiply(fractal.getSinAngle(), mctx), mctx));
            fractal.setY(fractal.getY().add(moveAbsoluteY.multiply(fractal.getCosAngle(), mctx), mctx));
        }

    }

    private static final class Zoom extends SingleValueTransition<RasterFractalBuilder<RasterFractal>> {

        private Zoom(BigDecimal value) {
            super(value);
        }

        @Override
        public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getSame(BigDecimal amount) {
            return new Zoom(amount);
        }

        @Override
        public void run(RasterFractalBuilder<RasterFractal> fractal, View view) {
            fractal.setWidth(fractal.getWidth().divide(getValue(), Env.instance().getMathContext()));
        }

        @Override
        public List<Transition<RasterFractalBuilder<RasterFractal>>> split(int steps) {
            List<Transition<RasterFractalBuilder<RasterFractal>>> result = new ArrayList<>(steps);
            Transition<RasterFractalBuilder<RasterFractal>> stepTransition = getSame(
                    new BigDecimal(Math.pow(getValue().doubleValue(), 1.0 / steps), Env.instance().getMathContext()));
            for (int i = 0; i < steps; i++) {
                result.add(stepTransition);
            }
            return result;
        }

    }

    private static final class Rotate extends SingleValueTransition<RasterFractalBuilder<RasterFractal>> {

        private Rotate(BigDecimal value) {
            super(value);
        }

        @Override
        public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getSame(BigDecimal amount) {
            return new Rotate(amount);
        }

        @Override
        public void run(RasterFractalBuilder<RasterFractal> fractal, View view) {
            fractal.setAngle(fractal.getAngle().add(getValue(), Env.instance().getMathContext()));
        }

    }

    private static final class AdjustMaxIters extends SingleValueTransition<RasterFractalBuilder<RasterFractal>> {

        private AdjustMaxIters(BigDecimal value) {
            super(value);
        }

        @Override
        public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getSame(BigDecimal amount) {
            return new AdjustMaxIters(amount);
        }

        @Override
        public void run(RasterFractalBuilder<RasterFractal> fractal, View view) {
            long value = getValue().multiply(FractalMath.getBigDecimal(fractal.getMaxIters()), Env.instance().getMathContext())
                    .longValue();
            if (value > 0 && value < Integer.MAX_VALUE) {
                fractal.setMaxIters((int) value);
            }
        }

        @Override
        public List<Transition<RasterFractalBuilder<RasterFractal>>> split(int steps) {
            List<Transition<RasterFractalBuilder<RasterFractal>>> result = new ArrayList<>(steps);
            Transition<RasterFractalBuilder<RasterFractal>> stepTransition = getSame(
                    new BigDecimal(Math.pow(getValue().doubleValue(), 1.0 / steps), Env.instance().getMathContext()));
            for (int i = 0; i < steps; i++) {
                result.add(stepTransition);
            }
            return result;
        }

    }

    @Override
    protected List<Parameter.Value> getPredefinedValues(String parameterId) {
        if ("MaxIters".equals(parameterId)) {
            return Arrays.asList(new Parameter.Value("Auto", "Auto", AUTO_MAX_ITERS),
                    new Value("100", "100", Integer.valueOf(100)));
        }
        return super.getPredefinedValues(parameterId);
    }

    public RasterFractalBuilder() {
        UserTransition<RasterFractalBuilder<RasterFractal>> moveLeft = new UserTransition<>(
                new MoveRelativeX(new BigDecimal("-0.125")), "Left", "Move left~4");
        UserTransition<RasterFractalBuilder<RasterFractal>> moveRight = new UserTransition<>(
                new MoveRelativeX(new BigDecimal("0.125")), "Right", "Move right~6");
        UserTransition<RasterFractalBuilder<RasterFractal>> moveUp = new UserTransition<>(
                new MoveRelativeY(new BigDecimal("-0.125")), "Up", "Move up~8");
        UserTransition<RasterFractalBuilder<RasterFractal>> moveDown = new UserTransition<>(
                new MoveRelativeY(new BigDecimal("0.125")), "Down", "Move down~2");
        UserTransition<RasterFractalBuilder<RasterFractal>> zoomIn = new UserTransition<>(new Zoom(new BigDecimal("1.25")),
                "ZoomIn", "Zoom in~+");
        UserTransition<RasterFractalBuilder<RasterFractal>> zoomOut = new UserTransition<>(new Zoom(new BigDecimal("0.8")),
                "ZoomOut", "Zoom out~-");
        BigDecimal deltaAngle = new BigDecimal(Math.PI / 12.0, Env.instance().getMathContext());
        UserTransition<RasterFractalBuilder<RasterFractal>> rotate = new UserTransition<>(
                new Rotate(new BigDecimal(deltaAngle.negate().toString())), "Rotate", "Rotate~9");
        UserTransition<RasterFractalBuilder<RasterFractal>> rotateBack = new UserTransition<>(
                new Rotate(new BigDecimal(deltaAngle.toString())), "RotateBack", "Rotate back~7");
        UserTransition<RasterFractalBuilder<RasterFractal>> increaseIter = new UserTransition<>(
                new AdjustMaxIters(new BigDecimal("1.25")), "IncreaseIter", "Increase maximum iterations~*");
        UserTransition<RasterFractalBuilder<RasterFractal>> decreaseIter = new UserTransition<>(
                new AdjustMaxIters(new BigDecimal("0.80")), "DecreaseIter", "Decrease maximum iterations~/");

        List<UserTransition<RasterFractalBuilder<RasterFractal>>> transitions = new ArrayList<>();
        transitions.add(moveLeft);
        transitions.add(moveRight);
        transitions.add(moveUp);
        transitions.add(moveDown);
        transitions.add(zoomIn);
        transitions.add(zoomOut);
        transitions.add(rotate);
        transitions.add(rotateBack);
        transitions.add(increaseIter);
        transitions.add(decreaseIter);
        allTransitions = Collections.unmodifiableList(transitions);

        commonTransitions = new CommonTransitions<RasterFractalBuilder<RasterFractal>>() {
            @Override
            public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getZoomOut() {
                return (SingleValueTransition<RasterFractalBuilder<RasterFractal>>) zoomOut.getTransition();
            }

            @Override
            public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getZoomIn() {
                return (SingleValueTransition<RasterFractalBuilder<RasterFractal>>) zoomIn.getTransition();
            }

            @Override
            public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getRotate() {
                return (SingleValueTransition<RasterFractalBuilder<RasterFractal>>) rotate.getTransition();
            }

            @Override
            public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getMoveSecondaryAxis() {
                return (SingleValueTransition<RasterFractalBuilder<RasterFractal>>) moveDown.getTransition();
            }

            @Override
            public SingleValueTransition<RasterFractalBuilder<RasterFractal>> getMovePrimaryAxis() {
                return (SingleValueTransition<RasterFractalBuilder<RasterFractal>>) moveRight.getTransition();
            }

        };
    }

    @Override
    public List<UserTransition<? extends FractalBuilder<? extends F>>> getTransitions() {
        return Collections.unmodifiableList(Collections.emptyList());
        //return allTransitions;
    }

    @Override
    public CommonTransitions<? extends FractalBuilder<? extends F>> getCommonTransitions() {
        return null;
        //return (CommonTransitions<? extends FractalBuilder<? extends F>>) commonTransitions;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
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

    public void setAngle(BigDecimal angle) {
        this.angle = angle;
    }

    public Integer getMaxIters() {
        if (maxIters == AUTO_MAX_ITERS) {
            return (int) (200.0 / Math.sqrt(Math.sqrt(getWidth().doubleValue())));
        }
        return maxIters;
    }

    public void setMaxIters(Integer maxIters) {
        this.maxIters = maxIters;
    }

}
