package common.player;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import common.game.GameMode;
import common.player.attribute.Attribute;

class PlayerDataTest {

  private PlayerData player;

  private final UUID testUuid = UUID.randomUUID();

  private final String testName = "Gonzo";

  @BeforeEach
  void setUp() {
    player = new PlayerData(testUuid, testName);
  }

  @Test
  @DisplayName("Constructor should correctly initialize fields and inventory")
  void testInitialization() {
    float expectedMoveSpeed = PlayerData.DEFAULT_SPEED;
    float currentMoveSpeed = player.getAttributes().get(Attribute.MOVE_SPEED);
    assertAll(
        "Initial state validation",
        () -> assertEquals(testUuid, player.getUuid()),
        () -> assertEquals(testName, player.getName()),
        () -> assertEquals(PlayerData.DEFAULT_SPEED, player.getSpeed(), 0.001f),
        () -> assertEquals(expectedMoveSpeed, currentMoveSpeed, 0.001f),
        () -> assertNotNull(player.getInventory()),
        () -> assertEquals(45, player.getInventory().getSize()),
        () -> assertEquals(0f, player.getYaw(), 0.001f),
        () -> assertEquals(0f, player.getPitch(), 0.001f),
        () -> assertEquals(0f, player.getX(), 0.001f),
        () -> assertEquals(0f, player.getY(), 0.001f),
        () -> assertEquals(0f, player.getZ(), 0.001f));
  }

  @Test
  @DisplayName("Yaw setter should update yaw value")
  void testYaw() {
    float yaw = 90f;

    player.setYaw(yaw);

    assertEquals(yaw, player.getYaw(), 0.001f);
  }

  @Test
  @DisplayName("Pitch setter should update pitch value")
  void testPitch() {
    float pitch = 45f;

    player.setPitch(pitch);

    assertEquals(pitch, player.getPitch(), 0.001f);
  }

  @Test
  @DisplayName("Position methods should update the underlying Vector3f")
  void testPosition() {
    float x = 10.5f;
    float y = 64.0f;
    float z = -25.2f;

    player.setPosition(x, y, z);

    assertAll(
        "Coordinate validation",
        () -> assertEquals(x, player.getX(), 0.001f),
        () -> assertEquals(y, player.getY(), 0.001f),
        () -> assertEquals(z, player.getZ(), 0.001f),
        () -> assertEquals(x, player.getPosition().x, 0.001f),
        () -> assertEquals(y, player.getPosition().y, 0.001f),
        () -> assertEquals(z, player.getPosition().z, 0.001f));
  }

  @Test
  @DisplayName("Inventory getter should always return the same instance")
  void testInventoryReference() {
    assertSame(player.getInventory(), player.getInventory());
  }

  @Test
  @DisplayName("Position vector should not be null")
  void testPositionVectorExists() {
    assertNotNull(player.getPosition());
  }

  @Test
  @DisplayName("Abilities should be initialized by default")
  void testAbilitiesNotNullByDefault() {
    assertNotNull(player.getAbilities());
  }

  @Test
  @DisplayName("Attributes should be initialized by default")
  void testAttributesNotNullByDefault() {
    assertNotNull(player.getAttributes());
  }

  @Test
  void testGameModeIsSurvivalBýDefautlt() {
    assertEquals(GameMode.SURVIVAL, player.getGameMode());
  }

  @Test
  void testGetSetGameMode() {
    player.setGameMode(GameMode.CREATIVE);
    assertEquals(GameMode.CREATIVE, player.getGameMode());
  }
}
