package fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public abstract class RasterRenderer<F extends Fractal> implements Renderer<F> {

    private class BlockRunnable implements Runnable {
        private F fractal;
        private RenderingContext context;

        private BlockRunnable(F fractal, RenderingContext context) {
            this.fractal = fractal;
            this.context = context;
        }

        @Override
        public void run() {
            renderBlock(fractal, context);
        }

    }

    /**
     * Creates a new instance.
     */

    protected RasterRenderer() {
    }

    /**
     * Returns the renderer precision.
     *
     * @return the renderer precision
     */

    protected abstract String getPrecision();

    @Override
    public RenderingSummary render(F fractal, RenderingContext context) {
        long time0 = System.nanoTime();
        renderImpl(fractal, context);
        int threads = 1;
        if (isParalell()) {
            threads = Env.instance().getParallelThreadCount();
        }
        return new RenderingSummary(System.nanoTime() - time0, context, getPrecision(), threads);
    }

    protected void renderImpl(final F fractal, final RenderingContext context) {
        if (isParalell() && Env.instance().getParallelThreadCount() > 1) {
            int height = context.getHeight();
            int blockSize = getParallelBlockSize();
            List<Callable<Object>> subtasks = new ArrayList<>(height / blockSize + 1);
            for (int y = 0; y < height; y += blockSize) {
                subtasks.add(Executors.callable(
                        new BlockRunnable(fractal, SimpleRenderingContext.getSlice(context, y, Math.min(height - y, blockSize)))));
            }
            try {
                Env.instance().getExecutorService().invokeAll(subtasks);
            } catch (Exception e) {
                Env.instance().logException(e);
            }
        } else {
            renderBlock(fractal, context);
        }
    }

    protected boolean isParalell() {
        return true;
    }

    private final int getParallelBlockSize() {
        return 32;
    }

    protected abstract void renderBlock(final F fractal, final RenderingContext context);

}
