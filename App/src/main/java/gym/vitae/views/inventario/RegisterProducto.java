package gym.vitae.views.inventario;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.IventarioController;
import gym.vitae.model.Categoria;
import gym.vitae.model.Producto;
import gym.vitae.model.Proveedore;
import gym.vitae.views.components.primitives.ButtonOutline;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

public class RegisterProducto extends JPanel {

    private JComboBox<String> comboTipo;
    private JComboBox<ProveedorItem> comboProveedor;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JLabel lblError;
    private JComboBox<String> comboUnidadMedida;

    private ButtonOutline btnGuardar;
    private ButtonOutline btnCancelar;

    private IventarioController controller;

    private static final Logger LOGGER = Logger.getLogger(RegisterEquipo.class.getName());

    public RegisterProducto(IventarioController controller) {
        this.controller = controller;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 15", "[fill]", "[][grow]"));

        JLabel lblTitulo = new JLabel("Registro de Producto");
        lblTitulo.putClientProperty(FlatClientProperties.STYLE, "font: bold +5");
        JSeparator separator = new JSeparator();

        // Combo Tipo
        comboTipo = new JComboBox<>(new String[]{"Alimenticio", "Herramienta"});

        // Combo Proveedor
        comboProveedor = new JComboBox<>();
        //loadProveedores();

        // Código generado automáticamente
        txtCodigo = new JTextField(controller.generarCodigoProducto());
        txtCodigo.setEditable(false);

        // Campos de texto
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();

        comboUnidadMedida = new JComboBox<>(new String[]{"Unidad", "Kg", "Litro", "Caja", "Paquete"});

        lblError = new JLabel("Mensaje de error");
        lblError.setForeground(Color.RED);
        lblError.setVisible(false);
        lblError.putClientProperty(FlatClientProperties.STYLE, "font: bold");

        btnGuardar = new ButtonOutline("Guardar");
        btnCancelar = new ButtonOutline("Cancelar");

        JPanel contentPanel = new JPanel(new MigLayout("wrap 2, fillx, hidemode 3", "[pref!][grow, fill]", ""));
        contentPanel.add(new JLabel("Tipo de producto:"), "align label");
        contentPanel.add(comboTipo, "growx");
        contentPanel.add(new JLabel("Proveedor:"), "align label");
        contentPanel.add(comboProveedor, "growx");
        contentPanel.add(new JLabel("Código:"), "align label");
        contentPanel.add(txtCodigo, "growx");
        contentPanel.add(new JLabel("Nombre:"), "align label");
        contentPanel.add(txtNombre, "growx");
        contentPanel.add(new JLabel("Descripción:"), "align label");
        contentPanel.add(txtDescripcion, "growx");
        contentPanel.add(new JLabel("Precio Unitario:"), "align label");
        contentPanel.add(txtPrecio, "growx");
        contentPanel.add(new JLabel("Stock:"), "align label");
        contentPanel.add(txtStock, "growx");
        contentPanel.add(new JLabel("Unidad de medida:"), "align label");
        contentPanel.add(comboUnidadMedida, "growx");
        contentPanel.add(lblError, "span 2, growx, gapy 10");

        JPanel buttonPanel = new JPanel(new MigLayout("flowx, alignx right, gap 10", "[fill]"));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);

        add(lblTitulo, "wrap, growx");
        add(separator, "wrap, growx, gapy 5 10");
        add(contentPanel, "wrap, grow");
        add(buttonPanel, "alignx right, growx");
    }


    public ButtonOutline getBtnGuardar() {
        return btnGuardar;
    }

    public boolean saveBeneficio() {
        return true;
    }

    public ButtonOutline getBtnCancelar() {
        return btnCancelar;
    }

    // Clase interna para combo de proveedores
    private static class ProveedorItem {
        private Integer id;
        private String nombre;

        public ProveedorItem(Integer id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}
