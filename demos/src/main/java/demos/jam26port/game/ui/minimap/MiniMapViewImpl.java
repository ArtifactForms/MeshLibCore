package demos.jam26port.game.ui.minimap;

import demos.jam26port.level.TileMap;
import demos.jam26port.level.TileType;
import engine.render.Graphics;
import math.Color;
import math.Vector3f;

/** Simple top-down minimap centered on the player. */
public class MiniMapViewImpl implements MinimapView {

  private static final float BORDER_PADDING = 4f;
  private static final float PLAYER_MARKER_SIZE = 8f;

  private final TileMap tileMap;

  private float x = 0;
  private float y = 0;
  private float size = 300;

  /** Tiles around player. */
  private int viewRadius = 20;

  int playerTileX;
  int playerTileY;

  public MiniMapViewImpl(TileMap tileMap) {
    this.tileMap = tileMap;
  }

  @Override
  public void render(Graphics g) {
    x = g.getWidth() - 320;
    y = 20;

    drawBackground(g);
    drawTiles(g, playerTileX, playerTileY);
    drawPlayerMarker(g);
  }

  private void drawBackground(Graphics g) {
    g.setColor(new Color(0, 0, 0, 0.35f));
    g.fillRect(
        x - BORDER_PADDING,
        y - BORDER_PADDING,
        size + BORDER_PADDING * 2,
        size + BORDER_PADDING * 2);

    g.setColor(new Color(1f, 1f, 1f, 0.5f));
    g.drawRect(
        x - BORDER_PADDING,
        y - BORDER_PADDING,
        size + BORDER_PADDING * 2,
        size + BORDER_PADDING * 2);
  }

  private void drawTiles(Graphics g, int centerTileX, int centerTileY) {
    int tilesVisible = viewRadius * 2 + 1;
    float tileSizePx = size / tilesVisible;

    for (int dx = -viewRadius; dx <= viewRadius; dx++) {
      for (int dy = -viewRadius; dy <= viewRadius; dy++) {

        int tx = centerTileX + dx;
        int ty = centerTileY + dy;

        if (!tileMap.isInBounds(tx, ty)) continue;

        TileType tile = tileMap.getTileTypeAt(tx, ty);
        if (tile == TileType.NOTHING) continue;

        float sx = x + (dx + viewRadius) * tileSizePx;
        float sy = y + (viewRadius - dy) * tileSizePx; // Y flip

        g.setColor(colorForTile(tile));
        g.fillRect(sx, sy, tileSizePx, tileSizePx);
      }
    }
  }

  private void drawPlayerMarker(Graphics g) {
    float center = size * 0.5f;

    g.setColor(Color.RED);
    g.fillOval(
        x + center - PLAYER_MARKER_SIZE * 0.5f,
        y + center - PLAYER_MARKER_SIZE * 0.5f,
        PLAYER_MARKER_SIZE,
        PLAYER_MARKER_SIZE);
  }

  private int worldToTile(float worldPos) {
    return (int) (worldPos / TileMap.TILE_SIZE);
  }

  private Color colorForTile(TileType tile) {
    switch (tile) {
      case WALL:
        return new Color(1f, 1f, 1f, 0.5f);
      default:
        return new Color(1f, 1f, 1f, 0.15f);
    }
  }

  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void setSize(float size) {
    this.size = size;
  }

  public void setViewRadius(int viewRadius) {
    this.viewRadius = viewRadius;
  }

  public void setPlayerWorldPosition(Vector3f position) {
    playerTileX = worldToTile(position.x);
    playerTileY = worldToTile(position.z);
  }
}
