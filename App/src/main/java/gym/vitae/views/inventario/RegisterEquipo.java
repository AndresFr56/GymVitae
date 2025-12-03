package gym.vitae.views.inventario;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.InventarioController;
import gym.vitae.model.Categoria;
import gym.vitae.model.dtos.inventario.EquipoCreateDTO;
import gym.vitae.model.enums.TipoCategoria;
import gym.vitae.model.enums.UbicacionEquipo;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;

/**
 * Formulario para registrar un nuevo equipo (RF-22). Campos obligatorios: Código, nombre,
 * categoría, marca, modelo, costo, ubicación. Campos opcionales: Descripción, observaciones.
 * Código, fecha de adquisición y estado se generan automáticamente.
 */
public class RegisterEquipo extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(RegisterEquipo.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_DESC_LENGTH = 500;

  private final transient InventarioController controller;

  // Panel de contenido scrollable
  private JPanel contentPanel;
  private JLabel lblError;

  // Campos del formulario
  private JTextField txtCodigo;
  private JTextField txtNombre;
  private JTextArea txtDescripcion;
  private JComboBox<CategoriaItem> cmbCategoria;
  private JTextField txtMarca;
  private JTextField txtModelo;
  private JTextField txtCosto;
  private JComboBox<UbicacionEquipo> cmbUbicacion;
  private JTextArea txtObservaciones;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public RegisterEquipo(InventarioController controller) {
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
    // Código generado automáticamente (no editable)
    txtCodigo = new JTextField();
    txtCodigo.setEditable(false);
    txtCodigo.setText(controller.generateCodigoEquipo());

    txtNombre = new JTextField();
    txtDescripcion = new JTextArea(3, 20);
    txtDescripcion.setLineWrap(true);
    txtDescripcion.setWrapStyleWord(true);

    // ComboBox de categorías (solo tipo EQUIPO)
    cmbCategoria = new JComboBox<>();
    loadCategorias();

    txtMarca = new JTextField();
    txtModelo = new JTextField();
    txtCosto = new JTextField();

    // ComboBox de ubicación
    cmbUbicacion = new JComboBox<>(UbicacionEquipo.values());
    cmbUbicacion.setSelectedItem(UbicacionEquipo.AREA_CARDIO);

    txtObservaciones = new JTextArea(3, 20);
    txtObservaciones.setLineWrap(true);
    txtObservaciones.setWrapStyleWord(true);

    // Aplicar filtros
    applyTextFilter(txtNombre);
    applyTextFilter(txtMarca);
    applyTextFilter(txtModelo);
    applyMaxLengthFilter(txtDescripcion);
    applyMaxLengthFilter(txtObservaciones);
    applyDecimalFilter(txtCosto);
  }

  private void loadCategorias() {
    cmbCategoria.removeAllItems();
    try {
      List<Categoria> categorias = controller.getCategoriasByTipo(TipoCategoria.EQUIPO);
      for (Categoria cat : categorias) {
        cmbCategoria.addItem(new CategoriaItem(cat.getId(), cat.getNombre()));
      }
    } catch (Exception e) {
      LOGGER.warning("No se pudieron cargar las categorías: " + e.getMessage());
    }
  }

  private void applyStyles() {
    txtCodigo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Generado automáticamente");
    txtNombre.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Caminadora, Bicicleta estática");
    txtDescripcion.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Descripción del equipo (opcional)");
    txtMarca.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Life Fitness, Precor");
    txtModelo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: T5, 885");
    txtCosto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: 25000.00");
    txtObservaciones.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Observaciones adicionales (opcional)");
  }

  /** Aplica filtro para permitir letras, números y espacios. */
  private void applyTextFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (isValidText(string)
                    && (fb.getDocument().getLength() + string.length() <= MAX_TEXT_LENGTH)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (isValidText(text)
                    && (fb.getDocument().getLength() - length + text.length() <= MAX_TEXT_LENGTH)) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }

              private boolean isValidText(String text) {
                return text.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s0-9\\-]*$");
              }
            });
  }

  /** Aplica filtro de longitud máxima. */
  private void applyMaxLengthFilter(JTextComponent textComponent) {
    ((AbstractDocument) textComponent.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (fb.getDocument().getLength() + string.length() <= MAX_DESC_LENGTH) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (fb.getDocument().getLength() - length + text.length() <= MAX_DESC_LENGTH) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  /** Aplica filtro para permitir solo números decimales. */
  private void applyDecimalFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength());
                newText = newText.substring(0, offset) + string + newText.substring(offset);
                if (isValidDecimal(newText)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) text = "";
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText =
                    currentText.substring(0, offset)
                        + text
                        + currentText.substring(offset + length);
                if (isValidDecimal(newText)) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }

              private boolean isValidDecimal(String text) {
                if (text.isEmpty()) return true;
                return text.matches("^\\d*\\.?\\d{0,2}$");
              }
            });
  }

  private void buildForm() {
    createTitle("Información del Equipo");

    contentPanel.add(new JLabel("Código *"));
    contentPanel.add(txtCodigo);

    contentPanel.add(new JLabel("Nombre del Equipo *"));
    contentPanel.add(txtNombre);

    contentPanel.add(new JLabel("Descripción"));
    JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
    scrollDesc.setPreferredSize(new Dimension(0, 80));
    contentPanel.add(scrollDesc);

    contentPanel.add(new JLabel("Categoría *"));
    contentPanel.add(cmbCategoria);

    contentPanel.add(new JLabel("Marca *"));
    contentPanel.add(txtMarca);

    contentPanel.add(new JLabel("Modelo *"));
    contentPanel.add(txtModelo);

    contentPanel.add(new JLabel("Costo *"));
    contentPanel.add(txtCosto);

    contentPanel.add(new JLabel("Ubicación *"));
    contentPanel.add(cmbUbicacion);

    contentPanel.add(new JLabel("Observaciones"));
    JScrollPane scrollObs = new JScrollPane(txtObservaciones);
    scrollObs.setPreferredSize(new Dimension(0, 80));
    contentPanel.add(scrollObs);

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
        showErrorMessage("El nombre del equipo es obligatorio");
        txtNombre.requestFocus();
        return false;
      }

      // Validar categoría
      if (cmbCategoria.getSelectedItem() == null) {
        showErrorMessage("Debe seleccionar una categoría");
        cmbCategoria.requestFocus();
        return false;
      }

      // Validar marca
      String marca = txtMarca.getText().trim();
      if (marca.isEmpty()) {
        showErrorMessage("La marca es obligatoria");
        txtMarca.requestFocus();
        return false;
      }

      // Validar modelo
      String modelo = txtModelo.getText().trim();
      if (modelo.isEmpty()) {
        showErrorMessage("El modelo es obligatorio");
        txtModelo.requestFocus();
        return false;
      }

      // Validar costo
      String costoStr = txtCosto.getText().trim();
      if (costoStr.isEmpty()) {
        showErrorMessage("El costo es obligatorio");
        txtCosto.requestFocus();
        return false;
      }
      try {
        BigDecimal costo = new BigDecimal(costoStr);
        if (costo.compareTo(BigDecimal.ZERO) <= 0) {
          showErrorMessage("El costo debe ser mayor a cero");
          txtCosto.requestFocus();
          return false;
        }
      } catch (NumberFormatException e) {
        showErrorMessage("El costo no tiene un formato válido");
        txtCosto.requestFocus();
        return false;
      }

      // Validar ubicación
      if (cmbUbicacion.getSelectedItem() == null) {
        showErrorMessage("Debe seleccionar una ubicación");
        cmbUbicacion.requestFocus();
        return false;
      }

      return true;
    } catch (Exception e) {
      showErrorMessage("Error al validar el formulario: " + e.getMessage());
      return false;
    }
  }

  public boolean saveEquipo() {
    try {
      LOGGER.info("Iniciando guardado de equipo...");
      EquipoCreateDTO dto = buildDTOFromForm();
      LOGGER.info(
          "DTO construido: nombre="
              + dto.getNombre()
              + ", categoriaId="
              + dto.getCategoriaId()
              + ", marca="
              + dto.getMarca()
              + ", modelo="
              + dto.getModelo()
              + ", costo="
              + dto.getCosto()
              + ", ubicacion="
              + dto.getUbicacion());

      controller.createEquipo(dto);
      LOGGER.info("Equipo creado exitosamente");

      JOptionPane.showMessageDialog(
          this, "Equipo registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

      clearForm();
      return true;

    } catch (IllegalArgumentException e) {
      LOGGER.warning("Error de validación al guardar equipo: " + e.getMessage());
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al guardar el equipo: " + e.getMessage());
      e.printStackTrace();
      showErrorMessage("Error al guardar el equipo: " + e.getMessage());
      return false;
    }
  }

  private EquipoCreateDTO buildDTOFromForm() {
    String descripcion = txtDescripcion.getText().trim();
    String observaciones = txtObservaciones.getText().trim();
    CategoriaItem categoriaItem = (CategoriaItem) cmbCategoria.getSelectedItem();
    UbicacionEquipo ubicacion = (UbicacionEquipo) cmbUbicacion.getSelectedItem();

    EquipoCreateDTO dto = new EquipoCreateDTO();
    dto.setNombre(txtNombre.getText().trim());
    dto.setDescripcion(descripcion.isEmpty() ? null : descripcion);
    dto.setCategoriaId(categoriaItem != null ? categoriaItem.id() : null);
    dto.setMarca(txtMarca.getText().trim());
    dto.setModelo(txtModelo.getText().trim());
    dto.setCosto(new BigDecimal(txtCosto.getText().trim()));
    dto.setUbicacion(ubicacion != null ? ubicacion.getDisplayName() : null);
    dto.setObservaciones(observaciones.isEmpty() ? null : observaciones);

    return dto;
  }

  private void clearForm() {
    txtCodigo.setText(controller.generateCodigoEquipo());
    txtNombre.setText("");
    txtDescripcion.setText("");
    txtMarca.setText("");
    txtModelo.setText("");
    txtCosto.setText("");
    txtObservaciones.setText("");
    if (cmbCategoria.getItemCount() > 0) {
      cmbCategoria.setSelectedIndex(0);
    }
    cmbUbicacion.setSelectedItem(UbicacionEquipo.AREA_CARDIO);
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

  /** Wrapper para items del combo de categorías. */
  private record CategoriaItem(Integer id, String nombre) {

    @Override
    public String toString() {
      return nombre;
    }
  }
}
