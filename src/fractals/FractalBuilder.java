package fractals;

import java.util.List;

public interface FractalBuilder<F extends Fractal> {

    String getName();

    /**
     * Returns the fractal description.
     *
     * @return the fractal description
     */

    String getDescription();

    void init();

    List<Parameter> getParameters();

    List<UserTransition> getTransitions();

    CommonTransitions getCommonTransitions();

    F getFractal();

}
