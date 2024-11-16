package util;

import java.util.HashMap;

import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;

/**
 * A manifold mesh is a 3D mesh that has a well-defined interior and exterior.
 * It's essentially a "watertight" mesh, free from holes or gaps that would
 * allow the interior to leak. A key characteristic of a manifold mesh is that
 * every edge is shared by exactly two faces.
 */
public class ManifoldTest {

    private Mesh3D meshUnderTest;

    public ManifoldTest(Mesh3D meshUnderTest) {
        this.meshUnderTest = meshUnderTest;
    }

    public boolean isManifold() {
        if (meshUnderTest.faces.isEmpty())
            return false;
        
        if (meshUnderTest.vertices.size() < 3) {
            return false;
        }
        
        return eachEdgeHasExactlyTwoAdjacentFaces();
    }

    /**
     * For a mesh to be manifold, every edge must have exactly two adjacent
     * faces.
     * 
     * @return
     */
    private boolean eachEdgeHasExactlyTwoAdjacentFaces() {
        HashMap<Edge3D, Integer> edges = new HashMap<Edge3D, Integer>();

        for (Face3D face : meshUnderTest.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                int fromIndex = face.indices[i];
                int toIndex = face.indices[(i + 1) % face.indices.length];

                Edge3D edge = new Edge3D(fromIndex, toIndex);
                Edge3D pair = edge.createPair();

                if (edges.containsKey(pair)) {
                    edges.put(pair, edges.get(pair) + 1);
                } else {
                    edges.put(edge, 1);
                }
            }
        }

        for (Edge3D edge : edges.keySet()) {
            Integer adjacentFacesCount = edges.get(edge);
            if (adjacentFacesCount != 2)
                return false;
        }

        return true;
    }

}
