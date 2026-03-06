package demos.voxels;

import demos.voxels.ray.RaycastResult;
import demos.voxels.ray.Raycasting;
import demos.voxels.ui.TextDisplay;
import demos.voxels.world.BlockType;
import demos.voxels.world.World;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.components.StaticGeometry;
import engine.components.Transform;
import engine.rendering.Graphics;
import engine.rendering.Material;
import engine.runtime.debug.core.DebugDraw;
import engine.runtime.input.Input;
import engine.scene.camera.Camera;
import math.Color;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;

public class BlockInteractionComponent extends AbstractComponent implements RenderableComponent {

  private boolean lastPressed;
  private boolean lastPressedRight;
  private World world;
  private Input input;
  private RaycastResult lastHit = RaycastResult.miss();
  private TextDisplay display;
  private StaticGeometry geometry;

  public BlockInteractionComponent(World world, Input input, TextDisplay display) {
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
    lastHit = Raycasting.raycast(camera, world);
    display.setText(lastHit.toString());

    boolean pressed = input.isMousePressed(Input.LEFT);
    boolean pressedRight = input.isMousePressed(Input.RIGHT);

    if (lastHit.hit) {
      // LINKSSKLICK: Den getroffenen Block entfernen
      if (lastPressed && !pressed) {
        world.setBlock(lastHit.blockX, lastHit.blockY, lastHit.blockZ, BlockType.AIR);
      }

      // RECHTSKLICK: Den neuen Block an die Nachbarposition (placeX/Y/Z) setzen
      if (lastPressedRight && !pressedRight) {
        world.setBlock(lastHit.placeX, lastHit.placeY, lastHit.placeZ, BlockType.STONE);
      }
    }

    lastPressed = pressed;
    lastPressedRight = pressedRight;
    
    
//    if (lastPressedRight && !pressedRight) {
//        int px = lastHit.placeX;
//        int py = lastHit.placeY;
//        int pz = lastHit.placeZ;
//
//        // 1. Hole die Spieler-Position
//        Vector3f pPos = player.getPosition();
//        
//        // 2. Prüfe, ob die Platzierung mit dem Spieler kollidiert
//        // Da Voxel ganze Zahlen sind, nutzen wir Math.floor für die Spieler-Position
//        boolean collidesWithPlayer = 
//            (px == (int)Math.floor(pPos.x + 0.5f) && 
//             pz == (int)Math.floor(pPos.z + 0.5f) &&
//             (py == (int)Math.floor(-pPos.y) || py == (int)Math.floor(-pPos.y + 1)));
//
//        if (!collidesWithPlayer) {
//            world.setBlock(px, py, pz, BlockType.STONE);
//        }
//    }
    
  }

  @Override
  public void render(Graphics g) {
    DebugDraw.drawAxis(new Transform(), 1000);

    if (!lastHit.hit) return;
    float rx = (float) lastHit.blockX;
    float ry = (float) -lastHit.blockY;
    float rz = (float) lastHit.blockZ;

    g.pushMatrix();
    g.translate(rx, ry, rz);
    geometry.render(g);
    g.popMatrix();
  }
}
