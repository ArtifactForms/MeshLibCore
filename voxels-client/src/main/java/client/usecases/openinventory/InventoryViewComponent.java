package client.usecases.openinventory;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import client.network.ClientNetwork;
import client.ui.InventoryView;
import client.ui.hotbar.HotbarBlockIconRenderer;
import common.game.Inventory;
import common.game.InventoryActionType;
import common.game.ItemStack;
import common.network.packets.InventoryActionPacket;
import engine.components.AbstractComponent;
import engine.components.Geometry;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.rendering.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureManager;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import math.Color;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreatorUV;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.ScaleModifier;

public class InventoryViewComponent extends AbstractComponent
    implements RenderableComponent, InventoryView {

  private Input input;
  private Inventory inventory;

  private static final int SCALE = 4;
  private static final int SIZE = 16;
  private static final int BORDER = 1;

  private static final int HOTBAR_SIZE = 9;

  private final int WIDTH = 9;
  private final int HEIGHT;

  private final int slotSize;

  private Texture texture;
  private Geometry geometry;

  private boolean inventoryOpen = false;
  private int hoveredSlot = -1;

  private HotbarBlockIconRenderer iconRenderer = new HotbarBlockIconRenderer();

  private int inventoryVersion = 0;
  private ItemStack cursorStack = null;

  // Drag Distribution
  private boolean dragDistributeActive = false;
  private Set<Integer> dragVisitedSlots = new HashSet<>();

  private TooltipRenderer tooltipRenderer = new DefaultTooltipRenderer();

  private ClientNetwork network;

  public InventoryViewComponent(Input input, Inventory inventory, ClientNetwork network) {

    this.input = input;
    this.inventory = inventory;
    this.network = network;

    int inventorySlots = inventory.getSize() - HOTBAR_SIZE;
    this.HEIGHT = (int) Math.ceil(inventorySlots / (float) WIDTH);

    this.slotSize = SIZE * SCALE;

    texture = createAndConfigureTexture();
    Mesh3D plane = createPlaneMesh();

    Material material = new Material();
    material.setDiffuseTexture(texture);
    material.setUseLighting(false);
    material.setColor(new Color(0, 0, 0, 0));

    geometry = new Geometry(plane, material);
  }

  @Override
  public void onUpdate(float tpf) {

    if (hoveredSlot == -1) return;

    handleLeftClick();
    handleRightClick();
    handleDragDistribute();
  }

  private void handleLeftClick() {

    if (!input.isMousePressed(0)) return;
    if (hoveredSlot == -1) return;

    if (input.isKeyPressed(Key.SHIFT)) {

      network.send(
          new InventoryActionPacket(
              hoveredSlot, 0, InventoryActionType.SHIFT_CLICK.ordinal(), inventoryVersion));

    } else {

      network.send(
          new InventoryActionPacket(
              hoveredSlot, 0, InventoryActionType.CLICK.ordinal(), inventoryVersion));
    }
  }

  private void handleRightClick() {

    if (!input.isMousePressed(1)) return;
    if (hoveredSlot == -1) return;

    network.send(
        new InventoryActionPacket(
            hoveredSlot, 1, InventoryActionType.CLICK.ordinal(), inventoryVersion));
  }

  private void handleDragDistribute() {

    // Drag Start
    if (input.isMousePressed(1)) {

      dragDistributeActive = true;
      dragVisitedSlots.clear();

      network.send(
          new InventoryActionPacket(
              hoveredSlot, 1, InventoryActionType.DRAG_START.ordinal(), inventoryVersion));
    }

    // Dragging
    if (dragDistributeActive && input.isMouseDown(1)) {

      if (hoveredSlot != -1 && !dragVisitedSlots.contains(hoveredSlot)) {

        dragVisitedSlots.add(hoveredSlot);

        network.send(
            new InventoryActionPacket(
                hoveredSlot, 1, InventoryActionType.DRAG_ADD.ordinal(), inventoryVersion));
      }
    }

    // Drag End
    if (dragDistributeActive && input.isMouseReleased(1)) {

      dragDistributeActive = false;
      dragVisitedSlots.clear();

      network.send(
          new InventoryActionPacket(
              hoveredSlot, 1, InventoryActionType.DRAG_END.ordinal(), inventoryVersion));
    }
  }

  private Texture createAndConfigureTexture() {

    Texture tex = TextureManager.getInstance().createTexture(createTexture());
    tex.setFilterMode(FilterMode.POINT);

    return tex;
  }

  private Mesh3D createPlaneMesh() {

    int width = ((WIDTH * SIZE) + BORDER * 2) * SCALE;
    int height = ((HEIGHT * SIZE) + BORDER * 2) * SCALE;

    Mesh3D mesh = new PlaneCreatorUV(0.5f).create();

    new ScaleModifier(width, 1, height).modify(mesh);
    new RotateXModifier(-Mathf.HALF_PI).modify(mesh);

    return mesh;
  }

  private BufferedImage createTexture() {

    int texW = (WIDTH * SIZE) + BORDER * 2;
    int texH = (HEIGHT * SIZE) + BORDER * 2;

    BufferedImage image = new BufferedImage(texW, texH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g = image.createGraphics();

    for (int y = 0; y < HEIGHT; y++)
      for (int x = 0; x < WIDTH; x++) {

        int px = BORDER + x * SIZE;
        int py = BORDER + y * SIZE;

        g.setColor(new java.awt.Color(70, 70, 70));
        g.fillRect(px, py, SIZE, SIZE);

        g.setColor(new java.awt.Color(0, 0, 0, 120));
        g.drawRect(px, py, SIZE - 1, SIZE - 1);

        g.setColor(new java.awt.Color(150, 150, 150));
        g.drawLine(px, py, px + SIZE - 1, py);
        g.drawLine(px, py, px, py + SIZE - 1);

        g.setColor(new java.awt.Color(30, 30, 30));
        g.drawLine(px + SIZE - 1, py, px + SIZE - 1, py + SIZE - 1);
        g.drawLine(px, py + SIZE - 1, px + SIZE - 1, py + SIZE - 1);
      }

    g.dispose();
    return image;
  }

  @Override
  public void render(Graphics g) {

    hoveredSlot = calculateHoveredSlot(g);

    int screenW = g.getWidth();
    int screenH = g.getHeight();

    if (inventoryOpen) {
      g.setColor(new Color(0, 0, 0, 0.4f));
      g.fillRect(0, 0, screenW, screenH);
    }

    ItemStack tooltipStack = null;
    float tooltipX = 0;
    float tooltipY = 0;

    int barWidth = HOTBAR_SIZE * slotSize;
    float startX = (screenW / 2f) - (barWidth / 2f);
    float yHotbar = screenH - slotSize - 20;

    for (int i = 0; i < HOTBAR_SIZE; i++) {

      float x = startX + i * slotSize;

      ItemStack stack = inventory.getSlot(i);

      if (stack != null)
        iconRenderer.render(
            g, stack.getItemId(), stack.getAmount(), x + slotSize / 2, yHotbar + slotSize / 2);

      if (hoveredSlot == i) {

        g.setColor(new Color(1f, 1f, 1f, 0.25f));
        g.fillRect(x, yHotbar, slotSize, slotSize);

        tooltipStack = stack;
        tooltipX = x + slotSize / 2;
        tooltipY = yHotbar - slotSize;
      }
    }

    if (inventoryOpen) {

      int centerX = screenW / 2;
      int centerY = screenH / 2;

      g.pushMatrix();
      g.translate(centerX, centerY, 0);

      geometry.render(g);

      int planeW = WIDTH * slotSize;
      int planeH = HEIGHT * slotSize;

      for (int i = HOTBAR_SIZE; i < inventory.getSize(); i++) {

        int gridIndex = i - HOTBAR_SIZE;
        int col = gridIndex % WIDTH;
        int row = gridIndex / WIDTH;

        float x = (col * slotSize) - (planeW / 2) + slotSize / 2;
        float y = (row * slotSize) - (planeH / 2) + slotSize / 2;

        ItemStack stack = inventory.getSlot(i);

        if (stack != null) iconRenderer.render(g, stack.getItemId(), stack.getAmount(), x, y);

        if (hoveredSlot == i) {

          g.setColor(new Color(1f, 1f, 1f, 0.25f));
          g.fillRect(x - slotSize / 2f, y - slotSize / 2f, slotSize, slotSize);

          if (stack != null) {

            tooltipStack = stack;
            tooltipX = x + screenW / 2f;
            tooltipY = y + screenH / 2f;
          }
        }
      }

      if (cursorStack != null) {

        float mx = input.getMouseX() - screenW / 2f;
        float my = input.getMouseY() - screenH / 2f;

        iconRenderer.render(g, cursorStack.getItemId(), cursorStack.getAmount(), mx, my);
      }

      g.popMatrix();
    }

    if (tooltipStack != null) renderTooltip(g, tooltipStack, tooltipX, tooltipY);
  }

  private void renderTooltip(Graphics g, ItemStack stack, float mouseX, float mouseY) {

    float x = mouseX + 12;
    float y = mouseY + 12;

    tooltipRenderer.render(g, stack, x, y);
  }

  private int calculateHoveredSlot(Graphics g) {

    float mx = input.getMouseX();
    float my = input.getMouseY();

    int screenW = g.getWidth();
    int screenH = g.getHeight();

    int barWidth = HOTBAR_SIZE * slotSize;

    float hotbarX = (screenW / 2f) - barWidth / 2f;
    float hotbarY = screenH - slotSize - 20;

    if (mx >= hotbarX && mx <= hotbarX + barWidth && my >= hotbarY && my <= hotbarY + slotSize) {

      int col = (int) ((mx - hotbarX) / slotSize);

      if (col >= 0 && col < HOTBAR_SIZE) return col;
    }

    if (!inventoryOpen) return -1;

    int planeW = WIDTH * slotSize;
    int planeH = HEIGHT * slotSize;

    float left = (screenW / 2f) - planeW / 2f;
    float top = (screenH / 2f) - planeH / 2f;

    int col = (int) ((mx - left) / slotSize);
    int row = (int) ((my - top) / slotSize);

    if (col < 0 || col >= WIDTH || row < 0 || row >= HEIGHT) return -1;

    int index = row * WIDTH + col + HOTBAR_SIZE;

    if (index >= inventory.getSize()) return -1;

    return index;
  }

  @Override
  public void display() {
    inventoryOpen = true;
  }

  @Override
  public void hide() {
    inventoryOpen = false;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}

  @Override
  public void setCursorStack(ItemStack stack) {
    this.cursorStack = stack;
  }
  
  @Override
  public void setInventoryVersion(int inventoryVersion) {
	  this.inventoryVersion = inventoryVersion;
  }
}
