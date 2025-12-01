package gym.vitae.views.membresias;

import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;

import java.util.List;

public class TipoMembresiaTablePanel extends BaseTablePanel<TipoMembresiaListadoDTO> {

    private final TiposMembresiaController controller;

    public TipoMembresiaTablePanel(TiposMembresiaController controller) {
        super("Tipos de Membresía");
        this.controller = controller;
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{
            "SELECT", "#", "Nombre", "Descripción", "Duración (días)",
            "Costo", "Acceso Completo", "Activo"
        };
    }

    @Override
    protected List<TipoMembresiaListadoDTO> fetchAllData() {
        return controller.getTipos(); // <-- ESTE SÍ EXISTE
    }

    @Override
    protected Object[] entityToRow(TipoMembresiaListadoDTO t, int rowNumber) {
        return new Object[]{
            false,
            rowNumber,
            t.getNombre(),
            t.getDescripcion(),
            t.getDuracionDias(),
            t.getCosto(),
            t.getAccesoCompleto() ? "Sí" : "No",
            t.getActivo() ? "Activo" : "Inactivo"
        };
    }

    
}
