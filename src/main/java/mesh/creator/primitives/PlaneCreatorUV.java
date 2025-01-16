package mesh.creator.primitives;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

/**
 * A class for creating a 2D plane mesh centered at the origin. The plane lies on the XZ plane and
 * is defined by its radius.
 *
 * <p>This creator is UV-ready and generates UV coordinates for the plane, mapping it to a texture
 * space ranging from (0, 0) to (1, 1).
 */
public class PlaneCreatorUV implements IMeshCreator {

  /** The radius of the plane. Defines half the width and length of the plane. */
  private float radius;

  /** The mesh object that stores the geometry of the plane. */
  private Mesh3D mesh;

  /** Constructs a PlaneCreator with a default radius of 1. */
  public PlaneCreatorUV() {
    this(1);
  }

  /**
   * Constructs a PlaneCreator with the specified radius.
   *
   * @param radius the radius of the plane
   */
  public PlaneCreatorUV(float radius) {
    this.radius = radius;
  }

  /**
   * Creates the mesh for the plane.
   *
   * <p>The resulting mesh includes vertices, a face, and UV coordinates for texture mapping.
   *
   * @return the generated {@link Mesh3D} object representing the plane
   */
  @Override
  public Mesh3D create() {
    initializeMesh();
    createVertices();
    createUvCoordinates();
    createFace();
    return mesh;
  }

  /** Initializes the mesh object to store the geometry. */
  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  /**
   * Creates the vertices for the plane. The vertices are arranged in a clockwise order on the XZ
   * plane.
   */
  private void createVertices() {
    addVertex(+radius, 0, -radius); // Top-right corner
    addVertex(+radius, 0, +radius); // Bottom-right corner
    addVertex(-radius, 0, +radius); // Bottom-left corner
    addVertex(-radius, 0, -radius); // Top-left corner
  }

  /**
   * Creates UV coordinates for the plane's vertices. The UV coordinates map the plane to a texture
   * space from (0, 0) to (1, 1).
   *
   * <p>Note: The `v` values are flipped because the Processing backend uses a texture coordinate
   * system where `v = 0` is at the top and `v = 1` is at the bottom.
   */
  private void createUvCoordinates() {
    mesh.addUvCoordinate(1, 1); // Top-right
    mesh.addUvCoordinate(1, 0); // Bottom-right
    mesh.addUvCoordinate(0, 0); // Bottom-left
    mesh.addUvCoordinate(0, 1); // Top-left
  }

  /**
   * Adds a vertex to the mesh.
   *
   * @param x the X coordinate of the vertex
   * @param y the Y coordinate of the vertex
   * @param z the Z coordinate of the vertex
   */
  private void addVertex(float x, float y, float z) {
    mesh.addVertex(x, y, z);
  }

  /**
   * Creates a single face for the plane. The face uses the vertices and UV coordinates in a
   * clockwise order.
   */
  private void createFace() {
    int[] vertexIndices = new int[] {0, 1, 2, 3};
    int[] uvIndices = new int[] {0, 1, 2, 3};
    mesh.add(new Face3D(vertexIndices, uvIndices));
  }

  /**
   * Returns the radius of the plane.
   *
   * @return the radius of the plane
   */
  public float getRadius() {
    return radius;
  }

  /**
   * Sets the radius of the plane.
   *
   * @param radius the new radius of the plane
   */
  public void setRadius(float radius) {
    this.radius = radius;
  }

  /**
   * Returns the size (width or length) of the plane. The size is twice the radius.
   *
   * @return the size of the plane
   */
  public float getSize() {
    return radius * 2;
  }

  /**
   * Sets the size (width or length) of the plane. The radius is set to half the size.
   *
   * @param size the new size of the plane
   */
  public void setSize(float size) {
    setRadius(size * 0.5f);
  }
}
