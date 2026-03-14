package client.rendering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import client.app.GameClient;
import client.settings.GameSettings;
import client.world.Chunk;
import engine.rendering.Graphics;
import engine.scene.camera.Camera;
import engine.scene.camera.Frustum;
import math.Bounds;
import math.Color;
import math.Vector3f;

public class BasicChunkRenderer implements ChunkRenderer {

  private final Vector3f tempMin = new Vector3f();
  private final Vector3f tempMax = new Vector3f();
  private final List<Chunk> visibleChunksCache = new ArrayList<>();
  private final GameClient client;

  public BasicChunkRenderer(GameClient client) {
    this.client = client;
  }

  @Override
  public void renderChunks(Graphics g, Collection<Chunk> chunks) {

    Camera camera = client.getPlayerCamera();
    Frustum frustum = new Frustum();
    frustum.update(camera.getViewProjectionMatrix());

    visibleChunksCache.clear();

    for (Chunk chunk : chunks) {
      if (isChunkVisible(frustum, chunk)) {
        visibleChunksCache.add(chunk);
      }
    }

    // --- OPAQUE PASS ---
    if (GameSettings.fog) {
      beginFog(g);
    }

    g.enableFaceCulling();

    for (Chunk chunk : visibleChunksCache) {
      chunk.renderOpaque(g);
    }

    if (GameSettings.fog) {
      endFog(g);
    }

    // --- WATER PASS ---
    beginWater(g);

    for (Chunk chunk : visibleChunksCache) {
      Vector3f pos = chunk.getWorldPosition();
      g.setUniform("u_chunkPos", pos);
      chunk.renderWater(g);
    }

    endWater(g);

    g.disableFaceCulling();
  }

  private void beginWater(Graphics g) {
    g.setShader("water.vert", "water.frag");

    g.setUniform("u_time", (float) (System.nanoTime() * 1e-9));
    g.disableDepthMask();

    // Uniforms wie gewohnt
    g.setUniform("u_fogColor", Color.getColorFromInt(180, 210, 255));
    g.setUniform("u_fogDensity", 1.5f / (8.0f * 16.0f));
  }

  private void endWater(Graphics g) {
    g.resetShader();
    g.enableDepthMask();
  }

  private void beginFog(Graphics g) {
    Color skyColor = Color.getColorFromInt(180, 210, 255);
    g.setShader("voxel.vert", "voxel.frag");
    g.setUniform("u_fogColor", skyColor);
    g.setUniform("u_lightDir", new Vector3f(0.2f, 1.0f, 0.4f));
    g.setUniform("u_lightColor", new Vector3f(0.8f, 0.8f, 0.7f));
    g.setUniform("u_ambient", 0.5f);

    float blocks = 8.0f * 16.0f;
    float density = 1.5f / blocks;
    g.setUniform("u_fogDensity", density);
  }

  private void endFog(Graphics g) {
    g.resetShader();
  }

  private boolean isChunkVisible(Frustum frustum, Chunk chunk) {
    if (!RenderSettings.FRUSTUM_CULLING) return true;

    Bounds meshBounds = chunk.getMeshBounds();

    if (meshBounds != null) {

      Vector3f pos = chunk.getWorldPosition();

      tempMin.set(meshBounds.getMin());
      tempMin.addLocal(pos);

      tempMax.set(meshBounds.getMax());
      tempMax.addLocal(pos);

      // kleines Padding gegen Frustum-Clipping
      //      float padding = 0.25f;
      float padding = 1f;

      tempMin.x -= padding;
      tempMin.z -= padding;

      tempMax.x += padding;
      tempMax.z += padding;

      // -Y up -> nach oben = kleinere Werte
      tempMin.y -= padding;
      tempMax.y += padding;

      Bounds worldBounds = new Bounds(tempMin, tempMax);

      return frustum.intersectsAABB(worldBounds);
    }

    return frustum.intersectsAABB(chunk.getChunkBounds());
  }

  //    private boolean isChunkVisible(Frustum frustum, Chunk chunk) {
  //        Bounds mBounds = chunk.getMeshBounds();
  //        Vector3f pos = chunk.getWorldPosition();
  //        if (mBounds != null) {
  //            Vector3f min = mBounds.getMin().add(pos);
  //            Vector3f max = mBounds.getMax().add(pos);
  //            return frustum.intersectsAABB(new Bounds(min, max));
  //        }
  //        return frustum.intersectsAABB(chunk.getChunkBounds());
  //    }
}
