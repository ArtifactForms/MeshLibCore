package mesh.creator.primitives.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import util.MeshTest;

public class CubeCreatorTest {

    private void faceAtIndexHasExpectedFaceNormal(int index,
            Vector3f expected) {
        Mesh3D mesh = new CubeCreator().create();
        Face3D face = mesh.getFaces().get(index);
        assertEquals(expected, mesh.calculateFaceNormal(face));
    }

    @Test
    public void faceAtIndexZeroIsTopFace() {
        faceAtIndexHasExpectedFaceNormal(0, new Vector3f(0, -1, 0));
    }

    @Test
    public void faceAtIndexOneIsBottomFace() {
        faceAtIndexHasExpectedFaceNormal(1, new Vector3f(0, 1, 0));
    }

    @Test
    public void faceAtIndexTwoIsRightFace() {
        faceAtIndexHasExpectedFaceNormal(2, new Vector3f(1, 0, 0));
    }

    @Test
    public void faceAtIndexThreeIsFrontFace() {
        faceAtIndexHasExpectedFaceNormal(3, new Vector3f(0, 0, 1));
    }

    @Test
    public void faceAtIndexFourIsLeftFace() {
        faceAtIndexHasExpectedFaceNormal(4, new Vector3f(-1, 0, 0));
    }

    @Test
    public void faceAtIndexFiveIsBackFace() {
        faceAtIndexHasExpectedFaceNormal(5, new Vector3f(0, 0, -1));
    }

    @Test
    public void createdMeshHasSixFaces() {
        assertEquals(6, new CubeCreator().create().getFaceCount());
    }

    @Test
    public void createMeshHasEightVertices() {
        assertEquals(8, new CubeCreator().create().getVertexCount());
    }

    @Test
    public void createdMeshIsManifold() {
        Mesh3D mesh = new CubeCreator().create();
        assertTrue(MeshTest.isManifold(mesh));
    }

}
