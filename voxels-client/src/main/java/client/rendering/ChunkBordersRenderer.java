package client.rendering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import common.world.ChunkData;
import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import engine.rendering.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureManager;
import engine.resources.TextureWrapMode;
import engine.scene.CameraMode;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.transform.ScaleModifier;
import mesh.next.surface.SurfaceLayer;
import mesh.selection.FaceSelection;

public class ChunkBordersRenderer {

  private static boolean label = false;

  private static final int DIM = 16;

  private static final float PADDING = 0.002f; // Small padding to avoid Z-fighting

  private static final StaticGeometry CUBE;

  private ChunkBordersRenderer() {
    // No instances
  }

  static {
    Material material = new Material();
    material.setDiffuseTexture(createTexture());
    CUBE = new StaticGeometry(createMesh(), material);
  }

  private static Texture createTexture() {
    Texture texture = TextureManager.getInstance().createTexture(createTextureImage());
    texture.setFilterMode(FilterMode.POINT);
    texture.setTextureWrapMode(TextureWrapMode.REPEAT);
    return texture;
  }

  private static Mesh3D createMesh() {
    Mesh3D mesh = new CubeCreator(0.5f).create();

    // remove top + bottom
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectTopFaces();
    selection.selectBottomFaces();
    mesh.removeFaces(selection.getFaces());

    SurfaceLayer layer = mesh.getSurfaceLayer();

    layer.addUV(0, 1); // 3
    layer.addUV(1, 1); // 2
    layer.addUV(1, 0); // 1
    layer.addUV(0, 0); // 0

    layer.setFaceUVIndices(0, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(1, new int[] {1, 2, 3, 0});
    layer.setFaceUVIndices(2, new int[] {2, 3, 0, 1});
    layer.setFaceUVIndices(3, new int[] {1, 2, 3, 0});

    new ScaleModifier(DIM - PADDING, DIM, DIM - PADDING).modify(mesh);
    return mesh;
  }

  private static BufferedImage createTextureImage() {
    int size = 512;
    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) image.getGraphics();

    g2d.setColor(java.awt.Color.YELLOW);
    int cellSize = size / 16;
    for (int x = 0; x < 16; x++) {
      for (int y = 0; y < 16; y++) {
        g2d.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
      }
    }

    g2d.setColor(java.awt.Color.BLUE);
    g2d.drawRect(0, 0, size - 1, size - 1);
    if (label) {
      g2d.setColor(java.awt.Color.BLACK);
      g2d.drawString("Prototype-Chunk-Cage-Texture", 20, 20);
    }
    g2d.dispose();

    return image;
  }

  public static void render(Graphics g, Vector3f position, Vector3f camPos, CameraMode mode) {
    int cx = (int) Math.floor(position.x / (float) ChunkData.WIDTH);
    int cz = (int) Math.floor(position.z / (float) ChunkData.DEPTH);

    float worldX = cx * ChunkData.WIDTH;
    float worldZ = cz * ChunkData.DEPTH;

    float tx = worldX + (ChunkData.WIDTH / 2.0f) - 0.5f;
    float tz = worldZ + (ChunkData.DEPTH / 2.0f) - 0.5f;

    g.pushMatrix();

    // Kamera-Relative Translation
    if (mode == CameraMode.CAMERA_RELATIVE) {
      g.translate(tx - camPos.x, 0 - camPos.y, tz - camPos.z);
    } else {
      g.translate(tx, 0, tz);
    }

    renderSectionLines(g);

    g.popMatrix();
  }

  private static void renderSectionLines(Graphics g) {
    int sectionHeight = DIM; // 16
    int sectionsCount = ChunkData.HEIGHT / sectionHeight;

    for (int i = 0; i < sectionsCount; i++) {
      float renderY = -(i * sectionHeight + (sectionHeight / 2.0f)) + 0.5f;

      g.pushMatrix();
      g.translate(0, renderY, 0);
      CUBE.render(g);
      g.popMatrix();
    }
  }
}
