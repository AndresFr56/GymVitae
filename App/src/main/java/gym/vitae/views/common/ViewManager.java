package gym.vitae.views.common;

import gym.vitae.core.SessionManager;
import gym.vitae.views.auth.ViewLogin;
import gym.vitae.views.components.contents.SearchContext;
import gym.vitae.views.components.primitives.About;
import gym.vitae.views.personal.ViewPersonal;
import gym.vitae.views.utils.UndoRedo;
import javax.swing.*;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;

public final class ViewManager {

  public static final UndoRedo<ViewContainer> history = new UndoRedo<>();
  private static JFrame frame;

  // vistas especiales que el gestor puede mostrar en login/logout
  private static ViewLogin loginView;
  private static MainView mainView;

  private ViewManager() {}

  public static void install(JFrame f) {
    frame = f;
    install();
    logout();
  }

  private static void install() {
    SearchContext.getInstance().installKeyMap(getMainView());
  }

  public static void showView(ViewContainer view) {
    if (view == null) return;

    ViewContainer current = getCurrent();
    if (current != null && current.checkStats()) {
      return;
    }

    if (current != null && current == view) return;

    if (current != null) history.clearRedo();

    history.add(view);

    // lifecycle en el hilo de eventos
    SwingUtilities.invokeLater(
        () -> {
          mainView.setView(view);
          mainView.refresh();
        });
  }

  public static void undo() {
    if (history.isUndoAble()) {
      ViewContainer current = history.getCurrent();
      if (current != null && current.checkStats()) return;

      ViewContainer previous = history.undo();
      if (previous != null) {
        SwingUtilities.invokeLater(
            () -> {
              previous.open();
              swapTo(previous);
              Drawer.setSelectedItemClass(previous.getClass());
            });
      }
    }
  }

  public static void redo() {
    if (history.isRedoAble()) {
      ViewContainer current = getCurrent();
      if (current != null && current.checkStats()) return;

      ViewContainer v = history.redo();
      if (v != null) {
        SwingUtilities.invokeLater(
            () -> {
              v.open();
              swapTo(v);
              Drawer.setSelectedItemClass(v.getClass());
            });
      }
    }
  }

  public static void refresh() {
    ViewContainer cur = getCurrent();
    if (cur != null) {
      SwingUtilities.invokeLater(cur::refresh);
    }
  }

  public static void login() {

    clearHistory();

    Drawer.setVisible(true);
    frame.getContentPane().removeAll();
    frame.getContentPane().add(getMainView());

    Drawer.setSelectedItemClass(ViewPersonal.class);
    frame.repaint();
    frame.revalidate();
  }

  public static void logout() {

    clearHistory();

    Drawer.setVisible(false);
    frame.getContentPane().removeAll();

    ViewLogin login = getLoginView();
    SessionManager.getInstance().logout();

    SwingUtilities.invokeLater(
        () -> {
          login.onViewShow();
          swapTo(login);
        });
  }

  public static JFrame getFrame() {
    return frame;
  }

  public static ViewContainer getCurrent() {
    return history.getCurrent();
  }

  public static boolean isUndoable() {
    return history.isUndoAble();
  }

  public static boolean isRedoable() {
    return history.isRedoAble();
  }

  public static void clearHistory() {
    history.clear();
  }

  public static ViewLogin getLoginView() {
    if (loginView == null) {
      loginView = new ViewLogin();
    }
    return loginView;
  }

  public static MainView getMainView() {
    if (mainView == null) {
      mainView = new MainView();
    }
    return mainView;
  }

  public static void showAboutModal() {
    ModalDialog.showModal(
        frame,
        new SimpleModalBorder(new About(), "About"),
        ModalDialog.createOption().setAnimationEnabled(false));
  }

  private static void swapTo(ViewContainer v) {
    if (frame == null) return;
    SwingUtilities.invokeLater(
        () -> {
          frame.getContentPane().removeAll();
          frame.getContentPane().add(v);
          frame.revalidate();
          frame.repaint();
        });
  }
}
