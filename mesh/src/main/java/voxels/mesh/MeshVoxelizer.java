package voxels.mesh;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import voxels.Voxel;

/**
 * Converts a {@link Mesh3D} into a discrete set of {@link Voxel} coordinates.
 *
 * <p>The voxelizer performs surface sampling using a line-sweep strategy across triangle faces.
 * Sampled points are interpolated in continuous space and then snapped to integer grid coordinates
 * to form voxel positions.
 *
 * <p>Each voxel represents a discrete grid cell in 3D space. A {@link HashSet} ensures uniqueness,
 * preventing duplicate voxel entries during sampling.
 *
 * <p><b>Important:</b> This voxelizer only generates a surface shell. It does not fill the interior
 * volume of the mesh.
 */
public class MeshVoxelizer {

  /**
   * If enabled, each triangle is sampled from all three vertex pivots.
   *
   * <p>This improves surface coverage and helps eliminate potential gaps in the voxel shell,
   * especially for thin or steeply oriented triangles. Disabling this increases performance but may
   * introduce small holes.
   */
  private boolean smoothSampling;

  /**
   * Distance between sample points during line sweep.
   *
   * <p>Smaller values increase surface precision but reduce performance. Extremely small values may
   * cause redundant sampling because multiple points snap to the same voxel.
   *
   * <p><b>Recommended range:</b> 0.1 – 0.3
   */
  private float lineResolution;

  /** Internal storage for unique voxels generated during voxelization. */
  private HashSet<Voxel> voxels;

  /** Creates a new voxelizer with default settings. */
  public MeshVoxelizer() {
    this.smoothSampling = true;
    this.lineResolution = 0.1f;
  }

  /**
   * Samples a line segment between two vertices and snaps the interpolated points to integer voxel
   * coordinates.
   *
   * @param v1 start vertex of the line
   * @param v2 end vertex of the line
   */
  private void line(Vector3f v1, Vector3f v2) {

    float lineLength = v1.distance(v2);
    int steps = Math.max(1, (int) Math.ceil(lineLength / lineResolution));

    for (int i = 0; i <= steps; i++) {

      float t = (float) i / steps;

      // Interpolate between v2 -> v1
      Vector3f v = new Vector3f(v2).lerpLocal(v1, t);

      // Snap to integer voxel grid
      int x = Math.round(v.x);
      int y = Math.round(v.y);
      int z = Math.round(v.z);

      voxels.add(new Voxel(x, y, z));
    }
  }

  /**
   * Fills a triangle surface by sweeping lines from one vertex to the opposite edge.
   *
   * @param v0 pivot vertex
   * @param v1 first vertex of opposite edge
   * @param v2 second vertex of opposite edge
   */
  private void processTriangle(Vector3f v0, Vector3f v1, Vector3f v2) {

    float edgeLength = v1.distance(v2);
    int steps = Math.max(1, (int) Math.ceil(edgeLength / lineResolution));

    for (int i = 0; i <= steps; i++) {

      float t = (float) i / steps;

      Vector3f edgePoint = new Vector3f(v2).lerpLocal(v1, t);
      line(v0, edgePoint);
    }
  }

  /**
   * Voxelizes the entire mesh.
   *
   * @param mesh source mesh
   * @return unique collection of voxel coordinates representing the mesh surface
   */
  public Collection<Voxel> voxelize(Mesh3D mesh) {
    return voxelize(mesh, mesh.getFaces());
  }

  /**
   * Voxelizes a specific subset of triangle faces.
   *
   * <p>All faces must be triangles. Non-triangular faces must be triangulated before calling this
   * method.
   *
   * @param mesh source mesh
   * @param faces list of triangle faces
   * @return unique collection of voxel coordinates
   * @throws IllegalArgumentException if a face is not triangular
   */
  public Collection<Voxel> voxelize(Mesh3D mesh, List<Face3D> faces) {

    voxels = new HashSet<>();

    for (Face3D face : faces) {

      if (face.indices.length != 3) {
        throw new IllegalArgumentException(
            "Voxelizer only supports triangles. Please triangulate the mesh first.");
      }

      Vector3f v0 = mesh.getVertexAt(face.indices[0]);
      Vector3f v1 = mesh.getVertexAt(face.indices[1]);
      Vector3f v2 = mesh.getVertexAt(face.indices[2]);

      processTriangle(v0, v1, v2);

      if (smoothSampling) {
        processTriangle(v1, v2, v0);
        processTriangle(v2, v0, v1);
      }
    }

    return voxels;
  }

  /** @return {@code true} if smooth sampling is enabled */
  public boolean isSmoothSampling() {
    return smoothSampling;
  }

  /**
   * Enables or disables smooth sampling.
   *
   * @param smoothSampling {@code true} for maximum surface coverage, {@code false} for faster
   *     execution
   */
  public void setSmoothSampling(boolean smoothSampling) {
    this.smoothSampling = smoothSampling;
  }

  /** @return current line sampling resolution */
  public float getLineResolution() {
    return lineResolution;
  }

  /**
   * Sets the line sampling resolution.
   *
   * <p>Lower values increase precision but also computational cost.
   *
   * @param lineResolution sampling step size (recommended 0.1 – 0.3)
   */
  public void setLineResolution(float lineResolution) {
    this.lineResolution = lineResolution;
  }
}
