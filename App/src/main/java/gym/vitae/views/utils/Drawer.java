package gym.vitae.views.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Drawer {

  private static final Map<Class<?>, int[]> MENU_INDEX = new ConcurrentHashMap<>();

  private Drawer() {}

  public static void registerMenuIndex(Class<?> cls, int[] index) {
    if (cls == null || index == null) return;
    MENU_INDEX.put(cls, index.clone());
  }

  public static int[] getMenuIndexClass(Class<?> cls) {
    int[] idx = MENU_INDEX.get(cls);
    return idx == null ? null : idx.clone();
  }

  public static void clear() {
    MENU_INDEX.clear();
  }
}
