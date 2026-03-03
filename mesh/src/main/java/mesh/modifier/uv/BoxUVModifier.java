package mesh.modifier.uv;

import math.Vector3f;
import mesh.Axis;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.geometry.MeshGeometryUtil;
import mesh.modifier.IMeshModifier;
import mesh.next.surface.SurfaceLayer;

/**
 * Mesh modifier that generates box-projected UV coordinates.
 *
 * <p>This modifier applies a classic <b>box (cube) UV mapping</b> strategy by projecting each face
 * of the mesh onto one of the primary planes (XY, XZ, or YZ), based on the dominant axis of the
 * face normal.
 *
 * <p>Each face receives its own set of UV coordinates. UVs are not shared between faces, which
 * avoids texture distortion but may introduce visible seams, especially on curved or organic
 * meshes.
 *
 * <h3>Use cases</h3>
 *
 * <ul>
 *   <li>Procedural meshes
 *   <li>Debug checkerboard textures
 *   <li>Terrain, rocks, and architectural geometry
 * </ul>
 *
 * <h3>Texture conventions</h3>
 *
 * <ul>
 *   <li>UVs are generated in object space
 *   <li>UVs are scaled by {@code scale}
 *   <li>UVs are not normalized to the [0,1] range
 *   <li>Texture wrapping (e.g. REPEAT) is expected
 * </ul>
 *
 * <p>This modifier mutates the mesh by assigning new UV coordinates and UV indices to each face.
 *
 * @see IMeshModifier
 * @see Mesh3D
 * @see Face3D
 */
public class BoxUVModifier implements IMeshModifier {

  /**
   * Scale factor used to convert object-space coordinates into UV space. A value of {@code 1.0}
   * maps one texture unit per world unit.
   */
  private float scale;

  /**
   * Creates a new {@code BoxUVModifier} with the given UV scale.
   *
   * <p>The scale defines how object-space distances are mapped into UV space. A value of {@code
   * 1.0} maps one world unit to one texture unit. Larger values result in denser texture tiling,
   * while smaller values stretch the texture across a larger area.
   *
   * @param scale Scale factor used to convert object-space coordinates into UV space
   * @throws IllegalArgumentException if the UV scale is zero, a zero scale would result in invalid
   *     texture coordinates
   */
  public BoxUVModifier(float scale) {
    validate(scale);
    this.scale = scale;
  }

  /**
   * Validates the given UV scale value.
   *
   * <p>The scale must be non-zero, as a zero scale would result in invalid texture coordinates
   * during UV generation.
   *
   * @param scale The UV scale value to validate
   * @throws IllegalArgumentException if {@code scale} is zero
   */
  private void validate(float scale) {
    if (scale == 0f) {
      throw new IllegalArgumentException("UV scale must not be zero");
    }
  }

  /**
   * Applies box-projected UV mapping to the given mesh.
   *
   * <p>For each face, a projection plane is selected based on the face normal. The face's vertices
   * are then projected onto that plane to generate UV coordinates.
   *
   * <p>Existing UV data on the mesh will be replaced.
   *
   * @param mesh The mesh to modify
   * @return The modified mesh instance
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {

    SurfaceLayer surfaceLayer = mesh.getSurfaceLayer();

    for (int j = 0; j < mesh.getFaceCount(); j++) {

      Face3D face = mesh.getFaceAt(j);
      int vc = face.getVertexCount();
      int[] uvIndices = new int[vc];

      Vector3f normal = MeshGeometryUtil.calculateFaceNormal(mesh, face);
      Axis axis = axisFromNormal(normal);

      for (int i = 0; i < vc; i++) {

        Vector3f p = mesh.getVertexAt(face.getIndexAt(i));

        float u, v;

        switch (axis) {
          case XY:
            u = p.x / scale;
            v = p.y / scale;
            break;
          case XZ:
            u = p.x / scale;
            v = p.z / scale;
            break;
          case YZ:
            u = p.y / scale;
            v = p.z / scale;
            break;
          default:
            throw new IllegalStateException();
        }

        surfaceLayer.addUV(u, v);
        uvIndices[i] = surfaceLayer.getUVCount() - 1;
      }

      // Set once per face
      surfaceLayer.setFaceUVIndices(j, uvIndices);
    }

    return mesh;
  }

  /**
   * Determines the dominant projection axis for a face based on its normal.
   *
   * <p>The axis with the largest absolute normal component is selected:
   *
   * <ul>
   *   <li>X dominant → YZ plane
   *   <li>Y dominant → XZ plane
   *   <li>Z dominant → XY plane
   * </ul>
   *
   * @param n Face normal vector (must be non-zero)
   * @return The projection axis to use
   */
  private Axis axisFromNormal(Vector3f n) {

    float ax = Math.abs(n.x);
    float ay = Math.abs(n.y);
    float az = Math.abs(n.z);

    if (ax >= ay && ax >= az) return Axis.YZ;
    if (ay >= ax && ay >= az) return Axis.XZ;
    return Axis.XY;
  }
}
