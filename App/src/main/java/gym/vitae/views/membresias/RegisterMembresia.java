package gym.vitae.views.membresias;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.MembresiasController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.controller.ClienteController;
import gym.vitae.model.dtos.membresias.MembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.model.dtos.cliente.ClienteListadoDTO;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

/**
 * Formulario para registrar una nueva membresía.
 */
public class RegisterMembresia extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(RegisterMembresia.class.getName());

    private final MembresiasController controller;
    private final TiposMembresiaController tipoController;
    private final ClienteController clienteController;


    private JPanel contentPanel;
    private JLabel lblError;

    // Campos del formulario
    private JComboBox<ClienteListadoDTO> cmbCliente;
    private JComboBox<TipoMembresiaListadoDTO> cmbTipoMembresia;
    private DatePicker dateFechaInicio;
    private DatePicker dateFechaFin;
    private JTextField txtPrecioPagado;
    private JTextArea txtObservaciones;

    // Botones
    private ButtonOutline btnGuardar;
    private ButtonOutline btnCancelar;

    public RegisterMembresia(
            MembresiasController controller,
            TiposMembresiaController tipoController,
            ClienteController clienteController) {
        this.controller = controller;
        this.tipoController = tipoController;
        this.clienteController = clienteController;
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
        var clientes = clienteController.getClientes().stream()
                .filter(c -> c.estado().name().equals("ACTIVO"))
                .toArray(ClienteListadoDTO[]::new);
        cmbCliente = new JComboBox<>(clientes);
        cmbCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ClienteListadoDTO cliente) {
                    setText(cliente.nombreCompleto() + " - " + cliente.cedula());
                }
                return this;
            }
        });

        // Carga tipos de membresía activos
        var tipos = tipoController.getTipos().stream()
                .filter(TipoMembresiaListadoDTO::getActivo)
                .toArray(TipoMembresiaListadoDTO[]::new);
        cmbTipoMembresia = new JComboBox<>(tipos);
        cmbTipoMembresia.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoMembresiaListadoDTO tipo) {
                    setText(tipo.getNombre() + " - $" + tipo.getCosto() + " (" + tipo.getDuracionDias() + " días)");
                }
                return this;
            }
        });

        cmbTipoMembresia.addActionListener(e -> calcularFechaFin());

        dateFechaInicio = new DatePicker();
        dateFechaInicio.setEditor(new JFormattedTextField());
        dateFechaInicio.setSelectedDate(LocalDate.now());
        dateFechaInicio.addDateSelectionListener(e -> calcularFechaFin());

        dateFechaFin = new DatePicker();
        dateFechaFin.setEditor(new JFormattedTextField());
        dateFechaFin.setEnabled(false); 

        txtPrecioPagado = new JTextField();
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
    }

    private void applyStyles() {
        txtPrecioPagado.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "0.00");
        txtObservaciones.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT, "Observaciones adicionales (opcional)");
    }

    private void buildForm() {
        createTitle("Información de la Membresía");

        contentPanel.add(new JLabel("Cliente *"));
        contentPanel.add(cmbCliente);

        contentPanel.add(new JLabel("Tipo de Membresía *"));
        contentPanel.add(cmbTipoMembresia);

        contentPanel.add(new JLabel("Fecha de Inicio *"));
        contentPanel.add(dateFechaInicio);

        contentPanel.add(new JLabel("Fecha de Fin"));
        contentPanel.add(dateFechaFin);

        contentPanel.add(new JLabel("Precio Pagado *"));
        contentPanel.add(txtPrecioPagado);
        

        contentPanel.add(new JLabel("Observaciones"));
        JScrollPane scrollObs = new JScrollPane(txtObservaciones);
        scrollObs.setPreferredSize(new Dimension(0, 80));
        contentPanel.add(scrollObs);

        contentPanel.add(new JLabel(" "), "gapy 10");
        contentPanel.add(new JLabel("* Campos obligatorios"), "gapy 0");

        calcularFechaFin();
        actualizarPrecio();
    }

    private void createTitle(String title) {
        JLabel lblTitle = new JLabel(title);
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        contentPanel.add(lblTitle, "gapy 10 5");
    }

    private void calcularFechaFin() {
        LocalDate fechaInicio = dateFechaInicio.getSelectedDate();
        TipoMembresiaListadoDTO tipoSeleccionado =
                (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();

        if (fechaInicio != null && tipoSeleccionado != null) {
            LocalDate fechaFin = fechaInicio.plusDays(tipoSeleccionado.getDuracionDias());
            dateFechaFin.setSelectedDate(fechaFin);
            actualizarPrecio();
        }
    }

    private void actualizarPrecio() {
        TipoMembresiaListadoDTO tipoSeleccionado =
                (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();

        if (tipoSeleccionado != null) {
            txtPrecioPagado.setText(tipoSeleccionado.getCosto().toString());
        }
    }

    public boolean validateForm() {
        hideError();

        try {
            if (cmbCliente.getSelectedItem() == null) {
                showErrorMessage("Debe seleccionar un cliente");
                cmbCliente.requestFocus();
                return false;
            }

            if (cmbTipoMembresia.getSelectedItem() == null) {
                showErrorMessage("Debe seleccionar un tipo de membresía");
                cmbTipoMembresia.requestFocus();
                return false;
            }

            if (dateFechaInicio.getSelectedDate() == null) {
                showErrorMessage("La fecha de inicio es obligatoria");
                return false;
            }

            if (dateFechaFin.getSelectedDate() == null) {
                showErrorMessage("La fecha de fin es obligatoria");
                return false;
            }

            if (txtPrecioPagado.getText().trim().isEmpty()) {
                showErrorMessage("El precio pagado es obligatorio");
                txtPrecioPagado.requestFocus();
                return false;
            }

            try {
                BigDecimal precio = new BigDecimal(txtPrecioPagado.getText().trim());
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    showErrorMessage("El precio debe ser mayor a 0");
                    txtPrecioPagado.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                showErrorMessage("El precio no es válido");
                txtPrecioPagado.requestFocus();
                return false;
            }

            return true;
        } catch (Exception e) {
            showErrorMessage("Error al validar el formulario: " + e.getMessage());
            return false;
        }
    }

    public boolean saveMembresia() {
        try {
            MembresiaCreateDTO dto = buildDTOFromForm();
            controller.createMembresia(dto);

            JOptionPane.showMessageDialog(
                    this,
                    "Membresía registrada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            return true;

        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.severe("Error al guardar la membresía: " + e.getMessage());
            showErrorMessage("Error al guardar la membresía: " + e.getMessage());
            return false;
        }
    }

    private MembresiaCreateDTO buildDTOFromForm() {
        ClienteListadoDTO cliente = (ClienteListadoDTO) cmbCliente.getSelectedItem();
        TipoMembresiaListadoDTO tipo = (TipoMembresiaListadoDTO) cmbTipoMembresia.getSelectedItem();

        return new MembresiaCreateDTO(
                cliente.id(),
                tipo.getId(),
                null, 
                dateFechaInicio.getSelectedDate(),
                dateFechaFin.getSelectedDate(),
                new BigDecimal(txtPrecioPagado.getText().trim()),
                txtObservaciones.getText().trim().isEmpty() ? null : txtObservaciones.getText().trim());
    }

    private void clearForm() {
        cmbCliente.setSelectedIndex(0);
        cmbTipoMembresia.setSelectedIndex(0);
        dateFechaInicio.setSelectedDate(LocalDate.now());
        txtPrecioPagado.setText("");
        txtObservaciones.setText("");
        hideError();
        calcularFechaFin();
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