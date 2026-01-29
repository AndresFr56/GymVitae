package gym.vitae.views.membresias;

import gym.vitae.controller.ClienteController;
import gym.vitae.controller.MembresiasController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.model.dtos.membresias.MembresiaListadoDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.model.enums.EstadoMembresia;
import gym.vitae.views.components.tables.BaseTablePanel;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

public class MembresiaTablePanel extends BaseTablePanel<MembresiaListadoDTO> {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final MembresiasController controller;
  private final TiposMembresiaController tipoController;
  private final ClienteController clienteController;
  private JComboBox<String> cmbTipos;
  private boolean tiposCargados = false;

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
  protected JPanel createCustomFilters() {
    JPanel panel = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[]"));

    panel.add(new JLabel("Tipo de Membresía:"));
    cmbTipos = new JComboBox<>();
    panel.add(cmbTipos, "width 200!");


    cmbTipos.addActionListener(e -> refresh());

    return panel;
  }

  private void cargarTipos() {
    if (cmbTipos == null || tipoController == null) return;

    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    model.addElement("Todos");


    tipoController.getTipos().stream()
            .filter(TipoMembresiaListadoDTO::getActivo)
            .forEach(t -> model.addElement(t.getNombre()));

    cmbTipos.setModel(model);
  }

  @Override
  public void loadData() {

    if (!tiposCargados) {
      cargarTipos();
      tiposCargados = true;
    }
    super.loadData();
  }

  @Override
  protected String[] getColumnNames() {
    return new String[] {
      "SELECT", "#", "Cliente", "Documento",
      "Tipo", "Estado", "Fecha Inicio", "Fecha Fin",
      "Precio Pagado"
    };
  }

  @Override
  protected List<MembresiaListadoDTO> fetchAllData() {
    return controller.getMembresias();
  }

  @Override
  protected Object[] entityToRow(MembresiaListadoDTO m, int rowNumber) {
    return new Object[] {
      false,
      rowNumber,
      m.getClienteNombre(),
      m.getClienteDocumento(),
      m.getTipoMembresiaNombre(),
      m.getEstado().toString(),
      m.getFechaInicio() != null ? m.getFechaInicio().format(DATE_FORMAT) : "",
      m.getFechaFin() != null ? m.getFechaFin().format(DATE_FORMAT) : "",
      m.getPrecioPagado()
    };
  }

  @Override
  protected void onAdd() {
    RegisterMembresia form = new RegisterMembresia(controller, tipoController, clienteController);
    Option option = ModalDialog.createOption();
    option.getLayoutOption().setSize(0.45f, 1f).setLocation(Location.TRAILING, Location.TOP).setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(this, new SimpleModalBorder(form, "Nueva Membresía", SimpleModalBorder.DEFAULT_OPTION, (c, a) -> {
    }), option);

    form.getBtnGuardar().addOnClick(e -> {
      if (form.validateForm() && form.saveMembresia()) {
        ModalDialog.closeAllModal();
        refresh();
      }
    });
    form.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onUpdateEntity(MembresiaListadoDTO dto) {
    var detalle = controller.getMembresiaById(dto.getId());
    UpdateMembresia form = new UpdateMembresia(controller, tipoController, detalle);
    Option option = ModalDialog.createOption();
    option.getLayoutOption().setSize(0.45f, 1f).setLocation(Location.TRAILING, Location.TOP).setAnimateDistance(0.7f, 0);

    ModalDialog.showModal(this, new SimpleModalBorder(form, "Editar Membresía", SimpleModalBorder.DEFAULT_OPTION, (c, a) -> {
    }), option);

    form.getBtnGuardar().addOnClick(e -> {
      if (form.validateForm() && form.updateMembresia()) {
        ModalDialog.closeAllModal();
        refresh();
      }
    });
    form.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
  }

  @Override
  protected void onDeleteEntities(List<MembresiaListadoDTO> entities) {

    List<MembresiaListadoDTO> yaCanceladas = entities.stream()
            .filter(m -> m.getEstado() == EstadoMembresia.CANCELADA)
            .collect(Collectors.toList());

    if (!yaCanceladas.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Ya esta cancelada esta Membresia.", "Aviso", JOptionPane.WARNING_MESSAGE);
      if (yaCanceladas.size() == entities.size()) return;
    }

    List<MembresiaListadoDTO> paraCancelar = entities.stream()
            .filter(m -> m.getEstado() != EstadoMembresia.CANCELADA)
            .collect(Collectors.toList());

    int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas cancelar las membresías seleccionadas?", "Confirmar", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      for (MembresiaListadoDTO dto : paraCancelar) {
        controller.cancelarMembresia(dto.getId());
      }
      JOptionPane.showMessageDialog(this, "Membresía(s) cancelada(s) correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      refresh();
    }
  }

  @Override
  protected boolean filterEntity(MembresiaListadoDTO entity, String searchText) {
    boolean matchesSearchText = true;
    if (searchText != null && !searchText.isEmpty()) {
      String lowerSearch = searchText.toLowerCase();
      matchesSearchText = entity.getClienteNombre().toLowerCase().contains(lowerSearch)
              || entity.getClienteDocumento().toLowerCase().contains(lowerSearch)
              || entity.getTipoMembresiaNombre().toLowerCase().contains(lowerSearch);
    }

    boolean matchesTipo = true;
    if (cmbTipos != null && cmbTipos.getSelectedItem() != null) {
      String selectedTipo = cmbTipos.getSelectedItem().toString();
      if (!selectedTipo.equals("Todos")) {
        matchesTipo = entity.getTipoMembresiaNombre().equals(selectedTipo);
      }
    }

    return matchesSearchText && matchesTipo;
  }
}