package gym.vitae.views.clases;

import gym.vitae.controller.ClasesController;
import gym.vitae.model.Clase;
import gym.vitae.model.enums.NivelClase;
import gym.vitae.views.components.ModalBorder;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.option.Location;
import raven.modal.option.Option;

/**
 * Panel de tabla específico para gestión de clases. Extiende BaseTablePanel con lógica
 * personalizada para Clase.
 */
public class ClaseTablePanel extends BaseTablePanel<Clase> {

  private static final Logger LOGGER = Logger.getLogger(ClaseTablePanel.class.getName());

  private final ClasesController controller;
  private JComboBox<String> cmbNivel;

  public ClaseTablePanel(ClasesController controller) {
    super("Gestión de Clases");
    this.controller = controller;
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {"SELECT", "#", "Nombre", "Duración (min)", "Capacidad", "Nivel", "Estado"};
  }

  @Override
  protected Object[] entityToRow(Clase clase, int rowNumber) {
    return new Object[] {
      false, // SELECT checkbox
      rowNumber,
      clase.getNombre(),
      clase.getDuracionMinutos(),
      clase.getCapacidadMaxima(),
      clase.getNivel().name(),
      clase.getActiva() ? "Activa" : "Inactiva"
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true;
  }

  @Override
  protected List<Clase> fetchPagedData(int offset, int limit) {
    return controller.getClases(offset, limit);
  }

  @Override
  protected List<Clase> fetchPagedDataWithFilters(String searchText, int offset, int limit) {
    String nivel = getSelectedNivel();
    return controller.getClases().stream()
        .filter(c -> filterEntity(c, searchText))
        .skip(offset)
        .limit(limit)
        .toList();
  }

  @Override
  protected long fetchTotalCount() {
    return controller.countClases();
  }

  @Override
  protected long fetchTotalCountWithFilters(String searchText) {
    String nivel = getSelectedNivel();
    return controller.getClases().stream().filter(c -> filterEntity(c, searchText)).count();
  }

  @Override
  protected JPanel createCustomFilters() {
    JPanel filtersPanel = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[]"));

    // Filtro por nivel
    cmbNivel =
        new JComboBox<>(new String[] {"Todos", "PRINCIPIANTE", "INTERMEDIO", "AVANZADO", "TODOS"});
    cmbNivel.addActionListener(e -> loadData());

    filtersPanel.add(new JLabel("Nivel:"));
    filtersPanel.add(cmbNivel, "width 200!");

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
  protected boolean filterEntity(Clase clase, String searchText) {
    return matchesSearchText(clase, searchText) && matchesNivel(clase);
  }

  /** Verifica si la clase coincide con el texto de búsqueda. */
  private boolean matchesSearchText(Clase clase, String searchText) {
    String lower = searchText.toLowerCase();
    return clase.getNombre().toLowerCase().contains(lower)
        || (clase.getDescripcion() != null && clase.getDescripcion().toLowerCase().contains(lower));
  }

  /** Verifica si la clase coincide con el filtro de nivel. */
  private boolean matchesNivel(Clase clase) {
    String selectedNivel = getSelectedNivel();
    if (selectedNivel == null) return true;
    return clase.getNivel().name().equals(selectedNivel);
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
  protected void onUpdateEntity(Clase clase) {
    UpdateClase updateForm = new UpdateClase(controller, clase);

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
  protected void onDeleteEntities(List<Clase> clases) {
    // Implementar soft delete cambiando estado a inactiva
  }

  /** Obtiene el nivel seleccionado o null si es "Todos". */
  private String getSelectedNivel() {
    if (cmbNivel == null) return null;
    String selected = (String) cmbNivel.getSelectedItem();
    return (selected == null || selected.equals("Todos")) ? null : selected;
  }
}
