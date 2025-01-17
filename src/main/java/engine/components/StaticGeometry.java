package engine.components;

import engine.render.Material;
import engine.vbo.VBO;
import engine.vbo.VBOFactory;
import math.Bounds;
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
public class StaticGeometry extends AbstractComponent implements RenderableComponent {

  /** The bounding box of the mesh used for culling, spatial partitioning, and debugging. */
  private Bounds bounds;

  private VBO vbo;

  private Material material;

  /**
   * Constructs a {@code StaticGeometry} with the specified mesh and a default material.
   *
   * @param mesh The {@link Mesh3D} object representing the geometry of the object.
   * @throws IllegalArgumentException If the mesh is {@code null}.
   */
  public StaticGeometry(Mesh3D mesh) {
    this(mesh, Material.DEFAULT_WHITE);
  }

  /**
   * Constructs a {@code StaticGeometry} with the specified mesh and material.
   *
   * @param mesh The {@link Mesh3D} object representing the geometry of the object.
   * @param material The {@link Material} to be applied to the mesh.
   * @throws IllegalArgumentException If the mesh or material is {@code null}.
   */
  public StaticGeometry(Mesh3D mesh, Material material) {
    validate(mesh, material);
    this.bounds = MeshBoundsCalculator.calculateBounds(mesh);
    this.vbo = VBOFactory.getInstance().create();
    this.vbo.create(mesh, material);
    this.material = material;
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

  @Override
  public void render(Graphics g) {
    material.apply(g);
    g.draw(vbo);
  }

  @Override
  public void onUpdate(float tpf) {}

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
