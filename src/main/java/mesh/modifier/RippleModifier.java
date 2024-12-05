package mesh.modifier;

import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.VertexNormals;

/**
 * The RippleModifier applies a ripple effect to a 3D mesh by displacing
 * vertices along their normals based on sinusoidal wave functions. This effect
 * is inspired by the ripple modifier found in Autodesk 3ds Max, simulating
 * water-like wave patterns on a mesh surface.
 * 
 * <pre>
 * **Key Parameters:**
 * - **time:** Controls the temporal evolution of the ripple.
 * - **amplitude1, amplitude2:** Determine the intensity of the primary and secondary waves.
 * - **wavelength:** Sets the distance between wave peaks.
 * - **phaseShift:** Shifts the phase of the wave pattern.
 * - **decayFactor:** Controls the attenuation of waves over time.
 * - **center:** Defines the origin of the ripple.
 * </pre>
 * 
 * By adjusting these parameters, you can create various ripple effects, from
 * gentle undulations to intense turbulence.
 */
public class RippleModifier implements IMeshModifier {

	/**
	 * Represents the time progression of the ripple. Higher values simulate wave
	 * movement over time.
	 */
	private float time;

	/**
	 * Amplitude of the primary sine wave.
	 */
	private float amplitude1;

	/**
	 * Amplitude of the secondary cosine wave.
	 */
	private float amplitude2;

	/**
	 * Distance between successive wave peaks. Smaller values create denser ripples.
	 */
	private float waveLength;

	/**
	 * The wave number of the ripple, which determines the frequency of the
	 * sinusoidal wave used to calculate vertex displacement.
	 */
	private float waveNumber;

	/**
	 * Shifts the phase of the wave pattern, controlling where waves begin.
	 */
	private float phaseShift;

	/**
	 * Controls the attenuation of waves over time.
	 */
	private float decayFactor;

	/**
	 * Sets the origin of the ripple. Vertices closer to this point experience
	 * stronger displacement.
	 */
	private Vector3f center;

	/**
	 * The mesh to which the ripple effect is applied.
	 */
	private Mesh3D mesh;

	/**
	 * List of vertex normals used to calculate displacement direction.
	 */
	private List<Vector3f> vertexNormals;

	public RippleModifier() {
		amplitude1 = 1.0f;
		amplitude2 = 0.5f;
		waveLength = 5.0f;
		decayFactor = 0.1f;
		center = new Vector3f(0, 0, 0);
	}

	/**
	 * Modifies the given mesh by applying the ripple effect. This method displaces
	 * vertices based on their distance from the center, the vertex normals, and the
	 * configured parameters such as amplitude, wave length, and decay factor.
	 *
	 * @param mesh the 3D mesh to modify (must not be null)
	 * @return the modified mesh
	 * @throws IllegalArgumentException if the mesh is null
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh must not be null.");
		}
		setMesh(mesh);
		initializeWaveNumber();
		calculateVertexNormals();
		applyRippleToVertices();
		return mesh;
	}

	/**
	 * Applies the ripple effect to all vertices of the mesh. This method iterates
	 * through each vertex and modifies its position based on the ripple effect
	 * formula.
	 */
	private void applyRippleToVertices() {
		for (int index = 0; index < mesh.getVertexCount(); index++)
			applyRippleToVertexAt(index);
	}

	/**
	 * Applies the ripple effect to a specific vertex, identified by its index.
	 *
	 * @param index the index of the vertex to modify
	 */
	private void applyRippleToVertexAt(int index) {
		Vector3f vertex = mesh.getVertexAt(index);
		Vector3f surfaceNormal = getVertexNormalAt(index).normalize();

		float wavePhaseInput = calculateWavePhaseInput(vertex);
		float wave1 = Mathf.sin(wavePhaseInput);
		float wave2 = Mathf.cos(wavePhaseInput);

		float displacement = amplitude1 * wave1 + amplitude2 * wave2;

		vertex.addLocal(surfaceNormal.mult(displacement));
	}

	/**
	 * Computes the wave phase input based on the vertex's distance from the center
	 * and the configured ripple parameters.
	 *
	 * @param vertex the vertex for which to calculate the wave phase input
	 * @return the wave phase input, incorporating distance, phase shift, and decay
	 *         factor
	 */
	private float calculateWavePhaseInput(Vector3f vertex) {
		float distanceToCenter = vertex.distance(center);
		float wavePhaseInput = waveNumber * distanceToCenter - phaseShift - decayFactor * time;
		return wavePhaseInput;
	}

	/**
	 * Calculates vertex normals for the mesh. These normals are used to displace
	 * vertices along their respective directions during the ripple effect
	 * application.
	 */
	private void calculateVertexNormals() {
		vertexNormals = new VertexNormals(mesh).getVertexNormals();
	}

	/**
	 * Calculates the wave number based on the provided wavelength. The wave number
	 * is used to determine the frequency of the sinusoidal wave function that
	 * drives the ripple effect.
	 */
	private void initializeWaveNumber() {
		waveNumber = Mathf.TWO_PI / waveLength;
	}

	private Vector3f getVertexNormalAt(int index) {
		return vertexNormals.get(index);
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public float getTime() {
		return time;
	}

	/**
	 * Sets the time progression of the ripple. This value controls the temporal
	 * evolution of the ripple effect.
	 *
	 * @param time the time value, typically non-negative
	 */
	public void setTime(float time) {
		this.time = time;
	}

	public float getAmplitude1() {
		return amplitude1;
	}

	public void setAmplitude1(float amplitude1) {
		this.amplitude1 = amplitude1;
	}

	public float getAmplitude2() {
		return amplitude2;
	}

	public void setAmplitude2(float amplitude2) {
		this.amplitude2 = amplitude2;
	}

	public float getWaveLength() {
		return waveLength;
	}

	/**
	 * Sets the wave length, which is the distance between successive wave peaks.
	 *
	 * @param waveLength the wave length (must be greater than 0)
	 * @throws IllegalArgumentException if waveLength is less than or equal to 0
	 */
	public void setWaveLength(float waveLength) {
		if (waveLength <= 0) {
			throw new IllegalArgumentException("Wave length must be greater than 0.");
		}
		this.waveLength = waveLength;
	}

	public float getPhaseShift() {
		return phaseShift;
	}

	public void setPhaseShift(float phaseShift) {
		this.phaseShift = phaseShift;
	}

	public float getDecayFactor() {
		return decayFactor;
	}

	public void setDecayFactor(float decayFactor) {
		this.decayFactor = decayFactor;
	}

	public Vector3f getCenter() {
		return center;
	}

	/**
	 * Sets the center point of the ripple effect. Vertices closer to this point
	 * experience a stronger displacement due to the ripple.
	 * 
	 * @param center the new center point (must not be null)
	 * @throws IllegalArgumentException if the provided center is null
	 */
	public void setCenter(Vector3f center) {
		if (center == null) {
			throw new IllegalArgumentException("Center must not be null.");
		}
		this.center = center;
	}

}