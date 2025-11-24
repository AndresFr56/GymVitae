package gym.vitae.views.personal;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.PersonalController;
import gym.vitae.model.Cargo;
import gym.vitae.model.Empleado;
import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

/** Formulario para registrar un nuevo empleado. */
public class RegisterPersonal extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(RegisterPersonal.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_NUMERIC_LENGTH = 10;

  private static final PersonalController controller = new PersonalController();

  // Panel de contenido scrollable
  private JPanel contentPanel;
  private JLabel lblError;

  // Información Personal
  private JTextField txtNombres;
  private JTextField txtApellidos;
  private JTextField txtCedula;
  private JComboBox<Genero> cmbGenero;

  // Información de Contacto
  private JTextField txtTelefono;
  private JTextField txtEmail;
  private JTextArea txtDireccion;

  // Información Laboral
  private JComboBox<CargoWrapper> cmbCargo;
  private DatePicker dateFechaIngreso;
  private JComboBox<TipoContrato> cmbTipoContrato;
  private JComboBox<EstadoEmpleado> cmbEstado;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public RegisterPersonal() {
    init();
  }

  private void init() {
    setLayout(new BorderLayout());

    // Panel superior con mensaje de error
    lblError = new JLabel();
    lblError.setVisible(false);
    lblError.putClientProperty(
        FlatClientProperties.STYLE, "foreground:#F44336;font:bold;border:0,0,10,0");

    // Panel de contenido con scroll
    contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 0", "[fill]", ""));
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    // Panel de botones fijo en la parte inferior
    JPanel buttonPanel = createButtonPanel();

    // Ensamblar layout
    JPanel topPanel = new JPanel(new MigLayout("fillx,wrap,insets 10 20 0 20", "[fill]"));
    topPanel.add(lblError);

    add(topPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    initializeComponents();
    buildForm();
    loadCargos();
  }

  private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new MigLayout("insets 15 20 20 20", "[grow][120][120]"));
    panel.putClientProperty(
        FlatClientProperties.STYLE,
        "background:$Panel.background;border:1,0,0,0,$Component.borderColor");

    btnCancelar = new ButtonOutline("Cancelar");
    btnCancelar.putClientProperty(FlatClientProperties.STYLE, "font:bold +1");

    btnGuardar = new ButtonOutline("Guardar");
    btnGuardar.putClientProperty(FlatClientProperties.STYLE, "foreground:#4CAF50;font:bold +1");

    panel.add(new JLabel(), "grow");
    panel.add(btnCancelar, "center");
    panel.add(btnGuardar, "center");

    return panel;
  }

  private void initializeComponents() {
    // Información Personal
    txtNombres = new JTextField();
    applyLettersAndSpacesFilter(txtNombres, MAX_TEXT_LENGTH);

    txtApellidos = new JTextField();
    applyLettersAndSpacesFilter(txtApellidos, MAX_TEXT_LENGTH);

    txtCedula = new JTextField();
    applyNumericFilter(txtCedula, MAX_NUMERIC_LENGTH);

    cmbGenero = new JComboBox<>(Genero.values());

    // Información de Contacto
    txtTelefono = new JTextField();
    applyNumericFilter(txtTelefono, MAX_NUMERIC_LENGTH);

    txtEmail = new JTextField();
    applyMaxLengthFilter(txtEmail, MAX_TEXT_LENGTH);

    txtDireccion = new JTextArea(3, 20);
    txtDireccion.setWrapStyleWord(true);
    txtDireccion.setLineWrap(true);
    applyMaxLengthFilter(txtDireccion, MAX_TEXT_LENGTH);

    // Información Laboral
    cmbCargo = new JComboBox<>();
    dateFechaIngreso = new DatePicker();
    dateFechaIngreso.setSelectedDate(LocalDate.now());
    cmbTipoContrato = new JComboBox<>(TipoContrato.values());
    cmbEstado = new JComboBox<>(EstadoEmpleado.values());
    cmbEstado.setSelectedItem(EstadoEmpleado.ACTIVO);
    cmbEstado.setEnabled(false); // Auto-generado

    applyStyles();
  }

  private void applyStyles() {
    // Placeholders
    txtNombres.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Solo letras y espacios");
    txtApellidos.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Solo letras y espacios");
    txtCedula.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "10 dígitos");
    txtTelefono.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "10 dígitos");
    txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ejemplo@gymvitae.com");
    txtDireccion.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Máximo 100 caracteres");
  }

  /** Aplica filtro para permitir solo letras y espacios. */
  private void applyLettersAndSpacesFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (isValidLettersAndSpaces(string)
                    && (fb.getDocument().getLength() + string.length()) <= maxLength) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (isValidLettersAndSpaces(text)
                    && (fb.getDocument().getLength() - length + text.length()) <= maxLength) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }

              private boolean isValidLettersAndSpaces(String text) {
                return text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*");
              }
            });
  }

  /** Aplica filtro para permitir solo números. */
  private void applyNumericFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d*")
                    && (fb.getDocument().getLength() + string.length()) <= maxLength) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (text.matches("\\d*")
                    && (fb.getDocument().getLength() - length + text.length()) <= maxLength) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  /** Aplica filtro de longitud máxima. */
  private void applyMaxLengthFilter(JTextComponent textComponent, int maxLength) {
    ((AbstractDocument) textComponent.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if ((fb.getDocument().getLength() + string.length()) <= maxLength) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if ((fb.getDocument().getLength() - length + text.length()) <= maxLength) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  private void buildForm() {
    // Información Personal
    createTitle("Información Personal");
    contentPanel.add(new JLabel("Nombres *"), "gapy 5 0");
    contentPanel.add(txtNombres);
    contentPanel.add(new JLabel("Apellidos *"), "gapy 5 0");
    contentPanel.add(txtApellidos);
    contentPanel.add(new JLabel("Cédula *"), "gapy 5 0");
    contentPanel.add(txtCedula);
    contentPanel.add(new JLabel("Género *"), "gapy 5 0");
    contentPanel.add(cmbGenero);

    // Información de Contacto
    createTitle("Información de Contacto");
    contentPanel.add(new JLabel("Teléfono *"), "gapy 5 0");
    contentPanel.add(txtTelefono);
    contentPanel.add(new JLabel("Email"), "gapy 5 0");
    contentPanel.add(txtEmail);
    contentPanel.add(new JLabel("Dirección"), "gapy 5 0");
    JScrollPane scroll = new JScrollPane(txtDireccion);
    scroll.setBorder(null);
    contentPanel.add(scroll, "height 80,grow");

    // Información Laboral
    createTitle("Información Laboral");
    contentPanel.add(new JLabel("Cargo *"), "gapy 5 0");
    contentPanel.add(cmbCargo);
    contentPanel.add(new JLabel("Fecha de Ingreso *"), "gapy 5 0");
    contentPanel.add(dateFechaIngreso);
    contentPanel.add(new JLabel("Tipo de Contrato *"), "gapy 5 0");
    contentPanel.add(cmbTipoContrato);
    contentPanel.add(new JLabel("Estado"), "gapy 5 0");
    contentPanel.add(cmbEstado);

    // Espacio adicional al final para scroll
    contentPanel.add(new JLabel(), "height 20");
  }

  private void createTitle(String title) {
    JLabel lb = new JLabel(title);
    lb.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lb, "gapy 15 0");
    contentPanel.add(new JSeparator(), "height 2!,gapy 0 5");
  }

  private void loadCargos() {
    try {
      List<Cargo> cargos = controller.getCargos();
      cmbCargo.removeAllItems();

      cargos.stream()
          .filter(Cargo::getActivo)
          .forEach(cargo -> cmbCargo.addItem(new CargoWrapper(cargo)));

      if (cmbCargo.getItemCount() == 0) {
        showErrorMessage("No hay cargos disponibles. Por favor, cree un cargo primero.");
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al cargar cargos", e);
      showErrorMessage("Error al cargar cargos: " + e.getMessage());
    }
  }

  public boolean validateForm() {
    hideError();

    // Verificar campos obligatorios vacíos
    if (txtNombres.getText().trim().isEmpty()
        || txtApellidos.getText().trim().isEmpty()
        || txtCedula.getText().trim().isEmpty()
        || txtTelefono.getText().trim().isEmpty()
        || cmbCargo.getSelectedItem() == null
        || dateFechaIngreso.getSelectedDate() == null) {
      showErrorMessage("Por favor rellenar todos los campos obligatorios");
      return false;
    }

    // Validar longitud de cédula
    if (txtCedula.getText().trim().length() != 10) {
      showErrorMessage("La cédula debe contener exactamente 10 dígitos");
      txtCedula.requestFocus();
      return false;
    }

    // Validar longitud de teléfono
    if (txtTelefono.getText().trim().length() != 10) {
      showErrorMessage("El teléfono debe contener exactamente 10 dígitos");
      txtTelefono.requestFocus();
      return false;
    }

    // Validar email si no está vacío
    String email = txtEmail.getText().trim();
    if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      showErrorMessage("El formato del email no es válido");
      txtEmail.requestFocus();
      return false;
    }

    // Validar fecha de ingreso no futura
    if (dateFechaIngreso.getSelectedDate() != null
        && dateFechaIngreso.getSelectedDate().isAfter(LocalDate.now())) {
      showErrorMessage("La fecha de ingreso no puede ser posterior a la fecha actual");
      dateFechaIngreso.requestFocus();
      return false;
    }

    return true;
  }

  public boolean saveEmpleado() {
    if (!validateForm()) {
      return false;
    }

    try {
      Empleado empleado = buildEmpleadoFromForm();
      LOGGER.info(
          "Intentando guardar empleado: " + empleado.getNombres() + " " + empleado.getApellidos());

      Empleado saved = controller.createEmpleado(empleado);

      LOGGER.info(
          "Empleado guardado exitosamente con ID: "
              + saved.getId()
              + " y código: "
              + saved.getCodigoEmpleado());

      // Mostrar mensaje de éxito
      JOptionPane.showMessageDialog(
          this,
          "Los datos han sido guardados correctamente",
          "Éxito",
          JOptionPane.INFORMATION_MESSAGE);

      clearForm();
      hideError();
      return true;
    } catch (IllegalArgumentException e) {
      // Errores de validación del controller
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al guardar empleado", e);
      showErrorMessage("Error al guardar: " + e.getMessage());
      return false;
    }
  }

  private Empleado buildEmpleadoFromForm() {
    Empleado empleado = new Empleado();

    // El código de empleado será auto-generado por la base de datos
    empleado.setNombres(txtNombres.getText().trim());
    empleado.setApellidos(txtApellidos.getText().trim());
    empleado.setCedula(txtCedula.getText().trim());
    empleado.setGenero((Genero) cmbGenero.getSelectedItem());
    empleado.setTelefono(txtTelefono.getText().trim());

    String email = txtEmail.getText().trim();
    if (!email.isEmpty()) {
      empleado.setEmail(email);
    }

    String direccion = txtDireccion.getText().trim();
    if (!direccion.isEmpty()) {
      empleado.setDireccion(direccion);
    }

    CargoWrapper cargoWrapper = (CargoWrapper) cmbCargo.getSelectedItem();
    if (cargoWrapper != null) {
      empleado.setCargo(cargoWrapper.cargo());
    }

    LocalDate fechaIngreso = dateFechaIngreso.getSelectedDate();
    if (fechaIngreso != null) {
      empleado.setFechaIngreso(fechaIngreso);
    }

    empleado.setTipoContrato((TipoContrato) cmbTipoContrato.getSelectedItem());
    empleado.setEstado((EstadoEmpleado) cmbEstado.getSelectedItem());

    return empleado;
  }

  private void clearForm() {
    hideError();
    txtNombres.setText("");
    txtApellidos.setText("");
    txtCedula.setText("");
    cmbGenero.setSelectedIndex(0);
    txtTelefono.setText("");
    txtEmail.setText("");
    txtDireccion.setText("");
    dateFechaIngreso.setSelectedDate(LocalDate.now());
    cmbTipoContrato.setSelectedIndex(0);
    cmbEstado.setSelectedItem(EstadoEmpleado.ACTIVO);

    if (cmbCargo.getItemCount() > 0) {
      cmbCargo.setSelectedIndex(0);
    }

    txtNombres.requestFocus();
  }

  /** Muestra un mensaje de error en el label superior en color rojo. */
  private void showErrorMessage(String message) {
    lblError.setText(message);
    lblError.setVisible(true);
    revalidate();
    repaint();
  }

  /** Oculta el mensaje de error. */
  private void hideError() {
    lblError.setText("");
    lblError.setVisible(false);
  }

  public ButtonOutline getBtnGuardar() {
    return btnGuardar;
  }

  public ButtonOutline getBtnCancelar() {
    return btnCancelar;
  }

  /** Wrapper para mostrar el nombre del cargo en el ComboBox. */
  private record CargoWrapper(Cargo cargo) {

    @Override
    public String toString() {
      return cargo.getNombre();
    }
  }
}
