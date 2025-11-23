package gym.vitae.views.components.primitives;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatComboBoxUI;
import java.awt.*;
import javax.swing.*;

public class GroupComboBox<T> extends JComboBox<T> {

  private JComponent embeddedComponent;

  public GroupComboBox(T[] items) {
    super(items);
  }

  public GroupComboBox() {
    setUI(new EmbeddedComboBoxUI());
  }

  @Override
  public void updateUI() {
    super.updateUI();
    EmbeddedComboBoxUI newUI = new EmbeddedComboBoxUI();
    if (embeddedComponent != null) {
      newUI.setEmbeddedComponent(embeddedComponent);
    }
    setUI(newUI);
  }

  public void setEmbedded(JComponent component) {
    embeddedComponent = component;
    ((EmbeddedComboBoxUI) getUI()).setEmbeddedComponent(embeddedComponent);
  }

  private static class EmbeddedComboBoxUI extends FlatComboBoxUI {

    private final JPanel panel;

    EmbeddedComboBoxUI() {
      panel = new JPanel();
      panel.setOpaque(false);
    }

    public void setEmbeddedComponent(JComponent component) {
      panel.removeAll();
      panel.setLayout(new BorderLayout());

      if (component == null) {
        panel.revalidate();
        panel.repaint();
        return;
      }

      applyInTextFieldStyle(component);

      if (component instanceof JButton || component instanceof JToggleButton) {
        styleButtonOrToggle(component);
      } else if (component instanceof JToolBar tollComponent) {
        styleToolBar(tollComponent);
      } else {
        setDefaultCursorIfNeeded(component);
      }

      panel.add(component, BorderLayout.CENTER);
      panel.revalidate();
      panel.repaint();
    }

    private void applyInTextFieldStyle(JComponent component) {
      component.putClientProperty(FlatClientProperties.STYLE_CLASS, "inTextField");
    }

    private void setDefaultCursorIfNeeded(Component component) {
      if (!component.isCursorSet()) {
        component.setCursor(Cursor.getDefaultCursor());
      }
    }

    private void styleButtonOrToggle(JComponent component) {
      component.putClientProperty(
          FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
      setDefaultCursorIfNeeded(component);
    }

    private void styleToolBar(JToolBar toolBar) {
      for (Component child : toolBar.getComponents()) {
        if (child instanceof JComponent coldComponent) {
          coldComponent.putClientProperty(FlatClientProperties.STYLE_CLASS, "inTextField");
        }
      }
      setDefaultCursorIfNeeded(toolBar);
    }

    @Override
    protected void installComponents() {
      super.installComponents();
      comboBox.add(panel);
    }

    @Override
    protected void uninstallComponents() {
      super.uninstallComponents();
      comboBox.remove(panel);
    }

    @Override
    protected LayoutManager createLayoutManager() {
      return new ComboBoxLayoutManager() {

        @Override
        public void layoutContainer(Container parent) {
          super.layoutContainer(parent);
          Insets insets = parent.getInsets();
          Dimension arrowSize = arrowButton.getSize();
          Dimension panelSize = panel.getPreferredSize();
          int availableWidth = parent.getWidth() - (insets.left + insets.right) - arrowSize.width;
          int availableHeight = parent.getHeight() - (insets.top + insets.bottom);
          if (parent.getComponentOrientation().isLeftToRight()) {
            panel.setBounds(
                Math.max(insets.left + availableWidth - panelSize.width, 0),
                insets.top + (availableHeight - panelSize.height) / 2,
                Math.min(panelSize.width, availableWidth),
                panelSize.height);
          } else {
            panel.setBounds(
                insets.left + arrowSize.width,
                insets.top + (availableHeight - panelSize.height) / 2,
                Math.min(panelSize.width, availableWidth),
                panelSize.height);
          }
        }
      };
    }

    @Override
    protected Rectangle rectangleForCurrentValue() {
      Rectangle rectangle = super.rectangleForCurrentValue();
      rectangle.width -= panel.getWidth();
      if (!comboBox.getComponentOrientation().isLeftToRight()) {
        rectangle.x += panel.getWidth();
      }
      return rectangle;
    }
  }
}
