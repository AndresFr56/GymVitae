package gym.vitae.views.membresias;

import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.controller.BeneficiosController;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

/**
 * Panel de tabla para gestión de tipos de membresía.
 */
public class TipoMembresiaTablePanel extends BaseTablePanel<TipoMembresiaListadoDTO> {

  private final transient TiposMembresiaController controller;
  private JComboBox<String> cmbEstado;
  private JButton btnGestionarBeneficios;

  public TipoMembresiaTablePanel(TiposMembresiaController controller) {
    super("Gestión de Tipos de Membresía");
    this.controller = controller;
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {
      "SELECT", "#", "Nombre", "Descripción", "Duración (días)", 
      "Costo", "Acceso Completo", "Estado"
    };
  }

  @Override
  protected Object[] entityToRow(TipoMembresiaListadoDTO tipo, int rowNumber) {
    return new Object[] {
      false, // SELECT checkbox
      rowNumber,
      tipo.getNombre(),
      tipo.getDescripcion(),
      tipo.getDuracionDias(),
      String.format("$%.2f", tipo.getCosto()),
      tipo.getAccesoCompleto() ? "Sí" : "No",
      tipo.getActivo() ? "ACTIVO" : "INACTIVO"
    };
  }

  @Override
  protected boolean usesServerPagination() {
    return true;
  }

  @Override
  protected List<TipoMembresiaListadoDTO> fetchPagedData(int offset, int limit) {
    return controller.getTipos();
  }

  @Override
  protected List<TipoMembresiaListadoDTO> fetchPagedDataWithFilters(
      String searchText, int offset, int limit) {
    return controller.getTipos().stream()
        .filter(t -> filterEntity(t, searchText))
        .skip(offset)
        .limit(limit)
        .toList();
  }

  @Override
  protected long fetchTotalCount() {
    return controller.getTipos().size();
  }

  @Override
  protected long fetchTotalCountWithFilters(String searchText) {
    return controller.getTipos().stream()
        .filter(t -> filterEntity(t, searchText))
        .count();
  }

  @Override
  protected JPanel createCustomFilters() {
    JPanel filtersPanel = new JPanel(new MigLayout("insets 0", "[][grow,fill][]", "[]"));

    // Filtro por estado
    cmbEstado = new JComboBox<>(new String[] {"Todos", "ACTIVO", "INACTIVO"});
    cmbEstado.addActionListener(e -> loadData());

    // Botón para gestionar beneficios
    btnGestionarBeneficios = new JButton("Gestionar Beneficios");
    btnGestionarBeneficios.addActionListener(e -> abrirGestionBeneficios());

    filtersPanel.add(new JLabel("Estado:"));
    filtersPanel.add(cmbEstado, "width 200!");
    filtersPanel.add(btnGestionarBeneficios, "gapleft 10");

    return filtersPanel;
  }

  @Override
  protected void configureColumnWidths(JTable table) {
    table.getColumnModel().getColumn(0).setMaxWidth(50);  // SELECT
    table.getColumnModel().getColumn(1).setMaxWidth(50);  // #
    table.getColumnModel().getColumn(4).setPreferredWidth(120); // Duración
    table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Costo
    table.getColumnModel().getColumn(6).setPreferredWidth(120); // Acceso Completo
    table.getColumnModel().getColumn(7).setPreferredWidth(80);  // Estado
  }

  @Override
  protected boolean filterEntity(TipoMembresiaListadoDTO tipo, String searchText) {
    return matchesSearchText(tipo, searchText) && matchesEstado(tipo);
  }

  private boolean matchesSearchText(TipoMembresiaListadoDTO tipo, String searchText) {
    String lower = searchText.toLowerCase();
    return tipo.getNombre().toLowerCase().contains(lower)
        || (tipo.getDescripcion() != null && tipo.getDescripcion().toLowerCase().contains(lower));
  }

  private boolean matchesEstado(TipoMembresiaListadoDTO tipo) {
    String selectedEstado = getSelectedEstado();
    if (selectedEstado == null) return true;
    boolean activo = selectedEstado.equals("ACTIVO");
    return tipo.getActivo() == activo;
  }

  @Override
  protected void onAdd() {
    RegisterTipoMembresia registerForm = new RegisterTipoMembresia(controller);

    Option option = ModalDialog.createOption();
    option.getLayoutOption()
        .setSize(0.4f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            registerForm,
            "Registrar Tipo de Membresía",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    registerForm.getBtnGuardar().addOnClick(e -> {
      if (registerForm.validateForm() && registerForm.saveTipoMembresia()) {
        ModalDialog.closeAllModal();
        refresh();
      }
    });

    registerForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onUpdateEntity(TipoMembresiaListadoDTO tipoDTO) {
    var tipoDetalle = controller.getTipoById(tipoDTO.getId());
    UpdateTipoMembresia updateForm = new UpdateTipoMembresia(controller, tipoDetalle);

    Option option = ModalDialog.createOption();
    option.getLayoutOption()
        .setSize(0.4f, 1f)
        .setLocation(Location.TRAILING, Location.TOP)
        .setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(
        this,
        new SimpleModalBorder(
            updateForm,
            "Actualizar Tipo de Membresía",
            SimpleModalBorder.DEFAULT_OPTION,
            (control, action) -> {}),
        option);

    updateForm.getBtnGuardar().addOnClick(e -> {
      if (updateForm.validateForm() && updateForm.updateTipoMembresia()) {
        ModalDialog.closeAllModal();
        refresh();
      }
    });

    updateForm.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onDeleteEntities(List<TipoMembresiaListadoDTO> tipos) {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de desactivar " + tipos.size() + " tipo(s) de membresía?",
        "Confirmar Desactivación",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      for (TipoMembresiaListadoDTO tipo : tipos) {
        try {
          controller.deleteTipo(tipo.getId());
        } catch (Exception e) {
          JOptionPane.showMessageDialog(
              this,
              "Error al desactivar tipo: " + e.getMessage(),
              "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      refresh();
    }
  }
  
  private void abrirGestionBeneficios() {
      BeneficiosController beneficiosController = new BeneficiosController();
      AsignarBeneficiosPanel panel = new AsignarBeneficiosPanel(
          controller, beneficiosController);
  
      Option option = ModalDialog.createOption();
      option.getLayoutOption()
          .setSize(0.6f, 0.8f)
          .setLocation(Location.CENTER, Location.CENTER);
  
      ModalDialog.showModal(
          this,
          new SimpleModalBorder(
              panel,
              "Gestionar Beneficios de Membresías",
              SimpleModalBorder.DEFAULT_OPTION,
              (control, action) -> {}),
          option);
  }

  private String getSelectedEstado() {
    if (cmbEstado == null) return null;
    String selected = (String) cmbEstado.getSelectedItem();
    return (selected == null || selected.equals("Todos")) ? null : selected;
  }
}