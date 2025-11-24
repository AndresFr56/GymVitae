package gym.vitae.views.clientes;

import gym.vitae.controller.ClienteController;
import gym.vitae.model.Cliente;
import gym.vitae.views.components.ModalBorder;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.option.Location;
import raven.modal.option.Option;

/**
 * Panel de tabla específico para gestión de clientes. Extiende BaseTablePanel con lógica
 * personalizada para Cliente.
 */
public class ClienteTablePanel extends BaseTablePanel<Cliente> {

  private final ClienteController controller;
  private JComboBox<String> cmbEstado;

  public ClienteTablePanel(ClienteController controller) {
    super("Gestión de Clientes");
    this.controller = controller;
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {
      "SELECT", "#", "Código", "Nombres", "Apellidos", "Cédula", "Teléfono", "Email", "Estado"
    };
  }

  @Override
  protected Object[] entityToRow(Cliente cliente, int rowNumber) {
    return new Object[] {
      false, // SELECT checkbox
      rowNumber,
      cliente.getCodigoCliente(),
      cliente.getNombres(),
      cliente.getApellidos(),
      cliente.getCedula(),
      cliente.getTelefono(),
      cliente.getEmail() != null ? cliente.getEmail() : "",
      cliente.getEstado().name()
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true;
  }

  @Override
  protected List<Cliente> fetchPagedData(int offset, int limit) {
    return controller.getClientes(offset, limit);
  }

  @Override
  protected List<Cliente> fetchPagedDataWithFilters(String searchText, int offset, int limit) {
    return controller.getClientes().stream()
        .filter(c -> filterEntity(c, searchText))
        .skip(offset)
        .limit(limit)
        .toList();
  }

  @Override
  protected long fetchTotalCount() {
    return controller.countClientes();
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
    table.getColumnModel().getColumn(0).setMaxWidth(50); // SELECT
    table.getColumnModel().getColumn(1).setMaxWidth(50); // #
    table.getColumnModel().getColumn(2).setPreferredWidth(100); // Código
    table.getColumnModel().getColumn(5).setPreferredWidth(100); // Cédula
    table.getColumnModel().getColumn(6).setPreferredWidth(100); // Teléfono
    table.getColumnModel().getColumn(8).setPreferredWidth(100); // Estado
  }

  @Override
  protected boolean filterEntity(Cliente cliente, String searchText) {
    return matchesSearchText(cliente, searchText) && matchesEstado(cliente);
  }

  /** Verifica si el cliente coincide con el texto de búsqueda. */
  private boolean matchesSearchText(Cliente cliente, String searchText) {
    String lower = searchText.toLowerCase();
    return cliente.getNombres().toLowerCase().contains(lower)
        || cliente.getApellidos().toLowerCase().contains(lower)
        || cliente.getCedula().contains(searchText)
        || (cliente.getEmail() != null && cliente.getEmail().toLowerCase().contains(lower));
  }

  /** Verifica si el cliente coincide con el filtro de estado. */
  private boolean matchesEstado(Cliente cliente) {
    String selectedEstado = getSelectedEstado();
    if (selectedEstado == null) return true;
    return cliente.getEstado().name().equals(selectedEstado);
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
        SwingUtilities.getWindowAncestor(this),
        new ModalBorder(
            registerForm,
            (modalController, action) -> {
              if (action == ModalBorder.OPENED) {
                // Modal abierto
              }
            }),
        option);
  }

  @Override
  protected void onUpdateEntity(Cliente cliente) {
    UpdateCliente updateForm = new UpdateCliente(controller, cliente);

    Option option = ModalDialog.createOption();
    option
        .getLayoutOption()
        .setSize(0.4f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        SwingUtilities.getWindowAncestor(this),
        new ModalBorder(
            updateForm,
            (modalController, action) -> {
              if (action == ModalBorder.OPENED) {
                // Modal abierto
              }
            }),
        option);
  }

  @Override
  protected void onDeleteEntities(List<Cliente> clientes) {
    // Implementar soft delete cambiando estado a SUSPENDIDO
  }

  /** Obtiene el estado seleccionado o null si es "Todos". */
  private String getSelectedEstado() {
    if (cmbEstado == null) return null;
    String selected = (String) cmbEstado.getSelectedItem();
    return (selected == null || selected.equals("Todos")) ? null : selected;
  }
}
