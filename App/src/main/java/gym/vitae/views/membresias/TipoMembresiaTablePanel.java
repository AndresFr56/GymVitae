package gym.vitae.views.membresias;

import gym.vitae.controller.BeneficiosController;
import gym.vitae.controller.TiposMembresiaController;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.views.components.tables.BaseTablePanel;
import gym.vitae.views.components.tables.TableAction; // <-- ASUMO que esta clase existe
import java.util.List;
import javax.swing.JOptionPane;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Location;
import raven.modal.option.Option;

public class TipoMembresiaTablePanel extends BaseTablePanel<TipoMembresiaListadoDTO> {

    private final TiposMembresiaController controller;
    private final BeneficiosController beneficioController;
    
    public TipoMembresiaTablePanel(TiposMembresiaController controller, BeneficiosController beneficioController) {
        super("Tipos de Membresía");
        this.controller = controller;
        this.beneficioController = beneficioController; 
    }
    
    
    @Override
    protected List<TableAction> getTableActions() {
        List<TableAction> actions = super.getTableActions(); 
        
        TableAction addBeneficioAction = new TableAction("Nuevo Beneficio", "#00BCD4", this::onAddBeneficio);
        
        actions.add(0, addBeneficioAction); 
        
        return actions;
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
        return controller.getTipos();
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
    
    protected void onAddBeneficio() {
        RegisterBeneficio form = new RegisterBeneficio(beneficioController);
        
        Option option = ModalDialog.createOption();
        option.getLayoutOption()
                .setSize(0.40f, 1f)                      
                .setLocation(Location.TRAILING, Location.TOP) 
                .setAnimateDistance(0.7f, 0);         

        ModalDialog.showModal(
                this,
                new SimpleModalBorder(
                        form,
                        "Nuevo Beneficio de Membresía",
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
        RegisterTipoMembresia form = new RegisterTipoMembresia(controller, beneficioController);
        Option option = ModalDialog.createOption();
        option.getLayoutOption()
                .setSize(0.40f, 1f)                      
                .setLocation(Location.TRAILING, Location.TOP) 
                .setAnimateDistance(0.7f, 0);         

        ModalDialog.showModal(
                this,
                new SimpleModalBorder(
                        form,
                        "Nuevo Tipo de Membresía",
                        SimpleModalBorder.DEFAULT_OPTION,
                        (control, action) -> {}
                ),
                option
        );


        form.getBtnGuardar().addOnClick(e -> {
            if (form.validateForm() && form.saveTipoMembresia()) { 
                ModalDialog.closeAllModal();
                refresh();
            }
        });

        form.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
    }


    @Override
    protected void onUpdateEntity(TipoMembresiaListadoDTO dto) {

        var detalle = controller.getTipoById(dto.getId());
        UpdateTipoMembresia form = new UpdateTipoMembresia(controller, detalle);

        Option option = ModalDialog.createOption();
        option.getLayoutOption()
                .setSize(0.40f, 1f)
                .setLocation(Location.TRAILING, Location.TOP)
                .setAnimateDistance(0.7f, 0);

        ModalDialog.showModal(
                this,
                new SimpleModalBorder(
                        form,
                        "Editar Tipo de Membresía",
                        SimpleModalBorder.DEFAULT_OPTION,
                        (control, action) -> {}
                ),
                option
        );

        form.getBtnGuardar().addOnClick(e -> {
            if (form.validateForm() && form.updateTipoMembresia()) {
                ModalDialog.closeAllModal();
                refresh();
            }
        });

        form.getBtnCancelar().addOnClick(e -> ModalDialog.closeAllModal());
    }


    @Override
    protected void onDeleteEntities(List<TipoMembresiaListadoDTO> tipos) {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas inactivar " + tipos.size() + " tipo(s)?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        for (var t : tipos) {
            controller.deleteTipo(t.getId());
        }

        JOptionPane.showMessageDialog(
                this,
                "Tipo(s) de membresía inactivados correctamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );

        refresh();
    }
}