package fractals;

public interface CommonTransitions {

    SingleValueTransition getMovePrimaryAxis();

    SingleValueTransition getMoveSecondaryAxis();

    SingleValueTransition getZoomIn();

    SingleValueTransition getZoomOut();

    SingleValueTransition getRotate();

}