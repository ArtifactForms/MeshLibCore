package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedCuboctahedronCreator;
import util.MeshTest;

public class TruncatedCuboctahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TruncatedCuboctahedronCreator().create();
    }

    @Test
    public void hasFourtyEightVertices() {
        Assert.assertEquals(48, mesh.getVertexCount());
    }

    @Test
    public void hasTwentySixFaces() {
        Assert.assertEquals(26, mesh.getFaceCount());
    }

    @Test
    public void hasTwelveQuadFaces() {
        assertTrue(MeshTest.isQuadCountEquals(mesh, 12));
    }

    @Test
    public void hasEightHexagonFaces() {
        assertTrue(MeshTest.isHexagonCountEquals(mesh, 8));
    }

    @Test
    public void hasSixOctagonFaces() {
        assertTrue(MeshTest.isOctagonCountEquals(mesh, 6));
    }

    @Test
    public void hasSeventyTwoEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 72);
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
    public void everyEdgeHasLengthOfTwo() {
        float delta = 0.000001f;
        MeshTest.assertEveryEdgeHasALengthOf(mesh, 2, delta);
    }
    
    @Test
    public void testNormalsPointOutwards() {
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }

}
