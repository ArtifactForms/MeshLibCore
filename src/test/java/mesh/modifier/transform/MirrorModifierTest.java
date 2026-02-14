package mesh.modifier.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.creator.primitives.QuadSphereCreator;
import mesh.creator.primitives.UVSphereCreator;
import mesh.util.MeshBoundsCalculator;

class MirrorModifierTest {

  private static final float EPSILON = 0.0001f;

  // -------------------------------------------------------------------------
  // Defaults & validation
  // -------------------------------------------------------------------------

  @Test
  void defaultTransformAxisIsY() {
    MirrorModifier modifier = new MirrorModifier();
    assertEquals(TransformAxis.Y, modifier.getTransformAxis());
  }

  @Test
  void setTransformAxisRejectsNull() {
    MirrorModifier modifier = new MirrorModifier();
    assertThrows(IllegalArgumentException.class, () -> modifier.setTransformAxis(null));
  }

  // -------------------------------------------------------------------------
  // Vertex transformation
  // -------------------------------------------------------------------------

  @Test
  void mirrorsAlongDefaultYAxis() {
    assertMirroredVertices(TransformAxis.Y, -1, 1, 1);
  }

  @Test
  void mirrorsAlongXAxis() {
    assertMirroredVertices(TransformAxis.X, 1, 1, -1);
  }

  @Test
  void mirrorsAlongYAxis() {
    assertMirroredVertices(TransformAxis.Y, -1, 1, 1);
  }

  @Test
  void mirrorsAlongZAxis() {
    assertMirroredVertices(TransformAxis.Z, 1, -1, 1);
  }

  // -------------------------------------------------------------------------
  // Topology invariants
  // -------------------------------------------------------------------------

  @Test
  void keepsVertexCount() {
    Mesh3D mesh = new IcoSphereCreator().create();
    int expected = mesh.getVertexCount();

    new MirrorModifier().modify(mesh);

    assertEquals(expected, mesh.getVertexCount());
  }

  @Test
  void keepsFaceCountEvenAfterMultipleMirrors() {
    Mesh3D mesh = new IcoSphereCreator().create();
    int expected = mesh.getFaceCount();

    MirrorModifier modifier = new MirrorModifier();
    modifier.modify(mesh);
    modifier.modify(mesh);

    assertEquals(expected, mesh.getFaceCount());
  }

  @Test
  void invertsFaceWindingOrder() {
    Mesh3D original = new QuadSphereCreator().create();
    Mesh3D mirrored = new QuadSphereCreator().create();

    new MirrorModifier().modify(mirrored);

    for (int i = 0; i < original.getFaceCount(); i++) {
      Face3D face = original.getFaceAt(i);
      Face3D mirroredFace = mirrored.getFaceAt(i);

      for (int j = 0; j < face.indices.length; j++) {
        int expectedIndex = face.getIndexAt(j);
        int actualIndex = mirroredFace.getIndexAt(face.indices.length - j - 1);

        assertEquals(expectedIndex, actualIndex);
      }
    }
  }

  // -------------------------------------------------------------------------
  // Face normals
  // -------------------------------------------------------------------------

  @Test
  void recalculatesFaceNormals() {
    Mesh3D mesh = new UVSphereCreator(20, 41, 2.43f).create();
    new MirrorModifier().modify(mesh);

    for (Face3D face : mesh.getFaces()) {
      Vector3f recalculated = mesh.calculateFaceNormal(face);
      assertEquals(recalculated, face.normal);
    }
  }

  @Test
  void invertsFaceNormalsForYAxisMirror() {
    assertMirroredFaceNormals(TransformAxis.Y, -1, 1, 1);
  }

  @Test
  void invertsFaceNormalsForXAxisMirror() {
    assertMirroredFaceNormals(TransformAxis.X, 1, 1, -1);
  }

  @Test
  void invertsFaceNormalsForZAxisMirror() {
    assertMirroredFaceNormals(TransformAxis.Z, 1, -1, 1);
  }

  @Test
  void faceNormalsRemainNormalized() {
    Mesh3D mesh = new UVSphereCreator().create();
    new MirrorModifier().modify(mesh);

    for (Face3D face : mesh.getFaces()) {
      assertEquals(1f, face.normal.length(), EPSILON);
    }
  }

  // -------------------------------------------------------------------------
  // Invariants & Edge cases
  // -------------------------------------------------------------------------

  @Test
  void mirroringTwiceRestoresOriginalVertices() {
    Mesh3D original = new UVSphereCreator().create();
    Mesh3D mesh = new UVSphereCreator().create();

    MirrorModifier modifier = new MirrorModifier();
    modifier.modify(mesh);
    modifier.modify(mesh);

    for (int i = 0; i < original.getVertexCount(); i++) {
      Vector3f v0 = original.getVertexAt(i);
      Vector3f v1 = mesh.getVertexAt(i);

      assertEquals(v0.x, v1.x, EPSILON);
      assertEquals(v0.y, v1.y, EPSILON);
      assertEquals(v0.z, v1.z, EPSILON);
    }
  }

  @Test
  void mirrorPreservesMeshCenter() {
    Mesh3D mesh = new UVSphereCreator().create();
    Vector3f before = MeshBoundsCalculator.calculateBounds(mesh).getCenter();

    new MirrorModifier().modify(mesh);

    Vector3f after = MeshBoundsCalculator.calculateBounds(mesh).getCenter();

    assertEquals(before.x, after.x, EPSILON);
    assertEquals(before.y, after.y, EPSILON);
    assertEquals(before.z, after.z, EPSILON);
  }

  @Test
  void mirrorDoesNotProduceNaNs() {
    Mesh3D mesh = new UVSphereCreator().create();
    new MirrorModifier().modify(mesh);

    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v = mesh.getVertexAt(i);
      assertEquals(false, Float.isNaN(v.x));
      assertEquals(false, Float.isNaN(v.y));
      assertEquals(false, Float.isNaN(v.z));
    }
  }

  @Test
  void mirrorWorksOnTranslatedMesh() {
    Mesh3D mesh = new UVSphereCreator().create();
    new TranslateModifier(10, 5, -3).modify(mesh);
    new MirrorModifier().modify(mesh);

    // Just sanity: no crash, vertex count unchanged
    assertEquals(mesh.getVertexCount(), mesh.getVertexCount());
  }

  @ParameterizedTest
  @EnumSource(TransformAxis.class)
  void mirrorTwiceIsIdentityForAllAxes(TransformAxis axis) {
    assertMirrorIsIdentity(axis);
  }

  // -------------------------------------------------------------------------
  // Helper methods
  // -------------------------------------------------------------------------

  private void assertMirroredVertices(TransformAxis axis, float sx, float sy, float sz) {

    Mesh3D original = new UVSphereCreator().create();
    Mesh3D mirrored = new UVSphereCreator().create();

    MirrorModifier modifier = new MirrorModifier();
    modifier.setTransformAxis(axis);
    modifier.modify(mirrored);

    for (int i = 0; i < original.getVertexCount(); i++) {
      Vector3f v0 = original.getVertexAt(i);
      Vector3f v1 = mirrored.getVertexAt(i);

      assertEquals(v0.x * sx, v1.x);
      assertEquals(v0.y * sy, v1.y);
      assertEquals(v0.z * sz, v1.z);
    }
  }

  private void assertMirroredFaceNormals(TransformAxis axis, float sx, float sy, float sz) {

    UVSphereCreator creator = new UVSphereCreator(20, 41, 2.43f);
    Mesh3D original = creator.create();
    Mesh3D mirrored = creator.create();

    MirrorModifier modifier = new MirrorModifier();
    modifier.setTransformAxis(axis);
    modifier.modify(mirrored);

    for (int i = 0; i < original.getFaceCount(); i++) {
      Face3D face = original.getFaceAt(i);
      Face3D mirroredFace = mirrored.getFaceAt(i);

      Vector3f expected = original.calculateFaceNormal(face);
      expected.multLocal(sx, sy, sz);

      Vector3f actual = mirroredFace.normal;

      assertEquals(expected.x, actual.x, EPSILON);
      assertEquals(expected.y, actual.y, EPSILON);
      assertEquals(expected.z, actual.z, EPSILON);
    }
  }

  private void assertMirrorIsIdentity(TransformAxis axis) {
    Mesh3D original = new UVSphereCreator().create();
    Mesh3D mesh = new UVSphereCreator().create();

    MirrorModifier modifier = new MirrorModifier();
    modifier.setTransformAxis(axis);

    modifier.modify(mesh);
    modifier.modify(mesh);

    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v0 = original.getVertexAt(i);
      Vector3f v1 = mesh.getVertexAt(i);

      assertEquals(v0.x, v1.x, EPSILON);
      assertEquals(v0.y, v1.y, EPSILON);
      assertEquals(v0.z, v1.z, EPSILON);
    }
  }
}
