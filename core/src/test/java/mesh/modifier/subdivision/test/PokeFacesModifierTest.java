package mesh.modifier.subdivision.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CapsuleCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.geometry.MeshGeometryUtil;
import mesh.modifier.IMeshModifier;
import mesh.modifier.subdivision.PokeFacesModifier;
import util.MeshTestUtil;

public class PokeFacesModifierTest {

    private PokeFacesModifier modifier;

    @Before
    public void setUp() {
        modifier = new PokeFacesModifier();
    }

    @Test
    public void constructWithRandomOffsetGet() {
        float randomPokeOffset = (float) (Math.random() * 304);
        modifier = new PokeFacesModifier(randomPokeOffset);
        assertEquals(randomPokeOffset, modifier.getPokeOffset(), 0);
    }

    @Test
    public void modifierImplementsModifierInterface() {
        assertTrue(modifier instanceof IMeshModifier);
    }

    @Test
    public void pokeOffsetIsZeroPointOneByDefault() {
        assertEquals(0.1f, modifier.getPokeOffset(), 0);
    }

    @Test
    public void getSetRandomPokeOffset() {
        float expected = (float) (Math.random() * 1000);
        modifier.setPokeOffset(expected);
        assertEquals(expected, modifier.getPokeOffset(), 0);
    }

    @Test
    public void modifyReturnsReferenceToOriginalMesh() {
        Mesh3D mesh = new Mesh3D();
        Mesh3D result = modifier.modify(mesh);
        assertTrue(mesh == result);
    }

    @Test
    public void vertexCountIsOriginalVertexCountPlusOriginalFaceCount() {
        Mesh3D mesh = new CapsuleCreator().create();
        int expected = mesh.getVertexCount() + mesh.getFaceCount();
        modifier.modify(mesh);
        assertEquals(expected, mesh.getVertexCount());
    }

    @Test
    public void modifiedMeshConsistsOfTrianglesOnlyCubeCase() {
        Mesh3D mesh = new CubeCreator().create();
        modifier.modify(mesh);
        assertTrue(
                MeshTestUtil.isTriangleCountEquals(mesh, mesh.getFaceCount()));
    }

    @Test
    public void modifiedMeshConsistsOfTrianglesOnlyCapsuleCase() {
        Mesh3D mesh = new CapsuleCreator().create();
        modifier.modify(mesh);
        assertTrue(
                MeshTestUtil.isTriangleCountEquals(mesh, mesh.getFaceCount()));
    }

    @Test
    public void modifiedMeshDoesNotContainAnyLooseVertices() {
        Mesh3D mesh = new CapsuleCreator().create();
        modifier.modify(mesh);
        assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
    }

    @Test
    public void normalsPointOutwardsCubeCase() {
        Mesh3D mesh = new CubeCreator().create();
        modifier.modify(mesh);
        assertTrue(MeshTestUtil.normalsPointOutwards(mesh));
    }

    @Test
    public void containsAllVerticesAtOriginalPositionCubeCase() {
        Mesh3D originalCube = new CubeCreator().create();
        Mesh3D modifiedCube = new CubeCreator().create();
        modifier.modify(modifiedCube);
        List<Vector3f> originalVertices = originalCube.getVertices();
        for (Vector3f v : originalVertices) {
            assertTrue(modifiedCube.getVertices().contains(v));
        }
    }

    @Test
    public void containsAllVerticesAtOriginalPositionCapsuleCase() {
        Mesh3D originalCapsule = new CapsuleCreator().create();
        Mesh3D modifiedCapsule = new CapsuleCreator().create();
        modifier.modify(modifiedCapsule);
        List<Vector3f> originalVertices = originalCapsule.getVertices();
        for (Vector3f v : originalVertices) {
            assertTrue(modifiedCapsule.getVertices().contains(v));
        }
    }

    @Test
    public void containsFaceCentersTranslatedAlongNormalWithRandomPokeOffset() {
        float randomPokeOffset = (float) (Math.random() * 60000);
        Mesh3D cube = new CubeCreator().create();
        Mesh3D modifiedCube = new CubeCreator().create();
        modifier.setPokeOffset(randomPokeOffset);
        modifier.modify(modifiedCube);
        for (Face3D face : cube.getFaces()) {
            Vector3f normal = MeshGeometryUtil.calculateFaceNormal(cube, face);
            Vector3f center = MeshGeometryUtil.calculateFaceCenter(cube, face);
            Vector3f expected = center.add(normal.mult(randomPokeOffset));
            assertTrue(modifiedCube.getVertices().contains(expected));
        }
    }

    @Test
    public void modifiedMeshIsManifoldCubeCase() {
        Mesh3D cube = new CubeCreator().create();
        modifier.modify(cube);
        assertTrue(MeshTestUtil.isManifold(cube));
    }

    @Test
    public void modifiedMeshIsManifoldCapsuleCase() {
        Mesh3D capsule = new CapsuleCreator().create();
        modifier.modify(capsule);
        assertTrue(MeshTestUtil.isManifold(capsule));
    }

    @Test
    public void distanceFaceCentersToOriginCubeCase() {
        float radius = 10;
        float randomPokeOffset = (float) (Math.random() * 10223);
        float expectedDistance = randomPokeOffset + radius;
        Mesh3D mesh = new CubeCreator(radius).create();
        Mesh3D modifiedMesh = new CubeCreator(radius).create();
        modifier.setPokeOffset(randomPokeOffset);
        modifier.modify(modifiedMesh);
        modifiedMesh.remove(mesh.getVertices());
        for (Vector3f v : modifiedMesh.getVertices()) {
            float distance = v.distance(Vector3f.ZERO);
            assertEquals(expectedDistance, distance, 0);
        }
    }

}
