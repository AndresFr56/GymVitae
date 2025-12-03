package gym.vitae.views.inventario;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.InventarioController;
import gym.vitae.model.Categoria;
import gym.vitae.model.dtos.inventario.ProductoDetalleDTO;
import gym.vitae.model.dtos.inventario.ProductoUpdateDTO;
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
 * Formulario para actualizar un producto existente (RF-20). Campos editables: nombre, descripción,
 * categoría, proveedor, precio, stock, unidad de medida, activo. Campos no editables: código, fecha
 * de ingreso.
 */
public class UpdateProducto extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(UpdateProducto.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_DESC_LENGTH = 500;

  private final transient InventarioController controller;
  private final transient ProductoDetalleDTO productoDetalle;

  // Panel de contenido scrollable
  private JPanel contentPanel;
  private JLabel lblError;

  // Campos del formulario
  private JTextField txtCodigo;
  private JTextField txtNombre;
  private JTextArea txtDescripcion;
  private JComboBox<CategoriaItem> cmbCategoria;
  private JComboBox<ProveedorItem> cmbProveedor;
  private JTextField txtPrecio;
  private JSpinner spnStock;
  private JComboBox<UnidadMedida> cmbUnidadMedida;
  private JCheckBox chkActivo;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public UpdateProducto(InventarioController controller, ProductoDetalleDTO productoDetalle) {
    this.controller = controller;
    this.productoDetalle = productoDetalle;
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
    loadProductoData();
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
    // Código no editable (RF-20)
    txtCodigo = new JTextField();
    txtCodigo.setEditable(false);

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

    // CheckBox para estado activo
    chkActivo = new JCheckBox("Producto Activo");
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
    txtNombre.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Proteína Whey, Barra energética");
    txtDescripcion.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Descripción del producto (opcional)");
    txtPrecio.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: 199.99");
  }

  /** Aplica los filtros de entrada después de cargar los datos iniciales. */
  private void applyInputFilters() {
    applyLettersAndSpacesFilter(txtNombre);
    applyMaxLengthFilter(txtDescripcion);
    applyDecimalFilter(txtPrecio);
  }

  /** Aplica filtro para permitir letras, números y espacios. */
  private void applyLettersAndSpacesFilter(JTextField textField) {
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
                return text.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s0-9]*$");
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
    createTitle("Actualizar Producto");

    contentPanel.add(new JLabel("Código"));
    contentPanel.add(txtCodigo);

    contentPanel.add(new JLabel("Nombre del Producto *"));
    contentPanel.add(txtNombre);

    contentPanel.add(new JLabel("Descripción"));
    JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
    scrollDesc.setPreferredSize(new Dimension(0, 80));
    contentPanel.add(scrollDesc);

    contentPanel.add(new JLabel("Categoría *"));
    contentPanel.add(cmbCategoria);

    contentPanel.add(new JLabel("Proveedor"));
    contentPanel.add(cmbProveedor);

    contentPanel.add(new JLabel("Precio Unitario *"));
    contentPanel.add(txtPrecio);

    contentPanel.add(new JLabel("Stock *"));
    contentPanel.add(spnStock);

    contentPanel.add(new JLabel("Unidad de Medida *"));
    contentPanel.add(cmbUnidadMedida);

    contentPanel.add(new JLabel("Estado"));
    contentPanel.add(chkActivo);

    contentPanel.add(new JLabel(" "), "gapy 10");
    contentPanel.add(new JLabel("* Campos obligatorios"), "gapy 0");
  }

  private void createTitle(String title) {
    JLabel lblTitle = new JLabel(title);
    lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lblTitle, "gapy 10 5");
  }

  private void loadProductoData() {
    txtCodigo.setText(productoDetalle.getCodigo());
    txtNombre.setText(productoDetalle.getNombre());
    txtDescripcion.setText(
        productoDetalle.getDescripcion() != null ? productoDetalle.getDescripcion() : "");

    if (productoDetalle.getPrecioUnitario() != null) {
      txtPrecio.setText(productoDetalle.getPrecioUnitario().toString());
    }

    if (productoDetalle.getStock() != null) {
      spnStock.setValue(productoDetalle.getStock());
    }

    // Seleccionar categoría
    if (productoDetalle.getCategoriaId() != null) {
      for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
        CategoriaItem item = cmbCategoria.getItemAt(i);
        if (item.id() != null && item.id().equals(productoDetalle.getCategoriaId())) {
          cmbCategoria.setSelectedIndex(i);
          break;
        }
      }
    }

    // Seleccionar proveedor
    if (productoDetalle.getProveedorId() != null) {
      for (int i = 0; i < cmbProveedor.getItemCount(); i++) {
        ProveedorItem item = cmbProveedor.getItemAt(i);
        if (item.id() != null && item.id().equals(productoDetalle.getProveedorId())) {
          cmbProveedor.setSelectedIndex(i);
          break;
        }
      }
    }

    // Seleccionar unidad de medida
    if (productoDetalle.getUnidadMedida() != null) {
      for (UnidadMedida um : UnidadMedida.values()) {
        if (um.getDisplayName().equalsIgnoreCase(productoDetalle.getUnidadMedida())) {
          cmbUnidadMedida.setSelectedItem(um);
          break;
        }
      }
    }

    chkActivo.setSelected(productoDetalle.getActivo() != null && productoDetalle.getActivo());

    // Aplicar filtros DESPUÉS de cargar los datos
    applyInputFilters();
  }

  public boolean validateForm() {
    hideError();

    try {
      // Validar nombre
      String nombre = txtNombre.getText().trim();
      if (nombre.isEmpty()) {
        showErrorMessage("El nombre del producto es obligatorio");
        txtNombre.requestFocus();
        return false;
      }

      // Validar categoría
      if (cmbCategoria.getSelectedItem() == null) {
        showErrorMessage("Debe seleccionar una categoría");
        cmbCategoria.requestFocus();
        return false;
      }

      // Validar precio
      String precioStr = txtPrecio.getText().trim();
      if (precioStr.isEmpty()) {
        showErrorMessage("El precio unitario es obligatorio");
        txtPrecio.requestFocus();
        return false;
      }
      try {
        BigDecimal precio = new BigDecimal(precioStr);
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
          showErrorMessage("El precio debe ser mayor a cero");
          txtPrecio.requestFocus();
          return false;
        }
      } catch (NumberFormatException e) {
        showErrorMessage("El precio no tiene un formato válido");
        txtPrecio.requestFocus();
        return false;
      }

      // Validar stock
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

  public boolean updateProducto() {
    try {
      ProductoUpdateDTO dto = buildDTOFromForm();
      controller.updateProducto(productoDetalle.getId(), dto);

      JOptionPane.showMessageDialog(
          this, "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

      return true;

    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al actualizar el producto: " + e.getMessage());
      showErrorMessage("Error al actualizar el producto: " + e.getMessage());
      return false;
    }
  }

  private ProductoUpdateDTO buildDTOFromForm() {
    String descripcion = txtDescripcion.getText().trim();
    CategoriaItem categoriaItem = (CategoriaItem) cmbCategoria.getSelectedItem();
    ProveedorItem proveedorItem = (ProveedorItem) cmbProveedor.getSelectedItem();
    UnidadMedida unidadMedida = (UnidadMedida) cmbUnidadMedida.getSelectedItem();

    ProductoUpdateDTO dto = new ProductoUpdateDTO();
    dto.setNombre(txtNombre.getText().trim());
    dto.setDescripcion(descripcion.isEmpty() ? null : descripcion);
    dto.setCategoriaId(categoriaItem != null ? categoriaItem.id() : null);
    dto.setProveedorId(proveedorItem != null ? proveedorItem.id() : null);
    dto.setPrecioUnitario(new BigDecimal(txtPrecio.getText().trim()));
    dto.setStock((Integer) spnStock.getValue());
    dto.setUnidadMedida(unidadMedida != null ? unidadMedida.getDisplayName() : null);
    dto.setActivo(chkActivo.isSelected());

    return dto;
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
