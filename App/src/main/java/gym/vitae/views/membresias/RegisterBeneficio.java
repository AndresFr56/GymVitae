package gym.vitae.views.membresias;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.BeneficiosController;
import gym.vitae.model.dtos.membresias.BeneficioCreateDTO;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class RegisterBeneficio extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(RegisterBeneficio.class.getName());

    private final BeneficiosController controller;

    private JPanel contentPanel;
    private JLabel lblError;
    private JLabel lblTitulo;
    private JSeparator separator;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JCheckBox chkActivo;
    private ButtonOutline btnGuardar;
    private ButtonOutline btnCancelar;

    public RegisterBeneficio(BeneficiosController controller) {
        this.controller = controller;
        init();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx, insets 15", "[fill]", "[][grow]"));

        lblTitulo = new JLabel("Registro de Nuevo Beneficio");
        lblTitulo.putClientProperty(FlatClientProperties.STYLE, "font: bold +5");
        separator = new JSeparator();

        txtNombre = new JTextField();
        txtNombre.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Acceso a piscina");
        
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Breve descripción del beneficio");
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        
        chkActivo = new JCheckBox("Activo", true);

        lblError = new JLabel("Mensaje de Error");
        lblError.putClientProperty(FlatClientProperties.STYLE, "font: bold");
        lblError.setVisible(false);
        lblError.setForeground(Color.RED);

        btnGuardar = new ButtonOutline("Guardar");
        btnCancelar = new ButtonOutline("Cancelar");

        contentPanel = new JPanel(new MigLayout("wrap 2, fillx, hidemode 3", "[pref!][grow, fill]", ""));
        contentPanel.add(new JLabel("Nombre:"), "align label");
        contentPanel.add(txtNombre, "growx");
        contentPanel.add(new JLabel("Descripción:"), "align label");
        contentPanel.add(scrollDescripcion, "growx, height 80");
        contentPanel.add(chkActivo, "span 2, gapy 10");
        contentPanel.add(lblError, "span 2, growx, gapy 10");
        
        JPanel buttonPanel = new JPanel(new MigLayout("flowx, alignx right, gap 10", "[fill]"));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);
        
        add(lblTitulo, "wrap, growx");
        add(separator, "wrap, growx, gapy 5 10");
        add(contentPanel, "wrap, grow");
        add(buttonPanel, "alignx right, growx");
    }

    public boolean saveBeneficio() {
        try {
            hideError();
            
            if (txtNombre.getText().trim().isEmpty()) {
                showErrorMessage("El nombre del beneficio es obligatorio");
                return false;
            }
            
            BeneficioCreateDTO dto = new BeneficioCreateDTO();
            dto.setNombre(txtNombre.getText().trim());
            dto.setDescripcion(txtDescripcion.getText().trim().isEmpty() ? null : txtDescripcion.getText().trim());
            dto.setActivo(chkActivo.isSelected());

            controller.createBeneficio(dto);

            JOptionPane.showMessageDialog(
                this,
                "Beneficio registrado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            return true;

        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.severe("Error al guardar el beneficio: " + e.getMessage());
            showErrorMessage("Error al guardar: " + e.getMessage());
            return false;
        }
    }

    private void clearForm() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        chkActivo.setSelected(true);
        hideError();
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