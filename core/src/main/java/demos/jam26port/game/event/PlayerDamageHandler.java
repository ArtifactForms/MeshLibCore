package demos.jam26port.game.event;

import demos.jam26port.assets.AssetRefs;
import demos.jam26port.game.ui.GameUi;
import demos.jam26port.player.PlayerContext;
import engine.scene.audio.SoundManager;

public class PlayerDamageHandler implements GameEventHandler {

  private final PlayerContext player;
  private final GameUi ui;

  public PlayerDamageHandler(PlayerContext player, GameUi ui) {
    this.player = player;
    this.ui = ui;
  }

  @Override
  public void handle(GameEvent event) {
    if (event instanceof PlayerDamageEvent e) {
      player.getHealth().applyDamage(e.getAmount());
      ui.displayHitFlash(1f);
      SoundManager.playEffect(AssetRefs.SOUND_PLAYER_HIT_KEY);
    }
  }
}