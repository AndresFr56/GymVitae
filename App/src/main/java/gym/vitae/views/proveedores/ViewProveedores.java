package gym.vitae.views.proveedores;

import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import gym.vitae.views.components.CeoHeader;
import net.miginfocom.swing.MigLayout;

@Metadata(
    name = "Listado de Proveedores",
    description = "Gestione los proveedores de productos e equipos")
public class ViewProveedores extends ViewContainer {

  @Override
  protected void init() {
    setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));
    add(CeoHeader.createHeaderPanel(getClass().getAnnotation(Metadata.class)), "growx");
  }
}
