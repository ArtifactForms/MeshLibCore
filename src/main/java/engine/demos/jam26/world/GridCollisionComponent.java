package engine.demos.jam26.world;

import engine.components.AbstractComponent;
import engine.components.Transform;
import engine.demos.jam26.level.TileMap;
import engine.demos.jam26.player.MovementFilter;
import math.Vector3f;

public class GridCollisionComponent extends AbstractComponent implements MovementFilter {

  private final TileMap map;
  private final float radius;
  private final float tileSize;

  public GridCollisionComponent(TileMap map, float radius, float tileSize) {
    this.map = map;
    this.radius = radius;
    this.tileSize = tileSize;
  }

  @Override
  public Vector3f filter(Transform transform, Vector3f delta, float tpf) {
    Vector3f pos = transform.getPosition();
    Vector3f out = new Vector3f();

    float half = tileSize * 0.5f;

    // x-axis
    if (delta.x != 0) {
      float dir = Math.signum(delta.x);
      float testX = pos.x + delta.x + dir * radius;

      boolean blocked = blockedAt(testX, pos.z - radius) || blockedAt(testX, pos.z + radius);

      if (!blocked) {
        out.x = delta.x;
      }
    }

    // z-axis
    if (delta.z != 0) {
      float dir = Math.signum(delta.z);
      float testZ = pos.z + delta.z + dir * radius;

      boolean blocked = blockedAt(pos.x - radius, testZ) || blockedAt(pos.x + radius, testZ);

      if (!blocked) {
        out.z = delta.z;
      }
    }

    return out;
  }

  private boolean blockedAt(float wx, float wz) {
    float half = tileSize * 0.5f;
    int tx = (int) Math.floor((wx + half) / tileSize);
    int tz = (int) Math.floor((wz + half) / tileSize);
    return map.isWall(tx, tz);
  }
}
