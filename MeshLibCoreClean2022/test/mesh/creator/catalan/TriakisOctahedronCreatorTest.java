package mesh.creator.catalan;

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
import mesh.creator.IMeshCreator;
import util.MeshTest;

public class TriakisOctahedronCreatorTest {

	private Mesh3D mesh;
	
	@BeforeEach
	public void setup() {
		TriakisOctahedronCreator creator = new TriakisOctahedronCreator();
		mesh = creator.create();
	}
	
	@Test
	public void testImplementsMeshCreatorInterface() {
		TriakisOctahedronCreator creator = new TriakisOctahedronCreator();
		assertTrue(creator instanceof IMeshCreator);
	}
	
	@Test
	public void testCreatedMeshIsNotNull() {
		assertNotNull(mesh);
	}
	
	@Test
	public void createsNewMeshEveryTime() {
		TriakisOctahedronCreator creator = new TriakisOctahedronCreator();
		Mesh3D mesh0 = creator.create();
		Mesh3D mesh1 = creator.create();
		Mesh3D mesh2 = creator.create();
		assertTrue(mesh0 != mesh1);
		assertTrue(mesh1 != mesh2);
	}
	
	@Test
	public void testVertexCount() {
		int expectedVertexCount = 14;
		int actualVertexCount = mesh.getVertexCount();
		assertEquals(expectedVertexCount, actualVertexCount);
	}
	
	@Test
	public void testFaceCount() {
		int expectedFaceCount = 24;
		int actualFaceCount = mesh.getFaceCount();
		assertEquals(expectedFaceCount, actualFaceCount);
	}
	
	@Test
	public void testTriangleCount() {
		int expectedTriangleCount = 24;
		MeshTest.assertTriangleCountEquals(mesh, expectedTriangleCount);
	}
		
	@Test
	public void conatainsMidEdgeVertices() {
		List<Vector3f> vertices = mesh.vertices;
		assertTrue(vertices.contains(new Vector3f(1, 0, 0)));
		assertTrue(vertices.contains(new Vector3f(-1, 0, 0)));
		assertTrue(vertices.contains(new Vector3f(0, 1, 0)));
		assertTrue(vertices.contains(new Vector3f(0, -1, 0)));
		assertTrue(vertices.contains(new Vector3f(0, 0, 1)));
		assertTrue(vertices.contains(new Vector3f(0, 0, -1)));
	}
	
	@Test
	public void containsCornerVertices() {
		float a = Mathf.sqrt(2) - 1;
		List<Vector3f> vertices = mesh.vertices;
		assertTrue(vertices.contains(new Vector3f(a, a, a)));
		assertTrue(vertices.contains(new Vector3f(-a, -a, -a)));
		assertTrue(vertices.contains(new Vector3f(a, -a, -a)));
		assertTrue(vertices.contains(new Vector3f(-a, a, -a)));
		assertTrue(vertices.contains(new Vector3f(-a, -a, a)));
		assertTrue(vertices.contains(new Vector3f(-a, a, a)));
		assertTrue(vertices.contains(new Vector3f(a, a, -a)));
		assertTrue(vertices.contains(new Vector3f(a, -a, a)));
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
	
}
