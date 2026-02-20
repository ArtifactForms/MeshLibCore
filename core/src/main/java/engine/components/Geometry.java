package engine.components;

import engine.physics.ray.RaycastComponent;
import engine.physics.ray.RaycastHit;
import engine.render.Material;
import math.Bounds;
import math.Ray3f;
import math.Vector3f;
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
 * @see RaycastComponent
 * @see Material
 * @see Mesh3D
 * @see Bounds
 */
public class Geometry extends AbstractComponent implements RenderableComponent, RaycastComponent {

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
   * Renders the geometry by applying its material and issuing draw calls for the mesh.
   *
   * <p>This method is invoked during the scene's render traversal.
   *
   * @param g the graphics context used for rendering
   */
  @Override
  public void render(Graphics g) {
    g.setMaterial(material);
    g.fillFaces(mesh);
  }

  /**
   * Returns a copy of the mesh's axis-aligned bounding box in local space.
   *
   * <p>The returned bounds are independent of the owning node's transform and should be treated as
   * immutable by callers.
   *
   * @return the local-space bounding box of this geometry
   */
  public Bounds getLocalBounds() {
    return new Bounds(bounds.getMin(), bounds.getMax());
  }

  /**
   * Computes and returns the axis-aligned bounding box of this geometry in world space.
   *
   * <p>The world bounds are derived by translating the local bounds using the owning node's world
   * position. Rotation and scaling are currently not applied.
   *
   * <p>This method allocates a new {@link Bounds} instance on each call.
   *
   * @return the world-space bounding box of this geometry
   */
  public Bounds getWorldBounds() {
    Vector3f worldPos = getOwner().getWorldPosition();

    Vector3f min = bounds.getMin().add(worldPos);
    Vector3f max = bounds.getMax().add(worldPos);

    return new Bounds(min, max);
  }

  /**
   * Performs a raycast against this geometry using its world-space bounding box.
   *
   * <p>This method implements a coarse intersection test using an axis-aligned bounding box (AABB).
   * If the ray intersects the bounds, a {@link RaycastHit} is returned containing the hit point and
   * distance along the ray.
   *
   * <p>No triangle-level intersection testing is performed.
   *
   * @param ray the ray in world space
   * @return a {@link RaycastHit} if the ray intersects this geometry, or {@code null} otherwise
   */
  @Override
  public RaycastHit raycast(Ray3f ray) {
    Bounds worldBounds = getWorldBounds();

    Float distance = worldBounds.intersectRayDistance(ray);
    if (distance == null) {
      return null;
    }

    Vector3f hitPoint = ray.getOrigin().add(ray.getDirection().mult(distance));

    return new RaycastHit(getOwner(), hitPoint, distance);
  }

  /**
   * Returns the material applied to this geometry.
   *
   * <p>The returned {@link Material} is the actual instance used for rendering. Modifying it will
   * immediately affect this geometry.
   *
   * @return the material instance applied to this geometry
   */
  public Material getMaterial() {
    return material;
  }

  public Mesh3D getMesh() {
    return mesh;
  }

  /**
   * Updates the geometry component.
   *
   * <p>This implementation is currently a no-op but exists as an extension point for future dynamic
   * geometry behavior.
   *
   * @param tpf time per frame in seconds
   */
  @Override
  public void onUpdate(float tpf) {
    // Placeholder for potential mesh state updates
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
