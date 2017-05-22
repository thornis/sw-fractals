package fractals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Transition composed of multiple transitions.
 */

public final class TransitionSet implements Transition {
    private List<Transition> transitions;

    /**
     * Creates a new instance.
     *
     * @param transitions transitions
     */

    @SafeVarargs
    public TransitionSet(Transition... transitions) {
        this.transitions = Arrays.asList(transitions);
    }

    @Override
    public String getType() {
        return getClass().getName();
    }

    @Override
    public void run(FractalBuilder fractalBuilder, View view) {
        for (Transition transition : transitions) {
            if (transition != null) {
                transition.run(fractalBuilder, view);
            }
        }
    }

    @Override
    public List<Transition> split(int steps) {
        List<Transition> result = new ArrayList<>(steps);
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
