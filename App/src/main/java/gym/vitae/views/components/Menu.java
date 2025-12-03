package gym.vitae.views.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import gym.vitae.model.Empleado;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.views.clases.ViewClases;
import gym.vitae.views.clientes.ViewClientes;
import gym.vitae.views.common.StateView;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.common.ViewManager;
import gym.vitae.views.components.primitives.Icons;
import gym.vitae.views.inventario.ViewInventario;
import gym.vitae.views.membresias.ViewMembresias;
import gym.vitae.views.membresias.ViewTiposMembresia;
import gym.vitae.views.personal.ViewPersonal;
import gym.vitae.views.proveedores.ViewProveedores;
import javax.swing.*;
import raven.extras.AvatarIcon;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.drawer.menu.*;
import raven.modal.drawer.renderer.DrawerStraightDotLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.LightDarkButtonFooter;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeader;
import raven.modal.drawer.simple.header.SimpleHeaderData;

public class Menu extends SimpleDrawerBuilder {

  private static final int SHADOWSIZE = 12;
  private static Menu instance;

  private Menu() {
    super(createMenuOptions());
    LightDarkButtonFooter lightBtn = (LightDarkButtonFooter) getFooter();
    lightBtn.addModeChangeListener(
        isDarkMode -> {
          UIManager.put("Component.shadowWidth", SHADOWSIZE);
          JOptionPane.showMessageDialog(
              null,
              "Acaba de cambiar a " + (isDarkMode ? "Dark" : "Light") + " Mode!.",
              "Cambio de Tema",
              JOptionPane.INFORMATION_MESSAGE);
        });
  }

  public static Menu getInstance() {
    if (instance == null) {
      instance = new Menu();
    }
    return instance;
  }

  public static MenuOption createMenuOptions() {

    MenuOption menuOption = new MenuOption();

    MenuItem[] items =
        new MenuItem[] {
          new Item.Label("Menu"),
          new Item("Inicio", "dashboard.svg"),
          new Item("Personal", "email.svg")
              .subMenu("Lista de Personal", ViewPersonal.class)
              .subMenu("Nominass"),
          new Item("Facturacion", "page.svg")
              .subMenu("Crear Factura")
              .subMenu("Historial de Facturas"),
          new Item("Clientes", "customer.svg").subMenu("Listado de Clientes", ViewClientes.class),
          new Item("Clases", "calendar.svg")
              .subMenu("Clases Programadas", ViewClases.class)
              .subMenu("Horrarios de Clases"),
          new Item("Membresias", "employee.svg")
              .subMenu("Listado de Membresias", ViewMembresias.class)
              .subMenu("Tipos de Membresias", ViewTiposMembresia.class),
          new Item("Iventario", "pack.svg")
              .subMenu("Productos y Equipos", ViewInventario.class)
              .subMenu("Proveedores", ViewProveedores.class),
          new Item("Cerrar Session", "logout.svg")
        };

    // line style
    menuOption.setMenuStyle(
        new MenuStyle() {
          @Override
          public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
            boolean isTopLevel = index.length == 1;
            if (isTopLevel) {
              menu.putClientProperty(
                  FlatClientProperties.STYLE, "margin:8,10,8,10;arc:8;iconTextGap:12;");
            }
          }

          @Override
          public void styleMenu(JComponent component) {
            component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
          }
        });

    menuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
    menuOption.setMenuValidation(new MenuValidation());

    // Events de menu
    menuOption.addMenuEvent(
        (menuAction, ints) -> {
          Class<?> itemMenu = menuAction.getItem().getItemClass();

          String text = menuAction.getItem().getName();
          if (text != null && text.equalsIgnoreCase("Cerrar Session")) {
            menuAction.consume();
            ViewManager.logout();
            return;
          }

          if (itemMenu == null || !ViewContainer.class.isAssignableFrom(itemMenu)) {
            menuAction.consume();
            return;
          }

          @SuppressWarnings("unchecked")
          Class<? extends ViewContainer> viewClass = (Class<? extends ViewContainer>) itemMenu;

          ViewManager.showView(StateView.getView(viewClass));
        });

    // agregando el icon Path Resources/icons/modules

    menuOption.setMenus(items).setBaseIconPath("icons/modules").setIconScale(0.46f);

    return menuOption;
  }

  private static String getDrawerBackgroundStyle() {
    return "[light]background:tint($Panel.background,20%);"
        + "[dark]background:tint($Panel.background,5%);";
  }

  public void seUserInfo(Empleado empleado) {

    SimpleHeader header = (SimpleHeader) getHeader();
    SimpleHeaderData data = header.getSimpleHeaderData();

    AvatarIcon icon =
        new AvatarIcon(new FlatSVGIcon(Icons.AVATAR_MALE.toString(), 100, 100), 50, 50, 3.5f);

    icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
    icon.setBorder(2, 2);

    data.setTitle(empleado.getNombres());
    data.setDescription(empleado.getEmail());

    header.setSimpleHeaderData(data);

    rebuildMenu();
  }

  @Override
  public SimpleHeaderData getSimpleHeaderData() {

    AvatarIcon icon =
        new AvatarIcon(new FlatSVGIcon(Icons.AVATAR_MALE.toString(), 100, 100), 50, 50, 3.5f);

    icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
    icon.setBorder(2, 2);

    changeAvatarIconBorderColor(icon);

    UIManager.addPropertyChangeListener(
        evt -> {
          if (evt.getPropertyName().equals("lookAndFeel")) {
            changeAvatarIconBorderColor(icon);
          }
        });

    String title = "Usuario";
    String desc = "defaut@gmail.com";

    return new SimpleHeaderData().setIcon(icon).setTitle(title).setDescription(desc);
  }

  private void changeAvatarIconBorderColor(AvatarIcon icon) {
    icon.setBorderColor(
        new AvatarIcon.BorderColor(UIManager.getColor("Component.accentColor"), 0.7f));
  }

  @Override
  public SimpleFooterData getSimpleFooterData() {
    return new SimpleFooterData().setTitle("Gym Vitae").setDescription("Version 1.0");
  }

  @Override
  public int getDrawerWidth() {
    return 270 + SHADOWSIZE;
  }

  @Override
  public int getDrawerCompactWidth() {
    return 80 + SHADOWSIZE;
  }

  @Override
  public int getOpenDrawerAt() {
    return 1000;
  }

  @Override
  public boolean openDrawerAtScale() {
    return false;
  }

  @Override
  public void build(DrawerPanel drawerPanel) {
    drawerPanel.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
  }
}
