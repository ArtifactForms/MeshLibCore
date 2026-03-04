package voxels.editor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.physics.ray.Raycaster;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.MouseInput;
import engine.scene.Scene;
import engine.scene.camera.Camera;
import math.Color;
import math.Ray3f;
import math.Vector3f;
import voxels.world.Blocks;
import voxels.world.Chunk;
import voxels.world.VoxelWorld;
import voxels.world.VoxelWorldIO;

public class VoxelEditTool extends AbstractComponent implements RenderableComponent {

  private static final float MAX_RAY_DISTANCE = 400f;
  private static final float REMOVE_REPEAT_INTERVAL = 0.04f;
  private static final Path SAVE_ROOT =
      Paths.get("workspace", "voxel-saves", "default");

  private final Input input;
  private final VoxelWorld world;
  private final WorldStreamer streamer;

  private short selectedBlock = Blocks.STONE;
  private int brushRadius = 2;

  private boolean lastLeftPressed;
  private float removeHoldTimer = 0f;

  private RaycastResult lastHit = RaycastResult.miss();

  public VoxelEditTool(Input input, VoxelWorld world, WorldStreamer streamer) {
    this.input = input;
    this.world = world;
    this.streamer = streamer;
  }

  // =========================================================
  // Update
  // =========================================================

  @Override
  public void onUpdate(float tpf) {

    handleBlockSelection();
    handleBrushSize();
    handlePersistenceShortcuts();

    Scene scene = getOwner().getScene();
    if (scene == null) return;

    Camera camera = scene.getActiveCamera();
    if (camera == null) return;

    lastHit = cast(camera);

    boolean leftPressed = input.isMousePressed(MouseInput.LEFT);
    boolean rightPressed = input.isMousePressed(MouseInput.RIGHT);

    if (leftPressed && !lastLeftPressed) {
      if (lastHit.hit) {
        applySphere(lastHit.placeX, lastHit.placeY, lastHit.placeZ, selectedBlock);
      }
    }

    if (rightPressed) {
      removeHoldTimer += tpf;
      if (removeHoldTimer >= REMOVE_REPEAT_INTERVAL) {
        if (lastHit.hit) {
          applySphere(lastHit.blockX, lastHit.blockY, lastHit.blockZ, Blocks.AIR);
        }
        removeHoldTimer = 0f;
      }
    } else {
      removeHoldTimer = REMOVE_REPEAT_INTERVAL;
    }

    lastLeftPressed = leftPressed;
  }

  // =========================================================
  // Robust DDA Raycast (zoom-stabil)
  // =========================================================

  private RaycastResult cast(Camera camera) {

    Ray3f ray = Raycaster.screenPointToRay(
        camera,
        input.getMouseX(),
        input.getMouseY(),
        input.getScreenWidth(),
        input.getScreenHeight());

    Vector3f origin = ray.getOrigin();
    Vector3f dir = ray.getDirection().normalize();

    int x = (int) Math.floor(origin.x);
    int y = (int) Math.floor(origin.y);
    int z = (int) Math.floor(origin.z);

    int stepX = dir.x >= 0 ? 1 : -1;
    int stepY = dir.y >= 0 ? 1 : -1;
    int stepZ = dir.z >= 0 ? 1 : -1;

    float tDeltaX = dir.x != 0 ? Math.abs(1f / dir.x) : Float.MAX_VALUE;
    float tDeltaY = dir.y != 0 ? Math.abs(1f / dir.y) : Float.MAX_VALUE;
    float tDeltaZ = dir.z != 0 ? Math.abs(1f / dir.z) : Float.MAX_VALUE;

    float nextX = x + (stepX > 0 ? 1 : 0);
    float nextY = y + (stepY > 0 ? 1 : 0);
    float nextZ = z + (stepZ > 0 ? 1 : 0);

    float tMaxX = dir.x != 0 ? (nextX - origin.x) / dir.x : Float.MAX_VALUE;
    float tMaxY = dir.y != 0 ? (nextY - origin.y) / dir.y : Float.MAX_VALUE;
    float tMaxZ = dir.z != 0 ? (nextZ - origin.z) / dir.z : Float.MAX_VALUE;

    float t = 0f;

    int prevX = x;
    int prevY = y;
    int prevZ = z;

    while (t <= MAX_RAY_DISTANCE) {

      if (world.getBlock(x, y, z) != Blocks.AIR) {
        return new RaycastResult(true, x, y, z, prevX, prevY, prevZ);
      }

      prevX = x;
      prevY = y;
      prevZ = z;

      if (tMaxX < tMaxY) {
        if (tMaxX < tMaxZ) {
          x += stepX;
          t = tMaxX;
          tMaxX += tDeltaX;
        } else {
          z += stepZ;
          t = tMaxZ;
          tMaxZ += tDeltaZ;
        }
      } else {
        if (tMaxY < tMaxZ) {
          y += stepY;
          t = tMaxY;
          tMaxY += tDeltaY;
        } else {
          z += stepZ;
          t = tMaxZ;
          tMaxZ += tDeltaZ;
        }
      }
    }

    return RaycastResult.miss();
  }

  // =========================================================
  // Sphere Editing
  // =========================================================

  private void applySphere(int cx, int cy, int cz, short blockId) {

    int r2 = brushRadius * brushRadius;
    Set<Long> touched = new HashSet<>();

    for (int dx = -brushRadius; dx <= brushRadius; dx++) {
      for (int dy = -brushRadius; dy <= brushRadius; dy++) {
        for (int dz = -brushRadius; dz <= brushRadius; dz++) {

          if (dx*dx + dy*dy + dz*dz > r2) continue;

          int wx = cx + dx;
          int wy = cy + dy;
          int wz = cz + dz;

          boolean changed =
              (blockId == Blocks.AIR)
                  ? world.removeBlockWorld(wx, wy, wz)
                  : world.setBlockWorld(wx, wy, wz, blockId);

          if (!changed) continue;

          int chunkX = Math.floorDiv(wx, Chunk.SIZE_X);
          int chunkZ = Math.floorDiv(wz, Chunk.SIZE_Z);
          touched.add(pack(chunkX, chunkZ));
        }
      }
    }

    for (long key : touched) {
      streamer.onChunkEdited((int)(key >> 32), (int) key);
    }
  }

  // =========================================================

  private void handleBlockSelection() {
    if (input.wasKeyPressed(Key.NUM_1)) selectedBlock = Blocks.STONE;
    if (input.wasKeyPressed(Key.NUM_2)) selectedBlock = Blocks.DIRT;
    if (input.wasKeyPressed(Key.NUM_3)) selectedBlock = Blocks.GRASS;
    if (input.wasKeyPressed(Key.NUM_4)) selectedBlock = Blocks.LOG;
    if (input.wasKeyPressed(Key.NUM_5)) selectedBlock = Blocks.LEAF;
  }

  private void handleBrushSize() {
    if (input.wasKeyPressed(Key.NUM_6)) brushRadius = 1;
    if (input.wasKeyPressed(Key.NUM_7)) brushRadius = 2;
    if (input.wasKeyPressed(Key.NUM_8)) brushRadius = 3;
    if (input.wasKeyPressed(Key.NUM_9)) brushRadius = 4;
  }

  private void handlePersistenceShortcuts() {

    if (input.wasKeyPressed(Key.F5)) {
      try {
        VoxelWorldIO.saveLoadedChunks(world, SAVE_ROOT);
      } catch (IOException e) {
        System.err.println("Save failed: " + e.getMessage());
      }
    }

    if (input.wasKeyPressed(Key.F9)) {
      try {
        int loaded = VoxelWorldIO.loadAllChunks(world, SAVE_ROOT);
        if (loaded > 0) {
          for (Chunk chunk : world.getChunks()) {
            streamer.onChunkEdited(chunk.getChunkX(), chunk.getChunkZ());
          }
        }
      } catch (IOException e) {
        System.err.println("Load failed: " + e.getMessage());
      }
    }
  }

  @Override
  public void render(Graphics g) {

    g.setColor(Color.WHITE);
    g.text("Block[1..5] Radius[6..9] Save[F5] Load[F9]", 12, 24);

    if (lastHit.hit) {
      g.text(
          "Hit: " + lastHit.blockX + "," + lastHit.blockY + "," + lastHit.blockZ,
          12, 44);
    }
  }

  private long pack(int x, int z) {
    return (((long)x) << 32) | (z & 0xffffffffL);
  }

  // =========================================================
  // Raycast Result
  // =========================================================

  private static class RaycastResult {

    final boolean hit;
    final int blockX, blockY, blockZ;
    final int placeX, placeY, placeZ;

    private RaycastResult(boolean hit,
                          int blockX, int blockY, int blockZ,
                          int placeX, int placeY, int placeZ) {

      this.hit = hit;
      this.blockX = blockX;
      this.blockY = blockY;
      this.blockZ = blockZ;
      this.placeX = placeX;
      this.placeY = placeY;
      this.placeZ = placeZ;
    }

    static RaycastResult miss() {
      return new RaycastResult(false,0,0,0,0,0,0);
    }
  }
}