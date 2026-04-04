package common.player;

import java.util.UUID;

import common.game.GameMode;
import common.game.Inventory;
import common.player.ability.AbilityContainer;
import common.player.attribute.Attribute;
import common.player.attribute.AttributeContainer;
import common.world.Location;
import common.world.WorldMath;
import math.Vector3f;

public class PlayerData {

  public static final float DEFAULT_SPEED = 12f;

  protected final UUID uuid;

  protected final String name;

  protected final PlayerProperties properties;

  protected final Vector3f tmpPosition = new Vector3f();

  protected Location position = new Location();

  protected Inventory inventory;

  protected GameMode gameMode;

  public PlayerData(UUID uuid, String name) {
    this.uuid = uuid;
    this.name = name;
    this.inventory = new Inventory(9 * 5);
    this.properties = new PlayerProperties();
    this.gameMode = GameMode.SURVIVAL;

    getAttributes().set(Attribute.MOVE_SPEED, DEFAULT_SPEED);
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public float getYaw() {
    return position.getYaw();
  }

  public void setYaw(float yaw) {
    position.setYaw(yaw);
  }

  public float getPitch() {
    return position.getPitch();
  }

  public void setPitch(float pitch) {
    position.setPitch(pitch);
  }

  public float getSpeed() {
    return properties.getAttributes().get(Attribute.MOVE_SPEED);
  }

  public Vector3f getPosition() {
    tmpPosition.setX((float) position.getX());
    tmpPosition.setY((float) position.getY());
    tmpPosition.setZ((float) position.getZ());
    return tmpPosition;
  }

  public void setPosition(float x, float y, float z) {
    position.setPosition(x, y, z);
  }

  public float getX() {
    return (float) position.getX();
  }

  public float getY() {
    return (float) position.getY();
  }

  public float getZ() {
    return (float) position.getZ();
  }

  public AbilityContainer getAbilities() {
    return properties.getAbilities();
  }

  public AttributeContainer getAttributes() {
    return properties.getAttributes();
  }

  public int getChunkX() {
    return WorldMath.worldToChunkX(position);
  }

  public int getChunkZ() {
    return WorldMath.worldToChunkZ(position);
  }

  public GameMode getGameMode() {
    return gameMode;
  }

  public void setGameMode(GameMode gameMode) {
    this.gameMode = gameMode;
  }
}
