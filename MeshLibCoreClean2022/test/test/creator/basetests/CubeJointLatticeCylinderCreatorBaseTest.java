package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.CubeJointLatticeCylinderCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CubeJointLatticeCylinderCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new CubeJointLatticeCylinderCreator().create();
    }

    public void implementsCreatorInterface() {
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
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
	Mesh3D mesh0 = new CubeJointLatticeCylinderCreator().create();
	Mesh3D mesh1 = new CubeJointLatticeCylinderCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new CubeJointLatticeCylinderCreator().create();
	Mesh3D mesh1 = new CubeJointLatticeCylinderCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
	float expected = 2.9634839018968377E38f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	creator.setRadius(expected);
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
	float expected = 1.0f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetVertices() {
	int expected = 104925516;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	creator.setVertices(expected);
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
	int expected = 32;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetHeight() {
	float expected = 3.272698480738145E38f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	creator.setHeight(expected);
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
	float expected = 2.0f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetScale0() {
	float expected = 7.470445167471532E36f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	creator.setScale0(expected);
	Assert.assertEquals(expected, creator.getScale0(), 0);
    }

    @Test
    public void getScale0ReturnsDefaultValue() {
	float expected = 0.5f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	Assert.assertEquals(expected, creator.getScale0(), 0);
    }

    @Test
    public void getSetScale1() {
	float expected = 3.110907920815089E38f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	creator.setScale1(expected);
	Assert.assertEquals(expected, creator.getScale1(), 0);
    }

    @Test
    public void getScale1ReturnsDefaultValue() {
	float expected = 0.5f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	Assert.assertEquals(expected, creator.getScale1(), 0);
    }

    @Test
    public void getSetSubdivisionsY() {
	int expected = 1598431422;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	creator.setSubdivisionsY(expected);
	Assert.assertEquals(expected, creator.getSubdivisionsY());
    }

    @Test
    public void getSubdivisionsYReturnsDefaultValue() {
	int expected = 3;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	Assert.assertEquals(expected, creator.getSubdivisionsY());
    }

    @Test
    public void getSetJointSize() {
	float expected = 3.030030255492517E38f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	creator.setJointSize(expected);
	Assert.assertEquals(expected, creator.getJointSize(), 0);
    }

    @Test
    public void getJointSizeReturnsDefaultValue() {
	float expected = 0.01f;
	CubeJointLatticeCylinderCreator creator = new CubeJointLatticeCylinderCreator();
	Assert.assertEquals(expected, creator.getJointSize(), 0);
    }

}
