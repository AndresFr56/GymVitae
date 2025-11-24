package gym.vitae.views.clases;

import gym.vitae.controller.ClasesController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

/** Vista principal para gestión de clases del gimnasio. */
@Metadata(name = "Vista de Clases", description = "Vista para gestionar las clases del gimnasio")
public class ViewClases extends ViewContainer {

  private ClaseTablePanel tablePanel;

  @Override
  protected void init() {
    setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));

    ClasesController controller = new ClasesController();
    tablePanel = new ClaseTablePanel(controller);

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
    if (tablePanel != null) {
      tablePanel.loadData();
    }
  }

  @Override
  protected void refresh() {
    if (tablePanel != null) {
      tablePanel.refresh();
    }
  }
}
