package common.player;

import common.player.ability.AbilityContainer;
import common.player.attribute.AttributeContainer;

public class PlayerProperties {

  private final AbilityContainer abilities = new AbilityContainer();
  private final AttributeContainer attributes = new AttributeContainer();

  public AbilityContainer getAbilities() {
    return abilities;
  }

  public AttributeContainer getAttributes() {
    return attributes;
  }
}
