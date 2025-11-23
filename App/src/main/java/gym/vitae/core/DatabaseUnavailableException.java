package gym.vitae.core;

public class DatabaseUnavailableException extends RuntimeException {
  public DatabaseUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseUnavailableException(String message) {
    super(message);
  }
}
