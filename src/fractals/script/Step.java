package fractals.script;

import fractals.FractalBuilder;
import fractals.View;

public interface Step<B extends FractalBuilder> {

    long getDuration();

    long getPlannedStartTime();

    void run(B fractalBuilder, View view);

}
