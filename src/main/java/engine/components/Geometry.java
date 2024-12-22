package engine.components;

import engine.render.Material;
import math.Bounds;
import math.Color;
import mesh.Mesh3D;
import mesh.util.MeshBoundsCalculator;
import workspace.ui.Graphics;

/**
 * The {@code Geometry} class represents a 3D object in a scene with a mesh and material applied to
 * it. It is responsible for rendering the mesh and applying the appropriate material to it. The
 * class also provides access to the mesh's bounding box, which is useful for purposes like culling,
 * spatial partitioning, and debugging.
 *
 * <p>This class implements the {@link RenderableComponent} interface, indicating that it has a
 * render method to be invoked during the render loop of the engine.
 *
 * @see RenderableComponent
 * @see Material
 * @see Mesh3D
 * @see Bounds
 */
public class Geometry extends AbstractComponent implements RenderableComponent {

  /** The mesh representing the geometry of the object. */
  private Mesh3D mesh;

  /** The material applied to the mesh for rendering. */
  private Material material;

  /** The bounding box of the mesh used for culling, spatial partitioning, and debugging. */
  private Bounds bounds;

  /**
   * Constructs a {@code Geometry} with the specified mesh and a default material.
   *
   * @param mesh The {@link Mesh3D} object representing the geometry of the object.
   * @throws IllegalArgumentException If the mesh is {@code null}.
   */
  public Geometry(Mesh3D mesh) {
    this(mesh, Material.DEFAULT_WHITE);
  }

  /**
   * Constructs a {@code Geometry} with the specified mesh and material.
   *
   * @param mesh The {@link Mesh3D} object representing the geometry of the object.
   * @param material The {@link Material} to be applied to the mesh.
   * @throws IllegalArgumentException If the mesh or material is {@code null}.
   */
  public Geometry(Mesh3D mesh, Material material) {
    validate(mesh, material);
    this.mesh = mesh;
    this.material = material;
    this.bounds = MeshBoundsCalculator.calculateBounds(mesh);
  }

  /**
   * Validates the mesh and material to ensure they are not {@code null}.
   *
   * @param mesh The {@link Mesh3D} object to validate.
   * @param material The {@link Material} to validate.
   * @throws IllegalArgumentException If the mesh or material is {@code null}.
   */
  private void validate(Mesh3D mesh, Material material) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    if (material == null) {
      throw new IllegalArgumentException("Material cannot be null.");
    }
  }

  /**
   * Renders the geometry by applying the material and drawing the mesh using the specified graphics
   * context.
   *
   * @param g The {@link Graphics} context used for rendering.
   */
  @Override
  public void render(Graphics g) {
    material.apply(g);
    g.fillFaces(mesh);
    material.release(g);
    debugRenderBounds(g);
  }

  /**
   * Debugs the rendering by drawing the bounding box of the mesh using the specified graphics
   * context. The bounding box is rendered in red to help visualize the mesh's extents. This method
   * can be used for debugging purposes to ensure the mesh is properly positioned and scaled in the
   * scene.
   *
   * <p>Beyond debugging, the bounding box is useful for spatial partitioning techniques like
   * frustum culling, as well as for determining the overall size and position of the mesh in the 3D
   * world.
   *
   * @param g The {@link Graphics} context used for rendering the debug bounding box.
   */
  public void debugRenderBounds(Graphics g) {
    if (bounds == null) {
      return;
    }

    g.setColor(Color.RED);

    // Extract corner points for readability
    float minX = bounds.getMin().x;
    float minY = bounds.getMin().y;
    float minZ = bounds.getMin().z;
    float maxX = bounds.getMax().x;
    float maxY = bounds.getMax().y;
    float maxZ = bounds.getMax().z;

    // Draw lines for each edge of the bounding box
    g.drawLine(minX, minY, minZ, maxX, minY, minZ);
    g.drawLine(minX, minY, minZ, minX, maxY, minZ);
    g.drawLine(minX, minY, minZ, minX, minY, maxZ);

    g.drawLine(maxX, maxY, maxZ, minX, maxY, maxZ);
    g.drawLine(maxX, maxY, maxZ, maxX, minY, maxZ);
    g.drawLine(maxX, maxY, maxZ, maxX, maxY, minZ);

    g.drawLine(minX, maxY, minZ, maxX, maxY, minZ);
    g.drawLine(maxX, minY, minZ, maxX, maxY, minZ);
    g.drawLine(maxX, minY, minZ, maxX, minY, maxZ);

    g.drawLine(minX, maxY, maxZ, minX, minY, maxZ);
    g.drawLine(maxX, minY, maxZ, minX, minY, maxZ);
    g.drawLine(minX, maxY, maxZ, minX, maxY, minZ);
  }

  /**
   * Updates the state of the geometry. This method is a placeholder for potential updates to the
   * mesh state over time.
   *
   * @param tpf The time per frame used for the update (in seconds).
   */
  @Override
  public void update(float tpf) {
    // Placeholder for potential mesh state updates
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
