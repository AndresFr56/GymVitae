package gym.vitae.views.components.primitives;

import com.formdev.flatlaf.icons.FlatAbstractIcon;
import java.awt.*;
import raven.extras.AvatarIcon;

public class Avatar extends FlatAbstractIcon {

  private final AvatarIcon.BorderColor borderColor;

  public Avatar(AvatarIcon.BorderColor borderColor) {
    super(16, 16, null);
    this.borderColor = borderColor;
  }

  @Override
  protected void paintIcon(Component component, Graphics2D g) {
    borderColor.paint(g, 1, 1, width - 2, height - 2);
    g.fillRoundRect(1, 1, width - 2, height - 2, 5, 5);
    g.dispose();
  }
}
