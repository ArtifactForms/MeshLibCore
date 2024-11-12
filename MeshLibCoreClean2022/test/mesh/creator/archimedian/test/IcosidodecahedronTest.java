package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.IcosidodecahedronCreator;
import util.MeshTest;

public class IcosidodecahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new IcosidodecahedronCreator().create();
    }

    @Test
    public void hasThirtyVertices() {
        Assert.assertEquals(30, mesh.getVertexCount());
    }

    @Test
    public void hasThirtyTwoFaces() {
        Assert.assertEquals(32, mesh.getFaceCount());
    }

    @Test
    public void hasTwentyTriangularFaces() {
        MeshTest.assertTriangleCountEquals(mesh, 20);
    }

    @Test
    public void hasTwelvePentagonFaces() {
        MeshTest.assertPentagonCountEquals(mesh, 12);
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
        assertTrue(MeshTest.fulfillsEulerCharacteristic(mesh));
    }

    @Test
    public void everyEdgeHasALengthOfOneIfRadiusIsGoldenRatio() {
        float delta = 0.000001f;
        float expectedEdgeLength = 1f;
        MeshTest.assertEveryEdgeHasALengthOf(mesh, expectedEdgeLength, delta);
    }
    
    @Test
    public void testNormalsPointOutwards() {
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }

}
