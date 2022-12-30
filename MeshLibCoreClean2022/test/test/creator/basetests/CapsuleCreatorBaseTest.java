package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.CapsuleCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CapsuleCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new CapsuleCreator().create();
	}

	public void implementsCreatorInterface() {
		CapsuleCreator creator = new CapsuleCreator();
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
		Mesh3D mesh0 = new CapsuleCreator().create();
		Mesh3D mesh1 = new CapsuleCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new CapsuleCreator().create();
		Mesh3D mesh1 = new CapsuleCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetRotationSegments() {
		int expected = 1034029805;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setRotationSegments(expected);
		Assert.assertEquals(expected, creator.getRotationSegments());
	}

	@Test
	public void getRotationSegmentsReturnsDefaultValue() {
		int expected = 32;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getRotationSegments());
	}

	@Test
	public void getSetTopRadius() {
		float expected = 3.3448804615056716E38f;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setTopRadius(expected);
		Assert.assertEquals(expected, creator.getTopRadius(), 0);
	}

	@Test
	public void getTopRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getTopRadius(), 0);
	}

	@Test
	public void getSetBottomRadius() {
		float expected = 1.814389338188287E38f;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setBottomRadius(expected);
		Assert.assertEquals(expected, creator.getBottomRadius(), 0);
	}

	@Test
	public void getBottomRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getBottomRadius(), 0);
	}

	@Test
	public void getSetBottomCapSegments() {
		int expected = 2110423740;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setBottomCapSegments(expected);
		Assert.assertEquals(expected, creator.getBottomCapSegments());
	}

	@Test
	public void getBottomCapSegmentsReturnsDefaultValue() {
		int expected = 16;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getBottomCapSegments());
	}

	@Test
	public void getSetCylinderSegments() {
		int expected = 1441552168;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setCylinderSegments(expected);
		Assert.assertEquals(expected, creator.getCylinderSegments());
	}

	@Test
	public void getCylinderSegmentsReturnsDefaultValue() {
		int expected = 8;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getCylinderSegments());
	}

	@Test
	public void getSetCylinderHeight() {
		float expected = 4.4731548588259975E37f;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setCylinderHeight(expected);
		Assert.assertEquals(expected, creator.getCylinderHeight(), 0);
	}

	@Test
	public void getCylinderHeightReturnsDefaultValue() {
		float expected = 2.0f;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getCylinderHeight(), 0);
	}

	@Test
	public void getSetTopCapSegments() {
		int expected = 900705204;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setTopCapSegments(expected);
		Assert.assertEquals(expected, creator.getTopCapSegments());
	}

	@Test
	public void getTopCapSegmentsReturnsDefaultValue() {
		int expected = 16;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getTopCapSegments());
	}

	@Test
	public void getSetBottomCapHeight() {
		float expected = 2.290597104245639E37f;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setBottomCapHeight(expected);
		Assert.assertEquals(expected, creator.getBottomCapHeight(), 0);
	}

	@Test
	public void getBottomCapHeightReturnsDefaultValue() {
		float expected = 1.0f;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getBottomCapHeight(), 0);
	}

	@Test
	public void getSetTopCapHeight() {
		float expected = 2.065743154314431E38f;
		CapsuleCreator creator = new CapsuleCreator();
		creator.setTopCapHeight(expected);
		Assert.assertEquals(expected, creator.getTopCapHeight(), 0);
	}

	@Test
	public void getTopCapHeightReturnsDefaultValue() {
		float expected = 1.0f;
		CapsuleCreator creator = new CapsuleCreator();
		Assert.assertEquals(expected, creator.getTopCapHeight(), 0);
	}

}
