package fractals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for {@link Transition} implementations that change one parameter.
 *
 * @param <B> the fractal type
 */

public abstract class SingleValueTransition<B extends FractalBuilder<?>> implements Transition<B> {
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

    public abstract SingleValueTransition<B> getSame(BigDecimal amount);

    @Override
    public List<Transition<B>> split(int steps) {
        List<Transition<B>> result = new ArrayList<>(steps);
        Transition<B> stepTransition = getSame(
                getValue().divide(FractalMath.getBigDecimal(steps), Env.instance().getMathContext()));
        for (int i = 0; i < steps; i++) {
            // TODO exact last step
            result.add(stepTransition);
        }
        return result;
    }

}
