package gym.vitae.views.clases;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.controller.ClasesController;
import gym.vitae.model.Clase;
import gym.vitae.model.enums.NivelClase;
import gym.vitae.views.components.primitives.ButtonOutline;
import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;

/** Formulario para actualizar una clase existente. */
public class UpdateClase extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(UpdateClase.class.getName());
  private static final int MAX_TEXT_LENGTH = 100;
  private static final int MAX_DESC_LENGTH = 500;

  private final ClasesController controller;
  private final Clase clase;

  // Panel de contenido scrollable
  private JPanel contentPanel;
  private JLabel lblError;

  // Campos del formulario
  private JTextField txtNombre;
  private JTextArea txtDescripcion;
  private JSpinner spnDuracion;
  private JSpinner spnCapacidad;
  private JComboBox<NivelClase> cmbNivel;
  private JCheckBox chkActiva;

  // Botones
  private ButtonOutline btnGuardar;
  private ButtonOutline btnCancelar;

  public UpdateClase(ClasesController controller, Clase clase) {
    this.controller = controller;
    this.clase = clase;
    init();
  }

  private void init() {
    setLayout(new BorderLayout());
    setOpaque(false);

    // Panel principal con scroll
    contentPanel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[fill]", "[]10[]"));
    contentPanel.setOpaque(false);

    // Label para errores
    lblError = new JLabel();
    lblError.setForeground(new Color(220, 53, 69));
    lblError.setVisible(false);
    contentPanel.add(lblError, "growx");

    initializeComponents();
    applyStyles();
    buildForm();
    loadClaseData();

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

    add(scrollPane, BorderLayout.CENTER);
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
    chkActiva = new JCheckBox("Clase Activa");

    btnGuardar = new ButtonOutline();
    btnCancelar = new ButtonOutline();

    // Aplicar filtros
    applyLettersAndSpacesFilter(txtNombre, MAX_TEXT_LENGTH);
    applyMaxLengthFilter(txtDescripcion);
  }

  private void applyStyles() {
    txtNombre.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Yoga, Spinning");
    txtDescripcion.putClientProperty(
        FlatClientProperties.PLACEHOLDER_TEXT, "Descripción de la clase (opcional)");

    txtNombre.putClientProperty(FlatClientProperties.STYLE, "arc:8");
    txtDescripcion.putClientProperty(FlatClientProperties.STYLE, "arc:8");
  }

  /** Aplica filtro para permitir solo letras y espacios. */
  private void applyLettersAndSpacesFilter(JTextField textField, int maxLength) {
    ((AbstractDocument) textField.getDocument())
        .setDocumentFilter(
            new DocumentFilter() {
              @Override
              public void insertString(
                  FilterBypass fb, int offset, String string, AttributeSet attr)
                  throws BadLocationException {
                if (string == null) return;
                if (isValidLettersAndSpaces(string)
                    && (fb.getDocument().getLength() + string.length() <= maxLength)) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (isValidLettersAndSpaces(text)
                    && (fb.getDocument().getLength() - length + text.length() <= maxLength)) {
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
                if (fb.getDocument().getLength() + string.length() <= UpdateClase.MAX_DESC_LENGTH) {
                  super.insertString(fb, offset, string, attr);
                }
              }

              @Override
              public void replace(
                  FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                  throws BadLocationException {
                if (text == null) return;
                if (fb.getDocument().getLength() - length + text.length()
                    <= UpdateClase.MAX_DESC_LENGTH) {
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

    contentPanel.add(new JLabel("Estado"));
    contentPanel.add(chkActiva);

    contentPanel.add(new JLabel(" "), "gapy 10");
    contentPanel.add(new JLabel("* Campos obligatorios"), "gapy 0");
  }

  private void createTitle(String title) {
    JLabel lblTitle = new JLabel(title);
    lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    contentPanel.add(lblTitle, "gapy 10 5");
  }

  private void loadClaseData() {
    txtNombre.setText(clase.getNombre());
    txtDescripcion.setText(clase.getDescripcion() != null ? clase.getDescripcion() : "");
    spnDuracion.setValue(clase.getDuracionMinutos());
    spnCapacidad.setValue(clase.getCapacidadMaxima());
    cmbNivel.setSelectedItem(clase.getNivel());
    chkActiva.setSelected(clase.getActiva());
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

  public boolean updateClase() {
    try {
      // Actualizar los datos de la clase
      clase.setNombre(txtNombre.getText().trim());

      String descripcion = txtDescripcion.getText().trim();
      clase.setDescripcion(descripcion.isEmpty() ? null : descripcion);

      clase.setDuracionMinutos((Integer) spnDuracion.getValue());
      clase.setCapacidadMaxima((Integer) spnCapacidad.getValue());
      clase.setNivel((NivelClase) cmbNivel.getSelectedItem());
      clase.setActiva(chkActiva.isSelected());

      controller.updateClase(clase);

      JOptionPane.showMessageDialog(
          this, "Clase actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

      return true;

    } catch (IllegalArgumentException e) {
      showErrorMessage(e.getMessage());
      return false;
    } catch (Exception e) {
      LOGGER.severe("Error al actualizar la clase: " + e.getMessage());
      showErrorMessage("Error al actualizar la clase: " + e.getMessage());
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
