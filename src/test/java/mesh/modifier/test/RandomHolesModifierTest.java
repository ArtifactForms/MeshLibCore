package mesh.modifier.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.topology.ExtrudeModifier;
import mesh.modifier.topology.RandomHolesModifier;

public class RandomHolesModifierTest {

  private RandomHolesModifier modifier;

  @BeforeEach
  public void setUp() {
    modifier = new RandomHolesModifier();
  }

  @Test
  public void modifierImplementsIMeshModifierInterface() {
    assertTrue(modifier instanceof IMeshModifier);
  }

  @Test
  public void modifyMeshReturnsNonNullMesh() {
    Mesh3D mesh = modifier.modify(new CubeCreator().create());
    assertNotNull(mesh);
  }

  @Test
  public void modifyFaceReturnsNonNullMesh() {
    Mesh3D mesh = new CubeCreator().create();
    assertNotNull(modifier.modify(mesh, mesh.getFaceAt(0)));
  }

  @Test
  public void modifyMeshreturnsReferenceToModifiedMesh() {
    Mesh3D expected = new CubeCreator().create();
    Mesh3D actual = modifier.modify(expected);
    assertSame(expected, actual);
  }

  @Test
  public void testDefaultSeed() {
    assertEquals(0, modifier.getSeed());
  }

  @ParameterizedTest
  @ValueSource(longs = {0, 1, 20032, -130, Long.MIN_VALUE, Long.MAX_VALUE})
  public void testGetSetSeed(long expectedSeed) {
    modifier.setSeed(expectedSeed);
    assertEquals(expectedSeed, modifier.getSeed());
  }

  @Test
  public void testDefaultMinAmount() {
    float expected = 0.1f;
    assertEquals(expected, modifier.getMinAmount());
  }

  @Test
  public void testDefaultMaxAmount() {
    float expected = 0.9f;
    assertEquals(expected, modifier.getMaxAmount());
  }

  @Test
  public void faceCountPlane() {
    Mesh3D mesh = new PlaneCreator().create();
    mesh.apply(modifier);
    assertEquals(4, mesh.getFaceCount());
  }

  @Test
  public void testVerticesPlaneWithZeroSeed() {
    Vector3f[] expected =
        new Vector3f[] {
          new Vector3f(1.0f, 0.0f, -1.0f),
          new Vector3f(1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, -1.0f),
          new Vector3f(0.6847742f, 0.0f, -0.6847742f),
          new Vector3f(0.6847742f, 0.0f, 0.6847742f),
          new Vector3f(-0.6847742f, 0.0f, 0.6847742f),
          new Vector3f(-0.6847742f, 0.0f, -0.6847742f)
        };
    Mesh3D mesh = new PlaneCreator().create();
    RandomHolesModifier modifier = new RandomHolesModifier();
    modifier.setSeed(0);
    modifier.modify(mesh);
    assertArrayEquals(expected, mesh.vertices.toArray());
  }

  @Test
  public void testVerticesPlaneWithPositiveSeed() {
    Vector3f[] expected =
        new Vector3f[] {
          new Vector3f(1.0f, 0.0f, -1.0f),
          new Vector3f(1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, -1.0f),
          new Vector3f(0.62473404f, 0.0f, -0.62473404f),
          new Vector3f(0.62473404f, 0.0f, 0.62473404f),
          new Vector3f(-0.62473404f, 0.0f, 0.62473404f),
          new Vector3f(-0.62473404f, 0.0f, -0.62473404f)
        };

    Mesh3D mesh = new PlaneCreator().create();
    modifier.setSeed(1321441);
    modifier.modify(mesh);
    assertArrayEquals(expected, mesh.vertices.toArray());
  }

  @Test
  public void testVerticesPlaneWithNegativeSeed() {
    Vector3f[] expected =
        new Vector3f[] {
          new Vector3f(1.0f, 0.0f, -1.0f),
          new Vector3f(1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, -1.0f),
          new Vector3f(0.3719052f, 0.0f, -0.3719052f),
          new Vector3f(0.3719052f, 0.0f, 0.3719052f),
          new Vector3f(-0.3719052f, 0.0f, 0.3719052f),
          new Vector3f(-0.3719052f, 0.0f, -0.3719052f),
        };

    Mesh3D mesh = new PlaneCreator().create();
    modifier.setSeed(-133221441);
    modifier.modify(mesh);
    assertArrayEquals(expected, mesh.vertices.toArray());
  }

  @Test
  public void testVerticesWithMinMaxAndPositiveSeed() {
    Vector3f[] expected =
        new Vector3f[] {
          new Vector3f(1.0f, 0.0f, -1.0f),
          new Vector3f(1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, -1.0f),
          new Vector3f(0.7055122f, 0.0f, -0.7055122f),
          new Vector3f(0.7055122f, 0.0f, 0.7055122f),
          new Vector3f(-0.7055122f, 0.0f, 0.7055122f),
          new Vector3f(-0.7055122f, 0.0f, -0.7055122f)
        };
    Mesh3D mesh = new PlaneCreator().create();
    modifier = new RandomHolesModifier(0.24f, 0.9872f);
    modifier.setSeed(2323424);
    modifier.modify(mesh);
    assertArrayEquals(expected, mesh.vertices.toArray());
  }

  @Test
  public void testVerticesWithMinMaxAndNegativeSeed() {
    Vector3f[] expected =
        new Vector3f[] {
          new Vector3f(1.0f, 0.0f, -1.0f),
          new Vector3f(1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, -1.0f),
          new Vector3f(0.63038445f, 0.0f, -0.63038445f),
          new Vector3f(0.63038445f, 0.0f, 0.63038445f),
          new Vector3f(-0.63038445f, 0.0f, 0.63038445f),
          new Vector3f(-0.63038445f, 0.0f, -0.63038445f),
        };
    Mesh3D mesh = new PlaneCreator().create();
    modifier = new RandomHolesModifier(0.2344f, 0.87f);
    modifier.setSeed(2323424);
    modifier.modify(mesh);
    assertArrayEquals(expected, mesh.vertices.toArray());
  }

  @Test
  public void testVerticesWithMinMaxContstuctorAndPositiveSeed() {
    Vector3f[] expected =
        new Vector3f[] {
          new Vector3f(1.0f, 0.0f, -1.0f),
          new Vector3f(1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, 1.0f),
          new Vector3f(-1.0f, 0.0f, -1.0f),
          new Vector3f(0.7055122f, 0.0f, -0.7055122f),
          new Vector3f(0.7055122f, 0.0f, 0.7055122f),
          new Vector3f(-0.7055122f, 0.0f, 0.7055122f),
          new Vector3f(-0.7055122f, 0.0f, -0.7055122f)
        };
    float minAmount = 0.24f;
    float maxAmount = 0.9872f;
    Mesh3D mesh = new PlaneCreator().create();
    modifier = new RandomHolesModifier(minAmount, maxAmount);
    modifier.setSeed(2323424);
    modifier.modify(mesh);
    assertArrayEquals(expected, mesh.vertices.toArray());
  }

  @Test
  public void testPlaneFacesWithZeroSeed() {
    int[][] expected = new int[][] {{0, 1, 5, 4}, {1, 2, 6, 5}, {2, 3, 7, 6}, {3, 0, 4, 7}};
    Mesh3D mesh = new PlaneCreator().create();
    modifier.modify(mesh);
    for (int i = 0; i < mesh.getFaceCount(); i++) {
      int[] actual = mesh.getFaceAt(i).indices;
      assertArrayEquals(expected[i], actual);
    }
  }

  @ParameterizedTest
  @ValueSource(
      longs = {0, 1000, 134424, -14, -1342424293, 432424229, Long.MAX_VALUE, Long.MIN_VALUE})
  public void testPlaneFacesWithDifferentSeeds(long seed) {
    int[][] expected = new int[][] {{0, 1, 5, 4}, {1, 2, 6, 5}, {2, 3, 7, 6}, {3, 0, 4, 7}};
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
      longs = {0, 1000, 134424, -14, -1342424293, 432424229, Long.MAX_VALUE, Long.MIN_VALUE})
  public void testFacesWithDifferentSeeds(long seed) {
    int[][] expected =
        new int[][] {
          {3, 0, 9, 8},
          {0, 1, 10, 9},
          {1, 2, 11, 10},
          {2, 3, 8, 11},
          {6, 5, 13, 12},
          {5, 4, 14, 13},
          {4, 7, 15, 14},
          {7, 6, 12, 15},
          {1, 0, 17, 16},
          {0, 4, 18, 17},
          {4, 5, 19, 18},
          {5, 1, 16, 19},
          {1, 5, 21, 20},
          {5, 6, 22, 21},
          {6, 2, 23, 22},
          {2, 1, 20, 23},
          {6, 7, 25, 24},
          {7, 3, 26, 25},
          {3, 2, 27, 26},
          {2, 6, 24, 27},
          {3, 7, 29, 28},
          {7, 4, 30, 29},
          {4, 0, 31, 30},
          {0, 3, 28, 31}
        };
    Mesh3D mesh = new CubeCreator().create();
    modifier.setSeed(seed);
    modifier.modify(mesh);
    for (int i = 0; i < expected.length; i++) {
      int[] actual = mesh.getFaceAt(i).indices;
      assertArrayEquals(expected[i], actual);
    }
  }

  @ParameterizedTest
  @ValueSource(floats = {0.1f, 0.135f, 0.13f, 0.2f, 0.23555f, 0.721f, 0.9654f, Float.MIN_VALUE})
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
    modifier.modify(actual);

    assertArrayEquals(expected.vertices.toArray(), actual.vertices.toArray());
  }
}
