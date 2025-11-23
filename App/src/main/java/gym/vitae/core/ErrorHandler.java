package gym.vitae.core;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/** Muestra diálogos de error relacionados con la BD en el EDT. */
public final class ErrorHandler {

  private ErrorHandler() {}

  public static void showDatabaseError(Throwable t) {
    String detail = t == null ? "" : ("\n\nDetalle: " + t.getMessage());
    SwingUtilities.invokeLater(
        () ->
            JOptionPane.showMessageDialog(
                null,
                "No hay conexión a la base de datos. Compruebe la configuración o intente reconectar."
                    + detail,
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE));
  }
}
