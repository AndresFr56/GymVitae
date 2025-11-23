package gym.vitae.views.personal;

import gym.vitae.views.common.Metadata;
import gym.vitae.views.common.ViewContainer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

@Metadata(name = "Vista de Personal", description = "Vista para gestionar el personal del gimnasio")
public class ViewPersonal extends ViewContainer {

  @Override
  protected void init() {
    setLayout(new MigLayout("al center center"));
    JLabel text = new JLabel("Input");
    add(text);
  }

  @Override
  protected void load() {}

  @Override
  protected void open() {}

  @Override
  protected void refresh() {}
}
