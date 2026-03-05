package demos.voxels;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.components.StaticGeometry;
import engine.components.Transform;
import engine.physics.ray.Raycaster;
import engine.rendering.Graphics;
import engine.rendering.Material;
import engine.runtime.debug.core.DebugDraw;
import engine.runtime.input.Input;
import engine.scene.camera.Camera;
import math.Color;
import math.Ray3f;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;

public class RaycastComponent extends AbstractComponent implements RenderableComponent {

  private static final int MAX_DISTANCE = 500;
  private World world;
  private Input input;
  private RaycastResult lastHit = RaycastResult.miss();
  private TextDisplay display;
  private StaticGeometry geometry;

  public RaycastComponent(World world, Input input, TextDisplay display) {
    this.world = world;
    this.input = input;
    this.display = display;

    // Radius 0.505f umschließt den 0.5f Radius Block des Meshers perfekt (mit Epsilon)
    Mesh3D cube = new CubeCreator(0.505f).create();
    Material material = new Material(new Color(1, 1, 0, 0.3f)); // Gelb-Transparent
    geometry = new StaticGeometry(cube, material);
  }

  @Override
  public void update(float tpf) {
    Camera camera = getOwner().getScene().getActiveCamera();
    if (camera != null) {
      lastHit = raycast(camera);
      display.setText(lastHit.toString());
    }
  }

  @Override
  public void render(Graphics g) {
	  DebugDraw.drawAxis(new Transform(), 1000);
	  
	  
    if (!lastHit.hit) return;

    // --- SYNCHRONISATION MIT CHUNKMESHER ---
    // Mesher Formel: x - radius, -radius - y, z - radius
    // Da CubeCreator den Cube um (0,0,0) zentriert, müssen wir zu den 
    // Startpunkten des Meshers wieder 0.5 (den Radius) addieren.
    
    float rx = (float) lastHit.blockX; 
    float ry = (float) -lastHit.blockY;
    float rz = (float) lastHit.blockZ;

    g.pushMatrix();
    g.translate(rx, ry, rz);
    geometry.render(g);
    g.popMatrix();
  }

  private RaycastResult raycast(Camera camera) {
    Ray3f ray = Raycaster.crossHairRay(camera);
    Vector3f origin = ray.getOrigin();
    Vector3f dir = ray.getDirection().normalize();

    // Welt-Raum Transformation (Y-Spiegelung)
    float startX = origin.x;
    float startY = -origin.y; 
    float startZ = origin.z;

    float dirX = dir.x;
    float dirY = -dir.y;
    float dirZ = dir.z;

    // Korrektur: Wir runden auf den nächsten Voxel-Mittelpunkt ab
    int x = (int) Math.floor(startX + 0.5f);
    int y = (int) Math.floor(startY + 0.5f);
    int z = (int) Math.floor(startZ + 0.5f);

    int stepX = dirX >= 0 ? 1 : -1;
    int stepY = dirY >= 0 ? 1 : -1;
    int stepZ = dirZ >= 0 ? 1 : -1;

    float tDeltaX = Math.abs(1.0f / dirX);
    float tDeltaY = Math.abs(1.0f / dirY);
    float tDeltaZ = Math.abs(1.0f / dirZ);

    float tMaxX, tMaxY, tMaxZ;

    // DDA Startdistanzen
    if (dirX >= 0) tMaxX = (float) ((Math.floor(startX + 0.5f) + 0.5 - startX) * tDeltaX);
    else           tMaxX = (float) ((startX - (Math.floor(startX + 0.5f) - 0.5)) * tDeltaX);

    if (dirY >= 0) tMaxY = (float) ((Math.floor(startY + 0.5f) + 0.5 - startY) * tDeltaY);
    else           tMaxY = (float) ((startY - (Math.floor(startY + 0.5f) - 0.5)) * tDeltaY);

    if (dirZ >= 0) tMaxZ = (float) ((Math.floor(startZ + 0.5f) + 0.5 - startZ) * tDeltaZ);
    else           tMaxZ = (float) ((startZ - (Math.floor(startZ + 0.5f) - 0.5)) * tDeltaZ);

    int prevX = x, prevY = y, prevZ = z;
    float t = 0;

    while (t <= MAX_DISTANCE) {
      if (world.isSolid(x, y, z)) {
        return new RaycastResult(true, x, y, z, prevX, prevY, prevZ);
      }

      prevX = x; prevY = y; prevZ = z;

      if (tMaxX < tMaxY) {
        if (tMaxX < tMaxZ) { t = tMaxX; tMaxX += tDeltaX; x += stepX; }
        else { t = tMaxZ; tMaxZ += tDeltaZ; z += stepZ; }
      } else {
        if (tMaxY < tMaxZ) { t = tMaxY; tMaxY += tDeltaY; y += stepY; }
        else { t = tMaxZ; tMaxZ += tDeltaZ; z += stepZ; }
      }

      if (y < 0 || y >= Chunk.HEIGHT) break;
    }
    return RaycastResult.miss();
  }

  public static class RaycastResult {
    public final boolean hit;
    public final int blockX, blockY, blockZ; 
    public final int placeX, placeY, placeZ;

    public RaycastResult(boolean hit, int bx, int by, int bz, int px, int py, int pz) {
      this.hit = hit;
      this.blockX = bx; this.blockY = by; this.blockZ = bz;
      this.placeX = px; this.placeY = py; this.placeZ = pz;
    }

    public static RaycastResult miss() {
      return new RaycastResult(false, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public String toString() {
      return String.format("Hit: %b | Block: [%d, %d, %d]", hit, blockX, blockY, blockZ);
    }
  }
}