package fractals.ui.swing;

import java.awt.*;

final class ParametersLayout implements LayoutManager {
    private static final int colgap = 4;
    private static final int rowgap = 4;

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
        Dimension result = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        int leftWidth = 0;
        int rightWidth = 0;
        for (int componentCount = parent.getComponentCount(), i = 0; i < componentCount; i += 2) {
            Dimension leftPreferred = parent.getComponent(i).getPreferredSize();
            Dimension rightPreferred = parent.getComponent(i + 1).getPreferredSize();
            result.height += Math.max(leftPreferred.height, rightPreferred.height) + rowgap;
            leftWidth = Math.max(leftWidth, leftPreferred.width);
            rightWidth = Math.max(rightWidth, rightPreferred.width);
        }
        result.width += (leftWidth + colgap + rightWidth);
        return result;
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int currenty = insets.top;
        int leftWidth = 0;
        for (int componentCount = parent.getComponentCount(), i = 0; i < componentCount; i += 2) {
            leftWidth = Math.max(leftWidth, parent.getComponent(i).getPreferredSize().width);
        }
        for (int componentCount = parent.getComponentCount(), i = 0; i < componentCount; i += 2) {
            Component left = parent.getComponent(i);
            Component right = parent.getComponent(i + 1);
            Dimension leftPreferred = left.getPreferredSize();
            Dimension rightPrefered = right.getPreferredSize();
            int rowHeight = Math.max(leftPreferred.height, rightPrefered.height);
            left.setBounds(insets.left, currenty + ((rowHeight - leftPreferred.height) >> 1), leftPreferred.width,
                    leftPreferred.height);
            right.setBounds(insets.left + leftWidth + colgap, currenty + ((rowHeight - rightPrefered.height) >> 1),
                    rightPrefered.width, rightPrefered.height);
            currenty += rowHeight + rowgap;
        }
    }

}
