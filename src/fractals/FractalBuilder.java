package fractals;

import java.util.List;

public interface FractalBuilder<F extends Fractal> {

    void init();

    List<Parameter> getParameters();

    List<UserTransition<? extends FractalBuilder<? extends F>>> getTransitions();

    CommonTransitions<? extends FractalBuilder<? extends F>> getCommonTransitions();

    F getFractal();

}
