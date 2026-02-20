package regression;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.modifier.topology.BevelEdgesModifier;
import util.MeshTestUtil;

/**
 * Test to validate the bug fix in BevelEdgesModifier.
 *
 * <p><b>Bug Summary:</b> The {@code BevelEdgesModifier} produced incorrect face indices for
 * pentagonal faces when applied to an icosphere. This issue was traced to the {@code
 * toReverseArray} method, which incorrectly sorted indices in reverse order instead of merely
 * reversing them.
 *
 * <p><b>Purpose:</b> Ensure the {@code BevelEdgesModifier} correctly generates pentagonal faces
 * with the proper index order after the bug fix.
 *
 * <p><b>Reproduction Details:</b>
 *
 * <ul>
 *   <li>Create an icosphere using {@code IcosahedronCreator}.
 *   <li>Apply the {@code BevelEdgesModifier} with a small amount (e.g., 0.1).
 *   <li>Compare the indices of generated pentagonal faces with expected values.
 * </ul>
 *
 * <p><b>Expected Behavior:</b> The modifier produces pentagonal faces with index order matching the
 * predefined {@code expected} array.
 *
 * <p><b>Validation:</b> This test confirms that the revised {@code toReverseArray} implementation
 * resolves the issue, ensuring correct face generation for an icosphere.
 */
public class BevelEdgesModifierIndexOrderRegressionTest {

  private Mesh3D mesh;

  @BeforeEach
  public void setUp() {
    float amount = 0.1f;
    BevelEdgesModifier modifier = new BevelEdgesModifier(amount);
    mesh = new IcosahedronCreator().create();
    modifier.modify(mesh);
  }

  @Test
  public void test() {
    int[][] expected =
        new int[][] {
          {4, 1, 7, 10, 13},
          {19, 2, 3, 15, 32},
          {22, 8, 0, 18, 35},
          {25, 11, 6, 21, 38},
          {28, 14, 9, 24, 41},
          {16, 5, 12, 27, 44},
          {33, 20, 31, 45, 49},
          {36, 23, 34, 48, 52},
          {39, 26, 37, 51, 55},
          {42, 29, 40, 54, 58},
          {46, 30, 17, 43, 57},
          {56, 53, 50, 47, 59}
        };

    int faceIndex = 0;
    for (int j = 0; j < mesh.getFaceCount(); j++) {
      Face3D face = mesh.getFaceAt(j);
      if (face.indices.length == 5) {
        int[] expectedIndices = expected[faceIndex];
        int[] actualIndices = face.indices;
        assertArrayEquals(expectedIndices, actualIndices);
        faceIndex++;
      }
    }
  }

  @Test
  public void testNormalsPointOutwards() {
    assertTrue(MeshTestUtil.normalsPointOutwards(mesh));
  }
}
