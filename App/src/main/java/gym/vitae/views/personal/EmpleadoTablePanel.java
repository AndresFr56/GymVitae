package gym.vitae.views.personal;

import gym.vitae.controller.PersonalController;
import gym.vitae.model.Cargo;
import gym.vitae.model.dtos.empleado.EmpleadoListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

/**
 * Panel de tabla específico para gestión de empleados. Extiende BaseTablePanel con lógica
 * personalizada para EmpleadoListadoDTO.
 */
public class EmpleadoTablePanel extends BaseTablePanel<EmpleadoListadoDTO> {

  private static final Logger LOGGER = Logger.getLogger(EmpleadoTablePanel.class.getName());
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final transient PersonalController controller;
  private JComboBox<String> cmbCargo;
  private JComboBox<String> cmbGenero;

  public EmpleadoTablePanel(PersonalController controller) {
    super("Gestión de Personal");
    this.controller = controller;
    loadCargos();
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {
      "SELECT",
      "#",
      "CÓDIGO",
      "NOMBRES",
      "APELLIDOS",
      "CÉDULA",
      "TELÉFONO",
      "GÉNERO",
      "EMAIL",
      "FECHA INGRESO",
      "TIPO CONTRATO"
    };
  }

  @Override
  protected Object[] entityToRow(EmpleadoListadoDTO emp, int rowNumber) {
    return new Object[] {
      false,
      rowNumber,
      emp.codigoEmpleado(),
      emp.nombres(),
      emp.apellidos(),
      emp.cedula(),
      emp.telefono(),
      emp.genero() != null ? emp.genero().name() : "",
      emp.email() != null ? emp.email() : "",
      emp.fechaIngreso() != null ? emp.fechaIngreso().format(DATE_FORMATTER) : "",
      emp.tipoContrato() != null ? emp.tipoContrato().name().replace("_", " ") : ""
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true; // Usar filtrado desde el servidor
  }

  @Override
  protected List<EmpleadoListadoDTO> fetchPagedData(int offset, int limit) {
    return controller.getEmpleados(offset, limit);
  }

  @Override
  protected List<EmpleadoListadoDTO> fetchPagedDataWithFilters(
      String searchText, int offset, int limit) {
    Integer cargoId = getSelectedCargoId();
    String genero = getSelectedGenero();
    return controller.getEmpleadosWithFilters(searchText, cargoId, genero, offset, limit);
  }

  @Override
  protected long fetchTotalCount() {
    return controller.countEmpleados();
  }

  @Override
  protected long fetchTotalCountWithFilters(String searchText) {
    Integer cargoId = getSelectedCargoId();
    String genero = getSelectedGenero();
    return controller.countEmpleadosWithFilters(searchText, cargoId, genero);
  }

  @Override
  protected List<EmpleadoListadoDTO> fetchAllData() {
    return controller.getEmpleados();
  }

  @Override
  protected JPanel createCustomFilters() {
    JPanel panel = new JPanel(new MigLayout("insets 0", "[150][150]"));

    // ComboBox de Cargo
    cmbCargo = new JComboBox<>();
    cmbCargo.addItem("Todos los cargos");
    cmbCargo.addActionListener(e -> refresh());

    // ComboBox de Género
    cmbGenero = new JComboBox<>();
    cmbGenero.addItem("Todos los géneros");
    cmbGenero.addItem("Masculino");
    cmbGenero.addItem("Femenino");
    cmbGenero.addItem("Otro");
    cmbGenero.addActionListener(e -> refresh());

    panel.add(cmbCargo);
    panel.add(cmbGenero);
    return panel;
  }

  @Override
  protected void configureColumnWidths(JTable table) {
    table.getColumnModel().getColumn(2).setPreferredWidth(100);
    table.getColumnModel().getColumn(3).setPreferredWidth(120);
    table.getColumnModel().getColumn(4).setPreferredWidth(120);
    table.getColumnModel().getColumn(5).setPreferredWidth(100);
    table.getColumnModel().getColumn(6).setPreferredWidth(100);
    table.getColumnModel().getColumn(7).setMaxWidth(80);
    table.getColumnModel().getColumn(8).setPreferredWidth(150);
    table.getColumnModel().getColumn(9).setPreferredWidth(120);
    table.getColumnModel().getColumn(10).setPreferredWidth(120);
  }

  @Override
  protected boolean filterEntity(EmpleadoListadoDTO emp, String searchText) {
    return matchesSearchText(emp, searchText) && matchesCargo(emp) && matchesGenero(emp);
  }

  /** Verifica si el empleado coincide con el texto de búsqueda. */
  private boolean matchesSearchText(EmpleadoListadoDTO emp, String searchText) {
    if (searchText.isEmpty()) {
      return true;
    }
    String fullName = (emp.nombres() + " " + emp.apellidos()).toLowerCase();
    return fullName.contains(searchText.toLowerCase());
  }

  /** Verifica si el empleado coincide con el filtro de cargo. */
  private boolean matchesCargo(EmpleadoListadoDTO emp) {
    if (cmbCargo == null || cmbCargo.getSelectedIndex() <= 0) {
      return true;
    }
    String selectedCargo = (String) cmbCargo.getSelectedItem();
    return emp.cargoNombre() != null && emp.cargoNombre().equals(selectedCargo);
  }

  /** Verifica si el empleado coincide con el filtro de género. */
  private boolean matchesGenero(EmpleadoListadoDTO emp) {
    if (cmbGenero == null || cmbGenero.getSelectedIndex() <= 0) {
      return true;
    }
    if (emp.genero() == null) {
      return false;
    }

    String selectedGenero = (String) cmbGenero.getSelectedItem();
    String empGenero = emp.genero().name();

    assert selectedGenero != null;
    if (selectedGenero.equalsIgnoreCase("Masculino")) {
      return empGenero.equals("MASCULINO");
    }
    if (selectedGenero.equalsIgnoreCase("Femenino")) {
      return empGenero.equals("FEMENINO");
    }
    if (selectedGenero.equalsIgnoreCase("Otro")) {
      return empGenero.equals("OTRO");
    }
    return true;
  }

  @Override
  protected void onAdd() {
    RegisterPersonal registerForm = new RegisterPersonal();

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
            "Registrar Nuevo Empleado",
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
              if (registerForm.saveEmpleado()) {
                ModalDialog.closeAllModal();
                refresh(); // Recargar tabla
              }
            });

    registerForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onUpdateEntity(EmpleadoListadoDTO empleadoDTO) {
    // Cargar el empleado completo para edición
    var empleadoDetalle = controller.getEmpleadoById(empleadoDTO.id());
    UpdatePersonal updateForm = new UpdatePersonal(controller, empleadoDetalle);

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
            "Actualizar Empleado",
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
              if (updateForm.updateEmpleado()) {
                ModalDialog.closeAllModal();
                refresh(); // Recargar tabla
              }
            });

    updateForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onDeleteEntities(List<EmpleadoListadoDTO> empleados) {
    // Soft delete - cambia estado a INACTIVO
    for (EmpleadoListadoDTO empleado : empleados) {
      controller.deleteEmpleado(empleado.id());
    }
    loadData();
  }

  private void loadCargos() {
    try {
      List<Cargo> cargos = controller.getCargos();
      if (cmbCargo != null) {
        cmbCargo.removeAllItems();
        cmbCargo.addItem("Todos los cargos");
        cargos.stream()
            .filter(Cargo::getActivo)
            .forEach(cargo -> cmbCargo.addItem(cargo.getNombre()));
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al cargar cargos", e);
    }
  }

  /** Obtiene el ID del cargo seleccionado o null si es "Todos". */
  private Integer getSelectedCargoId() {
    if (cmbCargo == null || cmbCargo.getSelectedIndex() <= 0) {
      return null;
    }

    String cargoNombre = (String) cmbCargo.getSelectedItem();
    try {
      return controller.getCargos().stream()
          .filter(c -> c.getNombre().equals(cargoNombre))
          .findFirst()
          .map(Cargo::getId)
          .orElse(null);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al obtener cargo ID", e);
      return null;
    }
  }

  /** Obtiene el género seleccionado o null si es "Todos". */
  private String getSelectedGenero() {
    if (cmbGenero == null || cmbGenero.getSelectedIndex() <= 0) {
      return null;
    }

    String selectedGenero = (String) cmbGenero.getSelectedItem();
    if (selectedGenero.equalsIgnoreCase("Masculino")) {
      return "MASCULINO";
    }
    if (selectedGenero.equalsIgnoreCase("Femenino")) {
      return "FEMENINO";
    }
    if (selectedGenero.equalsIgnoreCase("Otro")) {
      return "OTRO";
    }
    return null;
  }
}
