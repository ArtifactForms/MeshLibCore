package mesh.modifier.uv;

import math.Vector2f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * Mesh modifier that normalizes all UV coordinates to the [0,1] range.
 *
 * <p>This modifier computes the minimum and maximum U and V values across the mesh and remaps all
 * UVs so that the full UV layout fits into the normalized texture space.
 *
 * <p>The relative proportions of the UV layout are preserved.
 *
 * <h3>Use cases</h3>
 *
 * <ul>
 *   <li>Preparing meshes for single-use textures
 *   <li>Baked textures (AO, lightmaps, curvature)
 *   <li>Exporting meshes to DCC tools
 *   <li>Debugging UV layouts
 * </ul>
 *
 * <p>This modifier mutates the mesh in place by updating its existing UV coordinates.
 *
 * <p>If all U or all V values are identical, normalization along that axis is skipped to avoid
 * division by zero.
 *
 * @see IMeshModifier
 * @see Mesh3D
 * @see Vector2f
 */
public class NormalizeUVsModifier implements IMeshModifier {

  /**
   * Normalizes all UV coordinates of the given mesh to the [0,1] range.
   *
   * @param mesh The mesh whose UV coordinates will be normalized
   * @return The modified mesh instance
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {

    if (mesh.getUvCount() == 0) {
      return mesh;
    }

    float minU = Float.POSITIVE_INFINITY;
    float minV = Float.POSITIVE_INFINITY;
    float maxU = Float.NEGATIVE_INFINITY;
    float maxV = Float.NEGATIVE_INFINITY;

    // Compute bounds
    for (Vector2f uv : mesh.getUVCoordinates()) {
      minU = Math.min(minU, uv.x);
      minV = Math.min(minV, uv.y);
      maxU = Math.max(maxU, uv.x);
      maxV = Math.max(maxV, uv.y);
    }

    float rangeU = maxU - minU;
    float rangeV = maxV - minV;

    // Normalize
    for (Vector2f uv : mesh.getUVCoordinates()) {
      if (rangeU != 0f) {
        uv.x = (uv.x - minU) / rangeU;
      }
      if (rangeV != 0f) {
        uv.y = (uv.y - minV) / rangeV;
      }
    }

    return mesh;
  }
}
