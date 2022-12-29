package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.ArcCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ArcCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new ArcCreator().create();
    }

    public void implementsCreatorInterface() {
	ArcCreator creator = new ArcCreator();
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
    public void createdMeshHasNoDuplicatedFaces() {
	// Running this test is very time expensive
	MeshTest.assertMeshHasNoDuplicatedFaces(mesh);
    }

    @Test
    public void eachCallOfCreateReturnsNewUniqueMeshInstance() {
	Mesh3D mesh0 = new ArcCreator().create();
	Mesh3D mesh1 = new ArcCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new ArcCreator().create();
	Mesh3D mesh1 = new ArcCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
	float expected = 2.492942381332076E38f;
	ArcCreator creator = new ArcCreator();
	creator.setRadius(expected);
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
	float expected = 1.0f;
	ArcCreator creator = new ArcCreator();
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetStartAngle() {
	float expected = 1.18114154193016E38f;
	ArcCreator creator = new ArcCreator();
	creator.setStartAngle(expected);
	Assert.assertEquals(expected, creator.getStartAngle(), 0);
    }

    @Test
    public void getStartAngleReturnsDefaultValue() {
	float expected = 0.0f;
	ArcCreator creator = new ArcCreator();
	Assert.assertEquals(expected, creator.getStartAngle(), 0);
    }

    @Test
    public void getSetVertices() {
	int expected = 2013559457;
	ArcCreator creator = new ArcCreator();
	creator.setVertices(expected);
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
	int expected = 32;
	ArcCreator creator = new ArcCreator();
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetEndAngle() {
	float expected = 6.825403549379195E37f;
	ArcCreator creator = new ArcCreator();
	creator.setEndAngle(expected);
	Assert.assertEquals(expected, creator.getEndAngle(), 0);
    }

    @Test
    public void getEndAngleReturnsDefaultValue() {
	float expected = 6.2831855f;
	ArcCreator creator = new ArcCreator();
	Assert.assertEquals(expected, creator.getEndAngle(), 0);
    }

}
