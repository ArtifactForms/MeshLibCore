package demos.landmass;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import math.Color;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import workspace.ui.Graphics;

public class ChunkBoxDisplay extends AbstractComponent implements RenderableComponent {

  private Color color = Color.WHITE;
  private Mesh3D chunkBoxMesh;

  public ChunkBoxDisplay(int chunkSize) {
    chunkBoxMesh = new CubeCreator(chunkSize / 2f).create();
  }

  @Override
  public void render(Graphics g) {
    g.setColor(color);
    g.drawFaces(chunkBoxMesh);
  }

  @Override
  public void onUpdate(float tpf) {}

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
