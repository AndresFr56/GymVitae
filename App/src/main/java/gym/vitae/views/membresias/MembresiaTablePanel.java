package gym.vitae.views.membresias;

import gym.vitae.controller.MembresiasController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.controller.ClienteController;
import gym.vitae.model.dtos.membresias.MembresiaListadoDTO;
import gym.vitae.model.enums.EstadoMembresia;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

/**
 * Panel de tabla para gestión de membresías.
 */
public class MembresiaTablePanel extends BaseTablePanel<MembresiaListadoDTO> {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  
  private final transient MembresiasController controller;
  private final transient TiposMembresiaController tipoController;
  private final transient ClienteController clienteController;
  
  private JComboBox<String> cmbEstado;
  private JComboBox<String> cmbTipoMembresia;

  public MembresiaTablePanel(
      MembresiasController controller,
      TiposMembresiaController tipoController,
      ClienteController clienteController) {
    super("Gestión de Membresías");
    this.controller = controller;
    this.tipoController = tipoController;
    this.clienteController = clienteController;
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {
      "SELECT", "#", "Cliente", "Documento", "Tipo Membresía", 
      "Fecha Inicio", "Fecha Fin", "Precio", "Estado"
    };
  }

  @Override
  protected Object[] entityToRow(MembresiaListadoDTO membresia, int rowNumber) {
    return new Object[] {
      false, // SELECT checkbox
      rowNumber,
      membresia.getClienteNombre(),
      membresia.getClienteDocumento(),
      membresia.getTipoMembresiaNombre(),
      membresia.getFechaInicio().format(DATE_FORMATTER),
      membresia.getFechaFin().format(DATE_FORMATTER),
      String.format("$%.2f", membresia.getPrecioPagado()),
      membresia.getEstado().name()
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true;
  }

  @Override
  protected List<MembresiaListadoDTO> fetchPagedData(int offset, int limit) {
    return controller.getMembresias();
  }

  @Override
  protected List<MembresiaListadoDTO> fetchPagedDataWithFilters(
      String searchText, int offset, int limit) {
    return controller.getMembresias().stream()
        .filter(m -> filterEntity(m, searchText))
        .skip(offset)
        .limit(limit)
        .toList();
  }

  @Override
  protected long fetchTotalCount() {
    return controller.getMembresias().size();
  }

  @Override
  protected long fetchTotalCountWithFilters(String searchText) {
    return controller.getMembresias().stream()
        .filter(m -> filterEntity(m, searchText))
        .count();
  }

  @Override
  protected JPanel createCustomFilters() {
    JPanel filtersPanel = new JPanel(new MigLayout("insets 0", "[][grow,fill][]", "[]"));

    // Filtro por estado
    cmbEstado = new JComboBox<>(new String[] {
      "Todos", "ACTIVA", "VENCIDA", "CANCELADA", "SUSPENDIDA"
    });
    cmbEstado.addActionListener(e -> loadData());

    // Filtro por tipo de membresía
    String[] tiposArray = tipoController.getTipos().stream()
        .map(t -> t.getNombre())
        .toArray(String[]::new);
    
    String[] tiposConTodos = new String[tiposArray.length + 1];
    tiposConTodos[0] = "Todos";
    System.arraycopy(tiposArray, 0, tiposConTodos, 1, tiposArray.length);
    
    cmbTipoMembresia = new JComboBox<>(tiposConTodos);
    cmbTipoMembresia.addActionListener(e -> loadData());

    filtersPanel.add(new JLabel("Estado:"));
    filtersPanel.add(cmbEstado, "width 200!");
    filtersPanel.add(new JLabel("Tipo:"), "gapleft 10");
    filtersPanel.add(cmbTipoMembresia, "width 200!");

    return filtersPanel;
  }

  @Override
  protected void configureColumnWidths(JTable table) {
    table.getColumnModel().getColumn(0).setMaxWidth(50);  // SELECT
    table.getColumnModel().getColumn(1).setMaxWidth(50);  // #
    table.getColumnModel().getColumn(3).setPreferredWidth(100); // Documento
    table.getColumnModel().getColumn(5).setPreferredWidth(100); // Fecha Inicio
    table.getColumnModel().getColumn(6).setPreferredWidth(100); // Fecha Fin
    table.getColumnModel().getColumn(7).setPreferredWidth(80);  // Precio
    table.getColumnModel().getColumn(8).setPreferredWidth(100); // Estado
  }

  @Override
  protected boolean filterEntity(MembresiaListadoDTO membresia, String searchText) {
    return matchesSearchText(membresia, searchText) 
        && matchesEstado(membresia)
        && matchesTipo(membresia);
  }

  private boolean matchesSearchText(MembresiaListadoDTO membresia, String searchText) {
    String lower = searchText.toLowerCase();
    return membresia.getClienteNombre().toLowerCase().contains(lower)
        || membresia.getClienteDocumento().contains(searchText);
  }

  private boolean matchesEstado(MembresiaListadoDTO membresia) {
    String selectedEstado = getSelectedEstado();
    if (selectedEstado == null) return true;
    return membresia.getEstado().name().equals(selectedEstado);
  }

  private boolean matchesTipo(MembresiaListadoDTO membresia) {
    String selectedTipo = getSelectedTipo();
    if (selectedTipo == null) return true;
    return membresia.getTipoMembresiaNombre().equals(selectedTipo);
  }

  @Override
  protected void onAdd() {
    RegisterMembresia registerForm = new RegisterMembresia(
        controller, tipoController, clienteController);

    Option option = ModalDialog.createOption();
    option.getLayoutOption()
        .setSize(0.5f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            registerForm,
            "Registrar Nueva Membresía",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    registerForm.getBtnGuardar().addOnClick(e -> {
      if (registerForm.validateForm() && registerForm.saveMembresia()) {
        ModalDialog.closeAllModal();
        refresh();
      }
    });

    registerForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onUpdateEntity(MembresiaListadoDTO membresiaDTO) {
    var membresiaDetalle = controller.getMembresiaById(membresiaDTO.getId());
    UpdateMembresia updateForm = new UpdateMembresia(controller, membresiaDetalle);

    Option option = ModalDialog.createOption();
    option.getLayoutOption()
        .setSize(0.5f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            updateForm,
            "Actualizar Membresía",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    updateForm.getBtnGuardar().addOnClick(e -> {
      if (updateForm.validateForm() && updateForm.updateMembresia()) {
        ModalDialog.closeAllModal();
        refresh();
      }
    });

    updateForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onDeleteEntities(List<MembresiaListadoDTO> membresias) {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de cancelar " + membresias.size() + " membresía(s)?",
        "Confirmar Cancelación",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      for (MembresiaListadoDTO membresia : membresias) {
        try {
          controller.cancelarMembresia(membresia.getId());
        } catch (Exception e) {
          JOptionPane.showMessageDialog(
              this,
              "Error al cancelar membresía: " + e.getMessage(),
              "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      refresh();
    }
  }

  private String getSelectedEstado() {
    if (cmbEstado == null) return null;
    String selected = (String) cmbEstado.getSelectedItem();
    return (selected == null || selected.equals("Todos")) ? null : selected;
  }

  private String getSelectedTipo() {
    if (cmbTipoMembresia == null) return null;
    String selected = (String) cmbTipoMembresia.getSelectedItem();
    return (selected == null || selected.equals("Todos")) ? null : selected;
  }
}