package gym.vitae.views.inventario;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.InventarioController;
import gym.vitae.model.Categoria;
import gym.vitae.model.dtos.inventario.ProductoCreateDTO;
import gym.vitae.model.dtos.inventario.ProveedorListadoDTO;
import gym.vitae.model.enums.TipoCategoria;
import gym.vitae.model.enums.UnidadMedida;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;

/**
 * Formulario para registrar un nuevo producto en el sistema (RF-19). Campos obligatorios: Código, nombre,
 * categoría, precio unitario, stock, unidad de medida. Campos opcionales: Descripción, proveedor.
 * Código y fecha de ingreso se generan automáticamente.
 */
public class RegisterProducto extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(RegisterProducto.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_DESC_LENGTH = 500;

  private final transient InventarioController controller;

  // JPanel es el Panel de contenido scrollable
  private JPanel contentPanel;
  private JLabel lblError;

  // Campos del formulario de Registrar Producto
  private JTextField txtCodigo;
  private JTextField txtNombre;
  private JTextArea txtDescripcion;
  private JComboBox<CategoriaItem> cmbCategoria;
  private JComboBox<ProveedorItem> cmbProveedor;
  private JTextField txtPrecio;
  private JSpinner spnStock;
  private JComboBox<UnidadMedida> cmbUnidadMedida;

  // Botones de accion
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public RegisterProducto(InventarioController controller) {
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
    txtCodigo.setText(controller.generateCodigoProducto());

    txtNombre = new JTextField();
    txtDescripcion = new JTextArea(3, 20);
    txtDescripcion.setLineWrap(true);
    txtDescripcion.setWrapStyleWord(true);

    // ComboBox de categorías (solo tipo PRODUCTO)
    cmbCategoria = new JComboBox<>();
    loadCategorias();

    // ComboBox de proveedores (opcional)
    cmbProveedor = new JComboBox<>();
    loadProveedores();

    // Campo de precio
    txtPrecio = new JTextField();

    // Spinner para stock (0-99999)
    SpinnerNumberModel stockModel = new SpinnerNumberModel(0, 0, 99999, 1);
    spnStock = new JSpinner(stockModel);

    // ComboBox de unidad de medida
    cmbUnidadMedida = new JComboBox<>(UnidadMedida.values());
    cmbUnidadMedida.setSelectedItem(UnidadMedida.UNIDAD);

    // Aplicar filtros
    applyLettersAndSpacesFilter(txtNombre);
    applyMaxLengthFilter(txtDescripcion);
    applyDecimalFilter(txtPrecio);
  }

  private void loadCategorias() {
    cmbCategoria.removeAllItems();
    try {
      List<Categoria> categorias = controller.getCategoriasByTipo(TipoCategoria.PRODUCTO);
      for (Categoria cat : categorias) {
        cmbCategoria.addItem(new CategoriaItem(cat.getId(), cat.getNombre()));
      }
    } catch (Exception e) {
      LOGGER.warning("No se pudieron cargar las categorías: " + e.getMessage());
    }
  }

  private void loadProveedores() {
    cmbProveedor.removeAllItems();
    cmbProveedor.addItem(new ProveedorItem(null, "-- Sin proveedor --"));
    try {
      List<ProveedorListadoDTO> proveedores = controller.getProveedoresActivos();
      for (ProveedorListadoDTO prov : proveedores) {
        cmbProveedor.addItem(new ProveedorItem(prov.getId(), prov.getNombre()));
      }
    } catch (Exception e) {
      LOGGER.warning("No se pudieron cargar los proveedores: " + e.getMessage());
    }
  }

  private void applyStyles() {
    txtCodigo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Generado de forma automática");
    txtNombre.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Proteína, Barra energética");
    txtDescripcion.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Descripción del producto (opcional)");
    txtPrecio.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: 999.99");
  }

  /** Filtro para permitir solo letras y espacios. */
  private void applyLettersAndSpacesFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (isValidLettersAndSpaces(string)
                    && (fb.getDocument().getLength() + string.length() <= MAX_TEXT_LENGTH)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (isValidLettersAndSpaces(text)
                    && (fb.getDocument().getLength() - length + text.length() <= MAX_TEXT_LENGTH)) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }

              private boolean isValidLettersAndSpaces(String text) {
                return text.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s0-9]*$");
              }
            });
  }

  /** Filtro de longitud máxima. */
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

  /** Filtro para permitir solo números decimales. */
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
    createTitle("Información del Producto");

    contentPanel.add(new JLabel("Código*"));
    contentPanel.add(txtCodigo);

    contentPanel.add(new JLabel("Nombre del Producto*"));
    contentPanel.add(txtNombre);

    contentPanel.add(new JLabel("Descripción del Producto"));
    JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
    scrollDesc.setPreferredSize(new Dimension(0, 80));
    contentPanel.add(scrollDesc);

    contentPanel.add(new JLabel("Categoría del Producto*"));
    contentPanel.add(cmbCategoria);

    contentPanel.add(new JLabel("Proveedor del Producto"));
    contentPanel.add(cmbProveedor);

    contentPanel.add(new JLabel("Precio Unitario del Producto*"));
    contentPanel.add(txtPrecio);

    contentPanel.add(new JLabel("Stock Inicial del Producto*"));
    contentPanel.add(spnStock);

    contentPanel.add(new JLabel("Unidad de Medida del Producto*"));
    contentPanel.add(cmbUnidadMedida);

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
      // Validar el nombre
      String nombre = txtNombre.getText().trim();
      if (nombre.isEmpty()) {
        showErrorMessage("El nombre del producto es obligatorio");
        txtNombre.requestFocus();
        return false;
      }

      // Validar la categoría
      if (cmbCategoria.getSelectedItem() == null) {
        showErrorMessage("Debe seleccionar una categoría");
        cmbCategoria.requestFocus();
        return false;
      }

      // Validar precio
      String precioStr = txtPrecio.getText().trim();
      if (precioStr.isEmpty()) {
        showErrorMessage("El precio unitario del producto es obligatorio");
        txtPrecio.requestFocus();
        return false;
      }
      try {
        BigDecimal precio = new BigDecimal(precioStr);
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
          showErrorMessage("El precio del producto debe ser mayor a cero");
          txtPrecio.requestFocus();
          return false;
        }
      } catch (NumberFormatException e) {
        showErrorMessage("El precio no tiene un formato válido");
        txtPrecio.requestFocus();
        return false;
      }

      // Validar el stock
      int stock = (Integer) spnStock.getValue();
      if (stock < 0) {
        showErrorMessage("El stock no puede ser negativo");
        spnStock.requestFocus();
        return false;
      }

      return true;
    } catch (Exception e) {
      showErrorMessage("Error al validar el formulario: " + e.getMessage());
      return false;
    }
  }

  public boolean saveProducto() {
    try {
      ProductoCreateDTO dto = buildDTOFromForm();
      controller.createProducto(dto);

      JOptionPane.showMessageDialog(
          this, "El producto ha sido registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

      clearForm();
      return true;

    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al guardar el producto: " + e.getMessage());
      showErrorMessage("Error al guardar el producto: " + e.getMessage());
      return false;
    }
  }

  private ProductoCreateDTO buildDTOFromForm() {
    String descripcion = txtDescripcion.getText().trim();
    CategoriaItem categoriaItem = (CategoriaItem) cmbCategoria.getSelectedItem();
    ProveedorItem proveedorItem = (ProveedorItem) cmbProveedor.getSelectedItem();
    UnidadMedida unidadMedida = (UnidadMedida) cmbUnidadMedida.getSelectedItem();

    ProductoCreateDTO dto = new ProductoCreateDTO();
    dto.setNombre(txtNombre.getText().trim());
    dto.setDescripcion(descripcion.isEmpty() ? null : descripcion);
    dto.setCategoriaId(categoriaItem != null ? categoriaItem.id() : null);
    dto.setProveedorId(proveedorItem != null ? proveedorItem.id() : null);
    dto.setPrecioUnitario(new BigDecimal(txtPrecio.getText().trim()));
    dto.setStock((Integer) spnStock.getValue());
    dto.setUnidadMedida(unidadMedida != null ? unidadMedida.getDisplayName() : "Unidad");

    return dto;
  }

  private void clearForm() {
    txtCodigo.setText(controller.generateCodigoProducto());
    txtNombre.setText("");
    txtDescripcion.setText("");
    txtPrecio.setText("");
    spnStock.setValue(0);
    if (cmbCategoria.getItemCount() > 0) {
      cmbCategoria.setSelectedIndex(0);
    }
    if (cmbProveedor.getItemCount() > 0) {
      cmbProveedor.setSelectedIndex(0);
    }
    cmbUnidadMedida.setSelectedItem(UnidadMedida.UNIDAD);
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

  /** Wrapper para items del combo de proveedores. */
  private record ProveedorItem(Integer id, String nombre) {

        @Override
        public String toString() {
      return nombre;
        }
  }
}
