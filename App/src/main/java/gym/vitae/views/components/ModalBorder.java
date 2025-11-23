package gym.vitae.views.components;

import java.awt.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.Modal;
import raven.modal.component.ModalBorderAction;
import raven.modal.listener.ModalCallback;
import raven.modal.listener.ModalController;

public class ModalBorder extends Modal implements ModalBorderAction {
  public static final int OPENED = 20;
  protected final Component component;
  private final transient ModalCallback callback;

  public ModalBorder(Component component) {
    this(component, null);
  }

  public ModalBorder(Component component, ModalCallback callback) {
    this.component = component;
    this.callback = callback;
    setLayout(new MigLayout("fill,insets 8 0 8 0", "[fill]", "[fill]"));
    add(component);
  }

  @Override
  protected void modalOpened() {
    if (callback != null) {
      callback.action(createController(), OPENED);
    }
  }

  @Override
  public void doAction(int action) {
    if (callback == null) {
      getController().closeModal();
    } else {
      ModalController controller = createController();
      callback.action(controller, action);
      if (!controller.getConsume()) {
        getController().closeModal();
      }
    }
  }

  @Override
  public Color getBackground() {
    if (component == null) {
      return super.getBackground();
    }
    return component.getBackground();
  }

  private ModalController createController() {
    return new ModalController(this) {
      @Override
      public void close() {
        getController().closeModal();
      }
    };
  }
}
