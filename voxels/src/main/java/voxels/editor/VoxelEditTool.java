package voxels.editor;

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

public class VoxelEditTool extends AbstractComponent implements RenderableComponent {

  private static final float MAX_DISTANCE = 10000;

  private final Input input;

  private final VoxelWorld world;

  private final WorldStreamer streamer;

  private short selectedBlock = Blocks.STONE;

  private int brushRadius = 20;

  private boolean lastLeftPressed;

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

    Scene scene = getOwner().getScene();
    if (scene == null) return;

    Camera camera = scene.getActiveCamera();
    if (camera == null) return;

    lastHit = castVoxel(camera);

    boolean leftPressed = input.isMousePressed(MouseInput.LEFT);
    boolean rightPressed = input.isMousePressed(MouseInput.RIGHT);

    if (leftPressed && !lastLeftPressed && lastHit.hit) {
      applySphere(lastHit.placeX, lastHit.placeY, lastHit.placeZ, selectedBlock);
    }

    if (rightPressed && lastHit.hit) {
      applySphere(lastHit.blockX, lastHit.blockY, lastHit.blockZ, Blocks.AIR);
    }

    lastLeftPressed = leftPressed;
  }

  // -------------------------------------------------------
  // Raycast
  // -------------------------------------------------------

  private RaycastResult castVoxel(Camera camera) {

    Ray3f ray =
        Raycaster.screenPointToRay(
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

    float nextVoxelBoundaryX = x + (stepX > 0 ? 1 : 0);
    float nextVoxelBoundaryY = y + (stepY > 0 ? 1 : 0);
    float nextVoxelBoundaryZ = z + (stepZ > 0 ? 1 : 0);

    float tMaxX = dir.x != 0 ? (nextVoxelBoundaryX - origin.x) / dir.x : Float.MAX_VALUE;
    float tMaxY = dir.y != 0 ? (nextVoxelBoundaryY - origin.y) / dir.y : Float.MAX_VALUE;
    float tMaxZ = dir.z != 0 ? (nextVoxelBoundaryZ - origin.z) / dir.z : Float.MAX_VALUE;

    float t = 0f;

    int prevX = x;
    int prevY = y;
    int prevZ = z;

    while (t <= MAX_DISTANCE) {

      int worldY = -y; // Engine Y-down → World Y-up

      if (world.getBlock(x, worldY, z) != Blocks.AIR) {
        return new RaycastResult(true, x, worldY, z, prevX, -prevY, prevZ);
      }

      prevX = x;
      prevY = y;
      prevZ = z;

      // Step to next voxel
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

  // -------------------------------------------------------
  // Sphere Editing (Boundary Safe)
  // -------------------------------------------------------

  private void applySphere(int cx, int cy, int cz, short blockId) {

    int r2 = brushRadius * brushRadius;
    Set<Long> touched = new HashSet<>();

    for (int dx = -brushRadius; dx <= brushRadius; dx++) {
      for (int dy = -brushRadius; dy <= brushRadius; dy++) {
        for (int dz = -brushRadius; dz <= brushRadius; dz++) {

          if (dx * dx + dy * dy + dz * dz > r2) continue;

          int wx = cx + dx;
          int wy = cy + dy;
          int wz = cz + dz;

          boolean changed =
              (blockId == Blocks.AIR)
                  ? world.removeBlockWorld(wx, wy, wz)
                  : world.setBlockWorld(wx, wy, wz, blockId);

          if (!changed) continue;

          markChunkAndNeighbors(wx, wz, touched);
        }
      }
    }

    for (long key : touched) {
      streamer.onChunkEdited((int) (key >> 32), (int) key);
    }
  }

  private void markChunkAndNeighbors(int wx, int wz, Set<Long> touched) {

    int chunkX = Math.floorDiv(wx, Chunk.SIZE_X);
    int chunkZ = Math.floorDiv(wz, Chunk.SIZE_Z);

    touched.add(pack(chunkX, chunkZ));

    int localX = Math.floorMod(wx, Chunk.SIZE_X);
    int localZ = Math.floorMod(wz, Chunk.SIZE_Z);

    if (localX == 0) touched.add(pack(chunkX - 1, chunkZ));
    if (localX == Chunk.SIZE_X - 1) touched.add(pack(chunkX + 1, chunkZ));

    if (localZ == 0) touched.add(pack(chunkX, chunkZ - 1));
    if (localZ == Chunk.SIZE_Z - 1) touched.add(pack(chunkX, chunkZ + 1));
  }

  // -------------------------------------------------------

  private void handleBlockSelection() {
    if (input.wasKeyPressed(Key.NUM_1)) selectedBlock = Blocks.STONE;
    if (input.wasKeyPressed(Key.NUM_2)) selectedBlock = Blocks.DIRT;
    if (input.wasKeyPressed(Key.NUM_3)) selectedBlock = Blocks.GRASS;
  }

  private void handleBrushSize() {
    if (input.wasKeyPressed(Key.NUM_6)) brushRadius = 2;
    if (input.wasKeyPressed(Key.NUM_7)) brushRadius = 4;
    if (input.wasKeyPressed(Key.NUM_8)) brushRadius = 6;
  }

  @Override
  public void render(Graphics g) {

    g.setColor(Color.WHITE);
    g.text("Hit: " + lastHit.hit, 20, 20);

    if (!lastHit.hit) return;

    int bx = lastHit.blockX;
    int by = lastHit.blockY;
    int bz = lastHit.blockZ;

    float ry = -by; // World Y-up → Engine Y-down

    g.setColor(Color.YELLOW);

    float x0 = bx;
    float x1 = bx + 1;

    float y0 = ry;
    float y1 = ry - 1;

    float z0 = bz;
    float z1 = bz + 1;

    g.drawLine(x0, y0, z0, x1, y0, z0);
    g.drawLine(x1, y0, z0, x1, y0, z1);
    g.drawLine(x1, y0, z1, x0, y0, z1);
    g.drawLine(x0, y0, z1, x0, y0, z0);

    g.drawLine(x0, y1, z0, x1, y1, z0);
    g.drawLine(x1, y1, z0, x1, y1, z1);
    g.drawLine(x1, y1, z1, x0, y1, z1);
    g.drawLine(x0, y1, z1, x0, y1, z0);

    g.drawLine(x0, y0, z0, x0, y1, z0);
    g.drawLine(x1, y0, z0, x1, y1, z0);
    g.drawLine(x1, y0, z1, x1, y1, z1);
    g.drawLine(x0, y0, z1, x0, y1, z1);
  }

  private long pack(int x, int z) {
    return (((long) x) << 32) | (z & 0xffffffffL);
  }

  private static class RaycastResult {
    private final boolean hit;
    private final int blockX, blockY, blockZ;
    private final int placeX, placeY, placeZ;

    private RaycastResult(boolean hit, int bx, int by, int bz, int px, int py, int pz) {
      this.hit = hit;
      this.blockX = bx;
      this.blockY = by;
      this.blockZ = bz;
      this.placeX = px;
      this.placeY = py;
      this.placeZ = pz;
    }

    private static RaycastResult miss() {
      return new RaycastResult(false, 0, 0, 0, 0, 0, 0);
    }
  }
}
