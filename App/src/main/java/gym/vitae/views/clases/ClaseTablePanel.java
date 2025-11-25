package gym.vitae.views.clases;

import gym.vitae.controller.ClasesController;
import gym.vitae.model.dtos.clase.ClaseDetalleDTO;
import gym.vitae.model.dtos.clase.ClaseListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

/**
 * Panel de tabla específico para gestión de clases. Extiende BaseTablePanel con lógica
 * personalizada para ClaseListadoDTO.
 */
public class ClaseTablePanel extends BaseTablePanel<ClaseListadoDTO> {

  private static final Logger LOGGER = Logger.getLogger(ClaseTablePanel.class.getName());

  private final transient ClasesController controller;
  private JComboBox<String> cmbNivel;
  private JCheckBox chkConCupos;

  public ClaseTablePanel(ClasesController controller) {
    super("Gestión de Clases");
    this.controller = controller;
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {"SELECT", "#", "Nombre", "Duración (min)", "Capacidad", "Nivel", "Estado"};
  }

  @Override
  protected Object[] entityToRow(ClaseListadoDTO clase, int rowNumber) {
    return new Object[] {
      false, // SELECT checkbox
      rowNumber,
      clase.nombre(),
      clase.duracionMinutos(),
      clase.capacidadMaxima(),
      clase.nivel().name(),
      Boolean.TRUE.equals(clase.activa()) ? "Activa" : "Inactiva"
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true;
  }

  @Override
  protected List<ClaseListadoDTO> fetchPagedData(int offset, int limit) {
    return controller.getClases(offset, limit);
  }

  @Override
  protected List<ClaseListadoDTO> fetchPagedDataWithFilters(
      String searchText, int offset, int limit) {
    String nivel = getSelectedNivel();
    Boolean conCupos = getConCuposFilter();
    return controller.getClasesWithFilters(searchText, nivel, conCupos, offset, limit);
  }

  @Override
  protected long fetchTotalCount() {
    return controller.countClases();
  }

  @Override
  protected long fetchTotalCountWithFilters(String searchText) {
    String nivel = getSelectedNivel();
    Boolean conCupos = getConCuposFilter();
    return controller.countClasesWithFilters(searchText, nivel, conCupos);
  }

  @Override
  protected JPanel createCustomFilters() {
    JPanel filtersPanel = new JPanel(new MigLayout("insets 0", "[][grow,fill][][]", "[]"));

    // Filtro por nivel
    cmbNivel =
        new JComboBox<>(new String[] {"Todos", "PRINCIPIANTE", "INTERMEDIO", "AVANZADO", "TODOS"});
    cmbNivel.addActionListener(e -> loadData());

    // Filtro por disponibilidad de cupos
    chkConCupos = new JCheckBox("Con cupos disponibles");
    chkConCupos.addActionListener(e -> loadData());

    filtersPanel.add(new JLabel("Nivel:"));
    filtersPanel.add(cmbNivel, "width 150!");
    filtersPanel.add(chkConCupos, "gapleft 20");

    return filtersPanel;
  }

  @Override
  protected void configureColumnWidths(JTable table) {
    table.getColumnModel().getColumn(0).setMaxWidth(50); // SELECT
    table.getColumnModel().getColumn(1).setMaxWidth(50); // #
    table.getColumnModel().getColumn(3).setPreferredWidth(120); // Duración
    table.getColumnModel().getColumn(4).setPreferredWidth(100); // Capacidad
    table.getColumnModel().getColumn(5).setPreferredWidth(130); // Nivel
    table.getColumnModel().getColumn(6).setPreferredWidth(100); // Estado
  }

  @Override
  protected boolean filterEntity(ClaseListadoDTO clase, String searchText) {
    return matchesSearchText(clase, searchText) && matchesNivel(clase);
  }

  /** Verifica si la clase coincide con el texto de búsqueda. */
  private boolean matchesSearchText(ClaseListadoDTO clase, String searchText) {
    String lower = searchText.toLowerCase();
    return clase.nombre().toLowerCase().contains(lower);
  }

  /** Verifica si la clase coincide con el filtro de nivel. */
  private boolean matchesNivel(ClaseListadoDTO clase) {
    String selectedNivel = getSelectedNivel();
    if (selectedNivel == null) return true;
    return clase.nivel().name().equals(selectedNivel);
  }

  @Override
  protected void onAdd() {
    RegisterClase registerForm = new RegisterClase(controller);

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
            "Registrar Nueva Clase",
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
              if (registerForm.validateForm() && registerForm.saveClase()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    registerForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onUpdateEntity(ClaseListadoDTO claseListado) {
    // Cargar detalle completo para edición
    ClaseDetalleDTO claseDetalle = controller.getClaseById(claseListado.id());
    UpdateClase updateForm = new UpdateClase(controller, claseDetalle);

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
            "Actualizar Clase",
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
              if (updateForm.validateForm() && updateForm.updateClase()) {
                ModalDialog.closeAllModal();
                refresh();
              }
            });

    updateForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onDeleteEntities(List<ClaseListadoDTO> clases) {
    // Implementar soft delete cambiando estado a inactiva
    for (ClaseListadoDTO clase : clases) {
      controller.deleteClase(clase.id());
    }
    loadData();
  }

  /** Obtiene el nivel seleccionado o null si es "Todos". */
  private String getSelectedNivel() {
    if (cmbNivel == null) return null;
    String selected = (String) cmbNivel.getSelectedItem();
    return (selected == null || selected.equals("Todos")) ? null : selected;
  }

  /** Obtiene el filtro de disponibilidad de cupos o null si no está seleccionado. */
  private Boolean getConCuposFilter() {
    return chkConCupos.isSelected() ? Boolean.TRUE : null;
  }
}
