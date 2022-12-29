package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.DresserCreator;

// Auto-generated test class to execute base tests for mesh creators
public class DresserCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new DresserCreator().create();
    }

    public void implementsCreatorInterface() {
	DresserCreator creator = new DresserCreator();
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
	Mesh3D mesh0 = new DresserCreator().create();
	Mesh3D mesh1 = new DresserCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new DresserCreator().create();
	Mesh3D mesh1 = new DresserCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetDepth() {
	float expected = 9.724452021401858E37f;
	DresserCreator creator = new DresserCreator();
	creator.setDepth(expected);
	Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getDepthReturnsDefaultValue() {
	float expected = 0.5f;
	DresserCreator creator = new DresserCreator();
	Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getSetHeight() {
	float expected = 2.3091114844522907E38f;
	DresserCreator creator = new DresserCreator();
	creator.setHeight(expected);
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
	float expected = 0.9f;
	DresserCreator creator = new DresserCreator();
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetDrawerRows() {
	int expected = 22245745;
	DresserCreator creator = new DresserCreator();
	creator.setDrawerRows(expected);
	Assert.assertEquals(expected, creator.getDrawerRows());
    }

    @Test
    public void getDrawerRowsReturnsDefaultValue() {
	int expected = 2;
	DresserCreator creator = new DresserCreator();
	Assert.assertEquals(expected, creator.getDrawerRows());
    }

    @Test
    public void getSetPanelThickness() {
	float expected = 2.642741570539911E38f;
	DresserCreator creator = new DresserCreator();
	creator.setPanelThickness(expected);
	Assert.assertEquals(expected, creator.getPanelThickness(), 0);
    }

    @Test
    public void getPanelThicknessReturnsDefaultValue() {
	float expected = 0.01f;
	DresserCreator creator = new DresserCreator();
	Assert.assertEquals(expected, creator.getPanelThickness(), 0);
    }

    @Test
    public void getSetDrawerCols() {
	int expected = 1362876085;
	DresserCreator creator = new DresserCreator();
	creator.setDrawerCols(expected);
	Assert.assertEquals(expected, creator.getDrawerCols());
    }

    @Test
    public void getDrawerColsReturnsDefaultValue() {
	int expected = 4;
	DresserCreator creator = new DresserCreator();
	Assert.assertEquals(expected, creator.getDrawerCols());
    }

    @Test
    public void getSetWidth() {
	float expected = 4.1930212190066024E37f;
	DresserCreator creator = new DresserCreator();
	creator.setWidth(expected);
	Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getWidthReturnsDefaultValue() {
	float expected = 1.5f;
	DresserCreator creator = new DresserCreator();
	Assert.assertEquals(expected, creator.getWidth(), 0);
    }

}
