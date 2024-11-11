package mesh.creator.archimedian.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedIcosahedronCreator;
import util.MeshTest;

public class TruncatedIcosahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TruncatedIcosahedronCreator().create();
    }

    @Test
    public void hasSixtyVertices() {
        Assert.assertEquals(60, mesh.getVertexCount());
    }

    @Test
    public void hasThirtyTwoFaces() {
        Assert.assertEquals(32, mesh.getFaceCount());
    }

    @Test
    public void hasTwelvePentagonFaces() {
        MeshTest.assertPentagonCountEquals(mesh, 12);
    }

    @Test
    public void hasTwentyHexagonFaces() {
        MeshTest.assertHexagonCountEquals(mesh, 20);
    }

    @Test
    public void hasNinetyEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 90);
    }

    @Test
    public void isManifold() {
        MeshTest.assertIsManifold(mesh);
    }

    @Test
    public void fulfillsEulerCharacteristic() {
        MeshTest.assertFulfillsEulerCharacteristic(mesh);
    }

    @Test
    public void everyEdgeHasALengthOfTwo() {
        float delta = 0.00001f;
        MeshTest.assertEveryEdgeHasALengthOf(mesh, 2, delta);
    }

}
