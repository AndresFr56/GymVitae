package gym.vitae;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.util.FontUtils;
import gym.vitae.core.ApplicationConfig;
import gym.vitae.views.common.ViewManager;
import gym.vitae.views.components.Menu;
import gym.vitae.views.utils.Preferences;
import raven.modal.Drawer;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

  public Main() {
    init();
  }

  public static void main(String[] args) {
    // cargar configuraciones y repositorios
    ApplicationConfig.init();

    // iniciar las preferencias
    Preferences.init();

    // configurar el tema path resources/themes/*
    FlatRobotoFont.install();
    FlatLaf.registerCustomDefaultsSource("themes");
    UIManager.put("defaultFont", FontUtils.getCompositeFont(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
    Preferences.setupLaf();
    EventQueue.invokeLater(() -> new Main().setVisible(true));
  }

  private void init() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
    Drawer.installDrawer(this, Menu.getInstance());
    ViewManager.install(this);
    setSize(new java.awt.Dimension(1366, 768));
    setLocationRelativeTo(null);
  }
}
