package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.SegmentedTubeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class SegmentedTubeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new SegmentedTubeCreator().create();
    }

    public void implementsCreatorInterface() {
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
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
	Mesh3D mesh0 = new SegmentedTubeCreator().create();
	Mesh3D mesh1 = new SegmentedTubeCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new SegmentedTubeCreator().create();
	Mesh3D mesh1 = new SegmentedTubeCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetSegments() {
	int expected = 309263485;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	creator.setSegments(expected);
	Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSegmentsReturnsDefaultValue() {
	int expected = 2;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSetVertices() {
	int expected = 551738060;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	creator.setVertices(expected);
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
	int expected = 32;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetHeight() {
	float expected = 2.5413409140338182E38f;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	creator.setHeight(expected);
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
	float expected = 2.0f;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetInnerRadius() {
	float expected = 2.962735806659614E38f;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	creator.setInnerRadius(expected);
	Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getInnerRadiusReturnsDefaultValue() {
	float expected = 0.5f;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getSetOuterRadius() {
	float expected = 2.5768663778487008E38f;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	creator.setOuterRadius(expected);
	Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

    @Test
    public void getOuterRadiusReturnsDefaultValue() {
	float expected = 1.0f;
	SegmentedTubeCreator creator = new SegmentedTubeCreator();
	Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

}
