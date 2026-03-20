package client.rendering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import client.app.GameClient;
import client.settings.GameSettings;
import client.world.Chunk;
import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import engine.scene.camera.Camera;
import engine.scene.camera.Frustum;
import math.Bounds;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.topology.FlipFacesModifier;

public class BasicChunkRenderer implements ChunkRenderer {

  private final Vector3f tempMin = new Vector3f();
  private final Vector3f tempMax = new Vector3f();
  private final List<Chunk> visibleChunksCache = new ArrayList<>();
  private final GameClient client;

  private Vector3f currentLightDir = new Vector3f();
  private Vector3f currentLightColor = new Vector3f();
  private float currentAmbient;
  private float currentTimeOfDay;

  public BasicChunkRenderer(GameClient client) {
    this.client = client;
  }

  @Override
  public void renderChunks(Graphics g, Collection<Chunk> chunks) {

    // =========================
    // TIME + SUN
    // =========================
    float timeOfDay = (System.currentTimeMillis() % 60000L) / 60000f;
    float angle = timeOfDay * (float) Math.PI * 2f;

    Vector3f sunDir =
        new Vector3f((float) Math.sin(angle), (float) Math.cos(angle), 0.2f).normalize();

    Vector3f moonDir = new Vector3f(sunDir).negateLocal();

    float sunHeight = sunDir.y * 0.5f + 0.5f;
    sunHeight = Math.max(0f, Math.min(1f, sunHeight));

    float dayFactor = sunHeight;
    float nightFactor = 1.0f - dayFactor;

    // =========================
    // MIX LIGHT
    // =========================
    currentLightDir =
        new Vector3f(sunDir)
            .multLocal(dayFactor)
            .addLocal(new Vector3f(moonDir).multLocal(nightFactor))
            .normalizeLocal();

    Vector3f dayColor = new Vector3f(1.0f, 0.95f, 0.8f);
    Vector3f nightColor = new Vector3f(0.2f, 0.3f, 0.5f);

    currentLightColor =
        new Vector3f(dayColor)
            .multLocal(dayFactor)
            .addLocal(new Vector3f(nightColor).multLocal(nightFactor));

    currentAmbient = 0.08f + 0.5f * dayFactor;

    currentTimeOfDay = timeOfDay;

    // =========================
    // CAMERA + FRUSTUM
    // =========================
    Camera camera = client.getPlayerCamera();
    Frustum frustum = new Frustum();
    frustum.update(camera.getViewProjectionMatrix());

    visibleChunksCache.clear();

    // =========================
    // SKY
    // =========================
    g.disableDepthMask();
    renderSky(g);
    g.enableDepthMask();

    // =========================
    // Collect visible Chunks
    // =========================
    for (Chunk chunk : chunks) {
      if (isChunkVisible(frustum, chunk)) {
        visibleChunksCache.add(chunk);
      }
    }

    // =========================
    // OPAQUE PASS
    // =========================
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

    // =========================
    // WATER PASS
    // =========================
    beginWater(g);

    for (Chunk chunk : visibleChunksCache) {
      Vector3f pos = chunk.getWorldPosition();
      g.setUniform("u_chunkPos", pos);
      chunk.renderWater(g);
    }

    endWater(g);

    g.disableFaceCulling();

    // =========================
    // DECOR PASS / CUT OUT
    // =========================
    g.setShader("voxel.vert", "voxel.frag"); // ✅ SAME SHADER

    g.setUniform("u_cameraPos", client.getPlayerCamera().getTransform().getPosition());

    g.setUniform("u_lightDir", currentLightDir);
    g.setUniform("u_lightColor", currentLightColor);
    g.setUniform("u_ambient", currentAmbient);

    Vector3f fogColor = getSkyColor(currentTimeOfDay);
    g.setUniform("u_fogColor", fogColor);
    g.setUniform("u_fogDensity", 0.0035f);

    g.disableDepthMask();

    for (Chunk chunk : visibleChunksCache) {
      chunk.renderDecor(g);
    }

    g.enableDepthMask();
    g.resetShader();
  }

  private void beginWater(Graphics g) {
    g.setShader("water.vert", "water.frag");

    g.setUniform("u_time", (float) (System.nanoTime() * 1e-9));

    g.setUniform("u_cameraPos", client.getPlayerCamera().getTransform().getPosition());
    g.setUniform("u_ambient", currentAmbient);

    Vector3f fogColor = getSkyColor(currentTimeOfDay);
    g.setUniform("u_fogColor", fogColor);

    g.setUniform("u_fogDensity", 0.0035f);

    g.disableDepthMask();
  }

  private void endWater(Graphics g) {
    g.resetShader();
    g.enableDepthMask();
  }

  private void beginFog(Graphics g) {
    g.setShader("voxel.vert", "voxel.frag");

    g.setUniform("u_cameraPos", client.getPlayerCamera().getTransform().getPosition());

    Vector3f fogColor = getSkyColor(currentTimeOfDay);

    g.setUniform("u_fogColor", fogColor);

    // Dynamic light
    g.setUniform("u_lightDir", currentLightDir);
    g.setUniform("u_lightColor", currentLightColor);
    g.setUniform("u_ambient", currentAmbient);

    g.setUniform("u_fogDensity", 0.0035f);
  }

  private void endFog(Graphics g) {
    g.resetShader();
  }

  private boolean isChunkVisible(Frustum frustum, Chunk chunk) {
    if (!RenderSettings.frustum_Culling) return true;

    Bounds meshBounds = chunk.getMeshBounds();

    if (meshBounds != null) {

      Vector3f pos = chunk.getWorldPosition();

      tempMin.set(meshBounds.getMin());
      tempMin.addLocal(pos);

      tempMax.set(meshBounds.getMax());
      tempMax.addLocal(pos);

      float padding = 1f;

      tempMin.x -= padding;
      tempMin.z -= padding;

      tempMax.x += padding;
      tempMax.z += padding;

      tempMin.y -= padding;
      tempMax.y += padding;

      Bounds worldBounds = new Bounds(tempMin, tempMax);

      return frustum.intersectsAABB(worldBounds);
    }

    return frustum.intersectsAABB(chunk.getChunkBounds());
  }

  private void renderSky(Graphics g) {

    Camera cam = client.getPlayerCamera();
    Vector3f camPos = cam.getTransform().getPosition();

    g.setShader("sky.vert", "sky.frag");

    g.pushMatrix();
    g.translate(camPos.x, camPos.y, camPos.z);

    g.setUniform("u_timeOfDay", currentTimeOfDay);

    g.disableDepthMask();

    renderSkyCube(g);

    g.enableDepthMask();
    g.popMatrix();

    g.resetShader();
  }

  private static StaticGeometry skyGeo;

  static {
    Mesh3D cube = new CubeCreator(0.5f).create();
    new FlipFacesModifier().modify(cube);
    skyGeo = new StaticGeometry(cube);
  }

  private void renderSkyCube(Graphics g) {
    skyGeo.render(g);
  }

  private Vector3f getSkyColor(float timeOfDay) {

    float angle = timeOfDay * (float) Math.PI * 2f;

    Vector3f sunDir =
        new Vector3f((float) Math.sin(angle), (float) Math.cos(angle), 0.2f).normalize();

    float sunHeight = Math.max(0f, Math.min(1f, sunDir.y * 0.5f + 0.5f));

    // make soft
    sunHeight = sunHeight * sunHeight * (3f - 2f * sunHeight);

    Vector3f day = new Vector3f(0.6f, 0.75f, 1.0f);
    Vector3f night = new Vector3f(0.05f, 0.08f, 0.15f);

    return new Vector3f(night)
        .multLocal(1f - sunHeight)
        .addLocal(new Vector3f(day).multLocal(sunHeight));
  }
}
