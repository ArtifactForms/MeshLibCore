package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.TubeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TubeCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new TubeCreator().create();
	}

	public void implementsCreatorInterface() {
		TubeCreator creator = new TubeCreator();
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
		Mesh3D mesh0 = new TubeCreator().create();
		Mesh3D mesh1 = new TubeCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new TubeCreator().create();
		Mesh3D mesh1 = new TubeCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetVertices() {
		int expected = 698734711;
		TubeCreator creator = new TubeCreator();
		creator.setVertices(expected);
		Assert.assertEquals(expected, creator.getVertices());
	}

	@Test
	public void getVerticesReturnsDefaultValue() {
		int expected = 32;
		TubeCreator creator = new TubeCreator();
		Assert.assertEquals(expected, creator.getVertices());
	}

	@Test
	public void getSetHeight() {
		float expected = 8.764754896561172E37f;
		TubeCreator creator = new TubeCreator();
		creator.setHeight(expected);
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getHeightReturnsDefaultValue() {
		float expected = 2.0f;
		TubeCreator creator = new TubeCreator();
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getSetBottomOuterRadius() {
		float expected = 1.51831288884837E38f;
		TubeCreator creator = new TubeCreator();
		creator.setBottomOuterRadius(expected);
		Assert.assertEquals(expected, creator.getBottomOuterRadius(), 0);
	}

	@Test
	public void getBottomOuterRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		TubeCreator creator = new TubeCreator();
		Assert.assertEquals(expected, creator.getBottomOuterRadius(), 0);
	}

	@Test
	public void getSetBottomInnerRadius() {
		float expected = 2.0628279282259606E38f;
		TubeCreator creator = new TubeCreator();
		creator.setBottomInnerRadius(expected);
		Assert.assertEquals(expected, creator.getBottomInnerRadius(), 0);
	}

	@Test
	public void getBottomInnerRadiusReturnsDefaultValue() {
		float expected = 0.5f;
		TubeCreator creator = new TubeCreator();
		Assert.assertEquals(expected, creator.getBottomInnerRadius(), 0);
	}

	@Test
	public void getSetTopOuterRadius() {
		float expected = 3.0725253407046497E38f;
		TubeCreator creator = new TubeCreator();
		creator.setTopOuterRadius(expected);
		Assert.assertEquals(expected, creator.getTopOuterRadius(), 0);
	}

	@Test
	public void getTopOuterRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		TubeCreator creator = new TubeCreator();
		Assert.assertEquals(expected, creator.getTopOuterRadius(), 0);
	}

	@Test
	public void getSetTopInnerRadius() {
		float expected = 3.2887208275317236E38f;
		TubeCreator creator = new TubeCreator();
		creator.setTopInnerRadius(expected);
		Assert.assertEquals(expected, creator.getTopInnerRadius(), 0);
	}

	@Test
	public void getTopInnerRadiusReturnsDefaultValue() {
		float expected = 0.5f;
		TubeCreator creator = new TubeCreator();
		Assert.assertEquals(expected, creator.getTopInnerRadius(), 0);
	}

}
