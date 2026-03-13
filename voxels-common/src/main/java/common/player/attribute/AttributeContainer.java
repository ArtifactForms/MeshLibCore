package common.player.attribute;

import java.util.EnumMap;

public class AttributeContainer {

  private final EnumMap<Attribute, Float> values = new EnumMap<>(Attribute.class);

  public float get(Attribute attr) {
    return values.getOrDefault(attr, 0f);
  }

  public void set(Attribute attr, float value) {
    values.put(attr, value);
  }
}
