package gym.vitae.views.inventario;

import gym.vitae.controller.InventarioController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

/**
 * Vista de Inventario que muestra tabla combinada de productos y equipos. Implementa RF-17:
 * visualización del inventario con filtros por tipo, proveedor y nombre.
 */
@Metadata(
    name = "Gestión de Inventario",
    description = "Administración de productos y equipos del gimnasio")
public class ViewInventario extends ViewContainer {

  private transient InventarioTablePanel inventarioPanel;

  @Override
  protected void init() {
    setLayout(new MigLayout("fillx,wrap,insets 0", "[fill]", "[][fill,grow]"));

    // Controlador
    InventarioController inventarioController = new InventarioController();

    // Panel de tabla combinada de inventario
    inventarioPanel = new InventarioTablePanel(inventarioController);

    add(CeoHeader.createHeaderPanel(getClass().getAnnotation(Metadata.class)), "growx");
    add(inventarioPanel, "grow");
  }

  @Override
  protected void load() {
    inventarioPanel.loadData();
  }

  @Override
  protected void open() {
    load();
  }

  @Override
  protected void refresh() {
    inventarioPanel.refresh();
  }
}
