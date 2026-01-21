package mesh.creator.primitives;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

/**
 * The CubeCreatorUV class generates a 3D cube mesh with UV mapping. It supports creating a cube
 * with customizable size (radius) and generates the appropriate UV coordinates for each face of the
 * cube. The cube is centered around the origin (0, 0, 0) and uses specific UV coordinates to map
 * textures onto the cube faces.
 *
 * <p>The cube is created with 6 faces, each having its own set of UV coordinates that map specific
 * areas of a texture to the cube's surfaces. This class is designed to be used with a texture atlas
 * that divides the texture into different regions corresponding to the faces of the cube.
 */
public class CubeCreatorUV implements IMeshCreator {

  private static final float EPSILON = 0.001f; // Small offset to avoid texture bleeding

  private static final float ONE_THIRD = 1f / 3f;

  private static final float TWO_THIRDS = 2f / 3f;

  private float radius;

  private Mesh3D mesh;

  /** Default constructor that creates a cube with a radius of 1. */
  public CubeCreatorUV() {
    this(1);
  }

  /**
   * Constructor to create a cube with a specific radius.
   *
   * @param radius the size of the cube
   */
  public CubeCreatorUV(float radius) {
    this.radius = radius;
  }

  /**
   * Creates the cube mesh by calling methods to initialize the mesh, create vertices, assign UV
   * coordinates, and create the faces.
   *
   * @return the generated 3D mesh representing the cube
   */
  @Override
  public Mesh3D create() {
    initializeMesh();
    createVertices();
    createUvCoordinates();
    createFaces();
    return mesh;
  }

  /** Initializes a new empty Mesh3D object to store the cube data. */
  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  /**
   * Creates the vertices for the cube based on the specified radius. The vertices are arranged in
   * two sets: top and bottom faces.
   */
  private void createVertices() {
    // Top vertices (y = -radius in Processing coordinate system)
    addVertex(+radius, -radius, -radius); // Vertex 0
    addVertex(+radius, -radius, +radius); // Vertex 1
    addVertex(-radius, -radius, +radius); // Vertex 2
    addVertex(-radius, -radius, -radius); // Vertex 3

    // Bottom vertices (y = +radius in Processing coordinate system)
    addVertex(+radius, +radius, -radius); // Vertex 4
    addVertex(+radius, +radius, +radius); // Vertex 5
    addVertex(-radius, +radius, +radius); // Vertex 6
    addVertex(-radius, +radius, -radius); // Vertex 7
  }

  /**
   * Creates the UV coordinates for the faces of the cube. The coordinates are arranged to map to
   * the following layout:
   *
   * <pre>
   * +--------+--------+--------+--------+
   * |        |  Top   |        |        |
   * +--------+--------+--------+--------+
   * |  Left  |  Front |  Right |  Back  |
   * +--------+--------+--------+--------+
   * |        | Bottom |        |        |
   * +--------+--------+--------+--------+
   * </pre>
   *
   * <p>Note: The `v` values are flipped because the Processing backend uses a texture coordinate
   * system where `v = 0` is at the top and `v = 1` is at the bottom.
   */
  private void createUvCoordinates() {
    // Top
    mesh.addUvCoordinate(0.25f + EPSILON, 1 - 0 - EPSILON);
    mesh.addUvCoordinate(0.50f - EPSILON, 1 - 0 - EPSILON);
    mesh.addUvCoordinate(0.50f - EPSILON, 1 - ONE_THIRD + EPSILON);
    mesh.addUvCoordinate(0.25f + EPSILON, 1 - ONE_THIRD + EPSILON);

    // Bottom
    mesh.addUvCoordinate(0.25f + EPSILON, 1 - TWO_THIRDS - EPSILON);
    mesh.addUvCoordinate(0.50f - EPSILON, 1 - TWO_THIRDS - EPSILON);
    mesh.addUvCoordinate(0.50f - EPSILON, 1 - 1 + EPSILON);
    mesh.addUvCoordinate(0.25f + EPSILON, 1 - 1 + EPSILON);

    // Right
    mesh.addUvCoordinate(0.50f + EPSILON, 1 - ONE_THIRD - EPSILON);
    mesh.addUvCoordinate(0.75f - EPSILON, 1 - ONE_THIRD - EPSILON);
    mesh.addUvCoordinate(0.75f - EPSILON, 1 - TWO_THIRDS + EPSILON);
    mesh.addUvCoordinate(0.50f + EPSILON, 1 - TWO_THIRDS + EPSILON);

    // Front
    mesh.addUvCoordinate(0.25f + EPSILON, 1 - ONE_THIRD - EPSILON);
    mesh.addUvCoordinate(0.50f - EPSILON, 1 - ONE_THIRD - EPSILON);
    mesh.addUvCoordinate(0.50f - EPSILON, 1 - TWO_THIRDS + EPSILON);
    mesh.addUvCoordinate(0.25f + EPSILON, 1 - TWO_THIRDS + EPSILON);

    // Left
    mesh.addUvCoordinate(0.00f + EPSILON, 1 - ONE_THIRD - EPSILON);
    mesh.addUvCoordinate(0.25f - EPSILON, 1 - ONE_THIRD - EPSILON);
    mesh.addUvCoordinate(0.25f - EPSILON, 1 - TWO_THIRDS + EPSILON);
    mesh.addUvCoordinate(0.00f + EPSILON, 1 - TWO_THIRDS + EPSILON);

    // Back
    mesh.addUvCoordinate(0.75f + EPSILON, 1 - ONE_THIRD - EPSILON);
    mesh.addUvCoordinate(1.00f - EPSILON, 1 - ONE_THIRD - EPSILON);
    mesh.addUvCoordinate(1.00f - EPSILON, 1 - TWO_THIRDS + EPSILON);
    mesh.addUvCoordinate(0.75f + EPSILON, 1 - TWO_THIRDS + EPSILON);
  }

  /**
   * Creates the faces of the cube by defining the vertex indices and the corresponding UV
   * coordinate indices for each face.
   */
  private void createFaces() {
    // Top face
    addFace(new int[] {3, 0, 1, 2}, new int[] {0, 1, 2, 3});

    // Bottom face
    addFace(new int[] {6, 5, 4, 7}, new int[] {4, 5, 6, 7});

    // Right face
    addFace(new int[] {1, 0, 4, 5}, new int[] {8, 9, 10, 11});

    // Front face
    addFace(new int[] {1, 5, 6, 2}, new int[] {13, 14, 15, 12});

    // Left face
    addFace(new int[] {6, 7, 3, 2}, new int[] {18, 19, 16, 17});

    // Back face
    addFace(new int[] {3, 7, 4, 0}, new int[] {21, 22, 23, 20});
  }

  /**
   * Adds a vertex to the cube's mesh.
   *
   * @param x the x-coordinate of the vertex
   * @param y the y-coordinate of the vertex
   * @param z the z-coordinate of the vertex
   */
  private void addVertex(float x, float y, float z) {
    mesh.addVertex(x, y, z);
  }

  /**
   * Adds a face to the cube's mesh by specifying the vertex indices and UV coordinate indices.
   *
   * @param vertexIndices the indices of the vertices that form the face
   * @param uvIndices the indices of the UV coordinates for texture mapping
   */
  private void addFace(int[] vertexIndices, int[] uvIndices) {
    mesh.add(new Face3D(vertexIndices, uvIndices));
  }

  /**
   * Gets the radius of the cube.
   *
   * @return the radius of the cube
   */
  public float getRadius() {
    return radius;
  }

  /**
   * Sets the radius of the cube.
   *
   * @param radius the new radius of the cube
   */
  public void setRadius(float radius) {
    this.radius = radius;
  }
}
