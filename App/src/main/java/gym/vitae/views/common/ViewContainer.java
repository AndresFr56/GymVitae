package gym.vitae.views.common;

import javax.swing.*;

public abstract class ViewContainer extends JPanel {

  private boolean initialized = false;

  protected final void onViewShow() {
    if (!initialized) {
      initialized = true;
      init();
    }
  }

  /** Hooks para mantener/inicializar/actualizar/verificar el estado delViewContainer */
  protected abstract void init();

  protected void load() {}

  protected void open() {}

  protected void refresh() {}

  public boolean checkStats() {
    return false;
  }

  public String getTitle() {
    return this.getClass().getSimpleName();
  }

  public String getDescription() {
    return "";
  }
}
