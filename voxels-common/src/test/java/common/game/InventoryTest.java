package common.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Inventory} class.
 *
 * <p>Coverage: - constructor validation - adding items - stacking logic - removing items - boundary
 * checks - invariants (duplication / loss protection) - reference safety - stress testing
 */
public class InventoryTest {

  // ----------------------------------------------------
  // Constructor
  // ----------------------------------------------------

  @Test
  void testInvalidConstructorSize() {
    assertThrows(IllegalArgumentException.class, () -> new Inventory(0));
    assertThrows(IllegalArgumentException.class, () -> new Inventory(-1));
  }

  @Test
  void testInventorySize() {
    Inventory inv = new Inventory(7);
    assertEquals(7, inv.getSize());
  }

  // ----------------------------------------------------
  // Add Item
  // ----------------------------------------------------

  @Test
  void testAddItemStandardCase() {
    Inventory inv = new Inventory(5);

    boolean result = inv.addItem((short) 1, 10);

    assertTrue(result);
    assertNotNull(inv.getSlot(0));
    assertEquals(10, inv.getSlot(0).getAmount());
  }

  @Test
  void testAddItemInvalidInput() {
    Inventory inv = new Inventory(5);

    assertFalse(inv.addItem((short) 1, 0));
    assertFalse(inv.addItem((short) 1, -5));
  }

  @Test
  void testInventoryFull() {

    Inventory inv = new Inventory(1);

    inv.addItem((short) 1, 64);

    boolean added = inv.addItem((short) 2, 1);

    assertFalse(added);
  }

  @Test
  void testAddWhenInventoryFullDoesNotModifyInventory() {

    Inventory inv = new Inventory(1);

    inv.addItem((short) 1, 64);

    int before = inv.getSlot(0).getAmount();

    boolean result = inv.addItem((short) 1, 10);

    int after = inv.getSlot(0).getAmount();

    assertFalse(result);
    assertEquals(before, after);
  }

  // ----------------------------------------------------
  // Stacking
  // ----------------------------------------------------

  @Test
  void testStackingCombineExisting() {

    Inventory inv = new Inventory(5);

    inv.addItem((short) 1, 30);
    inv.addItem((short) 1, 20);

    assertEquals(50, inv.getSlot(0).getAmount());
  }

  @Test
  void testStackOverflowToNextSlot() {

    Inventory inv = new Inventory(2);

    inv.addItem((short) 1, 70);

    assertEquals(64, inv.getSlot(0).getAmount());
    assertEquals(6, inv.getSlot(1).getAmount());
  }

  @Test
  void testStackingMultipleSlots() {

    Inventory inv = new Inventory(3);

    inv.addItem((short) 1, 64);
    inv.addItem((short) 1, 64);
    inv.addItem((short) 1, 10);

    assertEquals(64, inv.getSlot(0).getAmount());
    assertEquals(64, inv.getSlot(1).getAmount());
    assertEquals(10, inv.getSlot(2).getAmount());
  }

  @Test
  void testStackNeverExceedsMaxSize() {

    Inventory inv = new Inventory(3);

    inv.addItem((short) 1, 200);

    for (int i = 0; i < inv.getSize(); i++) {

      ItemStack stack = inv.getSlot(i);

      if (stack == null) continue;

      assertTrue(stack.getAmount() <= 64);
    }
  }

  // ----------------------------------------------------
  // Removing Items
  // ----------------------------------------------------

  @Test
  void testRemovePartial() {

    Inventory inv = new Inventory(5);

    inv.setSlot(0, new ItemStack((short) 1, 10));

    ItemStack removed = inv.remove(0, 5);

    assertNotNull(removed);
    assertEquals(5, removed.getAmount());
    assertEquals(5, inv.getSlot(0).getAmount());
  }

  @Test
  void testRemoveMoreThanAvailable() {

    Inventory inv = new Inventory(5);

    inv.setSlot(0, new ItemStack((short) 1, 5));

    ItemStack removed = inv.remove(0, 10);

    assertEquals(5, removed.getAmount());
    assertNull(inv.getSlot(0));
  }

  @Test
  void testRemoveToEmpty() {

    Inventory inv = new Inventory(1);

    inv.addItem((short) 1, 10);

    inv.remove(0, 10);

    assertNull(inv.getSlot(0));
  }

  @Test
  void testRemoveEmptySlot() {

    Inventory inv = new Inventory(5);

    ItemStack removed = inv.remove(0, 5);

    assertNull(removed);
  }

  @Test
  void testRemoveOne() {

    Inventory inv = new Inventory(5);

    inv.setSlot(0, new ItemStack((short) 1, 2));

    inv.removeOne(0);

    assertEquals(1, inv.getSlot(0).getAmount());
  }

  @Test
  void testRemoveOneEmptySlot() {

    Inventory inv = new Inventory(5);

    ItemStack removed = inv.removeOne(0);

    assertNull(removed);
  }

  // ----------------------------------------------------
  // Query
  // ----------------------------------------------------

  @Test
  void testHasItem() {

    Inventory inv = new Inventory(5);

    inv.setSlot(0, new ItemStack((short) 1, 20));

    assertTrue(inv.hasItem((short) 1, 10));
    assertFalse(inv.hasItem((short) 1, 25));
    assertFalse(inv.hasItem((short) 2, 1));
  }

  @Test
  void testHasItemEmptyInventory() {

    Inventory inv = new Inventory(5);

    assertFalse(inv.hasItem((short) 1, 1));
  }

  // ----------------------------------------------------
  // Boundary Checks
  // ----------------------------------------------------

  @Test
  void testOutOfBounds() {

    Inventory inv = new Inventory(5);

    assertThrows(IndexOutOfBoundsException.class, () -> inv.getSlot(10));
    assertThrows(IndexOutOfBoundsException.class, () -> inv.remove(-1, 1));
  }

  // ----------------------------------------------------
  // Invariants
  // ----------------------------------------------------

  @Test
  void testItemConservationOnRemove() {

    Inventory inv = new Inventory(5);

    inv.setSlot(0, new ItemStack((short) 1, 64));

    int before = inv.getSlot(0).getAmount();

    ItemStack removed = inv.remove(0, 20);

    int after = inv.getSlot(0).getAmount();

    assertEquals(before, after + removed.getAmount());
  }

  @Test
  void testTotalItemCountIntegrity() {

    Inventory inv = new Inventory(10);

    int added = 150;

    inv.addItem((short) 1, added);

    int total = 0;

    for (ItemStack stack : inv.getItems()) {
      if (stack != null) total += stack.getAmount();
    }

    assertEquals(added, total);
  }

  // ----------------------------------------------------
  // Reference Safety
  // ----------------------------------------------------

  @Test
  void testSetSlotCreatesDefensiveCopy() {

    Inventory inv = new Inventory(5);

    ItemStack original = new ItemStack((short) 1, 10);

    inv.setSlot(0, original);

    ItemStack stored = inv.getSlot(0);

    assertNotSame(original, stored);

    original.setAmount(1);

    assertEquals(10, inv.getSlot(0).getAmount());
  }

  @Test
  void testSlotsNeverShareItemStackReference() {

    Inventory inv = new Inventory(5);

    ItemStack stack = new ItemStack((short) 1, 10);

    inv.setSlot(0, stack);
    inv.setSlot(1, stack);

    assertNotSame(inv.getSlot(0), inv.getSlot(1));
  }

  // ----------------------------------------------------
  // Stress Tests
  // ----------------------------------------------------

  @Test
  void testRandomInventoryOperations() {

    Inventory inv = new Inventory(10);

    for (int i = 0; i < 10000; i++) {

      inv.addItem((short) 1, 1);

      int slot = (int) (Math.random() * inv.getSize());

      inv.removeOne(slot);
    }

    for (ItemStack stack : inv.getItems()) {

      if (stack == null) continue;

      assertTrue(stack.getAmount() >= 0);
    }
  }

  @Test
  void testInventoryInvariantAfterOperations() {

    Inventory inv = new Inventory(10);

    for (int i = 0; i < 1000; i++) {

      inv.addItem((short) 1, 1);

      int slot = (int) (Math.random() * inv.getSize());

      inv.removeOne(slot);

      inv.validateInventory();
    }
  }
}
