package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.beam.BeamCProfileCreator;

// Auto-generated test class to execute base tests for mesh creators
public class BeamCProfileCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new BeamCProfileCreator().create();
	}

	public void implementsCreatorInterface() {
		BeamCProfileCreator creator = new BeamCProfileCreator();
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
		Mesh3D mesh0 = new BeamCProfileCreator().create();
		Mesh3D mesh1 = new BeamCProfileCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new BeamCProfileCreator().create();
		Mesh3D mesh1 = new BeamCProfileCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetDepth() {
		float expected = 2.5857025638290235E37f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		creator.setDepth(expected);
		Assert.assertEquals(expected, creator.getDepth(), 0);
	}

	@Test
	public void getDepthReturnsDefaultValue() {
		float expected = 2.0f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		Assert.assertEquals(expected, creator.getDepth(), 0);
	}

	@Test
	public void getSetHeight() {
		float expected = 2.7702429625271823E38f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		creator.setHeight(expected);
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getHeightReturnsDefaultValue() {
		float expected = 0.85f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getSetWidth() {
		float expected = 1.398181456804523E38f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		creator.setWidth(expected);
		Assert.assertEquals(expected, creator.getWidth(), 0);
	}

	@Test
	public void getWidthReturnsDefaultValue() {
		float expected = 0.5f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		Assert.assertEquals(expected, creator.getWidth(), 0);
	}

	@Test
	public void getSetThickness() {
		float expected = 2.682673411705632E38f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		creator.setThickness(expected);
		Assert.assertEquals(expected, creator.getThickness(), 0);
	}

	@Test
	public void getThicknessReturnsDefaultValue() {
		float expected = 0.1f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		Assert.assertEquals(expected, creator.getThickness(), 0);
	}

	@Test
	public void getTaperReturnsDefaultValue() {
		float expected = 0.0f;
		BeamCProfileCreator creator = new BeamCProfileCreator();
		Assert.assertEquals(expected, creator.getTaper(), 0);
	}

}
