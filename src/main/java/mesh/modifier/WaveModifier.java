package mesh.modifier;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;

/**
 * The {@code WaveModifier} class applies a wave-like deformation to a 3D mesh,
 * simulating the appearance of sinusoidal waves across the surface of the
 * mesh.The wave modifier is particularly useful for simulating effects like
 * water surfaces, undulating terrain, or other wave-like phenomena in computer
 * graphics. This modifier operates by displacing the vertices of a given mesh
 * based on a mathematical wave function:
 * 
 * <pre>
 * y = A ⋅ sin((2π / λ) ⋅ (D ⋅ P) + ϕ)
 * 
 * - A (Amplitude): Controls the height of the wave peaks.
 * - λ (Wavelength): Determines the distance between consecutive wave peaks.
 * - D (Direction): A normalized vector representing the direction of
 *     wave propagation.
 * - P (Vertex Position): The position of each vertex in 3D space.
 * - ϕ (Phase): An optional offset used to animate or shift the wave
 *     over time.
 *   
 * Usage:
 * 
 * Mesh3D mesh = new Mesh3D();
 * WaveModifier modifier = new WaveModifier();
 * modifier.setAmplitude(2.0f);
 * modifier.setWavelength(5.0f);
 * modifier.setPhase(1.0f);
 * modifier.setDirection(new Vector3f(1, 0, 0));
 * modifier.modify(mesh);
 * </pre>
 * 
 * @see Mesh3D
 * @see IMeshModifier
 */
public class WaveModifier implements IMeshModifier {

	/**
	 * Height of the wave peaks.
	 */
	private float amplitude;

	/**
	 * Distance between wave peaks.
	 */
	private float wavelength;

	/**
	 * Phase offset for animation.
	 */
	private float phase;

	/**
	 * Direction of the wave.
	 */
	private Vector3f direction;

	/**
	 * The mesh to be modified.
	 */
	private Mesh3D mesh;

	/**
	 * Constructs a new {@link WaveModifier} with default values for the wave effect
	 * parameters. The default values are:
	 * 
	 * <pre>
	 * Amplitude: 1
	 * Wavelength: 1
	 * Phase: 0
	 * Direction: (1, 0, 0)
	 * </pre>
	 * 
	 * These default settings generate a basic wave along the X-axis. Users can
	 * modify these properties using the corresponding setter methods after
	 * instantiation.
	 */
	public WaveModifier() {
		setAmplitude(1);
		setWavelength(1);
		setDirection(new Vector3f(1, 0, 0));
	}

	/**
	 * Applies the wave effect to the provided mesh by modifying its vertices by
	 * using the wave function defined in the {@link WaveModifier}.
	 * 
	 * @param mesh The {@link Mesh3D} instance to be modified. This mesh must not be
	 *             null.
	 * @return The modified {@link Mesh3D} with the wave effect applied to its
	 *         vertices.
	 * @throws IllegalArgumentException If the provided mesh is null.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh must not be null.");
		}
		setMesh(mesh);
		applyWaveToVertices();
		return mesh;
	}

	/**
	 * Applies the wave deformation to all vertices of the mesh. This method does
	 * not return a value; it directly modifies the vertex positions of the mesh in
	 * place.
	 */
	private void applyWaveToVertices() {
		mesh.vertices.parallelStream().forEach(this::applyWaveToVertex);
	}

	/**
	 * Applies a wave deformation to a single vertex based on a sinusoidal wave
	 * equation.
	 * 
	 * <pre>
	 * The wave function is defined as:
	 *  
	 * y = A ⋅ sin((2π / λ) ⋅ (D ⋅ P) + ϕ)
	 * 
	 * where:
	 * 
	 * A: Amplitude - the height of the wave peaks.
	 * λ: Wavelength - the distance between consecutive wave peaks.
	 * D: Direction - a unit vector representing the direction of wave
	 * propagation.
	 * <P: Vertex position - the position of the vertex in 3D space.
	 * ϕ: Phase - an offset used to animate or shift the wave over time.
	 * 
	 * The vertex's position is projected onto the wave's direction, and 
	 * the wave offset is calculated and added to the y-coordinate,
	 * simulating the effect of a wave.
	 * </pre>
	 * 
	 * @param vertex The 3D vertex to which the wave deformation is applied. The
	 *               vertex's position is directly modified in place.
	 * @throws NullPointerException if the vertex is null.
	 */
	private void applyWaveToVertex(Vector3f vertex) {
		// Project the vertex position onto the wave direction
		float projection = vertex.dot(direction);

		// Calculate the wave offset using the wave function
		float waveOffset = amplitude * Mathf.sin((2 * Mathf.PI / wavelength) * projection + phase);

		// Apply the offset to the y-coordinate (height)
		vertex.y += waveOffset;
	}

	/**
	 * Sets the mesh that will be modified by the wave effect.
	 * 
	 * @param mesh The {@link Mesh3D} instance to modify. Must not be null.
	 */
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	/**
	 * Retrieves the amplitude of the wave.
	 * 
	 * @return The amplitude, representing the height of the wave peaks.
	 */
	public float getAmplitude() {
		return amplitude;
	}

	/**
	 * Sets the amplitude of the wave.
	 * 
	 * @param amplitude The height of the wave peaks. Must be non-negative.
	 * @throws IllegalArgumentException if the amplitude is negative.
	 */
	public void setAmplitude(float amplitude) {
		if (amplitude < 0) {
			throw new IllegalArgumentException("Amplitude must be non-negative.");
		}
		this.amplitude = amplitude;
	}

	/**
	 * Retrieves the wavelength of the wave.
	 * 
	 * @return The wavelength, representing the distance between consecutive wave
	 *         peaks.
	 */
	public float getWavelength() {
		return wavelength;
	}

	/**
	 * Sets the wavelength of the wave.
	 * 
	 * @param wavelength The distance between consecutive wave peaks. Must be
	 *                   positive.
	 * @throws IllegalArgumentException if the wavelength is less than or equal to
	 *                                  zero.
	 */
	public void setWavelength(float wavelength) {
		if (wavelength <= 0) {
			throw new IllegalArgumentException("Wavelength must be positive.");
		}
		this.wavelength = wavelength;
	}

	/**
	 * Retrieves the current phase offset of the wave.
	 * 
	 * @return The phase offset, used to animate or shift the wave.
	 */
	public float getPhase() {
		return phase;
	}

	/**
	 * Sets the phase offset for the wave.
	 * 
	 * @param phase The phase offset, typically used for animation.
	 */
	public void setPhase(float phase) {
		this.phase = phase;
	}

	/**
	 * Retrieves the direction of the wave.
	 * 
	 * @return A {@link Vector3f} representing the unit vector of the wave's
	 *         direction.
	 */
	public Vector3f getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the wave. The direction vector is normalized
	 * automatically.
	 * 
	 * @param direction A {@link Vector3f} representing the direction of wave
	 *                  propagation. Must not be null or a zero vector.
	 * @throws IllegalArgumentException if the direction is null or a zero vector.
	 */
	public void setDirection(Vector3f direction) {
		if (direction == null || direction.equals(Vector3f.ZERO)) {
			throw new IllegalArgumentException("Direction must not be null or zero.");
		}
		this.direction = direction.normalize();
	}

}