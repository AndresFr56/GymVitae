package gym.vitae.views.inventario;

import gym.vitae.views.components.primitives.ButtonOutline;
import gym.vitae.views.membresias.RegisterBeneficio;

import javax.swing.*;
import java.util.logging.Logger;

public class RegisterEquipo extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(RegisterEquipo.class.getName());

    private JPanel contentPanel;
    private JLabel lblError;
    private JLabel lblTitulo;
    private JSeparator separator;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private ButtonOutline btnGuardar;
    private ButtonOutline btnCancelar;




    public ButtonOutline getBtnGuardar() {
        return btnGuardar;
    }

    public boolean saveBeneficio() {
        return true;
    }

    public ButtonOutline getBtnCancelar() {
        return btnCancelar;
    }
}
