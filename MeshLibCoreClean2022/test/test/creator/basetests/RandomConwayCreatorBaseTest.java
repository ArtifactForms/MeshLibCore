package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.conway.RandomConwayCreator;

// Auto-generated test class to execute base tests for mesh creators
public class RandomConwayCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new RandomConwayCreator().create();
    }

    public void implementsCreatorInterface() {
        RandomConwayCreator creator = new RandomConwayCreator();
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
        Mesh3D mesh0 = new RandomConwayCreator().create();
        Mesh3D mesh1 = new RandomConwayCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void getSetN() {
        int expected = 1473779600;
        RandomConwayCreator creator = new RandomConwayCreator();
        creator.setN(expected);
        Assert.assertEquals(expected, creator.getN());
    }

}
