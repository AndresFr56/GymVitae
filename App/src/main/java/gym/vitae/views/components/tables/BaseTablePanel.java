package gym.vitae.views.components.tables;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import gym.vitae.views.components.primitives.Pagination;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

/**
 * Panel base genérico para tablas con paginación, filtros y acciones CRUD.
 *
 * @param <T> Tipo de entidad que se mostrará en la tabla (Empleado, Cliente, etc.)
 */
public abstract class BaseTablePanel<T> extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(BaseTablePanel.class.getName());
  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int MAX_SEARCH_LENGTH = 200;
  private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
  private final String title;
  // Componentes UI
  protected DefaultTableModel tableModel;
  protected Pagination pagination;
  protected JLabel lbTotalRecords;
  protected JTextField txtSearch;
  // Datos
  private transient List<T> cachedData = new ArrayList<>();
  private transient List<T> filteredData = new ArrayList<>();
  private transient List<T> currentPageData =
      new ArrayList<>(); // Datos de la página actual (modo servidor)

  /**
   * Constructor del panel de tabla.
   *
   * @param title Título que se mostrará en el panel
   */
  protected BaseTablePanel(String title) {
    this.title = title;
    initializePanel();
  }

  /**
   * Define los nombres de las columnas de la tabla. La primera columna DEBE ser "SELECT" para el
   * checkbox de selección.
   *
   * @return Array con los nombres de las columnas
   */
  protected abstract String[] getColumnNames();

  /**
   * Convierte una entidad en una fila de la tabla.
   *
   * @param entity Entidad a convertir
   * @param rowNumber Número de fila (para la columna #)
   * @return Array de objetos que representa la fila
   */
  protected abstract Object[] entityToRow(T entity, int rowNumber);

  /**
   * Obtiene todos los datos desde el controlador/repositorio. Solo se usa si usesServerPagination()
   * retorna false.
   *
   * @return Lista completa de entidades
   */
  protected List<T> fetchAllData() {
    return List.of(); // Implementación por defecto vacía
  }

  /**
   * Obtiene datos paginados desde el servidor. Solo se usa si usesServerPagination() retorna true.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de entidades de la página actual
   */
  protected List<T> fetchPagedData(int offset, int limit) {
    return List.of(); // Implementación por defecto vacía
  }

  /**
   * Obtiene datos paginados desde el servidor CON FILTROS. Override este método si usas paginación
   * del servidor Y filtros.
   *
   * @param searchText Texto de búsqueda
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de entidades de la página actual filtradas
   */
  protected List<T> fetchPagedDataWithFilters(String searchText, int offset, int limit) {
    // Por defecto delega a fetchPagedData ignorando filtros
    return fetchPagedData(offset, limit);
  }

  /**
   * Obtiene el total de registros desde el servidor. Solo se usa si usesServerPagination() retorna
   * true.
   *
   * @return Total de registros
   */
  protected long fetchTotalCount() {
    return 0L; // Implementación por defecto
  }

  /**
   * Obtiene el total de registros desde el servidor CON FILTROS. Override este método si usas
   * paginación del servidor Y filtros.
   *
   * @param searchText Texto de búsqueda
   * @return Total de registros filtrados
   */
  protected long fetchTotalCountWithFilters(String searchText) {
    // Por defecto delega a fetchTotalCount ignorando filtros
    return fetchTotalCount();
  }

  /**
   * Indica si se debe usar paginación del servidor o en memoria. Override para retornar true si
   * quieres usar fetchPagedData().
   *
   * @return true para paginación del servidor, false para paginación en memoria
   */
  protected boolean usesServerPagination() {
    return false; // Por defecto usa paginación en memoria
  }

  // ==================== MÉTODOS HOOK (PUEDEN SOBRESCRIBIRSE) ====================

  /**
   * Configura los anchos de las columnas de la tabla. Override para personalizar.
   *
   * @param table Tabla a configurar
   */
  protected void configureColumnWidths(JTable table) {
    // Configuración por defecto: checkbox y # son columnas pequeñas
    if (table.getColumnCount() > 0) {
      table.getColumnModel().getColumn(0).setMaxWidth(50); // SELECT
    }
    if (table.getColumnCount() > 1) {
      table.getColumnModel().getColumn(1).setMaxWidth(50); // #
    }
  }

  /**
   * Crea filtros personalizados adicionales. Override para agregar ComboBox, DatePickers, etc.
   *
   * @return Panel con filtros personalizados o null
   */
  protected JPanel createCustomFilters() {
    return null;
  }

  /**
   * Filtra una entidad según los criterios actuales. Override para implementar lógica de filtrado
   * personalizada.
   *
   * @param entity Entidad a filtrar
   * @param searchText Texto de búsqueda actual
   * @return true si la entidad pasa el filtro, false en caso contrario
   */
  protected boolean filterEntity(T entity, String searchText) {
    return true; // Por defecto no filtra
  }

  /**
   * Define las acciones disponibles en el panel. Override para personalizar botones.
   *
   * @return Lista de acciones de tabla
   */
  protected List<TableAction> getTableActions() {
    List<TableAction> actions = new ArrayList<>();
    actions.add(new TableAction("Agregar", "#2196F3", this::onAdd));
    actions.add(new TableAction("Actualizar", "#4CAF50", this::onUpdate));
    actions.add(new TableAction("Eliminar", "#F44336", this::onDelete));
    return actions;
  }

  protected void onAdd() {
    showInfoMessage("Funcionalidad de agregar en desarrollo");
  }

  /** Callback cuando se presiona el botón Actualizar. */
  protected void onUpdate() {
    List<T> selected = getSelectedEntities();
    if (selected.isEmpty()) {
      showWarning("Por favor seleccione al menos un registro");
      return;
    }
    if (selected.size() > 1) {
      showWarning("Por favor seleccione solo un registro para actualizar");
      return;
    }
    onUpdateEntity(selected.get(0));
  }

  /**
   * Callback cuando se actualiza una entidad específica.
   *
   * @param entity Entidad seleccionada
   */
  protected void onUpdateEntity(T entity) {
    showInfoMessage("Funcionalidad de actualizar en desarrollo");
  }

  /** Callback cuando se presiona el botón Eliminar. */
  protected void onDelete() {
    List<T> selected = getSelectedEntities();
    if (selected.isEmpty()) {
      showWarning("Por favor seleccione al menos un registro");
      return;
    }

    int result =
        JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar " + selected.size() + " registro(s)?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

    if (result == JOptionPane.YES_OPTION) {
      onDeleteEntities(selected);
    }
  }

  /**
   * Callback cuando se confirma la eliminación de entidades.
   *
   * @param entities Lista de entidades a eliminar
   */
  protected void onDeleteEntities(List<T> entities) {
    showInfoMessage("Funcionalidad de eliminar en desarrollo");
  }

  // ==================== MÉTODOS PÚBLICOS ====================

  /** Carga los datos desde el origen y actualiza la tabla. */
  public void loadData() {
    try {
      if (usesServerPagination()) {
        updateTableDataFromServer();
      } else {
        cachedData = fetchAllData();
        applyFiltersAndRefresh();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al cargar datos", e);
      showError("Error al cargar datos: " + e.getMessage());
    }
  }

  /** Recarga los datos y actualiza la tabla. */
  public void refresh() {
    loadData();
  }

  /**
   * Obtiene las entidades seleccionadas en la tabla.
   *
   * @return Lista de entidades seleccionadas
   */
  public List<T> getSelectedEntities() {
    List<T> selected = new ArrayList<>();
    int[] indices = getSelectedRowIndices();

    // Usar currentPageData en modo servidor, filteredData en modo memoria
    List<T> sourceData = usesServerPagination() ? currentPageData : filteredData;

    for (int index : indices) {
      if (index >= 0 && index < sourceData.size()) {
        selected.add(sourceData.get(index));
      }
    }

    return selected;
  }

  // ==================== MÉTODOS PRIVADOS DE INICIALIZACIÓN ====================

  private void initializePanel() {
    setLayout(new MigLayout("fillx,wrap,insets 16 0 10 0", "[fill]", "[][][][fill,grow][]"));
    putClientProperty(FlatClientProperties.STYLE, "arc:10;background:$Table.background;");

    add(createTitleLabel(), "gapx 20");
    add(createFiltersPanel(), "growx");
    add(createActionsPanel(), "growx");
    add(createTable(), "grow");
    add(createPaginationPanel(), "growx");
  }

  private Component createTitleLabel() {
    JLabel titleLabel = new JLabel(title);
    titleLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
    return titleLabel;
  }

  private Component createFiltersPanel() {
    JPanel panel = new JPanel(new MigLayout("insets 5 20 5 20", "[fill,200][]push[]"));

    // Campo de búsqueda
    txtSearch = new JTextField();
    txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar...");
    txtSearch.putClientProperty(
        FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("icons/search.svg", 0.4f));

    txtSearch
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              public void changedUpdate(DocumentEvent e) {
                applyFiltersAndRefresh();
              }

              public void removeUpdate(DocumentEvent e) {
                applyFiltersAndRefresh();
              }

              public void insertUpdate(DocumentEvent e) {
                validateSearchText();
                applyFiltersAndRefresh();
              }
            });

    panel.add(txtSearch);

    // Filtros personalizados
    JPanel customFilters = createCustomFilters();
    if (customFilters != null) {
      panel.add(customFilters);
    }

    // Botón limpiar filtros
    JButton btnClear = new JButton("Limpiar Filtros");
    btnClear.addActionListener(e -> clearFilters());
    panel.add(btnClear);

    panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
    return panel;
  }

  private Component createActionsPanel() {
    JPanel panel = new JPanel(new MigLayout("insets 5 20 5 20", "push[]"));

    List<TableAction> actions = getTableActions();
    for (TableAction action : actions) {
      panel.add(action.createButton());
    }

    panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
    return panel;
  }

  private Component createTable() {
    String[] columns = getColumnNames();

    tableModel =
        new DefaultTableModel(columns, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return column == 0; // Solo la columna SELECT es editable
          }

          @Override
          public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Boolean.class;
            return super.getColumnClass(columnIndex);
          }
        };

    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());

    // Configurar columnas
    configureColumnWidths(table);
    table.getTableHeader().setReorderingAllowed(false);

    // Checkbox renderer para columna SELECT
    table
        .getColumnModel()
        .getColumn(0)
        .setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));

    // Alineación del header
    table.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(table));

    // Estilos de tabla
    table
        .getTableHeader()
        .putClientProperty(
            FlatClientProperties.STYLE,
            "height:32;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;");

    table.putClientProperty(
        FlatClientProperties.STYLE,
        "rowHeight:40;"
            + "showHorizontalLines:true;"
            + "intercellSpacing:0,1;"
            + "cellFocusColor:$TableHeader.hoverBackground;"
            + "selectionBackground:$TableHeader.hoverBackground;"
            + "selectionInactiveBackground:$TableHeader.hoverBackground;"
            + "selectionForeground:$Table.foreground;");

    scrollPane
        .getVerticalScrollBar()
        .putClientProperty(
            FlatClientProperties.STYLE,
            "trackArc:$ScrollBar.thumbArc;"
                + "trackInsets:3,3,3,3;"
                + "thumbInsets:3,3,3,3;"
                + "background:$Table.background;");

    return scrollPane;
  }

  private Component createPaginationPanel() {
    JPanel panel = new JPanel(new MigLayout("insets 10 16 10 16,fillx,ay center", "[][]push[]"));

    lbTotalRecords = new JLabel("0");
    pagination = new Pagination();
    pagination.addChangeListener(
        e -> {
          if (usesServerPagination()) {
            updateTableDataFromServer();
          } else {
            updateTableData();
          }
        });

    panel.add(new JLabel("Total:"));
    panel.add(lbTotalRecords);
    panel.add(pagination, "ay center,h 32!");

    panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
    return panel;
  }

  private void validateSearchText() {
    String text = txtSearch.getText();
    if (text.length() > MAX_SEARCH_LENGTH) {
      String trimmed = text.substring(0, MAX_SEARCH_LENGTH);
      SwingUtilities.invokeLater(() -> txtSearch.setText(trimmed));
    }
  }

  private void clearFilters() {
    txtSearch.setText("");
    applyFiltersAndRefresh();
  }

  private void applyFiltersAndRefresh() {
    if (usesServerPagination()) {
      // Con paginación del servidor, resetear a página 1 y recargar
      if (pagination != null) {
        pagination.getModel().setSelectedPage(1);
      }
      updateTableDataFromServer();
    } else {
      // Con paginación en memoria, filtrar datos cacheados
      String searchText = txtSearch.getText().trim();

      filteredData =
          cachedData.stream().filter(entity -> filterEntity(entity, searchText)).toList();

      if (pagination != null) {
        pagination.getModel().setSelectedPage(1);
      }

      updateTableData();
    }
  }

  /** Actualiza la tabla obteniendo datos directamente del servidor (paginación del servidor). */
  private void updateTableDataFromServer() {
    try {
      int pageSize = DEFAULT_PAGE_SIZE;
      int currentPage = pagination != null ? pagination.getSelectedPage() : 1;
      int offset = (currentPage - 1) * pageSize;

      // Obtener texto de búsqueda actual
      String searchText = txtSearch != null ? txtSearch.getText().trim() : "";

      // Obtener total y datos de la página actual desde el servidor CON FILTROS
      long total = fetchTotalCountWithFilters(searchText);
      List<T> pageData = fetchPagedDataWithFilters(searchText, offset, pageSize);

      // Guardar datos actuales para getSelectedEntities
      currentPageData = new ArrayList<>(pageData);

      // Configurar paginación
      if (pagination != null) {
        pagination.getModel().setPageSize((int) total);
        pagination.getModel().setPageSize(pageSize);
      }

      // Actualizar tabla
      populateTable(pageData, offset);

      // Actualizar contador
      if (lbTotalRecords != null) {
        lbTotalRecords.setText(NUMBER_FORMAT.format(total));
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error al cargar datos paginados", e);
      showError("Error al cargar datos: " + e.getMessage());
    }
  }

  private void updateTableData() {
    int total = filteredData.size();
    int pageSize = DEFAULT_PAGE_SIZE;

    // Configurar paginación
    if (pagination != null) {
      pagination.getModel().setPageSize(total);
      pagination.getModel().setPageSize(pageSize);
    }

    // Calcular offset
    int currentPage = pagination != null ? pagination.getSelectedPage() : 1;
    int offset = (currentPage - 1) * pageSize;

    // Obtener datos de la página actual
    List<T> paginatedData = filteredData.stream().skip(offset).limit(pageSize).toList();

    // Actualizar tabla
    populateTable(paginatedData, offset);

    // Actualizar contador
    if (lbTotalRecords != null) {
      lbTotalRecords.setText(NUMBER_FORMAT.format(total));
    }
  }

  private void populateTable(List<T> entities, int offset) {
    tableModel.setRowCount(0);
    int rowNumber = offset + 1;

    for (T entity : entities) {
      Object[] row = entityToRow(entity, rowNumber++);
      tableModel.addRow(row);
    }
  }

  private int[] getSelectedRowIndices() {
    List<Integer> selected = new ArrayList<>();
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
      if (isSelected != null && isSelected) {
        selected.add(i);
      }
    }
    return selected.stream().mapToInt(Integer::intValue).toArray();
  }

  protected void showWarning(String message) {
    JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
  }

  protected void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  protected void showInfoMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
  }
}
