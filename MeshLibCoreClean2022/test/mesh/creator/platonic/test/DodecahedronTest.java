package mesh.creator.platonic.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.platonic.DodecahedronCreator;
import util.MeshTest;

public class DodecahedronTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new DodecahedronCreator().create();
    }

    @Test
    public void hasTwentyVertices() {
        MeshTest.assertVertexCountEquals(mesh, 20);
    }

    @Test
    public void hasTwelveFaces() {
        MeshTest.assertFaceCountEquals(mesh, 12);
    }

    @Test
    public void hasThirtyEdges() {
        MeshTest.assertEdgeCountEquals(mesh, 30);
    }

    @Test
    public void isManifold() {
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void hasTwelvePentagonFaces() {
        assertTrue(MeshTest.isPentagonCountEquals(mesh, 12));
    }

    @Test
    public void fulfillsEulerCharacteristic() {
        assertTrue(MeshTest.fulfillsEulerCharacteristic(mesh));
    }

    @Test
    public void everyEdgeHasLengthOfApproxOne() {
        float delta = 0.000001f;
        MeshTest.assertEveryEdgeHasALengthOf(mesh, 1, delta);
    }

}
