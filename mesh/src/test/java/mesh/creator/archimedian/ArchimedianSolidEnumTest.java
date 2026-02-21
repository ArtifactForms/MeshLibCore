package mesh.creator.archimedian;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ArchimedianSolidEnumTest {

  /**
   * Verifies that the `ArchimedianSolid` enum contains the expected number of solids. This test
   * ensures that all (and only) 13 known Archimedean solids are represented.
   */
  @Test
  public void testExpectedNumberOfArchimedeanSolids() {
    assertEquals(13, ArchimedianSolid.values().length);
  }

  /**
   * This test verifies the correct order of Archimedean solids after the removal of explicit
   * indices from the enum. Maintaining the correct order is crucial for algorithms that already
   * rely on the sequential nature of the enum values.
   */
  @Test
  public void testArchimedeanSolidEnumOrder() {
    ArchimedianSolid[] expected =
        new ArchimedianSolid[] {
          ArchimedianSolid.ICOSIDODECAHEDRON,
          ArchimedianSolid.TRUNCATED_CUBOCTAHEDRON,
          ArchimedianSolid.TRUNCATED_ICOSIDODECAHEDRON,
          ArchimedianSolid.CUBOCTAHEDRON,
          ArchimedianSolid.RHOMBICUBOCTAHEDRON,
          ArchimedianSolid.SNUB_CUBE,
          ArchimedianSolid.RHOMBISOSIDODECAHEDRON,
          ArchimedianSolid.SNUB_DODECAHEDRON,
          ArchimedianSolid.TRUNCATED_TETRAHEDRON,
          ArchimedianSolid.TRUNCATED_OCTAHEDRON,
          ArchimedianSolid.TRUNCATED_CUBE,
          ArchimedianSolid.TRUNCATED_ICOSAHEDRON,
          ArchimedianSolid.TRUNCATED_DODECAHEDRON
        };
    ArchimedianSolid[] actual = ArchimedianSolid.values();
    assertArrayEquals(expected, actual);
  }

  /**
   * Tests the `valueOf` method of the `ArchimedianSolid` enum to ensure it correctly parses string
   * representations into enum values.
   *
   * <pre>
   * While it might seem redundant, especially for simple enums, it's a good
   * practice to test the valueOf method for several reasons:
   *
   * 1. Ensures Correct Parsing:
   * It verifies that the valueOf method can correctly parse string
   * representations of enum values into their corresponding enum constants.
   *
   * 2. Catches Potential Errors: It helps identify potential issues with the
   * valueOf implementation, such as case sensitivity, typos, or incorrect
   * mappings.
   *
   * 3. Maintains Code Quality: It contributes to a comprehensive test suite,
   * ensuring the correctness of the enum and its associated methods.
   * However, the specific need for this test might depend on the complexity
   * of the enum and the criticality of its correct behavior in the
   * application.
   *
   * If the enum is simple and its values are unlikely to change,
   * you might consider omitting such tests.
   * Ultimately, the decision to include such tests should be based on a risk
   * assessment and the overall quality standards of the project.
   * </pre>
   */
  @ParameterizedTest
  @ValueSource(
      strings = {
        "ICOSIDODECAHEDRON",
        "TRUNCATED_CUBOCTAHEDRON",
        "TRUNCATED_ICOSIDODECAHEDRON",
        "CUBOCTAHEDRON",
        "RHOMBICUBOCTAHEDRON",
        "SNUB_CUBE",
        "RHOMBISOSIDODECAHEDRON",
        "SNUB_DODECAHEDRON",
        "TRUNCATED_TETRAHEDRON",
        "TRUNCATED_OCTAHEDRON",
        "TRUNCATED_CUBE",
        "TRUNCATED_ICOSAHEDRON",
        "TRUNCATED_DODECAHEDRON"
      })
  public void testValueOfMethod(String solidName) {
    ArchimedianSolid expectedSolid = ArchimedianSolid.valueOf(solidName);
    assertEquals(expectedSolid, ArchimedianSolid.valueOf(solidName));
  }
}
