package gym.vitae.views.utils;

import gym.vitae.model.Empleado;
import gym.vitae.views.common.ViewContainer;
import raven.modal.drawer.menu.MenuValidation;

public class ValidationItems extends MenuValidation {

  public static Empleado user;

  private static boolean checkMenu(int[] index, int[] indexHide) {
    if (index.length == indexHide.length) {
      for (int i = 0; i < index.length; i++) {
        if (index[i] != indexHide[i]) {
          return true;
        }
      }
      return false;
    }
    return true;
  }

  public static boolean validation(Class<? extends ViewContainer> itemClass) {
    int[] index = Drawer.getMenuIndexClass(itemClass);
    if (index == null) {
      return false;
    }
    return validation(index);
  }

  public static boolean validation(int[] index) {
    if (user == null) {
      return false;
    }
    if (user.getCargo().getNombre() == null) {
      return true;
    }

    return checkMenu(index, new int[] {2, 0})
        && checkMenu(index, new int[] {2, 1})
        && checkMenu(index, new int[] {1, 2});
  }

  @Override
  public boolean menuValidation(int[] index) {
    return validation(index);
  }
}
