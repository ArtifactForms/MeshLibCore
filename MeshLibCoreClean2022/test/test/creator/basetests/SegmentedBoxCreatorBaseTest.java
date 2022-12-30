package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.SegmentedBoxCreator;

// Auto-generated test class to execute base tests for mesh creators
public class SegmentedBoxCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new SegmentedBoxCreator().create();
	}

	public void implementsCreatorInterface() {
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
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
		Mesh3D mesh0 = new SegmentedBoxCreator().create();
		Mesh3D mesh1 = new SegmentedBoxCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new SegmentedBoxCreator().create();
		Mesh3D mesh1 = new SegmentedBoxCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetDepth() {
		float expected = 2.8638057283133634E38f;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		creator.setDepth(expected);
		Assert.assertEquals(expected, creator.getDepth(), 0);
	}

	@Test
	public void getDepthReturnsDefaultValue() {
		float expected = 2.0f;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		Assert.assertEquals(expected, creator.getDepth(), 0);
	}

	@Test
	public void getSetHeight() {
		float expected = 1.2293847767940616E38f;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		creator.setHeight(expected);
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getHeightReturnsDefaultValue() {
		float expected = 2.0f;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getSetWidth() {
		float expected = 2.4320135177482173E38f;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		creator.setWidth(expected);
		Assert.assertEquals(expected, creator.getWidth(), 0);
	}

	@Test
	public void getWidthReturnsDefaultValue() {
		float expected = 2.0f;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		Assert.assertEquals(expected, creator.getWidth(), 0);
	}

	@Test
	public void getSetSegmentsY() {
		int expected = 1726947038;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		creator.setSegmentsY(expected);
		Assert.assertEquals(expected, creator.getSegmentsY());
	}

	@Test
	public void getSegmentsYReturnsDefaultValue() {
		int expected = 10;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		Assert.assertEquals(expected, creator.getSegmentsY());
	}

	@Test
	public void getSetSegmentsZ() {
		int expected = 1678334769;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		creator.setSegmentsZ(expected);
		Assert.assertEquals(expected, creator.getSegmentsZ());
	}

	@Test
	public void getSegmentsZReturnsDefaultValue() {
		int expected = 10;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		Assert.assertEquals(expected, creator.getSegmentsZ());
	}

	@Test
	public void getSetSegmentsX() {
		int expected = 706776093;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		creator.setSegmentsX(expected);
		Assert.assertEquals(expected, creator.getSegmentsX());
	}

	@Test
	public void getSegmentsXReturnsDefaultValue() {
		int expected = 10;
		SegmentedBoxCreator creator = new SegmentedBoxCreator();
		Assert.assertEquals(expected, creator.getSegmentsX());
	}

}
