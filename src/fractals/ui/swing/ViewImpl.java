package fractals.ui.swing;

import fractals.*;
import fractals.RenderingHints;
import fractals.script.TransitionStep;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;

final class ViewImpl extends View {
    private final FractalExplorer explorer;

    BufferedImage frontImage;
    private BufferedImage backImage;
    private JComponent swingView;

    private class MouseHandler extends MouseAdapter {
        private int lastx;
        private int lasty;
        private long lasttime;

        public MouseHandler() {
            endTracing();
        }

        private CommonTransitions getCommonTransitions() {
            return explorer.getFractalBuilder().getCommonTransitions();
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            CommonTransitions ct = getCommonTransitions();
            int rot = e.getWheelRotation();
            if (rot != 0) {
                Transition transition = (rot > 0) ? ct.getZoomOut() : ct.getZoomIn();
                explorer.getProcessingQueue().add(new TransitionStep<>(transition, 250, Env.instance().currentTimeMillis()), true);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (lastx < 0 || lasty < 0) {
                startTracing(e);
                return;
            }
            long time = e.getWhen();
            if (time - lasttime < 30) {
                return;
            }
            int dx = lastx - e.getXOnScreen();
            int dy = lasty - e.getYOnScreen();

            CommonTransitions ct = getCommonTransitions();
            if (SwingUtilities.isLeftMouseButton(e)) {
                explorer.getProcessingQueue().add(getMoveStep(dx, dy, time), false);
            } else if (SwingUtilities.isRightMouseButton(e) && dx != 0) {
                explorer.getProcessingQueue().add(
                        new TransitionStep<>(ct.getRotate().getSame(FractalMath.divide(dx * 2, getWidth())), 250, time), false);
            }

            lastx = e.getXOnScreen();
            lasty = e.getYOnScreen();
            lasttime = time;
        }

        private TransitionStep<? extends FractalBuilder<Fractal>> getMoveStep(int dx, int dy, long time) {
            CommonTransitions ct = getCommonTransitions();
            if (dx != 0 && dy != 0) {
                Transition t1 = ct.getMovePrimaryAxis().getSame(FractalMath.divide(dx, getWidth()));
                Transition t2 = ct.getMoveSecondaryAxis().getSame(FractalMath.divide(dy, getHeight()));
                return (TransitionStep) new TransitionStep<>(new TransitionSet(t1, t2));
            } else if (dx != 0) {
                return new TransitionStep<>(ct.getMovePrimaryAxis().getSame(FractalMath.divide(dx, getWidth())));
            } else {
                return new TransitionStep<>(ct.getMoveSecondaryAxis().getSame(FractalMath.divide(dy, getHeight())));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            endTracing();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            endTracing();
        }

        private void startTracing(MouseEvent e) {
            lastx = e.getXOnScreen();
            lasty = e.getYOnScreen();
            return;
        }

        private void endTracing() {
            lastx = -1;
            lasty = -1;
        }

    }

    ViewImpl(FractalExplorer explorer) {
        this.explorer = explorer;
        swingView = new JComponent() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                if (frontImage != null) {
                    g.drawImage(frontImage, 0, 0, null);
                }
            }

            @Override
            protected void processEvent(AWTEvent e) {
                super.processEvent(e);
            }

        };
        swingView.setDoubleBuffered(false);
        swingView.setPreferredSize(new Dimension(1024, 800));
        MouseHandler handler = new MouseHandler();
        swingView.addMouseWheelListener(handler);
        swingView.addMouseMotionListener(handler);
        swingView.addMouseListener(handler);
    }

    private synchronized void swapBuffers() {
        BufferedImage img = frontImage;
        frontImage = backImage;
        backImage = img;
    }

    @Override
    protected RenderingContext getFinalContext(RenderingHints hints) {
        int width = getWidth();
        int height = getHeight();
        if (backImage == null || backImage.getWidth() != width || backImage.getHeight() != height) {
            backImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        return new SimpleRenderingContext(width, height, ((DataBufferInt) backImage.getRaster().getDataBuffer()).getData());
    }

    @Override
    protected List<Filter> getFilters(RenderingHints hints) {
        return explorer.getFilters(hints);
    }

    @Override
    public void render(Fractal fractal, RenderingHints hints) {
        super.render(fractal, hints);
        swingView.repaint();
        Toolkit.getDefaultToolkit().sync();
        swapBuffers();
    }

    JComponent getComponent() {
        return swingView;
    }

    @Override
    public int getWidth() {
        return swingView.getWidth();
    }

    @Override
    public int getHeight() {
        return swingView.getHeight();
    }

}