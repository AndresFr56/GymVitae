package gym.vitae.views.common;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import gym.vitae.views.components.contents.SearchContext;
import gym.vitae.views.components.primitives.Icons;
import gym.vitae.views.components.primitives.RefreshLine;
import gym.vitae.views.components.primitives.SearchButton;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;

import javax.swing.*;
import java.awt.*;

public class MainView extends ViewContainer {

  private JPanel mainPanel;
  private RefreshLine refreshLine;

  private JButton buttonUndo;
  private JButton buttonRedo;

  public MainView() {
    init();
  }

  @Override
  protected void init() {
    setLayout(new MigLayout("fillx,wrap,insets 0,gap 0", "[fill]", "[][][fill,grow][]"));
    add(createHeader());
    add(createRefreshLine(), "height 3!");
    add(createMain());
    add(new JSeparator(), "height 2!");
  }

  private JPanel createHeader() {
    JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push", "[fill]"));
    JToolBar toolBar = new JToolBar();
    JButton buttonDrawer = new JButton(new FlatSVGIcon(Icons.MENU.getPath(), 0.5f));
    buttonUndo = new JButton(new FlatSVGIcon(Icons.UNDO.getPath(), 0.5f));
    buttonRedo = new JButton(new FlatSVGIcon(Icons.REDO.getPath(), 0.5f));

    JButton buttonRefresh = new JButton(new FlatSVGIcon(Icons.REFRESH.toString(), 0.5f));

    // style
    buttonDrawer.putClientProperty(FlatClientProperties.STYLE, "arc:10;");
    buttonUndo.putClientProperty(FlatClientProperties.STYLE, "arc:10;");
    buttonRedo.putClientProperty(FlatClientProperties.STYLE, "arc:10;");
    buttonRefresh.putClientProperty(FlatClientProperties.STYLE, "arc:10;");

    buttonDrawer.addActionListener(
        e -> {
          if (Drawer.isOpen()) {
            Drawer.showDrawer();
          } else {
            Drawer.toggleMenuOpenMode();
          }
        });

    buttonUndo.addActionListener(e -> ViewManager.undo());
    buttonRedo.addActionListener(e -> ViewManager.redo());
    buttonRefresh.addActionListener(e -> ViewManager.refresh());

    toolBar.add(buttonDrawer);
    toolBar.add(buttonUndo);
    toolBar.add(buttonRedo);
    toolBar.add(buttonRefresh);
    panel.add(toolBar);
    panel.add(createSearchBox(), "gapx n 135");
    return panel;
  }

  private JPanel createSearchBox() {
    JPanel panel = new JPanel(new MigLayout("fill", "[fill,center,200:250:]", "[fill]"));
    SearchButton button = new SearchButton();
    button.addActionListener(e -> SearchContext.getInstance().showSearch());
    panel.add(button);
    return panel;
  }

  private RefreshLine createRefreshLine() {
    refreshLine = new RefreshLine();
    return refreshLine;
  }

  private Component createMain() {
    mainPanel = new JPanel(new BorderLayout());
    return mainPanel;
  }

  /**
   * Sets view in main panel Habilitando el undo y redo
   *
   * @param view ViewContainer to set
   */
  public void setView(ViewContainer view) {
    mainPanel.removeAll();
    mainPanel.add(view);
    mainPanel.repaint();
    mainPanel.revalidate();

    // check button
    buttonUndo.setEnabled(ViewManager.isUndoable());
    buttonRedo.setEnabled(ViewManager.isRedoable());
    // check component orientation and update
    if (mainPanel.getComponentOrientation().isLeftToRight()
        != view.getComponentOrientation().isLeftToRight()) {
      applyComponentOrientation(mainPanel.getComponentOrientation());
    }
  }

  public void refresh() {
    refreshLine.refresh();
  }
}
