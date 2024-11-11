package mesh.creator.archimedian.test;

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
        MeshTest.assertTriangleCountEquals(mesh, 8);
    }

    @Test
    public void hasSixQuadFaces() {
        MeshTest.assertQuadCountEquals(mesh, 6);
    }

    @Test
    public void hasTwentyFourEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 24);
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
    public void everyEdgeHasLengthOfSqrtOfTwo() {
        MeshTest.assertEveryEdgeHasALengthOf(mesh, Mathf.sqrt(2), 0);
    }

}
