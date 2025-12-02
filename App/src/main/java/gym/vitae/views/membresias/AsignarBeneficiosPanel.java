package gym.vitae.views.membresias;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.BeneficiosController;
import gym.vitae.controller.MembresiaBeneficioController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.model.dtos.membresias.*;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

/**
 * Panel para asignar beneficios a tipos de membresía.
 * Permite visualizar los beneficios actuales y agregar/eliminar beneficios.
 */
public class AsignarBeneficiosPanel extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(AsignarBeneficiosPanel.class.getName());

  private final TiposMembresiaController tipoController;
  private final BeneficiosController beneficioController;
  private final MembresiaBeneficioController asociacionController;

  private JPanel contentPanel;
  private JLabel lblError;

  // Componentes
  private JComboBox<TipoMembresiaListadoDTO> cmbTipoMembresia;
  private JTable tableBeneficiosAsignados;
  private DefaultTableModel modelAsignados;
  private JList<BeneficioListadoDTO> listBeneficiosDisponibles;
  private DefaultListModel<BeneficioListadoDTO> modelDisponibles;

  // Botones
  private ButtonOutline btnAgregar;
  private ButtonOutline btnEliminar;

  public AsignarBeneficiosPanel(
      TiposMembresiaController tipoController,
      BeneficiosController beneficioController) {
    this.tipoController = tipoController;
    this.beneficioController = beneficioController;
    this.asociacionController = new MembresiaBeneficioController();
    init();
  }

  private void init() {
    setLayout(new BorderLayout());

    lblError = new JLabel();
    lblError.setVisible(false);
    lblError.putClientProperty(
        FlatClientProperties.STYLE, "foreground:#F44336;font:bold;border:0,0,10,0");

    contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 10", "[fill]", ""));

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    JPanel topPanel = new JPanel(new MigLayout("fillx,wrap,insets 10 20 0 20", "[fill]"));
    topPanel.add(lblError);

    add(topPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    initializeComponents();
    buildForm();
    loadTiposMembresia();
  }

  private void initializeComponents() {
    cmbTipoMembresia = new JComboBox<>();
    cmbTipoMembresia.setRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof TipoMembresiaListadoDTO tipo) {
          setText(tipo.getNombre() + " - $" + tipo.getCosto());
        }
        return this;
      }
    });
    cmbTipoMembresia.addActionListener(e -> cargarBeneficios());

    modelAsignados = new DefaultTableModel(
        new Object[]{"ID", "Beneficio", "Descripción"}, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tableBeneficiosAsignados = new JTable(modelAsignados);
    tableBeneficiosAsignados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    modelDisponibles = new DefaultListModel<>();
    listBeneficiosDisponibles = new JList<>(modelDisponibles);
    listBeneficiosDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listBeneficiosDisponibles.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof BeneficioListadoDTO beneficio) {
          setText(beneficio.getNombre() + " - " + beneficio.getDescripcion());
        }
        return this;
      }
    });

    // Botones
    btnAgregar = new ButtonOutline("Agregar →");
    btnAgregar.putClientProperty(FlatClientProperties.STYLE, "foreground:#4CAF50;font:bold");
    btnAgregar.addOnClick(e -> agregarBeneficio());

    btnEliminar = new ButtonOutline("← Eliminar");
    btnEliminar.putClientProperty(FlatClientProperties.STYLE, "foreground:#F44336;font:bold");
    btnEliminar.addOnClick(e -> eliminarBeneficio());
  }

  private void buildForm() {
    createTitle("Seleccionar Tipo de Membresía");

    contentPanel.add(new JLabel("Tipo de Membresía:"));
    contentPanel.add(cmbTipoMembresia);

    createTitle("Gestión de Beneficios");

    JPanel splitPanel = new JPanel(new MigLayout(
        "fill,insets 0", 
        "[fill,grow][100][fill,grow]", 
        "[fill,grow]"));

    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.add(new JLabel("Beneficios Disponibles"), BorderLayout.NORTH);
    JScrollPane scrollDisponibles = new JScrollPane(listBeneficiosDisponibles);
    scrollDisponibles.setPreferredSize(new Dimension(250, 300));
    leftPanel.add(scrollDisponibles, BorderLayout.CENTER);

    JPanel centerPanel = new JPanel(new MigLayout("fill,insets 0", "[fill]", "[][][grow][]"));
    centerPanel.add(new JLabel(" "), "grow");
    centerPanel.add(btnAgregar, "center");
    centerPanel.add(new JLabel(" "), "grow");
    centerPanel.add(btnEliminar, "center");

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.add(new JLabel("Beneficios Asignados"), BorderLayout.NORTH);
    JScrollPane scrollAsignados = new JScrollPane(tableBeneficiosAsignados);
    scrollAsignados.setPreferredSize(new Dimension(250, 300));
    rightPanel.add(scrollAsignados, BorderLayout.CENTER);

    splitPanel.add(leftPanel, "grow");
    splitPanel.add(centerPanel, "center");
    splitPanel.add(rightPanel, "grow");

    contentPanel.add(splitPanel, "grow,height 350!");
  }

  private void createTitle(String title) {
    JLabel lblTitle = new JLabel(title);
    lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lblTitle, "gapy 10 5");
  }

  private void loadTiposMembresia() {
    try {
      cmbTipoMembresia.removeAllItems();
      var tipos = tipoController.getTipos().stream()
          .filter(TipoMembresiaListadoDTO::getActivo)
          .toList();
      
      for (TipoMembresiaListadoDTO tipo : tipos) {
        cmbTipoMembresia.addItem(tipo);
      }

      if (cmbTipoMembresia.getItemCount() > 0) {
        cmbTipoMembresia.setSelectedIndex(0);
      }
    } catch (Exception e) {
      showErrorMessage("Error al cargar tipos de membresía: " + e.getMessage());
    }
  }

  private void cargarBeneficios() {
    TipoMembresiaListadoDTO tipoSeleccionado = 
        (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();
    
    if (tipoSeleccionado == null) return;

    try {
      // Cargar beneficios asignados
      var asociaciones = asociacionController.getAll().stream()
          .filter(a -> a.getTipoMembresiaNombre().equals(tipoSeleccionado.getNombre()))
          .toList();

      modelAsignados.setRowCount(0);
      for (MembresiaBeneficioListadoDTO asoc : asociaciones) {
        modelAsignados.addRow(new Object[]{
            asoc.getId(),
            asoc.getBeneficioNombre(),
            "" 
        });
      }

      List<String> beneficiosAsignados = asociaciones.stream()
          .map(MembresiaBeneficioListadoDTO::getBeneficioNombre)
          .toList();

      modelDisponibles.clear();
      beneficioController.getBeneficios().stream()
          .filter(BeneficioListadoDTO::getActivo)
          .filter(b -> !beneficiosAsignados.contains(b.getNombre()))
          .forEach(modelDisponibles::addElement);

      hideError();
    } catch (Exception e) {
      showErrorMessage("Error al cargar beneficios: " + e.getMessage());
      LOGGER.severe("Error al cargar beneficios: " + e.getMessage());
    }
  }

  private void agregarBeneficio() {
    BeneficioListadoDTO beneficioSeleccionado = listBeneficiosDisponibles.getSelectedValue();
    TipoMembresiaListadoDTO tipoSeleccionado = 
        (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();

    if (beneficioSeleccionado == null) {
      showErrorMessage("Debe seleccionar un beneficio disponible");
      return;
    }

    if (tipoSeleccionado == null) {
      showErrorMessage("Debe seleccionar un tipo de membresía");
      return;
    }

    try {
      MembresiaBeneficioCreateDTO dto = new MembresiaBeneficioCreateDTO(
          tipoSeleccionado.getId(),
          beneficioSeleccionado.getId()
      );

      asociacionController.create(dto);
      cargarBeneficios();
      hideError();

      JOptionPane.showMessageDialog(
          this,
          "Beneficio asignado correctamente",
          "Éxito",
          JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
      showErrorMessage("Error al asignar beneficio: " + e.getMessage());
      LOGGER.severe("Error al asignar beneficio: " + e.getMessage());
    }
  }

  private void eliminarBeneficio() {
    int selectedRow = tableBeneficiosAsignados.getSelectedRow();
    
    if (selectedRow == -1) {
      showErrorMessage("Debe seleccionar un beneficio asignado para eliminar");
      return;
    }

    int asociacionId = (int) modelAsignados.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de eliminar este beneficio?",
        "Confirmar Eliminación",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        asociacionController.delete(asociacionId);
        cargarBeneficios();
        hideError();

        JOptionPane.showMessageDialog(
            this,
            "Beneficio eliminado correctamente",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);

      } catch (Exception e) {
        showErrorMessage("Error al eliminar beneficio: " + e.getMessage());
        LOGGER.severe("Error al eliminar beneficio: " + e.getMessage());
      }
    }
  }

  private void showErrorMessage(String message) {
    lblError.setText(message);
    lblError.setVisible(true);
  }

  private void hideError() {
    lblError.setVisible(false);
  }
}