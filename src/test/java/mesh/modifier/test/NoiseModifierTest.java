package mesh.modifier.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.deform.NoiseModifier;

public class NoiseModifierTest {

  private NoiseModifier modifier;

  @BeforeEach
  public void setUp() {
    modifier = new NoiseModifier();
  }

  @Test
  public void testImplementsMeshModifierInterface() {
    assertTrue(modifier instanceof IMeshModifier);
  }

  @Test
  public void testReturnsNonNullReference() {
    Mesh3D mesh = new CubeCreator().create();
    assertNotNull(modifier.modify(mesh));
  }

  @Test
  public void testReturnsReferenceToTheModifiedMesh() {
    Mesh3D expected = new CubeCreator().create();
    Mesh3D actual = modifier.modify(expected);
    assertSame(expected, actual);
  }

  @Test
  public void testDefaultMinimum() {
    assertEquals(0, modifier.getMinimum());
  }

  @Test
  public void testDefaultMaximum() {
    assertEquals(1.0f, modifier.getMaximum());
  }

  @ParameterizedTest
  @ValueSource(
      floats = {
        0.1f,
        0.322f,
        0.123f,
        0.022f,
        0.245f,
        0.751f,
        0.965f,
        Float.MIN_VALUE,
        Float.MAX_VALUE,
      })
  public void testGetSetMinumum(float minimum) {
    modifier.setMinimum(minimum);
    assertEquals(minimum, modifier.getMinimum());
  }

  @ParameterizedTest
  @ValueSource(
      floats = {
        0.123f,
        0.422f,
        0.1553f,
        0.2f,
        1.245f,
        3.751f,
        100.965f,
        Float.MIN_VALUE,
        Float.MAX_VALUE,
      })
  public void testGetSetMaximum(float maximum) {
    modifier.setMaximum(maximum);
    assertEquals(maximum, modifier.getMaximum());
  }

  @Test
  public void testDefaultSeed() {
    assertEquals(0, modifier.getSeed());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1000, -234, 0, 10000, 22344, Long.MIN_VALUE, Long.MAX_VALUE})
  public void testGetSetSeed(long seed) {
    modifier.setSeed(seed);
    assertEquals(seed, modifier.getSeed());
  }

  @Test
  public void testModifyEmptyMesh() {
    Mesh3D mesh = new Mesh3D();
    modifier.modify(mesh);
    assertEquals(0, mesh.getFaceCount());
    assertEquals(0, mesh.getVertexCount());
  }

  @Test
  public void testVertexCountStaysConsistent() {
    Mesh3D mesh = new IcoSphereCreator().create();
    int expected = mesh.getVertexCount();
    assertEquals(expected, modifier.modify(mesh).getVertexCount());
  }

  @Test
  public void testFaceCountStaysConsistent() {
    Mesh3D mesh = new IcoSphereCreator().create();
    int expected = mesh.getFaceCount();
    assertEquals(expected, modifier.modify(mesh).getFaceCount());
  }

  @Test
  public void testDoesNotModifyFaceIndices() {
    Mesh3D expected = new IcoSphereCreator().create();
    Mesh3D actual = new IcoSphereCreator().create();
    modifier.modify(actual);
    for (int i = 0; i < actual.getFaceCount(); i++) {
      int[] expectedIndices = expected.getFaceAt(i).indices;
      int[] actualIndices = actual.getFaceAt(i).indices;
      assertArrayEquals(expectedIndices, actualIndices);
    }
  }

  @Test
  public void testCubeVerticesDefaultSeed() {
    Vector3f[] expectedVertices =
        new Vector3f[] {
          new Vector3f(1.4220245f, -1.4220245f, -1.4220245f),
          new Vector3f(1.4800327f, -1.4800327f, 1.4800327f),
          new Vector3f(-1.1388737f, -1.1388737f, 1.1388737f),
          new Vector3f(-1.3500736f, -1.3500736f, -1.3500736f),
          new Vector3f(1.3680131f, 1.3680131f, -1.3680131f),
          new Vector3f(1.1784304f, 1.1784304f, 1.1784304f),
          new Vector3f(-1.3177949f, 1.3177949f, 1.3177949f),
          new Vector3f(-1.0675538f, 1.0675538f, -1.0675538f),
        };

    Mesh3D cube = new CubeCreator().create();
    modifier.modify(cube);
    for (int i = 0; i < cube.getFaceCount(); i++) {
      Vector3f expectedVertex = expectedVertices[i];
      Vector3f actualVertex = cube.getVertexAt(i);
      assertEquals(expectedVertex, actualVertex);
    }
  }

  @Test
  public void testCubeVerticesPositiveSeed() {
    Vector3f[] expectedVertices =
        new Vector3f[] {
          new Vector3f(1.4390444f, -1.4390444f, -1.4390444f),
          new Vector3f(1.4232731f, -1.4232731f, 1.4232731f),
          new Vector3f(-1.4091227f, -1.4091227f, 1.4091227f),
          new Vector3f(-1.1638807f, -1.1638807f, -1.1638807f),
          new Vector3f(1.2712379f, 1.2712379f, -1.2712379f),
          new Vector3f(1.111674f, 1.111674f, 1.111674f),
          new Vector3f(-1.210412f, 1.210412f, 1.210412f),
          new Vector3f(-1.2455546f, 1.2455546f, -1.2455546f),
        };

    Mesh3D cube = new CubeCreator().create();
    modifier.setSeed(30445);
    modifier.modify(cube);

    for (int i = 0; i < cube.getFaceCount(); i++) {
      Vector3f expectedVertex = expectedVertices[i];
      Vector3f actualVertex = cube.getVertexAt(i);
      assertEquals(expectedVertex, actualVertex);
    }
  }

  @Test
  public void testCubeVerticesNegativeSeed() {
    Vector3f[] expectedVertices =
        new Vector3f[] {
          new Vector3f(1.0861642f, -1.0861642f, -1.0861642f),
          new Vector3f(1.1106155f, -1.1106155f, 1.1106155f),
          new Vector3f(-1.0040842f, -1.0040842f, 1.0040842f),
          new Vector3f(-1.4769852f, -1.4769852f, -1.4769852f),
          new Vector3f(1.5054972f, 1.5054972f, -1.5054972f),
          new Vector3f(1.5229388f, 1.5229388f, 1.5229388f),
          new Vector3f(-1.2344728f, 1.2344728f, 1.2344728f),
          new Vector3f(-1.1652328f, 1.1652328f, -1.1652328f),
        };

    Mesh3D cube = new CubeCreator().create();
    modifier.setSeed(-738739);
    modifier.modify(cube);

    for (int i = 0; i < cube.getFaceCount(); i++) {
      Vector3f expectedVertex = expectedVertices[i];
      Vector3f actualVertex = cube.getVertexAt(i);
      assertEquals(expectedVertex, actualVertex);
    }
  }

  @Test
  public void testDefaulDisplacement() {
    Mesh3D originalMesh = new IcoSphereCreator(1, 3).create();
    Mesh3D modifiedMesh = new IcoSphereCreator(1, 3).create();

    modifier.modify(modifiedMesh);

    float totalDistance = 0;

    for (int i = 0; i < originalMesh.getVertexCount(); i++) {
      Vector3f originalVertex = originalMesh.getVertexAt(i);
      Vector3f modifiedVertex = modifiedMesh.getVertexAt(i);
      float distance = originalVertex.distance(modifiedVertex);
      totalDistance += distance;
    }

    float averageDisplacement = totalDistance / originalMesh.getVertexCount();

    assertTrue(averageDisplacement <= 1 && averageDisplacement >= 0);
  }

  @Test
  public void testDisplacementWithPositiveSeed() {
    Mesh3D originalMesh = new IcoSphereCreator(1, 3).create();
    Mesh3D modifiedMesh = new IcoSphereCreator(1, 3).create();

    modifier.setSeed(23445453);
    modifier.modify(modifiedMesh);

    float totalDistance = 0;

    for (int i = 0; i < originalMesh.getVertexCount(); i++) {
      Vector3f originalVertex = originalMesh.getVertexAt(i);
      Vector3f modifiedVertex = modifiedMesh.getVertexAt(i);
      float distance = originalVertex.distance(modifiedVertex);
      totalDistance += distance;
    }

    float averageDisplacement = totalDistance / originalMesh.getVertexCount();

    assertTrue(averageDisplacement <= 1 && averageDisplacement >= 0);
  }

  @ParameterizedTest
  @ValueSource(
      floats = {
        0.1f,
        0.322f,
        0.123f,
        0.022f,
        0.245f,
        0.751f,
        0.965f,
        10.98f,
        11.44521f,
        Float.MIN_VALUE,
      })
  public void testDislacementAverageWithPositiveMaximumValues(float maximum) {
    Mesh3D originalMesh = new IcoSphereCreator(1, 3).create();
    Mesh3D modifiedMesh = new IcoSphereCreator(1, 3).create();

    modifier.setMaximum(maximum);
    modifier.modify(modifiedMesh);

    float totalDistance = 0;

    for (int i = 0; i < originalMesh.getVertexCount(); i++) {
      Vector3f originalVertex = originalMesh.getVertexAt(i);
      Vector3f modifiedVertex = modifiedMesh.getVertexAt(i);
      float distance = originalVertex.distance(modifiedVertex);
      totalDistance += distance;
    }

    float averageDisplacement = totalDistance / originalMesh.getVertexCount();

    assertTrue(averageDisplacement <= maximum);
  }
}
