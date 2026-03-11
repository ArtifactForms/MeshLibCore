package server.world.structures;

public class StructureContext {

  public final long worldSeed;
  public final WorldAccess world;

  public StructureContext(long worldSeed, WorldAccess world) {
    this.worldSeed = worldSeed;
    this.world = world;
  }
}
