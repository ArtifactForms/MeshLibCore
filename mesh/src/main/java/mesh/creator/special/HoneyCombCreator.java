package mesh.creator.special;

import java.util.HashMap;
import java.util.Map;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.transform.CenterAtModifier;

/**
 * Procedural creator for a 3D Honeycomb (hexagonal) grid. This creator generates a hollowed-out
 * hexagonal structure. It handles complex topology logic to ensure that adjacent cells share
 * vertices and that internal walls between cells are omitted, resulting in a "manifold"
 * (watertight) mesh.
 */
public class HoneyCombCreator implements IMeshCreator {

  // --- Configuration Fields ---
  private int rowCount = 2;
  private int colCount = 2;
  private float cellRadius = 0.5f;
  private float innerScale = 0.9f;
  private float height = 0.2f;

  // --- State Management ---
  private Mesh3D mesh;

  /** Precision for vertex merging; prevents floating point gaps. */
  private static final int DECIMALS = 4;

  private static final float FACTOR = Mathf.pow(10, DECIMALS);

  /** Lookup for shared vertices on the top plane. */
  private final Map<VertexKey, Integer> outerTopVertexMap = new HashMap<>();

  /** Lookup for shared vertices on the bottom plane. */
  private final Map<VertexKey, Integer> outerBottomVertexMap = new HashMap<>();

  /** Tracks edge usage to identify boundary vs. internal edges. */
  private final Map<EdgeKey, DirectedEdge> boundaryEdges = new HashMap<>();

  // -------------------------------------------------------------------------
  // Execution Entry Point
  // -------------------------------------------------------------------------

  /**
   * Creates the Honeycomb Mesh based on current settings.
   *
   * @return A centered Mesh3D object representing the honeycomb.
   */
  @Override
  public Mesh3D create() {
    mesh = new Mesh3D();

    // Reset state for new generation
    outerTopVertexMap.clear();
    outerBottomVertexMap.clear();
    boundaryEdges.clear();

    createSegments();

    // Only create side walls if the mesh has thickness
    if (height != 0f) {
      createOuterWalls();
    }

    centerAtOrigin();
    return mesh;
  }

  // -------------------------------------------------------------------------
  // Geometry Generation Logic
  // -------------------------------------------------------------------------

  /**
   * Calculates the grid layout and iterates through cells. Uses an 'odd-row' offset to interlock
   * the hexagons.
   */
  private void createSegments() {
    // ri = inner radius (apothem) of the hexagon
    float ri = Mathf.sqrt(3) * 0.5f;
    float width = ri * cellRadius * 2f;
    float depth = cellRadius * 2f;

    for (int col = 0; col < colCount; col++) {
      for (int row = 0; row < rowCount; row++) {
        // Shift every odd row by half a width to create interlocking effect
        float tx = (row & 1) == 1 ? col * width + width * 0.5f : col * width;
        // Hexagons overlap by 0.5 radius vertically in a grid
        float tz = row * (depth - cellRadius * 0.5f);

        createHexCell(tx, tz);
      }
    }
  }

  /**
   * Constructs the geometry for a single hexagonal cell. Generates top/bottom faces and the
   * vertical inner cavity. *
   */
  private void createHexCell(float tx, float tz) {
    float step = Mathf.TWO_PI / 6f;

    int[] outerTop = new int[6];
    int[] innerTop = new int[6];
    int[] outerBottom = new int[6];
    int[] innerBottom = new int[6];

    float yTop = -height * 0.5f;
    float yBottom = height * 0.5f;
    boolean hasHeight = height != 0f;

    for (int i = 0; i < 6; i++) {
      // HALF_PI rotates the hex so it's "pointy-topped"
      float angle = step * i + Mathf.HALF_PI;
      float cos = Mathf.cos(angle);
      float sin = Mathf.sin(angle);

      // Outer ring coordinates
      float ox = tx + cellRadius * cos;
      float oz = tz + cellRadius * sin;

      // Inner ring coordinates (the hole)
      float ix = tx + cellRadius * innerScale * cos;
      float iz = tz + cellRadius * innerScale * sin;

      // Manage vertex sharing for outer boundary
      outerTop[i] = getOrCreateOuterTopVertex(ox, oz, yTop);
      innerTop[i] = mesh.addVertex(ix, yTop, iz);

      if (hasHeight) {
        outerBottom[i] = getOrCreateOuterBottomVertex(ox, oz, yBottom);
        innerBottom[i] = mesh.addVertex(ix, yBottom, iz);
      }
    }

    // Create the top horizontal face (annulus)
    createInsetFaces(outerTop, innerTop);

    if (hasHeight) {
      // Create the bottom horizontal face (flipped winding order for normal direction)
      createInsetFaces(innerBottom, outerBottom);
      // Create the vertical walls inside the hole
      createInnerWalls(innerTop, innerBottom);
      // Mark the outer edges to determine where side walls are needed
      registerBoundaryEdges(outerTop);
    }
  }

  /** Creates faces between an outer ring and an inner ring of vertices. */
  private void createInsetFaces(int[] outer, int[] inner) {
    for (int i = 0; i < 6; i++) {
      int next = (i + 1) % 6;
      mesh.addFace(outer[i], outer[next], inner[next], inner[i]);
    }
  }

  /** Creates vertical faces connecting the top and bottom inner rings. */
  private void createInnerWalls(int[] top, int[] bottom) {
    for (int i = 0; i < 6; i++) {
      int next = (i + 1) % 6;
      mesh.addFace(top[i], top[next], bottom[next], bottom[i]);
    }
  }

  // -------------------------------------------------------------------------
  // Perimeter and Wall Management
  // -------------------------------------------------------------------------

  /**
   * Records edges and counts their occurrences. Shared edges between cells will be registered
   * twice, while perimeter edges are registered only once.
   */
  private void registerBoundaryEdges(int[] ring) {
    for (int i = 0; i < ring.length; i++) {
      int a = ring[i];
      int b = ring[(i + 1) % ring.length];

      EdgeKey key = new EdgeKey(a, b);
      DirectedEdge edge = boundaryEdges.get(key);

      if (edge == null) {
        boundaryEdges.put(key, new DirectedEdge(a, b));
      } else {
        edge.count++;
      }
    }
  }

  /**
   * Creates vertical walls on the outside of the honeycomb. Skips edges where two cells touch
   * (count != 1) to keep the interior hollow.
   */
  private void createOuterWalls() {
    float yBottom = height * 0.5f;

    for (DirectedEdge edge : boundaryEdges.values()) {
      // Only edges on the mesh perimeter have a count of 1
      if (edge.count != 1) continue;

      int topA = edge.a;
      int topB = edge.b;

      Vector3f a = mesh.getVertexAt(topA);
      Vector3f b = mesh.getVertexAt(topB);

      // Find or create the corresponding bottom vertices for the wall
      int botA = getOrCreateOuterBottomVertex(a.x, a.z, yBottom);
      int botB = getOrCreateOuterBottomVertex(b.x, b.z, yBottom);

      mesh.addFace(topA, botA, botB, topB);
    }
  }

  // -------------------------------------------------------------------------
  // Vertex Management & Quantization
  // -------------------------------------------------------------------------

  private int getOrCreateOuterTopVertex(float x, float z, float y) {
    return getOrCreateVertex(outerTopVertexMap, x, z, y);
  }

  private int getOrCreateOuterBottomVertex(float x, float z, float y) {
    return getOrCreateVertex(outerBottomVertexMap, x, z, y);
  }

  /**
   * Ensures vertex reuse. If a vertex at (x, z) already exists in the provided map, its index is
   * returned. Otherwise, a new vertex is added to the mesh.
   */
  private int getOrCreateVertex(Map<VertexKey, Integer> map, float x, float z, float y) {
    float qx = quantize(x);
    float qz = quantize(z);

    VertexKey key = new VertexKey(qx, qz);
    Integer index = map.get(key);

    if (index != null) {
      return index;
    }

    int newIndex = mesh.addVertex(qx, y, qz);
    map.put(key, newIndex);
    return newIndex;
  }

  /** Rounds a value to the specified DECIMALS to avoid floating point floating errors. */
  private static float quantize(float v) {
    return Mathf.round(v * FACTOR) / FACTOR;
  }

  private void centerAtOrigin() {
    new CenterAtModifier().modify(mesh);
  }

  // -------------------------------------------------------------------------
  // Helper Data Structures
  // -------------------------------------------------------------------------

  /** Represents a directed edge between two vertices. */
  private static final class DirectedEdge {
    final int a;
    final int b;
    int count = 1;

    DirectedEdge(int a, int b) {
      this.a = a;
      this.b = b;
    }
  }

  /**
   * * Immutable key representing an edge between two vertex indices. Orders indices (a < b) so that
   * direction doesn't affect equality.
   */
  private static final class EdgeKey {
    final int a;
    final int b;

    EdgeKey(int v1, int v2) {
      if (v1 < v2) {
        a = v1;
        b = v2;
      } else {
        a = v2;
        b = v1;
      }
    }

    @Override
    public int hashCode() {
      return 31 * a + b;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof EdgeKey)) return false;
      EdgeKey e = (EdgeKey) o;
      return a == e.a && b == e.b;
    }
  }

  /** Key based on XZ coordinates for vertex merging. */
  private static final class VertexKey {
    final float x;
    final float z;

    VertexKey(float x, float z) {
      this.x = x;
      this.z = z;
    }

    @Override
    public int hashCode() {
      return 31 * Float.floatToIntBits(x) + Float.floatToIntBits(z);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof VertexKey)) return false;
      VertexKey v = (VertexKey) o;
      return Float.compare(v.x, x) == 0 && Float.compare(v.z, z) == 0;
    }
  }

  // -------------------------------------------------------------------------
  // Setters for Configuration
  // -------------------------------------------------------------------------

  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }

  public void setColCount(int colCount) {
    this.colCount = colCount;
  }

  public void setCellRadius(float cellRadius) {
    this.cellRadius = cellRadius;
  }

  public void setInnerScale(float innerScale) {
    this.innerScale = innerScale;
  }

  public void setHeight(float height) {
    this.height = height;
  }
}
