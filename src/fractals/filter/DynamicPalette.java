package fractals.filter;

import fractals.Filter;
import fractals.RenderingContext;

/**
 * Standard dynamic palette.
 */

public final class DynamicPalette implements Filter {
    private String name;
    private int rmag;
    private int gmag;
    private int bmag;

    /**
     * Creates a new instance.
     *
     * @param name palette name
     * @param cols RGB colors
     */

    public DynamicPalette(String name, int cols) {
        this(name, (cols >> 16) & 0xFF, (cols >> 8) & 0xFF, cols & 0xFF);
    }

    private DynamicPalette(String name, int rmag, int gmag, int bmag) {
        this.name = name;
        this.rmag = rmag;
        this.gmag = gmag;
        this.bmag = bmag;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public RenderingContext getRequiredContext(RenderingContext context, RenderingContext proposedContext) {
        return context;
    }

    private int[] getExtremes(final int[] data) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
            int n = data[i];
            if (n < min) {
                min = n;
            }
            if (n > max) {
                max = n;
            }
        }
        return new int[]{min, max};
    }

    public void apply(final RenderingContext inContext, final RenderingContext outContext) {
        final int[] inData = inContext.getData();
        final int[] outData = outContext.getData();
        final int[] extremes = getExtremes(inData);
        final int min = extremes[0];
        final int max = extremes[1];
        final int size = max - min + 1;
        final int[] palette = new int[size];
        for (int i = 0; i < size; i++) {
            int n = ((i << 16) / size) >> 8;
            palette[i] = 0xFF000000 | ((rmag * n) >> 8) << 16 | ((gmag * n) >> 8) << 8 | ((bmag * n) >> 8);
        }
        palette[size - 1] = palette[0];
        for (int i = 0; i < inData.length; i++) {
            outData[i] = palette[inData[i] - min];
        }
    }

}
