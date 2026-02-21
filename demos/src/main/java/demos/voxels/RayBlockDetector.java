package demos.voxels;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;
import engine.scene.camera.Camera;
import math.Color;
import math.Mathf;
import math.Ray3f;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;

public class RayBlockDetector extends AbstractComponent implements RenderableComponent {

  private Camera camera;
  private Ray3f ray;
  private Vector3f hitBlock; // Coordinates of the block hit
  private boolean blockHit; // If the ray hit a block

  private World voxelWorld;
  private final Mesh3D highlightCube = new CubeCreator(0.5f).create();
  private TextDisplay display;

  public RayBlockDetector(Camera camera, World voxelWorld, TextDisplay display) {
    this.camera = camera;
    this.voxelWorld = voxelWorld;
    this.ray = new Ray3f(new Vector3f(), new Vector3f());
    this.blockHit = false;
    this.display = display;
  }

  @Override
  public void render(Graphics g) {
    g.disableDepthTest();

    if (blockHit) {
      g.pushMatrix();
      g.setColor(Color.YELLOW);
      g.translate(hitBlock.x, -hitBlock.y, hitBlock.z);
      g.drawFaces(highlightCube);
      g.popMatrix();

      BlockType type = voxelWorld.getBlock((int) hitBlock.x, (int) hitBlock.y, (int) hitBlock.z);
      display.setText("Hit: " + hitBlock + ", Type: " + type + ", Origin: " + ray.getOrigin());
      g.setColor(Color.CYAN);
      g.drawLine(
          ray.getOrigin().x,
          ray.getOrigin().y,
          ray.getOrigin().z,
          hitBlock.x + 0.5f,
          hitBlock.y + 0.5f,
          hitBlock.z + 0.5f);
    } else {
      display.setText("Hit: NONE");
    }

    g.enableDepthTest();
  }

  @Override
  public void onUpdate(float tpf) {
    Vector3f origin = camera.getTransform().getPosition();
    Vector3f direction = camera.getTransform().getForward();
    ray = new Ray3f(origin, direction);

    detectBlock();
  }

  private void detectBlock() {
    Vector3f rayOrigin = ray.getOrigin();
    Vector3f rayDir = ray.getDirection();

    int x = (int) Math.floor(rayOrigin.x);
    int y = (int) Math.floor(rayOrigin.y);
    int z = (int) Math.floor(rayOrigin.z);

    float stepX = Math.signum(rayDir.x);
    float stepY = Math.signum(rayDir.y);
    float stepZ = Math.signum(rayDir.z);

    float tMaxX = intBound(rayOrigin.x, rayDir.x);
    float tMaxY = intBound(rayOrigin.y, rayDir.y);
    float tMaxZ = intBound(rayOrigin.z, rayDir.z);

    float tDeltaX = Math.abs(1 / rayDir.x);
    float tDeltaY = Math.abs(1 / rayDir.y);
    float tDeltaZ = Math.abs(1 / rayDir.z);

    for (int i = 0; i < 500; i++) {
      if (voxelWorld.isBlockAt(x, -y, z)) {
        hitBlock = new Vector3f(x, -y, z);
        blockHit = true;
        return;
      }

      if (tMaxX < tMaxY && tMaxX < tMaxZ) {
        tMaxX += tDeltaX;
        x += stepX;
      } else if (tMaxY < tMaxZ) {
        tMaxY += tDeltaY;
        y += stepY;
      } else {
        tMaxZ += tDeltaZ;
        z += stepZ;
      }
    }

    blockHit = false;
  }

  private float intBound(float s, float ds) {
    if (ds == 0) return Float.POSITIVE_INFINITY;
    return (ds > 0 ? Mathf.ceil(s) - s : s - Mathf.floor(s)) / Mathf.abs(ds);
  }

  @Override
  public void onAttach() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDetach() {
    // TODO Auto-generated method stub

  }
}
