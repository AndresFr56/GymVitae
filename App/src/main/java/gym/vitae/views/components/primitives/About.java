package gym.vitae.views.components.primitives;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.LoggingFacade;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import net.miginfocom.swing.MigLayout;

public class About extends JPanel {

  public About() {
    init();
  }

  private void init() {
    setLayout(new MigLayout("fillx,wrap,insets 5 30 5 30,width 400", "[fill,330::]", ""));

    JTextPane title = createText("Gym Vitae - Sistema de Gestión de Gimnasios Open Source");
    title.putClientProperty(FlatClientProperties.STYLE, "font:bold +5");

    JTextPane description = createText("");
    description.setContentType("text/html");
    description.setText(getDescriptionText());
    description.addHyperlinkListener(
        e -> {
          if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            showUrl(e.getURL());
          }
        });

    add(title);
    add(description);
    add(createSystemInformation());
  }

  private JTextPane createText(String text) {
    JTextPane textPane = new JTextPane();
    textPane.setBorder(BorderFactory.createEmptyBorder());
    textPane.setText(text);
    textPane.setEditable(false);
    return textPane;
  }

  private String getDescriptionText() {
    return "Este es un proyecto de código abierto para la gestión de gimnasios, "
        + "Esta construido usando  FlatLaf Look and Feel and MigLayout library.<br>"
        + "Este desarrollo no se pudo realizar sin los componentes de DJ-Raven <a href=\"https://github.com/DJ-Raven/swing-modal-dialog/\">GitHub Project.</a>"
        + "El proyecto esta disponible para todos en <a href=\"https://github.com/DJ-Raven/swing-modal-dialog/\">Savecoders.</a>";
  }

  private String getSystemInformationText() {
    return "<b>Demo Version: </b>%s<br/>" + "<b>Java: </b>%s<br/>" + "<b>System: </b>%s<br/>";
  }

  private JComponent createSystemInformation() {
    JPanel panel = new JPanel(new MigLayout("wrap"));
    panel.setBorder(new TitledBorder("System Information"));
    JTextPane textPane = createText("");
    textPane.setContentType("text/html");
    String version = "1.0.0";
    String java = System.getProperty("java.vendor") + " - v" + System.getProperty("java.version");
    String system =
        System.getProperty("os.name")
            + " "
            + System.getProperty("os.arch")
            + " - v"
            + System.getProperty("os.version");
    String text = String.format(getSystemInformationText(), version, java, system);
    textPane.setText(text);
    panel.add(textPane);
    return panel;
  }

  private void showUrl(URL url) {
    if (Desktop.isDesktopSupported()) {
      Desktop desktop = Desktop.getDesktop();
      if (desktop.isSupported(Desktop.Action.BROWSE)) {
        try {
          desktop.browse(url.toURI());
        } catch (IOException | URISyntaxException e) {
          LoggingFacade.INSTANCE.logSevere("Error browse url", e);
        }
      }
    }
  }
}
