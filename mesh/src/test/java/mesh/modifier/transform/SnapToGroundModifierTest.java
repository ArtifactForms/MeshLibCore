package mesh.modifier.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.creator.primitives.QuadSphereCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.transform.SnapToGroundModifier;
import mesh.modifier.transform.TranslateModifier;

public class SnapToGroundModifierTest {

  private SnapToGroundModifier modifier;

  @BeforeEach
  public void setUp() {
    modifier = new SnapToGroundModifier();
  }

  @Test
  public void implementsModifierInterface() {
    assertTrue(modifier instanceof IMeshModifier);
  }

  @Test
  public void modifiedMeshIsNotNull() {
    Mesh3D mesh = new Mesh3D();
    assertNotNull(modifier.modify(mesh));
  }

  @Test
  public void returnsReferenceToModifiedMesh() {
    Mesh3D expected = new Mesh3D();
    Mesh3D actual = modifier.modify(expected);
    assertSame(expected, actual);
  }

  @Test
  public void defaultGroundLevel() {
    assertEquals(0, modifier.getGroundLevel());
  }

  @ParameterizedTest
  @ValueSource(floats = {0, -1, 2.024f, 3.235f, Float.MIN_VALUE, Float.MAX_VALUE})
  public void testGetSetGroundLevel(float groundLevel) {
    modifier.setGroundLevel(groundLevel);
    assertEquals(groundLevel, modifier.getGroundLevel());
  }

  @ParameterizedTest
  @ValueSource(floats = {0, -1, 66.024f, -240.235f, Float.MIN_VALUE, Float.MAX_VALUE})
  public void testConstructor(float groundLevel) {
    modifier = new SnapToGroundModifier(groundLevel);
    assertEquals(groundLevel, modifier.getGroundLevel());
  }

  @Test
  public void testConsistentVertexCount() {
    QuadSphereCreator creator = new QuadSphereCreator();
    Mesh3D mesh = creator.create();
    int expectedVertexCount = mesh.getVertexCount();
    modifier.modify(mesh);
    assertEquals(expectedVertexCount, mesh.getVertexCount());
  }

  @Test
  public void testConsistentFaceCount() {
    QuadSphereCreator creator = new QuadSphereCreator();
    Mesh3D mesh = creator.create();
    int expectedFaceCount = mesh.getFaceCount();
    modifier.modify(mesh);
    assertEquals(expectedFaceCount, mesh.getFaceCount());
  }

  @Test
  public void doesOnlyModifiyY() {
    IcoSphereCreator creator = new IcoSphereCreator(1, 3);
    Mesh3D expected = creator.create();
    Mesh3D actual = creator.create();
    modifier.modify(actual);
    for (int i = 0; i < actual.getVertexCount(); i++) {
      Vector3f expectedVertex = expected.getVertexAt(i);
      Vector3f actualVertex = actual.getVertexAt(i);
      assertEquals(expectedVertex.x, actualVertex.x);
      assertEquals(expectedVertex.z, actualVertex.z);
    }
  }

  @Test
  public void testPlaneNegativeY() {
    Mesh3D plane = new PlaneCreator().create();
    new TranslateModifier(0, -10.23f, 0).modify(plane);
    modifier.modify(plane);
    for (int i = 0; i < plane.getVertexCount(); i++) {
      Vector3f vertex = plane.getVertexAt(i);
      assertEquals(0, vertex.y);
    }
  }

  @Test
  public void testPlanePositiveY() {
    Mesh3D plane = new PlaneCreator().create();
    new TranslateModifier(0, 10.23f, 0).modify(plane);
    modifier.modify(plane);
    for (int i = 0; i < plane.getVertexCount(); i++) {
      Vector3f vertex = plane.getVertexAt(i);
      assertEquals(0, vertex.y);
    }
  }

  @ParameterizedTest
  @ValueSource(floats = {-5.02f, -7.24f, -100, -2000.3f})
  public void testCubeAboveGround(float translateY) {
    Mesh3D expeted = new CubeCreator().create();
    Mesh3D actual = new CubeCreator().create();
    new TranslateModifier(0, -1, 0).modify(expeted);
    new TranslateModifier(0, translateY, 0).modify(actual);
    modifier.modify(actual);
    for (int i = 0; i < actual.getVertexCount(); i++) {
      Vector3f expectedVertex = expeted.getVertexAt(i);
      Vector3f actualVertex = actual.getVertexAt(i);
      assertEquals(expectedVertex, actualVertex);
    }
  }

  @ParameterizedTest
  @ValueSource(floats = {8.02f, 20.24f, 100.234f, 2030.321f})
  public void testCubeBelowGround(float translateY) {
    Mesh3D expeted = new CubeCreator().create();
    Mesh3D actual = new CubeCreator().create();
    new TranslateModifier(0, -1, 0).modify(expeted);
    new TranslateModifier(0, translateY, 0).modify(actual);
    modifier.modify(actual);
    for (int i = 0; i < actual.getVertexCount(); i++) {
      Vector3f expectedVertex = expeted.getVertexAt(i);
      Vector3f actualVertex = actual.getVertexAt(i);
      assertEquals(expectedVertex, actualVertex);
    }
  }

  @Test
  public void testCubeCenteredAtOrigin() {
    Mesh3D expeted = new CubeCreator().create();
    Mesh3D actual = new CubeCreator().create();
    new TranslateModifier(0, -1, 0).modify(expeted);
    modifier.modify(actual);
    for (int i = 0; i < actual.getVertexCount(); i++) {
      Vector3f expectedVertex = expeted.getVertexAt(i);
      Vector3f actualVertex = actual.getVertexAt(i);
      assertEquals(expectedVertex, actualVertex);
    }
  }

  @ParameterizedTest
  @ValueSource(floats = {-5.02f, -7.24f, -100, -2000.3f, 0, 10, 20, 100})
  public void testDifferentGroundLevelsPlane(float groundLevel) {
    Mesh3D plane = new PlaneCreator().create();
    modifier.setGroundLevel(groundLevel);
    modifier.modify(plane);
    for (Vector3f vertex : plane.getVertices()) {
      assertEquals(groundLevel, vertex.y, 0.001);
    }
  }

  @ParameterizedTest
  @ValueSource(floats = {-3.122f, -8.2454f, -100, -2024.3f, 0, 10.34f, 20.33f, 100.32f})
  public void testDifferentGroundLevelsCube(float groundLevel) {
    Mesh3D cube = new CubeCreator().create();
    modifier.setGroundLevel(groundLevel);
    modifier.modify(cube);

    for (int i = 0; i < 4; i++) {
      Vector3f vertex = cube.getVertexAt(i);
      assertEquals((groundLevel - 2), vertex.y, 0.001);
    }

    for (int i = 4; i < 8; i++) {
      Vector3f vertex = cube.getVertexAt(i);
      assertEquals(groundLevel, vertex.y, 0.001);
    }
  }
}
