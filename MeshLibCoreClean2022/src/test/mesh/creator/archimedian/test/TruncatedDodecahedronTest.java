package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedDodecahedronCreator;
import util.MeshTest;

public class TruncatedDodecahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TruncatedDodecahedronCreator().create();
    }

    @Test
    public void hasSixtyVertices() {
        assertEquals(60, mesh.getVertexCount());
    }

    @Test
    public void hasThirtyTwoFaces() {
        assertEquals(32, mesh.getFaceCount());
    }

    @Test
    public void hasTwentyTriangularFaces() {
        assertTrue(MeshTest.isTriangleCountEquals(mesh, 20));
    }

    @Test
    public void hasTwelveDecagons() {
        assertTrue(MeshTest.isDecagonCountEquals(mesh, 12));
    }

    @Test
    public void hasNinetyEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 90);
    }

    @Test
    public void isManifold() {
        assertTrue(MeshTest.isManifold(mesh));
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
