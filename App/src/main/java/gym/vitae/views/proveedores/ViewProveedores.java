package gym.vitae.views.proveedores;

import gym.vitae.controller.ProveedoresController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

@Metadata(
    name = "Vista de Proveedores",
    description = "Vista para gestionar los proveedores de productos y equipos")
public class ViewProveedores extends ViewContainer {

  private transient ProveedorTablePanel tablePanel;

  @Override
  protected void init() {
    ProveedoresController controller;
    setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));

    controller = new ProveedoresController();
    tablePanel = new ProveedorTablePanel(controller);

    add(CeoHeader.createHeaderPanel(getClass().getAnnotation(Metadata.class)), "growx");
    add(tablePanel, "grow");
  }

  @Override
  protected void load() {
    if (tablePanel != null) {
      tablePanel.loadData();
    }
  }

  @Override
  protected void open() {
    load();
  }

  @Override
  protected void refresh() {
    if (tablePanel != null) {
      tablePanel.refresh();
    }
  }
}
