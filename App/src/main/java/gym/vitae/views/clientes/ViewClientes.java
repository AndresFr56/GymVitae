package gym.vitae.views.clientes;

import gym.vitae.controller.ClienteController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@Metadata(
    name = "Vista de Clientes",
    description = "Vista para gestionar los clientes del gimnasio")
public class ViewClientes extends ViewContainer {

  private ClienteController controller;
  private ClienteTablePanel tablePanel;

  @Override
  protected void init() {
    setLayout(new BorderLayout());

    controller = new ClienteController();

    JPanel contentPanel = new JPanel(new MigLayout("fill,insets 0", "[fill]", "[fill]"));
    contentPanel.setOpaque(false);

    tablePanel = new ClienteTablePanel(controller);
    contentPanel.add(tablePanel, "grow");

    add(CeoHeader.createHeaderPanel(getClass().getAnnotation(Metadata.class)), "growx");
    add(contentPanel, BorderLayout.CENTER);
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
