package mesh;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mesh.Axis;

/**
 * Unit tests for the {@link Axis} enum.
 *
 * <p>This test class verifies:
 *
 * <ul>
 *   <li>The enum contains exactly three values
 *   <li>The enum defines exactly the expected plane combinations (XY, XZ, YZ)
 * </ul>
 *
 * <p>These tests protect against:
 *
 * <ul>
 *   <li>Accidental addition of new enum constants
 *   <li>Renaming of existing constants
 *   <li>Structural regressions
 * </ul>
 */
class AxisTest {

  @Test
  @DisplayName("Axis enum contains exactly three values")
  void shouldContainExactlyThreeValues() {
    assertEquals(3, Axis.values().length);
  }

  @Test
  @DisplayName("Axis enum contains exactly XY, XZ and YZ")
  void shouldContainExpectedAxisPlanes() {
    Set<Axis> expected = EnumSet.of(Axis.XY, Axis.XZ, Axis.YZ);

    Set<Axis> actual = EnumSet.allOf(Axis.class);

    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Axis enum constant names are stable")
  void shouldHaveStableNames() {
    Set<String> expectedNames = Set.of("XY", "XZ", "YZ");

    Set<String> actualNames =
        EnumSet.allOf(Axis.class)
            .stream()
            .map(Enum::name)
            .collect(java.util.stream.Collectors.toSet());

    assertEquals(expectedNames, actualNames);
  }
}
