package gym.vitae.views.common;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class StateView {

  private static StateView instance;
  private final Map<Class<? extends ViewContainer>, ViewContainer> viewsMap;

  private StateView() {
    viewsMap = new HashMap<>();
  }

  private static StateView getInstance() {
    if (instance == null) {
      instance = new StateView();
    }
    return instance;
  }

  public static ViewContainer getView(Class<? extends ViewContainer> cls) {
    StateView a = getInstance();
    if (a.viewsMap.containsKey(cls)) {
      return a.viewsMap.get(cls);
    }
    try {
      ViewContainer v = cls.getDeclaredConstructor().newInstance();
      a.viewsMap.put(cls, v);
      // La inicialización (onViewShow) se hará en MainView.setView()
      return v;
    } catch (NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      throw new RuntimeException("No se pudo crear la vista: " + cls, e);
    }
  }

  public static void removeView(Class<? extends ViewContainer> cls) {
    getInstance().viewsMap.remove(cls);
  }

  public static void clear() {
    getInstance().viewsMap.clear();
  }
}
