package common.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

public class WorldTimeTest {

  // ------------------------------------
  // Keyword Mapping (Core)
  // ------------------------------------

  @Test
  void keywordMappingConsistency() {
    assertEquals(WorldTime.DAY, WorldTime.getTicksFromKeyword("day"));
    assertEquals(WorldTime.NOON, WorldTime.getTicksFromKeyword("noon"));
    assertEquals(WorldTime.SUNSET, WorldTime.getTicksFromKeyword("sunset"));
    assertEquals(WorldTime.NIGHT, WorldTime.getTicksFromKeyword("night"));
    assertEquals(WorldTime.MIDNIGHT, WorldTime.getTicksFromKeyword("midnight"));
    assertEquals(WorldTime.SUNRISE, WorldTime.getTicksFromKeyword("sunrise"));
  }

  // ------------------------------------
  // Parameterized Keyword Tests
  // ------------------------------------

  @ParameterizedTest
  @ValueSource(strings = {"day", "DAY", "DaY", " day "})
  void dayKeywordVariants(String input) {
    assertEquals(WorldTime.DAY, WorldTime.getTicksFromKeyword(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"noon", "NOON", "NoOn", " noon "})
  void noonKeywordVariants(String input) {
    assertEquals(WorldTime.NOON, WorldTime.getTicksFromKeyword(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"sunset", "SUNSET", "SuNSeT", " sunset "})
  void sunsetKeywordVariants(String input) {
    assertEquals(WorldTime.SUNSET, WorldTime.getTicksFromKeyword(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"night", "NIGHT", "NiGHt", " night "})
  void nightKeywordVariants(String input) {
    assertEquals(WorldTime.NIGHT, WorldTime.getTicksFromKeyword(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"midnight", "MIDNIGHT", "mIdNiGHt", " midnight "})
  void midnightKeywordVariants(String input) {
    assertEquals(WorldTime.MIDNIGHT, WorldTime.getTicksFromKeyword(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"sunrise", "SUNRISE", "SUNRise", " sunrise "})
  void sunriseKeywordVariants(String input) {
    assertEquals(WorldTime.SUNRISE, WorldTime.getTicksFromKeyword(input));
  }

  // ------------------------------------
  // Exceptions / Edge cases
  // ------------------------------------

  @Test
  void nullKeywordThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> WorldTime.getTicksFromKeyword(null));
  }

  @Test
  void emptyKeywordThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> WorldTime.getTicksFromKeyword(""));
  }

  @Test
  void whitespaceOnlyKeywordThrows() {
    assertThrows(IllegalArgumentException.class, () -> WorldTime.getTicksFromKeyword("   "));
  }

  @Test
  void invalidKeywordThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> WorldTime.getTicksFromKeyword("ee3"));
  }

  // ------------------------------------
  // Unique constants
  // ------------------------------------

  @Test
  void allConstantsAreUnique() {
    assertEquals(
        true,
        WorldTime.DAY != WorldTime.NOON
            && WorldTime.NOON != WorldTime.SUNSET
            && WorldTime.SUNSET != WorldTime.NIGHT
            && WorldTime.NIGHT != WorldTime.MIDNIGHT
            && WorldTime.MIDNIGHT != WorldTime.SUNRISE);
  }

  // ------------------------------------
  // Constants
  // ------------------------------------

  @Test
  void dayLengthConstant() {
    assertEquals(24000, WorldTime.DAY_LENGTH);
  }

  @Test
  void dayConstant() {
    assertEquals(1000, WorldTime.DAY);
  }

  @Test
  void noonConstant() {
    assertEquals(6000, WorldTime.NOON);
  }

  @Test
  void sunsetConstant() {
    assertEquals(12000, WorldTime.SUNSET);
  }

  @Test
  void nightConstant() {
    assertEquals(13000, WorldTime.NIGHT);
  }

  @Test
  void midNightConstant() {
    assertEquals(18000, WorldTime.MIDNIGHT);
  }

  @Test
  void sunriseConstant() {
    assertEquals(23000, WorldTime.SUNRISE);
  }

  // ------------------------------------
  // Constants time order
  // ------------------------------------

  @Test
  void timeOrderIsCorrect() {
    assertEquals(true, WorldTime.DAY < WorldTime.NOON);
    assertEquals(true, WorldTime.NOON < WorldTime.SUNSET);
    assertEquals(true, WorldTime.SUNSET < WorldTime.NIGHT);
    assertEquals(true, WorldTime.NIGHT < WorldTime.MIDNIGHT);
    assertEquals(true, WorldTime.MIDNIGHT < WorldTime.SUNRISE);
    assertEquals(true, WorldTime.SUNRISE < WorldTime.DAY_LENGTH);
  }
}
