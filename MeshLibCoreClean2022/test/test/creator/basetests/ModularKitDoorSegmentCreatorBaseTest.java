package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.ModularKitDoorSegmentCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ModularKitDoorSegmentCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new ModularKitDoorSegmentCreator().create();
    }

    public void implementsCreatorInterface() {
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
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
	Mesh3D mesh0 = new ModularKitDoorSegmentCreator().create();
	Mesh3D mesh1 = new ModularKitDoorSegmentCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new ModularKitDoorSegmentCreator().create();
	Mesh3D mesh1 = new ModularKitDoorSegmentCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetDoorHeight() {
	float expected = 2.2217039659519194E37f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	creator.setDoorHeight(expected);
	Assert.assertEquals(expected, creator.getDoorHeight(), 0);
    }

    @Test
    public void getDoorHeightReturnsDefaultValue() {
	float expected = 2.0f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	Assert.assertEquals(expected, creator.getDoorHeight(), 0);
    }

    @Test
    public void getSetSegmentWidth() {
	float expected = 2.731461790062248E38f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	creator.setSegmentWidth(expected);
	Assert.assertEquals(expected, creator.getSegmentWidth(), 0);
    }

    @Test
    public void getSegmentWidthReturnsDefaultValue() {
	float expected = 2.0f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	Assert.assertEquals(expected, creator.getSegmentWidth(), 0);
    }

    @Test
    public void getSetDoorWidth() {
	float expected = 3.026384920048376E38f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	creator.setDoorWidth(expected);
	Assert.assertEquals(expected, creator.getDoorWidth(), 0);
    }

    @Test
    public void getDoorWidthReturnsDefaultValue() {
	float expected = 1.0f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	Assert.assertEquals(expected, creator.getDoorWidth(), 0);
    }

    @Test
    public void getSetSegmentHeight() {
	float expected = 2.6102435465799415E38f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	creator.setSegmentHeight(expected);
	Assert.assertEquals(expected, creator.getSegmentHeight(), 0);
    }

    @Test
    public void getSegmentHeightReturnsDefaultValue() {
	float expected = 3.0f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	Assert.assertEquals(expected, creator.getSegmentHeight(), 0);
    }

    @Test
    public void getSetSegmentDepth() {
	float expected = 2.601711058294991E37f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	creator.setSegmentDepth(expected);
	Assert.assertEquals(expected, creator.getSegmentDepth(), 0);
    }

    @Test
    public void getSegmentDepthReturnsDefaultValue() {
	float expected = 0.0f;
	ModularKitDoorSegmentCreator creator = new ModularKitDoorSegmentCreator();
	Assert.assertEquals(expected, creator.getSegmentDepth(), 0);
    }

}
