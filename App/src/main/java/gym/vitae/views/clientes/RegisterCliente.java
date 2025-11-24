package gym.vitae.views.clientes;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.ClienteController;
import gym.vitae.model.Cliente;
import gym.vitae.model.enums.EstadoCliente;
import gym.vitae.model.enums.Genero;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

/** Formulario para registrar un nuevo cliente. */
public class RegisterCliente extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(RegisterCliente.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_NUMERIC_LENGTH = 10;

  private final ClienteController controller;

  // Panel de contenido scrollable
  private JPanel contentPanel;
  private JLabel lblError;

  // Información Personal
  private JTextField txtCodigoCliente;
  private JTextField txtNombres;
  private JTextField txtApellidos;
  private JTextField txtCedula;
  private JComboBox<Genero> cmbGenero;
  private DatePicker dateFechaNacimiento;

  // Información de Contacto
  private JTextField txtTelefono;
  private JTextField txtEmail;
  private JTextArea txtDireccion;

  // Información de Emergencia
  private JTextField txtContactoEmergencia;
  private JTextField txtTelefonoEmergencia;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public RegisterCliente(ClienteController controller) {
    this.controller = controller;
    init();
  }

  private void init() {
    setLayout(new BorderLayout());
    setOpaque(false);

    // Panel principal con scroll
    contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[fill]", "[]10[]"));
    contentPanel.setOpaque(false);

    // Label para errores
    lblError = new JLabel();
    lblError.setForeground(new Color(220, 53, 69));
    lblError.setVisible(false);
    contentPanel.add(lblError, "growx");

    initializeComponents();
    applyStyles();
    buildForm();

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

    add(scrollPane, BorderLayout.CENTER);
  }

  private void initializeComponents() {
    txtCodigoCliente = new JTextField();
    txtNombres = new JTextField();
    txtApellidos = new JTextField();
    txtCedula = new JTextField();
    cmbGenero = new JComboBox<>(Genero.values());
    dateFechaNacimiento = new DatePicker();

    txtTelefono = new JTextField();
    txtEmail = new JTextField();
    txtDireccion = new JTextArea(2, 20);
    txtDireccion.setLineWrap(true);
    txtDireccion.setWrapStyleWord(true);

    txtContactoEmergencia = new JTextField();
    txtTelefonoEmergencia = new JTextField();

    btnGuardar = new ButtonOutline();
    btnCancelar = new ButtonOutline();

    // Aplicar filtros
    applyMaxLengthFilter(txtCodigoCliente, 20);
    applyLettersAndSpacesFilter(txtNombres, MAX_TEXT_LENGTH);
    applyLettersAndSpacesFilter(txtApellidos, MAX_TEXT_LENGTH);
    applyNumericFilter(txtCedula, MAX_NUMERIC_LENGTH);
    applyNumericFilter(txtTelefono, MAX_NUMERIC_LENGTH);
    applyMaxLengthFilter(txtEmail, MAX_TEXT_LENGTH);
    applyMaxLengthFilter(txtDireccion, MAX_TEXT_LENGTH);
    applyLettersAndSpacesFilter(txtContactoEmergencia, MAX_TEXT_LENGTH);
    applyNumericFilter(txtTelefonoEmergencia, MAX_NUMERIC_LENGTH);

    // Configurar DatePicker
    dateFechaNacimiento.setEditor(new JFormattedTextField());
  }

  private void applyStyles() {
    txtCodigoCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: CLI-001");
    txtNombres.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombres del cliente");
    txtApellidos.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Apellidos del cliente");
    txtCedula.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "10 dígitos");
    txtTelefono.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "10 dígitos");
    txtEmail.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "correo@ejemplo.com (opcional)");
    txtDireccion.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Dirección completa (opcional)");

    txtCodigoCliente.putClientProperty(FlatClientProperties.STYLE, "arc:8");
    txtNombres.putClientProperty(FlatClientProperties.STYLE, "arc:8");
    txtApellidos.putClientProperty(FlatClientProperties.STYLE, "arc:8");
    txtCedula.putClientProperty(FlatClientProperties.STYLE, "arc:8");
    txtTelefono.putClientProperty(FlatClientProperties.STYLE, "arc:8");
    txtEmail.putClientProperty(FlatClientProperties.STYLE, "arc:8");
    txtDireccion.putClientProperty(FlatClientProperties.STYLE, "arc:8");
  }

  /** Aplica filtro para permitir solo letras y espacios. */
  private void applyLettersAndSpacesFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (isValidLettersAndSpaces(string)
                    && (fb.getDocument().getLength() + string.length() <= maxLength)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (isValidLettersAndSpaces(text)
                    && (fb.getDocument().getLength() - length + text.length() <= maxLength)) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }

              private boolean isValidLettersAndSpaces(String text) {
                return text.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$");
              }
            });
  }

  /** Aplica filtro para permitir solo números. */
  private void applyNumericFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d*")
                    && (fb.getDocument().getLength() + string.length() <= maxLength)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (text.matches("\\d*")
                    && (fb.getDocument().getLength() - length + text.length() <= maxLength)) {
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
              public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (fb.getDocument().getLength() + string.length() <= maxLength) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (fb.getDocument().getLength() - length + text.length() <= maxLength) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  private void buildForm() {
    createTitle("Información Personal");

    contentPanel.add(new JLabel("Código de Cliente *"));
    contentPanel.add(txtCodigoCliente);

    contentPanel.add(new JLabel("Nombres *"));
    contentPanel.add(txtNombres);

    contentPanel.add(new JLabel("Apellidos *"));
    contentPanel.add(txtApellidos);

    contentPanel.add(new JLabel("Cédula *"));
    contentPanel.add(txtCedula);

    contentPanel.add(new JLabel("Género *"));
    contentPanel.add(cmbGenero);

    contentPanel.add(new JLabel("Fecha de Nacimiento"));
    contentPanel.add(dateFechaNacimiento);

    createTitle("Información de Contacto");

    contentPanel.add(new JLabel("Teléfono *"));
    contentPanel.add(txtTelefono);

    contentPanel.add(new JLabel("Email"));
    contentPanel.add(txtEmail);

    contentPanel.add(new JLabel("Dirección"));
    JScrollPane scrollDir = new JScrollPane(txtDireccion);
    scrollDir.setPreferredSize(new Dimension(0, 60));
    contentPanel.add(scrollDir);

    createTitle("Contacto de Emergencia");

    contentPanel.add(new JLabel("Nombre del Contacto"));
    contentPanel.add(txtContactoEmergencia);

    contentPanel.add(new JLabel("Teléfono de Emergencia"));
    contentPanel.add(txtTelefonoEmergencia);

    contentPanel.add(new JLabel(" "), "gapy 10");
    contentPanel.add(new JLabel("* Campos obligatorios"), "gapy 0");
  }

  private void createTitle(String title) {
    JLabel lblTitle = new JLabel(title);
    lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lblTitle, "gapy 10 5");
  }

  public boolean validateForm() {
    hideError();

    try {
      // Validar campos obligatorios
      if (txtCodigoCliente.getText().trim().isEmpty()) {
        showErrorMessage("El código de cliente es obligatorio");
        txtCodigoCliente.requestFocus();
        return false;
      }

      if (txtNombres.getText().trim().isEmpty()) {
        showErrorMessage("Los nombres son obligatorios");
        txtNombres.requestFocus();
        return false;
      }

      if (txtApellidos.getText().trim().isEmpty()) {
        showErrorMessage("Los apellidos son obligatorios");
        txtApellidos.requestFocus();
        return false;
      }

      if (txtCedula.getText().trim().isEmpty()) {
        showErrorMessage("La cédula es obligatoria");
        txtCedula.requestFocus();
        return false;
      }

      if (txtCedula.getText().trim().length() != 10) {
        showErrorMessage("La cédula debe tener 10 dígitos");
        txtCedula.requestFocus();
        return false;
      }

      if (txtTelefono.getText().trim().isEmpty()) {
        showErrorMessage("El teléfono es obligatorio");
        txtTelefono.requestFocus();
        return false;
      }

      if (txtTelefono.getText().trim().length() != 10) {
        showErrorMessage("El teléfono debe tener 10 dígitos");
        txtTelefono.requestFocus();
        return false;
      }

      // Validar email si se proporcionó
      String email = txtEmail.getText().trim();
      if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        showErrorMessage("El formato del email no es válido");
        txtEmail.requestFocus();
        return false;
      }

      return true;
    } catch (Exception e) {
      showErrorMessage("Error al validar el formulario: " + e.getMessage());
      return false;
    }
  }

  public boolean saveCliente() {
    try {
      Cliente cliente = buildClienteFromForm();
      controller.createCliente(cliente);

      JOptionPane.showMessageDialog(
          this,
          "Cliente registrado exitosamente con código: " + cliente.getCodigoCliente(),
          "Éxito",
          JOptionPane.INFORMATION_MESSAGE);

      clearForm();
      return true;

    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al guardar el cliente: " + e.getMessage());
      showErrorMessage("Error al guardar el cliente: " + e.getMessage());
      return false;
    }
  }

  private Cliente buildClienteFromForm() {
    Cliente cliente = new Cliente();
    cliente.setCodigoCliente(txtCodigoCliente.getText().trim());
    cliente.setNombres(txtNombres.getText().trim());
    cliente.setApellidos(txtApellidos.getText().trim());
    cliente.setCedula(txtCedula.getText().trim());
    cliente.setGenero((Genero) cmbGenero.getSelectedItem());

    // Fecha de nacimiento (opcional)
    if (dateFechaNacimiento.getSelectedDate() != null) {
      cliente.setFechaNacimiento(dateFechaNacimiento.getSelectedDate());
    }

    // Contacto
    cliente.setTelefono(txtTelefono.getText().trim());

    String email = txtEmail.getText().trim();
    cliente.setEmail(email.isEmpty() ? null : email);

    String direccion = txtDireccion.getText().trim();
    cliente.setDireccion(direccion.isEmpty() ? null : direccion);

    // Emergencia (opcional)
    String contactoEmergencia = txtContactoEmergencia.getText().trim();
    cliente.setContactoEmergencia(contactoEmergencia.isEmpty() ? null : contactoEmergencia);

    String telefonoEmergencia = txtTelefonoEmergencia.getText().trim();
    cliente.setTelefonoEmergencia(telefonoEmergencia.isEmpty() ? null : telefonoEmergencia);

    cliente.setEstado(EstadoCliente.ACTIVO);

    return cliente;
  }

  private void clearForm() {
    txtCodigoCliente.setText("");
    txtNombres.setText("");
    txtApellidos.setText("");
    txtCedula.setText("");
    cmbGenero.setSelectedIndex(0);
    dateFechaNacimiento.clearSelectedDate();
    txtTelefono.setText("");
    txtEmail.setText("");
    txtDireccion.setText("");
    txtContactoEmergencia.setText("");
    txtTelefonoEmergencia.setText("");
    hideError();
  }

  private void showErrorMessage(String message) {
    lblError.setText(message);
    lblError.setVisible(true);
  }

  private void hideError() {
    lblError.setVisible(false);
  }

  public ButtonOutline getBtnGuardar() {
    return btnGuardar;
  }

  public ButtonOutline getBtnCancelar() {
    return btnCancelar;
  }
}
