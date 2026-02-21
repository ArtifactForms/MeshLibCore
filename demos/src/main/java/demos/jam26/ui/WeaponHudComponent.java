package demos.jam26.ui;

import demos.jam26.assets.AssetRefs;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.resources.Image;
import engine.resources.ResourceManager;

public class WeaponHudComponent extends AbstractComponent implements RenderableComponent {

  private int pixelScale = 4;
  private int offsetX = 0;

  private int width = 256;
  private int height = 62;
//private int width = 512;
//private int height = 245;

  private Image image;

  public WeaponHudComponent() {
    image = ResourceManager.getInstance().loadImage(AssetRefs.IMAGE_WEAPON_CROSS_BOW_HUD_PATH);
  }

  @Override
  public void render(Graphics g) {
    int x = (g.getWidth() - ((width * pixelScale) + offsetX)) / 2;
    int y = g.getHeight() - (height * pixelScale);
    g.drawImage(image, x, y, width * pixelScale, height * pixelScale);
  }
}
