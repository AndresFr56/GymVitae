package gym.vitae.views.membresias;

import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

@Metadata(
        name = "Tipos de Membresía",
        description = "Administración de los tipos de membresía")
public class ViewTiposMembresia extends ViewContainer {

    private TipoMembresiaTablePanel tipoMembresiaTablePanel;

    @Override
    protected void init() {

        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));

        TiposMembresiaController tipoController = new TiposMembresiaController();

        add(CeoHeader.createHeaderPanel(getClass().getAnnotation(Metadata.class)), "growx");

        tipoMembresiaTablePanel = new TipoMembresiaTablePanel(tipoController);

        add(tipoMembresiaTablePanel, "grow");
    }

    @Override
    protected void load() {
        if (tipoMembresiaTablePanel != null) tipoMembresiaTablePanel.loadData();
    }

    @Override
    protected void open() {
        load();
    }

    @Override
    protected void refresh() {
        if (tipoMembresiaTablePanel != null) tipoMembresiaTablePanel.refresh();
    }
}
