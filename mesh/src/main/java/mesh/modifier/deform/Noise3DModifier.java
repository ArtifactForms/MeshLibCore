package mesh.modifier.deform;

import java.util.List;

import math.Vector3f;
import math.noise.Noise3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.VertexNormals;

/**
 * A mesh modifier that displaces vertices along their vertex normals
 * using a 3D noise function.
 * <p>
 * The displacement is computed per vertex by sampling a {@link Noise3D}
 * instance at the vertex position (scaled by {@code frequency}) and
 * multiplying the result by {@code factor}. The resulting scalar value
 * is applied along the corresponding vertex normal.
 * <p>
 * Vertex normals are calculated on demand via {@link VertexNormals#calculate(mesh.Mesh3D)}
 * and are not stored in the mesh. The mesh is modified in place.
 * <p>
 * Notes:
 * <ul>
 *   <li>If {@code factor == 0} or {@code frequency == 0}, the mesh is returned unchanged.</li>
 *   <li>The noise is sampled in object space using the current vertex positions.</li>
 *   <li>This modifier assumes that calculated vertex normals are normalized.</li>
 * </ul>
 */
public class Noise3DModifier implements IMeshModifier {

  /**
   * Controls the strength of the displacement.
   * A value of {@code 0} disables the modifier.
   */
  private float factor = 1f;

  /**
   * Controls the spatial frequency of the noise.
   * Higher values produce more detailed deformation.
   * A value of {@code 0} disables the modifier.
   */
  private float frequency = 1f;

  /**
   * The noise generator used for displacement.
   */
  private final Noise3D noise;

  /**
   * Creates a new {@code Noise3DModifier}.
   *
   * @param noise the noise generator used to sample displacement values
   * @throws IllegalArgumentException if {@code noise} is {@code null}
   */
  public Noise3DModifier(Noise3D noise) {
    if (noise == null) {
      throw new IllegalArgumentException("Noise cannot be null.");
    }
    this.noise = noise;
  }

  /**
   * Applies noise-based displacement to the given mesh.
   * <p>
   * Vertices are moved along their computed vertex normals.
   * The mesh is modified in place and returned.
   *
   * @param mesh the mesh to modify
   * @return the modified mesh
   * @throws IllegalArgumentException if {@code mesh} is {@code null}
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
	if (mesh == null) {
		throw new IllegalArgumentException("Mesh cannot be null.");  
	}
	
	if (factor == 0f || frequency == 0f) {
	    return mesh;
	}

    List<Vector3f> normals = VertexNormals.calculate(mesh);

    int vertexCount = mesh.getVertexCount();
    for (int index = 0; index < vertexCount; index++) {
      Vector3f normal = normals.get(index);
      Vector3f v = mesh.getVertexAt(index);


      float noiseValue = noise.sample(
    		  v.x * frequency, 
    		  v.y * frequency, 
    		  v.z * frequency
    		);

      float displacement = noiseValue * factor;
      v.addLocal(
    		  normal.x * displacement,
    		  normal.y * displacement,
    		  normal.z * displacement
    		);
    }

    return mesh;
  }

  /**
   * Returns the displacement strength factor.
   *
   * @return the displacement factor
   */
  public float getFactor() {
    return factor;
  }

  /**
   * Sets the displacement strength factor.
   * <p>
   * Higher values increase the magnitude of the noise-based deformation.
   * A value of {@code 0} disables the modifier.
   *
   * @param factor the displacement factor
   */
  public void setFactor(float factor) {
    this.factor = factor;
  }

  /**
   * Returns the noise sampling frequency.
   *
   * @return the noise frequency
   */
  public float getFrequency() {
    return frequency;
  }

  /**
   * Sets the noise sampling frequency.
   * <p>
   * Higher values produce finer, more detailed deformation.
   * A value of {@code 0} disables the modifier.
   *
   * @param frequency the noise frequency
   */
  public void setFrequency(float frequency) {
    this.frequency = frequency;
  }
}
