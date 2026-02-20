package mesh.modifier.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.TraverseHelper;

/**
 * This class modifies a 3D mesh by beveling its vertices.
 *
 * <p>The beveling process creates new vertices along the edges of the mesh and generates new faces
 * to connect these vertices, resulting in a chamfered effect.
 *
 * <p>This implementation supports basic vertex beveling with a customizable bevel amount.
 */
public class BevelVerticesModifier implements IMeshModifier {

  /**
   * The default bevel amount used if no custom amount is specified during instantiation. This
   * determines the default intensity of the bevel effect applied to the mesh edges.
   */
  private static final float DEFAULT_AMOUNT = 0.1f;

  /**
   * The amount of bevel to apply to the vertices and edges of the mesh. This value defines how much
   * the edges are chamfered.
   */
  private float amount;

  /** The 3D mesh to be modified by this class. */
  private Mesh3D mesh;

  /** A list of new vertices generated during the beveling process. */
  private List<Vector3f> verticesToAdd;

  /** A list of new faces to be added to the mesh to form the bevel effect. */
  private List<Face3D> facesToAdd;

  /**
   * A map connecting edges to their corresponding new beveled vertex indices. This is used to
   * efficiently track which new vertices are associated with specific edges, ensuring the mesh
   * geometry is modified correctly.
   */
  private Map<Edge3D, Integer> edgeToEdgePointIndex;

  /**
   * A map that tracks unique vertices (edge points) to their corresponding index. This ensures that
   * vertices are reused rather than duplicated during the beveling process, maintaining the mesh's
   * geometric integrity and avoiding unnecessary computations.
   */
  private Map<Vector3f, Integer> vertexIndexMap;

  /** Default constructor with a predefined bevel amount of 0.1. */
  public BevelVerticesModifier() {
    this(DEFAULT_AMOUNT);
  }

  /**
   * Constructs a new BevelVerticesModifier with a custom bevel amount.
   *
   * @param amount The amount to bevel. Must be positive.
   * @throws IllegalArgumentException if the bevel amount is less than or equal to zero.
   */
  public BevelVerticesModifier(float amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Bevel amount must be positive.");
    }
    this.amount = amount;
    this.verticesToAdd = new ArrayList<>();
    this.facesToAdd = new ArrayList<>();
    this.edgeToEdgePointIndex = new HashMap<>();
    this.vertexIndexMap = new HashMap<>();
  }

  /**
   * Modifies the given mesh by applying the beveling workflow.
   *
   * @param mesh The 3D mesh to be modified.
   * @return The modified mesh after applying the bevel.
   * @throws IllegalArgumentException if the mesh is null.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    modifyWorkflow(mesh);
    return mesh;
  }

  /**
   * Runs the entire beveling workflow, including edge point generation, face creation, clearing old
   * geometry, and adding new geometry.
   *
   * @param mesh The 3D mesh to modify during the workflow.
   */
  private void modifyWorkflow(Mesh3D mesh) {
    clear();
    setMesh(mesh);
    generateEdgePointsForFaces();
    createFacesAroundVertices();
    removeOldFacesAndVertices();
    addNewVerticesAndFaces();
  }

  /** Generates edge points for all faces in the mesh. */
  private void generateEdgePointsForFaces() {
    for (Face3D face : mesh.getFaces()) {
      generateEdgePointsForFace(face);
    }
  }

  /**
   * Generates edge points for a single face and maps them for further processing.
   *
   * @param face The face to process edge points for.
   */
  private void generateEdgePointsForFace(Face3D face) {
    int[] indices = new int[face.indices.length * 2];
    for (int i = 0; i < face.indices.length; i++) {
      int index = i * 2;
      Vector3f from = getVertexForIndexAt(face, i);
      Vector3f to = getVertexForIndexAt(face, i + 1);

      indices[index] = addEdgePoint(calculateEdgePoint(to, from));
      indices[index + 1] = addEdgePoint(calculateEdgePoint(from, to));

      mapEdgeToEdgePointIndex(face, i, indices[index]);
    }
    addFace(indices);
  }

  /**
   * Maps edge points to their corresponding indices for quick lookup.
   *
   * @param face The face associated with the edge.
   * @param i The index of the edge in the face.
   * @param index The computed index for the edge.
   */
  private void mapEdgeToEdgePointIndex(Face3D face, int i, int index) {
    Edge3D edge = new Edge3D(getIndexAt(face, i), getIndexAt(face, i + 1));
    edgeToEdgePointIndex.put(edge, index);
  }

  /** Creates new faces by connecting edge points around vertices in the mesh. */
  private void createFacesAroundVertices() {
    TraverseHelper helper = new TraverseHelper(mesh);
    for (int i = 0; i < mesh.getVertexCount(); i++) {
      List<Integer> indices = collectEdgePointsAroundVertex(helper, i);
      if (!indices.isEmpty()) {
        facesToAdd.add(new Face3D(toReverseArray(indices)));
      }
    }
  }

  /**
   * Collects edge points surrounding a specific vertex using traversal logic.
   *
   * @param helper The traversal helper instance.
   * @param vertexIndex The index of the vertex to process.
   * @return A list of edge indices surrounding the vertex.
   */
  private List<Integer> collectEdgePointsAroundVertex(TraverseHelper helper, int vertexIndex) {
    Edge3D outgoingEdge = helper.getOutgoing(vertexIndex);
    Edge3D edge = outgoingEdge;
    List<Integer> indices = new ArrayList<>();

    do {
      int index = edgeToEdgePointIndex.get(edge);
      indices.add(index);
      edge = helper.getPairNext(edge.getFromIndex(), edge.getToIndex());
    } while (!outgoingEdge.equals(edge));

    return indices;
  }

  /**
   * Adds a new edge point to the list or retrieves the index if it already exists.
   *
   * @param edgePoint The edge point to add.
   * @return The index of the edge point.
   */
  private int addEdgePoint(Vector3f edgePoint) {
    if (!vertexIndexMap.containsKey(edgePoint)) {
      vertexIndexMap.put(edgePoint, verticesToAdd.size());
      verticesToAdd.add(edgePoint);
    }
    return vertexIndexMap.get(edgePoint);
  }

  /** Clears out old faces and vertices from the mesh. */
  private void removeOldFacesAndVertices() {
    mesh.clearFaces();
    mesh.clearVertices();
  }

  /** Adds newly computed vertices and faces to the mesh. */
  private void addNewVerticesAndFaces() {
    mesh.addVertices(verticesToAdd);
    mesh.addFaces(facesToAdd);
  }

  /**
   * Adds a new face to the list of faces to be added to the mesh.
   *
   * @param indices The indices defining the new face.
   */
  private void addFace(int[] indices) {
    facesToAdd.add(new Face3D(indices));
  }

  /**
   * Calculates a new edge point based on a linear interpolation with the given bevel amount.
   *
   * @param from The starting vertex.
   * @param to The ending vertex.
   * @return The calculated edge point.
   */
  private Vector3f calculateEdgePoint(Vector3f from, Vector3f to) {
    return from.subtract(to).mult(amount).add(to);
  }

  /**
   * Retrieves the index of the vertex at the specified position in the face's indices list, with
   * support for circular indexing by wrapping around when necessary.
   *
   * @param face The face from which to retrieve the index. Must not be null.
   * @param i The position of the index to retrieve within the face's indices.
   * @return The vertex index at the specified position, with wrapping support.
   */
  private int getIndexAt(Face3D face, int i) {
    return face.indices[i % face.indices.length];
  }

  /**
   * Retrieves the actual 3D vertex (as a Vector3f) associated with the specified index in the
   * face's indices list. This maps the index to the corresponding vertex in the provided mesh.
   *
   * @param face The face whose index to map to a vertex. Must not be null.
   * @param i The index position within the face's indices.
   * @return The 3D vertex (Vector3f) corresponding to the index.
   */
  private Vector3f getVertexForIndexAt(Face3D face, int i) {
    int index = getIndexAt(face, i);
    return mesh.getVertexAt(index);
  }

  /**
   * Reverses the order of integers in the provided list and converts it to an array. This is used
   * to reverse traversal or ordering logic in certain geometric calculations.
   *
   * @param values The list of integer indices to reverse.
   * @return A new integer array with the order of elements reversed compared to the input list.
   */
  private int[] toReverseArray(List<Integer> values) {
    int[] a = new int[values.size()];
    for (int j = 0; j < a.length; j++) {
      a[j] = values.get(values.size() - 1 - j);
    }
    return a;
  }

  /** Clears the old data structures related to the beveling process. */
  private void clear() {
    verticesToAdd.clear();
    facesToAdd.clear();
    edgeToEdgePointIndex.clear();
    vertexIndexMap.clear();
  }

  /**
   * Sets the mesh to be modified during the workflow.
   *
   * @param mesh The mesh to work on.
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }
}
