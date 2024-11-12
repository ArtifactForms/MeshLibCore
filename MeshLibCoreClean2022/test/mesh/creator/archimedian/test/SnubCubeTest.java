package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
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
        Assert.assertEquals(24, mesh.getVertexCount());
    }

    @Test
    public void hasThirtyEightFaces() {
        Assert.assertEquals(38, mesh.getFaceCount());
    }

    @Test
    public void hasThirtyTwoTriangularFaces() {
        MeshTest.assertTriangleCountEquals(mesh, 32);
    }

    @Test
    public void hasSixQuadFaces() {
        MeshTest.assertQuadCountEquals(mesh, 6);
    }

    @Test
    public void hasSixtyEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 60);
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
