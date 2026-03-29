package server.events.events.world;

public class WorldTimeChangedEevent extends WorldEvent {

  private final long time;

  public WorldTimeChangedEevent(long time) {
    this.time = time;
  }

  public long getTime() {
    return time;
  }
}
