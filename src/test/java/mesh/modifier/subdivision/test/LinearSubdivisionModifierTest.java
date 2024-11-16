package mesh.modifier.subdivision.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.archimedian.CuboctahedronCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.subdivision.LinearSubdivisionModifier;
import mesh.util.Bounds3;
import util.MeshTest;

public class LinearSubdivisionModifierTest {

    private LinearSubdivisionModifier modifier;

    private Mesh3D cubeMesh;

    @Before
    public void setUp() {
        modifier = new LinearSubdivisionModifier();
        cubeMesh = new CubeCreator().create();
    }

    @Test
    public void modifierImplementsModifierInterface() {
        assertTrue(modifier instanceof IMeshModifier);
    }

    @Test
    public void returnsReferenceToOriginalMeshInstance() {
        Mesh3D result = modifier.modify(cubeMesh);
        assertTrue(cubeMesh == result);
    }

    @Test
    public void subdividedCubeHasTwentyFourFaces() {
        modifier.modify(cubeMesh);
        assertEquals(24, cubeMesh.getFaceCount());
    }

    @Test
    public void twiceSubdividedCubeHasNintySixFaces() {
        modifier.modify(cubeMesh);
        modifier.modify(cubeMesh);
        assertEquals(96, cubeMesh.getFaceCount());
    }

    @Test
    public void subdividedCubeHasTwentySixVertices() {
        modifier.modify(cubeMesh);
        assertEquals(26, cubeMesh.getVertexCount());
    }

    @Test
    public void twiceSubdividedCubeHasNintyEightVertices() {
        modifier.modify(cubeMesh);
        modifier.modify(cubeMesh);
        assertEquals(98, cubeMesh.getVertexCount());
    }

    @Test
    public void subdividedCubeIsManifold() {
        modifier.modify(cubeMesh);
        assertTrue(MeshTest.isManifold(cubeMesh));
    }

    @Test
    public void subdividedCubeHasNoLooseVertices() {
        modifier.modify(cubeMesh);
        assertTrue(MeshTest.meshHasNoLooseVertices(cubeMesh));
    }

    @Test
    public void twiceSubdividedCubeHasNoLooseVertices() {
        modifier.modify(cubeMesh);
        modifier.modify(cubeMesh);
        assertTrue(MeshTest.meshHasNoLooseVertices(cubeMesh));
    }

    @Test
    public void subdividedCubeHasFaceNormalPointOutward() {
        modifier.modify(cubeMesh);
        assertTrue(MeshTest.normalsPointOutwards(cubeMesh));
    }

    @Test
    public void subdividedCubeContainsVerticesAtOriginalCubePositions() {
        Mesh3D originalCube = new CubeCreator().create();
        Mesh3D subdividedCube = new CubeCreator().create();
        modifier.modify(subdividedCube);
        for (Vector3f v : originalCube.getVertices()) {
            assertTrue(subdividedCube.getVertices().contains(v));
        }
    }

    @Test
    public void subdividedCubeContainsFaceCenterVertices() {
        Mesh3D originalCube = new CubeCreator().create();
        Mesh3D subdividedCube = new CubeCreator().create();
        modifier.modify(subdividedCube);
        for (Face3D face : originalCube.getFaces()) {
            Vector3f center = originalCube.calculateFaceCenter(face);
            assertTrue(subdividedCube.getVertices().contains(center));
        }
    }

    @Test
    public void subdividedCuveHasNoDuplicatedFaces() {
        modifier.modify(cubeMesh);
        assertTrue(MeshTest.meshHasNoDuplicatedFaces(cubeMesh));
    }

    @Test
    public void subdividedCubeConsistsOfQuadsOnly() {
        modifier.modify(cubeMesh);
        int faceCount = cubeMesh.getFaceCount();
        assertTrue(MeshTest.isQuadCountEquals(cubeMesh, faceCount));
    }

    @Test
    public void subdividedIcosahedronConsistsOfTrianglesOnly() {
        Mesh3D mesh = new IcosahedronCreator().create();
        modifier.modify(mesh);
        int faceCount = mesh.getFaceCount();
        assertTrue(MeshTest.isTriangleCountEquals(mesh, faceCount));
    }

    @Test
    public void subdividedIcosahedronHasEightyFaces() {
        Mesh3D mesh = new IcosahedronCreator().create();
        modifier.modify(mesh);
        assertEquals(80, mesh.getFaceCount());
    }

    @Test
    public void subdividedIcosahedronHasFourtyTwoVertices() {
        Mesh3D mesh = new IcosahedronCreator().create();
        modifier.modify(mesh);
        assertEquals(42, mesh.getVertexCount());
    }

    @Test
    public void subdividedIcosahedronIsManifold() {
        Mesh3D mesh = new IcosahedronCreator().create();
        modifier.modify(mesh);
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void subdividedIcosahedronHasNoLooseVertices() {
        Mesh3D mesh = new IcosahedronCreator().create();
        modifier.modify(mesh);
        assertTrue(MeshTest.meshHasNoLooseVertices(mesh));
    }

    @Test
    public void subdividedCubeIsWithinBounds() {
        modifier.modify(cubeMesh);
        Bounds3 bounds = cubeMesh.calculateBounds();
        assertEquals(2, bounds.getWidth(), 0);
        assertEquals(2, bounds.getHeight(), 0);
        assertEquals(2, bounds.getDepth(), 0);
    }

    @Test
    public void everyFaceContainsCenterVertexIndex() {
        Mesh3D plane = new PlaneCreator().create();
        Face3D face = plane.getFaceAt(0);
        Vector3f center = plane.calculateFaceCenter(face);
        modifier.modify(plane);
        for (Face3D f : plane.getFaces()) {
            boolean containsCenter = false;
            for (int i = 0; i < f.indices.length; i++) {
                Vector3f v = plane.getVertexAt(f.indices[i]);
                containsCenter |= v.equals(center);
            }
            assertTrue(containsCenter);
        }
    }

    @Test
    public void removingCenterAndEdgePointsLeavesOriginalVerticesQuadCase() {
        Mesh3D originalCube = new CubeCreator().create();
        modifier.modify(cubeMesh);
        List<Vector3f> vertices = cubeMesh.getVertices();
        vertices.removeAll(calculateEdgePoints(originalCube));
        for (Face3D face : originalCube.getFaces()) {
            Vector3f center = originalCube.calculateFaceCenter(face);
            vertices.remove(center);
        }
        assertEquals(8, vertices.size());
        for (Vector3f v : originalCube.getVertices()) {
            assertTrue(vertices.contains(v));
        }
    }

    @Test
    public void removingEdgePointsLeavesOriginalVerticesTriangleCase() {
        Mesh3D original = new IcosahedronCreator().create();
        Mesh3D mesh = new IcosahedronCreator().create();
        int expectedVertexCount = original.getVertexCount();
        modifier.modify(mesh);
        List<Vector3f> vertices = mesh.getVertices();
        vertices.removeAll(calculateEdgePoints(original));
        assertEquals(expectedVertexCount, vertices.size());
        for (Vector3f v : original.getVertices()) {
            assertTrue(vertices.contains(v));
        }
    }

    private List<Vector3f> calculateEdgePoints(Mesh3D mesh) {
        List<Vector3f> edgePoints = new ArrayList<>();
        for (Face3D face : mesh.getFaces()) {
            int length = face.indices.length;
            for (int i = 0; i < length; i++) {
                Vector3f from = mesh.getVertexAt(face.indices[i]);
                Vector3f to = mesh.getVertexAt(face.indices[(i + 1) % length]);
                Vector3f edgePoint = from.add(to).mult(0.5f);
                edgePoints.add(edgePoint);
            }
        }
        return edgePoints;
    }

    @Test
    public void subdividedCuboctahedronHasThrirtyTwoTriangularFaces() {
        CuboctahedronCreator creator = new CuboctahedronCreator();
        Mesh3D mesh = creator.create();
        modifier.modify(mesh);
        assertTrue(MeshTest.isTriangleCountEquals(mesh, 32));
    }

    @Test
    public void subdividedCuboctahedronHasTwentyFourQuads() {
        CuboctahedronCreator creator = new CuboctahedronCreator();
        Mesh3D mesh = creator.create();
        modifier.modify(mesh);
        assertTrue(MeshTest.isQuadCountEquals(mesh, 24));
    }

    @Test
    public void subdividedCuboctahedronHasNoLooseVertices() {
        CuboctahedronCreator creator = new CuboctahedronCreator();
        Mesh3D mesh = creator.create();
        modifier.modify(mesh);
        assertTrue(MeshTest.meshHasNoLooseVertices(mesh));
    }

    @Test
    public void subdividedCuboctahedronHasNoDuplicatedFaces() {
        CuboctahedronCreator creator = new CuboctahedronCreator();
        Mesh3D mesh = creator.create();
        modifier.modify(mesh);
        assertTrue(MeshTest.meshHasNoDuplicatedFaces(mesh));
    }

    @Test
    public void subdividedCuboctahedronIsManifold() {
        CuboctahedronCreator creator = new CuboctahedronCreator();
        Mesh3D mesh = creator.create();
        modifier.modify(mesh);
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void subdividedCuboctahedronHasFourtyTwoVertices() {
        CuboctahedronCreator creator = new CuboctahedronCreator();
        Mesh3D mesh = creator.create();
        modifier.modify(mesh);
        assertEquals(42, mesh.getVertexCount());
    }

    @Test
    public void subdividedCuboctahedronHasNoDuplicatedVertices() {
        CuboctahedronCreator creator = new CuboctahedronCreator();
        Mesh3D mesh = creator.create();
        modifier.modify(mesh);
        HashSet<Vector3f> vertices = new HashSet<>();
        vertices.addAll(mesh.getVertices());
        assertEquals(42, mesh.getVertexCount());
    }

    @Test
    public void subdividedCuboctahedronHasNoDuplicatedVerticesThreshold() {
        CuboctahedronCreator creator = new CuboctahedronCreator();
        Mesh3D mesh = creator.create();
        modifier.modify(mesh);
        for (Vector3f v : mesh.vertices)
            v.roundLocalDecimalPlaces(2);
        HashSet<Vector3f> vertices = new HashSet<>();
        vertices.addAll(mesh.getVertices());
        assertEquals(42, mesh.getVertexCount());
    }

}
