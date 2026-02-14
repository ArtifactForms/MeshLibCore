package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mesh.Face3D;
import mesh.Mesh3D;

/**
 * A manifold mesh is a 3D mesh that has a well-defined interior and exterior. It's essentially a
 * "watertight" mesh, free from holes or gaps that would allow the interior to leak.
 *
 * <p>A key characteristic of a manifold mesh is that every edge is shared by exactly two faces.
 */
public class ManifoldTest {

  private final Mesh3D meshUnderTest;

  public ManifoldTest(Mesh3D meshUnderTest) {
    this.meshUnderTest = meshUnderTest;
  }

  public boolean isManifold() {

    if (meshUnderTest.faces.isEmpty()) {
      return false;
    }

    if (meshUnderTest.vertices.size() < 3) {
      return false;
    }

    return eachEdgeHasExactlyTwoAdjacentFaces();
  }

  /** For a mesh to be manifold, every edge must have exactly two adjacent faces. */
  private boolean eachEdgeHasExactlyTwoAdjacentFaces() {

    Map<UndirectedEdge, Integer> edges = new HashMap<>();

    for (Face3D face : meshUnderTest.getFaces()) {

      for (int i = 0; i < face.indices.length; i++) {

        int from = face.indices[i];
        int to = face.indices[(i + 1) % face.indices.length];

        UndirectedEdge edge = new UndirectedEdge(from, to);
        edges.merge(edge, 1, Integer::sum);
      }
    }

    return edges.values().stream().allMatch(count -> count == 2);
  }

  /** Undirected canonical edge used for topology validation. (a,b) is always stored as (min,max) */
  private static class UndirectedEdge {

    private final int a;
    private final int b;

    public UndirectedEdge(int a, int b) {
      this.a = Math.min(a, b);
      this.b = Math.max(a, b);
    }

    @Override
    public int hashCode() {
      return Objects.hash(a, b);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof UndirectedEdge)) return false;
      UndirectedEdge other = (UndirectedEdge) obj;
      return a == other.a && b == other.b;
    }
  }
}
