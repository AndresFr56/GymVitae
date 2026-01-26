package gym.vitae.views.membresias;

import gym.vitae.controller.MembresiasController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.model.dtos.membresias.MembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaUpdateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.model.enums.EstadoMembresia;
import gym.vitae.views.components.primitives.ButtonOutline;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateMembresia extends JPanel {

    private final MembresiasController controller;
    private final TiposMembresiaController tipoController;
    private final MembresiaDetalleDTO membresiaDetalle;
    private JPanel contentPanel;
    private JTextField txtCliente, txtFechaInicio;
    private JComboBox<TipoMembresiaListadoDTO> cmbTipoMembresia;
    private JComboBox<EstadoMembresia> cmbEstado;
    private DatePicker dateFechaFin;
    private JTextArea txtObservaciones;
    private JLabel errTipo, errFecha;
    private ButtonOutline btnGuardar, btnCancelar;

    public UpdateMembresia(MembresiasController c, TiposMembresiaController t, MembresiaDetalleDTO d) {
        this.controller = c;
        this.tipoController = t;
        this.membresiaDetalle = d;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[fill]", "[]5[]10[]5[]0[]10[]5[]10[]5[]10[]5[]push"));
        initializeComponents();
        buildForm();
        loadMembresiaData();
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private void initializeComponents() {
        txtCliente = new JTextField();
        txtCliente.setEditable(false);
        txtFechaInicio = new JTextField();
        txtFechaInicio.setEditable(false);

        TipoMembresiaListadoDTO[] tiposActivos = tipoController.getTipos().stream()
                .filter(TipoMembresiaListadoDTO::getActivo)
                .toArray(TipoMembresiaListadoDTO[]::new);

        cmbTipoMembresia = new JComboBox<>(tiposActivos);
        cmbTipoMembresia.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoMembresiaListadoDTO tipo) {
                    setText(tipo.getNombre());
                }
                return this;
            }
        });
        cmbTipoMembresia.addActionListener(e -> calcularFechaFin());

        dateFechaFin = new DatePicker();
        dateFechaFin.setEditor(null);
        dateFechaFin.addDateSelectionListener(e -> validateForm());

        cmbEstado = new JComboBox<>(EstadoMembresia.values());

        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        errTipo = createErrorLabel();
        errFecha = createErrorLabel();
    }

    private void calcularFechaFin() {
        LocalDate inicio = membresiaDetalle.getFechaInicio();
        TipoMembresiaListadoDTO tipo = (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();
        if (inicio != null && tipo != null) {
            dateFechaFin.setSelectedDate(inicio.plusDays(tipo.getDuracionDias()));
        }
        validateForm();
    }

    private void buildForm() {
        contentPanel.add(new JLabel("Cliente"));
        contentPanel.add(txtCliente);

        contentPanel.add(new JLabel("Tipo de Membresía *"), "gapy 5 0");
        contentPanel.add(cmbTipoMembresia);
        contentPanel.add(errTipo, "hidemode 3");

        contentPanel.add(new JLabel("Fecha Inicio"), "gapy 5 0");
        contentPanel.add(txtFechaInicio);

        contentPanel.add(new JLabel("Fecha Fin *"), "gapy 5 0");
        contentPanel.add(dateFechaFin);
        contentPanel.add(errFecha, "hidemode 3");

        contentPanel.add(new JLabel("Estado *"), "gapy 5 0");
        contentPanel.add(cmbEstado);

        contentPanel.add(new JLabel("Observaciones"), "gapy 5 0");
        JScrollPane scrollObs = new JScrollPane(txtObservaciones);
        contentPanel.add(scrollObs, "h 80!");
    }

    private void loadMembresiaData() {
        txtCliente.setText(membresiaDetalle.getClienteNombre());
        txtFechaInicio.setText(membresiaDetalle.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateFechaFin.setSelectedDate(membresiaDetalle.getFechaFin());
        cmbEstado.setSelectedItem(membresiaDetalle.getEstado());
        txtObservaciones.setText(membresiaDetalle.getObservaciones());

        for (int i = 0; i < cmbTipoMembresia.getItemCount(); i++) {
            if (cmbTipoMembresia.getItemAt(i).getId().equals(membresiaDetalle.getTipoMembresiaId())) {
                cmbTipoMembresia.setSelectedIndex(i);
                break;
            }
        }
    }

    public boolean validateForm() {
        boolean v = true;
        errTipo.setVisible(false);
        errFecha.setVisible(false);
        if (cmbTipoMembresia.getSelectedItem() == null) {
            errTipo.setText("Tipo obligatorio");
            errTipo.setVisible(true);
            v = false;
        }
        if (dateFechaFin.getSelectedDate() != null && dateFechaFin.getSelectedDate().isBefore(membresiaDetalle.getFechaInicio())) {
            errFecha.setText("La fecha de fin no puede ser anterior a la de inicio");
            errFecha.setVisible(true);
            v = false;
        }
        return v;
    }

    public boolean updateMembresia() {
        TipoMembresiaListadoDTO t = (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();

        MembresiaUpdateDTO updateDto = new MembresiaUpdateDTO(
                t.getId(),
                (EstadoMembresia) cmbEstado.getSelectedItem(),
                dateFechaFin.getSelectedDate(),
                txtObservaciones.getText(),
                t.getCosto()
        );

        controller.updateMembresia(membresiaDetalle.getId(), updateDto);
        return true;
    }

    private JLabel createErrorLabel() {
        JLabel l = new JLabel();
        l.setForeground(new Color(244, 67, 54));
        l.setFont(l.getFont().deriveFont(11f));
        return l;
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new MigLayout("insets 15", "[grow][120][120]"));
        btnCancelar = new ButtonOutline("Cancelar");
        btnGuardar = new ButtonOutline("Guardar");
        p.add(new JLabel(), "grow");
        p.add(btnCancelar);
        p.add(btnGuardar);
        return p;
    }

    public ButtonOutline getBtnGuardar() {
        return btnGuardar;
    }

    public ButtonOutline getBtnCancelar() {
        return btnCancelar;
    }
}