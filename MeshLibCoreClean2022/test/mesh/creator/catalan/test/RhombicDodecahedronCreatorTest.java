package mesh.creator.catalan;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import util.MeshTest;

public class RhombicDodecahedronCreatorTest {

    private Mesh3D mesh;

    @BeforeEach
    public void setup() {
        RhombicDodecahedronCreator creator = new RhombicDodecahedronCreator();
        mesh = creator.create();
    }
    
	@Test
	public void testCreatedMeshIsNotNull() {
		assertNotNull(mesh);
	}

    @Test
    public void testRhombusFaces() {
        for (Face3D face : mesh.getFaces()) {
        	Vector3f v1 = mesh.getVertexAt(face.getIndexAt(0));
        	Vector3f v2 = mesh.getVertexAt(face.getIndexAt(1));
        	Vector3f v3 = mesh.getVertexAt(face.getIndexAt(2));
            Vector3f v4 = mesh.getVertexAt(face.getIndexAt(3));

            // Check for opposite sides being equal
            assertEquals(v1.distance(v2), v3.distance(v4), Mathf.ZERO_TOLERANCE);
            assertEquals(v2.distance(v3), v1.distance(v4), Mathf.ZERO_TOLERANCE);
        }
    }

    @Test
    public void testDiagonalsPerpendicular() {
        for (Face3D face : mesh.getFaces()) {
        	Vector3f v1 = mesh.getVertexAt(face.getIndexAt(0));
        	Vector3f v2 = mesh.getVertexAt(face.getIndexAt(1));
        	Vector3f v3 = mesh.getVertexAt(face.getIndexAt(2));
            Vector3f v4 = mesh.getVertexAt(face.getIndexAt(3));

            Vector3f diagonal1 = v1.subtract(v3);
            Vector3f diagonal2 = v2.subtract(v4);

            // Check if dot product is approximately zero
            assertEquals(diagonal1.dot(diagonal2), 0.0, Mathf.ZERO_TOLERANCE);
        }
    }
    
	@Test
	public void testQuadCount() {
		int expectedQuadCount = 12;
		MeshTest.assertQuadCountEquals(mesh, expectedQuadCount);
	}

	@Test
	public void testFaceCount() {
		int expectedFaceCount = 12;
		assertEquals(expectedFaceCount, mesh.getFaceCount());
	}

	@Test
	public void testVertexCount() {
		int expectedVertexCount = 14;
		assertEquals(expectedVertexCount, mesh.getVertexCount());
	}

	@Test
	public void testEdgeCount() {
		int expectedEdgeCount = 24;
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
		MeshTest.assertMeshHasNoDuplicatedFaces(mesh);
	}

	@Test
	public void testMeshHasNoLooseVertices() {
		MeshTest.assertMeshHasNoLooseVertices(mesh);
	}

	@Test
	public void testNormalsPointOutwards() {
		MeshTest.assertNormalsPointOutwards(mesh);
	}

}