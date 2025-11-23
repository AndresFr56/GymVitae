package gym.vitae.views.themes;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.util.ArrayList;
import java.util.List;

public class ThemesManager {
  final List<Theme> coreThemes = new ArrayList<>();

  void loadThemes() {
    coreThemes.add(new Theme("FlatLaf Light", FlatLightLaf.class.getName()));
    coreThemes.add(new Theme("FlatLaf Dark", FlatDarkLaf.class.getName()));
  }
}
