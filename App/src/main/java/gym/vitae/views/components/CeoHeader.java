package gym.vitae.views.components;

import com.formdev.flatlaf.FlatClientProperties;
import gym.vitae.views.common.Metadata;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public record CeoHeader(Metadata metadata) {

  public CeoHeader(Metadata metadata) {
    this.metadata = metadata;
  }

  public static JPanel createHeaderPanel(Metadata metadata) {
    JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
    JLabel lbTitle = new JLabel(metadata.name());
    JLabel text = new JLabel();
    text.setText(metadata.description());
    text.setBorder(BorderFactory.createEmptyBorder());
    lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +" + 4);
    text.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground");
    panel.add(lbTitle);
    panel.add(text, "width 500");
    return panel;
  }
}
