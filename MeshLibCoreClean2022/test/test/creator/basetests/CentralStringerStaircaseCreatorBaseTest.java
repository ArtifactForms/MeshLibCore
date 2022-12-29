package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.CentralStringerStaircaseCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CentralStringerStaircaseCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new CentralStringerStaircaseCreator().create();
    }

    public void implementsCreatorInterface() {
	CentralStringerStaircaseCreator creator = new CentralStringerStaircaseCreator();
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
	Mesh3D mesh0 = new CentralStringerStaircaseCreator().create();
	Mesh3D mesh1 = new CentralStringerStaircaseCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new CentralStringerStaircaseCreator().create();
	Mesh3D mesh1 = new CentralStringerStaircaseCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRailingRotationSegments() {
	int expected = 898102948;
	CentralStringerStaircaseCreator creator = new CentralStringerStaircaseCreator();
	creator.setRailingRotationSegments(expected);
	Assert.assertEquals(expected, creator.getRailingRotationSegments());
    }

    @Test
    public void getRailingRotationSegmentsReturnsDefaultValue() {
	int expected = 8;
	CentralStringerStaircaseCreator creator = new CentralStringerStaircaseCreator();
	Assert.assertEquals(expected, creator.getRailingRotationSegments());
    }

    @Test
    public void getSetHeight() {
	float expected = 1.0119965029834056E38f;
	CentralStringerStaircaseCreator creator = new CentralStringerStaircaseCreator();
	creator.setHeight(expected);
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
	float expected = 4.0f;
	CentralStringerStaircaseCreator creator = new CentralStringerStaircaseCreator();
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetStepsCount() {
	int expected = 1064925831;
	CentralStringerStaircaseCreator creator = new CentralStringerStaircaseCreator();
	creator.setStepsCount(expected);
	Assert.assertEquals(expected, creator.getStepsCount());
    }

    @Test
    public void getStepsCountReturnsDefaultValue() {
	int expected = 20;
	CentralStringerStaircaseCreator creator = new CentralStringerStaircaseCreator();
	Assert.assertEquals(expected, creator.getStepsCount());
    }

}
