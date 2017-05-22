package fractals;

import java.util.List;

public interface FractalBuilder<F extends Fractal> {

    void init();

    List<Parameter> getParameters();

    List<UserTransition> getTransitions();

    CommonTransitions getCommonTransitions();

    F getFractal();

}
