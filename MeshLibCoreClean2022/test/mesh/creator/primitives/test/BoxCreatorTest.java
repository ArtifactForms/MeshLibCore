package mesh.creator.primitives.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.selection.FaceSelection;
import mesh.util.Bounds3;
import util.MeshTest;

public class BoxCreatorTest {

    @Test
    public void createdMeshIsManifold() {
        Mesh3D mesh = new BoxCreator().create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void createdMeshIsNotNullByDefault() {
        Mesh3D mesh = new BoxCreator().create();
        assertNotNull(mesh);
    }

    @Test
    public void everyEdgeHasTheLengthOfOne() {
        float delta = 0;
        float expectedEdgeLength = 1.0f;
        Mesh3D mesh = new BoxCreator().create();
        MeshTest.assertEveryEdgeHasALengthOf(mesh, expectedEdgeLength, delta);
    }

    @Test
    public void fulfillsEulerCharacteristic() {
        Mesh3D mesh = new BoxCreator().create();
        assertTrue(MeshTest.fulfillsEulerCharacteristic(mesh));
    }

    @Test
    public void faceCountIsSix() {
        Mesh3D mesh = new BoxCreator().create();
        assertEquals(6, mesh.getFaceCount());
    }

    @Test
    public void vertexCountIsEight() {
        Mesh3D mesh = new BoxCreator().create();
        assertEquals(8, mesh.getVertexCount());
    }

    @Test
    public void sixSquareFaces() {
        Mesh3D mesh = new BoxCreator().create();
        FaceSelection selection = new FaceSelection(mesh);
        selection.selectQuads();
        assertEquals(6, selection.size());
    }

    @Test
    public void twelveEdges() {
        Mesh3D mesh = new BoxCreator().create();
        MeshTest.assertEdgeCountEquals(mesh, 12);
    }

    @Test
    public void absValueOfEveryXCoordinateIsOPointFiveByDefault() {
        Mesh3D mesh = new BoxCreator().create();
        for (Vector3f v : mesh.vertices)
            assertEquals(0.5f, Mathf.abs(v.getX()), 0);
    }

    @Test
    public void absValueOfEveryYCoordinateIsOPointFiveByDefault() {
        Mesh3D mesh = new BoxCreator().create();
        for (Vector3f v : mesh.vertices)
            assertEquals(0.5f, Mathf.abs(v.getY()), 0);
    }

    @Test
    public void absValueOfEveryZCoordinateIsOPointFiveByDefault() {
        Mesh3D mesh = new BoxCreator().create();
        for (Vector3f v : mesh.vertices)
            assertEquals(0.5f, Mathf.abs(v.getZ()), 0);
    }

    @Test
    public void widthIsOneByDefault() {
        Mesh3D mesh = new BoxCreator().create();
        Bounds3 bounds = mesh.calculateBounds();
        assertEquals(1, bounds.getWidth(), 0);
    }

    @Test
    public void heightIsOneByDefault() {
        Mesh3D mesh = new BoxCreator().create();
        Bounds3 bounds = mesh.calculateBounds();
        assertEquals(1, bounds.getHeight(), 0);
    }

    @Test
    public void depthIsOneByDefault() {
        Mesh3D mesh = new BoxCreator().create();
        Bounds3 bounds = mesh.calculateBounds();
        assertEquals(1, bounds.getDepth(), 0);
    }

    @Test
    public void getSetDepth() {
        float depth = Mathf.random(Float.MIN_VALUE, Float.MAX_VALUE);
        BoxCreator creator = new BoxCreator();
        creator.setDepth(depth);
        assertEquals(depth, creator.getDepth(), 0);
    }

    @Test
    public void normalsPointOutwards() {
        Mesh3D mesh = new BoxCreator().create();
        Vector3f center = new Vector3f();
        for (Face3D face : mesh.getFaces()) {
            Vector3f faceNormal = mesh.calculateFaceNormal(face);
            Vector3f faceCenter = mesh.calculateFaceCenter(face);
            Vector3f a = faceCenter.subtract(center);
            float dotProduct = faceNormal.dot(a);
            assertTrue(dotProduct >= 0);
        }
    }

    @Test
    public void defaultContainsCubeVertices() {
        Mesh3D cube = new CubeCreator(0.5f).create();
        Mesh3D box = new BoxCreator().create();
        for (Vector3f v : cube.vertices)
            assertTrue(box.vertices.contains(v));
    }

    @Test
    public void boundsWidth() {
        float expectedWidth = 1922.24f;
        BoxCreator creator = new BoxCreator();
        creator.setWidth(expectedWidth);
        Mesh3D box = creator.create();
        Bounds3 bounds = box.calculateBounds();
        assertEquals(expectedWidth, bounds.getWidth(), 0);
    }

    @Test
    public void boundsHeight() {
        float expectedHeight = 23.2421f;
        BoxCreator creator = new BoxCreator();
        creator.setHeight(expectedHeight);
        Mesh3D box = creator.create();
        Bounds3 bounds = box.calculateBounds();
        assertEquals(expectedHeight, bounds.getHeight(), 0);
    }

    @Test
    public void boundsDepth() {
        float expectedDepth = 2348.21112f;
        BoxCreator creator = new BoxCreator();
        creator.setDepth(expectedDepth);
        Mesh3D box = creator.create();
        Bounds3 bounds = box.calculateBounds();
        assertEquals(expectedDepth, bounds.getDepth(), 0);
    }

}
