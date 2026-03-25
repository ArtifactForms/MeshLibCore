package common.player.ability;

import common.player.attribute.Attribute;
import common.player.attribute.AttributeContainer;

public class GameModePresets {

  public static void applySurvival(AbilityContainer a) {
    a.revoke(Ability.FLY);
    a.revoke(Ability.NOCLIP);
    a.revoke(Ability.PICK_BLOCKS);
    a.revoke(Ability.INSTANT_BREAK);

    a.grant(Ability.TAKE_DAMAGE);
    a.grant(Ability.BREAK_BLOCKS);
  }

  public static void applyCreative(AbilityContainer a) {
    a.grant(Ability.FLY);
    a.grant(Ability.PICK_BLOCKS);
    a.grant(Ability.BREAK_BLOCKS);
    a.grant(Ability.INSTANT_BREAK);
    
    a.revoke(Ability.NO_GRAVITY);
    a.revoke(Ability.TAKE_DAMAGE);
  }

  public static void applyCreative(AttributeContainer a) {
    a.set(Attribute.REACH_DISTANCE, 6);
    a.set(Attribute.HEALTH, 100);
    a.set(Attribute.STAMINA, 100);
    //    a.set(Attribute.EYE_HEIGHT, 1.8f);
  }

  public static void applySurvival(AttributeContainer a) {
    a.set(Attribute.REACH_DISTANCE, 6);
    a.set(Attribute.HEALTH, 100);
    a.set(Attribute.STAMINA, 100);
    //    a.set(Attribute.EYE_HEIGHT, 1.8f);
  }
}
