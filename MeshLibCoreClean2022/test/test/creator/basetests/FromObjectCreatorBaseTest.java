package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.FromObjectCreator;

// Auto-generated test class to execute base tests for mesh creators
public class FromObjectCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new FromObjectCreator().create();
    }

    public void implementsCreatorInterface() {
	FromObjectCreator creator = new FromObjectCreator();
	Assert.assertTrue(creator instanceof IMeshCreator);
    }

    @Test
    public void createdMeshIsNotNullByDefault() {
	Assert.assertNotNull(mesh);
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
	Mesh3D mesh0 = new FromObjectCreator().create();
	Mesh3D mesh1 = new FromObjectCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new FromObjectCreator().create();
	Mesh3D mesh1 = new FromObjectCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetScale() {
	float expected = 7.44804379664772E37f;
	FromObjectCreator creator = new FromObjectCreator();
	creator.setScale(expected);
	Assert.assertEquals(expected, creator.getScale(), 0);
    }

    @Test
    public void getScaleReturnsDefaultValue() {
	float expected = 1.0f;
	FromObjectCreator creator = new FromObjectCreator();
	Assert.assertEquals(expected, creator.getScale(), 0);
    }

}
