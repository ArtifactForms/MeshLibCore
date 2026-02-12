package mesh.modifier.subdivision.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.archimedian.SnubCubeCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.subdivision.QuadsToTrianglesModifier;
import util.MeshTestUtil;

public class QuadsToTrianglesTest {

    private QuadsToTrianglesModifier modifier;

    @BeforeEach
    public void setUp() {
        modifier = new QuadsToTrianglesModifier();
    }

    @Test
    public void implementsModifierInterface() {
        assertTrue(modifier instanceof IMeshModifier);
    }

    @Test
    public void returnsMeshReference() {
        Mesh3D mesh = new Mesh3D();
        Mesh3D result = modifier.modify(mesh);
        assertTrue(mesh == result);
    }

    @Test
    public void subdividedPlaneHasTwoFaces() {
        Mesh3D mesh = new PlaneCreator().create();
        modifier.modify(mesh);
        assertEquals(2, mesh.getFaceCount());
    }

    @Test
    public void subdividedPlaneHasTwoTriangularFaces() {
        Mesh3D mesh = new PlaneCreator().create();
        modifier.modify(mesh);
        assertTrue(MeshTestUtil.isTriangleCountEquals(mesh, 2));
    }

    @Test
    public void subdividedCubeHasTwelveTriangularFaces() {
        Mesh3D mesh = new CubeCreator().create();
        modifier.modify(mesh);
        assertTrue(MeshTestUtil.isTriangleCountEquals(mesh, 12));
    }

    @Test
    public void subdividedCubeHasNoLooseVertices() {
        Mesh3D mesh = new CubeCreator().create();
        modifier.modify(mesh);
        assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
    }

    @Test
    public void subdivideIcosahedronVertices() {
        Mesh3D expected = new IcosahedronCreator().create();
        Mesh3D actual = new IcosahedronCreator().create();
        modifier.modify(actual);
        for (int i = 0; i < expected.getVertexCount(); i++) {
            Vector3f expectedVertex = expected.getVertexAt(i);
            Vector3f actualVertex = actual.getVertexAt(i);
            assertEquals(expectedVertex, actualVertex);
        }
    }

    @Test
    public void subdivideIcosahedronFaces() {
        Mesh3D expected = new IcosahedronCreator().create();
        Mesh3D actual = new IcosahedronCreator().create();
        modifier.modify(actual);
        for (int i = 0; i < expected.getFaceCount(); i++) {
            int[] expectedIndices = expected.getFaceAt(i).indices;
            int[] actualIndices = actual.getFaceAt(i).indices;
            assertArrayEquals(expectedIndices, actualIndices);
        }
    }

    @Test
    public void subdividedSnubCubeHasNoLooseVertices() {
        Mesh3D mesh = new SnubCubeCreator().create();
        modifier.modify(mesh);
        assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
    }

    @Test
    public void subdividedSnubCubeCompletelyConsistsOfTriangles() {
        Mesh3D mesh = new SnubCubeCreator().create();
        int expected = 44;
        modifier.modify(mesh);
        assertTrue(MeshTestUtil.isTriangleCountEquals(mesh, expected));
    }

    @Test
    public void subdividePlaneVertexIndicesOfFaceOne() {
        Mesh3D mesh = new PlaneCreator().create();
        modifier.modify(mesh);
        int[] expected = new int[] { 0, 1, 2 };
        int[] actual = mesh.getFaceAt(0).indices;
        assertArrayEquals(expected, actual);
    }

    @Test
    public void subdividePlaneVertexIndicesOfFaceTwo() {
        Mesh3D mesh = new PlaneCreator().create();
        modifier.modify(mesh);
        int[] expected = new int[] { 2, 3, 0 };
        int[] actual = mesh.getFaceAt(1).indices;
        assertArrayEquals(expected, actual);
    }

    @Test
    public void subdivideCubeFaceCheckFirstTriangleIndices() {
        Mesh3D cube = new CubeCreator().create();
        Mesh3D modifiedCube = new CubeCreator().create();
        modifier.modify(modifiedCube);
        for (int i = 0; i < cube.getFaceCount(); i++) {
            Face3D originalFace = cube.getFaceAt(i);
            Face3D modifiedFace = modifiedCube.getFaceAt(i * 2);
            int[] originalIndices = originalFace.indices;
            int index0 = originalIndices[0];
            int index1 = originalIndices[1];
            int index2 = originalIndices[2];
            int[] expectedIndices = new int[] { index0, index1, index2 };
            assertArrayEquals(expectedIndices, modifiedFace.indices);
        }
    }

    @Test
    public void normalsPointOutwards() {
        Mesh3D cube = new CubeCreator().create();
        modifier.modify(cube);
        assertTrue(MeshTestUtil.normalsPointOutwards(cube));
    }

}
