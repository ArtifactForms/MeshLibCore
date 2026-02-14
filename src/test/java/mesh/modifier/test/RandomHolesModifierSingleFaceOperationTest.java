package mesh.modifier.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.FaceModifier;
import mesh.modifier.topology.ExtrudeModifier;
import mesh.modifier.topology.RandomHolesModifier;

public class RandomHolesModifierSingleFaceOperationTest {

  private RandomHolesModifier modifier;

  @BeforeEach
  public void setUp() {
    modifier = new RandomHolesModifier();
  }

  @Test
  public void implementsFaceModifierInterface() {
    assertTrue(modifier instanceof FaceModifier);
  }

  @Test
  public void returnsNonNullMesh() {
    Mesh3D mesh = new CubeCreator().create();
    assertNotNull(modifier.modify(mesh, mesh.getFaceAt(4)));
  }

  @Test
  public void returnsReferenceOfModifiedMesh() {
    Mesh3D mesh = new CubeCreator().create();
    assertSame(mesh, modifier.modify(mesh, mesh.getFaceAt(0)));
  }

  @Test
  public void nullMeshThrowsException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          modifier.modify(null, new Face3D());
        });
  }

  @Test
  public void nullFaceThrowsException() {
    Face3D face = null;
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          modifier.modify(new Mesh3D(), face);
        });
  }

  @Test
  public void nullMeshAndNullFaceThrowsException() {
    Mesh3D mesh = null;
    Face3D face = null;
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          modifier.modify(mesh, face);
        });
  }

  @Test
  public void testFaceCountPlane() {
    Mesh3D plane = new PlaneCreator().create();
    modifier.modify(plane, plane.getFaceAt(0));
    assertEquals(4, plane.getFaceCount());
  }

  @Test
  public void testVertexCountPlane() {
    Mesh3D plane = new PlaneCreator().create();
    modifier.modify(plane, plane.getFaceAt(0));
    assertEquals(8, plane.getVertexCount());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3, 4, 5})
  public void testFaceCountCube(int faceIndex) {
    Mesh3D cube = new CubeCreator().create();
    modifier.modify(cube, cube.getFaceAt(faceIndex));
    assertEquals(9, cube.getFaceCount());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3, 4, 5})
  public void testVertexCountCube(int faceIndex) {
    Mesh3D cube = new CubeCreator().create();
    modifier.modify(cube, cube.getFaceAt(faceIndex));
    assertEquals(12, cube.getVertexCount());
  }

  @Test
  public void testPlaneVerticesWithZeroSeeed() {
    Vector3f[] expected = {
      new Vector3f(1.0f, 0.0f, -1.0f),
      new Vector3f(1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, -1.0f),
      new Vector3f(0.6847742f, 0.0f, -0.6847742f),
      new Vector3f(0.6847742f, 0.0f, 0.6847742f),
      new Vector3f(-0.6847742f, 0.0f, 0.6847742f),
      new Vector3f(-0.6847742f, 0.0f, -0.6847742f)
    };
    Mesh3D plane = new PlaneCreator().create();
    modifier.setSeed(0);
    modifier.modify(plane, plane.getFaceAt(0));
    assertArrayEquals(expected, plane.getVertices().toArray());
  }

  @Test
  public void testPlaneVerticesWithPositiveSeeed() {
    Vector3f[] expected = {
      new Vector3f(1.0f, 0.0f, -1.0f),
      new Vector3f(1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, -1.0f),
      new Vector3f(0.31211585f, 0.0f, -0.31211585f),
      new Vector3f(0.31211585f, 0.0f, 0.31211585f),
      new Vector3f(-0.31211585f, 0.0f, 0.31211585f),
      new Vector3f(-0.31211585f, 0.0f, -0.31211585f),
    };
    Mesh3D plane = new PlaneCreator().create();
    modifier.setSeed(13424);
    modifier.modify(plane, plane.getFaceAt(0));
    assertArrayEquals(expected, plane.getVertices().toArray());
  }

  @Test
  public void testPlaneVerticesWithNegativeSeeed() {
    Vector3f[] expected = {
      new Vector3f(1.0f, 0.0f, -1.0f),
      new Vector3f(1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, -1.0f),
      new Vector3f(0.2969781f, 0.0f, -0.2969781f),
      new Vector3f(0.2969781f, 0.0f, 0.2969781f),
      new Vector3f(-0.2969781f, 0.0f, 0.2969781f),
      new Vector3f(-0.2969781f, 0.0f, -0.2969781f),
    };
    Mesh3D plane = new PlaneCreator().create();
    modifier.setSeed(-3244324);
    modifier.modify(plane, plane.getFaceAt(0));
    assertArrayEquals(expected, plane.getVertices().toArray());
  }

  @Test
  public void testPlaneVerticesWithSeedMaxLong() {
    Vector3f[] expected = {
      new Vector3f(1.0f, 0.0f, -1.0f),
      new Vector3f(1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, -1.0f),
      new Vector3f(0.31515408f, 0.0f, -0.31515408f),
      new Vector3f(0.31515408f, 0.0f, 0.31515408f),
      new Vector3f(-0.31515408f, 0.0f, 0.31515408f),
      new Vector3f(-0.31515408f, 0.0f, -0.31515408f),
    };
    Mesh3D plane = new PlaneCreator().create();
    modifier.setSeed(Long.MAX_VALUE);
    modifier.modify(plane, plane.getFaceAt(0));
    assertArrayEquals(expected, plane.getVertices().toArray());
  }

  @Test
  public void testPlaneVerticesWithSeedMinLong() {
    Vector3f[] expected = {
      new Vector3f(1.0f, 0.0f, -1.0f),
      new Vector3f(1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, 1.0f),
      new Vector3f(-1.0f, 0.0f, -1.0f),
      new Vector3f(0.6847742f, 0.0f, -0.6847742f),
      new Vector3f(0.6847742f, 0.0f, 0.6847742f),
      new Vector3f(-0.6847742f, 0.0f, 0.6847742f),
      new Vector3f(-0.6847742f, 0.0f, -0.6847742f),
    };
    Mesh3D plane = new PlaneCreator().create();
    modifier.setSeed(Long.MIN_VALUE);
    modifier.modify(plane, plane.getFaceAt(0));
    assertArrayEquals(expected, plane.getVertices().toArray());
  }

  @Test
  public void tesCubeVerticesWithPositiveSeed() {
    Vector3f[] expected =
        new Vector3f[] {
          new Vector3f(1.0f, -1.0f, -1.0f),
          new Vector3f(1.0f, -1.0f, 1.0f),
          new Vector3f(-1.0f, -1.0f, 1.0f),
          new Vector3f(-1.0f, -1.0f, -1.0f),
          new Vector3f(1.0f, 1.0f, -1.0f),
          new Vector3f(1.0f, 1.0f, 1.0f),
          new Vector3f(-1.0f, 1.0f, 1.0f),
          new Vector3f(-1.0f, 1.0f, -1.0f),
          new Vector3f(-1.0f, 0.2517369f, 0.2517369f),
          new Vector3f(-1.0f, 0.2517369f, -0.2517369f),
          new Vector3f(-1.0f, -0.2517369f, -0.2517369f),
          new Vector3f(-1.0f, -0.2517369f, 0.2517369f),
        };
    Mesh3D mesh = new CubeCreator().create();

    modifier = new RandomHolesModifier(0.1f, 0.3f);
    modifier.setSeed(234453);
    modifier.modify(mesh, mesh.getFaceAt(4));
    assertArrayEquals(expected, mesh.getVertices().toArray());
  }

  @Test
  public void testCubeFacesWithPositiveSeed() {
    int[][] expected =
        new int[][] {
          {3, 0, 1, 2},
          {6, 5, 4, 7},
          {1, 0, 4, 5},
          {1, 5, 6, 2},
          {3, 7, 4, 0},
          {6, 7, 9, 8},
          {7, 3, 10, 9},
          {3, 2, 11, 10},
          {2, 6, 8, 11},
        };
    Mesh3D mesh = new CubeCreator().create();
    modifier = new RandomHolesModifier(0.1f, 0.3f);
    modifier.setSeed(234453);
    modifier.modify(mesh, mesh.getFaceAt(4));
    for (int i = 0; i < mesh.getFaceCount(); i++) {
      int[] actual = mesh.getFaceAt(i).indices;
      assertArrayEquals(expected[i], actual);
    }
  }

  @ParameterizedTest
  @ValueSource(longs = {0, 13424, -3244324, Long.MAX_VALUE, Long.MIN_VALUE})
  public void testPlaneFaceIndicesWithSeeds(long seed) {
    int[][] expected =
        new int[][] {
          {0, 1, 5, 4}, {1, 2, 6, 5}, {2, 3, 7, 6}, {3, 0, 4, 7},
        };
    Mesh3D mesh = new PlaneCreator().create();
    modifier.setSeed(seed);
    modifier.modify(mesh);
    for (int i = 0; i < mesh.getFaceCount(); i++) {
      int[] actual = mesh.getFaceAt(i).indices;
      assertArrayEquals(expected[i], actual);
    }
  }

  @ParameterizedTest
  @ValueSource(
      floats = {
        0.1f, 0.322f, 0.123f, 0.022f, 0.245f, 0.751f, 0.965f,
      })
  public void testMinMaxAmountPositiveValuesAndSeed(float amount) {
    Mesh3D expected = new PlaneCreator().create();
    ExtrudeModifier extrudeModifier = new ExtrudeModifier();
    extrudeModifier.setRemoveFaces(true);
    extrudeModifier.setScale(amount);
    extrudeModifier.setAmount(0);
    expected.apply(extrudeModifier);

    Mesh3D actual = new PlaneCreator().create();
    modifier = new RandomHolesModifier(amount, amount);
    modifier.setSeed(234);
    modifier.modify(actual, actual.getFaceAt(0));

    assertArrayEquals(expected.getVertices().toArray(), actual.getVertices().toArray());
  }
}
