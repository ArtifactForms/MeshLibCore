package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.DoubleConeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class DoubleConeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new DoubleConeCreator().create();
    }

    public void implementsCreatorInterface() {
        DoubleConeCreator creator = new DoubleConeCreator();
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
        Mesh3D mesh0 = new DoubleConeCreator().create();
        Mesh3D mesh1 = new DoubleConeCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new DoubleConeCreator().create();
        Mesh3D mesh1 = new DoubleConeCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 1.5227332086440567E38f;
        DoubleConeCreator creator = new DoubleConeCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        DoubleConeCreator creator = new DoubleConeCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetVertices() {
        int expected = 1047595407;
        DoubleConeCreator creator = new DoubleConeCreator();
        creator.setVertices(expected);
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
        int expected = 32;
        DoubleConeCreator creator = new DoubleConeCreator();
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetHeight() {
        float expected = 1.6832336178260156E38f;
        DoubleConeCreator creator = new DoubleConeCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 2.0f;
        DoubleConeCreator creator = new DoubleConeCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

}
