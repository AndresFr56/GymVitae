package gym.vitae.views.auth;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.AuthController;
import gym.vitae.core.SessionManager;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.common.ViewManager;
import gym.vitae.views.components.Menu;
import gym.vitae.views.components.primitives.ButtonOutline;
import gym.vitae.views.components.primitives.DropShadowBorder;
import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

@Metadata(
    name = "Vista de inicio de session",
    description = "Vista para que el usuario inicie sesion en la aplicacion")
public class ViewLogin extends ViewContainer {

  // Integración de autenticación
  private final AuthController authController = new AuthController();
  private final SessionManager sessionManager = SessionManager.getInstance();
  private JPanel loginPanel;
  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JCheckBox chRememberMe;
  private JButton cmdLogin;

  @Override
  protected void init() {

    setLayout(new MigLayout("al center center"));

    loginPanel =
        new JPanel(new BorderLayout()) {
          @Override
          public void updateUI() {
            super.updateUI();
            applyShadowBorder(this);
          }
        };

    loginPanel.setOpaque(false);

    JPanel loginContent = new JPanel(new MigLayout("fillx,wrap,insets 35 35 25 35", "[fill,300]"));
    loginContent.putClientProperty(FlatClientProperties.STYLE, "background:null;");

    JLabel lbTitle = new JLabel("Bienvenido de Vuelta !");
    JLabel lbDescription = new JLabel("Porfavor ingresa tus datos para continuar.");
    lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +12;");

    loginContent.add(lbTitle);
    loginContent.add(lbDescription);

    txtUsername = new JTextField();
    txtPassword = new JPasswordField();
    chRememberMe = new JCheckBox("Remember Me");
    cmdLogin =
        new JButton("Iniciar Sesión") {
          @Override
          public boolean isDefaultButton() {
            return true;
          }
        };

    // estilo
    txtUsername.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese su nombre de usuario");
    txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese su contraseña");

    txtUsername.putClientProperty(FlatClientProperties.STYLE, "margin:4,10,4,10;arc:12;");
    txtPassword.putClientProperty(
        FlatClientProperties.STYLE, "margin:4,10,4,10;arc:12;showRevealButton:true;");
    cmdLogin.putClientProperty(FlatClientProperties.STYLE, "margin:4,10,4,10;arc:12;");

    loginContent.add(new JLabel("Username"), "gapy 25");
    loginContent.add(txtUsername);

    loginContent.add(new JLabel("Password"), "gapy 10");
    loginContent.add(txtPassword);
    loginContent.add(chRememberMe);
    loginContent.add(cmdLogin, "gapy 20");
    loginContent.add(createInfo());

    loginPanel.add(loginContent);
    add(loginPanel);

    // listener mínimo; delega a AuthController y maneja SessionManager
    cmdLogin.addActionListener(
        e -> {
          String user = txtUsername.getText();
          String pass = String.valueOf(txtPassword.getPassword());

          if (user == null || user.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "El correo o nombre de usuario es obligatorio",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
          }
          if (pass == null || pass.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "La cédula es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
            return;
          }

          try {

            boolean authenticated = authController.login(user.trim(), pass.trim());

            if (authenticated && sessionManager.isAuthenticated()) {
              String nombre = sessionManager.getNombreCompleto();

              JOptionPane.showMessageDialog(
                  this,
                  "Bienvenido" + (nombre != null ? " " + nombre : "") + "!",
                  "Acceso concedido",
                  JOptionPane.INFORMATION_MESSAGE);

              txtUsername.setText("");
              txtPassword.setText("");
              chRememberMe.setSelected(false);

              SwingUtilities.invokeLater(() -> txtUsername.requestFocusInWindow());
              Menu.getInstance().seUserInfo(sessionManager.getEmpleadoActual());
              ViewManager.login();

            } else {
              JOptionPane.showMessageDialog(
                  this, "Credenciales inválidas", "Error", JOptionPane.ERROR_MESSAGE);
            }
          } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error inesperado durante la autenticación",
                "Error",
                JOptionPane.ERROR_MESSAGE);
          }
        });
  }

  @Override
  protected void load() {
    // cada vez que la vista se muestra: limpiar campos y dar foco
    if (txtUsername != null) {
      txtUsername.setText("");
      txtPassword.setText("");
      chRememberMe.setSelected(false);
      SwingUtilities.invokeLater(() -> txtUsername.requestFocusInWindow());
    }
  }

  @Override
  protected void open() {
    // acciones/adaptaciones al mostrarse (animaciones, default button)
    if (cmdLogin != null) {
      SwingUtilities.invokeLater(
          () -> {
            JRootPane rp = SwingUtilities.getRootPane(this);
            if (rp != null) rp.setDefaultButton(cmdLogin);
          });
    }
  }

  @Override
  protected void refresh() {
    if (loginPanel != null) loginPanel.updateUI();
  }

  @Override
  public boolean checkStats() {
    return this.loginPanel != null;
  }

  private JPanel createInfo() {
    JPanel panelInfo = new JPanel(new MigLayout("wrap,al center", "[center]"));
    panelInfo.putClientProperty(FlatClientProperties.STYLE, "background:null;");

    panelInfo.add(new JLabel("No puedes acceder a tu cuenta?"));
    panelInfo.add(new JLabel("Contactanos a Gymvitae Support"), "split 2");
    ButtonOutline lbLink = new ButtonOutline("GymVitae@gmail.com");

    panelInfo.add(lbLink);

    lbLink.addOnClick(
        e -> {
          // acción opcional
        });
    return panelInfo;
  }

  private void applyShadowBorder(JPanel panel) {
    if (panel != null) {
      panel.setBorder(new DropShadowBorder(new Insets(5, 8, 12, 8), 1, 25));
    }
  }
}
