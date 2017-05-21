package fractals;

/**
 * Rendering hints, source information for creating a rendering context.
 */

public class RenderingHints {
    private boolean preview;

    /**
     * Creates a new instance.
     *
     * @param preview if rendering is intended for preview only
     */

    public RenderingHints(boolean preview) {
    }

    /**
     * Returns true if rendering is intended for preview only
     *
     * @return true if rendering is intended for preview only
     */

    public boolean isPreview() {
        return preview;
    }

}
