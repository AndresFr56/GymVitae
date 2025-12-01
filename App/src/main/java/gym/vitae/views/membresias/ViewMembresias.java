package gym.vitae.views.membresias;

import gym.vitae.controller.MembresiasController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.controller.ClienteController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

@Metadata(
    name = "Vista de Membresías",
    description = "Vista para gestionar las membresías del gimnasio")
public class ViewMembresias extends ViewContainer {
  
  private transient MembresiaTablePanel membresiaTablePanel;
  private transient TipoMembresiaTablePanel tipoMembresiaTablePanel;
  private JTabbedPane tabbedPane;

  @Override
  protected void init() {
    setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));

    // Controladores
    MembresiasController membresiaController = new MembresiasController();
    TiposMembresiaController tipoController = new TiposMembresiaController();
    ClienteController clienteController = new ClienteController();

    // Header
    add(CeoHeader.createHeaderPanel(getClass().getAnnotation(Metadata.class)), "growx");

    // Tabs
    tabbedPane = new JTabbedPane();
    
    membresiaTablePanel = new MembresiaTablePanel(membresiaController, tipoController, clienteController);
    tipoMembresiaTablePanel = new TipoMembresiaTablePanel(tipoController);

    tabbedPane.addTab("Membresías", membresiaTablePanel);
    tabbedPane.addTab("Tipos de Membresía", tipoMembresiaTablePanel);

    add(tabbedPane, "grow");
  }

  @Override
  protected void load() {
    if (membresiaTablePanel != null) {
      membresiaTablePanel.loadData();
    }
    if (tipoMembresiaTablePanel != null) {
      tipoMembresiaTablePanel.loadData();
    }
  }

  @Override
  protected void open() {
    load();
  }

  @Override
  protected void refresh() {
    if (membresiaTablePanel != null) {
      membresiaTablePanel.refresh();
    }
    if (tipoMembresiaTablePanel != null) {
      tipoMembresiaTablePanel.refresh();
    }
  }
}