package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.special.HoneyCombCreator;

// Auto-generated test class to execute base tests for mesh creators
public class HoneyCombCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new HoneyCombCreator().create();
    }

    public void implementsCreatorInterface() {
        HoneyCombCreator creator = new HoneyCombCreator();
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
        Assert.assertTrue(MeshTest.meshHasNoLooseVertices(mesh));
    }

    @Test
    public void createdMeshHasNoDuplicatedFaces() {
        // Running this test is very time expensive
        Assert.assertTrue(MeshTest.meshHasNoDuplicatedFaces(mesh));
    }

    @Test
    public void eachCallOfCreateReturnsNewUniqueMeshInstance() {
        Mesh3D mesh0 = new HoneyCombCreator().create();
        Mesh3D mesh1 = new HoneyCombCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new HoneyCombCreator().create();
        Mesh3D mesh1 = new HoneyCombCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetHeight() {
        float expected = 3.31474584629949E38f;
        HoneyCombCreator creator = new HoneyCombCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 0.2f;
        HoneyCombCreator creator = new HoneyCombCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetColCount() {
        int expected = 1493605407;
        HoneyCombCreator creator = new HoneyCombCreator();
        creator.setColCount(expected);
        Assert.assertEquals(expected, creator.getColCount());
    }

    @Test
    public void getColCountReturnsDefaultValue() {
        int expected = 2;
        HoneyCombCreator creator = new HoneyCombCreator();
        Assert.assertEquals(expected, creator.getColCount());
    }

    @Test
    public void getSetCellRadius() {
        float expected = 1.8199423120060088E38f;
        HoneyCombCreator creator = new HoneyCombCreator();
        creator.setCellRadius(expected);
        Assert.assertEquals(expected, creator.getCellRadius(), 0);
    }

    @Test
    public void getCellRadiusReturnsDefaultValue() {
        float expected = 0.5f;
        HoneyCombCreator creator = new HoneyCombCreator();
        Assert.assertEquals(expected, creator.getCellRadius(), 0);
    }

    @Test
    public void getSetInnerScale() {
        float expected = 7.404633815231409E37f;
        HoneyCombCreator creator = new HoneyCombCreator();
        creator.setInnerScale(expected);
        Assert.assertEquals(expected, creator.getInnerScale(), 0);
    }

    @Test
    public void getInnerScaleReturnsDefaultValue() {
        float expected = 0.9f;
        HoneyCombCreator creator = new HoneyCombCreator();
        Assert.assertEquals(expected, creator.getInnerScale(), 0);
    }

    @Test
    public void getSetRowCount() {
        int expected = 498291753;
        HoneyCombCreator creator = new HoneyCombCreator();
        creator.setRowCount(expected);
        Assert.assertEquals(expected, creator.getRowCount());
    }

    @Test
    public void getRowCountReturnsDefaultValue() {
        int expected = 2;
        HoneyCombCreator creator = new HoneyCombCreator();
        Assert.assertEquals(expected, creator.getRowCount());
    }

}
