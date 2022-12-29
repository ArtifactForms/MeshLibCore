package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.creative.PortedCubeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class PortedCubeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new PortedCubeCreator().create();
    }

    public void implementsCreatorInterface() {
	PortedCubeCreator creator = new PortedCubeCreator();
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
	Mesh3D mesh0 = new PortedCubeCreator().create();
	Mesh3D mesh1 = new PortedCubeCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new PortedCubeCreator().create();
	Mesh3D mesh1 = new PortedCubeCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetSubdivisions() {
	int expected = 1419571449;
	PortedCubeCreator creator = new PortedCubeCreator();
	creator.setSubdivisions(expected);
	Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSubdivisionsReturnsDefaultValue() {
	int expected = 1;
	PortedCubeCreator creator = new PortedCubeCreator();
	Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSetRemoveCorners() {
	boolean expected = true;
	PortedCubeCreator creator = new PortedCubeCreator();
	creator.setRemoveCorners(expected);
	Assert.assertEquals(expected, creator.isRemoveCorners());
    }

    @Test
    public void getRemoveCornersReturnsDefaultValue() {
	boolean expected = true;
	PortedCubeCreator creator = new PortedCubeCreator();
	Assert.assertEquals(expected, creator.isRemoveCorners());
    }

    @Test
    public void getSetThickness() {
	float expected = 1.2954825760924337E38f;
	PortedCubeCreator creator = new PortedCubeCreator();
	creator.setThickness(expected);
	Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getThicknessReturnsDefaultValue() {
	float expected = 0.1f;
	PortedCubeCreator creator = new PortedCubeCreator();
	Assert.assertEquals(expected, creator.getThickness(), 0);
    }

}
