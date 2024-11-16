package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedOctahedronCreator;
import util.MeshTest;

public class TruncatedOctahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TruncatedOctahedronCreator().create();
    }

    @Test
    public void hasTwentyFourVertices() {
        assertEquals(24, mesh.getVertexCount());
    }

    @Test
    public void hasFourteenFaces() {
        assertEquals(14, mesh.getFaceCount());
    }

    @Test
    public void hasSixQuadFaces() {
        assertTrue(MeshTest.isQuadCountEquals(mesh, 6));
    }

    @Test
    public void hasEightHexagonFaces() {
        assertTrue(MeshTest.isHexagonCountEquals(mesh, 8));
    }

    @Test
    public void hasThirtySixEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 36);
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
    public void everyEdgeHasLengthOfSqrtOfTwo() {
        MeshTest.assertEveryEdgeHasALengthOf(mesh, Mathf.sqrt(2), 0);
    }
    
    @Test
    public void testNormalsPointOutwards() {
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }

}
