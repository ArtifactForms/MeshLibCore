package mesh.modifier.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import mesh.Mesh3D;
import mesh.util.MeshBoundsCalculator;

class FitToAABBModifierTest {

  private static final float EPSILON = 0.0001f;

  @Test
  void shouldScaleMeshToFitTargetAABB() {
    Mesh3D mesh = createMesh(10, 20, 30);
    FitToAABBModifier modifier = new FitToAABBModifier(5, 5, 5);

    Mesh3D result = modifier.modify(mesh);

    assertSame(mesh, result);
    assertDimensions(result, 5f / 3f, 10f / 3f, 5f);
  }

  @Test
  void shouldPreserveAspectRatio() {
    Mesh3D mesh = createMesh(10, 20, 30);
    FitToAABBModifier modifier = new FitToAABBModifier(6, 8, 10);

    modifier.modify(mesh);

    assertDimensions(mesh, 2f, 4f, 6f);
  }

  @Test
  void shouldNotScaleMeshWhenAlreadyAtTargetScale() {
    Mesh3D mesh = createMesh(10, 20, 30);
    FitToAABBModifier modifier = new FitToAABBModifier(30, 40, 50);

    modifier.modify(mesh);

    assertDimensions(mesh, 10, 20, 30);
  }

  @Test
  void shouldReturnEmptyMeshUnchanged() {
    Mesh3D mesh = new Mesh3D();
    FitToAABBModifier modifier = new FitToAABBModifier(10, 10, 10);

    Mesh3D result = modifier.modify(mesh);

    assertSame(mesh, result);
    assertEquals(0, result.getVertexCount());
  }

  @Test
  void shouldThrowExceptionWhenMeshIsNull() {
    FitToAABBModifier modifier = new FitToAABBModifier(10, 10, 10);

    assertThrows(IllegalArgumentException.class, () -> modifier.modify(null));
  }

  @Test
  void shouldThrowExceptionWhenTargetWidthIsZero() {
    assertThrows(IllegalArgumentException.class, () -> new FitToAABBModifier(0, 10, 10));
  }

  @Test
  void shouldThrowExceptionWhenTargetHeightIsZero() {
    assertThrows(IllegalArgumentException.class, () -> new FitToAABBModifier(10, 0, 10));
  }

  @Test
  void shouldThrowExceptionWhenTargetDepthIsZero() {
    assertThrows(IllegalArgumentException.class, () -> new FitToAABBModifier(10, 10, 0));
  }

  @Test
  void shouldThrowExceptionWhenTargetDimensionIsNegative() {
    assertThrows(IllegalArgumentException.class, () -> new FitToAABBModifier(10, -5, 10));
  }

  @Test
  void shouldHandleMultipleMeshesIndependently() {
    FitToAABBModifier modifier = new FitToAABBModifier(5, 5, 5);

    Mesh3D firstMesh = createMesh(10, 20, 30);
    Mesh3D secondMesh = createMesh(20, 40, 60);

    modifier.modify(firstMesh);
    modifier.modify(secondMesh);

    assertDimensions(firstMesh, 5f / 3f, 10f / 3f, 5f);
    assertDimensions(secondMesh, 5f / 3f, 10f / 3f, 5f);
  }

  @Test
  void shouldNotAccumulateScalingAcrossCalls() {
    FitToAABBModifier modifier = new FitToAABBModifier(5, 5, 5);
    Mesh3D mesh = createMesh(10, 20, 30);

    modifier.modify(mesh);
    modifier.modify(mesh);

    assertDimensions(mesh, 5f / 3f, 10f / 3f, 5f);
  }

  /**
   * Creates a mesh with the specified bounding dimensions.
   *
   * @param width the mesh width.
   * @param height the mesh height.
   * @param depth the mesh depth.
   * @return the created mesh.
   */
  private Mesh3D createMesh(float width, float height, float depth) {
    Mesh3D mesh = new Mesh3D();

    mesh.addVertex(0, 0, 0);
    mesh.addVertex(width, 0, 0);
    mesh.addVertex(0, height, 0);
    mesh.addVertex(0, 0, depth);

    return mesh;
  }

  /**
   * Asserts that the mesh has the expected bounding dimensions.
   *
   * @param mesh the mesh to inspect.
   * @param expectedWidth the expected width.
   * @param expectedHeight the expected height.
   * @param expectedDepth the expected depth.
   */
  private void assertDimensions(
      Mesh3D mesh, float expectedWidth, float expectedHeight, float expectedDepth) {

    var bounds = MeshBoundsCalculator.calculateBounds(mesh);

    assertEquals(expectedWidth, bounds.getWidth(), EPSILON);
    assertEquals(expectedHeight, bounds.getHeight(), EPSILON);
    assertEquals(expectedDepth, bounds.getDepth(), EPSILON);
  }
}
