package demos.voxels;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import math.Color;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.transform.SnapToGroundModifier;
import workspace.ui.Graphics;

/**
 * ChunkVisualizer is a component that visualizes the chunk structure in the voxel engine. It
 * displays a 16x16x384 chunk with visual indicators for key world layers such as sea level, stone
 * layer, and the maximum build height limit.
 */
public class ChunkVisualizer1 extends AbstractComponent implements RenderableComponent {

  private final Mesh3D plane;
  private final Mesh3D chunk;

  /** Constructs a new ChunkVisualizer with a plane and chunk representation. */
  public ChunkVisualizer1() {
    plane = new PlaneCreator(8).create();
    chunk = new BoxCreator(16, 384, 16).create();
    new SnapToGroundModifier().modify(chunk);
  }

  /**
   * Renders the chunk and key world layers using the provided graphics context.
   *
   * @param g The graphics context used to render the chunk and planes.
   */
  @Override
  public void render(Graphics g) {
    // Render the chunk as a red box
    g.setColor(Color.RED);
    g.drawFaces(chunk);

    // Ground level at Y = 0
    g.setColor(Color.YELLOW);
    g.drawFaces(plane);

    // Stone layer at Y = -192
    g.setColor(Color.GRAY);
    g.pushMatrix();
    g.translate(0, -192, 0);
    g.drawFaces(plane);
    g.popMatrix();

    // Sea level at Y = -63
    g.setColor(Color.BLUE);
    g.pushMatrix();
    g.translate(0, -63, 0);
    g.drawFaces(plane);
    g.popMatrix();

    // Sky height limit at Y = -319
    g.setColor(Color.MAGENTA);
    g.pushMatrix();
    g.translate(0, -319, 0);
    g.drawFaces(plane);
    g.popMatrix();
  }

  /** Updates the component (currently unused). */
  @Override
  public void onUpdate(float tpf) {
    // No update logic needed for visualization
  }

  /** Called when the component is attached to a node (currently unused). */
  @Override
  public void onAttach() {
    // No additional logic on attach
  }

  /** Called when the component is detached from a node (currently unused). */
  @Override
  public void onDetach() {
    // No additional logic on detach
  }
}
