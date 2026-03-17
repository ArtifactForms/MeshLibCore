package common.player;

import java.util.UUID;

import common.game.Inventory;
import common.player.ability.AbilityContainer;
import common.player.attribute.AttributeContainer;
import math.Vector3f;

public class PlayerData {

  public static final float DEFAULT_SPEED = 12f;

  protected final UUID uuid;
  protected final String name;

  protected float yaw;
  protected float pitch;

  protected float speed = DEFAULT_SPEED;
  protected Vector3f position = new Vector3f();
  protected Inventory inventory;
  private final PlayerProperties properties;

  public PlayerData(UUID uuid, String name) {
    this.uuid = uuid;
    this.name = name;
    this.inventory = new Inventory(9 * 5);
    this.properties = new PlayerProperties();
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
    return speed;
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
}
