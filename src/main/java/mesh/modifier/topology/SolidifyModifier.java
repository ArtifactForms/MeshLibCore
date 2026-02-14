package mesh.modifier.topology;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.FaceBridging;
import mesh.util.VertexNormals;

/**
 * A modifier that solidifies a 3D mesh by creating an inner mesh offset along vertex normals, and
 * bridging the edges between the original and inner meshes.
 *
 * <p>This modifier is commonly used in modeling workflows to add thickness to 2D surfaces or thin
 * meshes.
 *
 * <pre>
 * Workflow:
 * 1. Creates a copy of the input mesh as the inner mesh.
 * 2. Offsets the inner mesh vertices along their normals by the
 *    specified thickness.
 * 3. Reverses the face directions of the inner mesh to ensure proper normals.
 * 4. Bridges the edges between the original and inner mesh to create a
 *    closed solid.
 * </pre>
 */
public class SolidifyModifier implements IMeshModifier {

  /** The thickness to apply when solidifying the mesh. */
  private float thickness;

  /** The original mesh to modify. */
  private Mesh3D mesh;

  /** The inner mesh created by offsetting the original mesh. */
  private Mesh3D innerMesh;

  /** The vertex normals of the original mesh. */
  private List<Vector3f> vertexNormals;

  /** The edges of the original mesh. */
  private HashSet<Edge3D> edges;

  /** The faces of the original mesh before modifications. */
  private List<Face3D> originalFaces;

  /** Creates a new SolidifyModifier with the default thickness of 0.01. */
  public SolidifyModifier() {
    this(0.01f);
  }

  /**
   * Creates a new SolidifyModifier with the specified thickness.
   *
   * @param thickness The thickness to apply when solidifying the mesh.
   * @throws IllegalArgumentException If the thickness is negative.
   */
  public SolidifyModifier(float thickness) {
    this.thickness = thickness;
  }

  /**
   * Modifies the given mesh by solidifying it.
   *
   * @param mesh The mesh to modify.
   * @return The solidified mesh.
   * @throws IllegalArgumentException If the mesh is null.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    setMesh(mesh);
    validateMesh();

    if (canExitEarly()) {
      return mesh;
    }

    initialize();
    mapEdges();
    createInnerMesh();
    bridgeHoles();

    return mesh;
  }

  /**
   * Creates the inner mesh by copying the original mesh, flipping its faces, and offsetting its
   * vertices along their normals.
   */
  private void createInnerMesh() {
    initializeInnerMesh();
    appendInnerMesh();
    flipDirectionOfInnerMesh();
    moveInnerMeshAlongVertexNormals();
  }

  /** Appends the inner mesh to the original mesh. */
  private void appendInnerMesh() {
    mesh.append(innerMesh);
  }

  /** Initializes data structures required for the modification process. */
  private void initialize() {
    initializeOriginalFaces();
    initializeEdgeMap();
    createVertexNormals();
  }

  /**
   * Bridges the gaps (holes) between the original and inner meshes by creating faces along unshared
   * edges.
   */
  private void bridgeHoles() {
    for (Face3D face : originalFaces) {
      int size = face.indices.length;
      for (int i = 0; i < size; i++) {
        Edge3D forwardEdge = createEdgeAt(face, i);
        Edge3D reverseEdge = forwardEdge.createPair();
        if (!edges.contains(reverseEdge)) {
          bridgeHole(forwardEdge);
        }
      }
    }
  }

  /**
   * Bridges a single hole between the original and inner meshes for the given edge.
   *
   * @param forwardEdge The edge of the face to bridge.
   */
  private void bridgeHole(Edge3D forwardEdge) {
    Vector3f v0 = innerMesh.getVertexAt(forwardEdge.getFromIndex());
    Vector3f v1 = innerMesh.getVertexAt(forwardEdge.getToIndex());
    Vector3f v2 = mesh.getVertexAt(forwardEdge.getFromIndex());
    Vector3f v3 = mesh.getVertexAt(forwardEdge.getToIndex());
    FaceBridging.bridge(mesh, v0, v1, v2, v3);
  }

  /** Maps all edges of the mesh and stores them in a hash set. */
  private void mapEdges() {
    for (Face3D face : mesh.faces) {
      for (int i = 0; i < face.indices.length; i++) {
        edges.add(createEdgeAt(face, i));
      }
    }
  }

  /**
   * Creates an edge for the specified face at the given index.
   *
   * @param face The face containing the edge.
   * @param i The index of the edge in the face.
   * @return The edge created at the specified index.
   */
  private Edge3D createEdgeAt(Face3D face, int i) {
    int fromIndex = face.indices[i];
    int toIndex = face.indices[(i + 1) % face.indices.length];
    return new Edge3D(fromIndex, toIndex);
  }

  /** Moves the vertices of the inner mesh along their normals by the specified thickness. */
  private void moveInnerMeshAlongVertexNormals() {
    IntStream.range(0, innerMesh.vertices.size())
        .parallel()
        .forEach(
            i -> {
              Vector3f vertex = innerMesh.getVertexAt(i);
              Vector3f normal = vertexNormals.get(i);
              vertex.set(normal.mult(-thickness).add(vertex));
            });
  }

  /** Reverses the direction of the faces in the inner mesh. */
  private void flipDirectionOfInnerMesh() {
    new FlipFacesModifier().modify(innerMesh);
  }

  /**
   * Validates that the provided mesh is not null.
   *
   * @throws IllegalArgumentException If the mesh is null.
   */
  private void validateMesh() {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
  }

  /**
   * Determines if the modification process can be skipped due to trivial cases such as zero
   * thickness or an empty mesh.
   *
   * @return True if the modification can be skipped, false otherwise.
   */
  private boolean canExitEarly() {
    return (thickness == 0 || mesh.vertices.isEmpty() || mesh.faces.isEmpty());
  }

  /** Initializes the inner mesh by creating a copy of the original mesh. */
  private void initializeInnerMesh() {
    innerMesh = mesh.copy();
  }

  /** Initializes the edge map for tracking unique edges in the mesh. */
  private void initializeEdgeMap() {
    edges = new HashSet<>();
  }

  /** Initializes the list of original faces of the mesh. */
  private void initializeOriginalFaces() {
    originalFaces = mesh.getFaces(0, mesh.getFaceCount());
  }

  /** Computes the vertex normals for the mesh. */
  private void createVertexNormals() {
    vertexNormals = new VertexNormals(mesh).getVertexNormals();
  }

  /**
   * Sets the mesh to be modified.
   *
   * @param mesh The mesh to set.
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  /**
   * Retrieves the thickness of the solidification process.
   *
   * @return The thickness value.
   */
  public float getThickness() {
    return thickness;
  }

  /**
   * Sets the thickness for the solidification process.
   *
   * @param thickness The thickness to set.
   * @throws IllegalArgumentException If the thickness is negative.
   */
  public void setThickness(float thickness) {
    if (thickness < 0) {
      throw new IllegalArgumentException("Thickness cannot be negative.");
    }
    this.thickness = thickness;
  }
}
