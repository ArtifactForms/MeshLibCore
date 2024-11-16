package mesh.test.catmullclark;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.platonic.DodecahedronCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.io.SimpleObjectReader;
import mesh.modifier.subdivision.CatmullClarkModifier;
import util.MeshTest;

public class CatmullClarkTest {

    private static final int ITERATIONS_TO_TEST = 8;

    private Mesh3D loadReference(int iteration) {
        String file = "Catmull_Clark_Reference_Level_" + iteration + ".obj";
        file = CatmullClarkTest.class.getResource(file).getPath();
        Mesh3D expectedMesh = new SimpleObjectReader().read(new File(file));
        return expectedMesh;
    }

    private void facesAreEqual(Mesh3D expected, Mesh3D actual) {
        for (int j = 0; j < expected.getFaceCount(); j++) {
            Face3D expectedFace = expected.getFaceAt(j);
            Face3D actualFace = actual.getFaceAt(j);
            for (int index = 0; index < expectedFace.indices.length; index++) {
                assertEquals(expectedFace.indices[index],
                        actualFace.indices[index]);
            }
        }
    }

    private void verticesAreEqual(Mesh3D expectedMesh, Mesh3D actualMesh) {
        for (int j = 0; j < expectedMesh.getVertexCount(); j++) {
            Vector3f expected = expectedMesh.getVertexAt(j);
            Vector3f actual = actualMesh.getVertexAt(j);
            assertEquals(expected.getX(), actual.getX(), 0.000001f);
            assertEquals(expected.getY(), actual.getY(), 0.000001f);
            assertEquals(expected.getZ(), actual.getZ(), 0.000001f);
        }
    }

    @Test
    public void subdivideOtherShapeInAdvanceTestFaces() {
        Mesh3D advance = new DodecahedronCreator().create();
        Mesh3D expected = loadReference(4);
        Mesh3D actual = new CubeCreator().create();
        CatmullClarkModifier modifier = new CatmullClarkModifier(4);
        modifier.modify(advance);
        modifier.modify(actual);
        facesAreEqual(expected, actual);
    }

    @Test
    public void subdivideOtherShapeInAdvanceTestVertices() {
        Mesh3D advance = new DodecahedronCreator().create();
        Mesh3D expected = loadReference(4);
        Mesh3D actual = new CubeCreator().create();
        CatmullClarkModifier modifier = new CatmullClarkModifier(4);
        modifier.modify(advance);
        modifier.modify(actual);
        verticesAreEqual(expected, actual);
    }

    @Test
    public void setIterationCountToSpecifiedValueTestFaces() {
        int iterations = 4;
        Mesh3D expected = loadReference(iterations);
        Mesh3D actual = new CubeCreator().create();
        CatmullClarkModifier modifier = new CatmullClarkModifier();
        modifier.setSubdivisions(iterations);
        modifier.modify(actual);
        facesAreEqual(expected, actual);
    }

    @Test
    public void setIterationCountToSpecifiedValueTestVertices() {
        int iterations = 5;
        Mesh3D expected = loadReference(iterations);
        Mesh3D actual = new CubeCreator().create();
        CatmullClarkModifier modifier = new CatmullClarkModifier();
        modifier.setSubdivisions(iterations);
        modifier.modify(actual);
        verticesAreEqual(expected, actual);
    }

    @Test
    public void defaultSettingsOneIterationTestFaces() {
        Mesh3D expected = loadReference(1);
        Mesh3D actual = new CubeCreator().create();
        new CatmullClarkModifier().modify(actual);
        facesAreEqual(expected, actual);
    }

    @Test
    public void defaultSettingsOneIterationTestVertices() {
        Mesh3D expected = loadReference(1);
        Mesh3D actual = new CubeCreator().create();
        new CatmullClarkModifier().modify(actual);
        verticesAreEqual(expected, actual);
    }

    @Test
    public void createdVerticesAreEqualToThoseOfReferenceFile() {
        for (int i = 0; i < ITERATIONS_TO_TEST; i++) {
            Mesh3D expected = loadReference(i);
            Mesh3D actual = new CubeCreator().create();
            new CatmullClarkModifier(i).modify(actual);
            verticesAreEqual(expected, actual);
        }
    }

    @Test
    public void createdFacesAreEqualToThoseOfReferenceFile() {
        for (int i = 0; i < ITERATIONS_TO_TEST; i++) {
            Mesh3D expected = loadReference(i);
            Mesh3D actual = new CubeCreator().create();
            new CatmullClarkModifier(i).modify(actual);
            facesAreEqual(expected, actual);
        }
    }

    @Test
    public void getSetSubdivisions() {
        int iterations = (int) (Math.random() * Integer.MAX_VALUE);
        CatmullClarkModifier modifier = new CatmullClarkModifier();
        modifier.setSubdivisions(iterations);
        assertEquals(iterations, modifier.getSubdivisions());
    }

    @Test
    public void cubeStaysManifold() {
        Mesh3D cube = new CubeCreator().create();
        for (int i = 0; i < ITERATIONS_TO_TEST; i++) {
            new CatmullClarkModifier().modify(cube);
            assertTrue(MeshTest.isManifold(cube));
        }
    }

    @Test
    public void resultConsistsOfQuadsOnly() {
        Mesh3D cube = new CubeCreator().create();
        int faceCount = cube.getFaceCount();
        assertTrue(MeshTest.isQuadCountEquals(cube, faceCount));
    }

}
