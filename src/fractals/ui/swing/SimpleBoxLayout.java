package fractals.ui.swing;

import java.awt.*;

final class SimpleBoxLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Insets insets = parent.getInsets();
        Dimension result = new Dimension(0, insets.top + insets.bottom);
        result.width = Math.max(160, result.width);
        for (Component component : parent.getComponents()) {
            if (component.isVisible()) {
                Dimension componentPreferred = component.getPreferredSize();
                result.height += componentPreferred.height + 8;
                result.width = Math.max(result.width, componentPreferred.width);
            }
        }
        result.width += insets.left + insets.right;
        return result;
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int currenty = insets.top;
        int width = parent.getWidth() - insets.left - insets.right;
        for (int componentCount = parent.getComponentCount(), i = 0; i < componentCount; i++) {
            Component component = parent.getComponent(i);
            if (component.isVisible()) {
                int preferredHeight = component.getPreferredSize().height;
                if (i == componentCount - 1) {
                    currenty = parent.getHeight() - insets.bottom - preferredHeight;
                }
                component.setBounds(insets.left, currenty, width, preferredHeight);
                currenty += preferredHeight + 8;
            }
        }
    }

}