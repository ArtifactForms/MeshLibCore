package server.modules.moderation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModerationService {

  private static final long PERMANENT = -1L;

  private final Map<UUID, Mute> muted = new HashMap<>();

  private final Map<UUID, Ban> banned = new HashMap<>();

  public void mute(UUID playerId) {
    mute(playerId, PERMANENT, null);
  }

  public void mute(UUID playerId, String reason) {
    mute(playerId, PERMANENT, reason);
  }

  public void mute(UUID playerId, long durationMillis) {
    mute(playerId, System.currentTimeMillis() + durationMillis, null);
  }

  public void mute(UUID playerId, long expiresAt, String reason) {
    muted.put(playerId, new Mute(expiresAt, reason));
  }

  public void unmute(UUID playerId) {
    muted.remove(playerId);
  }

  public boolean isMuted(UUID playerId) {
    Mute mute = muted.get(playerId);

    if (mute == null) return false;

    if (mute.isPermanent()) return true;

    if (System.currentTimeMillis() > mute.getExpiresAt()) {
      muted.remove(playerId);
      return false;
    }

    return true;
  }

  public Map<UUID, Mute> getMutedEntries() {
    cleanupExpired();
    return muted;
  }

  private void cleanupExpired() {
    long now = System.currentTimeMillis();

    muted
        .entrySet()
        .removeIf(
            entry -> {
              Mute mute = entry.getValue();
              return !mute.isPermanent() && mute.getExpiresAt() < now;
            });
  }

  public Ban getBan(UUID playerId) {
    return banned.get(playerId);
  }

  public void ban(UUID playerId) {
    ban(playerId, PERMANENT, null);
  }

  public void ban(UUID playerId, String reason) {
    ban(playerId, PERMANENT, reason);
  }

  public void ban(UUID playerId, long durationMillis) {
    ban(playerId, System.currentTimeMillis() + durationMillis, null);
  }

  public void ban(UUID playerId, long expiresAt, String reason) {
    banned.put(playerId, new Ban(expiresAt, reason));
  }

  public void unban(UUID playerId) {
    banned.remove(playerId);
  }

  public boolean isBanned(UUID playerId) {
    Ban ban = banned.get(playerId);

    if (ban == null) return false;

    if (ban.isPermanent()) return true;

    if (System.currentTimeMillis() > ban.getExpiresAt()) {
      banned.remove(playerId);
      return false;
    }

    return true;
  }

  public Map<UUID, Ban> getBannedEntries() {
    cleanupExpiredBans();
    return banned;
  }

  private void cleanupExpiredBans() {
    long now = System.currentTimeMillis();

    banned
        .entrySet()
        .removeIf(
            entry -> {
              Ban ban = entry.getValue();
              return !ban.isPermanent() && ban.getExpiresAt() < now;
            });
  }
}
