package gym.vitae.views.clientes;

import gym.vitae.controller.ClienteController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

@Metadata(
    name = "Vista de Clientes",
    description = "Vista para gestionar los clientes del gimnasio")
public class ViewClientes extends ViewContainer {

  private transient ClienteTablePanel tablePanel;

  @Override
  protected void init() {
    ClienteController controller;
    setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));

    controller = new ClienteController();
    tablePanel = new ClienteTablePanel(controller);

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
