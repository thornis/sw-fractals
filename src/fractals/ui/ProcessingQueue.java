package fractals.ui;

import fractals.Env;
import fractals.FractalBuilder;
import fractals.Transition;
import fractals.script.TransitionStep;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProcessingQueue implements Runnable {
    private FractalUI explorer;
    private BlockingQueue<TransitionStep<? extends FractalBuilder>> queue;

    public ProcessingQueue(FractalUI explorer) {
        this.explorer = explorer;
        queue = new ArrayBlockingQueue<>(1000);
    }

    public void start() {
        new Thread(this, "Transition processor").start();
    }

    public void run() {
        try {
            while (true) {
                TransitionStep<FractalBuilder<?>> step = (TransitionStep<FractalBuilder<?>>) queue.take();
                long currentTime = Env.instance().currentTimeMillis();
                long plannedTime = step.getPlannedStartTime();
                if (plannedTime > currentTime) {
                    try {
                        Thread.sleep(plannedTime - currentTime);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
                TransitionStep<? extends FractalBuilder> nextStep = queue.peek();
                boolean canSkip = nextStep != null && nextStep.getPlannedStartTime() < currentTime;
                step.run(explorer.getFractalBuilder(), explorer.getView());
                explorer.setFractal(explorer.getFractalBuilder().getFractal(), !canSkip, nextStep != null, false, nextStep == null);
            }
        } catch (InterruptedException ex) {
            // ignore
        }
    }

    ;

    public void add(TransitionStep<? extends FractalBuilder> step, boolean split) {
        if (split) {
            Transition transition = step.getTransition();
            int steps = (int) (Env.instance().getPreferredFrameRate() * step.getDuration() / 1000);
            List<? extends Transition> transitions = transition.split(steps);
            long time = step.getPlannedStartTime();
            for (Transition partialTransition : transitions) {
                try {
                    queue.put(new TransitionStep<>(partialTransition, Env.instance().getFrameTime(), time));
                    time += Env.instance().getFrameTime();
                } catch (InterruptedException e) {
                    // TODO handle
                }
            }
        } else {
            try {
                queue.put(step);
            } catch (InterruptedException e) {
                // TODO handle
            }
        }
    }
}
