package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.TruncatedConeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TruncatedConeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TruncatedConeCreator().create();
    }

    public void implementsCreatorInterface() {
        TruncatedConeCreator creator = new TruncatedConeCreator();
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
        Mesh3D mesh0 = new TruncatedConeCreator().create();
        Mesh3D mesh1 = new TruncatedConeCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new TruncatedConeCreator().create();
        Mesh3D mesh1 = new TruncatedConeCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetVertices() {
        int expected = 626180861;
        TruncatedConeCreator creator = new TruncatedConeCreator();
        creator.setVertices(expected);
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
        int expected = 32;
        TruncatedConeCreator creator = new TruncatedConeCreator();
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetHeight() {
        float expected = 1.8457551293161914E38f;
        TruncatedConeCreator creator = new TruncatedConeCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 2.0f;
        TruncatedConeCreator creator = new TruncatedConeCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetTopRadius() {
        float expected = 3.2235799396696372E38f;
        TruncatedConeCreator creator = new TruncatedConeCreator();
        creator.setTopRadius(expected);
        Assert.assertEquals(expected, creator.getTopRadius(), 0);
    }

    @Test
    public void getTopRadiusReturnsDefaultValue() {
        float expected = 0.5f;
        TruncatedConeCreator creator = new TruncatedConeCreator();
        Assert.assertEquals(expected, creator.getTopRadius(), 0);
    }

    @Test
    public void getSetBottomRadius() {
        float expected = 1.3175882769540362E38f;
        TruncatedConeCreator creator = new TruncatedConeCreator();
        creator.setBottomRadius(expected);
        Assert.assertEquals(expected, creator.getBottomRadius(), 0);
    }

    @Test
    public void getBottomRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        TruncatedConeCreator creator = new TruncatedConeCreator();
        Assert.assertEquals(expected, creator.getBottomRadius(), 0);
    }

}
