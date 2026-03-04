package voxels.editor;

import engine.components.AbstractComponent;
import engine.physics.ray.Raycaster;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.MouseInput;
import engine.scene.Scene;
import engine.scene.camera.Camera;
import math.Ray3f;
import math.Vector3f;
import voxels.world.Blocks;
import voxels.world.Chunk;
import voxels.world.VoxelWorld;
import voxels.world.VoxelWorldIO;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VoxelEditTool extends AbstractComponent {

  private static final float MAX_RAY_DISTANCE = 200f;
  private static final float STEP_SIZE = 0.25f;
  private static final Path SAVE_ROOT = Paths.get("workspace", "voxel-saves", "default");

  private final Input input;
  private final VoxelWorld world;
  private final WorldStreamer streamer;

  private short selectedBlock = Blocks.STONE;
  private boolean lastLeftPressed;
  private boolean lastRightPressed;

  public VoxelEditTool(Input input, VoxelWorld world, WorldStreamer streamer) {
    this.input = input;
    this.world = world;
    this.streamer = streamer;
  }

  @Override
  public void onUpdate(float tpf) {
    handleBlockSelection();
    handlePersistenceShortcuts();

    Scene scene = getOwner().getScene();
    if (scene == null) return;

    Camera camera = scene.getActiveCamera();
    if (camera == null) return;

    boolean leftPressed = input.isMousePressed(MouseInput.LEFT);
    boolean rightPressed = input.isMousePressed(MouseInput.RIGHT);

    if (leftPressed && !lastLeftPressed) {
      placeBlock(camera);
    }

    if (rightPressed && !lastRightPressed) {
      removeBlock(camera);
    }

    lastLeftPressed = leftPressed;
    lastRightPressed = rightPressed;
  }

  private void handleBlockSelection() {
    if (input.wasKeyPressed(Key.NUM_1)) selectedBlock = Blocks.STONE;
    if (input.wasKeyPressed(Key.NUM_2)) selectedBlock = Blocks.DIRT;
    if (input.wasKeyPressed(Key.NUM_3)) selectedBlock = Blocks.GRASS;
    if (input.wasKeyPressed(Key.NUM_4)) selectedBlock = Blocks.LOG;
    if (input.wasKeyPressed(Key.NUM_5)) selectedBlock = Blocks.LEAF;
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

  private void placeBlock(Camera camera) {
    RaycastResult hit = cast(camera);
    if (!hit.hit) return;

    if (world.setBlockWorld(hit.placeX, hit.placeY, hit.placeZ, selectedBlock)) {
      streamer.onBlockEdited(hit.placeX, hit.placeY, hit.placeZ);
    }
  }

  private void removeBlock(Camera camera) {
    RaycastResult hit = cast(camera);
    if (!hit.hit) return;

    if (world.removeBlockWorld(hit.blockX, hit.blockY, hit.blockZ)) {
      streamer.onBlockEdited(hit.blockX, hit.blockY, hit.blockZ);
    }
  }

  private RaycastResult cast(Camera camera) {
    Ray3f ray = Raycaster.crossHairRay(camera);

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
