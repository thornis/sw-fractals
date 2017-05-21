package fractals;

/**
 * Rendering context (buffer).
 */

public interface RenderingContext {

    int getWidth();

    int getHeight();

    int getClipX();

    int getClipY();

    int getClipWidth();

    int getClipHeight();

    int[] getData();

    long getTotalIterations();

    void addTotalIterations(long value);

}
