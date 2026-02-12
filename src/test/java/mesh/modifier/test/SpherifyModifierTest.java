package mesh.modifier.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.creator.primitives.SegmentedCubeCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.deform.SpherifyModifier;

public class SpherifyModifierTest {

  private SpherifyModifier modifier;

  @BeforeEach
  public void setUp() {
    modifier = new SpherifyModifier();
  }

  @Test
  public void testModifierImplementsMeshModifierInterface() {
    assertTrue(modifier instanceof IMeshModifier);
  }

  @Test
  public void testReturnsReferenceToModifedMesh() {
    Mesh3D epected = new CubeCreator().create();
    Mesh3D actual = modifier.modify(epected);
    assertSame(epected, actual);
  }

  @Test
  public void testDefaultConstructor() {
    SpherifyModifier modifier = new SpherifyModifier();
    assertEquals(1, modifier.getFactor());
    assertEquals(1, modifier.getRadius());
    assertEquals(Vector3f.ZERO, modifier.getCenter());
  }

  @ParameterizedTest
  @ValueSource(floats = {2.45f, 0.2f, 100.345f, Float.MIN_VALUE, Float.MAX_VALUE})
  public void testConstructorWithRadiusParameter() {
    float expectedRadius = 2.55f;
    SpherifyModifier modifier = new SpherifyModifier(expectedRadius);
    assertEquals(1, modifier.getFactor());
    assertEquals(expectedRadius, modifier.getRadius());
    assertEquals(Vector3f.ZERO, modifier.getCenter());
  }

  @ParameterizedTest
  @ValueSource(floats = {0, -0.1f, -100.3f, -Mathf.FLT_EPSILON})
  public void testConstructorWithRadiusLessOrEqualsToZero(float radius) {
    assertThrows(IllegalArgumentException.class, () -> new SpherifyModifier(radius));
  }

  @Test
  public void testDefaultCenter() {
    assertEquals(Vector3f.ZERO, modifier.getCenter());
  }

  @Test
  public void testGetSetCenterViaParameters() {
    float expectedX = 10.345f;
    float expectedY = 345.553f;
    float expectedZ = -1345.345f;
    modifier.setCenter(expectedX, expectedY, expectedZ);
    assertEquals(expectedX, modifier.getCenter().x);
    assertEquals(expectedY, modifier.getCenter().y);
    assertEquals(expectedZ, modifier.getCenter().z);
  }

  @Test
  public void testGetSetCenterViaVector3f() {
    Vector3f center = new Vector3f(0.134f, -23.443f, 100.0f);
    modifier.setCenter(center);
    assertEquals(center, modifier.getCenter());
  }

  @Test
  public void testGetCenterReturnsImutable() {
    Vector3f center = new Vector3f();
    modifier.setCenter(center);
    assertNotSame(center, modifier.getCenter());
  }

  @Test
  public void testDefaultFactor() {
    assertEquals(1.0f, modifier.getFactor());
  }

  @Test
  public void testDefaultRadius() {
    assertEquals(1, modifier.getRadius());
  }

  @Test
  public void testModifyReturnsNonNullMesh() {
    assertNotNull(modifier.modify(new CubeCreator().create()));
  }

  @Test
  public void testModifyReturnsReferenceToModifiedMesh() {
    Mesh3D expected = new CubeCreator().create();
    Mesh3D actual = modifier.modify(expected);
    assertSame(expected, actual);
  }

  @ParameterizedTest
  @ValueSource(floats = {-Float.MIN_VALUE, 0f, -1f, -Mathf.FLT_EPSILON})
  public void testSetRadiusToLessOrEqualToZeroThrowsException(float radius) {
    assertThrows(IllegalArgumentException.class, () -> modifier.setRadius(radius));
  }

  @Test
  public void testSetNullCenterThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> modifier.setCenter(null));
  }

  @Test
  public void testModifyNullMeshThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> modifier.modify(null));
  }

  @Test
  public void testSetFactorAboveBoundsThrowsException() {
    float factor = 1.0f + Mathf.FLT_EPSILON;
    assertThrows(IllegalArgumentException.class, () -> modifier.setFactor(factor));
  }

  @Test
  public void testSetFactorBelowBoundsThrowsException() {
    float factor = -Mathf.FLT_EPSILON;
    assertThrows(IllegalArgumentException.class, () -> modifier.setFactor(factor));
  }

  @Test
  public void testGetSetFactorWithinBouns() {
    float expectedFactor = 0.126f;
    modifier.setFactor(expectedFactor);
    assertEquals(expectedFactor, modifier.getFactor());
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 4, 10, 20})
  public void testMultipleIteractionsHaveSameEffectAsOne(int n) {
    Mesh3D expected = new CubeCreator().create();
    Mesh3D actual = new CubeCreator().create();
    modifier.modify(expected);
    for (int i = 0; i < n; i++) {
      modifier.modify(actual);
    }
    for (int i = 0; i < expected.getVertexCount(); i++) {
      Vector3f expectedVertex = expected.getVertexAt(i);
      Vector3f actualVertex = actual.getVertexAt(i);
      assertEquals(expectedVertex.x, actualVertex.x, 0.001f);
      assertEquals(expectedVertex.y, actualVertex.y, 0.001f);
      assertEquals(expectedVertex.z, actualVertex.z, 0.001f);
    }
  }

  @ParameterizedTest
  @ValueSource(floats = {2.134f, 4.234f, Float.MIN_VALUE})
  public void testMultipleRadiiWithFactorOne(float expectedRadius) {
    int segments = 30;
    float size = 1.0f;
    SegmentedCubeCreator creator = new SegmentedCubeCreator(segments, size);
    Mesh3D mesh = creator.create();
    modifier.setRadius(expectedRadius);
    modifier.modify(mesh);
    for (Vector3f vertex : mesh.vertices) {
      float distance = vertex.distance(Vector3f.ZERO);
      assertEquals(expectedRadius, distance, 0.001f);
    }
  }

  @Test
  public void testZeroFactorLeavesTheMeshUnchanged() {
    IcoSphereCreator creator = new IcoSphereCreator(1, 3);
    Mesh3D expected = creator.create();
    Mesh3D actual = creator.create();
    modifier.setFactor(0);
    modifier.setRadius(3);
    modifier.modify(actual);
    for (int i = 0; i < expected.getVertexCount(); i++) {
      Vector3f expectedVertex = expected.getVertexAt(i);
      Vector3f actualVertex = actual.getVertexAt(i);
      assertEquals(expectedVertex.x, actualVertex.x, 0.001f);
      assertEquals(expectedVertex.y, actualVertex.y, 0.001f);
      assertEquals(expectedVertex.z, actualVertex.z, 0.001f);
    }
  }
}
