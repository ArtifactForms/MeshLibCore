package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.SegmentedCylinderCreator;

// Auto-generated test class to execute base tests for mesh creators
public class SegmentedCylinderCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new SegmentedCylinderCreator().create();
	}

	public void implementsCreatorInterface() {
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
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
		Mesh3D mesh0 = new SegmentedCylinderCreator().create();
		Mesh3D mesh1 = new SegmentedCylinderCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new SegmentedCylinderCreator().create();
		Mesh3D mesh1 = new SegmentedCylinderCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetCapTop() {
		boolean expected = true;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		creator.setCapTop(expected);
		Assert.assertEquals(expected, creator.isCapTop());
	}

	@Test
	public void getCapTopReturnsDefaultValue() {
		boolean expected = true;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		Assert.assertEquals(expected, creator.isCapTop());
	}

	@Test
	public void getSetRotationSegments() {
		int expected = 2092084914;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		creator.setRotationSegments(expected);
		Assert.assertEquals(expected, creator.getRotationSegments());
	}

	@Test
	public void getRotationSegmentsReturnsDefaultValue() {
		int expected = 32;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		Assert.assertEquals(expected, creator.getRotationSegments());
	}

	@Test
	public void getSetHeight() {
		float expected = 1.1135840408510357E38f;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		creator.setHeight(expected);
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getHeightReturnsDefaultValue() {
		float expected = 2.0f;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getSetTopRadius() {
		float expected = 1.946748203756197E38f;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		creator.setTopRadius(expected);
		Assert.assertEquals(expected, creator.getTopRadius(), 0);
	}

	@Test
	public void getTopRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		Assert.assertEquals(expected, creator.getTopRadius(), 0);
	}

	@Test
	public void getSetBottomRadius() {
		float expected = 5.799255629014032E37f;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		creator.setBottomRadius(expected);
		Assert.assertEquals(expected, creator.getBottomRadius(), 0);
	}

	@Test
	public void getBottomRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		Assert.assertEquals(expected, creator.getBottomRadius(), 0);
	}

	@Test
	public void getSetCapBottom() {
		boolean expected = true;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		creator.setCapBottom(expected);
		Assert.assertEquals(expected, creator.isCapBottom());
	}

	@Test
	public void getCapBottomReturnsDefaultValue() {
		boolean expected = true;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		Assert.assertEquals(expected, creator.isCapBottom());
	}

	@Test
	public void getSetHeightSegments() {
		int expected = 1573910614;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		creator.setHeightSegments(expected);
		Assert.assertEquals(expected, creator.getHeightSegments());
	}

	@Test
	public void getHeightSegmentsReturnsDefaultValue() {
		int expected = 1;
		SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
		Assert.assertEquals(expected, creator.getHeightSegments());
	}

}
