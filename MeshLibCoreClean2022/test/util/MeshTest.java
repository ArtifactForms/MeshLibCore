package util;

import java.util.HashSet;
import java.util.List;

import org.junit.Assert;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.test.manifold.ManifoldTest;
import mesh.util.TraverseHelper;

public class MeshTest {

    /**
     * Running this test is very time expensive!
     * 
     * Checks if the given mesh has any duplicated faces.
     * 
     * This method iterates over all pairs of faces in the mesh and compares
     * their indices. If two faces share the same indices, they are considered
     * duplicates.
     * 
     * **Time Complexity:** O(N^2), where N is the number of faces in the mesh.
     * This quadratic time complexity arises from the nested loop structure,
     * which can become inefficient for large meshes.
     * 
     * @param mesh The 3D mesh to check.
     * @return `true` if the mesh has no duplicated faces, `false` otherwise.
     */
    public static boolean meshHasNoDuplicatedFaces(Mesh3D mesh) {
        int duplicatedFaces = 0;

        for (Face3D face0 : mesh.getFaces()) {
            for (Face3D face1 : mesh.getFaces()) {
                if (face0 != face1 && face0.sharesSameIndices(face1))
                    duplicatedFaces++;
            }
        }
        return duplicatedFaces == 0;
    }

    public static boolean meshHasNoLooseVertices(Mesh3D mesh) {
        List<Vector3f> vertices = mesh.getVertices();

        for (Face3D face : mesh.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = mesh.getVertexAt(face.indices[i]);
                vertices.remove(v);
            }
        }
        return vertices.isEmpty();
    }

    public static boolean normalsPointOutwards(Mesh3D mesh) {
        Vector3f center = new Vector3f();
        for (Face3D face : mesh.getFaces()) {
            Vector3f faceNormal = mesh.calculateFaceNormal(face);
            Vector3f faceCenter = mesh.calculateFaceCenter(face);
            Vector3f a = faceCenter.subtract(center);
            float dotProduct = faceNormal.dot(a);
            if (dotProduct < 0) {
                return false;
            }
        }
        return true;
    }

    public static int calculateEdgeCount(Mesh3D mesh) {
        HashSet<Edge3D> edges = new HashSet<Edge3D>();

        for (Face3D face : mesh.faces) {
            for (int i = 0; i < face.indices.length; i++) {
                int fromIndex = face.indices[i];
                int toIndex = face.indices[(i + 1) % face.indices.length];
                if (fromIndex == toIndex)
                    continue;
                if (fromIndex < 0 || toIndex < 0)
                    continue;
                Edge3D edge = new Edge3D(fromIndex, toIndex);
                Edge3D pair = edge.createPair();
                if (!edges.contains(pair))
                    edges.add(edge);
            }
        }
        return edges.size();
    }

    public static boolean fulfillsEulerCharacteristic(Mesh3D mesh) {
        int edgeCount = MeshTest.calculateEdgeCount(mesh);
        int faceCount = mesh.getFaceCount();
        int vertexCount = mesh.getVertexCount();
        int actual = vertexCount - edgeCount + faceCount;
        return actual == 2;
    }

    public static void assertMeshHasEdgesWithLengthOf(Mesh3D mesh,
            int expectedEdgeCount, float expectedLength) {
        int actualEdgeCount = 0;

        TraverseHelper helper = new TraverseHelper(mesh);
        List<Edge3D> edges = helper.getAllEdges();
        for (Edge3D edge : edges) {
            Vector3f from = mesh.getVertexAt(edge.fromIndex);
            Vector3f to = mesh.getVertexAt(edge.toIndex);
            float edgeLength = from.distance(to);
            if (edgeLength == expectedLength)
                actualEdgeCount++;
        }

        if (expectedEdgeCount != actualEdgeCount) {
            throw new AssertionError("Expected " + expectedEdgeCount
                    + " edges with length " + expectedLength + ", but found "
                    + actualEdgeCount);
        }
    }

    public static void assertEdgeCountEquals(Mesh3D mesh,
            int expectedEdgeCount) {
        Assert.assertEquals(calculateEdgeCount(mesh), expectedEdgeCount);
    }

    public static void assertEveryEdgeHasALengthOf(Mesh3D mesh,
            float expectedEdgeLength, float delta) {
        for (Face3D face : mesh.faces) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v0 = mesh.vertices.get(face.indices[i]);
                Vector3f v1 = mesh.vertices
                        .get(face.indices[(i + 1) % face.indices.length]);
                Assert.assertEquals(expectedEdgeLength, v0.distance(v1), delta);
            }
        }
    }

    public static void assertFaceCountEquals(Mesh3D mesh,
            int expectedFaceCount) {
        Assert.assertEquals(expectedFaceCount, mesh.getFaceCount());
        Assert.assertEquals(expectedFaceCount, mesh.faces.size());
        Assert.assertEquals(expectedFaceCount, mesh.getFaces().size());
        Assert.assertEquals(expectedFaceCount,
                mesh.getFaces(0, mesh.getFaceCount()).size());
    }

    public static void assertVertexCountEquals(Mesh3D mesh,
            int expectedVertexCount) {
        Assert.assertEquals(expectedVertexCount, mesh.getVertexCount());
        Assert.assertEquals(expectedVertexCount, mesh.vertices.size());
        Assert.assertEquals(expectedVertexCount, mesh.getVertices().size());
        Assert.assertEquals(expectedVertexCount,
                mesh.getVertices(0, mesh.getVertexCount()).size());
    }

    private static boolean hasCorrectNumberOfFacesWithVertexCount(Mesh3D mesh,
            int vertices, int expected) {
        int count = 0;
        for (Face3D face : mesh.faces) {
            if (face.getVertexCount() == vertices)
                count++;
        }
        return count == expected;
    }

    public static boolean isTriangleCountEquals(Mesh3D mesh,
            int expectedTriangleCount) {
        return hasCorrectNumberOfFacesWithVertexCount(mesh, 3,
                expectedTriangleCount);
    }

    public static boolean isQuadCountEquals(Mesh3D mesh,
            int expectedQuadCount) {
        return hasCorrectNumberOfFacesWithVertexCount(mesh, 4,
                expectedQuadCount);
    }

    public static boolean isHexagonCountEquals(Mesh3D mesh,
            int expectedHexagonCount) {
        return hasCorrectNumberOfFacesWithVertexCount(mesh, 6,
                expectedHexagonCount);
    }

    public static boolean isPentagonCountEquals(Mesh3D mesh,
            int expectedPentagonCount) {
        return hasCorrectNumberOfFacesWithVertexCount(mesh, 5,
                expectedPentagonCount);
    }

    public static boolean isOctagonCountEquals(Mesh3D mesh,
            int expectedOctagonCount) {
        return hasCorrectNumberOfFacesWithVertexCount(mesh, 8,
                expectedOctagonCount);
    }

    public static boolean isDecagonCountEquals(Mesh3D mesh,
            int expectedDecagonCount) {
        return hasCorrectNumberOfFacesWithVertexCount(mesh, 10,
                expectedDecagonCount);
    }

    public static boolean isManifold(Mesh3D mesh) {
        return new ManifoldTest(mesh).isManifold();
    }

    public static void assertFaceContainsVertexIndex(Face3D face,
            int expectedVertexIndex) {
        int count = 0;
        for (int i = 0; i < face.indices.length; i++)
            count += face.indices[i] == expectedVertexIndex ? 1 : 0;
        Assert.assertEquals(1, count);
    }

}
