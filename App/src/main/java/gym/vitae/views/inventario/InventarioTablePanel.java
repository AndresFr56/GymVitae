package gym.vitae.views.inventario;

import gym.vitae.controller.ClienteController;
import gym.vitae.controller.IventarioController;
import gym.vitae.model.dtos.inventario.InventarioListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;
import gym.vitae.views.components.tables.TableAction;
import gym.vitae.views.membresias.RegisterBeneficio;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

import java.util.List;

public class InventarioTablePanel extends BaseTablePanel<InventarioListadoDTO> {



    private final IventarioController controller;

    public InventarioTablePanel(IventarioController controller) {
        super("Gestión de Inventario");
        this.controller = controller;
    }

    @Override
    protected List<TableAction> getTableActions() {
        List<TableAction> actions = super.getTableActions();

        TableAction addEquipo = new TableAction("Nuevo Equipo", "#00BCD4", this::onAddEquipo);

        actions.add(0, addEquipo);

        return actions;
    }

    @Override
    protected String[] getColumnNames() {
        return new String[] { "SELECT", "Código", "Tipo", "Nombre", "Descripción", "Fecha Adquisición" };
    }

    @Override
    protected Object[] entityToRow(InventarioListadoDTO entity, int rowNumber) {
        return new Object[] {
                false, // checkbox
                entity.codigo(),
                entity.tipo(),
                entity.nombre(),
                entity.descripcion(),
                entity.fecha_Adquision()
        };
    }

    protected void onAddEquipo() {
        RegisterEquipo form = new RegisterEquipo();

        Option option = ModalDialog.createOption();
        option.getLayoutOption()
                .setSize(0.40f, 1f)
                .setLocation(Location.TRAILING, Location.TOP)
                .setAnimateDistance(0.7f, 0);

        ModalDialog.showModal(
                this,
                new SimpleModalBorder(
                        form,
                        "Nuevo Equipo",
                        SimpleModalBorder.DEFAULT_OPTION,
                        (control, action) -> {}
                ),
                option
        );

        form.getBtnGuardar().addOnClick(e -> {
            if (form.saveBeneficio()) {
                ModalDialog.closeAllModal();
            }
        });

        form.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
    }

    @Override
    protected void onAdd() {
        RegisterProducto form = new RegisterProducto(controller);

        Option option = ModalDialog.createOption();
        option.getLayoutOption()
                .setSize(0.40f, 1f)
                .setLocation(Location.TRAILING, Location.TOP)
                .setAnimateDistance(0.7f, 0);

        ModalDialog.showModal(
                this,
                new SimpleModalBorder(
                        form,
                        "Nuevo Producto",
                        SimpleModalBorder.DEFAULT_OPTION,
                        (control, action) -> {}
                ),
                option
        );

        form.getBtnGuardar().addOnClick(e -> {
            if (form.saveBeneficio()) {
                ModalDialog.closeAllModal();
            }
        });

        form.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
    }


    @Override
    protected boolean usesServerPagination() {
        return true;
    }
/*
    @Override
    protected List<InventarioListadoDTO> fetchPagedData(int offset, int limit) {
        return controller.getInventarioListado(offset, limit);
    }

    @Override
    protected List<InventarioListadoDTO> fetchPagedDataWithFilters(String searchText, int offset, int limit) {
        return controller.getInventarioListadoWithFilters(searchText, offset, limit);
    }

    @Override
    protected long fetchTotalCount() {
        return controller.countInventario();
    }

    @Override
    protected long fetchTotalCountWithFilters(String searchText) {
        return controller.countInventarioWithFilters(searchText);
    }


    @Override
    protected void onUpdateEntity(InventarioListadoDTO entity) {
        // Modal para editar producto o equipo
    }

    @Override
    protected void onDeleteEntities(List<InventarioListadoDTO> entities) {
        // Implementar eliminación o soft delete según tipo
    }
*/

}
