package fractals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Transition composed of multiple transitions.
 *
 * @param <B> fractal type
 */

public final class TransitionSet<B extends FractalBuilder<?>> implements Transition<B> {
    private List<Transition<B>> transitions;

    /**
     * Creates a new instance.
     *
     * @param transitions transitions
     */

    @SafeVarargs
    public TransitionSet(Transition<B>... transitions) {
        this.transitions = Arrays.asList(transitions);
    }

    @Override
    public String getType() {
        return getClass().getName();
    }

    @Override
    public void run(B fractalBuilder, View view) {
        for (Transition<B> transition : transitions) {
            if (transition != null) {
                transition.run(fractalBuilder, view);
            }
        }
    }

    @Override
    public List<Transition<B>> split(int steps) {
        List<Transition<B>> result = new ArrayList<>(steps);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(getType());
        b.append(' ');
        b.append(transitions);
        return b.toString();
    }

}
