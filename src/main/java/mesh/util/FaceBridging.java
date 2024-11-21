package mesh.util;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class FaceBridging {

    /**
     * FIXME: This method has high performance issues. The primary performance
     * issue with the bridge method lies in the use of indexOf to find vertex
     * indices within the mesh.vertices list. This operation has a time
     * complexity of O(n), where n is the number of vertices in the mesh.
     * 
     * <pre>
     * Improving Performance
     * 
     * To optimize the bridge method, we can consider the following approach:
     * 
     * 1. Vertex Hash Map:
     * 
     * Create a HashMap<Vector3f, Integer> to map vertices to their indices. 
     * When calling bridge, use the hash map to quickly retrieve indices,
     * avoiding linear searches.
     * </pre>
     */
    public static void bridge(Mesh3D mesh, Vector3f v0, Vector3f v1,
            Vector3f v2, Vector3f v3) {
        int idx0 = mesh.vertices.indexOf(v0);
        int idx1 = mesh.vertices.indexOf(v1);
        int idx2 = mesh.vertices.indexOf(v2);
        int idx3 = mesh.vertices.indexOf(v3);
        Face3D face = new Face3D(idx0, idx1, idx3, idx2);
        mesh.faces.add(face);
    }

    public static void bridge(Mesh3D mesh, Face3D f0, Face3D f1) {
        Face3D f2 = new Face3D(
                f0.indices[0], f0.indices[1], f1.indices[1], f1.indices[0]
        );
        Face3D f3 = new Face3D(
                f0.indices[1], f0.indices[2], f1.indices[2], f1.indices[1]
        );
        Face3D f4 = new Face3D(
                f0.indices[2], f0.indices[3], f1.indices[3], f1.indices[2]
        );
        Face3D f5 = new Face3D(
                f0.indices[3], f0.indices[0], f1.indices[0], f1.indices[3]
        );
        mesh.faces.add(f2);
        mesh.faces.add(f3);
        mesh.faces.add(f4);
        mesh.faces.add(f5);
    }

}
