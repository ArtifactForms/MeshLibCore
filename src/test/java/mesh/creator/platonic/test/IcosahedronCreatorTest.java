package mesh.creator.platonic.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.util.Bounds3;
import util.MeshTest;

public class IcosahedronCreatorTest {

    private static final int DEFAULT_VERTEX_COUNT = 12;

    private static final int DEFAULT_FACE_COUNT = 20;

    private static final int DEFAULT_EDGE_COUNT = 30;
    
    @Test
    public void testEdgeCount() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertEquals(DEFAULT_EDGE_COUNT, MeshTest.calculateEdgeCount(mesh));
    }

    @Test
    public void testGetFaceCount() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertEquals(DEFAULT_FACE_COUNT, mesh.getFaceCount());
    }

    @Test
    public void testGetFaceCountListSize() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertEquals(DEFAULT_FACE_COUNT, mesh.faces.size());
    }

    @Test
    public void testTriangleCount() {
        int expected = DEFAULT_FACE_COUNT;
        Mesh3D mesh = new IcosahedronCreator().create();
        assertTrue(MeshTest.isTriangleCountEquals(mesh, expected));
    }

    @Test
    public void testGetVertexCount() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertEquals(DEFAULT_VERTEX_COUNT, mesh.getVertexCount());
    }

    @Test
    public void testGetVertexCountListSize() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertEquals(DEFAULT_VERTEX_COUNT, mesh.vertices.size());
    }

    @Test
    public void testContainsBottomVertex() {
        Mesh3D mesh = new IcosahedronCreator().create();
        List<Vector3f> vertices = mesh.vertices;
        assertTrue(vertices.contains(new Vector3f(0, 1, 0)));
    }

    @Test
    public void testContainsTopVertex() {
        Mesh3D mesh = new IcosahedronCreator().create();
        List<Vector3f> vertices = mesh.vertices;
        assertTrue(vertices.contains(new Vector3f(0, -1, 0)));
    }

    @Test
    public void testSizeReturnsOneByDefault() {
        IcosahedronCreator creator = new IcosahedronCreator();
        assertEquals(1, creator.getSize());
    }

    @Test
    public void testGetSetSize() {
        float expected = 100.24f;
        IcosahedronCreator creator = new IcosahedronCreator();
        creator.setSize(expected);
        assertEquals(expected, creator.getSize());
    }

    @Test
    public void testGetSetSizeLargeValue() {
        float expetced = Float.MAX_VALUE;
        IcosahedronCreator creator = new IcosahedronCreator();
        creator.setSize(expetced);
        assertEquals(expetced, creator.getSize());
    }

    /**
     * Explanation:
     * 
     * Equilateral Triangles: For each face, we extract the three vertices. We
     * calculate the distances between these vertices to represent the side
     * lengths. We assert that all side lengths are approximately equal.
     */
    @Test
    public void testFaceEquilateralTriangles() {
        IcosahedronCreator creator = new IcosahedronCreator();
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
     * Bounds based on a footprint of the original legacy version of the
     * creator.
     */
    @Test
    public void testFootprintBounds() {
        Vector3f min = new Vector3f(-0.894425f, -1.0f, -0.85064f);
        Vector3f max = new Vector3f(0.894425f, 1.0f, 0.85064f);

        IcosahedronCreator creator = new IcosahedronCreator();
        Mesh3D mesh = creator.create();

        Bounds3 bounds = mesh.calculateBounds();

        Vector3f bMin = bounds.getMin();
        Vector3f bMax = bounds.getMax();

        assertEquals(min.x, bMin.x);
        assertEquals(min.y, bMin.y);
        assertEquals(min.z, bMin.z);

        assertEquals(max.x, bMax.x);
        assertEquals(max.y, bMax.y);
        assertEquals(max.z, bMax.z);
    }

    /**
     * Vertices based on a footprint of the original legacy version of the
     * creator.
     */
    @Test
    public void testFootprintVertices() {
        IcosahedronCreator creator = new IcosahedronCreator();
        Mesh3D mesh = creator.create();
        List<Vector3f> v = mesh.vertices;
        assertTrue(v.contains(new Vector3f(0.0f, -1.0f, 0.0f)));
        assertTrue(v.contains(new Vector3f(0.7236f, -0.447215f, 0.52572f)));
        assertTrue(v.contains(new Vector3f(-0.276385f, -0.447215f, 0.85064f)));
        assertTrue(v.contains(new Vector3f(-0.894425f, -0.447215f, 0.0f)));
        assertTrue(v.contains(new Vector3f(-0.276385f, -0.447215f, -0.85064f)));
        assertTrue(v.contains(new Vector3f(0.7236f, -0.447215f, -0.52572f)));
        assertTrue(v.contains(new Vector3f(0.276385f, 0.447215f, 0.85064f)));
        assertTrue(v.contains(new Vector3f(-0.7236f, 0.447215f, 0.52572f)));
        assertTrue(v.contains(new Vector3f(-0.7236f, 0.447215f, -0.52572f)));
        assertTrue(v.contains(new Vector3f(0.276385f, 0.447215f, -0.85064f)));
        assertTrue(v.contains(new Vector3f(0.894425f, 0.447215f, 0.0f)));
        assertTrue(v.contains(new Vector3f(0.0f, 1.0f, 0.0f)));
    }
    
    @Test
    public void testImplementsMeshCreatorInstance() {
        IcosahedronCreator creator = new IcosahedronCreator();
        assertTrue(creator instanceof IMeshCreator);
    }

    @Test
    public void testCreatedMeshIsNotNullByDefault() {
        IcosahedronCreator creator = new IcosahedronCreator();
        assertNotNull(creator.create());
    }

    @Test
    public void testCreatesUniqueInstances() {
        IcosahedronCreator creator = new IcosahedronCreator();
        Mesh3D mesh0 = creator.create();
        Mesh3D mesh1 = creator.create();
        Mesh3D mesh2 = creator.create();
        assertTrue(mesh0 != mesh1);
        assertTrue(mesh1 != mesh2);
    }

    @Test
    public void testVertexListIsNotEmpty() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertFalse(mesh.vertices.isEmpty());
        assertEquals(mesh.getVertexCount(), mesh.vertices.size());
    }

    @Test
    public void testMeshHasNoLooseVertices() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertTrue(MeshTest.meshHasNoLooseVertices(mesh));
    }
    
    @Test
    public void testDefaultVertexCount() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertEquals(DEFAULT_VERTEX_COUNT, mesh.getVertexCount());
        assertEquals(DEFAULT_VERTEX_COUNT, mesh.vertices.size());
    }
    
    @Test
    public void testDefaultFaceCount() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertEquals(DEFAULT_FACE_COUNT, mesh.getFaceCount());
        assertEquals(DEFAULT_FACE_COUNT, mesh.faces.size());
    }
    
    @Test
    public void testVertexCountIsConsistent() {
        IcosahedronCreator creator = new IcosahedronCreator();
        Mesh3D mesh0 = creator.create();
        Mesh3D mesh1 = creator.create();
        assertEquals(mesh0.getVertexCount(), mesh1.getVertexCount());
    }

    @Test
    public void testFaceCountIsConsistent() {
        IcosahedronCreator creator = new IcosahedronCreator();
        Mesh3D mesh0 = creator.create();
        Mesh3D mesh1 = creator.create();
        assertEquals(mesh0.getVertexCount(), mesh1.getVertexCount());
    }

    @Test
    public void testMeshHasNoDuplicatedFaces() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertTrue(MeshTest.meshHasNoDuplicatedFaces(mesh));
    }

    @Test
    public void testNormalsPointOutwards() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }
    
    /**
     * Testing Watertightness
     * 
     * Manifoldness: Ensure that every edge is shared by exactly two faces.
     */
    @Test
    public void testIsManifold() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    /**
     * Testing Watertightness
     * 
     * Euler's Characteristic: Verify that the mesh satisfies Euler's
     * characteristic formula: V - E + F = 2, where V is the number of vertices,
     * E is the number of edges, F is the number of faces.
     */
    @Test
    public void testEulerCharacteristic() {
        Mesh3D mesh = new IcosahedronCreator().create();
        assertTrue(MeshTest.fulfillsEulerCharacteristic(mesh));
    }

}
