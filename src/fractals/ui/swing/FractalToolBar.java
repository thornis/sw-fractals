package fractals.ui.swing;

import fractals.FractalBuilder;
import fractals.UserTransition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class FractalToolBar extends JToolBar {
    private static final long serialVersionUID = 1L;

    FractalToolBar() {
        super(SwingConstants.HORIZONTAL);
        setFloatable(false);
    }

    void addGap(int width) {
        add(Box.createHorizontalStrut(width));
    }

    JButton add(String name, String description, KeyStroke keyStroke, final ActionListener listener) {
        return add(new FractalAction(name, description, keyStroke) {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(e);
            }
        });
    }

    public JButton add(Action action) {
        JButton button = createActionComponent(action);
        button.setAction(action);
        if (action instanceof TransitionAction) {
            add(button, getComponentCount() - 3);
        } else {
            add(button);
        }
        button.setRequestFocusEnabled(false);
        KeyStroke stroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        if (stroke != null) {
            button.getActionMap().put("action", action);
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put((KeyStroke) action.getValue(Action.ACCELERATOR_KEY),
                    "action");
        }
        return button;
    }

    UserTransition<? extends FractalBuilder> getTransition(Component component) {
        if (component instanceof JButton && ((JButton) component).getAction() instanceof TransitionAction) {
            return ((TransitionAction) ((JButton) component).getAction()).getTransition();
        }
        return null;
    }

}