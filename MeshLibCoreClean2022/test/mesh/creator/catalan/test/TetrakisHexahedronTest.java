package mesh.creator.catalan.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import math.GeometryUtil;
import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.catalan.TetrakisHexahedronCreator;
import mesh.creator.catalan.TriakisTetrahedronCreator;
import util.MeshTest;

public class TetrakisHexahedronTest {

    private Mesh3D mesh;

    @BeforeEach
    public void setup() {
        TetrakisHexahedronCreator creator = new TetrakisHexahedronCreator();
        mesh = creator.create();
    }

    @Test
    public void testCreatedMeshIsNotNull() {
        assertNotNull(mesh);
    }

    @Test
    public void containsOuterVertices() {
        List<Vector3f> vertices = mesh.vertices;
        assertTrue(vertices.contains(new Vector3f(-1, 1, 1)));
        assertTrue(vertices.contains(new Vector3f(1, 1, 1)));
        assertTrue(vertices.contains(new Vector3f(1, 1, -1)));
        assertTrue(vertices.contains(new Vector3f(-1, 1, -1)));
        assertTrue(vertices.contains(new Vector3f(1, -1, 1)));
        assertTrue(vertices.contains(new Vector3f(-1, -1, 1)));
        assertTrue(vertices.contains(new Vector3f(-1, -1, -1)));
        assertTrue(vertices.contains(new Vector3f(1, -1, -1)));
    }

    @Test
    public void containsInnerVertices() {
        float a = 3f / 2f;
        List<Vector3f> vertices = mesh.vertices;
        assertTrue(vertices.contains(new Vector3f(0, 0, a)));
        assertTrue(vertices.contains(new Vector3f(0, 0, -a)));
        assertTrue(vertices.contains(new Vector3f(0, a, 0)));
        assertTrue(vertices.contains(new Vector3f(0, -a, 0)));
        assertTrue(vertices.contains(new Vector3f(a, 0, 0)));
        assertTrue(vertices.contains(new Vector3f(-a, 0, 0)));
    }

    /**
     * Explanation:
     * 
     * Equilateral Triangles: For each face, we extract the three vertices. We
     * calculate the distances between these vertices to represent the side lengths.
     * We assert that all side lengths are approximately equal.
     */
    @Test
    public void testFaceEquilateralTriangles() {
        TriakisTetrahedronCreator creator = new TriakisTetrahedronCreator();
        Mesh3D mesh = creator.create();

        for (Face3D face : mesh.getFaces()) {
            Vector3f v1 = mesh.getVertexAt(face.getIndexAt(0));
            Vector3f v2 = mesh.getVertexAt(face.getIndexAt(1));
            Vector3f v3 = mesh.getVertexAt(face.getIndexAt(2));

            double side1 = v1.distance(v2);
            double side2 = v2.distance(v3);
            double side3 = v3.distance(v1);

            // Assert that all sides are approximately equal
            assertEquals(side1, side2, Mathf.ZERO_TOLERANCE);
            assertEquals(side2, side3, Mathf.ZERO_TOLERANCE);
        }
    }

    /**
     * Face Angles:
     * 
     * For each face, we calculate the angles between the vectors formed by the
     * vertices. We assert that all angles are approximately 60 degrees.
     */
    @Test
    public void testFaceAngles() {
        TriakisTetrahedronCreator creator = new TriakisTetrahedronCreator();
        Mesh3D mesh = creator.create();

        for (Face3D face : mesh.getFaces()) {
            Vector3f v1 = mesh.getVertexAt(face.getIndexAt(0));
            Vector3f v2 = mesh.getVertexAt(face.getIndexAt(1));
            Vector3f v3 = mesh.getVertexAt(face.getIndexAt(2));

            double angle1 = GeometryUtil.angleBetweenVectors(v1.subtract(v2), v1.subtract(v3));
            double angle2 = GeometryUtil.angleBetweenVectors(v2.subtract(v1), v2.subtract(v3));
            double angle3 = GeometryUtil.angleBetweenVectors(v3.subtract(v1), v3.subtract(v2));

            // Assert that all angles are approximately 60 degrees
            assertEquals(angle1, Math.PI / 3, Mathf.ZERO_TOLERANCE);
            assertEquals(angle2, Math.PI / 3, Mathf.ZERO_TOLERANCE);
            assertEquals(angle3, Math.PI / 3, Mathf.ZERO_TOLERANCE);
        }
    }

    @Test
    public void testLongerEdgeLengths() {
        int expectedEdgeCount = 12;
        float expectedEdgeLength = 2f;
        MeshTest.assertMeshHasEdgesWithLengthOf(mesh, expectedEdgeCount, expectedEdgeLength);
    }

    @Test
    public void testShorterEdgeLengths() {
        int expectedEdgeCount = 24;
        float expectedEdgeLength = 1.5f;
        MeshTest.assertMeshHasEdgesWithLengthOf(mesh, expectedEdgeCount, expectedEdgeLength);
    }

    @Test
    public void testTriangleCount() {
        int expectedTriangleCount = 24;
        MeshTest.assertTriangleCountEquals(mesh, expectedTriangleCount);
    }

    @Test
    public void testFaceCount() {
        int expectedFaceCount = 24;
        assertEquals(expectedFaceCount, mesh.getFaceCount());
    }

    @Test
    public void testVertexCount() {
        int expectedVertexCount = 14;
        assertEquals(expectedVertexCount, mesh.getVertexCount());
    }

    @Test
    public void testEdgeCount() {
        int expectedEdgeCount = 36;
        MeshTest.assertEdgeCountEquals(mesh, expectedEdgeCount);
    }

    @Test
    public void testMeshIsManifold() {
        MeshTest.assertIsManifold(mesh);
    }

    @Test
    public void testFulfillsEulerCharacteristic() {
        MeshTest.assertFulfillsEulerCharacteristic(mesh);
    }

    @Test
    public void testNoDuplicatedFaces() {
        assertTrue(MeshTest.meshHasNoDuplicatedFaces(mesh));
    }

    @Test
    public void testMeshHasNoLooseVertices() {
        assertTrue(MeshTest.meshHasNoLooseVertices(mesh));
    }

    @Test
    public void testNormalsPointOutwards() {
        MeshTest.assertNormalsPointOutwards(mesh);
    }

}
