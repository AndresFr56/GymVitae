package gym.vitae.core.error;

/**
 * Contexto de error que encapsula la información sobre un error ocurrido en la aplicación. Permite
 * propagar información de errores de forma estructurada sin acoplar capas.
 */
public record ErrorContext(String message, Throwable cause, ErrorType type) {

  /**
   * Constructor del contexto de error.
   *
   * @param message Mensaje descriptivo del error
   * @param cause Excepción que causó el error (puede ser null)
   * @param type Tipo de error
   */

  /**
   * Constructor simplificado sin causa.
   *
   * @param message Mensaje descriptivo del error
   * @param type Tipo de error
   */
  public ErrorContext(String message, ErrorType type) {
    this(message, null, type);
  }

  /**
   * Obtiene el mensaje completo incluyendo la causa si existe.
   *
   * @return Mensaje completo del error
   */
  public String getFullMessage() {
    if (cause != null) {
      return message + "\n\nDetalle: " + cause.getMessage();
    }
    return message;
  }

  @Override
  public String toString() {
    return "ErrorContext{"
        + "type="
        + type
        + ", message='"
        + message
        + '\''
        + ", cause="
        + cause
        + '}';
  }

  /** Tipos de error de la aplicación. */
  public enum ErrorType {
    /** Error de conexión a la base de datos */
    DATABASE_CONNECTION,
    /** Error durante una transacción */
    TRANSACTION_FAILED,
    /** Error de validación de datos */
    VALIDATION_ERROR,
    /** Error inesperado */
    UNEXPECTED_ERROR
  }
}
