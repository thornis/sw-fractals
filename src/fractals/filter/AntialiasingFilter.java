package fractals.filter;

import fractals.Filter;
import fractals.RenderingContext;
import fractals.SimpleRenderingContext;

/**
 * The antialiasing (averaging) filter.
 */

public class AntialiasingFilter implements Filter {
    private int n;

    /**
     * No antialiasign filter.
     */

    public static final AntialiasingFilter NO_ANTIALIASING = new AntialiasingFilter(1);

    /**
     * 2x2 antialiasign filter.
     */

    public static final AntialiasingFilter ANTIALIASING_2 = new AntialiasingFilter(2);

    /**
     * 4x4 antialiasign filter.
     */

    public static final AntialiasingFilter ANTIALIASING_4 = new AntialiasingFilter(4);

    /**
     * Returns all filters of this type.
     *
     * @return all filters of this type
     */

    public static AntialiasingFilter[] getAllFilters() {
        return new AntialiasingFilter[]{NO_ANTIALIASING, ANTIALIASING_2, ANTIALIASING_4};
    }

    private AntialiasingFilter(int n) {
        this.n = n;
    }

    @Override
    public String getName() {
        if (n == 1) {
            return "No antialiasing";
        }
        return "Antialiasing " + n + "*" + n;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public RenderingContext getRequiredContext(RenderingContext context, RenderingContext proposedContext) {
        if (n == 1) {
            return context;
        }
        int requiredWidth = context.getWidth() * n;
        int requiredHeight = context.getHeight() * n;
        if (proposedContext != null && proposedContext.getWidth() == requiredWidth
                && proposedContext.getHeight() == requiredHeight) {
            proposedContext.addTotalIterations(-proposedContext.getTotalIterations());
            return proposedContext;
        }
        return new SimpleRenderingContext(requiredWidth, requiredHeight);
    }

    @Override
    public void apply(final RenderingContext inContext, final RenderingContext outContext) {
        if (n <= 1) {
            return;
        } else if (n == 2) {
            apply2(inContext, outContext);
        } else {
            applyn(inContext, outContext);
        }
    }

    private void apply2(final RenderingContext inContext, final RenderingContext outContext) {
        final int[] in = inContext.getData();
        final int[] out = outContext.getData();
        final int inW = inContext.getWidth();
        final int inW2 = inW + inW;
        final int inH = inContext.getHeight();
        final int outW = inW >> 1;
        final int outW2 = outW + outW;
        final int outH = inH >> 1;
        int yOutW = 0;
        int y1InW1 = 0;
        int y1InW2 = 1;
        int y2InW1 = inW;
        int y2InW2 = inW + 1;
        for (int y = 0; y < outH; y++, yOutW += outW, y1InW1 += inW2, y1InW2 += inW2, y2InW1 += inW2, y2InW2 += inW2) {
            for (int x = 0; x < outW2; x += 2) {
                out[yOutW + (x >> 1)] = (in[y1InW1 + x] + in[y1InW2 + x] + in[y2InW1 + x] + in[y2InW2 + x]) >> 2;
            }
        }
    }

    private void applyn(final RenderingContext inContext, final RenderingContext outContext) {
        // TODO initial shift (if not divisible)
        final int[] in = inContext.getData();
        final int[] out = outContext.getData();
        final int inW = inContext.getWidth();
        final int inH = inContext.getHeight();
        final int outW = inW / n;
        final int outH = inH / n;
        final int nn = n * n;
        final int[] indices = new int[nn];
        for (int i = 0, y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                indices[i++] = y * inW + x;
            }
        }
        int yOutW = 0;
        int base = 0;
        for (int y = 0; y < outH; y++, yOutW += outW, base += inW * n) {
            int baseX = base;
            for (int x = 0; x < outW; x++, baseX += n) {
                int value = 0;
                for (int i = 0; i < indices.length; i++) {
                    value += in[baseX + indices[i]];
                }
                out[yOutW + x] = value / nn;
            }
        }
    }

}
