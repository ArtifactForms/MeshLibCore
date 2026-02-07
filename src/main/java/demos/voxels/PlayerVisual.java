package demos.voxels;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import math.Color;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.modifier.SnapToGroundModifier;
import mesh.modifier.TranslateModifier;
import workspace.ui.Graphics;

public class PlayerVisual extends AbstractComponent implements RenderableComponent {

  private Mesh3D mesh;

  public PlayerVisual() {
    mesh = new BoxCreator(0.9f, 1.8f, 0.9f).create();
    mesh.apply(new SnapToGroundModifier());
    mesh.apply(new TranslateModifier(0, -0.5f, 0));
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.YELLOW);
    g.drawFaces(mesh);
    g.drawLine(0, 0, 0, 0, -200, 0);
  }

  @Override
  public void onUpdate(float tpf) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onAttach() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDetach() {
    // TODO Auto-generated method stub

  }
}
