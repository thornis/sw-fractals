package fractals.filter;

import fractals.Filter;
import fractals.RenderingContext;

/**
 * The default palette.
 */

public final class DefaultPalette implements Filter {
    private static final DefaultPalette INSTANCE = new DefaultPalette();

    /**
     * Returns the single instance of this class.
     *
     * @return the single instance of this class
     */

    public static DefaultPalette getInstance() {
        return INSTANCE;
    }

    private int[] argb;

    private DefaultPalette() {
        argb = new int[2048];
        for (int i = 0; i < argb.length; i++) {
            // double d = (double) i;
            // int r = (int) (Math.sin((d + 64) / 75.0) * 127) + 127;
            // int g = (int) (Math.sin((d + 128) / 50.0) * 96) + 127;
            // int b = (int) (Math.sin((d + 192) / 50.0) * 96) + 127;
            int r = Math.abs(i - 255) & 255;
            int g = Math.abs(i + 64 - 255) & 255;
            int b = Math.abs(i + 128 - 255) & 255;
            argb[i] = 0xFF000000 | (r << 16) | (g << 8) | b;
        }
    }

    @Override
    public String getName() {
        return "Default palette";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public RenderingContext getRequiredContext(RenderingContext context, RenderingContext proposedContext) {
        return context;
    }

    public void apply(final RenderingContext inContext, final RenderingContext outContext) {
        final int[] inData = inContext.getData();
        final int[] outData = outContext.getData();
        for (int length = inData.length, i = 0; i < length; i++) {
            outData[i] = argb[Math.abs(inData[i]) % argb.length];
        }
    }

}
