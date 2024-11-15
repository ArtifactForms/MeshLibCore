package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.RhombicuboctahedronCreator;
import util.MeshTest;

public class RhombicuboctahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new RhombicuboctahedronCreator().create();
    }

    @Test
    public void hasTwentyFourVertices() {
        assertEquals(24, mesh.getVertexCount());
    }

    @Test
    public void hasTwentySixFaces() {
        assertEquals(26, mesh.getFaceCount());
    }

    @Test
    public void hasEightTriangularFaces() {
        assertTrue(MeshTest.isTriangleCountEquals(mesh, 8));
    }

    @Test
    public void hasEighteenQuadFaces() {
        assertTrue(MeshTest.isQuadCountEquals(mesh, 18));
    }

    @Test
    public void hasFourtyEightEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 48);
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
    public void everyEdgeHasLengthOfTwo() {
        MeshTest.assertEveryEdgeHasALengthOf(mesh, 2, 0);
    }

    @Test
    public void testNormalsPointOutwards() {
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }

}
