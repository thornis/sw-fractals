package fractals;

/**
 * Base interface for fractal renderers. A fractal can be shown in different
 * ways, with different precision etc.
 *
 * @param <F> the fractal type
 */

public interface Renderer<F extends Fractal> {

    /**
     * Renders the fractal to given context.
     *
     * @param fractal the fracal
     * @param context rendering context
     * @return rendering information
     */

    RenderingSummary render(F fractal, RenderingContext context);

}
