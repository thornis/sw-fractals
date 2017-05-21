package fractals;

/**
 * Base fractal interface.
 */

public interface Fractal {

    /**
     * Returns the short fractal name.
     *
     * @return the short fractal name
     */

    String getName();

    /**
     * Returns the fractal description.
     *
     * @return the fractal description
     */

    String getDescription();

    /**
     * Returns the fractal renderer.
     *
     * @return the fractal renderer
     */

    Renderer<? extends Fractal> getRenderer();

}