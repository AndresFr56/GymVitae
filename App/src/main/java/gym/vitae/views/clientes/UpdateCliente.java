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

/** Formulario para actualizar un cliente existente. */
public class UpdateCliente extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(UpdateCliente.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_NUMERIC_LENGTH = 10;

  private final ClienteController controller;
  private final Cliente clienteActual;

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

  // Estado
  private JComboBox<EstadoCliente> cmbEstado;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public UpdateCliente(ClienteController controller, Cliente cliente) {
    this.controller = controller;
    this.clienteActual = cliente;
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
    loadClienteData();

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

    cmbEstado = new JComboBox<>(EstadoCliente.values());

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

    // El código no es editable
    txtCodigoCliente.setEditable(false);
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

    contentPanel.add(new JLabel("Código de Cliente"));
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

    createTitle("Estado");

    contentPanel.add(new JLabel("Estado *"));
    contentPanel.add(cmbEstado);

    contentPanel.add(new JLabel(" "), "gapy 10");
    contentPanel.add(new JLabel("* Campos obligatorios"), "gapy 0");
  }

  private void createTitle(String title) {
    JLabel lblTitle = new JLabel(title);
    lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lblTitle, "gapy 10 5");
  }

  public void loadClienteData() {
    if (clienteActual == null) {
      throw new IllegalArgumentException("El cliente no puede ser nulo");
    }

    txtCodigoCliente.setText(clienteActual.getCodigoCliente());
    txtNombres.setText(clienteActual.getNombres());
    txtApellidos.setText(clienteActual.getApellidos());
    txtCedula.setText(clienteActual.getCedula());
    cmbGenero.setSelectedItem(clienteActual.getGenero());

    if (clienteActual.getFechaNacimiento() != null) {
      dateFechaNacimiento.setSelectedDate(clienteActual.getFechaNacimiento());
    }

    txtTelefono.setText(clienteActual.getTelefono());
    txtEmail.setText(clienteActual.getEmail() != null ? clienteActual.getEmail() : "");
    txtDireccion.setText(clienteActual.getDireccion() != null ? clienteActual.getDireccion() : "");

    txtContactoEmergencia.setText(
        clienteActual.getContactoEmergencia() != null ? clienteActual.getContactoEmergencia() : "");
    txtTelefonoEmergencia.setText(
        clienteActual.getTelefonoEmergencia() != null ? clienteActual.getTelefonoEmergencia() : "");

    cmbEstado.setSelectedItem(clienteActual.getEstado());

    hideError();
  }

  public boolean validateForm() {
    hideError();

    try {
      // Validar campos obligatorios
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

  public boolean updateCliente() {
    if (clienteActual == null) {
      showErrorMessage("No hay cliente cargado para actualizar");
      return false;
    }

    try {
      // Actualizar datos del cliente
      clienteActual.setNombres(txtNombres.getText().trim());
      clienteActual.setApellidos(txtApellidos.getText().trim());
      clienteActual.setCedula(txtCedula.getText().trim());
      clienteActual.setGenero((Genero) cmbGenero.getSelectedItem());

      // Fecha de nacimiento
      if (dateFechaNacimiento.getSelectedDate() != null) {
        clienteActual.setFechaNacimiento(dateFechaNacimiento.getSelectedDate());
      } else {
        clienteActual.setFechaNacimiento(null);
      }

      // Contacto
      clienteActual.setTelefono(txtTelefono.getText().trim());

      String email = txtEmail.getText().trim();
      clienteActual.setEmail(email.isEmpty() ? null : email);

      String direccion = txtDireccion.getText().trim();
      clienteActual.setDireccion(direccion.isEmpty() ? null : direccion);

      // Emergencia
      String contactoEmergencia = txtContactoEmergencia.getText().trim();
      clienteActual.setContactoEmergencia(contactoEmergencia.isEmpty() ? null : contactoEmergencia);

      String telefonoEmergencia = txtTelefonoEmergencia.getText().trim();
      clienteActual.setTelefonoEmergencia(
          telefonoEmergencia.isEmpty() ? null : telefonoEmergencia);

      // Estado
      clienteActual.setEstado((EstadoCliente) cmbEstado.getSelectedItem());

      controller.updateCliente(clienteActual);

      JOptionPane.showMessageDialog(
          this, "Cliente actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

      return true;

    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al actualizar el cliente: " + e.getMessage());
      showErrorMessage("Error al actualizar el cliente: " + e.getMessage());
      return false;
    }
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

  public Cliente getClienteActual() {
    return clienteActual;
  }
}
