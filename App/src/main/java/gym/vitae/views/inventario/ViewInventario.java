package gym.vitae.views.inventario;

import gym.vitae.controller.ClienteController;
import gym.vitae.controller.IventarioController;
import gym.vitae.model.dtos.inventario.InventarioListadoDTO;
import gym.vitae.views.clientes.ClienteTablePanel;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

@Metadata(
    name = "Vista de Inventario",
    description = "Vista para gestionar el inventario del gimnasio")
public class ViewInventario extends ViewContainer {

    private transient InventarioTablePanel tablePanel;
    private transient IventarioController controller;

  @Override
  protected void init() {
      controller = new IventarioController();
      tablePanel = new InventarioTablePanel(controller);

      setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));
      add(CeoHeader.createHeaderPanel(getClass().getAnnotation(Metadata.class)), "growx");
      add(tablePanel, "grow");
  }

  /** */
  @Override
  protected void load() {

  }

  /** */
  @Override
  protected void open() {}

  /** */
  @Override
  protected void refresh() {}
}
