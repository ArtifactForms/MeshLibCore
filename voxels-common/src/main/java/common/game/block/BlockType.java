package common.game.block;

import java.util.HashMap;
import java.util.Map;

public final class BlockType {

  private final short id;
  private final String name;

  // Performance-critical flags
  private boolean solid = true;
  private boolean opaque = true;
  private int lightEmission = 0;

  private BlockShape shape = BlockShape.CUBE;

  // Behavior
  private final Map<Class<?>, Object> behaviors = new HashMap<>();

  public BlockType(short id, String name) {
    this.id = id;
    this.name = name;
  }

  public <T> BlockType withBehavior(Class<T> type, T behavior) {
    behaviors.put(type, behavior);
    return this;
  }

  public <T> T getBehavior(Class<T> type) {
    return type.cast(behaviors.get(type));
  }

  public boolean hasBehavior(Class<?> type) {
    return behaviors.containsKey(type);
  }

  public short getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BlockType setSolid(boolean solid) {
    this.solid = solid;
    return this;
  }

  public boolean isSolid() {
    return solid;
  }

  public BlockType setOpaque(boolean opaque) {
    this.opaque = opaque;
    return this;
  }

  public boolean isOpaque() {
    return opaque;
  }

  public BlockType setLightEmission(int value) {
    this.lightEmission = value;
    return this;
  }

  public int getLightEmission() {
    return lightEmission;
  }

  public BlockType setShape(BlockShape shape) {
    this.shape = shape;
    return this;
  }

  public BlockShape getShape() {
    return shape;
  }
}
