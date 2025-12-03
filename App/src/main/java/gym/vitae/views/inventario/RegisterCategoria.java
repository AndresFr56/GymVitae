package gym.vitae.views.inventario;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.InventarioController;
import gym.vitae.model.dtos.inventario.CategoriaCreateDTO;
import gym.vitae.model.enums.TipoCategoria;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;

/**
 * Formulario para registrar una nueva categoría. Campos obligatorios: Nombre, Tipo, Subtipo. Campos
 * opcionales: Descripción.
 */
public class RegisterCategoria extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(RegisterCategoria.class.getName());
  private static final int MAX_NAME_LENGTH = 100;
  private static final int MAX_DESC_LENGTH = 500;

  // Subtipos válidos según el tipo (restricción de la base de datos)
  private static final String[] SUBTIPOS_PRODUCTO = {
    "PRODUCTO", "ACCESORIO", "MATERIAL_DE_LIMPIEZA"
  };
  private static final String[] SUBTIPOS_EQUIPO = {"CARDIO", "PESAS", "FUNCIONAL"};

  private final transient InventarioController controller;

  // Panel de contenido scrollable
  private JPanel contentPanel;
  private JLabel lblError;

  // Campos del formulario
  private JTextField txtNombre;
  private JComboBox<TipoCategoria> cmbTipo;
  private JComboBox<String> cmbSubtipo;
  private JTextArea txtDescripcion;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public RegisterCategoria(InventarioController controller) {
    this.controller = controller;
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

    // ComboBox de tipo de categoría
    cmbTipo = new JComboBox<>(TipoCategoria.values());
    cmbTipo.setSelectedItem(TipoCategoria.PRODUCTO);

    // ComboBox de subtipo - se actualiza dinámicamente según el tipo
    cmbSubtipo = new JComboBox<>(SUBTIPOS_PRODUCTO);
    cmbSubtipo.setSelectedIndex(0);

    // Listener para actualizar subtipos cuando cambia el tipo
    cmbTipo.addActionListener(e -> updateSubtipoOptions());

    txtDescripcion = new JTextArea(4, 20);
    txtDescripcion.setLineWrap(true);
    txtDescripcion.setWrapStyleWord(true);

    // Aplicar filtros de longitud
    applyMaxLengthFilter(txtNombre, MAX_NAME_LENGTH);
    applyMaxLengthFilter(txtDescripcion, MAX_DESC_LENGTH);
  }

  /** Actualiza las opciones del ComboBox de subtipo según el tipo seleccionado. */
  private void updateSubtipoOptions() {
    TipoCategoria tipoSeleccionado = (TipoCategoria) cmbTipo.getSelectedItem();
    cmbSubtipo.removeAllItems();

    if (tipoSeleccionado == TipoCategoria.PRODUCTO) {
      for (String subtipo : SUBTIPOS_PRODUCTO) {
        cmbSubtipo.addItem(subtipo);
      }
    } else if (tipoSeleccionado == TipoCategoria.EQUIPO) {
      for (String subtipo : SUBTIPOS_EQUIPO) {
        cmbSubtipo.addItem(subtipo);
      }
    }

    if (cmbSubtipo.getItemCount() > 0) {
      cmbSubtipo.setSelectedIndex(0);
    }
  }

  private void applyStyles() {
    txtNombre.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Suplementos, Máquinas de Cardio");
    txtDescripcion.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Descripción de la categoría (opcional)");
  }

  /** Aplica filtro de longitud máxima a un campo de texto. */
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
                if (text == null) {
                  super.replace(fb, offset, length, text, attrs);
                  return;
                }
                int currentLength = fb.getDocument().getLength();
                int newLength = currentLength - length + text.length();
                if (newLength <= maxLength) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  private void buildForm() {
    createTitle("Nueva Categoría");

    contentPanel.add(new JLabel("Nombre *"));
    contentPanel.add(txtNombre);

    contentPanel.add(new JLabel("Tipo *"));
    contentPanel.add(cmbTipo);

    contentPanel.add(new JLabel("Subtipo *"));
    contentPanel.add(cmbSubtipo);

    contentPanel.add(new JLabel("Descripción"));
    JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
    scrollDesc.setPreferredSize(new Dimension(0, 100));
    contentPanel.add(scrollDesc);

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
      // Validar nombre
      String nombre = txtNombre.getText().trim();
      if (nombre.isEmpty()) {
        showErrorMessage("El nombre de la categoría es obligatorio");
        txtNombre.requestFocus();
        return false;
      }
      if (nombre.length() > MAX_NAME_LENGTH) {
        showErrorMessage("El nombre no puede exceder " + MAX_NAME_LENGTH + " caracteres");
        txtNombre.requestFocus();
        return false;
      }

      // Validar tipo
      if (cmbTipo.getSelectedItem() == null) {
        showErrorMessage("El tipo de categoría es obligatorio");
        cmbTipo.requestFocus();
        return false;
      }

      // Validar subtipo (obligatorio según restricción de BD)
      if (cmbSubtipo.getSelectedItem() == null) {
        showErrorMessage("El subtipo es obligatorio");
        cmbSubtipo.requestFocus();
        return false;
      }

      // Validar descripción (opcional)
      String descripcion = txtDescripcion.getText().trim();
      if (descripcion.length() > MAX_DESC_LENGTH) {
        showErrorMessage("La descripción no puede exceder " + MAX_DESC_LENGTH + " caracteres");
        txtDescripcion.requestFocus();
        return false;
      }

      return true;

    } catch (Exception e) {
      showErrorMessage("Error de validación: " + e.getMessage());
      return false;
    }
  }

  public boolean saveCategoria() {
    try {
      CategoriaCreateDTO dto = buildDTOFromForm();
      controller.createCategoria(dto);
      JOptionPane.showMessageDialog(
          this, "Categoría registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      clearForm();
      return true;
    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al guardar categoría: " + e.getMessage());
      showErrorMessage("Error al guardar la categoría: " + e.getMessage());
      return false;
    }
  }

  private CategoriaCreateDTO buildDTOFromForm() {
    String descripcion = txtDescripcion.getText().trim();
    TipoCategoria tipo = (TipoCategoria) cmbTipo.getSelectedItem();
    String subtipo = (String) cmbSubtipo.getSelectedItem();

    CategoriaCreateDTO dto = new CategoriaCreateDTO();
    dto.setNombre(txtNombre.getText().trim());
    dto.setTipo(tipo);
    dto.setSubtipo(subtipo);
    dto.setDescripcion(descripcion.isEmpty() ? null : descripcion);

    return dto;
  }

  private void clearForm() {
    txtNombre.setText("");
    cmbTipo.setSelectedItem(TipoCategoria.PRODUCTO);
    updateSubtipoOptions();
    txtDescripcion.setText("");
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
