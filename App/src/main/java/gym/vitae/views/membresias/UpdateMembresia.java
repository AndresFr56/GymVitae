package gym.vitae.views.membresias;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.MembresiasController;
import gym.vitae.model.dtos.membresias.MembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaUpdateDTO;
import gym.vitae.model.enums.EstadoMembresia;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

/** Formulario para actualizar una membresía existente. */
public class UpdateMembresia extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(UpdateMembresia.class.getName());
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final MembresiasController controller;
  private final MembresiaDetalleDTO membresiaDetalle;

  private JPanel contentPanel;
  private JLabel lblError;
  private JTextField txtCliente;
  private JTextField txtTipoMembresia;
  private JTextField txtFechaInicio;
  private JTextField txtPrecioPagado;
  private JTextField txtFactura;

  private DatePicker dateFechaFin;
  private JComboBox<EstadoMembresia> cmbEstado;
  private JTextArea txtObservaciones;

  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public UpdateMembresia(MembresiasController controller, MembresiaDetalleDTO membresiaDetalle) {
    this.controller = controller;
    this.membresiaDetalle = membresiaDetalle;
    init();
  }

  private void init() {
    setLayout(new BorderLayout());

    lblError = new JLabel();
    lblError.setVisible(false);
    lblError.putClientProperty(
        FlatClientProperties.STYLE, "foreground:#F44336;font:bold;border:0,0,10,0");

    contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 0", "[fill]", ""));
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    JPanel buttonPanel = createButtonPanel();

    JPanel topPanel = new JPanel(new MigLayout("fillx,wrap,insets 10 20 0 20", "[fill]"));
    topPanel.add(lblError);

    add(topPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    initializeComponents();
    applyStyles();
    buildForm();
    loadMembresiaData();
  }

  private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new MigLayout("insets 15 20 20 20", "[grow][120][120]"));
    panel.putClientProperty(
        FlatClientProperties.STYLE,
        "background:$Panel.background;border:1,0,0,0,$Component.borderColor");

    btnCancelar = new ButtonOutline("Cancelar");
    btnCancelar.putClientProperty(FlatClientProperties.STYLE, "font:bold +1");

    btnGuardar = new ButtonOutline("Guardar");
    btnGuardar.putClientProperty(FlatClientProperties.STYLE, "foreground:#4CAF50;font:bold +1");

    panel.add(new JLabel(), "grow");
    panel.add(btnCancelar, "center");
    panel.add(btnGuardar, "center");

    return panel;
  }

  private void initializeComponents() {
    txtCliente = new JTextField();
    txtCliente.setEditable(false);

    txtTipoMembresia = new JTextField();
    txtTipoMembresia.setEditable(false);

    txtFechaInicio = new JTextField();
    txtFechaInicio.setEditable(false);

    txtPrecioPagado = new JTextField();
    txtPrecioPagado.setEditable(false);

    txtFactura = new JTextField();
    txtFactura.setEditable(false);

    dateFechaFin = new DatePicker();
    dateFechaFin.setEditor(new JFormattedTextField());

    cmbEstado = new JComboBox<>(EstadoMembresia.values());

    txtObservaciones = new JTextArea(3, 20);
    txtObservaciones.setLineWrap(true);
    txtObservaciones.setWrapStyleWord(true);
  }

  private void applyStyles() {
    txtObservaciones.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Observaciones adicionales (opcional)");
  }

  private void buildForm() {
    createTitle("Información de la Membresía");

    contentPanel.add(new JLabel("Cliente"));
    contentPanel.add(txtCliente);

    contentPanel.add(new JLabel("Tipo de Membresía"));
    contentPanel.add(txtTipoMembresia);

    contentPanel.add(new JLabel("Fecha de Inicio"));
    contentPanel.add(txtFechaInicio);

    contentPanel.add(new JLabel("Fecha de Fin *"));
    contentPanel.add(dateFechaFin);

    contentPanel.add(new JLabel("Precio Pagado"));
    contentPanel.add(txtPrecioPagado);

    contentPanel.add(new JLabel("Factura"));
    contentPanel.add(txtFactura);

    createTitle("Estado y Observaciones");

    contentPanel.add(new JLabel("Estado *"));
    contentPanel.add(cmbEstado);

    contentPanel.add(new JLabel("Observaciones"));
    JScrollPane scrollObs = new JScrollPane(txtObservaciones);
    scrollObs.setPreferredSize(new Dimension(0, 80));
    contentPanel.add(scrollObs);

    contentPanel.add(new JLabel(" "), "gapy 10");
    contentPanel.add(new JLabel("* Campos obligatorios"), "gapy 0");
  }

  private void createTitle(String title) {
    JLabel lblTitle = new JLabel(title);
    lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lblTitle, "gapy 10 5");
  }

  private void loadMembresiaData() {
    if (membresiaDetalle == null) {
      throw new IllegalArgumentException("La membresía no puede ser nula");
    }

    txtCliente.setText(
        membresiaDetalle.getClienteNombre() + " - " + membresiaDetalle.getClienteDocumento());
    txtTipoMembresia.setText(membresiaDetalle.getTipoMembresiaNombre());
    txtFechaInicio.setText(membresiaDetalle.getFechaInicio().format(DATE_FORMATTER));
    dateFechaFin.setSelectedDate(membresiaDetalle.getFechaFin());
    txtPrecioPagado.setText(String.format("$%.2f", membresiaDetalle.getPrecioPagado()));
    txtFactura.setText(membresiaDetalle.getFacturaNumero());

    cmbEstado.setSelectedItem(membresiaDetalle.getEstado());

    txtObservaciones.setText(
        membresiaDetalle.getObservaciones() != null ? membresiaDetalle.getObservaciones() : "");

    hideError();
  }

  public boolean validateForm() {
    hideError();

    try {
      if (dateFechaFin.getSelectedDate() == null) {
        showErrorMessage("La fecha de fin es obligatoria");
        return false;
      }

      if (cmbEstado.getSelectedItem() == null) {
        showErrorMessage("El estado es obligatorio");
        cmbEstado.requestFocus();
        return false;
      }

      if (dateFechaFin.getSelectedDate().isBefore(membresiaDetalle.getFechaInicio())) {
        showErrorMessage("La fecha de fin no puede ser anterior a la fecha de inicio");
        return false;
      }

      return true;
    } catch (Exception e) {
      showErrorMessage("Error al validar el formulario: " + e.getMessage());
      return false;
    }
  }

  public boolean updateMembresia() {
    if (membresiaDetalle == null) {
      showErrorMessage("No hay membresía cargada para actualizar");
      return false;
    }

    try {
      MembresiaUpdateDTO updateDTO =
          new MembresiaUpdateDTO(
              (EstadoMembresia) cmbEstado.getSelectedItem(),
              dateFechaFin.getSelectedDate(),
              txtObservaciones.getText().trim().isEmpty()
                  ? null
                  : txtObservaciones.getText().trim());

      controller.updateMembresia(membresiaDetalle.getId(), updateDTO);

      JOptionPane.showMessageDialog(
          this, "Membresía actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

      return true;

    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al actualizar la membresía: " + e.getMessage());
      showErrorMessage("Error al actualizar la membresía: " + e.getMessage());
      return false;
    }
  }

  private void showErrorMessage(String message) {
    lblError.setText(message);
    lblError.setVisible(true);
  }

  private void hideError() {
    lblError.setVisible(false);
  }

  public ButtonOutline getBtnGuardar() {
    return btnGuardar;
  }

  public ButtonOutline getBtnCancelar() {
    return btnCancelar;
  }
}
