package client.ui.hotbar;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import common.game.Hotbar;
import engine.components.AbstractComponent;
import engine.components.Geometry;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.rendering.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureManager;
import math.Color;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreatorUV;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.ScaleModifier;

public class HotbarViewComponent extends AbstractComponent
    implements RenderableComponent, HotbarView {

  // Hotbar configuration
  private static final int SCALE = 4;

  private static final int SIZE = 16;

  private static final int BORDER = 1;

  private static final int MARGIN = 20;

  private static final int SLOTS = Hotbar.SIZE;

  // Hotbar dimensions
  private final int slotSize = SIZE * SCALE;

  private final int scaledWidth = calculateScaledWidth();

  private final int scaledHeight = calculateScaledHeight();

  private final Texture texture;

  private final Mesh3D plane;

  private final Geometry geometry;

  // Model
  private Hotbar hotbar;

  // Icons
  private HotbarBlockIconRenderer iconRenderer = new HotbarBlockIconRenderer();

  public HotbarViewComponent(Hotbar hotbar) {
    this.hotbar = hotbar;
    this.texture = createAndConfigureTexture();
    this.plane = createAndTransformPlane();

    Material planeMaterial = createPlaneMaterial();
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
    new ScaleModifier(scaledWidth, 1, scaledHeight).modify(mesh);
    new RotateXModifier(-Mathf.HALF_PI).modify(mesh);
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

      // Slot shadows
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

    int posY = g.getHeight() - (slotSize / 2) - MARGIN;

    g.pushMatrix();
    // Hotbar
    g.translate(offsetX, posY, 0);
    geometry.render(g);

    // Selection Border
    int selected = hotbar.getSelectedSlot();

    int x = (selected * slotSize) - (scaledWidth / 2);

    g.strokeWeight(2);
    g.setColor(Color.WHITE);
    g.drawRect(x, -slotSize / 2, slotSize, slotSize);

    //    for (int i = 0; i < SLOTS; i++) {
    //
    //      ItemStack itemStack = hotbar.getSlot(i);
    //      if (itemStack == null) continue;
    //
    //      int x1 = (i * slotSize) - (scaledWidth / 2) + slotSize / 2;
    //
    //      iconRenderer.render(g, itemStack.getItemId(), itemStack.getAmount(), x1, 0);
    //    }

    g.popMatrix();
  }

  @Override
  public Hotbar getModel() { // TODO Auto-generated method stub
    return hotbar;
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
