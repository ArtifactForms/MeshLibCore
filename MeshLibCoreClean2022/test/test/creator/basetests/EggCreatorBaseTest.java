package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.EggCreator;

// Auto-generated test class to execute base tests for mesh creators
public class EggCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new EggCreator().create();
	}

	public void implementsCreatorInterface() {
		EggCreator creator = new EggCreator();
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
		Mesh3D mesh0 = new EggCreator().create();
		Mesh3D mesh1 = new EggCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new EggCreator().create();
		Mesh3D mesh1 = new EggCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetSize() {
		float expected = 2.354587398142597E38f;
		EggCreator creator = new EggCreator();
		creator.setSize(expected);
		Assert.assertEquals(expected, creator.getSize(), 0);
	}

	@Test
	public void getSizeReturnsDefaultValue() {
		float expected = 1.0f;
		EggCreator creator = new EggCreator();
		Assert.assertEquals(expected, creator.getSize(), 0);
	}

	@Test
	public void getSetSubdivisions() {
		int expected = 1566575937;
		EggCreator creator = new EggCreator();
		creator.setSubdivisions(expected);
		Assert.assertEquals(expected, creator.getSubdivisions());
	}

	@Test
	public void getSubdivisionsReturnsDefaultValue() {
		int expected = 3;
		EggCreator creator = new EggCreator();
		Assert.assertEquals(expected, creator.getSubdivisions());
	}

	@Test
	public void getSetTopScale() {
		float expected = 2.7649184010232435E38f;
		EggCreator creator = new EggCreator();
		creator.setTopScale(expected);
		Assert.assertEquals(expected, creator.getTopScale(), 0);
	}

	@Test
	public void getTopScaleReturnsDefaultValue() {
		float expected = 0.5f;
		EggCreator creator = new EggCreator();
		Assert.assertEquals(expected, creator.getTopScale(), 0);
	}

}
