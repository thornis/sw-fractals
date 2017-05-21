package fractals;

import java.util.List;

/**
 * Base interface for transition - change of fractal.
 *
 * @param <B> the fractal type
 */

public interface Transition<B extends FractalBuilder<?>> {

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

    List<Transition<B>> split(int steps);

    /**
     * Runs this transition for the given fractal.
     *
     * @param fractalBuilder fractal builder
     * @param view           view
     */

    void run(B fractalBuilder, View view);

}
