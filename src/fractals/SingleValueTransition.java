package fractals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for {@link Transition} implementations that change one parameter.
 */

public abstract class SingleValueTransition implements Transition {
    private final BigDecimal value;

    /**
     * Creates a new instance.
     *
     * @param value change value
     */
    public SingleValueTransition(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return getClass().getName();
    }

    /**
     * Returns the change value.
     *
     * @return the change value
     */

    public final BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getType() + ' ' + getValue();
    }

    /**
     * Returns the same transition, scaled to value.
     *
     * @param amount amount
     * @return the same transition, scaled to value
     */

    public abstract SingleValueTransition getSame(BigDecimal amount);

    @Override
    public List<Transition> split(int steps) {
        List<Transition> result = new ArrayList<>(steps);
        Transition stepTransition = getSame(
                getValue().divide(FractalMath.getBigDecimal(steps), Env.instance().getMathContext()));
        for (int i = 0; i < steps; i++) {
            // TODO exact last step
            result.add(stepTransition);
        }
        return result;
    }

}
