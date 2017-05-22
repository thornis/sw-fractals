package fractals;

import fractals.util.Util;

/**
 * Base class for {@link Fractal} implementation.
 */

public abstract class AbstractFractal implements Fractal {

    /**
     * Creates a new instance.
     */

    public AbstractFractal() {
    }

    @Override
    public String getName() {
        return Util.getDescriptionFromClassName(getClass());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getDescription() {
        return null;
    }

}
