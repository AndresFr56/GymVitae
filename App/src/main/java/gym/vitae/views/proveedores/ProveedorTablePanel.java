package gym.vitae.views.proveedores;

import gym.vitae.controller.ProveedoresController;
import gym.vitae.model.dtos.inventario.ProveedorListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

/** Panel de tabla para la gestión de proveedores */
public class ProveedorTablePanel extends BaseTablePanel<ProveedorListadoDTO> {

  private final transient ProveedoresController controller;
  private JComboBox<String> cmbEstadoProveedor;

  public ProveedorTablePanel(ProveedoresController proveedoresController) {
    super("Gestión de Proveedores");
    this.controller = proveedoresController;
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {
      "SELECT", "#", "Código", "Nombre", "Contacto", "Teléfono", "Email", "Estado"
    };
  }

  @Override
  protected Object[] entityToRow(ProveedorListadoDTO proveedor, int rowNumber) {
    return new Object[] {
      false,
      rowNumber,
      proveedor.getCodigo(),
      proveedor.getNombre(),
      proveedor.getContacto(),
      proveedor.getTelefono(),
      proveedor.getEmail(),
      proveedor.getActivo() ? "ACTIVO" : "INACTIVO"
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true;
  }

  @Override
  protected List<ProveedorListadoDTO> fetchPagedDataWithFilters(
      String searchText, int offset, int limit) {
    return controller.getProveedores().stream()
        .filter(p -> filterEntity(p, searchText))
        .skip(offset)
        .limit(limit)
        .toList();
  }

  @Override
  protected long fetchTotalCount() {
    return controller.getProveedores().size();
  }

  @Override
  protected long fetchTotalCountWithFilters(String searchText) {
    return controller.getProveedores().stream().filter(p -> filterEntity(p, searchText)).count();
  }

  protected JPanel createCustomFilters() {
    JPanel filtersPanel = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[]"));
    cmbEstadoProveedor = new JComboBox<>(new String[] {"Todos", "ACTIVO", "INACTIVO"});
    cmbEstadoProveedor.addActionListener(e -> loadData());

    filtersPanel.add(new JLabel("Estado:"));
    filtersPanel.add(cmbEstadoProveedor, "width 200!");

    return filtersPanel;
  }

  @Override
  protected void configureColumnWidths(JTable table) {
    table.getColumnModel().getColumn(0).setMaxWidth(50); // Select
    table.getColumnModel().getColumn(1).setMaxWidth(50); // #
    table.getColumnModel().getColumn(2).setPreferredWidth(100); // Codigo
    table.getColumnModel().getColumn(3).setPreferredWidth(100); // Nombre
    table.getColumnModel().getColumn(4).setPreferredWidth(100); // Contacto
    table.getColumnModel().getColumn(5).setPreferredWidth(100); // Telefono
    table.getColumnModel().getColumn(6).setPreferredWidth(100); // Email
    table.getColumnModel().getColumn(7).setPreferredWidth(100); // Estado
  }

  @Override
  protected boolean filterEntity(ProveedorListadoDTO proveedor, String searchText) {
    return matchesSearchText(proveedor, searchText) && matchesEstado(proveedor);
  }

  private boolean matchesSearchText(ProveedorListadoDTO proveedor, String searchText) {
    String lowerTxt = searchText.toLowerCase();
    return proveedor.getNombre().toLowerCase().contains(lowerTxt)
        || proveedor.getCodigo().toLowerCase().contains(lowerTxt)
        || proveedor.getEmail().toLowerCase().contains(lowerTxt);
  }

  private boolean matchesEstado(ProveedorListadoDTO proveedor) {
    String selectedEstado = getSelectedEstado();
    if (selectedEstado == null) {
      return true;
    }
    boolean activo = proveedor.getActivo();

    if (selectedEstado.equals("ACTIVO")) {
      return activo;
    } else if (selectedEstado.equals("INACTIVO")) {
      return !activo;
    }
    return true;
  }

  @Override
  protected void onAdd() {
    RegisterProveedor registerForm = new RegisterProveedor(controller);
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
            "Registrar Nuevo Proveedor",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    registerForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (registerForm.validateForm() && registerForm.saveProveedor()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });
    registerForm
        .getBtnCancelar()
        .addOnClick(
            e -> {
              ModalDialog.closeAllModal();
            });
  }

  @Override
  protected void onUpdateEntity(ProveedorListadoDTO proveedorDTO) {
    var proveedorDetalle = controller.getProveedorById(proveedorDTO.getId());
    UpdateProveedor updateForm = new UpdateProveedor(controller, proveedorDetalle);

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
            "Actualizar Proveedor",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    // Configurar listeners de botones
    updateForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (updateForm.validateForm() && updateForm.updateProveedor()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    updateForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onDeleteEntities(List<ProveedorListadoDTO> proveedores) {
    try {
      for (ProveedorListadoDTO p : proveedores) {
        controller.logicalDeleteProveedor(p.getId());
      }
      showInfoMessage("Proveedor(es) dados de baja correctamente");
      refresh();
    } catch (Exception e) {
      showError("Error al dar de baja los proveedores " + e.getMessage());
    }
  }

  private String getSelectedEstado() {
    if (cmbEstadoProveedor == null) {
      return null;
    }
    String selectedEstado = cmbEstadoProveedor.getSelectedItem().toString();
    return (selectedEstado == null || selectedEstado.equals("Todos")) ? null : selectedEstado;
  }
}
