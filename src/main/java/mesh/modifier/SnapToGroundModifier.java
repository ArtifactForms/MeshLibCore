package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

/**
 * The Snap to Ground Modifier is a utility designed to align a 3D mesh's
 * highest point with a specified ground level. This modifier is particularly
 * useful in scenarios such as terrain modeling, architectural visualization,
 * and game development, where precise alignment with a reference plane is
 * essential.
 * 
 * The modifier calculates the vertical translation required to adjust the mesh,
 * ensuring that its highest point rests exactly at the defined ground level.
 * 
 * <pre>
 * Key Considerations: 
 * - Simple Ground Plane: Assumes a flat, horizontal
 * ground plane. It may not work as expected with complex terrain or curved
 * surfaces. 
 * - Mesh Orientation: Assumes the Y-axis is the vertical axis.
 * Meshes with different orientations might need preprocessing to achieve the
 * desired results. 
 * - Mesh Topology: Works best with simple, closed meshes.
 * Complex meshes with holes or self-intersections may require additional
 * adjustments or processing.
 * </pre>
 */
public class SnapToGroundModifier implements IMeshModifier {

	/** The level at which the mesh should be snapped. */
	private float groundLevel;

	/** The distance to translate the mesh to align it with the ground level. */
	private float distanceToGround;

	/** The mesh to be modified. */
	private Mesh3D mesh;

	public SnapToGroundModifier() {
		this(0);
	}

	public SnapToGroundModifier(float groundLevel) {
		this.groundLevel = groundLevel;
	}

	/**
	 * Modifies the given mesh by snapping its highest point to the ground level. If
	 * the provided mesh contains no vertices, the method safely returns the mesh
	 * without changes.
	 * 
	 * @param mesh the mesh to modify
	 * @return the modified mesh
	 * @throws IllegalArgumentException if the provided mesh is null
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (mesh.vertices.isEmpty()) {
			return mesh;
		}
		setMesh(mesh);
		calculateDistanceToGround();
		translateMesh();
		return mesh;
	}

	/**
	 * Calculates the vertical distance required to align the mesh's highest point
	 * with the ground level.
	 */
	private void calculateDistanceToGround() {
		float maxHeight = findHighestPoint();
		distanceToGround = groundLevel - maxHeight;
	}

	/**
	 * Translates the entire mesh vertically to align its highest point with the
	 * ground level.
	 */
	private void translateMesh() {
		Vector3f delta = new Vector3f(0, distanceToGround, 0);
		mesh.vertices.parallelStream().forEach(vertex -> vertex.addLocal(delta));
	}

	/**
	 * Finds the highest point (maximum Y-coordinate) among the vertices of the
	 * mesh.
	 *
	 * @return the Y-coordinate of the highest point
	 */
	private float findHighestPoint() {
		float max = mesh.getVertexAt(0).y;
		for (Vector3f vertex : mesh.getVertices()) {
			max = vertex.y > max ? vertex.y : max;
		}
		return max;
	}

	/**
	 * Sets the mesh to be modified.
	 *
	 * @param mesh the mesh to set
	 */
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	/**
	 * Gets the ground level that the mesh is snapped to.
	 *
	 * @return the ground level
	 */
	public float getGroundLevel() {
		return groundLevel;
	}

	public void setGroundLevel(float groundLevel) {
		this.groundLevel = groundLevel;
	}

}
