package fractals;

import java.util.List;

/**
 * Base interface for transition - change of fractal.
 */

public interface Transition {

    /**
     * Returns the transition type.
     *
     * @return the transition type.
     */

    String getType();

    /**
     * Splits this transition into several steps.
     *
     * @param steps number of steps
     * @return list of transitions
     */

    List<Transition> split(int steps);

    /**
     * Runs this transition for the given fractal.
     *
     * @param fractalBuilder fractal builder
     * @param view           view
     */

    void run(FractalBuilder fractalBuilder, View view);

}
