package engine.components;

import engine.scene.SceneNode;
import math.Vector3f;

/**
 * A simple rotation component that rotates the owning SceneNode around a
 * specified axis at a constant angular speed.
 * 
 * <p>
 * Properties:
 * <ul>
 * <li><b>axis:</b> The axis around which the rotation occurs.</li>
 * <li><b>angularSpeed:</b> The speed of rotation in radians per second.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The rotation is applied incrementally to the Transform component of the
 * owning SceneNode during each frame update.
 * </p>
 */
public class RotationComponent extends AbstractComponent {

	/** The axis around which the node will rotate. */
	private Vector3f axis;

	/** The angular speed of the rotation in radians per second. */
	private float angularSpeed;

	/**
	 * Constructs a RotationComponent with default axis (0, 1, 0) and angular
	 * speed. Default settings will rotate around the Y-axis.
	 */
	public RotationComponent() {
		this.axis = new Vector3f(0, 1, 0); // Default rotation axis: Y-axis
		this.angularSpeed = 1.0f; // Default angular speed: 1 radian/second
	}

	/**
	 * Constructs a RotationComponent with a specified axis and angular speed.
	 * 
	 * @param axis         The axis of rotation (must not be null).
	 * @param angularSpeed The angular speed in radians per second.
	 */
	public RotationComponent(Vector3f axis, float angularSpeed) {
		if (axis == null || axis.length() == 0) {
			throw new IllegalArgumentException(
			    "Rotation axis cannot be null or zero-length.");
		}
		this.axis = axis.normalize(); // Normalize the axis to ensure proper
		                              // rotation
		this.angularSpeed = angularSpeed;
	}

	/**
	 * Updates the rotation logic each frame.
	 * 
	 * @param tpf Time per frame (in seconds since the last frame).
	 */
	@Override
	public void update(float tpf) {
		SceneNode node = getOwner();
		if (node == null)
			return;

		// Calculate the incremental rotation angle
		float angleIncrement = angularSpeed * tpf;

		// Apply rotation to the owning SceneNode's Transform
		node.getTransform().rotate(axis.mult(angleIncrement));
	}

	/**
	 * Sets a new rotation axis for the component.
	 * 
	 * @param axis The new axis of rotation.
	 */
	public void setAxis(Vector3f axis) {
		if (axis == null || axis.length() == 0) {
			throw new IllegalArgumentException(
			    "Rotation axis cannot be null or zero-length.");
		}
		this.axis = axis.normalize();
	}

	/**
	 * Sets the angular speed of the rotation.
	 * 
	 * @param angularSpeed The new angular speed in radians per second.
	 */
	public void setAngularSpeed(float angularSpeed) {
		this.angularSpeed = angularSpeed;
	}

	/**
	 * Gets the current rotation axis.
	 * 
	 * @return The axis of rotation.
	 */
	public Vector3f getAxis() {
		return axis;
	}

	/**
	 * Gets the current angular speed.
	 * 
	 * @return The angular speed in radians per second.
	 */
	public float getAngularSpeed() {
		return angularSpeed;
	}

	@Override
	public void initialize() {
		// Initialization logic if needed
	}

	@Override
	public void cleanup() {
		// Cleanup logic if needed
	}
	
}