package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.wip.ChainCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ChainCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new ChainCreator().create();
    }

    public void implementsCreatorInterface() {
	ChainCreator creator = new ChainCreator();
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
	Mesh3D mesh0 = new ChainCreator().create();
	Mesh3D mesh1 = new ChainCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new ChainCreator().create();
	Mesh3D mesh1 = new ChainCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetMajorRadius() {
	float expected = 3.0344889193112182E38f;
	ChainCreator creator = new ChainCreator();
	creator.setMajorRadius(expected);
	Assert.assertEquals(expected, creator.getMajorRadius(), 0);
    }

    @Test
    public void getMajorRadiusReturnsDefaultValue() {
	float expected = 0.5f;
	ChainCreator creator = new ChainCreator();
	Assert.assertEquals(expected, creator.getMajorRadius(), 0);
    }

    @Test
    public void getSetLinks() {
	int expected = 1434846585;
	ChainCreator creator = new ChainCreator();
	creator.setLinks(expected);
	Assert.assertEquals(expected, creator.getLinks());
    }

    @Test
    public void getLinksReturnsDefaultValue() {
	int expected = 5;
	ChainCreator creator = new ChainCreator();
	Assert.assertEquals(expected, creator.getLinks());
    }

}
