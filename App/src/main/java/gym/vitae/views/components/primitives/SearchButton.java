package gym.vitae.views.components.primitives;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class SearchButton extends JButton {

  public SearchButton() {
    super("Busqueda Rapida...", new FlatSVGIcon(Icons.SEARCH.toString(), 0.4f));
    init();
  }

  private void init() {
    setLayout(new MigLayout("insets 0,al trailing,filly", "", "[center]"));
    setHorizontalAlignment(SwingConstants.LEADING);
    putClientProperty(
        FlatClientProperties.STYLE,
        "arc:10"
            + "margin:5,7,5,10;"
            + "arc:10;"
            + "borderWidth:0;"
            + "focusWidth:0;"
            + "innerFocusWidth:0;"
            + "[light]background:shade($Panel.background,10%);"
            + "[dark]background:tint($Panel.background,10%);"
            + "[light]foreground:tint($Button.foreground,40%);"
            + "[dark]foreground:shade($Button.foreground,30%);");
    JLabel label = new JLabel("Ctrl F");
    label.putClientProperty(
        FlatClientProperties.STYLE,
        "[light]foreground:tint($Button.foreground,40%);"
            + "[dark]foreground:shade($Button.foreground,30%);");
    add(label);
  }
}
