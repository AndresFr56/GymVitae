package gym.vitae.views.membresias;

import gym.vitae.controller.ClienteController;
import gym.vitae.controller.MembresiasController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

@Metadata(
        name = "Vista de Membresías",
        description = "Listado de Membresías del gimnasio")
public class ViewMembresias extends ViewContainer {

    private MembresiaTablePanel membresiaTablePanel;

    @Override
    protected void init() {

        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));

        MembresiasController membresiaController = new MembresiasController();
        TiposMembresiaController tipoController = new TiposMembresiaController();
        ClienteController clienteController = new ClienteController();

        add(CeoHeader.createHeaderPanel(getClass().getAnnotation(Metadata.class)), "growx");

        membresiaTablePanel = new MembresiaTablePanel(
                membresiaController,
                tipoController,
                clienteController
        );

        add(membresiaTablePanel, "grow");
    }

    @Override
    protected void load() {
        if (membresiaTablePanel != null) membresiaTablePanel.loadData();
    }

    @Override
    protected void open() {
        load();
    }

    @Override
    protected void refresh() {
        if (membresiaTablePanel != null) membresiaTablePanel.refresh();
    }
}
