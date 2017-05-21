package fractals;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Contains utility math methods.
 */

public class FractalMath {

    private FractalMath() {
    }

    private static MathContext getMathContext() {
        return Env.instance().getMathContext();
    }

    private static final BigDecimal[] BD_CACHE = new BigDecimal[1024];

    static {
        for (int i = 0; i < BD_CACHE.length; i++) {
            BD_CACHE[i] = BigDecimal.valueOf(i);
        }
    }

    /**
     * Returns a BigDecimal instance with the same value as <code>value<code>, possibly
     * using cached value.
     *
     * @param value the value
     * @return BigDecimal instance with the same value as <code>value<code>
     */

    public static final BigDecimal getBigDecimal(int value) {
        if (value >= 0 && value < BD_CACHE.length) {
            return BD_CACHE[value];
        }
        return new BigDecimal(value);
    }

    /**
     * Computes and returns <code>value/divisor</code>.
     *
     * @param value   the value
     * @param divisor divisor
     * @return value/divisor
     */
    public static final BigDecimal divide(int value, int divisor) {
        return divide(getBigDecimal(value), divisor);
    }

    /**
     * Computes and returns <code>value/divisor</code>.
     *
     * @param value   the value
     * @param divisor divisor
     * @return value/divisor
     */

    public static final BigDecimal divide(BigDecimal value, int divisor) {
        return value.divide(getBigDecimal(divisor), getMathContext());
    }

    /**
     * Computes and returns <code>sin(value)</code> as BigDecimal.
     *
     * @param value the value
     * @return sin(value)
     */
    public static BigDecimal sin(BigDecimal value) {
        return new BigDecimal(Math.sin(value.doubleValue()), getMathContext());
    }

    /**
     * Computes and returns <code>cos(value)</code> as BigDecimal.
     *
     * @param value the value
     * @return cos(value)
     */

    public static BigDecimal cos(BigDecimal value) {
        return new BigDecimal(Math.cos(value.doubleValue()), getMathContext());
    }

}
