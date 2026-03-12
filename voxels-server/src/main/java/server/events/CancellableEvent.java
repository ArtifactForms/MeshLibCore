package server.events;

public abstract class CancellableEvent extends GameEvent implements Cancellable {

  private boolean cancelled = false;

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
