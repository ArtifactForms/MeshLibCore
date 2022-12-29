package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.MedievalTowerRoundCreator;

// Auto-generated test class to execute base tests for mesh creators
public class MedievalTowerRoundCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new MedievalTowerRoundCreator().create();
    }

    public void implementsCreatorInterface() {
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
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
	Mesh3D mesh0 = new MedievalTowerRoundCreator().create();
	Mesh3D mesh1 = new MedievalTowerRoundCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new MedievalTowerRoundCreator().create();
	Mesh3D mesh1 = new MedievalTowerRoundCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetFloorSegments() {
	int expected = 2119803232;
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
	creator.setFloorSegments(expected);
	Assert.assertEquals(expected, creator.getFloorSegments());
    }

    @Test
    public void getFloorSegmentsReturnsDefaultValue() {
	int expected = 2;
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
	Assert.assertEquals(expected, creator.getFloorSegments());
    }

    @Test
    public void getSetRadius() {
	float expected = 5.254806828048337E37f;
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
	creator.setRadius(expected);
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
	float expected = 3.5f;
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetRotationSegments() {
	int expected = 605505905;
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
	creator.setRotationSegments(expected);
	Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getRotationSegmentsReturnsDefaultValue() {
	int expected = 12;
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
	Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getSetFloorSegmentHeight() {
	float expected = 3.0132808616068446E38f;
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
	creator.setFloorSegmentHeight(expected);
	Assert.assertEquals(expected, creator.getFloorSegmentHeight(), 0);
    }

    @Test
    public void getFloorSegmentHeightReturnsDefaultValue() {
	float expected = 3.5f;
	MedievalTowerRoundCreator creator = new MedievalTowerRoundCreator();
	Assert.assertEquals(expected, creator.getFloorSegmentHeight(), 0);
    }

}
