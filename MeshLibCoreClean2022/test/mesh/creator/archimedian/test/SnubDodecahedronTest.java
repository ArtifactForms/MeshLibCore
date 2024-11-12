package mesh.creator.archimedian.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.SnubDodecahedronCreator;
import util.MeshTest;

public class SnubDodecahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new SnubDodecahedronCreator().create();
    }

    @Test
    public void hasSixtyVertices() {
        Assert.assertEquals(60, mesh.getVertexCount());
    }

    @Test
    public void hasNintyTwoFaces() {
        Assert.assertEquals(92, mesh.getFaceCount());
    }

    @Test
    public void hasEightyTriangularFaces() {
        assertTrue(MeshTest.isTriangleCountEquals(mesh, 80));
    }

    @Test
    public void hasTwelvePentagonFaces() {
        assertTrue(MeshTest.isPentagonCountEquals(mesh, 12));
    }

    @Test
    public void hasHundredFiftyEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 150);
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
    public void testNormalsPointOutwards() {
        assertTrue(MeshTest.normalsPointOutwards(mesh));
    }

}
