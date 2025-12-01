package gym.vitae.views.membresias;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.model.dtos.membresias.TipoMembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaUpdateDTO;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.math.BigDecimal;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;

/**
 * Formulario para actualizar un tipo de membresía existente.
 */
public class UpdateTipoMembresia extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(UpdateTipoMembresia.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;

  private final TiposMembresiaController controller;
  private final TipoMembresiaDetalleDTO tipoDetalle;

  private JPanel contentPanel;
  private JLabel lblError;

  // Campos del formulario
  private JTextField txtNombre;
  private JTextArea txtDescripcion;
  private JTextField txtDuracionDias;
  private JTextField txtCosto;
  private JCheckBox chkAccesoCompleto;
  private JComboBox<String> cmbEstado;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public UpdateTipoMembresia(TiposMembresiaController controller, TipoMembresiaDetalleDTO tipoDetalle) {
    this.controller = controller;
    this.tipoDetalle = tipoDetalle;
    init();
  }

  private void init() {
    setLayout(new BorderLayout());

    lblError = new JLabel();
    lblError.setVisible(false);
    lblError.putClientProperty(
        FlatClientProperties.STYLE, "foreground:#F44336;font:bold;border:0,0,10,0");

    contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 0", "[fill]", ""));
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    JPanel buttonPanel = createButtonPanel();

    JPanel topPanel = new JPanel(new MigLayout("fillx,wrap,insets 10 20 0 20", "[fill]"));
    topPanel.add(lblError);

    add(topPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    initializeComponents();
    applyStyles();
    buildForm();
    loadTipoData();
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
    txtNombre = new JTextField();
    
    txtDescripcion = new JTextArea(3, 20);
    txtDescripcion.setLineWrap(true);
    txtDescripcion.setWrapStyleWord(true);
    
    txtDuracionDias = new JTextField();
    txtCosto = new JTextField();
    chkAccesoCompleto = new JCheckBox("El cliente tendrá acceso completo al gimnasio");
    
    cmbEstado = new JComboBox<>(new String[] {"ACTIVO", "INACTIVO"});
  }

  private void applyStyles() {
    txtNombre.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Mensual Básica");
    txtDescripcion.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Descripción del tipo de membresía");
    txtDuracionDias.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: 30");
    txtCosto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: 35.00");
  }

  private void applyInputFilters() {
    applyMaxLengthFilter(txtNombre, 50);
    applyMaxLengthFilter(txtDescripcion, 255);
    applyNumericFilter(txtDuracionDias, 5);
    applyDecimalFilter(txtCosto, 10);
  }

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

  private void applyDecimalFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (string.matches("[0-9.]*")
                    && (fb.getDocument().getLength() + string.length() <= maxLength)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (text.matches("[0-9.]*")
                    && (fb.getDocument().getLength() - length + text.length() <= maxLength)) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  private void applyMaxLengthFilter(JTextComponent textComponent, int maxLength) {
    ((AbstractDocument) textComponent.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
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
    createTitle("Información del Tipo de Membresía");

    contentPanel.add(new JLabel("Nombre *"));
    contentPanel.add(txtNombre);

    contentPanel.add(new JLabel("Descripción"));
    JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
    scrollDesc.setPreferredSize(new Dimension(0, 80));
    contentPanel.add(scrollDesc);

    contentPanel.add(new JLabel("Duración en días *"));
    contentPanel.add(txtDuracionDias);

    contentPanel.add(new JLabel("Costo *"));
    contentPanel.add(txtCosto);

    contentPanel.add(new JLabel(" "));
    contentPanel.add(chkAccesoCompleto);

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

  private void loadTipoData() {
    if (tipoDetalle == null) {
      throw new IllegalArgumentException("El tipo de membresía no puede ser nulo");
    }

    txtNombre.setText(tipoDetalle.getNombre());
    txtDescripcion.setText(tipoDetalle.getDescripcion() != null ? tipoDetalle.getDescripcion() : "");
    txtDuracionDias.setText(tipoDetalle.getDuracionDias().toString());
    txtCosto.setText(tipoDetalle.getCosto().toString());
    chkAccesoCompleto.setSelected(tipoDetalle.getAccesoCompleto());
    cmbEstado.setSelectedItem(tipoDetalle.getActivo() ? "ACTIVO" : "INACTIVO");

    hideError();

    // Aplicar filtros DESPUÉS de cargar los datos
    applyInputFilters();
  }

  public boolean validateForm() {
    hideError();

    try {
      if (txtNombre.getText().trim().isEmpty()) {
        showErrorMessage("El nombre es obligatorio");
        txtNombre.requestFocus();
        return false;
      }

      if (txtDuracionDias.getText().trim().isEmpty()) {
        showErrorMessage("La duración en días es obligatoria");
        txtDuracionDias.requestFocus();
        return false;
      }

      try {
        int duracion = Integer.parseInt(txtDuracionDias.getText().trim());
        if (duracion <= 0) {
          showErrorMessage("La duración debe ser mayor a 0");
          txtDuracionDias.requestFocus();
          return false;
        }
      } catch (NumberFormatException e) {
        showErrorMessage("La duración debe ser un número válido");
        txtDuracionDias.requestFocus();
        return false;
      }

      if (txtCosto.getText().trim().isEmpty()) {
        showErrorMessage("El costo es obligatorio");
        txtCosto.requestFocus();
        return false;
      }

      try {
        BigDecimal costo = new BigDecimal(txtCosto.getText().trim());
        if (costo.compareTo(BigDecimal.ZERO) <= 0) {
          showErrorMessage("El costo debe ser mayor a 0");
          txtCosto.requestFocus();
          return false;
        }
      } catch (NumberFormatException e) {
        showErrorMessage("El costo no es válido");
        txtCosto.requestFocus();
        return false;
      }

      return true;
    } catch (Exception e) {
      showErrorMessage("Error al validar el formulario: " + e.getMessage());
      return false;
    }
  }

  public boolean updateTipoMembresia() {
    if (tipoDetalle == null) {
      showErrorMessage("No hay tipo de membresía cargado para actualizar");
      return false;
    }

    try {
      TipoMembresiaUpdateDTO updateDTO = new TipoMembresiaUpdateDTO(
          txtNombre.getText().trim(),
          txtDescripcion.getText().trim().isEmpty() ? null : txtDescripcion.getText().trim(),
          Integer.parseInt(txtDuracionDias.getText().trim()),
          new BigDecimal(txtCosto.getText().trim()),
          chkAccesoCompleto.isSelected(),
          cmbEstado.getSelectedItem().equals("ACTIVO")
      );

      controller.updateTipo(tipoDetalle.getId(), updateDTO);

      JOptionPane.showMessageDialog(
          this,
          "Tipo de membresía actualizado exitosamente",
          "Éxito",
          JOptionPane.INFORMATION_MESSAGE);

      return true;

    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al actualizar el tipo de membresía: " + e.getMessage());
      showErrorMessage("Error al actualizar: " + e.getMessage());
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
}