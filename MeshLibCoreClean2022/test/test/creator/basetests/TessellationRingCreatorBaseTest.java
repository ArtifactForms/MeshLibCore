package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.creative.TessellationRingCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TessellationRingCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TessellationRingCreator().create();
    }

    public void implementsCreatorInterface() {
        TessellationRingCreator creator = new TessellationRingCreator();
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
        Mesh3D mesh0 = new TessellationRingCreator().create();
        Mesh3D mesh1 = new TessellationRingCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new TessellationRingCreator().create();
        Mesh3D mesh1 = new TessellationRingCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetVertices() {
        int expected = 274320435;
        TessellationRingCreator creator = new TessellationRingCreator();
        creator.setVertices(expected);
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
        int expected = 12;
        TessellationRingCreator creator = new TessellationRingCreator();
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetTopRadius() {
        float expected = 7.051034161668738E37f;
        TessellationRingCreator creator = new TessellationRingCreator();
        creator.setTopRadius(expected);
        Assert.assertEquals(expected, creator.getTopRadius(), 0);
    }

    @Test
    public void getTopRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        TessellationRingCreator creator = new TessellationRingCreator();
        Assert.assertEquals(expected, creator.getTopRadius(), 0);
    }

    @Test
    public void getSetBottomRadius() {
        float expected = 2.18425612144677E38f;
        TessellationRingCreator creator = new TessellationRingCreator();
        creator.setBottomRadius(expected);
        Assert.assertEquals(expected, creator.getBottomRadius(), 0);
    }

    @Test
    public void getBottomRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        TessellationRingCreator creator = new TessellationRingCreator();
        Assert.assertEquals(expected, creator.getBottomRadius(), 0);
    }

    @Test
    public void getSetExtrudeScale() {
        float expected = 1.1276402893533473E38f;
        TessellationRingCreator creator = new TessellationRingCreator();
        creator.setExtrudeScale(expected);
        Assert.assertEquals(expected, creator.getExtrudeScale(), 0);
    }

    @Test
    public void getExtrudeScaleReturnsDefaultValue() {
        float expected = 0.6f;
        TessellationRingCreator creator = new TessellationRingCreator();
        Assert.assertEquals(expected, creator.getExtrudeScale(), 0);
    }

    @Test
    public void getSetThickness() {
        float expected = 9.154859422836275E37f;
        TessellationRingCreator creator = new TessellationRingCreator();
        creator.setThickness(expected);
        Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getThicknessReturnsDefaultValue() {
        float expected = 0.1f;
        TessellationRingCreator creator = new TessellationRingCreator();
        Assert.assertEquals(expected, creator.getThickness(), 0);
    }

}
