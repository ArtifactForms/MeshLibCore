package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.special.MobiusStripCreator;

// Auto-generated test class to execute base tests for mesh creators
public class MobiusStripCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new MobiusStripCreator().create();
	}

	public void implementsCreatorInterface() {
		MobiusStripCreator creator = new MobiusStripCreator();
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
		Mesh3D mesh0 = new MobiusStripCreator().create();
		Mesh3D mesh1 = new MobiusStripCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new MobiusStripCreator().create();
		Mesh3D mesh1 = new MobiusStripCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetSegments() {
		int expected = 892007163;
		MobiusStripCreator creator = new MobiusStripCreator();
		creator.setSegments(expected);
		Assert.assertEquals(expected, creator.getSegments());
	}

	@Test
	public void getSegmentsReturnsDefaultValue() {
		int expected = 32;
		MobiusStripCreator creator = new MobiusStripCreator();
		Assert.assertEquals(expected, creator.getSegments());
	}

	@Test
	public void getSetRadius() {
		float expected = 2.3944004769944305E38f;
		MobiusStripCreator creator = new MobiusStripCreator();
		creator.setRadius(expected);
		Assert.assertEquals(expected, creator.getRadius(), 0);
	}

	@Test
	public void getRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		MobiusStripCreator creator = new MobiusStripCreator();
		Assert.assertEquals(expected, creator.getRadius(), 0);
	}

	@Test
	public void getSetRings() {
		int expected = 1767259701;
		MobiusStripCreator creator = new MobiusStripCreator();
		creator.setRings(expected);
		Assert.assertEquals(expected, creator.getRings());
	}

	@Test
	public void getRingsReturnsDefaultValue() {
		int expected = 5;
		MobiusStripCreator creator = new MobiusStripCreator();
		Assert.assertEquals(expected, creator.getRings());
	}

}
