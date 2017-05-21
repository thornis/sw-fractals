package fractals;

public interface CommonTransitions<B extends FractalBuilder<? extends Fractal>> {

    SingleValueTransition<B> getMovePrimaryAxis();

    SingleValueTransition<B> getMoveSecondaryAxis();

    SingleValueTransition<B> getZoomIn();

    SingleValueTransition<B> getZoomOut();

    SingleValueTransition<B> getRotate();

}