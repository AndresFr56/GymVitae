package gym.vitae.views.components.tables;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import net.miginfocom.swing.MigLayout;

public class TableProfileCellRenderer extends JPanel implements TableCellRenderer {

  transient TableCellRenderer delegateComponent;
  JLabel labelProfile;
  JLabel labelName;
  JLabel labelLocation;

  public TableProfileCellRenderer(JTable table) {
    delegateComponent = table.getDefaultRenderer(Object.class);
    init();
  }

  private void init() {
    setLayout(new MigLayout("ay center,insets 7 0 7 0", "[]10[]", "[sg h,bottom][sg h,top]"));
    labelProfile = new JLabel();
    labelName = new JLabel();
    labelLocation = new JLabel();
    labelLocation.putClientProperty(
        FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");

    add(labelProfile, "span 1 2,w 45::,h 45::,grow 0");
    add(labelName, "cell 1 0");
    add(labelLocation, "cell 1 1");
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    return delegateComponent.getTableCellRendererComponent(
        table, value, isSelected, hasFocus, row, column);
  }
}
