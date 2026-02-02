package mesh.uv;

import math.Vector2f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * Mesh modifier that flips the V (vertical) texture coordinate of all UVs.
 *
 * <p>This modifier inverts the {@code v} component of every texture coordinate by multiplying it by
 * {@code -1}. It is primarily intended to adapt meshes authored with a <b>bottom-left origin UV
 * convention</b> (e.g. OpenGL, most DCC tools) to rendering systems that assume a <b>top-left
 * origin</b> for textures, such as Processing.
 *
 * <h3>Use cases</h3>
 *
 * <ul>
 *   <li>Adapting OpenGL-style UVs for Processing rendering
 *   <li>Post-processing imported or procedurally generated meshes
 *   <li>Fixing vertically mirrored textures
 * </ul>
 *
 * <p>This modifier mutates the mesh in place by updating its existing UV coordinates.
 *
 * @see IMeshModifier
 * @see Mesh3D
 * @see Vector2f
 */
public class FlipVModifier implements IMeshModifier {

  /**
   * Flips the V component of all UV coordinates in the given mesh.
   *
   * @param mesh The mesh whose UV coordinates will be modified
   * @return The modified mesh instance
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    for (Vector2f uv : mesh.getUVCoordinates()) {
      uv.y = -uv.y;
    }
    return mesh;
  }
}
