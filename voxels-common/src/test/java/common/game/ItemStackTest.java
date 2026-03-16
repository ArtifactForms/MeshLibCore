package common.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ItemStackTest {

  @Test
  void testStackCreation() {
    ItemStack stack = new ItemStack((short) 1, 10);
    assertEquals(1, stack.getItemId());
    assertEquals(10, stack.getAmount());
    assertFalse(stack.isEmpty());
  }

  @Test
  void testStackCreation_NegativeAmountThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new ItemStack((short) 1, -1);
        });
  }

  @Test
  void testAdd_PartialFit() {
    ItemStack stack = new ItemStack((short) 1, 40);

    int remainder = stack.add(30, 64);

    assertEquals(64, stack.getAmount());
    assertEquals(6, remainder);
  }

  @Test
  void testAdd_PerfectFit() {
    ItemStack stack = new ItemStack((short) 1, 50);

    int remainder = stack.add(14, 64);

    assertEquals(64, stack.getAmount());
    assertEquals(0, remainder);
  }

  @Test
  void testAdd_AlreadyFull() {
    ItemStack stack = new ItemStack((short) 1, 64);

    int remainder = stack.add(10, 64);

    assertEquals(64, stack.getAmount());
    assertEquals(10, remainder);
  }

  @Test
  void testAdd_InvalidMaxStackSize() {
    ItemStack stack = new ItemStack((short) 1, 10);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          stack.add(5, 0);
        });
  }

  @Test
  void testAdd_NonPositiveAmountDoesNothing() {
    ItemStack stack = new ItemStack((short) 1, 10);

    int remainder = stack.add(0, 64);

    assertEquals(10, stack.getAmount());
    assertEquals(0, remainder);
  }

  @Test
  void testCopy_Integrity() {
    ItemStack original = new ItemStack((short) 42, 10);
    ItemStack copy = original.copy();

    assertNotSame(original, copy);
    assertEquals(original.getItemId(), copy.getItemId());
    assertEquals(original.getAmount(), copy.getAmount());

    copy.setAmount(5);

    assertNotEquals(original.getAmount(), copy.getAmount());
  }

  @Test
  void testIsEmpty() {
    ItemStack stack = new ItemStack((short) 1, 1);

    assertFalse(stack.isEmpty());

    stack.setAmount(0);

    assertTrue(stack.isEmpty());
  }

  @Test
  void testSetAmount() {
    ItemStack stack = new ItemStack((short) 1, 10);

    stack.setAmount(25);

    assertEquals(25, stack.getAmount());
  }

  @Test
  void testSetAmount_NegativeThrows() {
    ItemStack stack = new ItemStack((short) 1, 10);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          stack.setAmount(-5);
        });
  }

  @Test
  void testClear() {
    ItemStack stack = new ItemStack((short) 1, 20);

    stack.clear();

    assertEquals(0, stack.getAmount());
    assertTrue(stack.isEmpty());
  }

  @Test
  void testEqualsAndHashCode() {
    ItemStack a = new ItemStack((short) 1, 10);
    ItemStack b = new ItemStack((short) 1, 10);
    ItemStack c = new ItemStack((short) 2, 10);

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());

    assertNotEquals(a, c);
  }

  @Test
  void testToString() {
    ItemStack stack = new ItemStack((short) 5, 12);

    String text = stack.toString();

    assertTrue(text.contains("5"));
    assertTrue(text.contains("12"));
  }

  @Test
  void testEquals_Reflexive() {
    ItemStack stack = new ItemStack((short) 3, 7);

    assertEquals(stack, stack);
  }

  @Test
  void testEquals_Null() {
    ItemStack stack = new ItemStack((short) 3, 7);

    assertNotEquals(stack, null);
  }

  @Test
  void testEquals_DifferentType() {
    ItemStack stack = new ItemStack((short) 3, 7);

    assertNotEquals(stack, "notAnItemStack");
  }

  @Test
  void testAdd_NegativeAmountDoesNothing() {
    ItemStack stack = new ItemStack((short) 1, 10);

    int remainder = stack.add(-5, 64);

    assertEquals(10, stack.getAmount());
    assertEquals(0, remainder);
  }
}
