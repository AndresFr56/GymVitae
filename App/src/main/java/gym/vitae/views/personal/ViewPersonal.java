package gym.vitae.views.personal;

import gym.vitae.controller.PersonalController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

/** Vista principal para gestión de personal del gimnasio. */
@Metadata(name = "Vista de Personal", description = "Vista para gestionar el personal del gimnasio")
public class ViewPersonal extends ViewContainer {

  private EmpleadoTablePanel tablePanel;

  @Override
  protected void init() {
    setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));

    PersonalController controller = new PersonalController();
    tablePanel = new EmpleadoTablePanel(controller);

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
