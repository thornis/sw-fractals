package fractals;

import java.util.ArrayList;
import java.util.List;

public abstract class View {
    private List<Step> steps;

    public static class Step {
        public Filter filter;
        public RenderingContext inContext;
        public RenderingContext outContext;

        public Step() {
        }

        public Filter getFilter() {
            return filter;
        }

        public RenderingContext getInContext() {
            return inContext;
        }

        public void setInContext(RenderingContext inContext) {
            this.inContext = inContext;
        }

        public RenderingContext getOutContext() {
            return outContext;
        }

        public void setOutContext(RenderingContext outContext) {
            this.outContext = outContext;
        }

    }

    public View() {
    }

    protected abstract RenderingContext getFinalContext(RenderingHints hints);

    protected abstract List<Filter> getFilters(RenderingHints hints);

    protected void startRendering(RenderingHints hints) {
        RenderingContext finalContext = getFinalContext(hints);

        List<Filter> filters = getFilters(hints);

        if (steps == null) {
            steps = new ArrayList<>();
        }
        if (steps.size() != filters.size()) {
            steps.clear();
            for (int i = 0; i < filters.size(); i++) {
                steps.add(new Step());
            }
        }

        RenderingContext outContext = finalContext;
        RenderingContext inContext = null;
        for (int i = filters.size() - 1; i >= 0; i--) {
            Step step = steps.get(i);
            Filter filter = filters.get(i);
            RenderingContext proposedContext = step.getInContext();
            inContext = filter.getRequiredContext(outContext, proposedContext);
            step.filter = filter;
            step.setInContext(inContext);
            step.setOutContext(outContext);
            outContext = inContext;
        }

    }

    public void render(Fractal fractal, RenderingHints hints) {
        startRendering(hints);
        Renderer renderer = fractal.getRenderer();
        RenderingSummary renderingSummary = renderer.render(fractal, steps.get(0).getInContext());
        long time0 = System.nanoTime();
        for (Step step : steps) {
            step.getFilter().apply(step.getInContext(), step.getOutContext());
        }
        long time1 = System.nanoTime();
        renderingSummary.setFiltersTime(time1 - time0);
        if (!hints.isPreview() && Env.instance().isLoggingEnabled()) {
            Env.instance().log(renderingSummary.toString());
        }
    }

    public abstract int getWidth();

    public abstract int getHeight();

}
