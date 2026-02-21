package demos.jam26port.game.ui.weaponhud;

import demos.jam26port.assets.AssetRefs;
import engine.render.Graphics;
import engine.resources.Image;
import engine.resources.ResourceManager;

public class WeaponViewImpl implements WeaponView {

  private int pixelScale = 4;
  private int offsetX = 0;

  private int width = 256;
  private int height = 62;

  private Image image;

  public WeaponViewImpl() {
    image = ResourceManager.getInstance().loadImage(AssetRefs.IMAGE_WEAPON_CROSS_BOW_HUD_PATH);
  }

  @Override
  public void render(Graphics g) {
    int x = (g.getWidth() - ((width * pixelScale) + offsetX)) / 2;
    int y = g.getHeight() - (height * pixelScale);
    g.drawImage(image, x, y, width * pixelScale, height * pixelScale);
  }
}
