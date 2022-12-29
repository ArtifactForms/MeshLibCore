package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.PillarCreator;

// Auto-generated test class to execute base tests for mesh creators
public class PillarCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new PillarCreator().create();
    }

    public void implementsCreatorInterface() {
	PillarCreator creator = new PillarCreator();
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
	Mesh3D mesh0 = new PillarCreator().create();
	Mesh3D mesh1 = new PillarCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new PillarCreator().create();
	Mesh3D mesh1 = new PillarCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
	float expected = 5.917060713609388E37f;
	PillarCreator creator = new PillarCreator();
	creator.setRadius(expected);
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
	float expected = 1.0f;
	PillarCreator creator = new PillarCreator();
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetRotationSegments() {
	int expected = 590678556;
	PillarCreator creator = new PillarCreator();
	creator.setRotationSegments(expected);
	Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getRotationSegmentsReturnsDefaultValue() {
	int expected = 8;
	PillarCreator creator = new PillarCreator();
	Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getSetBottomHeight() {
	float expected = 3.3077751008020038E38f;
	PillarCreator creator = new PillarCreator();
	creator.setBottomHeight(expected);
	Assert.assertEquals(expected, creator.getBottomHeight(), 0);
    }

    @Test
    public void getBottomHeightReturnsDefaultValue() {
	float expected = 2.0f;
	PillarCreator creator = new PillarCreator();
	Assert.assertEquals(expected, creator.getBottomHeight(), 0);
    }

    @Test
    public void getSetTopHeight() {
	float expected = 1.2322241318886173E38f;
	PillarCreator creator = new PillarCreator();
	creator.setTopHeight(expected);
	Assert.assertEquals(expected, creator.getTopHeight(), 0);
    }

    @Test
    public void getTopHeightReturnsDefaultValue() {
	float expected = 1.0f;
	PillarCreator creator = new PillarCreator();
	Assert.assertEquals(expected, creator.getTopHeight(), 0);
    }

    @Test
    public void getSetCenterHeight() {
	float expected = 2.0433889507623635E38f;
	PillarCreator creator = new PillarCreator();
	creator.setCenterHeight(expected);
	Assert.assertEquals(expected, creator.getCenterHeight(), 0);
    }

    @Test
    public void getCenterHeightReturnsDefaultValue() {
	float expected = 5.0f;
	PillarCreator creator = new PillarCreator();
	Assert.assertEquals(expected, creator.getCenterHeight(), 0);
    }

    @Test
    public void getSetBottomSegments() {
	int expected = 2032124513;
	PillarCreator creator = new PillarCreator();
	creator.setBottomSegments(expected);
	Assert.assertEquals(expected, creator.getBottomSegments());
    }

    @Test
    public void getBottomSegmentsReturnsDefaultValue() {
	int expected = 2;
	PillarCreator creator = new PillarCreator();
	Assert.assertEquals(expected, creator.getBottomSegments());
    }

    @Test
    public void getSetTopSegments() {
	int expected = 987293035;
	PillarCreator creator = new PillarCreator();
	creator.setTopSegments(expected);
	Assert.assertEquals(expected, creator.getTopSegments());
    }

    @Test
    public void getTopSegmentsReturnsDefaultValue() {
	int expected = 2;
	PillarCreator creator = new PillarCreator();
	Assert.assertEquals(expected, creator.getTopSegments());
    }

}
