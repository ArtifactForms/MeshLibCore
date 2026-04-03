package server.modules.moderation;

public class Ban {

  private static final long PERMANENT = -1L;

  private final long expiresAt;

  private final String reason;

  public Ban(long expiresAt, String reason) {
    this.expiresAt = expiresAt;
    this.reason = reason;
  }

  public long getExpiresAt() {
    return expiresAt;
  }

  public String getReason() {
    return reason;
  }

  public boolean isPermanent() {
    return expiresAt == PERMANENT;
  }
}
