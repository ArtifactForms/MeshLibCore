package voxels.editor;

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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class VoxelEditTool extends AbstractComponent implements RenderableComponent {

  private static final float MAX_RAY_DISTANCE = 200f;
  private static final float STEP_SIZE = 0.25f;
  private static final float REMOVE_REPEAT_INTERVAL = 0.04f; // seconds
  private static final Path SAVE_ROOT = Paths.get("workspace", "voxel-saves", "default");

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
      placeSphere(lastHit);
    }

    if (rightPressed) {
      removeHoldTimer += tpf;
      if (removeHoldTimer >= REMOVE_REPEAT_INTERVAL) {
        removeSphere(lastHit);
        removeHoldTimer = 0f;
      }
    } else {
      removeHoldTimer = REMOVE_REPEAT_INTERVAL;
    }

    lastLeftPressed = leftPressed;
  }

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
        System.err.println("Failed to save voxel chunks: " + e.getMessage());
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
        System.err.println("Failed to load voxel chunks: " + e.getMessage());
      }
    }
  }

  private void placeSphere(RaycastResult hit) {
    if (!hit.hit) return;
    applySphere(hit.placeX, hit.placeY, hit.placeZ, selectedBlock);
  }

  private void removeSphere(RaycastResult hit) {
    if (!hit.hit) return;
    applySphere(hit.blockX, hit.blockY, hit.blockZ, Blocks.AIR);
  }

  private void applySphere(int cx, int cy, int cz, short blockId) {
    int r2 = brushRadius * brushRadius;

    Set<Long> touchedChunks = new HashSet<>();

    for (int dx = -brushRadius; dx <= brushRadius; dx++) {
      for (int dy = -brushRadius; dy <= brushRadius; dy++) {
        for (int dz = -brushRadius; dz <= brushRadius; dz++) {

          int dist2 = dx * dx + dy * dy + dz * dz;
          if (dist2 > r2) continue;

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
          touchedChunks.add(pack(chunkX, chunkZ));
        }
      }
    }

    for (long key : touchedChunks) {
      streamer.onChunkEdited((int) (key >> 32), (int) key);
    }
  }

  private RaycastResult cast(Camera camera) {
    Ray3f ray =
        Raycaster.screenPointToRay(
            camera,
            input.getMouseX(),
            input.getMouseY(),
            input.getScreenWidth(),
            input.getScreenHeight());

    Vector3f origin = ray.getOrigin();
    Vector3f direction = ray.getDirection().normalize();

    int prevX = Integer.MIN_VALUE;
    int prevY = Integer.MIN_VALUE;
    int prevZ = Integer.MIN_VALUE;

    for (float distance = 0f; distance <= MAX_RAY_DISTANCE; distance += STEP_SIZE) {
      float px = origin.x + direction.x * distance;
      float py = origin.y + direction.y * distance;
      float pz = origin.z + direction.z * distance;

      int bx = (int) Math.floor(px);
      int by = (int) Math.floor(py);
      int bz = (int) Math.floor(pz);

      if (bx == prevX && by == prevY && bz == prevZ) {
        continue;
      }

      short block = world.getBlock(bx, by, bz);
      if (block != Blocks.AIR) {
        if (prevX == Integer.MIN_VALUE) {
          return new RaycastResult(true, bx, by, bz, bx, by, bz);
        }
        return new RaycastResult(true, bx, by, bz, prevX, prevY, prevZ);
      }

      prevX = bx;
      prevY = by;
      prevZ = bz;
    }

    return RaycastResult.miss();
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.WHITE);
    g.text(
        "Block[1..5]: "
            + selectedBlock
            + " | Radius[6..9]: "
            + brushRadius
            + " | Save[F5] Load[F9]",
        12,
        24);

    if (lastHit.hit) {
      g.text(
          "Hit: "
              + lastHit.blockX
              + ","
              + lastHit.blockY
              + ","
              + lastHit.blockZ
              + " | Place: "
              + lastHit.placeX
              + ","
              + lastHit.placeY
              + ","
              + lastHit.placeZ
              + " | RMB=erase sphere",
          12,
          44);
    } else {
      g.text("Hit: none | RMB=erase sphere", 12, 44);
    }
  }

  private long pack(int x, int z) {
    return (((long) x) << 32) | (z & 0xffffffffL);
  }

  private static class RaycastResult {
    private final boolean hit;
    private final int blockX;
    private final int blockY;
    private final int blockZ;
    private final int placeX;
    private final int placeY;
    private final int placeZ;

    private RaycastResult(
        boolean hit, int blockX, int blockY, int blockZ, int placeX, int placeY, int placeZ) {
      this.hit = hit;
      this.blockX = blockX;
      this.blockY = blockY;
      this.blockZ = blockZ;
      this.placeX = placeX;
      this.placeY = placeY;
      this.placeZ = placeZ;
    }

    private static RaycastResult miss() {
      return new RaycastResult(false, 0, 0, 0, 0, 0, 0);
    }
  }
}
