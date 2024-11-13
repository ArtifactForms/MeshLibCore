package mesh.modifier.subdivision.test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.selection.FaceSelection;
import util.MeshTest;

public class PlanarVertexCenterCubeTest {

    private Mesh3D cube;

    private PlanarVertexCenterModifier modifier;

    @Before
    public void setUp() {
        cube = new CubeCreator().create();
        modifier = new PlanarVertexCenterModifier();
        modifier.modify(cube);
    }

    @Test
    public void resultMeshHasFourteenVertices() {
        MeshTest.assertVertexCountEquals(cube, 14);
    }

    @Test
    public void resultMeshHasTwentyFourFaces() {
        MeshTest.assertFaceCountEquals(cube, 24);
    }

    @Test
    public void resultMeshHasTwentyFourTriangularFaces() {
        assertTrue(MeshTest.isTriangleCountEquals(cube, 24));
    }

    @Test
    public void resultMeshHasThirtySixEdges() {
        MeshTest.assertEdgeCountEquals(cube, 36);
    }

    @Test
    public void resultMeshHasNoLooseVertices() {
        assertTrue(MeshTest.meshHasNoLooseVertices(cube));
    }

    @Test
    public void resultMeshHasNoDuplicatedFaces() {
        assertTrue(MeshTest.meshHasNoDuplicatedFaces(cube));
    }

    @Test
    public void resultMeshIsManifold() {
        assertTrue(MeshTest.isManifold(cube));
    }

    @Test
    public void resultMeshContainsFaceCentersOfOriginalCube() {
        Mesh3D original = new CubeCreator().create();
        for (Face3D face : original.getFaces()) {
            Vector3f faceCenter = original.calculateFaceCenter(face);
            Assert.assertTrue(cube.getVertices().contains(faceCenter));
        }
    }

    @Test
    public void resultMeshContainsOriginalCubeCoordinates() {
        Mesh3D original = new CubeCreator().create();
        for (Vector3f v : original.getVertices())
            Assert.assertTrue(cube.getVertices().contains(v));
    }

    @Test
    public void testTopFaceNormalsDirection() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectTopFaces();
        for (Face3D face : selection.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = cube.getVertexAt(face.indices[i]);
                Assert.assertEquals(-1, v.getY(), 0);
            }
        }
    }

    @Test
    public void testBottomFaceNormalsDirection() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectBottomFaces();
        for (Face3D face : selection.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = cube.getVertexAt(face.indices[i]);
                Assert.assertEquals(1, v.getY(), 0);
            }
        }
    }

    @Test
    public void testLeftFaceNormalsDirection() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectLeftFaces();
        for (Face3D face : selection.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = cube.getVertexAt(face.indices[i]);
                Assert.assertEquals(-1, v.getX(), 0);
            }
        }
    }

    @Test
    public void testRightFaceNormalsDirection() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectRightFaces();
        for (Face3D face : selection.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = cube.getVertexAt(face.indices[i]);
                Assert.assertEquals(1, v.getX(), 0);
            }
        }
    }

    @Test
    public void testBackFaceNormalsDirection() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectBackFaces();
        for (Face3D face : selection.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = cube.getVertexAt(face.indices[i]);
                Assert.assertEquals(-1, v.getZ(), 0);
            }
        }
    }

    @Test
    public void testFrontFaceNormalsDirection() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectFrontFaces();
        for (Face3D face : selection.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = cube.getVertexAt(face.indices[i]);
                Assert.assertEquals(1, v.getZ(), 0);
            }
        }
    }

    @Test
    public void resultMeshHasFourTopFaces() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectTopFaces();
        Assert.assertEquals(4, selection.getFaces().size());
    }

    @Test
    public void resultMeshHasFourBottomFaces() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectBottomFaces();
        Assert.assertEquals(4, selection.getFaces().size());
    }

    @Test
    public void resultMeshHasFourLeftFaces() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectLeftFaces();
        Assert.assertEquals(4, selection.getFaces().size());
    }

    @Test
    public void resultMeshHasFourRightFaces() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectRightFaces();
        Assert.assertEquals(4, selection.getFaces().size());
    }

    @Test
    public void resultMeshHasFourFrontFaces() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectFrontFaces();
        Assert.assertEquals(4, selection.getFaces().size());
    }

    @Test
    public void resultMeshHasFourBackFaces() {
        FaceSelection selection = new FaceSelection(cube);
        selection.selectBackFaces();
        Assert.assertEquals(4, selection.getFaces().size());
    }

}
