package engine.components;

import engine.scene.SceneNode;
import math.Mathf;

/**
 * A simple animation component that makes the owner SceneNode move in circular
 * motion over the XZ plane.
 * <p>
 * This component calculates circular motion based on a defined radius and
 * angular speed, updating the position of the owning SceneNode in real-time.
 * The motion occurs on the horizontal plane (XZ plane) while keeping the
 * Y-coordinate constant.
 * </p>
 * <p>
 * Properties:
 * <ul>
 * <li><b>radius:</b> The distance from the center point of the circular path to
 * the moving node along the X and Z axes.</li>
 * <li><b>angularSpeed:</b> The speed of rotation defined in radians per
 * second.</li>
 * <li><b>timeElapsed:</b> A counter tracking the elapsed time to calculate
 * motion over time.</li>
 * </ul>
 * </p>
 * <p>
 * This component updates the position of the owning SceneNode every frame,
 * creating smooth circular motion over time.
 * </p>
 * 
 * @see SceneNode
 * @see Transform
 */
public class CircularAnimationComponent extends AbstractComponent {

	/**
	 * Radius of circular motion. Determines how far from the center the object
	 * orbits.
	 */
	private float radius;

	/**
	 * Angular speed in radians per second that determines how fast the object
	 * orbits.
	 */
	private float angularSpeed;

	/** Tracks the total elapsed time since the start of the animation. */
	private float timeElapsed;

	/**
	 * Default constructor that initializes with default values: - radius = 30.0f
	 * - angularSpeed = 1.0f
	 */
	public CircularAnimationComponent() {
		this.radius = 30.0f;
		this.angularSpeed = 1.0f;
		this.timeElapsed = 0.0f;
	}

	/**
	 * Creates a CircularAnimationComponent with specified radius and angular
	 * speed.
	 * 
	 * @param radius       The distance from the center of circular motion to the
	 *                     object.
	 * @param angularSpeed The speed at which the object orbits, in radians per
	 *                     second.
	 */
	public CircularAnimationComponent(float radius, float angularSpeed) {
		this.radius = radius;
		this.angularSpeed = angularSpeed;
	}

	/**
	 * Updates the animation logic each frame.
	 * <p>
	 * This method calculates the new position of the owning SceneNode in a
	 * circular path using trigonometric functions. The position is updated on the
	 * XZ plane with constant Y-coordinate to simulate planar circular motion.
	 * </p>
	 * 
	 * @param tpf Time per frame (time in seconds since the last frame).
	 */
	@Override
	public void update(float tpf) {
		// Update the elapsed time based on the time per frame
		timeElapsed += tpf;

		// Calculate the new X and Z positions using circular motion formulas
		float x = radius * Mathf.cos(angularSpeed * timeElapsed);
		float z = radius * Mathf.sin(angularSpeed * timeElapsed);

		// Retrieve the transform of the owning SceneNode
		SceneNode node = getOwner();
		Transform transform = node.getTransform();

		// Set the new position while maintaining the current Y-coordinate
		transform.setPosition(x, transform.getPosition().y, z);
	}

	@Override
	public void onAttach() {
	}

	@Override
	public void onDetach() {
	}

}