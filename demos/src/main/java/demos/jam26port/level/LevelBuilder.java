package demos.jam26port.level;

import demos.jam26port.assets.AssetRefs;
import demos.jam26port.core.Settings;
import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.resources.Texture;
import engine.scene.SceneNode;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.repair.UpdateFaceNormalsModifier;
import mesh.modifier.topology.FlipFacesModifier;
import mesh.next.surface.SurfaceLayer;

public class LevelBuilder {

  private float tileSize;
  private float halfTileSize;
  private TileMap tileMap;
  private Mesh3D levelMesh;
  private Texture texture;
  private Vector3f playerSpawn = new Vector3f();
  private Vector3f exit = new Vector3f();
  private Vector3f lobbySpawn = new Vector3f();

  public LevelBuilder(TileMap tileMap) {
    this.tileMap = tileMap;
    this.tileSize = TileMap.TILE_SIZE;
    this.halfTileSize = TileMap.TILE_SIZE * 0.5f;
    texture = AssetRefs.ATLAS_TEXTURE;
  }

  public SceneNode createLevel() {
    createMesh();

    Material material = new Material();
    material.setColor(Color.WHITE);
    material.setUseLighting(true);
    material.setDiffuseTexture(texture);

    StaticGeometry geometry = new StaticGeometry(levelMesh, material);

    SceneNode levelNode = new SceneNode("Level", geometry);
    return levelNode;
  }

  private void createMesh() {
    levelMesh = new Mesh3D();

    for (int x = 0; x < tileMap.getNumTilesX(); x++) {
      for (int y = 0; y < tileMap.getNumTilesY(); y++) {
        Vector3f offset = new Vector3f(tileSize * x, 0, tileSize * y);
        TileType tileType = tileMap.getTileTypeAt(x, y);

        int row = Settings.DEBUG_TEXTURE ? Settings.DEBUG_TEXTURE_ATLAS_ROW : 0; // Debug texture

        if (tileType == TileType.PLAYER_SPAWN) {
          playerSpawn.set(x * TileMap.TILE_SIZE, 0, y * TileMap.TILE_SIZE);
        }

        if (tileType == TileType.LOBBY_SPAWN) {
          lobbySpawn.set(x * TileMap.TILE_SIZE, 0, y * TileMap.TILE_SIZE);
        }

        if (tileType == TileType.EXIT) {
          exit.set(x * TileMap.TILE_SIZE, 0, y * TileMap.TILE_SIZE);
        }

        createTile(offset, row, getAtlasCol(tileType));

        if (tileType == TileType.FLOOR) createCeiling(offset.add(0, -tileSize * 2, 0), row, 5);

        if (tileType == TileType.WALL) {
          if (needToCreateFrontWall(x, y)) createFrontWall(offset.add(0, -halfTileSize, 0), row, 1);
          if (needToCreateFrontWall(x, y))
            createFrontWall(offset.add(0, -tileSize * 1.5f, 0), row, 1);

          if (needToCreateRightWall(x, y)) createRightWall(offset.add(0, -halfTileSize, 0), row, 1);
          if (needToCreateRightWall(x, y))
            createRightWall(offset.add(0, -tileSize * 1.5f, 0), row, 1);

          if (needToCreateLeftWall(x, y)) createLeftWall(offset.add(0, -halfTileSize, 0), row, 1);
          if (needToCreateLeftWall(x, y))
            createLeftWall(offset.add(0, -tileSize * 1.5f, 0), row, 1);

          if (needToCreateBackWall(x, y)) createBackWall(offset.add(0, -halfTileSize, 0), row, 1);
          if (needToCreateBackWall(x, y))
            createBackWall(offset.add(0, -tileSize * 1.5f, 0), row, 1);
        }
      }
    }

    new FlipFacesModifier().modify(levelMesh);

    UpdateFaceNormalsModifier modifier = new UpdateFaceNormalsModifier();
    modifier.modify(levelMesh);
  }

  private int getAtlasCol(TileType tileType) {
    // BLUE 0 NOTHING
    // BLACK 1 WALL
    // WHITE 2 FLOOR
    // RED 3 PLAYER_SPAWN
    // GREEN 4 EXIT

    switch (tileType) {
      case NOTHING:
        return 0;
      case WALL:
        return 1;
      case FLOOR:
        //        return Mathf.random(0, 6);
        return 2;
      case PLAYER_SPAWN:
        return 3;
        //      case ENEMY_SPAWN:
        //        return 0;
      case EXIT:
        return 4;
      default:
        return -1;
    }
  }

  private void createTile(Vector3f offset, int row, int col) {
    int nextIndex = levelMesh.getVertexCount();
    float radius = halfTileSize;

    Vector3f v0 = new Vector3f(+radius, 0, -radius).add(offset);
    Vector3f v1 = new Vector3f(+radius, 0, +radius).add(offset);
    Vector3f v2 = new Vector3f(-radius, 0, +radius).add(offset);
    Vector3f v3 = new Vector3f(-radius, 0, -radius).add(offset);

    levelMesh.add(v2, v1, v0, v3);

    levelMesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);

    createUVs(nextIndex, row, col);
  }

  private void createCeiling(Vector3f offset, int row, int col) {
    int nextIndex = levelMesh.getVertexCount();
    float radius = halfTileSize;

    Vector3f v0 = new Vector3f(+radius, 0, -radius).add(offset);
    Vector3f v1 = new Vector3f(+radius, 0, +radius).add(offset);
    Vector3f v2 = new Vector3f(-radius, 0, +radius).add(offset);
    Vector3f v3 = new Vector3f(-radius, 0, -radius).add(offset);

    levelMesh.add(v3, v0, v1, v2);

    levelMesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);

    createUVs(nextIndex, row, col);
  }

  private void createBackWall(Vector3f offset, int row, int col) {
    int nextIndex = levelMesh.getVertexCount();
    float radius = halfTileSize;

    Vector3f v0 = new Vector3f(+radius, -radius, -halfTileSize).add(offset);
    Vector3f v1 = new Vector3f(+radius, +radius, -halfTileSize).add(offset);
    Vector3f v2 = new Vector3f(-radius, +radius, -halfTileSize).add(offset);
    Vector3f v3 = new Vector3f(-radius, -radius, -halfTileSize).add(offset);

    levelMesh.add(v1, v2, v3, v0);

    levelMesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);

    createUVs(nextIndex, row, col);
  }

  private void createFrontWall(Vector3f offset, int row, int col) {
    int nextIndex = levelMesh.getVertexCount();
    float radius = halfTileSize;

    Vector3f v0 = new Vector3f(+radius, -radius, halfTileSize).add(offset);
    Vector3f v1 = new Vector3f(+radius, +radius, halfTileSize).add(offset);
    Vector3f v2 = new Vector3f(-radius, +radius, halfTileSize).add(offset);
    Vector3f v3 = new Vector3f(-radius, -radius, halfTileSize).add(offset);

    levelMesh.add(v2, v1, v0, v3);

    levelMesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);

    createUVs(nextIndex, row, col);
  }

  private void createRightWall(Vector3f offset, int row, int col) {
    int nextIndex = levelMesh.getVertexCount();
    float radius = halfTileSize;

    Vector3f v0 = new Vector3f(halfTileSize, +radius, -radius).add(offset);
    Vector3f v1 = new Vector3f(halfTileSize, +radius, +radius).add(offset);
    Vector3f v2 = new Vector3f(halfTileSize, -radius, +radius).add(offset);
    Vector3f v3 = new Vector3f(halfTileSize, -radius, -radius).add(offset);

    levelMesh.add(v1, v0, v3, v2);

    levelMesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);

    createUVs(nextIndex, row, col);
  }

  private void createLeftWall(Vector3f offset, int row, int col) {
    int nextIndex = levelMesh.getVertexCount();
    float radius = halfTileSize;

    Vector3f v0 = new Vector3f(-halfTileSize, +radius, -radius).add(offset);
    Vector3f v1 = new Vector3f(-halfTileSize, +radius, +radius).add(offset);
    Vector3f v2 = new Vector3f(-halfTileSize, -radius, +radius).add(offset);
    Vector3f v3 = new Vector3f(-halfTileSize, -radius, -radius).add(offset);

    levelMesh.add(v0, v1, v2, v3);

    levelMesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);

    createUVs(nextIndex, row, col);
  }

  private boolean needToCreateFrontWall(int x, int y) {
    if ((y + 1) > tileMap.getNumTilesY()) {
      return true;
    }
    TileType type = tileMap.getTileTypeAt(x, y + 1);
    return type == TileType.FLOOR;
  }

  private boolean needToCreateBackWall(int x, int y) {
    if ((y - 1) > tileMap.getNumTilesY()) {
      return true;
    }
    TileType type = tileMap.getTileTypeAt(x, y - 1);
    return type == TileType.FLOOR;
  }

  private boolean needToCreateRightWall(int x, int y) {
    if ((x + 1) > tileMap.getNumTilesX()) {
      return true;
    }
    TileType type = tileMap.getTileTypeAt(x + 1, y);
    return type == TileType.FLOOR;
  }

  private boolean needToCreateLeftWall(int x, int y) {
    if ((x - 1) < 0) {
      return true;
    }
    TileType type = tileMap.getTileTypeAt(x - 1, y);
    return type == TileType.FLOOR;
  }

  private void createUVs(int nextIndex, int row, int col) {
    float atlasWidth = texture.getWidth();
    float atlasHeight = texture.getHeight();

    // inset to avoid bleeding
    float epsPx = 0.25f;

    // pixel-space coordinates
    float px1 = col * tileSize + epsPx;
    float py1 = row * tileSize + epsPx;

    float px2 = (col + 1) * tileSize - epsPx;
    float py2 = (row + 1) * tileSize - epsPx;

    // convert to UV space
    float x1 = px1 / atlasWidth;
    float y1 = py1 / atlasHeight;
    float x2 = px2 / atlasWidth;
    float y2 = py2 / atlasHeight;

    SurfaceLayer surfaceLayer = levelMesh.getSurfaceLayer();

    surfaceLayer.addUV(x1, y2);
    surfaceLayer.addUV(x2, y2);
    surfaceLayer.addUV(x2, y1);
    surfaceLayer.addUV(x1, y1);

    int faceIndex = levelMesh.getFaceCount() - 1;
    surfaceLayer.setFaceUVIndices(
        faceIndex, new int[] {nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3});
  }

  public Vector3f getPlayerSpawn() {
    return new Vector3f(playerSpawn);
  }

  public Vector3f getLobbySpawn() {
    return new Vector3f(lobbySpawn);
  }

  public Vector3f getExit() {
    return exit;
  }

  public Mesh3D getLevelMesh() {
    return levelMesh;
  }
}
