package fractals.script;

import fractals.FractalBuilder;

import java.util.ArrayList;
import java.util.List;

public class Script<B extends FractalBuilder> {
    private String name;
    private List<Step<B>> steps;

    public Script(String name) {
        this.name = name;
        this.steps = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getStepCount() {
        return steps.size();
    }

    public Step<B> getStep(int index) {
        return steps.get(index);
    }

}
