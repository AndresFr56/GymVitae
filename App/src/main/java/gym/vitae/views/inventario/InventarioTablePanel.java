package gym.vitae.views.inventario;

import gym.vitae.controller.InventarioController;
import gym.vitae.model.dtos.inventario.EquipoDetalleDTO;
import gym.vitae.model.dtos.inventario.InventarioListadoDTO;
import gym.vitae.model.dtos.inventario.ProductoDetalleDTO;
import gym.vitae.model.dtos.inventario.ProveedorListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;
import gym.vitae.views.components.tables.TableAction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

/**
 * Panel de tabla combinada para el inventario (productos y equipos). Permite visualizar todos los
 * items con filtros por tipo, proveedor y nombre.
 */
public class InventarioTablePanel extends BaseTablePanel<InventarioListadoDTO> {

  private static final Logger LOGGER = Logger.getLogger(InventarioTablePanel.class.getName());

  private final transient InventarioController controller;
  private JComboBox<TipoItem> cmbTipo;
  private JComboBox<ProveedorItem> cmbProveedor;

  /**
   * Constructor del panel de inventario combinado.
   *
   * @param controller Controlador de inventario
   */
  public InventarioTablePanel(InventarioController controller) {
    super("Gestión de Inventario");
    this.controller = controller;
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {
      "SELECT", "#", "Código", "Tipo", "Nombre", "Descripción", "Fecha Adquisición"
    };
  }

  @Override
  protected Object[] entityToRow(InventarioListadoDTO entity, int rowNumber) {
    return new Object[] {
      false, // SELECT
      rowNumber,
      entity.getCodigo(),
      entity.getTipo(),
      entity.getNombre(),
      entity.getDescripcion() != null ? entity.getDescripcion() : "-",
      entity.getFechaAdquisicion() != null ? entity.getFechaAdquisicion() : "-"
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected List<InventarioListadoDTO> fetchPagedData(int offset, int limit) {
    return controller.getInventarioGeneral(offset, limit);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected List<InventarioListadoDTO> fetchPagedDataWithFilters(
      String searchText, int offset, int limit) {
    String tipoSeleccionado = getSelectedTipo();
    Integer proveedorId = getSelectedProveedorId();
    return controller.getInventarioWithFilters(
        searchText, tipoSeleccionado, proveedorId, offset, limit);
  }

  @Override
  protected long fetchTotalCount() {
    return controller.countInventario();
  }

  @Override
  protected long fetchTotalCountWithFilters(String searchText) {
    String tipoSeleccionado = getSelectedTipo();
    Integer proveedorId = getSelectedProveedorId();
    return controller.countInventarioWithFilters(searchText, tipoSeleccionado, proveedorId);
  }

  @Override
  protected JPanel createCustomFilters() {
    JPanel filtersPanel = new JPanel(new MigLayout("insets 0", "[][grow,fill][][grow,fill]", "[]"));

    // Filtro por tipo de item
    cmbTipo = new JComboBox<>();
    cmbTipo.addItem(new TipoItem("Todos", "Todos"));
    cmbTipo.addItem(new TipoItem("Producto", "Producto"));
    cmbTipo.addItem(new TipoItem("Equipo", "Equipo"));
    cmbTipo.addActionListener(e -> loadData());

    // Filtro por proveedor
    cmbProveedor = new JComboBox<>();
    loadProveedores();
    cmbProveedor.addActionListener(e -> loadData());

    filtersPanel.add(new JLabel("Tipo:"));
    filtersPanel.add(cmbTipo, "width 150!");
    filtersPanel.add(new JLabel("Proveedor:"));
    filtersPanel.add(cmbProveedor, "width 200!");

    return filtersPanel;
  }

  private void loadProveedores() {
    cmbProveedor.removeAllItems();
    cmbProveedor.addItem(new ProveedorItem(null, "Todos"));
    try {
      List<ProveedorListadoDTO> proveedores = controller.getProveedoresActivos();
      for (ProveedorListadoDTO prov : proveedores) {
        cmbProveedor.addItem(new ProveedorItem(prov.getId(), prov.getNombre()));
      }
    } catch (Exception e) {
      LOGGER.warning("No pudo cargar los proveedores: " + e.getMessage());
    }
  }

  @Override
  protected void configureColumnWidths(JTable table) {
    table.getColumnModel().getColumn(0).setMaxWidth(50); // SELECT
    table.getColumnModel().getColumn(1).setMaxWidth(50); // #
    table.getColumnModel().getColumn(2).setPreferredWidth(100); // Código
    table.getColumnModel().getColumn(3).setPreferredWidth(80); // Tipo
    table.getColumnModel().getColumn(4).setPreferredWidth(180); // Nombre
    table.getColumnModel().getColumn(5).setPreferredWidth(200); // Descripción
    table.getColumnModel().getColumn(6).setPreferredWidth(120); // Fecha
  }

  @Override
  protected boolean filterEntity(InventarioListadoDTO entity, String searchText) {
    String lower = searchText.toLowerCase();
    return entity.getNombre().toLowerCase().contains(lower)
        || entity.getCodigo().toLowerCase().contains(lower)
        || (entity.getDescripcion() != null
            && entity.getDescripcion().toLowerCase().contains(lower));
  }

  @Override
  protected List<TableAction> getTableActions() {
    List<TableAction> actions = new ArrayList<>();
    actions.add(new TableAction("Agregar", "#2196F3", this::onAdd));
    actions.add(new TableAction("Actualizar", "#4CAF50", this::onUpdate));
    actions.add(new TableAction("Dar de Baja", "#F44336", this::onDelete));
    actions.add(new TableAction("+ Categoría", "#9C27B0", this::openRegisterCategoria));
    return actions;
  }

  @Override
  protected void onAdd() {
    // Mostrar dialog para seleccionar tipo
    String[] opciones = {"Producto", "Equipo"};
    int seleccion =
        JOptionPane.showOptionDialog(
            this,
            "¿Qué tipo de item desea registrar?",
            "Nuevo Item para el Inventario",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);

    if (seleccion == 0) {
      openRegisterProducto();
    } else if (seleccion == 1) {
      openRegisterEquipo();
    }
  }

  // Abrir formulario de registro de producto
  private void openRegisterProducto() {
    RegisterProducto registerForm = new RegisterProducto(controller);

    Option option = ModalDialog.createOption();
    option
        .getLayoutOption()
        .setSize(0.4f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            registerForm,
            "Registrar Nuevo Producto",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    registerForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (registerForm.validateForm() && registerForm.saveProducto()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    registerForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  // Abrir formulario de registro de equipo
  private void openRegisterEquipo() {
    RegisterEquipo registerForm = new RegisterEquipo(controller);

    Option option = ModalDialog.createOption();
    option
        .getLayoutOption()
        .setSize(0.4f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            registerForm,
            "Registrar Nuevo Equipo",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    registerForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (registerForm.validateForm() && registerForm.saveEquipo()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    registerForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  // Abrir formulario de registro de categoría
  private void openRegisterCategoria() {
    RegisterCategoria registerForm = new RegisterCategoria(controller);

    Option option = ModalDialog.createOption();
    option
        .getLayoutOption()
        .setSize(0.35f, 0.7f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            registerForm,
            "Registrar Nueva Categoría",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    registerForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (registerForm.validateForm() && registerForm.saveCategoria()) {
                ModalDialog.closeAllModal();
                // Refrescar combos de categoría en los formularios si están abiertos
              }
            });

    registerForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onUpdateEntity(InventarioListadoDTO entity) {
    if ("Producto".equals(entity.getTipo())) {
      openUpdateProducto(entity);
    } else if ("Equipo".equals(entity.getTipo())) {
      openUpdateEquipo(entity);
    }
  }

  // Abrir actualización de producto
  private void openUpdateProducto(InventarioListadoDTO item) {
    ProductoDetalleDTO detalle = controller.getProductoById(item.getId());
    UpdateProducto updateForm = new UpdateProducto(controller, detalle);

    Option option = ModalDialog.createOption();
    option
        .getLayoutOption()
        .setSize(0.4f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            updateForm,
            "Actualizar Producto",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    updateForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (updateForm.validateForm() && updateForm.updateProducto()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    updateForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  // Abrir actualización de equipo
  private void openUpdateEquipo(InventarioListadoDTO item) {
    EquipoDetalleDTO detalle = controller.getEquipoById(item.getId());
    UpdateEquipo updateForm = new UpdateEquipo(controller, detalle);

    Option option = ModalDialog.createOption();
    option
        .getLayoutOption()
        .setSize(0.4f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            updateForm,
            "Actualizar Equipo",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    updateForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (updateForm.validateForm() && updateForm.updateEquipo()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    updateForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onDeleteEntities(List<InventarioListadoDTO> entities) {
    int confirmResult =
        JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro en dar de baja los " + entities.size() + " registros seleccionados?",
            "Confirmar baja",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

    if (confirmResult != JOptionPane.YES_OPTION) {
      return;
    }

    for (InventarioListadoDTO entity : entities) {
      try {
        if ("Producto".equals(entity.getTipo())) {
          controller.darDeBajaProducto(entity.getId());
        } else if ("Equipo".equals(entity.getTipo())) {
          controller.darDeBajaEquipo(entity.getId());
        }
      } catch (Exception e) {
        LOGGER.severe("Error al dar de baja: " + e.getMessage());
      }
    }
    loadData();
  }

  /** Devuelve el tipo seleccionado, o null si se seleccionó "Todos". */
  private String getSelectedTipo() {
    if (cmbTipo == null) return null;
    TipoItem selected = (TipoItem) cmbTipo.getSelectedItem();
    if (selected != null && !"Todos".equals(selected.value())) {
      return selected.value();
    }
    return null;
  }

  /** Devuelve el ID del proveedor seleccionado o null si es "Todos". */
  private Integer getSelectedProveedorId() {
    if (cmbProveedor == null) return null;
    ProveedorItem selected = (ProveedorItem) cmbProveedor.getSelectedItem();
    return (selected != null) ? selected.id() : null;
  }

  /** Wrapper para items del combo de tipo. */
  private record TipoItem(String value, String label) {

    @Override
    public String toString() {
      return label;
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
