package fractals;

import java.util.Objects;

/**
 * Predefined user transition.
 *
 * @param <B> the fractal type
 */

public class UserTransition<B extends FractalBuilder<?>> {
    private Transition<B> transition;
    private String id;
    private String name;
    private char preferredKeyShortcut;

    /**
     * Creates a new instance.
     *
     * @param transition transition
     * @param id         transition id
     * @param name       user readable name
     */

    public UserTransition(Transition<B> transition, String id, String name) {
        Objects.requireNonNull(transition, "transition");
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        this.transition = transition;
        this.id = id;
        this.name = name;
        if (this.name.indexOf('~') >= 0) {
            preferredKeyShortcut = this.name.charAt(this.name.indexOf('~') + 1);
            this.name = this.name.substring(0, this.name.indexOf('~'));
        }
    }

    /**
     * Returns the transition to be run.
     *
     * @return the transition to be run
     */

    public Transition<B> getTransition() {
        return transition;
    }

    /**
     * Returns the unique id.
     *
     * @return the unique id
     */

    public String getId() {
        return id;
    }

    /**
     * Returns the human readable name.
     *
     * @return the human readable name
     */

    public String getName() {
        return name;
    }

    /**
     * Returns the preferred key shortcut, may be derived from name.
     *
     * @return the preferred key shortcut
     */

    public char getPreferredKeyShortcut() {
        return preferredKeyShortcut;
    }

}
