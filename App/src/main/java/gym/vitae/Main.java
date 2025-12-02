package gym.vitae;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.util.FontUtils;
import gym.vitae.core.ApplicationConfig;
import gym.vitae.core.ErrorHandler;
import gym.vitae.views.common.ViewManager;
import gym.vitae.views.components.Menu;
import gym.vitae.views.utils.Preferences;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;
import raven.modal.Drawer;

public class Main extends JFrame {

  public Main() {
    init();
  }

  public static void main(String[] args) {

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  try {
                    ApplicationConfig.shutdown();
                  } catch (Exception e) {
                    Logger.getLogger("Error durante shutdown: " + e.getMessage());
                  }
                }));

    ErrorHandler.setCallback(
        context ->
            SwingUtilities.invokeLater(
                () -> {
                  String message =
                      switch (context.type()) {
                        case DATABASE_CONNECTION ->
                            "No hay conexión a la base de datos.\n\n"
                                + "Compruebe la configuración o intente reconectar.\n\n"
                                + "Detalle: "
                                + (context.cause() != null
                                    ? context.cause().getMessage()
                                    : context.message());
                        case TRANSACTION_FAILED ->
                            "Error en la transacción.\n\n" + context.getFullMessage();
                        case VALIDATION_ERROR -> "Error de validación:\n\n" + context.message();
                        case UNEXPECTED_ERROR -> "Error inesperado:\n\n" + context.getFullMessage();
                      };

                  JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                }));

    try {
      ApplicationConfig.init();
      Preferences.init();

      FlatRobotoFont.install();
      FlatLaf.registerCustomDefaultsSource("themes");
      UIManager.put(
          "defaultFont", FontUtils.getCompositeFont(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
      Preferences.setupLaf();

      EventQueue.invokeLater(() -> new Main().setVisible(true));

    } catch (Exception e) {
      // Los errores de BD se manejan cuando el usuario intenta usar funcionalidades
      if (!(e instanceof gym.vitae.core.DatabaseUnavailableException)) {
        JOptionPane.showMessageDialog(
            null,
            "Error al iniciar la aplicación:\n\n" + e.getMessage(),
            "Error Fatal",
            JOptionPane.ERROR_MESSAGE);
        System.exit(1);
      }
    }
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
