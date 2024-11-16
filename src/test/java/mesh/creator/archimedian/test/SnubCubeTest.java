package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.archimedian.SnubCubeCreator;
import util.MeshTest;

public class SnubCubeTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new SnubCubeCreator().create();
    }

    @Test
    public void hasTwentyFourVertices() {
        assertEquals(24, mesh.getVertexCount());
    }

    @Test
    public void hasThirtyEightFaces() {
        assertEquals(38, mesh.getFaceCount());
    }

    @Test
    public void hasThirtyTwoTriangularFaces() {
        assertTrue(MeshTest.isTriangleCountEquals(mesh, 32));
    }

    @Test
    public void hasSixQuadFaces() {
        assertTrue(MeshTest.isQuadCountEquals(mesh, 6));
    }

    @Test
    public void hasSixtyEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 60);
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
    public void everyEdgeHasLengthOfAlpha() {
        float t = Mathf.TRIBONACCI_CONSTANT;
        float alpha = Mathf.sqrt(2 + (4 * t) - (2 * t * t));
        float delta = 0.000001f;
        MeshTest.assertEveryEdgeHasALengthOf(mesh, alpha, delta);
    }
    
    @Test
    public void testNormalsPointOutwards() {
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }

}