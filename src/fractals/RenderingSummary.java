package fractals;

import java.text.DecimalFormat;

/**
 * Contains summary rendering information.
 */

public class RenderingSummary {
    private long time;
    private long pixels;
    private long iterations;
    private long filtersTime;
    private String precision;
    private int threads;

    /**
     * Creates a new instance.
     *
     * @param time      rendering start time
     * @param context   rendering context
     * @param precision rendering precision
     * @param threads   maximum number of threads used to render
     */

    public RenderingSummary(long time, RenderingContext context, String precision, int threads) {
        this.time = time;
        this.pixels = context.getClipWidth() * context.getClipHeight();
        this.iterations = context.getTotalIterations();
        this.precision = precision;
        this.threads = threads;
    }

    public long getTime() {
        return time;
    }

    public long getPixels() {
        return pixels;
    }

    public long getIterations() {
        return iterations;
    }

    public String getPrecision() {
        return precision;
    }

    public int getThreads() {
        return threads;
    }

    public long getFiltersTime() {
        return filtersTime;
    }

    public void setFiltersTime(long filtersTime) {
        this.filtersTime = filtersTime;
    }

    @Override
    public String toString() {
        long timeInMs = Math.max(getTime() / 1000000, 1);
        long itersPerSecond = getIterations() * 1000 / timeInMs;
        StringBuilder b = new StringBuilder(80);
        b.append("Done by ");
        b.append(getThreads());
        b.append(" thread(s) with ");
        b.append(getPrecision());
        b.append(" precision in ");
        b.append(getTime() / 1000000);
        b.append(" ms, ");
        b.append(format(getPixels()));
        b.append(" pixels, ");
        b.append(format(getIterations()));
        b.append("iterations, ");
        b.append(format(itersPerSecond));
        b.append(" iterations/s, ");
        b.append("filtering ");
        b.append(getFiltersTime() / 1000000);
        b.append(" ms.");
        return b.toString();
    }

    private String format(long value) {
        // TODO better formatting
        if (value < 0) {
            throw new IllegalArgumentException("Value must be a positive number");
        }
        if (value <= 0) {
            return "0 ";
        }
        final String[] units = new String[]{"", "k", "M", "G", "T"};
        int digitGroups = (int) (Math.log10(value) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(value / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}