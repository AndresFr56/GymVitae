package gym.vitae.views.components.tables;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;

/**
 * Representa una acción de tabla con un botón personalizado.
 *
 * @param label Texto del botón
 * @param colorHex Color de fondo en formato hexadecimal (ej: "#2196F3")
 * @param action Acción a ejecutar cuando se presiona el botón
 */
public record TableAction(String label, String colorHex, Runnable action) {

  /**
   * Crea un botón configurado para esta acción.
   *
   * @return JButton configurado con estilos FlatLaf
   */
  public JButton createButton() {
    JButton btn = new JButton(label);
    btn.putClientProperty(
        FlatClientProperties.STYLE,
        String.format(
            "foreground:#fff;background:%s;borderWidth:0;focusWidth:0;innerFocusWidth:0",
            colorHex));
    btn.addActionListener(e -> action.run());
    return btn;
  }
}
