package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.RhombicosidodecahedronCreator;
import util.MeshTest;

public class RhombicosidodecahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new RhombicosidodecahedronCreator().create();
    }

    @Test
    public void hasSixtyVertices() {
        Assert.assertEquals(60, mesh.getVertexCount());
    }

    @Test
    public void hasSixtyTwoFaces() {
        Assert.assertEquals(62, mesh.getFaceCount());
    }

    @Test
    public void hasTwentyTriangularFaces() {
        assertTrue(MeshTest.isTriangleCountEquals(mesh, 20));
    }

    @Test
    public void hasThirtyQuadFaces() {
        assertTrue(MeshTest.isQuadCountEquals(mesh, 30));
    }

    @Test
    public void hasTwelvePentagonFaces() {
        assertTrue(MeshTest.isPentagonCountEquals(mesh, 12));
    }

    @Test
    public void hasHundredTwentyEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 120);
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
    public void everyEdgeHasALengthOfTwo() {
        float delta = 0.00001f;
        MeshTest.assertEveryEdgeHasALengthOf(mesh, 2, delta);
    }
    
    @Test
    public void testNormalsPointOutwards() {
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }

}
