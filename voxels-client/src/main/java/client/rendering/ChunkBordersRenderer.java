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
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.uv.BoxUVModifier;

public class ChunkBordersRenderer {

  private static float padding = 0.002f; // Small padding to avoid Z-fighting
  private static final StaticGeometry geometry;

  private ChunkBordersRenderer() {
    // No instances
  }

  static {
    Mesh3D mesh = createMesh();
    Material material = new Material();
    material.setDiffuseTexture(createGridTexture());

    geometry = new StaticGeometry(mesh, material);
  }

  private static Mesh3D createMesh() {
    CubeCreator creator = new CubeCreator(0.5f);
    Mesh3D mesh = creator.create();
    new ScaleModifier(
            ChunkData.WIDTH - padding, ChunkData.HEIGHT - padding, ChunkData.DEPTH - padding)
        .modify(mesh);

    new BoxUVModifier(1).modify(mesh);
    return mesh;
  }

  private static Texture createGridTexture() {
    Texture texture = TextureManager.getInstance().createTexture(createGridTextureImage());
    texture.setFilterMode(FilterMode.POINT);
    texture.setTextureWrapMode(TextureWrapMode.REPEAT);
    return texture;
  }

  private static BufferedImage createGridTextureImage() {
    int size = 64;
    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    g2d.setColor(java.awt.Color.YELLOW);
    g2d.drawRect(0, 0, size - 1, size - 1);
    g2d.dispose();
    return image;
  }

  public static void render(Graphics g, Vector3f position) {
    int cx = (int) Math.floor(position.x / (float) ChunkData.WIDTH);
    int cz = (int) Math.floor(position.z / (float) ChunkData.DEPTH);

    float worldX = cx * ChunkData.WIDTH;
    float worldZ = cz * ChunkData.DEPTH;

    float tx = worldX + (ChunkData.WIDTH / 2.0f) - 0.5f;
    float tz = worldZ + (ChunkData.DEPTH / 2.0f) - 0.5f;
    float ty = (ChunkData.HEIGHT / 2.0f) - 0.5f;

    g.pushMatrix();
    g.translate(tx, -ty, tz);
    g.setColor(Color.RED);
    geometry.render(g);
    g.strokeWeight(3);
    g.popMatrix();
  }
}
