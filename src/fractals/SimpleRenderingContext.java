package fractals;

public class SimpleRenderingContext implements RenderingContext {
    private volatile long totalIterations;
    private volatile int paletteSize;
    private volatile int width;
    private volatile int height;
    private volatile int[] data;

    private static final class Slice implements RenderingContext {
        private RenderingContext context;
        private int clipY;
        private int clipHeight;

        private Slice(RenderingContext context, int clipY, int clipHeight) {
            this.context = context;
            this.clipY = clipY;
            this.clipHeight = clipHeight;
        }

        @Override
        public int getWidth() {
            return context.getWidth();
        }

        @Override
        public int getHeight() {
            return context.getHeight();
        }

        @Override
        public int getClipX() {
            return context.getClipX();
        }

        @Override
        public int getClipY() {
            return clipY;
        }

        @Override
        public int getClipWidth() {
            return context.getClipWidth();
        }

        @Override
        public int getClipHeight() {
            return clipHeight;
        }

        @Override
        public int[] getData() {
            return context.getData();
        }

        @Override
        public long getTotalIterations() {
            return context.getTotalIterations();
        }

        @Override
        public void addTotalIterations(long value) {
            context.addTotalIterations(value);
        }

    }

    public SimpleRenderingContext(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = Env.getArray(width * height);
    }

    public SimpleRenderingContext(int width, int height, int[] data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    @Override
    public int[] getData() {
        return data;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getClipX() {
        return 0;
    }

    @Override
    public int getClipY() {
        return 0;
    }

    @Override
    public int getClipWidth() {
        return width;
    }

    @Override
    public int getClipHeight() {
        return height;
    }

    @Override
    public long getTotalIterations() {
        return totalIterations;
    }

    @Override
    public void addTotalIterations(long iterations) {
        totalIterations += iterations;
    }

    public static RenderingContext getSlice(RenderingContext context, int clipY, int clipHeight) {
        return new Slice(context, clipY, clipHeight);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getWidth() + ", " + getHeight();
    }

}
