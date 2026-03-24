package client.rendering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import client.app.GameClient;
import client.settings.GameSettings;
import client.world.Chunk;
import engine.rendering.Graphics;
import engine.scene.CameraMode;
import engine.scene.camera.Camera;
import engine.scene.camera.Frustum;
import engine.scene.camera.PerspectiveCamera;
import math.Bounds;
import math.Matrix4f;
import math.Vector3f;

public class BasicChunkRenderer implements ChunkRenderer {

  private final ProceduralSkyBox sky = new ProceduralSkyBox(500);

  private final Vector3f tempMin = new Vector3f();
  private final Vector3f tempMax = new Vector3f();
  private final Vector3f tempVec = new Vector3f();

  private final List<Chunk> visibleChunksCache = new ArrayList<>();
  private final GameClient client;

  private float currentTimeOfDay;

  private static final Vector3f ZERO = new Vector3f(0, 0, 0);

  private Bounds worldBounds = new Bounds(new Vector3f(), new Vector3f());

  private final Vector3f camPos = new Vector3f();
  private final Frustum frustum = new Frustum();

  public BasicChunkRenderer(GameClient client) {
    this.client = client;
  }

  public static Matrix4f getViewProjection(Camera camera, CameraMode mode) {
    Matrix4f projection = camera.getProjectionMatrix();

    if (mode == CameraMode.CAMERA_RELATIVE) {
      return projection.multiply(getRelativeViewMatrix(camera));
    } else {
      return projection.multiply(camera.getViewMatrix());
    }
  }

  public static Matrix4f getRelativeViewMatrix(Camera camera) {
    Vector3f forward = camera.getTransform().getForward().normalize();
    Vector3f up = camera.getTransform().getUp().normalize();

    return Matrix4f.lookAt(ZERO, new Vector3f(forward), up);
  }

  private void updateLightingState() {
    currentTimeOfDay = client.getWorld().getTimeOfDay();
    sky.update(currentTimeOfDay);
  }

  private void prepareVisibleChunks(Collection<Chunk> chunks) {
    visibleChunksCache.clear();

    for (Chunk chunk : chunks) {
      if (isChunkVisible(frustum, chunk, camPos)) {
        visibleChunksCache.add(chunk);
      }
    }
  }

  @Override
  public void renderChunks(Graphics g, Collection<Chunk> chunks) {
    PerspectiveCamera camera = (PerspectiveCamera) client.getPlayerCamera();
    camera.getTransform().getPosition(camPos);

    frustum.update(getViewProjection(camera, CameraMode.CAMERA_RELATIVE));

    updateLightingState();
    prepareVisibleChunks(chunks);

    sky.render(g, currentTimeOfDay);

    // OPAQUE PASS
    g.setShader("voxel.vert", "voxel.frag");
    applyBaseShaderSettings(g);
    g.enableFaceCulling();
    for (Chunk chunk : visibleChunksCache) {
      chunk.renderOpaque(g, camPos);
    }

    // WATER PASS
    renderWater(g);

    // DECOR PASS
    g.disableFaceCulling();
    renderDecor(g);

    g.resetShader();
  }

  private void renderDecor(Graphics g) {
    g.setShader("voxel.vert", "voxel.frag");
    applyBaseShaderSettings(g);
    for (Chunk chunk : visibleChunksCache) {
      chunk.renderDecor(g, camPos);
    }
  }

  private void renderWater(Graphics g) {
    g.setShader("water.vert", "water.frag");
    applyBaseShaderSettings(g);
    g.setUniform("u_time", (float) (System.nanoTime() * 1e-9));

    for (Chunk chunk : visibleChunksCache) {
      tempVec.set(chunk.getWorldPosition()).subtractLocal(camPos);
      g.setUniform("u_chunkPos", tempVec);
      chunk.renderWater(g, camPos);
    }
  }

  private void applyBaseShaderSettings(Graphics g) {
    g.setUniform("u_cameraPos", ZERO);
    g.setUniform("u_lightDir", sky.getLightDir());
    g.setUniform("u_lightColor", sky.getLightColor());
    g.setUniform("u_ambient", sky.getAmbient());
    g.setUniform("u_fogColor", sky.getSkyColor());
    g.setUniform("u_fogDensity", GameSettings.fog ? 0.0035f : 0.0f);
  }

  private boolean isChunkVisible(Frustum frustum, Chunk chunk, Vector3f camPos) {
    if (!RenderSettings.frustum_Culling) return true;

    Bounds meshBounds = chunk.getMeshBounds();

    if (meshBounds != null) {
      Vector3f pos = chunk.getWorldPosition();

      // WORLD → CAMERA RELATIVE
      tempMin.set(meshBounds.getMin()).addLocal(pos).subtractLocal(camPos);
      tempMax.set(meshBounds.getMax()).addLocal(pos).subtractLocal(camPos);

      float padding = 1f;

      tempMin.subtractLocal(padding, padding, padding);
      tempMax.addLocal(padding, padding, padding);

      worldBounds.setMinMax(tempMin, tempMax);
      return frustum.intersectsAABB(worldBounds);
    }

    // fallback
    Bounds bounds = chunk.getChunkBounds();

    tempMin.set(bounds.getMin()).subtractLocal(camPos);
    tempMax.set(bounds.getMax()).subtractLocal(camPos);
    worldBounds.setMinMax(tempMin, tempMax);

    return frustum.intersectsAABB(worldBounds);
  }
}
