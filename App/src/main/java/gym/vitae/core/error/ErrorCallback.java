package gym.vitae.core.error;

/**
 * Interfaz funcional para manejar errores de forma desacoplada. Permite a la capa de presentación
 * (UI) definir cómo mostrar los errores sin que la capa de datos conozca detalles de
 * implementación.
 *
 * <p>Ejemplo de uso:
 *
 * <pre>
 * ErrorHandler.setCallback(context -> {
 *   SwingUtilities.invokeLater(() -> {
 *     JOptionPane.showMessageDialog(null, context.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
 *   });
 * });
 * </pre>
 */
@FunctionalInterface
public interface ErrorCallback {

  /**
   * Maneja un error con el contexto proporcionado.
   *
   * @param context Contexto del error con mensaje, causa y tipo
   */
  void onError(ErrorContext context);
}
