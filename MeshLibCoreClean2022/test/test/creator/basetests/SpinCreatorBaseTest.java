package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.special.SpinCreator;

// Auto-generated test class to execute base tests for mesh creators
public class SpinCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new SpinCreator().create();
    }

    public void implementsCreatorInterface() {
        SpinCreator creator = new SpinCreator();
        Assert.assertTrue(creator instanceof IMeshCreator);
    }

    @Test
    public void createdMeshIsNotNullByDefault() {
        Assert.assertNotNull(mesh);
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
        Mesh3D mesh0 = new SpinCreator().create();
        Mesh3D mesh1 = new SpinCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new SpinCreator().create();
        Mesh3D mesh1 = new SpinCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetAngle() {
        float expected = 2.9568454990133953E38f;
        SpinCreator creator = new SpinCreator();
        creator.setAngle(expected);
        Assert.assertEquals(expected, creator.getAngle(), 0);
    }

    @Test
    public void getAngleReturnsDefaultValue() {
        float expected = 0.7853982f;
        SpinCreator creator = new SpinCreator();
        Assert.assertEquals(expected, creator.getAngle(), 0);
    }

    @Test
    public void getSetClose() {
        boolean expected = true;
        SpinCreator creator = new SpinCreator();
        creator.setClose(expected);
        Assert.assertEquals(expected, creator.isClose());
    }

    @Test
    public void getCloseReturnsDefaultValue() {
        boolean expected = false;
        SpinCreator creator = new SpinCreator();
        Assert.assertEquals(expected, creator.isClose());
    }

    @Test
    public void getSetSteps() {
        int expected = 778386027;
        SpinCreator creator = new SpinCreator();
        creator.setSteps(expected);
        Assert.assertEquals(expected, creator.getSteps());
    }

    @Test
    public void getStepsReturnsDefaultValue() {
        int expected = 9;
        SpinCreator creator = new SpinCreator();
        Assert.assertEquals(expected, creator.getSteps());
    }

}
