package common.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Inventory} class, covering standard operations, boundary cases, and error
 * handling.
 */
public class InventoryTest {

  @Test
  void testAddItem_StandardCase() {
    Inventory inv = new Inventory(5);
    boolean result = inv.addItem((short) 1, 10);

    assertTrue(result, "Adding items should return true");
    assertNotNull(inv.getSlot(0), "Slot 0 should contain an ItemStack");
    assertEquals(10, inv.getSlot(0).getAmount(), "Item amount should match input");
  }

  @Test
  void testStacking_CombineExisting() {
    Inventory inv = new Inventory(5);

    inv.addItem((short) 1, 30);
    inv.addItem((short) 1, 20);

    // Assumes max stack size is at least 50
    assertEquals(50, inv.getSlot(0).getAmount(), "Items should stack in the first available slot");
  }

  @Test
  void testStackOverflowToNextSlot() {
    Inventory inv = new Inventory(2);

    // Assumes default max stack size of 64
    inv.addItem((short) 1, 70);

    assertEquals(64, inv.getSlot(0).getAmount(), "First slot should be capped at max stack size");
    assertEquals(6, inv.getSlot(1).getAmount(), "Remaining items should overflow to the next slot");
  }

  @Test
  void testInventoryFull() {
    Inventory inv = new Inventory(1);
    inv.addItem((short) 1, 64); // Fill the only available slot

    // Try adding a different item
    boolean added = inv.addItem((short) 2, 1);

    assertFalse(added, "Should not be able to add items when inventory is full");
  }

  @Test
  void testRemove_Partial() {
    Inventory inv = new Inventory(5);
    inv.setSlot(0, new ItemStack((short) 1, 10));

    ItemStack removed = inv.remove(0, 5);

    assertNotNull(removed);
    assertEquals(5, removed.getAmount(), "Removed stack should have requested amount");
    assertEquals(5, inv.getSlot(0).getAmount(), "Original slot should have remaining items");
  }

  @Test
  void testRemoveOne() {
    Inventory inv = new Inventory(5);
    inv.setSlot(0, new ItemStack((short) 1, 2));

    inv.removeOne(0);

    assertEquals(1, inv.getSlot(0).getAmount(), "Amount should decrease by exactly one");
  }

  @Test
  void testRemoveToEmpty() {
    Inventory inv = new Inventory(1);
    inv.addItem((short) 1, 10);

    inv.remove(0, 10);

    assertNull(inv.getSlot(0), "Slot should be null after all items are removed");
  }

  @Test
  void testHasItem() {
    Inventory inv = new Inventory(5);
    inv.setSlot(0, new ItemStack((short) 1, 20));

    assertTrue(inv.hasItem((short) 1, 10), "Should find item with sufficient amount");
    assertFalse(inv.hasItem((short) 1, 25), "Should return false if amount is insufficient");
    assertFalse(inv.hasItem((short) 2, 1), "Should return false if item ID doesn't exist");
  }

  @Test
  void testInvalidConstructorSize() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Inventory(0),
        "Inventory size must be greater than zero");
    assertThrows(IllegalArgumentException.class, () -> new Inventory(-1));
  }

  @Test
  void testOutOfBounds() {
    Inventory inv = new Inventory(5);
    assertThrows(IndexOutOfBoundsException.class, () -> inv.getSlot(10));
    assertThrows(IndexOutOfBoundsException.class, () -> inv.remove(-1, 1));
  }

  @Test
  void testAddItem_InvalidInput() {
    Inventory inv = new Inventory(5);
    assertFalse(inv.addItem((short) 1, 0), "Adding zero items should return false");
    assertFalse(inv.addItem((short) 1, -5), "Adding negative items should return false");
  }

  @Test
  void testRemove_EmptySlot() {

    Inventory inv = new Inventory(5);

    ItemStack removed = inv.remove(0, 5);

    assertNull(removed, "Removing from empty slot should return null");
  }

  @Test
  void testRemoveOne_EmptySlot() {

    Inventory inv = new Inventory(5);

    ItemStack removed = inv.removeOne(0);

    assertNull(removed, "Removing one from empty slot should return null");
  }

  @Test
  void testRemove_MoreThanAvailable() {

    Inventory inv = new Inventory(5);
    inv.setSlot(0, new ItemStack((short) 1, 5));

    ItemStack removed = inv.remove(0, 10);

    assertEquals(5, removed.getAmount());
    assertNull(inv.getSlot(0), "Slot should become empty");
  }

  @Test
  void testHasItem_EmptyInventory() {

    Inventory inv = new Inventory(5);

    assertFalse(inv.hasItem((short) 1, 1));
  }

  @Test
  void testInventorySize() {

    Inventory inv = new Inventory(7);

    assertEquals(7, inv.getSize());
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
  void testMaxStackLimitNeverExceeded() {

    Inventory inv = new Inventory(3);

    inv.addItem((short) 1, 200);

    for (int i = 0; i < inv.getSize(); i++) {

      ItemStack stack = inv.getSlot(i);

      if (stack == null) continue;

      assertTrue(stack.getAmount() <= 64, "Stack size must never exceed max stack size");
    }
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

  @Test
  void testRemoveThenAddConsistency() {

    Inventory inv = new Inventory(5);

    inv.addItem((short) 1, 50);

    ItemStack removed = inv.remove(0, 20);

    assertEquals(20, removed.getAmount());

    inv.addItem((short) 1, removed.getAmount());

    int total = 0;

    for (ItemStack stack : inv.getItems()) {
      if (stack != null) total += stack.getAmount();
    }

    assertEquals(50, total);
  }

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

      assertTrue(stack.getAmount() >= 0, "Stack amount must never be negative");
    }
  }

  @Test
  void testRandomOperations() {

    Inventory inv = new Inventory(10);

    for (int i = 0; i < 1000; i++) {
      inv.addItem((short) 1, 1);
      inv.removeOne(0);
    }

    assertTrue(true); // test should not crash
  }

  @Test
  void testAddWhenInventoryFullDoesNotModifyInventory() {

    Inventory inv = new Inventory(1);

    inv.addItem((short) 1, 64);

    ItemStack before = inv.getSlot(0);

    boolean result = inv.addItem((short) 1, 10);

    ItemStack after = inv.getSlot(0);

    assertFalse(result);
    assertEquals(before.getAmount(), after.getAmount());
  }

  @Test
  void testRemoveDoesNotDuplicateItems() {

    Inventory inv = new Inventory(5);

    inv.setSlot(0, new ItemStack((short) 1, 64));

    ItemStack removed = inv.remove(0, 32);

    int slotAmount = inv.getSlot(0).getAmount();
    int removedAmount = removed.getAmount();

    assertEquals(32, slotAmount);
    assertEquals(32, removedAmount);
  }

  @Test
  void testRemoveConservationLaw() {

    Inventory inv = new Inventory(5);

    inv.setSlot(0, new ItemStack((short) 1, 64));

    int before = inv.getSlot(0).getAmount();

    ItemStack removed = inv.remove(0, 20);

    int after = inv.getSlot(0).getAmount();

    assertEquals(before, after + removed.getAmount(), "Items must be conserved");
  }

  @Test
  void testRemoveAllAndAddBack() {

    Inventory inv = new Inventory(5);

    inv.addItem((short) 1, 64);

    ItemStack removed = inv.remove(0, 64);

    inv.addItem(removed.getItemId(), removed.getAmount());

    assertEquals(64, inv.getSlot(0).getAmount());
  }

  @Test
  void testSlotsDoNotShareReferences() {

    Inventory inv = new Inventory(5);

    ItemStack stack = new ItemStack((short) 1, 10);

    inv.setSlot(0, stack);
    inv.setSlot(1, new ItemStack((short) 1, 10));

    inv.removeOne(0);

    assertEquals(9, inv.getSlot(0).getAmount());
    assertEquals(10, inv.getSlot(1).getAmount(), "Slot 1 should not be affected by slot 0");
  }

  @Test
  void testSlotsAreDifferentObjects() {

    Inventory inv = new Inventory(5);

    inv.setSlot(0, new ItemStack((short) 1, 10));
    inv.setSlot(1, new ItemStack((short) 1, 10));

    assertNotSame(
        inv.getSlot(0),
        inv.getSlot(1),
        "Inventory slots must never share the same ItemStack instance");
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
