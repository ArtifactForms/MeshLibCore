package common.player.ability;

import java.util.EnumSet;

public class AbilityContainer {

  private final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);

  public boolean has(Ability ability) {
    return abilities.contains(ability);
  }

  public void grant(Ability ability) {
    abilities.add(ability);
  }

  public void revoke(Ability ability) {
    abilities.remove(ability);
  }
}
