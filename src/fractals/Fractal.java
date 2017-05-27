package fractals;

/**
 * Base fractal interface.
 */

public interface Fractal {

    /**
     * Returns the fractal renderer.
     *
     * @return the fractal renderer
     */

    Renderer<? extends Fractal> getRenderer();

}