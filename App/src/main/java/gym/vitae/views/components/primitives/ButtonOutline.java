package gym.vitae.views.components.primitives;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.*;
import jdk.jfr.Event;

public class ButtonOutline extends JLabel {

  public ButtonOutline(String text) {
    super("<html><a href=\"#\">" + text + "</a></html>");
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    setFocusable(true);
  }

  public ButtonOutline() {
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    setFocusable(true);
  }

  public void addOnClick(Consumer<Event> event) {
    addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
              requestFocus();
              event.accept(null);
            }
          }
        });
  }
}
