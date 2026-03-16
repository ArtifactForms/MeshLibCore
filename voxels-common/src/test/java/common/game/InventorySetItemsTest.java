package common.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InventorySetItemsTest {

  @Test
  void testSetItems_StandardCase() {
    Inventory inv = new Inventory(3);
    ItemStack item1 = new ItemStack((short) 1, 10);
    ItemStack item2 = new ItemStack((short) 5, 20);

    ItemStack[] newContent = new ItemStack[] {item1, null, item2};

    inv.setItems(newContent);

    assertEquals(3, inv.getSize());
    assertEquals(10, inv.getSlot(0).getAmount());
    assertNull(inv.getSlot(1), "Middle slot should be null as provided in the array");
    assertEquals(20, inv.getSlot(2).getAmount());
  }

  @Test
  void testSetItems_DefensiveCopying() {
    Inventory inv = new Inventory(1);
    ItemStack originalStack = new ItemStack((short) 1, 10);
    ItemStack[] array = new ItemStack[] {originalStack};

    inv.setItems(array);

    // Modifying the original stack should NOT affect the inventory
    originalStack.setAmount(99);

    assertNotEquals(
        99,
        inv.getSlot(0).getAmount(),
        "Inventory should store a copy, not the original reference");
    assertEquals(10, inv.getSlot(0).getAmount());
  }

  @Test
  void testSetItems_WithShorterArray() {
    Inventory inv = new Inventory(5);
    // Fill it first
    inv.addItem((short) 1, 10);
    inv.addItem((short) 1, 10);

    // Set items with an array of only 1 element
    ItemStack[] shorterArray = new ItemStack[] {new ItemStack((short) 99, 1)};
    inv.setItems(shorterArray);

    assertEquals(1, inv.getSlot(0).getAmount());
    assertNull(inv.getSlot(1), "Remaining slots must be cleared if the input array is shorter");
    assertNull(inv.getSlot(4), "Last slot must also be cleared");
  }

  @Test
  void testSetItems_WithLongerArray() {
    Inventory inv = new Inventory(2);
    // Provide 3 items for a 2-slot inventory
    ItemStack[] longerArray =
        new ItemStack[] {
          new ItemStack((short) 1, 1), new ItemStack((short) 2, 1), new ItemStack((short) 3, 1)
        };

    // Should not throw IndexOutOfBoundsException
    assertDoesNotThrow(() -> inv.setItems(longerArray));
    assertEquals(2, inv.getSize());
    assertEquals((short) 2, inv.getSlot(1).getItemId());
  }

  @Test
  void testSetItems_NullArgument() {
    Inventory inv = new Inventory(5);
    assertThrows(
        IllegalArgumentException.class,
        () -> inv.setItems(null),
        "Passing null to setItems should throw an exception");
  }

  @Test
  void testSetItems_IntegrityValidation() {
    Inventory inv = new Inventory(3);
    ItemStack[] items = new ItemStack[] {new ItemStack((short) 1, 10)};

    inv.setItems(items);

    // Ensure validateInventory passes after bulk setting
    assertDoesNotThrow(
        inv::validateInventory, "Inventory should be in a valid state after setItems");
  }
}
