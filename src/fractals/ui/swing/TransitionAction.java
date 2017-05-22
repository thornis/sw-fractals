package fractals.ui.swing;

import fractals.Env;
import fractals.FractalBuilder;
import fractals.UserTransition;
import fractals.script.TransitionStep;

import javax.swing.*;
import java.awt.event.ActionEvent;

final class TransitionAction extends FractalAction {
    private static final long serialVersionUID = 1L;

    private final FractalExplorer explorer;
    private UserTransition transition;

    TransitionAction(FractalExplorer explorer, UserTransition transition) {
        super(transition.getId(), transition.getName(), getKeyStroke(explorer, transition));
        this.explorer = explorer;
        this.transition = transition;
    }

    UserTransition getTransition() {
        return transition;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        explorer.getProcessingQueue()
                .add(new TransitionStep<>(transition.getTransition(), 250L, Env.instance().currentTimeMillis()), true);
    }

    private static KeyStroke getKeyStroke(FractalExplorer explorer, UserTransition transition) {
        char shortcut = transition.getPreferredKeyShortcut();
        if (shortcut == 0) {
            return explorer.getNextAvailableShortcut();
        }
        return KeyStroke.getKeyStroke(shortcut);
    }
}