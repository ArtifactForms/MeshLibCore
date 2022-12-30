package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.creative.TorusCageCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TorusCageCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new TorusCageCreator().create();
	}

	public void implementsCreatorInterface() {
		TorusCageCreator creator = new TorusCageCreator();
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
		Mesh3D mesh0 = new TorusCageCreator().create();
		Mesh3D mesh1 = new TorusCageCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new TorusCageCreator().create();
		Mesh3D mesh1 = new TorusCageCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetSubdivisions() {
		int expected = 28849506;
		TorusCageCreator creator = new TorusCageCreator();
		creator.setSubdivisions(expected);
		Assert.assertEquals(expected, creator.getSubdivisions());
	}

	@Test
	public void getSubdivisionsReturnsDefaultValue() {
		int expected = 1;
		TorusCageCreator creator = new TorusCageCreator();
		Assert.assertEquals(expected, creator.getSubdivisions());
	}

	@Test
	public void getSetMajorRadius() {
		float expected = 2.033141729418698E38f;
		TorusCageCreator creator = new TorusCageCreator();
		creator.setMajorRadius(expected);
		Assert.assertEquals(expected, creator.getMajorRadius(), 0);
	}

	@Test
	public void getMajorRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		TorusCageCreator creator = new TorusCageCreator();
		Assert.assertEquals(expected, creator.getMajorRadius(), 0);
	}

	@Test
	public void getSetMinorRadius() {
		float expected = 2.895034366685629E38f;
		TorusCageCreator creator = new TorusCageCreator();
		creator.setMinorRadius(expected);
		Assert.assertEquals(expected, creator.getMinorRadius(), 0);
	}

	@Test
	public void getMinorRadiusReturnsDefaultValue() {
		float expected = 0.5f;
		TorusCageCreator creator = new TorusCageCreator();
		Assert.assertEquals(expected, creator.getMinorRadius(), 0);
	}

	@Test
	public void getSetMinorSegments() {
		int expected = 286313636;
		TorusCageCreator creator = new TorusCageCreator();
		creator.setMinorSegments(expected);
		Assert.assertEquals(expected, creator.getMinorSegments());
	}

	@Test
	public void getMinorSegmentsReturnsDefaultValue() {
		int expected = 12;
		TorusCageCreator creator = new TorusCageCreator();
		Assert.assertEquals(expected, creator.getMinorSegments());
	}

	@Test
	public void getSetMajorSegments() {
		int expected = 1634355855;
		TorusCageCreator creator = new TorusCageCreator();
		creator.setMajorSegments(expected);
		Assert.assertEquals(expected, creator.getMajorSegments());
	}

	@Test
	public void getMajorSegmentsReturnsDefaultValue() {
		int expected = 24;
		TorusCageCreator creator = new TorusCageCreator();
		Assert.assertEquals(expected, creator.getMajorSegments());
	}

	@Test
	public void getSetExtrude() {
		float expected = 1.5880153124807648E38f;
		TorusCageCreator creator = new TorusCageCreator();
		creator.setExtrude(expected);
		Assert.assertEquals(expected, creator.getExtrude(), 0);
	}

	@Test
	public void getExtrudeReturnsDefaultValue() {
		float expected = 0.8f;
		TorusCageCreator creator = new TorusCageCreator();
		Assert.assertEquals(expected, creator.getExtrude(), 0);
	}

	@Test
	public void getSetThickness() {
		float expected = 2.723764940646048E37f;
		TorusCageCreator creator = new TorusCageCreator();
		creator.setThickness(expected);
		Assert.assertEquals(expected, creator.getThickness(), 0);
	}

	@Test
	public void getThicknessReturnsDefaultValue() {
		float expected = 0.05f;
		TorusCageCreator creator = new TorusCageCreator();
		Assert.assertEquals(expected, creator.getThickness(), 0);
	}

}
