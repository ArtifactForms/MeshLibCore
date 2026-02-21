package regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mesh.Mesh3D;
import mesh.creator.special.VariableCylinderCreator;
import util.MeshTestUtil;

/**
 * Regression test for a bottom cap rotation bug in {@link VariableCylinderCreator}.
 *
 * <p>Previously, the bottom cap was rotated around the wrong axis (Z instead of X). This incorrect
 * transformation corrupted face connectivity and resulted in a non-manifold mesh (edges were not
 * shared by exactly two faces).
 *
 * <p>The issue became visible when generating a minimal two-ring cylinder, where the cap
 * orientation directly affected edge pairing.
 *
 * <p>The fix rotates the bottom cap around the X axis, restoring proper topology and guaranteeing
 * manifoldness.
 *
 * <p>This test ensures that the generated mesh is manifold and will fail if the rotation logic
 * regresses.
 */
public class VariableCylinderRegressionTest {

  @Test
  public void bottomCapRotationProducesManifoldMesh() {
    float rotationSegments = 15.850006f;

    VariableCylinderCreator creator = new VariableCylinderCreator();
    creator.setRotationSegments((int) rotationSegments);
    creator.addRingSegment(1, 0);
    creator.addRingSegment(2, 0);

    Mesh3D mesh = creator.create();

    assertTrue(
        MeshTestUtil.isManifold(mesh),
        "Cylinder must be manifold – bottom cap rotation must not break edge connectivity.");
  }
}
