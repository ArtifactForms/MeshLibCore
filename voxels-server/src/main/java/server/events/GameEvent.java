package server.events;

public abstract class GameEvent {

  private boolean cancelled = false;

  public boolean isCancelled() {
    return cancelled;
  }

  public void cancel() {
    this.cancelled = true;
  }
}
