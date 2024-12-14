package scene.light;

/**
 * Enum representing different types of lights.
 *
 * This enum defines the three primary types of lights commonly used in 3D
 * graphics:
 *
 * <pre>
 * - POINT: A point light emits light uniformly in all directions.
 * - DIRECTIONAL: A directional light emits light in parallel rays from 
 *   a specific direction.
 * - SPOT: A spotlight emits light in a cone shape, with a specific 
 *   direction and angle.
 * </pre>
 */
public enum LightType {

	POINT, DIRECTIONAL, SPOT

}
