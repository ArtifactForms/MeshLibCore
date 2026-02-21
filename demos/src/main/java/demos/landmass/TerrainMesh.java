package demos.landmass;

import math.Vector3f;
import mesh.Mesh3D;

/**
 * Generates a triangle-based grid mesh from a provided height map, suitable for procedural terrain
 * generation in the engine.
 *
 * <p>This class is inspired by Sebastian Lague's Unity script "MeshGenerator" from his YouTube
 * tutorial series *Procedural Landmass Generation* (Episode 5: Mesh). The original script has been
 * ported to fit the engine's architecture and existing mesh framework, with necessary adaptations
 * for compatibility.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Generates a 3D terrain mesh based on height map data.
 *   <li>Utilizes the {@link Mesh3D} framework for mesh creation.
 *   <li>Applies UV mapping for proper texture coordinates.
 * </ul>
 *
 * <p>Special thanks to Sebastian Lague for the excellent tutorial series, which provided valuable
 * insights and inspiration for this implementation.
 *
 * <p>For more details, watch the tutorial: <a
 * href="https://www.youtube.com/watch?v=4RpVBYW1r5M&list=PLFt_AvWsXl0eBW2EiBtl_sxmDtSgZBxB3&index=5">
 * Procedural Landmass Generation (E05: Mesh)</a>
 */
public class TerrainMesh {

  private float heightMultiplier = 50;

  private int width;

  private int height;

  private float[][] heightMap;

  private Mesh3D mesh;

  /**
   * Constructs a {@code TerrainMesh} object from the provided height map.
   *
   * <p>The height map is a 2D array of float values representing the terrain's elevation at each
   * point. The generated mesh will have a grid-like structure with vertex heights based on these
   * values.
   *
   * @param map a 2D float array representing the height map data.
   * @throws IllegalArgumentException if the height map is null or has inconsistent dimensions.
   */
  public TerrainMesh(float[][] map) {
    if (map == null || map.length == 0 || map[0].length == 0) {
      throw new IllegalArgumentException("Height map cannot be null or empty.");
    }
    this.width = map.length;
    this.height = map[0].length;
    this.heightMap = map;
    createMesh();
  }

  /**
   * Generates the mesh based on the height map.
   *
   * <p>Each vertex's Y-coordinate is determined by the height map value, multiplied by the {@code
   * heightMultiplier}. The generated mesh includes triangle faces and UV coordinates for texturing.
   */
  private void createMesh() {
    mesh = new Mesh3D();

    float topLeftX = (width - 1) / -2f;
    float topLeftZ = (height - 1) / 2f;
    int vertexIndex = 0;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        mesh.add(
            new Vector3f(
                topLeftX + x,
                -heightMap[x][y] * heightMultiplier,
                topLeftZ - y)); // Flip V-axis (Processing-specific)
        mesh.addUvCoordinate(x / (float) width, 1 - (y / (float) height));

        // Add triangles within the bounds of the height map
        if (x < width - 1 && y < height - 1) {
          mesh.addFaceWithSharedUVs(vertexIndex, vertexIndex + width + 1, vertexIndex + width);
          mesh.addFaceWithSharedUVs(vertexIndex + width + 1, vertexIndex, vertexIndex + 1);
        }
        vertexIndex++;
      }
    }
  }

  /**
   * Returns the generated {@link Mesh3D} object.
   *
   * @return the generated mesh.
   */
  public Mesh3D getMesh() {
    return mesh;
  }
}
