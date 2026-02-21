package demos.voxels;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import math.Color;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.modifier.transform.SnapToGroundModifier;
import mesh.modifier.transform.TranslateModifier;

public class PlayerVisual extends AbstractComponent implements RenderableComponent {

  private Mesh3D mesh;

  public PlayerVisual() {
    mesh = new BoxCreator(0.9f, 1.8f, 0.9f).create();
    new SnapToGroundModifier().modify(mesh);
    new TranslateModifier(0, -0.5f, 0).modify(mesh);
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
