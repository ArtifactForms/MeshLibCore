package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.archimedian.CuboctahedronCreator;
import util.MeshTest;

public class CuboctahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new CuboctahedronCreator().create();
    }

    @Test
    public void hasTwelveVertices() {
        Assert.assertEquals(12, mesh.getVertexCount());
    }

    @Test
    public void hasFourteenFaces() {
        Assert.assertEquals(14, mesh.getFaceCount());
    }

    @Test
    public void hasEightTriangularFaces() {
        assertTrue(MeshTest.isTriangleCountEquals(mesh, 8));
    }

    @Test
    public void hasSixQuadFaces() {
        assertTrue(MeshTest.isQuadCountEquals(mesh, 6));
    }

    @Test
    public void hasTwentyFourEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 24);
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
