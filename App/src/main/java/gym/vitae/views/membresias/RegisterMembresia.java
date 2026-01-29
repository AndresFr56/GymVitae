package gym.vitae.views.membresias;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.ClienteController;
import gym.vitae.controller.MembresiasController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.model.dtos.cliente.ClienteListadoDTO;
import gym.vitae.model.dtos.membresias.MembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

public class RegisterMembresia extends JPanel {

    private final MembresiasController controller;
    private final TiposMembresiaController tipoController;
    private final ClienteController clienteController;

    private JPanel contentPanel;
    private JComboBox<ClienteListadoDTO> cmbCliente;
    private JComboBox<TipoMembresiaListadoDTO> cmbTipoMembresia;

    private DatePicker dateFechaInicio, dateFechaFin;
    private JTextField txtPrecioPagado;
    private JTextArea txtObservaciones;
    private JLabel errCliente, errTipo, errFecha;
    private ButtonOutline btnGuardar, btnCancelar;

    public RegisterMembresia(MembresiasController controller, TiposMembresiaController tipoController, ClienteController clienteController) {
        this.controller = controller;
        this.tipoController = tipoController;
        this.clienteController = clienteController;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[fill]", "[]5[]0[]10[]5[]0[]10[]5[]10[]5[]0[]10[]5[]10[]5[]push"));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        initializeComponents();
        buildForm();

        dateFechaInicio.setSelectedDate(LocalDate.now());

        add(scrollPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private void initializeComponents() {
        var clientes = clienteController.getClientes().stream()
                .filter(c -> c.estado().name().equals("ACTIVO"))
                .toArray(ClienteListadoDTO[]::new);

        cmbCliente = new JComboBox<>(clientes);
        cmbCliente.setSelectedIndex(-1);
        String placeHolderCliente = "Seleccione un cliente...";
        cmbCliente.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeHolderCliente);

        cmbCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ClienteListadoDTO c) {
                    setText(c.nombreCompleto());
                } else if (index == -1) {
                    setText(placeHolderCliente);
                    setForeground(UIManager.getColor("TextField.placeholderForeground"));
                }
                return this;
            }
        });


        var tipos = tipoController.getTipos().stream()
                .filter(TipoMembresiaListadoDTO::getActivo)
                .toArray(TipoMembresiaListadoDTO[]::new);

        cmbTipoMembresia = new JComboBox<>(tipos);
        cmbTipoMembresia.setSelectedIndex(-1);
        String placeHolderPlan = "Seleccione un plan de membresia...";
        cmbTipoMembresia.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeHolderPlan);

        cmbTipoMembresia.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoMembresiaListadoDTO t) {
                    setText(t.getNombre());
                } else if (index == -1) {
                    setText(placeHolderPlan);
                    setForeground(UIManager.getColor("TextField.placeholderForeground"));
                }
                return this;
            }
        });

        cmbTipoMembresia.addActionListener(e -> {
            actualizarPrecio();
            calcularFechaFin();
        });

        dateFechaInicio = new DatePicker();
        dateFechaInicio.setEditor(null);
        dateFechaInicio.addDateSelectionListener(e -> calcularFechaFin());

        dateFechaFin = new DatePicker();
        dateFechaFin.setEditor(null);

        txtPrecioPagado = new JTextField();
        txtPrecioPagado.setEditable(false);
        txtPrecioPagado.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "$ 0.00");

        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Escriba aquí notas adicionales o condiciones especiales...");

        errCliente = createErrorLabel();
        errTipo = createErrorLabel();
        errFecha = createErrorLabel();
    }

    private void buildForm() {
        contentPanel.add(new JLabel("Cliente *"), "gapy 5 0");
        contentPanel.add(cmbCliente);
        contentPanel.add(errCliente, "hidemode 3");

        contentPanel.add(new JLabel("Tipo de Membresía *"), "gapy 5 0");
        contentPanel.add(cmbTipoMembresia);
        contentPanel.add(errTipo, "hidemode 3");

        contentPanel.add(new JLabel("Fecha de Inicio *"), "gapy 10 0");
        contentPanel.add(dateFechaInicio);

        contentPanel.add(new JLabel("Fecha de Fin *"), "gapy 10 0");
        contentPanel.add(dateFechaFin);
        contentPanel.add(errFecha, "hidemode 3");

        contentPanel.add(new JLabel("Precio Pagado"), "gapy 10 0");
        contentPanel.add(txtPrecioPagado);

        contentPanel.add(new JLabel("Observaciones"), "gapy 10 0");
        contentPanel.add(new JScrollPane(txtObservaciones), "h 80!");
    }

    private void calcularFechaFin() {
        LocalDate inicio = dateFechaInicio.getSelectedDate();
        TipoMembresiaListadoDTO tipo = (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();
        if (inicio != null && tipo != null) {
            dateFechaFin.setSelectedDate(inicio.plusDays(tipo.getDuracionDias()));
            validateForm();
        }
    }

    private void actualizarPrecio() {
        TipoMembresiaListadoDTO tipo = (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();
        if (tipo != null) {
            txtPrecioPagado.setText(tipo.getCosto().toString());
        }
    }

    public boolean validateForm() {
        boolean valid = true;
        errCliente.setVisible(false);
        errTipo.setVisible(false);
        errFecha.setVisible(false);

        if (cmbCliente.getSelectedItem() == null) {
            errCliente.setText("Debe seleccionar un cliente");
            errCliente.setVisible(true);
            valid = false;
        }
        if (cmbTipoMembresia.getSelectedItem() == null) {
            errTipo.setText("Debe seleccionar un tipo de membresía");
            errTipo.setVisible(true);
            valid = false;
        }

        LocalDate inicio = dateFechaInicio.getSelectedDate();
        LocalDate fin = dateFechaFin.getSelectedDate();
        if (inicio != null && fin != null && fin.isBefore(inicio)) {
            errFecha.setText("La Fecha de Fin no debe ser anterior a la de Fecha de Inicio");
            errFecha.setVisible(true);
            valid = false;
        }

        contentPanel.revalidate();
        return valid;
    }

    public boolean saveMembresia() {
        if (!validateForm()) return false;
        try {
            ClienteListadoDTO cliente = (ClienteListadoDTO) cmbCliente.getSelectedItem();
            TipoMembresiaListadoDTO tipo = (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();

            MembresiaCreateDTO dto = new MembresiaCreateDTO(
                    cliente.id(),
                    tipo.getId(),
                    null,
                    dateFechaInicio.getSelectedDate(),
                    dateFechaFin.getSelectedDate(),
                    new BigDecimal(txtPrecioPagado.getText()),
                    txtObservaciones.getText()
            );

            controller.createMembresia(dto);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private JLabel createErrorLabel() {
        JLabel label = new JLabel();
        label.setForeground(new Color(244, 67, 54));
        label.setFont(label.getFont().deriveFont(11f));
        label.setVisible(false);
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new MigLayout("insets 15 20 20 20", "[grow][120][120]"));
        btnCancelar = new ButtonOutline("Cancelar");
        btnGuardar = new ButtonOutline("Guardar");

        btnGuardar.putClientProperty(FlatClientProperties.STYLE, "foreground:#4CAF50;font:bold +1");

        panel.add(new JLabel(), "grow");
        panel.add(btnCancelar, "center");
        panel.add(btnGuardar, "center");
        return panel;
    }

    public ButtonOutline getBtnGuardar() {
        return btnGuardar;
    }

    public ButtonOutline getBtnCancelar() {
        return btnCancelar;
    }
}