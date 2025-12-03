package gym.vitae.views.proveedores;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.ProveedoresController;
import gym.vitae.model.dtos.inventario.ProveedorDetalleDTO;
import gym.vitae.model.dtos.inventario.ProveedorUpdateDTO;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;

/** Formulario para actualización del proveedor */
public class UpdateProveedor extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(UpdateProveedor.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_NUMERIC_LENGTH = 10;

  private final transient ProveedoresController controller;
  private final transient ProveedorDetalleDTO proveedorDetalle;

  // Panel de contenido scrolleable
  private JPanel contentPanel;
  private JLabel lblError;

  // Campos del formulario
  private JTextField txtCodigoProveedor;
  private JTextField txtNombreProveedor;
  private JTextField txtContactoProveedor;
  private JTextField txtTelefonoProveedor;
  private JTextField txtEmailProveedor;
  private JTextArea txtDireccionProveedor;

  // Estado del proveedor
  private JComboBox<String> cmbEstadoProveedor;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public UpdateProveedor(ProveedoresController controller, ProveedorDetalleDTO detalle) {
    this.controller = controller;
    this.proveedorDetalle = detalle;
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
    applyStyles();
    buildForm();
    loadProveedorData();
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
    txtCodigoProveedor = new JTextField();
    txtNombreProveedor = new JTextField();
    txtContactoProveedor = new JTextField();
    txtTelefonoProveedor = new JTextField();
    txtEmailProveedor = new JTextField();
    txtDireccionProveedor = new JTextArea(2, 20);
    txtDireccionProveedor.setLineWrap(true);
    txtDireccionProveedor.setWrapStyleWord(true);
    cmbEstadoProveedor = new JComboBox<>(new String[] {"ACTIVO", "INACTIVO"});
    txtCodigoProveedor.setEditable(false);
  }

  private void applyInputFilters() {
    applyCompanyNameFilter(txtNombreProveedor, MAX_TEXT_LENGTH);
    applyLettersAndSpacesFilter(txtContactoProveedor, MAX_TEXT_LENGTH);
    applyNumericFilter(txtTelefonoProveedor, MAX_NUMERIC_LENGTH);
    applyMaxLengthFilter(txtEmailProveedor, MAX_TEXT_LENGTH);
    applyMaxLengthFilter(txtDireccionProveedor, MAX_TEXT_LENGTH);
  }

  private void applyStyles() {
    txtNombreProveedor.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Suplementos Ecuador S.A.");
    txtContactoProveedor.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ej: María Fabiani");
    txtTelefonoProveedor.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: 0998765432");
    txtEmailProveedor.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ej: ventas@proveedor.com");
    txtDireccionProveedor.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Av. Barcelona y República");
  }

  private void applyMaxLengthFilter(JTextComponent textComponent, int maxLength) {
    ((AbstractDocument) textComponent.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) {
                  return;
                }
                if (fb.getDocument().getLength() + string.length() <= maxLength) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) {
                  return;
                }
                if (fb.getDocument().getLength() - length + text.length() <= maxLength) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  private void applyLettersAndSpacesFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) {
                  return;
                }
                if (isValidLettersAndSpaces(string)
                    && (fb.getDocument().getLength() + string.length() <= maxLength)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) {
                  return;
                }
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

  private void applyCompanyNameFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string != null
                    && isValidCompanyName(string)
                    && fb.getDocument().getLength() + string.length() <= maxLength) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text != null
                    && isValidCompanyName(text)
                    && fb.getDocument().getLength() - length + text.length() <= maxLength) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }

              private boolean isValidCompanyName(String text) {
                return text.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ .,&\\-]*$");
              }
            });
  }

  private void applyNumericFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) {
                  return;
                }
                if (string.matches("\\d*")
                    && (fb.getDocument().getLength() + string.length() <= maxLength)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) {
                  return;
                }
                if (text.matches("\\d*")
                    && (fb.getDocument().getLength() - length + text.length() <= maxLength)) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  private void buildForm() {
    createTitle("Información del Proveedor");
    contentPanel.add(new JLabel("Codigo del Proveedor *"));
    contentPanel.add(txtCodigoProveedor);

    contentPanel.add(new JLabel("Nombre del Proveedor *"));
    contentPanel.add(txtNombreProveedor);

    contentPanel.add(new JLabel("Contacto del Proveedor *"));
    contentPanel.add(txtContactoProveedor);

    contentPanel.add(new JLabel("Telefono del Proveedor *"));
    contentPanel.add(txtTelefonoProveedor);

    contentPanel.add(new JLabel("Email del Proveedor *"));
    contentPanel.add(txtEmailProveedor);

    contentPanel.add(new JLabel("Direccion del Proveedor *"));
    JScrollPane scrollDir = new JScrollPane(txtDireccionProveedor);
    scrollDir.setPreferredSize(new Dimension(0, 60));
    contentPanel.add(scrollDir);

    contentPanel.add(new JLabel("Estado del Proveedor *"));
    contentPanel.add(cmbEstadoProveedor);

    contentPanel.add(new JLabel(" "), "gapy 10");
    contentPanel.add(new JLabel("* Campos obligatorios"), "gapy 0");
  }

  public void loadProveedorData() {
    if (proveedorDetalle == null) {
      throw new IllegalArgumentException("El proveedor no puede ser nulo");
    }
    txtCodigoProveedor.setText(proveedorDetalle.getCodigo());
    txtNombreProveedor.setText(proveedorDetalle.getNombre());
    txtContactoProveedor.setText(proveedorDetalle.getContacto());
    txtTelefonoProveedor.setText(proveedorDetalle.getTelefono());
    txtEmailProveedor.setText(proveedorDetalle.getEmail());
    txtDireccionProveedor.setText(proveedorDetalle.getDireccion());
    cmbEstadoProveedor.setSelectedItem(proveedorDetalle.getActivo() ? "ACTIVO" : "INACTIVO");
    hideErrors();
    applyInputFilters();
  }

  public boolean validateForm() {
    hideErrors();
    try {
      if (txtCodigoProveedor.getText().trim().isEmpty()) {
        showErrorMessage("El codigo del proveedor es obligatorio");
        txtCodigoProveedor.requestFocus();
        return false;
      }

      if (txtNombreProveedor.getText().trim().isEmpty()) {
        showErrorMessage("El nombre del proveedor es obligatorio");
        txtNombreProveedor.requestFocus();
        return false;
      }

      if (txtContactoProveedor.getText().trim().isEmpty()) {
        showErrorMessage("El contacto del proveedor es obligatorio");
        txtContactoProveedor.requestFocus();
        return false;
      }

      if (txtTelefonoProveedor.getText().trim().isEmpty()) {
        showErrorMessage("El teléfono es obligatorio");
        txtTelefonoProveedor.requestFocus();
        return false;
      }

      if (txtTelefonoProveedor.getText().trim().length() != 10) {
        showErrorMessage("El telefono debe tener 10 dígitos");
        txtTelefonoProveedor.requestFocus();
        return false;
      }

      String email = txtEmailProveedor.getText().trim();
      if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        showErrorMessage("El formato del email no es válido");
        txtEmailProveedor.requestFocus();
        return false;
      }

      if (txtDireccionProveedor.getText().trim().isEmpty()) {
        showErrorMessage("La direccion del proveedor es obligatoria");
        txtDireccionProveedor.requestFocus();
        return false;
      }
      return true;
    } catch (Exception e) {
      showErrorMessage("Error al validar el formulario: " + e.getMessage());
      return false;
    }
  }

  public boolean updateProveedor() {
    if (proveedorDetalle == null) {
      showErrorMessage("No se ha cargado el cliente a actualizar");
      return false;
    }

    try {
      ProveedorUpdateDTO updateDTO =
          new ProveedorUpdateDTO(
              txtNombreProveedor.getText().trim(),
              txtContactoProveedor.getText().trim(),
              txtTelefonoProveedor.getText().trim(),
              txtEmailProveedor.getText().trim(),
              txtDireccionProveedor.getText().trim(),
              "ACTIVO".equals(cmbEstadoProveedor.getSelectedItem()));
      controller.updateProveedor(proveedorDetalle.getId(), updateDTO);
      JOptionPane.showMessageDialog(
          this, "Proveedor Actualizado Correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      return true;
    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al actualizar al proveedor: " + e.getMessage());
      showErrorMessage("Error al actualizar al proveedor: " + e.getMessage());
      return false;
    }
  }

  private void createTitle(String title) {
    JLabel lblTitle = new JLabel(title);
    lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lblTitle, "gapy 10 5");
  }

  private void showErrorMessage(String message) {
    lblError.setText(message);
    lblError.setVisible(true);
  }

  private void hideErrors() {
    lblError.setVisible(false);
  }

  public ButtonOutline getBtnGuardar() {
    return btnGuardar;
  }

  public ButtonOutline getBtnCancelar() {
    return btnCancelar;
  }
}
