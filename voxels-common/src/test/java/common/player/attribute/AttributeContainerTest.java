package common.player.attribute;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttributeContainerTest {

  private AttributeContainer container;

  @BeforeEach
  void setUp() {
    container = new AttributeContainer();
  }

  @Test
  @DisplayName("Should return 0.0 for uninitialized attributes")
  void testDefaults() {
    // Checking a few specific constants from your Enum
    assertEquals(0f, container.get(Attribute.MOVE_SPEED));
    assertEquals(0f, container.get(Attribute.EYE_HEIGHT));
  }

  @Test
  @DisplayName("Should store and retrieve specific player attributes")
  void testSetAndGet() {
    container.set(Attribute.JUMP_STRENGTH, 0.42f);
    container.set(Attribute.REACH_DISTANCE, 4.5f);

    assertEquals(0.42f, container.get(Attribute.JUMP_STRENGTH));
    assertEquals(4.5f, container.get(Attribute.REACH_DISTANCE));
  }

  @Test
  @DisplayName("Should update attribute value when set multiple times")
  void testUpdateValue() {
    container.set(Attribute.BREAK_SPEED, 1.0f);
    container.set(Attribute.BREAK_SPEED, 1.5f);

    assertEquals(1.5f, container.get(Attribute.BREAK_SPEED));
  }

  @Test
  @DisplayName("Should maintain separate values for all attributes")
  void testAttributeIsolation() {
    for (Attribute attr : Attribute.values()) {
      container.set(attr, (float) attr.ordinal());
    }

    for (Attribute attr : Attribute.values()) {
      assertEquals(
          (float) attr.ordinal(),
          container.get(attr),
          "Value for " + attr + " should match its ordinal index");
    }
  }
}
