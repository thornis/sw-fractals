package fractals;

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
        // TODO move to util
        String simpleName = getClass().getSimpleName();
        StringBuilder b = new StringBuilder(simpleName.length() + 1);
        for (int i = 0; i < simpleName.length(); i++) {
            if (i > 0 && Character.isUpperCase(simpleName.charAt(i))) {
                b.append(' ');
                b.append(Character.toLowerCase(simpleName.charAt(i)));
            } else {
                b.append(simpleName.charAt(i));
            }
        }
        return b.toString();
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
