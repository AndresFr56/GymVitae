package gym.vitae.core;

import gym.vitae.core.error.ErrorCallback;
import gym.vitae.core.error.ErrorContext;
import gym.vitae.core.error.ErrorContext.ErrorType;

/**
 * Manejador central de errores desacoplado de la UI. Usa el patrón Callback para permitir que la
 * capa de presentación defina cómo mostrar los errores.
 *
 * <p>La UI debe configurar el callback al inicio de la aplicación:
 *
 * <pre>
 * ErrorHandler.setCallback(context -> {
 *   SwingUtilities.invokeLater(() -> {
 *     JOptionPane.showMessageDialog(null, context.getFullMessage(), "Error", JOptionPane.ERROR_MESSAGE);
 *   });
 * });
 * </pre>
 */
public final class ErrorHandler {

  private static ErrorCallback callback = null;

  private ErrorHandler() {}

  /**
   * Configura el callback para manejo de errores.
   *
   * @param cb Callback que será invocado cuando ocurra un error
   */
  public static void setCallback(ErrorCallback cb) {
    callback = cb;
  }

  /**
   * Maneja un error de base de datos.
   *
   * @param t Excepción que causó el error
   */
  public static void handleDatabaseError(Throwable t) {
    if (callback != null) {
      callback.onError(
          new ErrorContext(
              "No hay conexión a la base de datos. Compruebe la configuración o intente reconectar.",
              t,
              ErrorType.DATABASE_CONNECTION));
    }
  }

  /**
   * Maneja un error de transacción.
   *
   * @param message Mensaje del error
   * @param t Excepción que causó el error
   */
  public static void handleTransactionError(String message, Throwable t) {
    if (callback != null) {
      callback.onError(new ErrorContext(message, t, ErrorType.TRANSACTION_FAILED));
    }
  }

  /**
   * Maneja un error de validación.
   *
   * @param message Mensaje del error
   */
  public static void handleValidationError(String message) {
    if (callback != null) {
      callback.onError(new ErrorContext(message, ErrorType.VALIDATION_ERROR));
    }
  }

  /**
   * Maneja un error inesperado.
   *
   * @param message Mensaje del error
   * @param t Excepción que causó el error
   */
  public static void handleUnexpectedError(String message, Throwable t) {
    if (callback != null) {
      callback.onError(new ErrorContext(message, t, ErrorType.UNEXPECTED_ERROR));
    }
  }
}
