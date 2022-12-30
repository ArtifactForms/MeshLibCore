package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.special.AntiprismCreator;

// Auto-generated test class to execute base tests for mesh creators
public class AntiprismCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new AntiprismCreator().create();
	}

	public void implementsCreatorInterface() {
		AntiprismCreator creator = new AntiprismCreator();
		Assert.assertTrue(creator instanceof IMeshCreator);
	}

	@Test
	public void createdMeshIsNotNullByDefault() {
		Assert.assertNotNull(mesh);
	}

	@Test
	public void vertexListIsNotEmpty() {
		Assert.assertFalse(mesh.vertices.isEmpty());
	}

	@Test
	public void getVertexCountReturnsSizeOfVertexList() {
		int vertexCount = mesh.getVertexCount();
		Assert.assertEquals(vertexCount, mesh.getVertices().size());
	}

	@Test
	public void getFaceCountReturnsSizeOfFaceList() {
		int faceCount = mesh.getFaceCount();
		Assert.assertEquals(faceCount, mesh.getFaces().size());
	}

	@Test
	public void createdMeshHasNoLooseVertices() {
		MeshTest.assertMeshHasNoLooseVertices(mesh);
	}

	@Test
	public void createdMeshHasNoDuplicatedFaces() {
		// Running this test is very time expensive
		MeshTest.assertMeshHasNoDuplicatedFaces(mesh);
	}

	@Test
	public void eachCallOfCreateReturnsNewUniqueMeshInstance() {
		Mesh3D mesh0 = new AntiprismCreator().create();
		Mesh3D mesh1 = new AntiprismCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new AntiprismCreator().create();
		Mesh3D mesh1 = new AntiprismCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetN() {
		int expected = 1132399486;
		AntiprismCreator creator = new AntiprismCreator();
		creator.setN(expected);
		Assert.assertEquals(expected, creator.getN());
	}

	@Test
	public void getNReturnsDefaultValue() {
		int expected = 6;
		AntiprismCreator creator = new AntiprismCreator();
		Assert.assertEquals(expected, creator.getN());
	}

}
