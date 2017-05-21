package fractals.ui.swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

abstract class FractalAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private static Map<String, Icon> iconCache = new HashMap<>();

    private static synchronized Icon getIcon(String name) {
        if (iconCache.containsKey(name)) {
            return iconCache.get(name);
        }
        Icon icon = null;
        try {
            icon = new ImageIcon(
                    ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(name.toLowerCase() + ".png")));
        } catch (IOException | IllegalArgumentException e) {
            // ignore
        }
        iconCache.put(name, icon);
        return icon;
    }

    FractalAction(String name, String description, KeyStroke keyStroke) {
        super(name);
        putValue(SHORT_DESCRIPTION, description);
        Icon icon = getIcon(name);
        if (icon != null) {
            putValue(LARGE_ICON_KEY, icon);
        }
        putValue(ACCELERATOR_KEY, keyStroke);
    }

}