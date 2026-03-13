package common.player.ability;

public class GameModePresets {

  public static void applySurvival(AbilityContainer a) {

    a.revoke(Ability.FLY);
    a.revoke(Ability.NOCLIP);

    a.grant(Ability.TAKE_DAMAGE);
    a.grant(Ability.BREAK_BLOCKS);
  }

  public static void applyCreative(AbilityContainer a) {

    a.grant(Ability.FLY);
    a.revoke(Ability.NO_GRAVITY);

    a.grant(Ability.BREAK_BLOCKS);
    a.revoke(Ability.TAKE_DAMAGE);
  }
}
