package demos.jam26port.game.world;

import demos.jam26port.game.event.GameEvent;
import demos.jam26port.player.PlayerContext;
import math.Vector3f;

public interface WorldContext {

  PlayerContext getPlayer();

  /** World queries */
  boolean isBlocked(Vector3f worldPos);

  /** World events */
  void startLevel();

  void requestPlayerDeath();

  void requestPlayerDamage();

  void requestLevelExit();

  void requestLevelCompleted();

  /** Spawning */
  void spawnEffect(String effectId, Vector3f position);

  void setLobbySpawn(Vector3f lobbySpawn);

  void setLevelSpawn(Vector3f levelSpawn);

  void update(float tpf);

  void perform(GameEvent event);

  void spawnEnemyAtTile(int tileX, int tileY);
}
