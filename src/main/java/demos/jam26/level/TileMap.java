package demos.jam26.level;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import demos.jam26.assets.AssetRefs;

public class TileMap {

  public static final int TILE_SIZE = 32;

  private BufferedImage levelImage;
  private BufferedImage overlayImage;

  public TileMap() {
    try {
      levelImage = ImageIO.read(TileMap.class.getResource(AssetRefs.IMAGE_LEVEL_PATH));
      overlayImage = ImageIO.read(TileMap.class.getResource(AssetRefs.IMAGE_LEVEL_OVERLAY_PATH));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public TileType getTileTypeAt(int x, int y) {
    if (!isInBounds(x, y)) return TileType.NOTHING;

    int rgb = levelImage.getRGB(x, y);

    if (rgb == Color.BLACK.getRGB()) return TileType.WALL;
    if (rgb == Color.BLUE.getRGB()) return TileType.NOTHING;
    if (rgb == Color.WHITE.getRGB()) return TileType.FLOOR;
    if (rgb == Color.RED.getRGB()) return TileType.PLAYER_SPAWN;
    if (rgb == Color.GREEN.getRGB()) return TileType.EXIT;
    if (rgb == Color.MAGENTA.getRGB()) return TileType.LOBBY_SPAWN;

    return TileType.NOTHING;
  }

  public TileType getOverlayTypeAt(int x, int y) {
    if (!isInBounds(x, y)) return TileType.NOTHING;

    int rgb = overlayImage.getRGB(x, y);

    if (rgb == Color.YELLOW.getRGB()) return TileType.ENEMY_SPAWN;
    if (rgb == Color.CYAN.getRGB()) return TileType.HEALTH;

    return TileType.NOTHING;
  }

  public boolean isBlockedAtWorld(float worldX, float worldZ) {
    int tileX = Math.floorDiv((int) worldX, TILE_SIZE);
    int tileY = Math.floorDiv((int) worldZ, TILE_SIZE);

    return getTileTypeAt(tileX, tileY) == TileType.WALL;
  }

  public boolean isWall(int x, int y) {
    return getTileTypeAt(x, y) == TileType.WALL;
  }

  public boolean isInBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < getNumTilesX() && y < getNumTilesY();
  }

  public int getNumTilesX() {
    return levelImage.getWidth();
  }

  public int getNumTilesY() {
    return levelImage.getHeight();
  }
}
