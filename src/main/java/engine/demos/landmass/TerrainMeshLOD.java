package engine.demos.landmass;

import math.Vector3f;
import mesh.Mesh3D;

public class TerrainMeshLOD {

  private int levelOfDetail;

  private float heightMultiplier = 50;

  private int width;

  private int height;

  private float[][] heightMap;

  private Mesh3D mesh;

  public TerrainMeshLOD(float[][] map, int levelOfDetail) {
    if (map == null || map.length == 0 || map[0].length == 0) {
      throw new IllegalArgumentException("Height map cannot be null or empty.");
    }
    if (levelOfDetail < 0 || levelOfDetail > 6) {
      throw new IllegalArgumentException("Level of Detail must be between 0 and 6 inclusive.");
    }
    this.levelOfDetail = levelOfDetail;
    this.width = map.length;
    this.height = map[0].length;
    this.heightMap = map;
    createMesh();
  }

  private void createMesh() {
    mesh = new Mesh3D();

    int lodIncrement = levelOfDetail == 0 ? 1 : levelOfDetail * 2;
    int verticesPerLine = (width - 1) / lodIncrement + 1;

    float topLeftX = (width - 1) / -2f;
    float topLeftZ = (height - 1) / 2f;
    int vertexIndex = 0;

    for (int y = 0; y < height; y += lodIncrement) {
      for (int x = 0; x < width; x += lodIncrement) {
        mesh.add(
            new Vector3f(
                topLeftX + x,
                -heightMap[x][y] * heightMultiplier,
                topLeftZ - y)); // Flip V-axis (Processing-specific)
        mesh.addUvCoordinate(x / (float) width, 1 - (y / (float) height));

        // Add triangles within the bounds of the height map
        if (x < width - 1 && y < height - 1) {
          boolean useIndicesAsUvs = true;
          mesh.addFace(
              useIndicesAsUvs,
              vertexIndex,
              vertexIndex + verticesPerLine + 1,
              vertexIndex + verticesPerLine);
          mesh.addFace(
              useIndicesAsUvs, vertexIndex + verticesPerLine + 1, vertexIndex, vertexIndex + 1);
        }
        vertexIndex++;
      }
    }
  }

  public Mesh3D getMesh() {
    return mesh;
  }
}
