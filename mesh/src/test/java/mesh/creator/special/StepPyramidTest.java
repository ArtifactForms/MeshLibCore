package mesh.creator.special;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Bounds;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.util.MeshBoundsCalculator;
import util.MeshTestUtil;

class StepPyramidTest {

  private StepPyramidCreator creator;

  @BeforeEach
  void setUp() {
    creator = new StepPyramidCreator();
  }

  // =========================================================
  // Basic Behavior
  // =========================================================

  @Test
  void implementsIMeshCreator() {
    assertTrue(creator instanceof IMeshCreator);
  }

  @Test
  void createReturnsNonNullMesh() {
    assertNotNull(creator.create());
  }

  // =========================================================
  // Geometry Validation
  // =========================================================

  @Test
  void verticesContainNoNaNOrInfinity() {
    Mesh3D mesh = creator.create();

    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v = mesh.getVertexAt(i);
      assertFalse(Float.isNaN(v.x) || Float.isNaN(v.y) || Float.isNaN(v.z));
      assertFalse(Float.isInfinite(v.x) || Float.isInfinite(v.y) || Float.isInfinite(v.z));
    }
  }

  @Test
  void meshHasValidBoundingBox() {
    Bounds bounds = MeshBoundsCalculator.calculateBounds(creator.create());

    assertTrue(bounds.getWidth() > 0);
    assertTrue(bounds.getHeight() > 0);
    assertTrue(bounds.getDepth() > 0);
  }

  // =========================================================
  // Face & Vertex Count
  // =========================================================

  @Test
  void defaultVertexCount() {
    int sides = 4;
    int steps = 10;
    int expected = (sides * steps) * 2;

    assertEquals(expected, creator.create().getVertexCount());
  }

  @Test
  void defaultFaceCount() {
    int sides = 4;
    int steps = 10;
    int sideFaces = (sides * steps) * 2 - sides;
    int expected = 1 + 1 + sideFaces;

    assertEquals(expected, creator.create().getFaceCount());
  }

  // =========================================================
  // Cap Variations
  // =========================================================

  @Test
  void faceCountWithNoTopAndNGonBottom() {
    verifyFaceCount(5, 26, FillType.NOTHING, FillType.N_GON, 0, 1);
  }

  @Test
  void faceCountWithNGonTopAndNoBottom() {
    verifyFaceCount(5, 26, FillType.N_GON, FillType.NOTHING, 1, 0);
  }

  @Test
  void faceCountWithNGonCaps() {
    verifyFaceCount(5, 26, FillType.N_GON, FillType.N_GON, 1, 1);
  }

  @Test
  void faceCountWithTopTriangleFan() {
    verifyFaceCount(5, 26, FillType.TRIANGLE_FAN, FillType.N_GON, 5, 1);
  }

  @Test
  void faceCountWithBottomTriangleFan() {
    verifyFaceCount(5, 26, FillType.N_GON, FillType.TRIANGLE_FAN, 1, 5);
  }

  @Test
  void faceCountWithBothTriangleFan() {
    verifyFaceCount(5, 26, FillType.TRIANGLE_FAN, FillType.TRIANGLE_FAN, 5, 5);
  }

  @Test
  void faceCountWithBottomFanAndNoTop() {
    verifyFaceCount(5, 26, FillType.NOTHING, FillType.TRIANGLE_FAN, 0, 5);
  }

  @Test
  void faceCountWithTopFanAndNoBottom() {
    verifyFaceCount(5, 26, FillType.TRIANGLE_FAN, FillType.NOTHING, 5, 0);
  }

  @Test
  void faceCountWithNoBottomNoTop() {
    verifyFaceCount(5, 26, FillType.NOTHING, FillType.NOTHING, 0, 0);
  }

  private void verifyFaceCount(
      int sides, int steps, FillType topType, FillType bottomType, int topFaces, int bottomFaces) {

    creator.setSidesCount(sides);
    creator.setStepsCount(steps);
    creator.setTopCapFillType(topType);
    creator.setBottomCapFillType(bottomType);

    int sideFaces = (sides * steps) * 2 - sides;
    int expected = topFaces + bottomFaces + sideFaces;

    assertEquals(expected, creator.create().getFaceCount());
  }

  // =========================================================
  // Setter Validation
  // =========================================================

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1, 2})
  void setSidesCountRejectsInvalidValues(int value) {
    assertThrows(IllegalArgumentException.class, () -> creator.setSidesCount(value));
  }

  @ParameterizedTest
  @ValueSource(ints = {-3, -2, -1, 0})
  void setStepsCountRejectsInvalidValues(int value) {
    assertThrows(IllegalArgumentException.class, () -> creator.setStepsCount(value));
  }

  @ParameterizedTest
  @ValueSource(floats = {-3f, -2f, -1f, 0f})
  void setInitialDiameterRejectsInvalidValues(float value) {
    assertThrows(IllegalArgumentException.class, () -> creator.setInitialDiameter(value));
  }

  @ParameterizedTest
  @ValueSource(floats = {-1f, 0f})
  void setStepHeightRejectsInvalidValues(float value) {
    assertThrows(IllegalArgumentException.class, () -> creator.setStepHeight(value));
  }

  @Test
  void setDiameterReductionRejectsNegativeValues() {
    assertThrows(IllegalArgumentException.class, () -> creator.setDiameterReductionPerStep(-0.1f));
  }

  @Test
  void setTopCapFillTypeRejectsNull() {
    assertThrows(IllegalArgumentException.class, () -> creator.setTopCapFillType(null));
  }

  @Test
  void setBottomCapFillTypeRejectsNull() {
    assertThrows(IllegalArgumentException.class, () -> creator.setBottomCapFillType(null));
  }

  // =========================================================
  // Default Values
  // =========================================================

  @Test
  void defaultValuesAreCorrect() {
    assertEquals(4, creator.getSidesCount());
    assertEquals(10, creator.getStepsCount());
    assertEquals(0.1f, creator.getStepHeight());
    assertEquals(2f, creator.getInitialDiameter());
    assertEquals(0.2f, creator.getDiameterReductionPerStep());
    assertEquals(FillType.N_GON, creator.getTopCapFillType());
    assertEquals(FillType.N_GON, creator.getBottomCapFillType());
  }

  // =========================================================
  // Mesh Quality
  // =========================================================

  @Test
  void meshIsManifold() {
    assertTrue(MeshTestUtil.isManifold(creator.create()));
  }

  @Test
  void meshHasNoLooseVertices() {
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(creator.create()));
  }

  @Test
  void meshHasNoDuplicateFaces() {
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(creator.create()));
  }

  @Test
  void meshHasNoDuplicateVertices() {
    assertTrue(MeshTestUtil.meshHasNoDuplicatedVertices(creator.create()));
  }

  // =========================================================
  // Edge case
  // =========================================================

  @Test
  void singleStepCreatesValidMesh() {
    creator.setStepsCount(1);

    Mesh3D mesh = creator.create();

    assertEquals(8, mesh.getVertexCount());
    assertEquals(6, mesh.getFaceCount());

    assertBaseMeshTests(mesh);
  }

  @Test
  void diameterExactlyCollapsesToZero() {
    creator.setSidesCount(4);
    creator.setStepsCount(10);
    creator.setInitialDiameter(2f);
    creator.setDiameterReductionPerStep(0.2f); // 10 * 0.2 = 2.0

    Mesh3D mesh = creator.create();

    assertNotNull(mesh);
    assertBaseMeshTests(mesh);
  }

  // =========================================================
  // Mesh characteristics
  // =========================================================

  static void assertBaseMeshTests(Mesh3D mesh) {
    assertTrue(MeshTestUtil.isManifold(mesh));
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(mesh));
    assertTrue(MeshTestUtil.meshHasNoDuplicatedVertices(mesh));
  }
}
