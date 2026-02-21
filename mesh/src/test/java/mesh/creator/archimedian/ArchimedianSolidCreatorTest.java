package mesh.creator.archimedian;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import mesh.Mesh3D;

public class ArchimedianSolidCreatorTest {

  @Test
  public void testIcosidodecahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.ICOSIDODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new IcosidodecahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testTruncatedCuboctahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_CUBOCTAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedCuboctahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testRhombicuboctahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.RHOMBICUBOCTAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new RhombicuboctahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testTruncatedIcosidodecahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_ICOSIDODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedIcosidodecahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testCuboctahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.CUBOCTAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new CuboctahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testSnubCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.SNUB_CUBE;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new SnubCubeCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testRhombicosidodecahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.RHOMBISOSIDODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new RhombicosidodecahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testSnubDodecahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.SNUB_DODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new SnubDodecahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testTruncatedTetrahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_TETRAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedTetrahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testTruncatedOctahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_OCTAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedOctahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testTruncatedCubeCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_CUBE;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedCubeCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testTruncatedIcosahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_ICOSAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedIcosahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testTruncatedDodecahedronCreatorVertices() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_DODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedDodecahedronCreator().create();
    assertEquals(expectedMesh.getVertices(), actualMesh.getVertices());
  }

  @Test
  public void testIcosidodecahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.ICOSIDODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new IcosidodecahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testTruncatedCuboctahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_CUBOCTAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedCuboctahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testRhombicuboctahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.RHOMBICUBOCTAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new RhombicuboctahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testTruncatedIcosidodecahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_ICOSIDODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedIcosidodecahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testCuboctahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.CUBOCTAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new CuboctahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testSnubCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.SNUB_CUBE;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new SnubCubeCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testRhombicosidodecahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.RHOMBISOSIDODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new RhombicosidodecahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testSnubDodecahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.SNUB_DODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new SnubDodecahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testTruncatedTetrahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_TETRAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedTetrahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testTruncatedOctahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_OCTAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedOctahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testTruncatedCubeCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_CUBE;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedCubeCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testTruncatedIcosahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_ICOSAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedIcosahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }

  @Test
  public void testTruncatedDodecahedronCreatorFaces() {
    ArchimedianSolid type = ArchimedianSolid.TRUNCATED_DODECAHEDRON;
    Mesh3D actualMesh = new ArchimedianSolidCreator(type).create();
    Mesh3D expectedMesh = new TruncatedDodecahedronCreator().create();
    for (int i = 0; i < actualMesh.getFaceCount(); i++) {
      int[] expected = expectedMesh.getFaceAt(i).indices;
      int[] actual = actualMesh.getFaceAt(i).indices;
      assertArrayEquals(expected, actual);
    }
  }
}
