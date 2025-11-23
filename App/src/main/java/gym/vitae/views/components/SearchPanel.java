package gym.vitae.views.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatMenuArrowIcon;
import gym.vitae.views.common.*;
import gym.vitae.views.components.contents.SearchContext;
import gym.vitae.views.components.primitives.Icons;
import gym.vitae.views.utils.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.component.ModalContainer;

public class SearchPanel extends JPanel {

  private static final int SEARCH_MAX_LENGTH = 50;
  private final Map<Metadata, Class<? extends ViewContainer>> formsMap;
  private final List<Item> listItems = new ArrayList<>();
  private transient LookAndFeel oldTheme = UIManager.getLookAndFeel();
  private JTextField textSearch;
  private JPanel panelResult;

  public SearchPanel(Map<Metadata, Class<? extends ViewContainer>> formsMap) {
    this.formsMap = formsMap;
    init();
  }

  private void init() {
    setLayout(new MigLayout("fillx,insets 0,wrap", "[fill,500]"));

    textSearch = new JTextField();
    panelResult = new PanelResult();

    textSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
    textSearch.putClientProperty(
        FlatClientProperties.TEXT_FIELD_LEADING_ICON,
        new FlatSVGIcon(Icons.SEARCH.getPath(), 0.4f));
    textSearch.putClientProperty(
        FlatClientProperties.STYLE,
        "border:3,3,3,3;" + "background:null;" + "showClearButton:true;");

    // aagregar el espaciado
    add(textSearch, "gap 17 17 0 0");
    add(new JSeparator(), "height 2!");

    JScrollPane scrollPane = new JScrollPane(panelResult);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());

    // customize scroll bar
    scrollPane
        .getVerticalScrollBar()
        .putClientProperty(
            FlatClientProperties.STYLE,
            "trackArc:$ScrollBar.thumbArc;"
                + "thumbInsets:0,3,0,3;"
                + "trackInsets:0,3,0,3;"
                + "width:12;");
    add(scrollPane);
    installSearchField();
  }

  public final void formCheck() {
    if (oldTheme != UIManager.getLookAndFeel()) {
      oldTheme = UIManager.getLookAndFeel();
      SwingUtilities.updateComponentTreeUI(this);
    }
  }

  // Configuracion de la busqueda
  private void installSearchField() {
    textSearch.setDocument(createLimitedDocument());
    textSearch.getDocument().addDocumentListener(createSearchDocumentListener());
    textSearch.addKeyListener(createSearchKeyAdapter());
  }

  // Limitar la longitud máxima del texto de búsqueda
  private PlainDocument createLimitedDocument() {
    return new PlainDocument() {
      @Override
      public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (getLength() + str.length() <= SEARCH_MAX_LENGTH) {
          super.insertString(offs, str, a);
        }
      }
    };
  }

  private DocumentListener createSearchDocumentListener() {
    return new DocumentListener() {
      private String text;

      @Override
      public void insertUpdate(DocumentEvent e) {
        onTextChanged();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        onTextChanged();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        onTextChanged();
      }

      private void onTextChanged() {
        String inputSearch = textSearch.getText().trim().toLowerCase();
        if (inputSearch.equals(text)) {
          return;
        }
        text = inputSearch;
        panelResult.removeAll();
        listItems.clear();

        if (inputSearch.isEmpty()) {
          showRecentResult();
        } else {
          performSearch(inputSearch);
        }
      }
    };
  }

  // listener para navegación con teclado Up and Down
  private KeyAdapter createSearchKeyAdapter() {
    return new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            move(true);
            break;
          case KeyEvent.VK_DOWN:
            move(false);
            break;
          case KeyEvent.VK_ENTER:
            showForm();
            break;
          default:
            break;
        }
      }
    };
  }

  /**
   * busqueda basada en el texto ingresado
   *
   * @param inputSearch texto de búsqueda
   */
  private void performSearch(String inputSearch) {
    for (Map.Entry<Metadata, Class<? extends ViewContainer>> entry : formsMap.entrySet()) {
      Metadata s = entry.getKey();
      if ((s.name().toLowerCase().contains(inputSearch)
              || s.description().toLowerCase().contains(inputSearch)
              || tagsContain(s.tags(), inputSearch))
          && ValidationItems.validation(entry.getValue())) {
        Item item = new Item(s, entry.getValue(), false, false);
        checkComponentOrientation(item);
        panelResult.add(item);
        listItems.add(item);
      }
    }

    if (!listItems.isEmpty()) {
      setSelected(0);
    } else {
      panelResult.add(createNoResult(inputSearch));
    }
    panelResult.repaint();
    updateLayout();
  }

  private boolean tagsContain(String[] tags, String st) {
    if (tags == null || tags.length == 0) return false;
    return Arrays.stream(tags).anyMatch(s -> s.contains(st));
  }

  private void updateLayout() {
    Container container = SwingUtilities.getAncestorOfClass(ModalContainer.class, SearchPanel.this);
    if (container != null) {
      container.revalidate();
    }
  }

  private void showForm() {
    int index = getSelectedIndex();
    if (index != -1) {
      listItems.get(index).showForm();
    }
  }

  private void setSelected(int index) {
    for (int i = 0; i < listItems.size(); i++) {
      listItems.get(i).setSelected(index == i);
    }
  }

  private int getSelectedIndex() {
    for (int i = 0; i < listItems.size(); i++) {
      if (listItems.get(i).isSelected()) {
        return i;
      }
    }
    return -1;
  }

  private void move(boolean up) {
    if (listItems.isEmpty()) return;
    int index = getSelectedIndex();
    int size = listItems.size();
    if (index == -1) {
      if (up) {
        index = listItems.size() - 1;
      } else {
        index = 0;
      }
    } else {
      if (up) {
        index = (index == 0) ? size - 1 : index - 1;
      } else {
        index = (index == size - 1) ? 0 : index + 1;
      }
    }
    setSelected(index);
  }

  private void showRecentResult() {

    List<Item> recentSearch = getRecentSearch(false);
    List<Item> favoriteSearch = getRecentSearch(true);

    panelResult.removeAll();
    listItems.clear();

    if (!recentSearch.isEmpty()) {
      panelResult.add(createLabel("Recent"));
      for (Item item : recentSearch) {
        checkComponentOrientation(item);
        panelResult.add(item);
        listItems.add(item);
      }
    }

    if (!favoriteSearch.isEmpty()) {
      panelResult.add(createLabel("Favorite"));
      for (Item item : favoriteSearch) {
        checkComponentOrientation(item);
        panelResult.add(item);
        listItems.add(item);
      }
    }

    if (listItems.isEmpty()) {
      panelResult.add(new NoRecentResult());
    } else {
      setSelected(0);
    }

    updateLayout();
  }

  private JLabel createLabel(String title) {
    JLabel label = new JLabel(title);
    label.putClientProperty(FlatClientProperties.STYLE, "font:bold +1;" + "border:5,15,5,15;");
    checkComponentOrientation(label);
    return label;
  }

  private List<Item> getRecentSearch(boolean favorite) {
    String[] recentSearch = Preferences.getRecentSearch(favorite);

    if (recentSearch == null) {
      return new ArrayList<>();
    }

    List<Item> list = new ArrayList<>();
    for (String beforeSearch : recentSearch) {
      Class<? extends ViewContainer> classForm = getClassForm(beforeSearch);
      if (ValidationItems.validation(classForm)) {
        Item item = createRecentItem(beforeSearch, favorite);
        if (item != null) {
          list.add(item);
        }
      }
    }
    return list;
  }

  private Class<? extends ViewContainer> getClassForm(String name) {
    for (Map.Entry<Metadata, Class<? extends ViewContainer>> entry : formsMap.entrySet()) {
      if (entry.getKey().name().equals(name)) {
        return entry.getValue();
      }
    }
    return null;
  }

  private Item createRecentItem(String name, boolean favorite) {
    for (Map.Entry<Metadata, Class<? extends ViewContainer>> entry : formsMap.entrySet()) {
      if (entry.getKey().name().equals(name)) {
        return new Item(entry.getKey(), entry.getValue(), true, favorite);
      }
    }
    return null;
  }

  private Component createNoResult(String text) {
    JPanel panel = new JPanel(new MigLayout("insets 15 5 15 5,al center,gapx 1"));
    JLabel label = new JLabel("No result for \"");
    JLabel labelEnd = new JLabel("\"");
    label.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
    labelEnd.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
    JLabel labelText = new JLabel(text);

    panel.add(label);
    panel.add(labelText);
    panel.add(labelEnd);
    return panel;
  }

  public void clearSearch() {
    if (!textSearch.getText().isEmpty()) {
      textSearch.setText("");
    } else {
      showRecentResult();
    }
  }

  public void searchGrabFocus() {
    textSearch.grabFocus();
  }

  private void checkComponentOrientation(Component com) {
    if (getComponentOrientation().isLeftToRight()
        != com.getComponentOrientation().isLeftToRight()) {
      com.applyComponentOrientation(getComponentOrientation());
    }
  }

  private static class NoRecentResult extends JPanel {

    public NoRecentResult() {
      init();
    }

    private void init() {
      setLayout(new MigLayout("insets 15 5 15 5,al center"));
      JLabel label = new JLabel("No recent searches");
      label.putClientProperty(
          FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;" + "font:bold;");
      add(label);
    }
  }

  private static class PanelResult extends JPanel implements Scrollable {

    public PanelResult() {
      super(new MigLayout("insets 3 10 3 10,fillx,wrap", "[fill]"));
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
      return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle rectangle, int i, int i1) {
      return 50;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle rectangle, int i, int i1) {
      return 50;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
      return false;
    }
  }

  private class Item extends JButton {

    private final Metadata data;
    private final Class<? extends ViewContainer> form;
    private final boolean isRecent;
    private final boolean isFavorite;
    private Component itemSource;

    public Item(
        Metadata data,
        Class<? extends ViewContainer> form,
        boolean isRecent,
        boolean isFavorite) {
      this.data = data;
      this.form = form;
      this.isRecent = isRecent;
      this.isFavorite = isFavorite;
      init();
    }

    private void init() {
      setFocusable(false);
      setHorizontalAlignment(SwingConstants.LEADING);
      setLayout(new MigLayout("insets 3 3 3 0,filly,gapy 2", "[]push[]"));
      putClientProperty(
          FlatClientProperties.STYLE,
          null
              + "background:null;"
              + "arc:10;"
              + "borderWidth:0;"
              + "focusWidth:0;"
              + "innerFocusWidth:0;"
              + "[light]selectedBackground:lighten($Button.selectedBackground,9%)");
      JLabel labelDescription = new JLabel(data.description());
      labelDescription.putClientProperty(
          FlatClientProperties.STYLE,   "foreground:$Label.disabledForeground;");
      add(new JLabel(data.name()), "cell 0 0");
      add(labelDescription, "cell 0 1");
      if (!isRecent) {
        add(new JLabel(new FlatMenuArrowIcon()), "cell 1 0,span 1 2");
      } else {
        add(createRecentOption(), "cell 1 0,span 1 2");
      }
      addActionListener(
          e -> {
            if (itemSource == null) {
              clearSelected();
              super.setSelected(true);
              showForm();
            } else if (itemSource.getName().equals("remove")) {
              removeRecent();
            } else if (itemSource.getName().equals("favorite")) {
              addFavorite();
            }
          });
    }

    private void clearSelected() {
      for (Component com : getParent().getComponents()) {
        if (com instanceof JButton comButton && com != this) {
          comButton.setSelected(false);
        }
      }
    }

    protected void showForm() {
      ModalDialog.closeModal(SearchContext.IDENTIFIER);
      Drawer.setSelectedItemClass(form);
      if (!isFavorite) {
        Preferences.addRecentSearch(data.name(), false);
      }
    }

    protected Component createRecentOption() {
      JPanel panel = new JPanel(new MigLayout("insets n 0 n 0,fill,gapx 2", "", "[fill]"));
      panel.setOpaque(false);
      JButton cmdRemove = createButton("remove", "clear.svg", 0.35f, "Label.foreground", 0.9f);
      if (!isFavorite) {
        JButton cmdFavorite =
            createButton("favorite", "favorite.svg", 0.4f, "Component.accentColor", 0.9f);
        panel.add(cmdFavorite);
      } else {
        JLabel label =
            new JLabel(
                new IconColorScheme(
                    Icons.FAVORITE.toString(), 0.4f, "Component.accentColor", 0.8f));
        label.putClientProperty(FlatClientProperties.STYLE,   "border:3,3,3,3;");
        panel.add(label);
      }
      panel.add(new JSeparator(SwingConstants.VERTICAL), "gapy 5 5");
      panel.add(cmdRemove);
      return panel;
    }

    private JButton createButton(
            String name, String icon, float scale, String hoverKey, float alpha) {
      IconColorScheme svgIcon = new IconColorScheme(icon, scale, "Label.disabledForeground", alpha);
      JButton button = new JButton(svgIcon);
      button.setName(name);
      button.setFocusable(false);
      button.setContentAreaFilled(false);
      button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      button.setModel(getModel());
      button.putClientProperty(FlatClientProperties.STYLE, "margin:4,4,4,4;");

      button.addMouseListener(
          new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
              svgIcon.setColorKey(hoverKey);
              itemSource = (Component) e.getSource();
            }

            @Override
            public void mouseExited(MouseEvent e) {
              svgIcon.setColorKey("Label.disabledForeground");
              itemSource = null;
            }
          });
      return button;
    }

    protected void removeRecent() {

      Preferences.removeRecentSearch(data.name(), isFavorite);

      panelResult.remove(this);
      listItems.remove(this);

      if (listItems.isEmpty()) {
        panelResult.removeAll();
        panelResult.add(new NoRecentResult());
      } else {

        if (getCount(isFavorite) == 0) {
          if (isFavorite) {
            panelResult.remove(panelResult.getComponentCount() - 1);
          } else {
            panelResult.remove(0);
          }
        }
      }
      updateLayout();
    }

    protected void addFavorite() {

      Preferences.addRecentSearch(data.name(), true);

      int[] index = getFirstFavoriteIndex();

      panelResult.remove(this);
      listItems.remove(this);

      Item item = new Item(data, form, isRecent, true);

      checkComponentOrientation(item);

      panelResult.remove(this);
      listItems.remove(this);
      panelResult.add(item, index[1] - 1);
      listItems.add(index[0] - 1, item);

      if (getCount(false) == 0) {
        panelResult.remove(0);
      }
      updateLayout();
    }

    private int getCount(boolean favorite) {
      int count = 0;
      for (Item item : listItems) {
        if (item.isFavorite == favorite) {
          count++;
        }
      }
      return count;
    }

    private int[] getFirstFavoriteIndex() {
      for (int i = 0; i < listItems.size(); i++) {
        if (listItems.get(i).isFavorite) {
          return new int[] {i, panelResult.getComponentZOrder(listItems.get(i))};
        }
      }
      // return de valor nulo si no hay elementos favoritos
      return new int[0];
    }
  }
}
