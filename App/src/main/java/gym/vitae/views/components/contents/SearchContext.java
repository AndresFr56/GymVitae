package gym.vitae.views.components.contents;

import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.common.ViewManager;
import gym.vitae.views.components.Menu;
import gym.vitae.views.components.ModalBorder;
import gym.vitae.views.components.SearchPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import raven.modal.ModalDialog;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.option.Location;
import raven.modal.option.Option;

public class SearchContext {

  public static final String IDENTIFIER = "search";
  private static SearchContext instance;
  private final Map<Metadata, Class<? extends ViewContainer>> viewContainers;
  private SearchPanel searchPanel;

  private SearchContext() {
    viewContainers = new HashMap<>();
    for (Class<? extends ViewContainer> cls : getViewContainerClasses()) {
      if (cls.isAnnotationPresent(Metadata.class)) {
        Metadata meta = cls.getAnnotation(Metadata.class);
        viewContainers.put(meta, cls);
      }
    }
  }

  // Constructor privado para singleton
  public static SearchContext getInstance() {
    if (instance == null) {
      instance = new SearchContext();
    }
    return instance;
  }

  private List<Class<? extends ViewContainer>> getViewContainerClasses() {
    MenuItem[] menuItems = Menu.getInstance().getSimpleMenuOption().getMenus();
    List<Class<? extends ViewContainer>> views = new ArrayList<>();
    getMenuClass(menuItems, views);
    return views;
  }

  /**
   * Recorre los elementos del menú para obtener las clases de ViewContainer
   * asociadas.
   *
   * @param menuItems
   * @param view
   */
  private void getMenuClass(MenuItem[] menuItems, List<Class<? extends ViewContainer>> view) {
    for (MenuItem menu : menuItems) {

      if (!menu.isMenu()) {
        continue;
      }

      Item item = (Item) menu;

      Class<?> itemsClass = item.getItemClass();

      if (itemsClass != null && ViewManager.class.isAssignableFrom(itemsClass)) {
        view.add(itemsClass.asSubclass(ViewContainer.class));
      }

      if (item.isSubmenuAble()) {
        getMenuClass(item.getSubMenu().toArray(new Item[0]), view);
      }
    }
  }

  public void installKeyMap(JComponent component) {
    ActionListener key = e -> showSearch();
    component.registerKeyboardAction(
        key,
        KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK),
        JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  /** Muestra el panel de búsqueda en un cuadro de diálogo modal. */
  public void showSearch() {
    if (ModalDialog.isIdExist(IDENTIFIER)) {
      return;
    }
    Option option = ModalDialog.createOption();
    option.setAnimationEnabled(false);
    option.getLayoutOption().setMargin(20, 10, 10, 10).setLocation(Location.CENTER, Location.TOP);
    ModalDialog.showModal(
        ViewManager.getFrame(),
        new ModalBorder(
            getSearchPanel(),
            (controller, action) -> {
              if (action == ModalBorder.OPENED) {
                searchPanel.searchGrabFocus();
              }
            }),
        option,
        IDENTIFIER);
  }

  private JPanel getSearchPanel() {

    if (searchPanel == null) {
      searchPanel = new SearchPanel(viewContainers);
    }

    searchPanel.formCheck();
    searchPanel.clearSearch();

    ComponentOrientation orientation = ViewManager.getFrame().getComponentOrientation();

    if (orientation.isLeftToRight() != searchPanel.getComponentOrientation().isLeftToRight()) {
      searchPanel.applyComponentOrientation(orientation);
    }
    return searchPanel;
  }
}
