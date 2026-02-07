package demos.voxels;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.components.AbstractComponent;
import engine.components.Geometry;
import engine.components.RenderableComponent;
import engine.render.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureManager;
import math.Color;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreatorUV;
import mesh.modifier.RotateXModifier;
import mesh.modifier.ScaleModifier;
import workspace.ui.Graphics;

public class Hotbar extends AbstractComponent implements RenderableComponent {

  // Hotbar configuration
  private static final int SCALE = 4;
  private static final int SIZE = 16;
  private static final int BORDER = 1;
  private static final int MARGIN = 20;
  private static final int SLOTS = 9;

  // Hotbar dimensions
  private final int slotSize = SIZE * SCALE;
  private final int scaledWidth = calculateScaledWidth();
  private final int scaledHeight = calculateScaledHeight();

  private final Texture texture;
  private final Mesh3D plane;
  private final Geometry geometry;

  public Hotbar() {
    this.texture = createAndConfigureTexture();
    Material planeMaterial = createPlaneMaterial();
    this.plane = createAndTransformPlane();
    this.geometry = new Geometry(plane, planeMaterial);
  }

  private Texture createAndConfigureTexture() {
    Texture tex = TextureManager.getInstance().createTexture(createTexture());
    tex.setFilterMode(FilterMode.POINT);
    return tex;
  }

  private Material createPlaneMaterial() {
    Material material = new Material();
    material.setDiffuseTexture(texture);
    material.setUseLighting(false);
    material.setColor(new Color(0, 0, 0, 0));
    return material;
  }

  private Mesh3D createAndTransformPlane() {
    Mesh3D mesh = new PlaneCreatorUV(0.5f).create();
    mesh.apply(new ScaleModifier(scaledWidth, 1, scaledHeight));
    mesh.apply(new RotateXModifier(-Mathf.HALF_PI));
    return mesh;
  }

  private BufferedImage createTexture() {
    int textureWidth = (SLOTS * SIZE) + BORDER * 2;
    int textureHeight = SIZE + BORDER * 2;
    BufferedImage image =
        new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();

    drawHotbarBorder(g2d, textureWidth, textureHeight);
    drawHotbarBackground(g2d);
    drawHotbarSlots(g2d);

    return image;
  }

  private void drawHotbarBorder(Graphics2D g2d, int width, int height) {
    g2d.setColor(java.awt.Color.BLACK);
    g2d.drawRoundRect(0, 0, width - 1, height - 1, 2, 2);
  }

  private void drawHotbarBackground(Graphics2D g2d) {
    g2d.translate(BORDER, BORDER);
    g2d.setColor(new java.awt.Color(0, 0, 0, 128));
    g2d.fillRect(0, 0, SLOTS * SIZE, SIZE);
  }

  private void drawHotbarSlots(Graphics2D g2d) {
    for (int i = 0; i < SLOTS; i++) {
      int x = i * SIZE;

      // Slot border
      g2d.setColor(java.awt.Color.LIGHT_GRAY);
      g2d.drawRoundRect(x, 0, SIZE - 1, SIZE - 1, 2, 2);

      // Slot highlights
      g2d.setColor(java.awt.Color.GRAY);
      g2d.drawLine(x + 2, 1, x + SIZE - 2, 1); // Horizontal top
      g2d.drawLine(x + 1, 1, x + 1, SIZE - 2); // Vertical top

      //      // Slot shadows
      g2d.setColor(java.awt.Color.DARK_GRAY);
      g2d.drawLine(x + 2, SIZE - 2, x + SIZE - 2, SIZE - 2); // Horizontal bottom
      g2d.drawLine(x + SIZE - 2, 2, x + SIZE - 2, SIZE - 2); // Vertical bottom
    }
  }

  private int calculateScaledWidth() {
    return ((SLOTS * SIZE) + BORDER * 2) * SCALE;
  }

  private int calculateScaledHeight() {
    return (SIZE + BORDER * 2) * SCALE;
  }

  @Override
  public void render(Graphics g) {
    int width = g.getWidth();
    int offsetX = width / 2;

    // Position the hotbar at the bottom center of the screen
    g.translate(offsetX, g.getHeight() - (slotSize / 2) - MARGIN, 0);
    geometry.render(g);
  }

  @Override
  public void onAttach() {
    // Optional: Add logic for when the component is attached to an entity
  }

  @Override
  public void onDetach() {
    // Optional: Add logic for when the component is detached from an entity
  }

  @Override
  public void onUpdate(float tpf) {
    // Optional: Add update logic (e.g., animations or interactions)
  }
}
