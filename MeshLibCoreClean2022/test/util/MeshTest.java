package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;

import org.junit.Assert;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.test.manifold.ManifoldTest;
import mesh.selection.FaceSelection;
import mesh.util.TraverseHelper;

public class MeshTest {

    public static void assertMeshHasEdgesWithLengthOf(Mesh3D mesh, int expectedEdgeCount, float expectedLength) {
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

        assertEquals(expectedEdgeCount, actualEdgeCount);
    }

    public static void assertNormalsPointOutwards(Mesh3D mesh) {
        Vector3f center = new Vector3f();
        for (Face3D face : mesh.getFaces()) {
            Vector3f faceNormal = mesh.calculateFaceNormal(face);
            Vector3f faceCenter = mesh.calculateFaceCenter(face);
            Vector3f a = faceCenter.subtract(center);
            float dotProduct = faceNormal.dot(a);
            Assert.assertTrue(dotProduct >= 0);
        }
    }

    public static void assertMeshHasNoDuplicatedFaces(Mesh3D mesh) {
        int duplicatedFaces = 0;

        for (Face3D face0 : mesh.getFaces()) {
            for (Face3D face1 : mesh.getFaces()) {
                if (face0 != face1 && face0.sharesSameIndices(face1))
                    duplicatedFaces++;
            }
        }

        Assert.assertEquals(0, duplicatedFaces);
    }

    public static void assertMeshHasNoLooseVertices(Mesh3D mesh) {
        List<Vector3f> vertices = mesh.getVertices();

        for (Face3D face : mesh.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = mesh.getVertexAt(face.indices[i]);
                vertices.remove(v);
            }
        }

        Assert.assertTrue(vertices.isEmpty());
    }

    private static int calculateEdgeCount(Mesh3D mesh) {
        HashSet<Edge3D> edges = new HashSet<Edge3D>();

        for (Face3D face : mesh.faces) {
            for (int i = 0; i < face.indices.length; i++) {
                int fromIndex = face.indices[i];
                int toIndex = face.indices[(i + 1) % face.indices.length];
                Edge3D edge = new Edge3D(fromIndex, toIndex);
                Edge3D pair = edge.createPair();
                if (!edges.contains(pair))
                    edges.add(edge);
            }
        }

        return edges.size();
    }

    public static void assertEdgeCountEquals(Mesh3D mesh, int expectedEdgeCount) {
        Assert.assertEquals(calculateEdgeCount(mesh), expectedEdgeCount);
    }

    public static void assertFulfillsEulerCharacteristic(Mesh3D mesh) {
        int edgeCount = MeshTest.calculateEdgeCount(mesh);
        int faceCount = mesh.getFaceCount();
        int vertexCount = mesh.getVertexCount();
        int actual = vertexCount - edgeCount + faceCount;
        Assert.assertEquals(2, actual);
    }

    public static void assertEveryEdgeHasALengthOf(Mesh3D mesh, float expectedEdgeLength, float delta) {
        for (Face3D face : mesh.faces) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v0 = mesh.vertices.get(face.indices[i]);
                Vector3f v1 = mesh.vertices.get(face.indices[(i + 1) % face.indices.length]);
                Assert.assertEquals(expectedEdgeLength, v0.distance(v1), delta);
            }
        }
    }

    public static void assertFaceCountEquals(Mesh3D mesh, int expectedFaceCount) {
        Assert.assertEquals(expectedFaceCount, mesh.getFaceCount());
        Assert.assertEquals(expectedFaceCount, mesh.faces.size());
        Assert.assertEquals(expectedFaceCount, mesh.getFaces().size());
        Assert.assertEquals(expectedFaceCount, mesh.getFaces(0, mesh.getFaceCount()).size());
    }

    public static void assertVertexCountEquals(Mesh3D mesh, int expectedVertexCount) {
        Assert.assertEquals(expectedVertexCount, mesh.getVertexCount());
        Assert.assertEquals(expectedVertexCount, mesh.vertices.size());
        Assert.assertEquals(expectedVertexCount, mesh.getVertices().size());
        Assert.assertEquals(expectedVertexCount, mesh.getVertices(0, mesh.getVertexCount()).size());
    }

    private static void assertFaceByVertexCount(Mesh3D mesh, int vertices, int expected) {
        FaceSelection selection = new FaceSelection(mesh);
        selection.selectByVertexCount(vertices);
        Assert.assertEquals(expected, selection.size());
    }

    public static void assertTriangleCountEquals(Mesh3D mesh, int expectedTriangleCount) {
        assertFaceByVertexCount(mesh, 3, expectedTriangleCount);
    }

    public static void assertQuadCountEquals(Mesh3D mesh, int expectedQuadCount) {
        assertFaceByVertexCount(mesh, 4, expectedQuadCount);
    }

    public static void assertHexagonCountEquals(Mesh3D mesh, int expectedHexagonCount) {
        assertFaceByVertexCount(mesh, 6, expectedHexagonCount);
    }

    public static void assertPentagonCountEquals(Mesh3D mesh, int expectedPentagonCount) {
        assertFaceByVertexCount(mesh, 5, expectedPentagonCount);
    }

    public static void assertOctagonCountEquals(Mesh3D mesh, int expectedOctagonCount) {
        assertFaceByVertexCount(mesh, 8, expectedOctagonCount);
    }

    public static void assertDecagonCountEquals(Mesh3D mesh, int expectedDecagonCount) {
        assertFaceByVertexCount(mesh, 10, expectedDecagonCount);
    }

    public static void assertIsManifold(Mesh3D mesh) {
        new ManifoldTest(mesh).assertIsManifold();
    }

    public static void assertFaceContainsVertexIndex(Face3D face, int expectedVertexIndex) {
        int count = 0;
        for (int i = 0; i < face.indices.length; i++)
            count += face.indices[i] == expectedVertexIndex ? 1 : 0;
        Assert.assertEquals(1, count);
    }

}
