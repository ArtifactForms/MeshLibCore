package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.TessellationSphereCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TessellationSphereCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new TessellationSphereCreator().create();
	}

	public void implementsCreatorInterface() {
		TessellationSphereCreator creator = new TessellationSphereCreator();
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
		Mesh3D mesh0 = new TessellationSphereCreator().create();
		Mesh3D mesh1 = new TessellationSphereCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new TessellationSphereCreator().create();
		Mesh3D mesh1 = new TessellationSphereCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetRadius() {
		float expected = 1.959028191556763E38f;
		TessellationSphereCreator creator = new TessellationSphereCreator();
		creator.setRadius(expected);
		Assert.assertEquals(expected, creator.getRadius(), 0);
	}

	@Test
	public void getRadiusReturnsDefaultValue() {
		float expected = 6.0f;
		TessellationSphereCreator creator = new TessellationSphereCreator();
		Assert.assertEquals(expected, creator.getRadius(), 0);
	}

	@Test
	public void getSetSubdivisions() {
		int expected = 2015978963;
		TessellationSphereCreator creator = new TessellationSphereCreator();
		creator.setSubdivisions(expected);
		Assert.assertEquals(expected, creator.getSubdivisions());
	}

	@Test
	public void getSubdivisionsReturnsDefaultValue() {
		int expected = 0;
		TessellationSphereCreator creator = new TessellationSphereCreator();
		Assert.assertEquals(expected, creator.getSubdivisions());
	}

	@Test
	public void getSetScaleExtrude() {
		float expected = 1.977341790953337E38f;
		TessellationSphereCreator creator = new TessellationSphereCreator();
		creator.setScaleExtrude(expected);
		Assert.assertEquals(expected, creator.getScaleExtrude(), 0);
	}

	@Test
	public void getScaleExtrudeReturnsDefaultValue() {
		float expected = 0.8f;
		TessellationSphereCreator creator = new TessellationSphereCreator();
		Assert.assertEquals(expected, creator.getScaleExtrude(), 0);
	}

	@Test
	public void getSetThickness() {
		float expected = 3.0311461337352987E38f;
		TessellationSphereCreator creator = new TessellationSphereCreator();
		creator.setThickness(expected);
		Assert.assertEquals(expected, creator.getThickness(), 0);
	}

	@Test
	public void getThicknessReturnsDefaultValue() {
		float expected = 0.1f;
		TessellationSphereCreator creator = new TessellationSphereCreator();
		Assert.assertEquals(expected, creator.getThickness(), 0);
	}

}
