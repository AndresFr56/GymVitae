package gym.vitae.views.components.tables;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class TableHeaderAlignment implements TableCellRenderer {

  private final TableCellRenderer headerDelegate;
  private final TableCellRenderer cellDelegate;

  public TableHeaderAlignment(JTable table) {
    this.headerDelegate = table.getTableHeader().getDefaultRenderer();
    this.cellDelegate = table.getDefaultRenderer(Object.class);
    table.setDefaultRenderer(
        Object.class,
        (jtable, o, bln, bln1, row, column) -> {
          JLabel label =
              (JLabel)
                  cellDelegate.getTableCellRendererComponent(jtable, o, bln, bln1, row, column);
          label.setHorizontalAlignment(getAlignment());
          return label;
        });
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    JLabel label =
        (JLabel)
            headerDelegate.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
    label.setHorizontalAlignment(getAlignment());
    return label;
  }

  protected int getAlignment() {
    return SwingConstants.CENTER;
  }
}
