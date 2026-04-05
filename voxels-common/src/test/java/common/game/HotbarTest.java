package common.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;

/** Unit tests for the Hotbar selection logic and listener system. */
public class HotbarTest {

  private Inventory inventory;

  private Hotbar hotbar;

  @BeforeEach
  void setUp() {
    // Hotbar logic is tied to an inventory of at least Hotbar.SIZE (9)
    inventory = new Inventory(27);
    hotbar = new Hotbar(inventory);
  }

  // --- Selection Logic Tests ---

  @Test
  void testInitialSelection() {
    assertEquals(0, hotbar.getSelectedSlot(), "New hotbar should default to slot 0");
  }

  @Test
  void testSetSelectedSlot_Valid() {
    hotbar.setSelectedSlot(5);
    assertEquals(5, hotbar.getSelectedSlot(), "Selection should update to a valid index");
  }

  @Test
  void testSetSelectedSlot_InvalidBounds() {
    // Test high bound
    hotbar.setSelectedSlot(Hotbar.SIZE);
    assertEquals(0, hotbar.getSelectedSlot(), "Selection should remain unchanged if index >= SIZE");

    // Test low bound
    hotbar.setSelectedSlot(-1);
    assertEquals(0, hotbar.getSelectedSlot(), "Selection should remain unchanged if index < 0");
  }

  // --- Navigation (Next/Previous) Tests ---

  @Test
  void testNextNavigationWithWrapping() {
    hotbar.setSelectedSlot(7);
    hotbar.next();
    assertEquals(8, hotbar.getSelectedSlot());

    hotbar.next();
    assertEquals(0, hotbar.getSelectedSlot(), "Next should wrap from 8 back to 0");
  }

  @Test
  void testPreviousNavigationWithWrapping() {
    hotbar.setSelectedSlot(1);
    hotbar.previous();
    assertEquals(0, hotbar.getSelectedSlot());

    hotbar.previous();
    assertEquals(8, hotbar.getSelectedSlot(), "Previous should wrap from 0 back to 8");
  }

  // --- Inventory Integration Tests ---

  @Test
  void testGetSelectedReturnsCorrectItem() {
    ItemStack item = new ItemStack((short) 5, 10);
    inventory.setSlot(3, item); // Put item in inventory slot 3

    hotbar.setSelectedSlot(3);

    ItemStack selected = hotbar.getSelected();
    assertNotNull(selected);
    assertEquals(
        item.getItemId(),
        selected.getItemId(),
        "getSelected must return the item from the selected index");
  }

  @Test
  void testHotbarInventoryDelegation() {
    ItemStack item = new ItemStack((short) 99, 1);
    hotbar.setSlot(0, item);

    // Verify that setting a slot in Hotbar affects the underlying Inventory
    assertNotNull(inventory.getSlot(0), "Hotbar setSlot should update the underlying inventory");
    assertEquals((short) 99, inventory.getSlot(0).getItemId());
  }

  // --- Listener / Observer Tests ---

  @Test
  void testListenerNotification() {
    AtomicInteger notifications = new AtomicInteger(0);
    HotbarListener listener = (hb) -> notifications.incrementAndGet();

    hotbar.addListener(listener);

    // 1. Change to new slot
    hotbar.setSelectedSlot(2);
    assertEquals(1, notifications.get(), "Listener should be notified on slot change");

    // 2. Change to SAME slot
    hotbar.setSelectedSlot(2);
    assertEquals(
        1,
        notifications.get(),
        "Listener should NOT be notified if the slot index didn't actually change");

    // 3. Navigation change
    hotbar.next();
    assertEquals(2, notifications.get(), "Listener should be notified by next()");
  }

  @Test
  void testRemoveListener() {
    AtomicInteger notifications = new AtomicInteger(0);
    HotbarListener listener = (hb) -> notifications.incrementAndGet();

    hotbar.addListener(listener);
    hotbar.removeHotbarListener(listener);

    hotbar.setSelectedSlot(5);
    assertEquals(0, notifications.get(), "Removed listeners should no longer receive updates");
  }

  @Test
  void testNullListenerSafety() {
    // Ensure adding a null listener doesn't crash the system
    assertDoesNotThrow(
        () -> {
          hotbar.addListener(null);
          hotbar.next();
        },
        "Hotbar should gracefully handle null listeners");
  }

  @Test
  void testGetSlotRetrievesCorrectItem() {
    // 1. Prepare data in the underlying inventory
    ItemStack item3 = new ItemStack((short) 10, 5);
    ItemStack item8 = new ItemStack((short) 20, 1);

    inventory.setSlot(3, item3);
    inventory.setSlot(8, item8);

    // 2. Verify Hotbar retrieves the exact same data
    assertNotNull(hotbar.getSlot(3));
    assertEquals(
        (short) 10,
        hotbar.getSlot(3).getItemId(),
        "Hotbar should return item from inventory index 3");
    assertEquals(5, hotbar.getSlot(3).getAmount());

    assertNotNull(hotbar.getSlot(8));
    assertEquals(
        (short) 20,
        hotbar.getSlot(8).getItemId(),
        "Hotbar should return item from inventory index 8");
  }

  @Test
  void testSetSlotUpdatesInventory() {
    ItemStack newItem = new ItemStack((short) 50, 64);

    // 1. Set via Hotbar
    hotbar.setSlot(0, newItem);

    // 2. Verify in Inventory
    ItemStack inInventory = inventory.getSlot(0);
    assertNotNull(inInventory, "Inventory slot 0 should no longer be null");
    assertEquals((short) 50, inInventory.getItemId());
    assertEquals(64, inInventory.getAmount());
  }

  @Test
  void testGetSlotEmptySlotReturnsNull() {
    // Ensure that an empty inventory slot returns null through the hotbar
    assertNull(hotbar.getSlot(5), "Empty inventory slot should result in null from hotbar");
  }

  @Test
  void testSlotAccessBounds() {
    // The Hotbar implementation delegates to inventory.getSlot(index)
    // We should verify that it throws the expected exception for out-of-bounds
    assertThrows(IndexOutOfBoundsException.class, () -> hotbar.getSlot(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> hotbar.getSlot(99));
  }
}
