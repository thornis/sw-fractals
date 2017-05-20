package fractals;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Interface to outside environment.
 */

public class Env {
    private static Env INSTANCE = new Env();

    /**
     * Returns the single instance of Env class.
     *
     * @return the single instance of Env class
     */

    public static Env instance() {
        return INSTANCE;
    }

    /**
     * Sets the instance to be used.
     *
     * @param instance the instance to be used
     */

    public static void initialize(Env instance) {
        INSTANCE = instance;
    }

    private MathContext mctx = new MathContext(12, RoundingMode.HALF_UP);
    private ExecutorService executorService;

    /**
     * Creates a new instance.
     */

    protected Env() {
        executorService = Executors.newFixedThreadPool(getParallelThreadCount());
    }

    /**
     * Returns the standard locale.
     *
     * @return the standard locale
     */

    public final Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Returns the current system time. Can be overriden for testing purposes.
     *
     * @return the current system time
     */

    public final long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Returns the maximum paralell thread count.
     *
     * @return maximum paralell thread count
     */

    public final int getParallelThreadCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Returns the {@linkplain ExecutorService} to be used when processing with
     * more than one thread.
     *
     * @return the ExecutorService to be used
     */

    public final ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Returns the preferred (maximum) frame rate.
     *
     * @return the preferred (maximum) frame rate
     */

    public final int getPreferredFrameRate() {
        return 30;
    }

    /**
     * Returns the preferred frame time in ms.
     *
     * @return the preferred frame time in ms
     */

    public final int getFrameTime() {
        return 1000 / getPreferredFrameRate();
    }

    /**
     * Returns the {@linkplain MathContext} to be used.
     *
     * @return the {@linkplain MathContext} to be used
     */

    public final MathContext getMathContext() {
        return mctx;
    }

    /**
     * Returns <code>true</code> if logging is enabled.
     *
     * @return true if logging is enabled
     */

    public final boolean isLoggingEnabled() {
        return true;
    }

    /**
     * Logs the given text.
     *
     * @param message text to log
     */

    public void log(String message) {
        System.out.println(message);
    }

    /**
     * Logs the given text with the given log level.
     *
     * @param level log level
     * @param message text to log
     */

    public void log(Level level, String message) {
        System.out.println(message);
    }

    /**
     * Logs the given exception, including the stack trace.
     *
     * @param t the exception to be logged
     */

    public void logException(Throwable t) {
        t.printStackTrace();
    }

    /**
     * Allocates a new array. Used for debugging purpose.
     *
     * @param size requested array size
     * @return the array
     */

    public static int[] getArray(int size) {
        System.out.println("Allocating: " + size);
        // Thread.dumpStack();
        return new int[size];
    }

}
