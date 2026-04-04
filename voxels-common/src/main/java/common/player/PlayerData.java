package common.player;

import java.util.UUID;

import common.game.GameMode;
import common.game.Inventory;
import common.player.ability.AbilityContainer;
import common.player.attribute.Attribute;
import common.player.attribute.AttributeContainer;
import common.world.WorldMath;
import math.Vector3f;

public class PlayerData {

  public static final float DEFAULT_SPEED = 12f;

  protected final UUID uuid;

  protected final String name;

  protected float yaw;

  protected float pitch;

  protected final PlayerProperties properties;

  protected Vector3f position = new Vector3f();

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
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  public float getSpeed() {
    return properties.getAttributes().get(Attribute.MOVE_SPEED);
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(float x, float y, float z) {
    position.set(x, y, z);
  }

  public float getX() {
    return position.x;
  }

  public float getY() {
    return position.y;
  }

  public float getZ() {
    return position.z;
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
