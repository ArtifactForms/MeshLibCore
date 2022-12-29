package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.beam.BeamLProfileCreator;

// Auto-generated test class to execute base tests for mesh creators
public class BeamLProfileCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new BeamLProfileCreator().create();
    }

    public void implementsCreatorInterface() {
	BeamLProfileCreator creator = new BeamLProfileCreator();
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
	Mesh3D mesh0 = new BeamLProfileCreator().create();
	Mesh3D mesh1 = new BeamLProfileCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new BeamLProfileCreator().create();
	Mesh3D mesh1 = new BeamLProfileCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetDepth() {
	float expected = 1.1804375911700091E38f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	creator.setDepth(expected);
	Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getDepthReturnsDefaultValue() {
	float expected = 2.0f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getSetHeight() {
	float expected = 2.076003942258668E38f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	creator.setHeight(expected);
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
	float expected = 0.85f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetWidth() {
	float expected = 3.2503018909163883E38f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	creator.setWidth(expected);
	Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getWidthReturnsDefaultValue() {
	float expected = 0.5f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getSetThickness() {
	float expected = 1.0709882714599256E38f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	creator.setThickness(expected);
	Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getThicknessReturnsDefaultValue() {
	float expected = 0.1f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getTaperReturnsDefaultValue() {
	float expected = 0.0f;
	BeamLProfileCreator creator = new BeamLProfileCreator();
	Assert.assertEquals(expected, creator.getTaper(), 0);
    }

}
