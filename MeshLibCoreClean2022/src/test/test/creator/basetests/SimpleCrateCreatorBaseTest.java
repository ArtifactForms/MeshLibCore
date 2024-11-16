package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.SimpleCrateCreator;

// Auto-generated test class to execute base tests for mesh creators
public class SimpleCrateCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new SimpleCrateCreator().create();
    }

    public void implementsCreatorInterface() {
        SimpleCrateCreator creator = new SimpleCrateCreator();
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
        Mesh3D mesh0 = new SimpleCrateCreator().create();
        Mesh3D mesh1 = new SimpleCrateCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new SimpleCrateCreator().create();
        Mesh3D mesh1 = new SimpleCrateCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetInset() {
        float expected = 3.1063418522281025E38f;
        SimpleCrateCreator creator = new SimpleCrateCreator();
        creator.setInset(expected);
        Assert.assertEquals(expected, creator.getInset(), 0);
    }

    @Test
    public void getInsetReturnsDefaultValue() {
        float expected = 0.3f;
        SimpleCrateCreator creator = new SimpleCrateCreator();
        Assert.assertEquals(expected, creator.getInset(), 0);
    }

    @Test
    public void getSetExtrudeAmount() {
        float expected = 3.018708483777774E38f;
        SimpleCrateCreator creator = new SimpleCrateCreator();
        creator.setExtrudeAmount(expected);
        Assert.assertEquals(expected, creator.getExtrudeAmount(), 0);
    }

    @Test
    public void getExtrudeAmountReturnsDefaultValue() {
        float expected = 0.1f;
        SimpleCrateCreator creator = new SimpleCrateCreator();
        Assert.assertEquals(expected, creator.getExtrudeAmount(), 0);
    }

}
