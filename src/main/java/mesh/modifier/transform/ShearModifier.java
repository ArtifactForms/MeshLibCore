package mesh.modifier.transform;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * A modifier that applies a shear transformation to a 3D mesh. The shear effect
 * distorts the mesh along a specified axis, creating a slanted or skewed
 * appearance.
 * 
 * The transformation is applied to each vertex of the mesh based on the
 * specified shear axis and factor.
 */
public class ShearModifier implements IMeshModifier {

	/**
	 * Represents the axis and plane along which the shear is applied.
	 * 
	 * <pre>
	 * - XY: Shear along the X-axis based on the Y-coordinate.
	 * - XZ: Shear along the X-axis based on the Z-coordinate.
	 * - YX: Shear along the Y-axis based on the X-coordinate.
	 * - YZ: Shear along the Y-axis based on the Z-coordinate.
	 * - ZX: Shear along the Z-axis based on the X-coordinate.
	 * - ZY: Shear along the Z-axis based on the Y-coordinate.
	 * </pre>
	 */
	public enum ShearAxis {
		XY, XZ, YX, YZ, ZX, ZY
	}

	/**
	 * The factor by which the mesh is sheared.
	 */
	private float shearFactor;

	/**
	 * The axis along which the shear transformation is applied.
	 */
	private ShearAxis axis;

	/**
	 * Constructs a ShearModifier with the specified shear axis and factor.
	 * 
	 * @param axis        the axis along which the shear transformation is applied
	 * @param shearFactor the factor by which the mesh is sheared
	 * @throws IllegalArgumentException if axis is null
	 */
	public ShearModifier(ShearAxis axis, float shearFactor) {
		if (axis == null) {
			throw new IllegalArgumentException("Shear axis cannot be null.");
		}
		this.axis = axis;
		this.shearFactor = shearFactor;
	}

	/**
	 * Applies the shear transformation to the given mesh.
	 * 
	 * @param mesh the mesh to be modified
	 * @return the modified mesh with the shear transformation applied
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		validateMesh(mesh);
		if (mesh.vertices.isEmpty()) {
			return mesh;
		}
		applyShear(mesh);
		return mesh;
	}

	/**
	 * Applies the shear transformation to all vertices in the given mesh.
	 * 
	 * This method uses a parallel stream to process the vertices, applying the
	 * shear transformation in a concurrent manner for improved performance on
	 * large meshes. The shear transformation is determined by the specified shear
	 * axis and factor.
	 * 
	 * @param mesh the mesh whose vertices will be sheared
	 * @throws IllegalArgumentException if the mesh is null or contains null
	 *                                  vertices
	 */
	private void applyShear(Mesh3D mesh) {
		mesh.vertices.parallelStream().forEach(this::applyShearToVertex);
	}

	/**
	 * Applies the shear transformation to a single vertex based on the specified
	 * shear axis and factor.
	 * 
	 * @param vertex the vertex to modify
	 */
	private void applyShearToVertex(Vector3f vertex) {
		switch (axis) {
		case XY -> vertex.x += shearFactor * vertex.y;
		case XZ -> vertex.x += shearFactor * vertex.z;
		case YX -> vertex.y += shearFactor * vertex.x;
		case YZ -> vertex.y += shearFactor * vertex.z;
		case ZX -> vertex.z += shearFactor * vertex.x;
		case ZY -> vertex.z += shearFactor * vertex.y;
		default ->
		  throw new IllegalArgumentException("Unsupported shear axis: " + axis);
		}
	}

	/**
	 * Validates that the mesh is not null.
	 *
	 * @param mesh the mesh to validate.
	 * @throws IllegalArgumentException if the mesh is null.
	 */
	private void validateMesh(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
	}

	/**
	 * Gets the current shear axis.
	 * 
	 * @return the shear axis
	 */
	public ShearAxis getAxis() {
		return axis;
	}

	/**
	 * Sets the shear axis.
	 * 
	 * @param axis the shear axis to set
	 */
	public void setAxis(ShearAxis axis) {
		if (axis == null) {
			throw new IllegalArgumentException("Shear axis cannot be null.");
		}
		this.axis = axis;
	}

	/**
	 * Gets the current shear factor.
	 * 
	 * @return the shear factor
	 */
	public float getShearFactor() {
		return shearFactor;
	}

	/**
	 * Sets the shear factor.
	 * 
	 * @param shearFactor the shear factor to set
	 */
	public void setShearFactor(float shearFactor) {
		this.shearFactor = shearFactor;
	}

}