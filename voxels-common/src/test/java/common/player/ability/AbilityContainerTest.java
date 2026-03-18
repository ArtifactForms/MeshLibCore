package common.player.ability;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbilityContainerTest {

  private AbilityContainer container;

  @BeforeEach
  void setUp() {
    container = new AbilityContainer();
  }

  @Test
  void testInitialStateIsEmpty() {
    // Verify that a new container has no abilities by default
    for (Ability ability : Ability.values()) {
      assertFalse(container.has(ability), "New container should not have " + ability);
    }
  }

  @Test
  void testGrantAbility() {
    container.grant(Ability.FLY);

    assertTrue(container.has(Ability.FLY), "Container should have FLY after grant");
    assertFalse(container.has(Ability.NOCLIP), "Container should not have unrelated abilities");
  }

  @Test
  void testRevokeAbility() {
    container.grant(Ability.NOCLIP);
    assertTrue(container.has(Ability.NOCLIP));

    container.revoke(Ability.NOCLIP);
    assertFalse(
        container.has(Ability.NOCLIP), "Container should no longer have NOCLIP after revoke");
  }

  @Test
  void testGrantMultipleAbilities() {
    container.grant(Ability.BREAK_BLOCKS);
    container.grant(Ability.INSTANT_BREAK);

    assertTrue(container.has(Ability.BREAK_BLOCKS));
    assertTrue(container.has(Ability.INSTANT_BREAK));
    assertFalse(container.has(Ability.FLY));
  }

  @Test
  void testClearAbilities() {
    container.grant(Ability.FLY);
    container.grant(Ability.TAKE_DAMAGE);

    container.clear();

    assertFalse(container.has(Ability.FLY));
    assertFalse(container.has(Ability.TAKE_DAMAGE));
    assertTrue(Ability.values().length > 0); // Sanity check for the enum itself
  }

  @Test
  void testGrantDuplicateAbility() {
    // Granting the same ability twice should not cause issues or change state
    container.grant(Ability.PICK_BLOCKS);
    container.grant(Ability.PICK_BLOCKS);

    assertTrue(container.has(Ability.PICK_BLOCKS));

    container.revoke(Ability.PICK_BLOCKS);
    assertFalse(container.has(Ability.PICK_BLOCKS));
  }

  @Test
  void testRevokeNonExistentAbility() {
    // Revoking an ability that was never granted should not throw an exception
    container.revoke(Ability.NO_GRAVITY);
    assertFalse(container.has(Ability.NO_GRAVITY));
  }
}
