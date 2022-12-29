package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.special.SimpleStarCreator;

// Auto-generated test class to execute base tests for mesh creators
public class SimpleStarCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new SimpleStarCreator().create();
    }

    public void implementsCreatorInterface() {
	SimpleStarCreator creator = new SimpleStarCreator();
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
	Mesh3D mesh0 = new SimpleStarCreator().create();
	Mesh3D mesh1 = new SimpleStarCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new SimpleStarCreator().create();
	Mesh3D mesh1 = new SimpleStarCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetVertices() {
	int expected = 561336806;
	SimpleStarCreator creator = new SimpleStarCreator();
	creator.setVertices(expected);
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
	int expected = 5;
	SimpleStarCreator creator = new SimpleStarCreator();
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetHeight() {
	float expected = 1.993555675458562E38f;
	SimpleStarCreator creator = new SimpleStarCreator();
	creator.setHeight(expected);
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
	float expected = 0.5f;
	SimpleStarCreator creator = new SimpleStarCreator();
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetInnerRadius() {
	float expected = 1.1695287722549463E38f;
	SimpleStarCreator creator = new SimpleStarCreator();
	creator.setInnerRadius(expected);
	Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getInnerRadiusReturnsDefaultValue() {
	float expected = 0.5f;
	SimpleStarCreator creator = new SimpleStarCreator();
	Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getSetOuterRadius() {
	float expected = 9.490075404872977E37f;
	SimpleStarCreator creator = new SimpleStarCreator();
	creator.setOuterRadius(expected);
	Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

    @Test
    public void getOuterRadiusReturnsDefaultValue() {
	float expected = 1.0f;
	SimpleStarCreator creator = new SimpleStarCreator();
	Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

}
