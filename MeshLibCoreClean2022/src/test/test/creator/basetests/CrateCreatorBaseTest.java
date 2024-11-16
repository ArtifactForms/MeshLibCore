package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.CrateCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CrateCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new CrateCreator().create();
    }

    public void implementsCreatorInterface() {
        CrateCreator creator = new CrateCreator();
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
        Mesh3D mesh0 = new CrateCreator().create();
        Mesh3D mesh1 = new CrateCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new CrateCreator().create();
        Mesh3D mesh1 = new CrateCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 2.6804687665969917E38f;
        CrateCreator creator = new CrateCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        CrateCreator creator = new CrateCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetInset() {
        float expected = 6.634873549127037E37f;
        CrateCreator creator = new CrateCreator();
        creator.setInset(expected);
        Assert.assertEquals(expected, creator.getInset(), 0);
    }

    @Test
    public void getInsetReturnsDefaultValue() {
        float expected = 0.2f;
        CrateCreator creator = new CrateCreator();
        Assert.assertEquals(expected, creator.getInset(), 0);
    }

    @Test
    public void getSetInsetDepth() {
        float expected = 1.0734435598953797E38f;
        CrateCreator creator = new CrateCreator();
        creator.setInsetDepth(expected);
        Assert.assertEquals(expected, creator.getInsetDepth(), 0);
    }

    @Test
    public void getInsetDepthReturnsDefaultValue() {
        float expected = 0.1f;
        CrateCreator creator = new CrateCreator();
        Assert.assertEquals(expected, creator.getInsetDepth(), 0);
    }

}
