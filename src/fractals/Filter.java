package fractals;

/**
 * Base interface for filters, such as antialiasing, palette etc.
 */

public interface Filter {

    /**
     * Returns the human readable filter name.
     *
     * @return the human readable filter name.
     */

    String getName();

    /**
     * Returns the required context, based on the next context in chain.
     *
     * @param context         the next context in chain
     * @param proposedContext proposed context to be used
     * @return the required context, based on the next context in chain
     */

    RenderingContext getRequiredContext(RenderingContext context, RenderingContext proposedContext);

    /**
     * Applies the filter between input and output contexts.
     *
     * @param inContext  the input context
     * @param outContext the output context
     */

    void apply(RenderingContext inContext, RenderingContext outContext);

}
