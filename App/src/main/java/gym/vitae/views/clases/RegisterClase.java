package gym.vitae.views.clases;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.ClasesController;
import gym.vitae.model.dtos.clase.ClaseCreateDTO;
import gym.vitae.model.enums.NivelClase;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;

/** Formulario para registrar una nueva clase. */
public class RegisterClase extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(RegisterClase.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_DESC_LENGTH = 500;

  private final transient ClasesController controller;

  // Panel de contenido scrollable
  private JPanel contentPanel;
  private JLabel lblError;

  // Campos del formulario
  private JTextField txtNombre;
  private JTextArea txtDescripcion;
  private JSpinner spnDuracion;
  private JSpinner spnCapacidad;
  private JComboBox<NivelClase> cmbNivel;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public RegisterClase(ClasesController controller) {
    this.controller = controller;
    init();
  }

  private void init() {
    setLayout(new BorderLayout());

    // Panel superior con mensaje de error
    lblError = new JLabel();
    lblError.setVisible(false);
    lblError.putClientProperty(
        FlatClientProperties.STYLE, "foreground:#F44336;font:bold;border:0,0,10,0");

    // Panel de contenido con scroll
    contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 0", "[fill]", ""));
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    // Panel de botones fijo en la parte inferior
    JPanel buttonPanel = createButtonPanel();

    // Ensamblar layout
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
    txtNombre = new JTextField();
    txtDescripcion = new JTextArea(3, 20);
    txtDescripcion.setLineWrap(true);
    txtDescripcion.setWrapStyleWord(true);

    // Spinner para duración (15-180, step 5)
    SpinnerNumberModel durationModel = new SpinnerNumberModel(45, 15, 180, 5);
    spnDuracion = new JSpinner(durationModel);

    // Spinner para capacidad (1-50)
    SpinnerNumberModel capacityModel = new SpinnerNumberModel(20, 1, 50, 1);
    spnCapacidad = new JSpinner(capacityModel);

    cmbNivel = new JComboBox<>(NivelClase.values());
    cmbNivel.setSelectedItem(NivelClase.TODOS);

    // Aplicar filtros
    applyLettersAndSpacesFilter(txtNombre);
    applyMaxLengthFilter(txtDescripcion);
  }

  private void applyStyles() {
    txtNombre.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Yoga, Spinning");
    txtDescripcion.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Descripción de la clase (opcional)");
  }

  /** Aplica filtro para permitir solo letras y espacios. */
  private void applyLettersAndSpacesFilter(JTextField textField) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (isValidLettersAndSpaces(string)
                    && (fb.getDocument().getLength() + string.length()
                        <= RegisterClase.MAX_TEXT_LENGTH)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (isValidLettersAndSpaces(text)
                    && (fb.getDocument().getLength() - length + text.length()
                        <= RegisterClase.MAX_TEXT_LENGTH)) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }

              private boolean isValidLettersAndSpaces(String text) {
                return text.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$");
              }
            });
  }

  /** Aplica filtro de longitud máxima. */
  private void applyMaxLengthFilter(JTextComponent textComponent) {
    ((AbstractDocument) textComponent.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (fb.getDocument().getLength() + string.length()
                    <= RegisterClase.MAX_DESC_LENGTH) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (fb.getDocument().getLength() - length + text.length()
                    <= RegisterClase.MAX_DESC_LENGTH) {
                  super.replace(fb, offset, length, text, attrs);
                }
              }
            });
  }

  private void buildForm() {
    createTitle("Información de la Clase");

    contentPanel.add(new JLabel("Nombre de la Clase *"));
    contentPanel.add(txtNombre);

    contentPanel.add(new JLabel("Descripción"));
    JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
    scrollDesc.setPreferredSize(new Dimension(0, 80));
    contentPanel.add(scrollDesc);

    contentPanel.add(new JLabel("Duración (minutos) *"));
    contentPanel.add(spnDuracion);

    contentPanel.add(new JLabel("Capacidad Máxima *"));
    contentPanel.add(spnCapacidad);

    contentPanel.add(new JLabel("Nivel *"));
    contentPanel.add(cmbNivel);

    contentPanel.add(new JLabel(" "), "gapy 10");
    contentPanel.add(new JLabel("* Campos obligatorios"), "gapy 0");
  }

  private void createTitle(String title) {
    JLabel lblTitle = new JLabel(title);
    lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lblTitle, "gapy 10 5");
  }

  public boolean validateForm() {
    hideError();

    try {
      // Validar nombre
      String nombre = txtNombre.getText().trim();
      if (nombre.isEmpty()) {
        showErrorMessage("El nombre de la clase es obligatorio");
        txtNombre.requestFocus();
        return false;
      }

      // Validar duración (debe ser múltiplo de 5)
      int duracion = (Integer) spnDuracion.getValue();
      if (duracion % 5 != 0) {
        showErrorMessage("La duración debe ser múltiplo de 5 minutos");
        spnDuracion.requestFocus();
        return false;
      }

      return true;
    } catch (Exception e) {
      showErrorMessage("Error al validar el formulario: " + e.getMessage());
      return false;
    }
  }

  public boolean saveClase() {
    try {
      ClaseCreateDTO dto = buildDTOFromForm();
      controller.createClase(dto);

      JOptionPane.showMessageDialog(
          this, "Clase registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

      clearForm();
      return true;

    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al guardar la clase: " + e.getMessage());
      showErrorMessage("Error al guardar la clase: " + e.getMessage());
      return false;
    }
  }

  private ClaseCreateDTO buildDTOFromForm() {
    String descripcion = txtDescripcion.getText().trim();
    return new ClaseCreateDTO(
        txtNombre.getText().trim(),
        descripcion.isEmpty() ? null : descripcion,
        (Integer) spnDuracion.getValue(),
        (Integer) spnCapacidad.getValue(),
        (NivelClase) cmbNivel.getSelectedItem());
  }

  private void clearForm() {
    txtNombre.setText("");
    txtDescripcion.setText("");
    spnDuracion.setValue(45);
    spnCapacidad.setValue(20);
    cmbNivel.setSelectedItem(NivelClase.TODOS);
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
