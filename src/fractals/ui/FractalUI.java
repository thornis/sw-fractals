package fractals.ui;

import fractals.Fractal;
import fractals.FractalBuilder;
import fractals.View;

public interface FractalUI {

    FractalBuilder getFractalBuilder();

    Fractal getFractal();

    View getView();

    void setFractal(FractalBuilder fractalBuilder, boolean render, boolean preview, boolean buildui, boolean refreshParamsAndTransitions);

}
