package demos.voxels;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.debug.DebugOverlay;

public class ChunkProfile extends AbstractComponent implements RenderableComponent {

  private DebugOverlay overlay;
  private ChunkManager chunkManager;

  public ChunkProfile(ChunkManager chunkManager) {
    this.chunkManager = chunkManager;
    this.overlay = new DebugOverlay();
  }

  @Override
  public void onAttach() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDetach() {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(Graphics g) {
    overlay.render(g);
  }

  @Override
  public void onUpdate(float tpf) {
    overlay.setDebugItem("Chunk", "Active Chunks", chunkManager.getActiveChunkCount());
    overlay.setDebugItem("Chunk", "Render Distance", chunkManager.getRenderDistance());
    overlay.setDebugItem("Chunk", "Pool Size", chunkManager.getChunkPoolSize());
    overlay.setDebugItem("Chunk", "Recycled", chunkManager.getRecycledChunksCount());
    overlay.setDebugItem(
        "Chunk",
        "Player Chunk",
        chunkManager.getPlayerChunkX() + "," + chunkManager.getPlayerChunkZ());
  }
}
