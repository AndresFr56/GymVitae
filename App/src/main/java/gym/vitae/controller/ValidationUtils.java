package gym.vitae.controller;

import java.time.LocalDate;

/** Utilidades de validación compartidas entre controllers. */
public final class ValidationUtils {

  private ValidationUtils() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static void validateNombres(String nombres) {
    validateRequiredString(nombres, "Los nombres", 100);
  }

  public static void validateApellidos(String apellidos) {
    validateRequiredString(apellidos, "Los apellidos", 100);
  }

  public static void validateRequiredString(String value, String fieldName, int maxLength) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException(fieldName + " son obligatorios");
    }
    if (value.length() > maxLength) {
      throw new IllegalArgumentException(
          fieldName + " no pueden exceder " + maxLength + " caracteres");
    }

    if (!value.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$")) {
      throw new IllegalArgumentException(fieldName + " solo pueden contener letras y espacios");
    }
  }

  public static void validateOptionalString(String value, String fieldName, int maxLength) {
    if (value != null && value.length() > maxLength) {
      throw new IllegalArgumentException(
          fieldName + " no puede exceder " + maxLength + " caracteres");
    }
  }

  public static void validateCedula(String cedula) {
    if (cedula == null || cedula.trim().isEmpty()) {
      throw new IllegalArgumentException("La cédula es obligatoria");
    }
    if (!cedula.matches("\\d{10}")) {
      throw new IllegalArgumentException("La cédula debe tener exactamente 10 dígitos numéricos");
    }
  }

  public static void validateTelefono(String telefono) {
    if (telefono == null || telefono.trim().isEmpty()) {
      throw new IllegalArgumentException("El teléfono es obligatorio");
    }
    if (!telefono.matches("\\d{10}")) {
      throw new IllegalArgumentException("El teléfono debe tener exactamente 10 dígitos numéricos");
    }

    // inicio  con 09
    if (!telefono.startsWith("09")) {
      throw new IllegalArgumentException("El teléfono debe iniciar con '09'");
    }
  }

  public static void validateTelefonoOpcional(String telefono, String fieldName) {
    if (telefono != null && !telefono.trim().isEmpty() && !telefono.matches("\\d{10}")) {
      throw new IllegalArgumentException(
          fieldName + " debe tener exactamente 10 dígitos numéricos");
    }
  }

  public static void validateEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("El email es obligatorio");
    }
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new IllegalArgumentException("El formato del email no es válido");
    }
    if (email.length() > 100) {
      throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
    }
  }

  public static void validateEmailOpcional(String email) {
    if (email != null && !email.trim().isEmpty()) {
      if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        throw new IllegalArgumentException("El formato del email no es válido");
      }
      if (email.length() > 100) {
        throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
      }
    }
  }

  public static void validateDireccion(String direccion) {
    validateOptionalString(direccion, "La dirección", 100);
    if (direccion == null || direccion.trim().length() < 5) {
      throw new IllegalArgumentException("La dirección debe tener al menos 5 caracteres");
    }
  }

  public static void validateFechaIngreso(LocalDate fechaIngreso) {
    if (fechaIngreso == null) {
      throw new IllegalArgumentException("La fecha de ingreso es obligatoria");
    }
    if (fechaIngreso.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("La fecha de ingreso no puede ser futura");
    }
  }

  public static void validateFechaSalida(LocalDate fechaIngreso, LocalDate fechaSalida) {
    if (fechaSalida != null && fechaSalida.isBefore(fechaIngreso)) {
      throw new IllegalArgumentException(
          "La fecha de salida no puede ser anterior a la fecha de ingreso");
    }
  }

  public static void validateFechaNacimiento(LocalDate fechaNacimiento) {
    if (fechaNacimiento != null && fechaNacimiento.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
    }
    if (fechaNacimiento != null && fechaNacimiento.isBefore(LocalDate.now().minusYears(120))) {
      throw new IllegalArgumentException("La fecha de nacimiento no puede ser anterior a 120 años");
    }
  }

  public static void validateId(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID no es valido");
    }
  }

  public static void validateCargoId(Integer cargoId) {
    if (cargoId == null || cargoId <= 0) {
      throw new IllegalArgumentException("El cargo es obligatorio");
    }
  }

  public static void validatePagination(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("El offset no puede ser negativo");
    }
    if (limit <= 0) {
      throw new IllegalArgumentException("El limit debe ser mayor a 0");
    }
  }
}
