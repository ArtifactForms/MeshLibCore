package mesh.modifier.subdivision.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.repair.UpdateFaceNormalsModifier;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import util.MeshTestUtil;

public class PlanarVertexCenterPlaneTest {

    private PlanarVertexCenterModifier modifier;

    private Mesh3D plane;

    @Before
    public void setUp() {
        modifier = new PlanarVertexCenterModifier();
        plane = new PlaneCreator().create();
        modifier.modify(plane);
    }

    @Test
    public void resultMeshHasFourFaces() {
        int expectedFaceCount = 4;
        assertEquals(expectedFaceCount, plane.getFaceCount());
        assertEquals(expectedFaceCount, plane.faces.size());
        assertEquals(expectedFaceCount, plane.getFaces().size());
        assertEquals(
                expectedFaceCount,
                plane.getFaces(0, plane.getFaceCount()).size()
        );
    }

    @Test
    public void resultMeshConsistsOfTrianglesOnly() {
        assertTrue(MeshTestUtil.isTriangleCountEquals(plane, 4));
    }

    @Test
    public void resultMeshHasEightEdges() {
        MeshTestUtil.assertEdgeCountEquals(plane, 8);
    }

    @Test
    public void resultMeshHasFiveVertices() {
        int expectedVertexCount = 5;
        assertEquals(expectedVertexCount, plane.getVertexCount());
        assertEquals(expectedVertexCount, plane.vertices.size());
        assertEquals(expectedVertexCount, plane.getVertices().size());
        assertEquals(
                expectedVertexCount,
                plane.getVertices(0, plane.getVertexCount()).size()
        );
    }

    @Test
    public void resultMeshHasNoLooseVertices() {
        assertTrue(MeshTestUtil.meshHasNoLooseVertices(plane));
    }

    @Test
    public void resultMeshHasNoDuplicatedFaces() {
        assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(plane));
    }

    @Test
    public void resultMeshContainsPlanerVertices() {
        Mesh3D original = new PlaneCreator().create();
        for (Vector3f v : original.getVertices())
            assertTrue(plane.vertices.contains(v));
    }

    @Test
    public void resultMeshContainsPlaneCenter() {
        Mesh3D original = new PlaneCreator().create();
        Face3D face = original.getFaceAt(0);
        Vector3f faceCenter = original.calculateFaceCenter(face);
        assertTrue(plane.vertices.contains(faceCenter));
    }

    @Test
    public void faceNormalsPointingTowardsNegativeY() {
        Vector3f expectedNormal = new Vector3f(0, -1, 0);
        plane.apply(new UpdateFaceNormalsModifier());
        for (int i = 0; i < plane.getFaceCount(); i++) {
            Face3D face = plane.getFaceAt(i);
            assertEquals(expectedNormal, face.normal);
        }
    }

    @Test
    public void eachFaceContainsFaceCenterVertexIndex() {
        Mesh3D original = new PlaneCreator().create();
        Face3D face = original.getFaceAt(0);
        Vector3f faceCenter = original.calculateFaceCenter(face);
        int index = plane.getVertices().indexOf(faceCenter);
        for (Face3D face0 : plane.getFaces())
            assertTrue(MeshTestUtil.containsVertexIndex(face0, index));
    }

}
