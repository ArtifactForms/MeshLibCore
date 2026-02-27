package mesh.analysis;

import mesh.FaceView;
import mesh.Mesh;

import java.util.HashSet;
import java.util.Set;

public final class MeshAnalysis {

    private MeshAnalysis() {}

    public static int edgeCount(Mesh mesh) {
        record Edge(int a, int b) {}

        Set<Edge> edges = new HashSet<>();

        for (int fIdx = 0; fIdx < mesh.getFaceCount(); fIdx++) {
            FaceView face = mesh.getFaceAt(fIdx);
            int n = face.getVertexCount();

            for (int vIdx = 0; vIdx < n; vIdx++) {
                int v0 = face.getIndexAt(vIdx);
                int v1 = face.getIndexAt((vIdx + 1) % n);

                int a = Math.min(v0, v1);
                int b = Math.max(v0, v1);

                edges.add(new Edge(a, b));
            }
        }

        return edges.size();
    }
}