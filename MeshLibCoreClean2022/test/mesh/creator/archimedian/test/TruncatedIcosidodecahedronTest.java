package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedIcosidodecahedronCreator;
import util.MeshTest;

public class TruncatedIcosidodecahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TruncatedIcosidodecahedronCreator().create();
    }

    @Test
    public void hasOneHundredAndTwentyVertices() {
        Assert.assertEquals(120, mesh.getVertexCount());
    }

    @Test
    public void hasSixtyTwoFaces() {
        Assert.assertEquals(62, mesh.getFaceCount());
    }

    @Test
    public void hasThirtyQuadFaces() {
        MeshTest.assertQuadCountEquals(mesh, 30);
    }

    @Test
    public void hasTwentyHexagonFaces() {
        MeshTest.assertHexagonCountEquals(mesh, 20);
    }

    @Test
    public void hasTwelveDacagonFaces() {
        MeshTest.assertDecagonCountEquals(mesh, 12);
    }

    @Test
    public void hasOneHundredAndEightyEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 180);
    }

    @Test
    public void isManifold() {
        MeshTest.assertIsManifold(mesh);
    }

    @Test
    public void fulfillsEulerCharacteristic() {
        assertTrue(MeshTest.fulfillsEulerCharacteristic(mesh));
    }

    @Test
    public void everyEdgeHasLengthOfTwiceTheGoldenRatioMinusTwo() {
        float delta = 0.000001f;
        float expcted = 2 * Mathf.GOLDEN_RATIO - 2;
        MeshTest.assertEveryEdgeHasALengthOf(mesh, expcted, delta);
    }
    
    @Test
    public void testNormalsPointOutwards() {
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }

}
