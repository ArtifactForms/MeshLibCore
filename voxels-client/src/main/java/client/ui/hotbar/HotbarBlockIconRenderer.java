package client.ui.hotbar;

import client.resources.TextureAtlas;
import client.ui.GameTextures;
import engine.components.Geometry;
import engine.rendering.Graphics;
import engine.rendering.Material;
import engine.resources.Font;
import engine.resources.Texture;
import math.Color;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.next.surface.SurfaceLayer;

public class HotbarBlockIconRenderer {

  private Mesh3D cube;
  private Geometry geometry;
  private TextureAtlas textureAtlas;

  public HotbarBlockIconRenderer() {
    textureAtlas = GameTextures.TEXTURE_ATLAS;
    Texture texture = textureAtlas.getTexture();

    Material material = new Material();
    material.setDiffuseTexture(texture);

    cube = createPlane();
    geometry = new Geometry(cube, material);
  }

  private Mesh3D createPlane() {

    Mesh3D cube = new CubeCreator(0.5f).create();

    new RotateModifier(-Mathf.QUARTER_PI, TransformAxis.Y).modify(cube);
    new RotateModifier(-Mathf.QUARTER_PI, TransformAxis.X).modify(cube);

    new ScaleModifier(24, 24, 24).modify(cube);

    SurfaceLayer layer = cube.getSurfaceLayer();
    for (int i = 0; i < 8; i++) layer.addUV(0, 0);

    return cube;
  }

  public void applyBlockTexture(int id) {
    SurfaceLayer layer = cube.getSurfaceLayer();

    int row = id;
    int col = 0;

    float epsPx = 0.002f;
    float atlasWidth = textureAtlas.getWidth();
    float atlasHeight = textureAtlas.getHeight();

    float tileSize = textureAtlas.getTileSize();

    float px1 = col * tileSize + epsPx;
    float py1 = row * tileSize + epsPx;

    float px2 = (col + 1) * tileSize - epsPx;
    float py2 = (row + 1) * tileSize - epsPx;

    // convert to UV space
    float x1 = px1 / atlasWidth;
    float x2 = px2 / atlasWidth;
    float y1 = 1f - (py2 / atlasHeight);
    float y2 = 1f - (py1 / atlasHeight);

    SurfaceLayer surfaceLayer = cube.getSurfaceLayer();

    surfaceLayer.getUvAt(0).set(x1, y2);
    surfaceLayer.getUvAt(1).set(x2, y2);
    surfaceLayer.getUvAt(2).set(x2, y1);
    surfaceLayer.getUvAt(3).set(x1, y1);

    layer.setFaceUVIndices(0, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(1, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(2, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(3, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(4, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(5, new int[] {0, 1, 2, 3});
  }

  public void render(Graphics g, int id, int amount, float x, float y) {
    g.pushMatrix();

    g.translate(x, y, 0);
    
    applyBlockTexture(id);
    geometry.render(g);

    g.setColor(Color.WHITE);
    g.setFont(new Font("monogram-extended", 20, Font.PLAIN));
    g.text("" + amount, 10, 20);

    g.popMatrix();
  }
}
