package gym.vitae.views.clientes;

import gym.vitae.controller.ClienteController;
import gym.vitae.model.dtos.cliente.ClienteListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

/**
 * Panel de tabla específico para gestión de clientes. Extiende BaseTablePanel con lógica
 * personalizada para ClienteListadoDTO.
 */
public class ClienteTablePanel extends BaseTablePanel<ClienteListadoDTO> {

  private final transient ClienteController controller;
  private JComboBox<String> cmbEstado;

  public ClienteTablePanel(ClienteController controller) {
    super("Gestión de Clientes");
    this.controller = controller;
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {
      "SELECT", "#", "Código", "Nombre Completo", "Cédula", "Teléfono", "Estado"
    };
  }

  @Override
  protected Object[] entityToRow(ClienteListadoDTO cliente, int rowNumber) {
    return new Object[] {
      false, // SELECT checkbox
      rowNumber,
      cliente.codigoCliente(),
      cliente.nombreCompleto(),
      cliente.cedula(),
      cliente.telefono(),
      cliente.estado().name()
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true;
  }

  @Override
  protected List<ClienteListadoDTO> fetchPagedData(int offset, int limit) {
    return controller.getClientes();
  }

  @Override
  protected List<ClienteListadoDTO> fetchPagedDataWithFilters(
      String searchText, int offset, int limit) {
    return controller.getClientes().stream()
        .filter(c -> filterEntity(c, searchText))
        .skip(offset)
        .limit(limit)
        .toList();
  }

  @Override
  protected long fetchTotalCount() {
    return controller.getClientes().size();
  }

  @Override
  protected long fetchTotalCountWithFilters(String searchText) {
    return controller.getClientes().stream().filter(c -> filterEntity(c, searchText)).count();
  }

  @Override
  protected JPanel createCustomFilters() {
    JPanel filtersPanel = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[]"));

    // Filtro por estado
    cmbEstado = new JComboBox<>(new String[] {"Todos", "ACTIVO", "INACTIVO", "SUSPENDIDO"});
    cmbEstado.addActionListener(e -> loadData());

    filtersPanel.add(new JLabel("Estado:"));
    filtersPanel.add(cmbEstado, "width 200!");

    return filtersPanel;
  }

  @Override
  protected void configureColumnWidths(JTable table) {
    // Columnas: 0=SELECT, 1=#, 2=Código, 3=Nombre, 4=Cédula, 5=Teléfono, 6=Estado
    table.getColumnModel().getColumn(0).setMaxWidth(50); // SELECT
    table.getColumnModel().getColumn(1).setMaxWidth(50); // #
    table.getColumnModel().getColumn(2).setPreferredWidth(100); // Código
    table.getColumnModel().getColumn(4).setPreferredWidth(100); // Cédula
    table.getColumnModel().getColumn(5).setPreferredWidth(100); // Teléfono
    table.getColumnModel().getColumn(6).setPreferredWidth(100); // Estado
  }

  @Override
  protected boolean filterEntity(ClienteListadoDTO cliente, String searchText) {
    return matchesSearchText(cliente, searchText) && matchesEstado(cliente);
  }

  /** Verifica si el cliente coincide con el texto de búsqueda. */
  private boolean matchesSearchText(ClienteListadoDTO cliente, String searchText) {
    String lower = searchText.toLowerCase();
    return cliente.nombreCompleto().toLowerCase().contains(lower)
        || cliente.cedula().contains(searchText);
  }

  /** Verifica si el cliente coincide con el filtro de estado. */
  private boolean matchesEstado(ClienteListadoDTO cliente) {
    String selectedEstado = getSelectedEstado();
    if (selectedEstado == null) return true;
    return cliente.estado().name().equals(selectedEstado);
  }

  @Override
  protected void onAdd() {
    RegisterCliente registerForm = new RegisterCliente(controller);

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
            "Registrar Nuevo Cliente",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {
              // No hacer nada aquí - los botones se manejan internamente
            }),
        option);

    // Configurar listeners de botones
    registerForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (registerForm.validateForm() && registerForm.saveCliente()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    registerForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onUpdateEntity(ClienteListadoDTO clienteDTO) {
    // Cargar el cliente completo para edición
    var clienteDetalle = controller.getClienteById(clienteDTO.id());
    UpdateCliente updateForm = new UpdateCliente(controller, clienteDetalle);

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
            "Actualizar Cliente",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {
              // No hacer nada aquí - los botones se manejan internamente
            }),
        option);

    // Configurar listeners de botones
    updateForm
        .getBtnGuardar()
        .addOnClick(
            e -> {
              if (updateForm.validateForm() && updateForm.updateCliente()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    updateForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onDeleteEntities(List<ClienteListadoDTO> clientes) {
    // Implementar soft delete cambiando estado a SUSPENDIDO
  }

  /** Obtiene el estado seleccionado o null si es "Todos". */
  private String getSelectedEstado() {
    if (cmbEstado == null) return null;
    String selected = (String) cmbEstado.getSelectedItem();
    return (selected == null || selected.equals("Todos")) ? null : selected;
  }
}
