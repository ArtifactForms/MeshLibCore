package common.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InventoryValidationTest {

  @Test
  void testValidate_EmptyInventoryIsPassing() {
    Inventory inv = new Inventory(5);
    // A fresh, empty inventory should always be valid
    assertDoesNotThrow(inv::validateInventory);
  }

  @Test
  void testValidate_ThrowsOnZeroAmount() {
    Inventory inv = new Inventory(5);
    inv.addItem((short) 1, 10);

    // Force an invalid state: reach into the slot and break the amount
    ItemStack stack = inv.getSlot(0);
    stack.setAmount(0);

    IllegalStateException ex =
        assertThrows(
            IllegalStateException.class,
            inv::validateInventory,
            "Should throw exception if a stack has an amount of 0");
    assertTrue(ex.getMessage().contains("Invalid stack size"));
  }

  @Test
  void testSetAmount_ThrowsOnNegativeAmount() {
    Inventory inv = new Inventory(5);
    inv.addItem((short) 1, 10);

    ItemStack stack = inv.getSlot(0);

    assertThrows(IllegalArgumentException.class, () -> stack.setAmount(-5));
  }

  @Test
  void testValidate_ThrowsOnStackOverflow() {
    Inventory inv = new Inventory(5);
    inv.addItem((short) 1, 10);

    // Force an invalid state: amount > maxStackSize (default 64)
    ItemStack stack = inv.getSlot(0);
    stack.setAmount(128);

    IllegalStateException ex =
        assertThrows(
            IllegalStateException.class,
            inv::validateInventory,
            "Should throw exception if amount exceeds max stack size");
    assertTrue(ex.getMessage().contains("Stack overflow"));
  }

  @Test
  void testValidate_ThrowsOnDuplicateReference() throws Exception {
    Inventory inv = new Inventory(5);
    ItemStack sharedStack = new ItemStack((short) 1, 10);

    // Bypass defensive copying using Reflection to simulate a corrupted state
    java.lang.reflect.Field field = Inventory.class.getDeclaredField("slots");
    field.setAccessible(true);
    Slot[] slots = (Slot[]) field.get(inv);

    // Manually inject the same reference into two different slot wrappers
    slots[0].setStack(sharedStack);
    slots[1].setStack(sharedStack);

    assertThrows(
        IllegalStateException.class,
        inv::validateInventory,
        "Validation must fail if two slots point to the same ItemStack instance.");
  }
}
