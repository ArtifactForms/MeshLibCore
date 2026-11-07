package mesh.modifier.deform;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * The RippleModifier applies a ripple effect to a 3D mesh by displacing vertices along a specified
 * direction based on sinusoidal wave functions. This effect is inspired by the ripple modifier
 * found in Autodesk 3ds Max, simulating water-like wave patterns on a mesh surface.
 *
 * <pre>
 * Key Parameters:
 * - time: Controls the temporal evolution of the ripple.
 * - amplitude1, amplitude2: Determine the intensity of the primary and
 *   secondary waves.
 * - wavelength: Sets the distance between wave peaks.
 * - phaseShift: Shifts the phase of the wave pattern.
 * - decayFactor: Controls the attenuation of waves over time.
 * - center: Defines the origin of the ripple.
 * </pre>
 *
 * By adjusting these parameters, you can create various ripple effects, from gentle undulations to
 * intense turbulence.
 */
public class RippleModifier implements IMeshModifier {

  /**
   * Represents the time progression of the ripple. Higher values simulate wave movement over time.
   */
  private float time;

  /** Amplitude of the primary sine wave. */
  private float amplitude1;

  /** Amplitude of the secondary cosine wave. */
  private float amplitude2;

  /** Distance between successive wave peaks. Smaller values create denser ripples. */
  private float waveLength;

  /**
   * The wave number of the ripple, which determines the frequency of the sinusoidal wave used to
   * calculate vertex displacement.
   */
  private float waveNumber;

  /** Shifts the phase of the wave pattern, controlling where waves begin. */
  private float phaseShift;

  /** Controls the attenuation of waves over time. */
  private float decayFactor;

  /**
   * Sets the origin of the ripple. Vertices closer to this point experience stronger displacement.
   */
  private Vector3f center;

  /** Defines the direction in which the ripple effect displaces vertices in the mesh. */
  private Vector3f direction;

  /**
   * Constructs a new {@link RippleModifier} with default values for the ripple effect parameters.
   * The default values are:
   *
   * <pre>
   * Amplitude1: 1.0f
   * Amplitude2: 0.5f
   * Wave Length: 5.0f
   * Decay Factor: 0.1f
   * Center: (0, 0, 0)
   * Direction: (0, -1, 0)
   * </pre>
   *
   * These defaults create a gentle ripple effect that propagates downward in the Y-axis direction.
   */
  public RippleModifier() {
    amplitude1 = 1.0f;
    amplitude2 = 0.5f;
    waveLength = 5.0f;
    decayFactor = 0.1f;
    center = new Vector3f(0, 0, 0);
    direction = new Vector3f(0, -1, 0);
  }

  /**
   * Modifies the given mesh by applying the ripple effect. This method displaces vertices based on
   * their distance from the center, the vertex normals, and the configured parameters such as
   * amplitude, wave length, and decay factor.
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
    calculateWaveNumber();
    applyRippleToVertices(mesh);
    return mesh;
  }

  /** Applies the ripple effect formula to all vertices of the mesh. */
  private void applyRippleToVertices(Mesh mesh) {
    Vector3f normalizedDirection = direction.normalize();
    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v = mesh.getVertexAt(i);
      applyRippleToVertex(v, normalizedDirection);
    }
  }

  /**
   * Applies the ripple effect to a specific vertex.
   *
   * @param vertex to modify
   */
  private void applyRippleToVertex(Vector3f vertex, Vector3f normalizedDirection) {
    float wavePhaseInput = calculateWavePhaseInput(vertex);
    float wave1 = Mathf.sin(wavePhaseInput);
    float wave2 = Mathf.cos(wavePhaseInput);
    float displacement = amplitude1 * wave1 + amplitude2 * wave2;

    vertex.addLocal(normalizedDirection.mult(displacement));
  }

  /**
   * Computes the wave phase input based on the vertex's distance from the center and the configured
   * ripple parameters.
   *
   * @param vertex the vertex for which to calculate the wave phase input
   * @return the wave phase input, incorporating distance, phase shift, and decay factor
   */
  private float calculateWavePhaseInput(Vector3f vertex) {
    float distanceToCenter = vertex.distance(center);
    float wavePhaseInput = waveNumber * distanceToCenter - phaseShift - decayFactor * time;
    return wavePhaseInput;
  }

  /**
   * Calculates the wave number based on the provided wavelength. The wave number is used to
   * determine the frequency of the sinusoidal wave function that drives the ripple effect.
   */
  private void calculateWaveNumber() {
    waveNumber = Mathf.TWO_PI / waveLength;
  }

  /**
   * Retrieves the time parameter of the ripple effect. The time parameter influences the temporal
   * evolution of the ripple, simulating wave movement over time.
   *
   * @return the current time value controlling the ripple's progression.
   */
  public float getTime() {
    return time;
  }

  /**
   * Sets the time progression of the ripple. This value controls the temporal evolution of the
   * ripple effect.
   *
   * @param time the time value, typically non-negative
   */
  public void setTime(float time) {
    this.time = time;
  }

  /**
   * Retrieves the amplitude of the primary sine wave. The amplitude controls the intensity of the
   * primary wave's displacement effect.
   *
   * @return the amplitude of the primary wave.
   */
  public float getAmplitude1() {
    return amplitude1;
  }

  /**
   * Sets the amplitude of the primary sine wave. The amplitude determines the magnitude of the
   * primary wave's effect on the mesh.
   *
   * @param amplitude1 the amplitude of the primary wave (must be greater or equal to 0).
   * @throws IllegalArgumentException if amplitude1 is less than 0
   */
  public void setAmplitude1(float amplitude1) {
    if (amplitude1 < 0) {
      throw new IllegalArgumentException("Aplitude1 must be greater or equal to 0.");
    }
    this.amplitude1 = amplitude1;
  }

  /**
   * Retrieves the amplitude of the secondary cosine wave. The amplitude controls the intensity of
   * the secondary wave's displacement effect.
   *
   * @return the amplitude of the secondary wave.
   */
  public float getAmplitude2() {
    return amplitude2;
  }

  /**
   * Sets the amplitude of the secondary cosine wave. The amplitude determines the magnitude of the
   * secondary wave's effect on the mesh.
   *
   * @param amplitude2 the amplitude of the secondary wave (must be greater or equal to 0).
   * @throws IllegalArgumentException if amplitude2 is less than 0
   */
  public void setAmplitude2(float amplitude2) {
    if (amplitude2 < 0) {
      throw new IllegalArgumentException("Aplitude2 must be greater or equal to 0.");
    }
    this.amplitude2 = amplitude2;
  }

  /**
   * Retrieves the wavelength of the ripple effect. The wavelength determines the distance between
   * successive wave peaks.
   *
   * @return the current wavelength of the ripple effect.
   */
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

  /**
   * Retrieves the phase shift of the ripple effect. The phase shift determines where the wave
   * pattern starts, effectively shifting the ripple along its progression.
   *
   * @return the current phase shift value.
   */
  public float getPhaseShift() {
    return phaseShift;
  }

  /**
   * Sets the phase shift of the ripple effect, wrapping it to the range [0, 2π]. This ensures that
   * the phase shift stays within a valid range to avoid unexpected behaviors. Adjusting this value
   * changes the starting point of the wave pattern, allowing you to offset the ripple phase.
   *
   * @param phaseShift the new phase shift value.
   */
  public void setPhaseShift(float phaseShift) {
    // Step 1: Wrap phaseShift to the range [-2π, 2π) using fmod
    this.phaseShift = Mathf.fmod(phaseShift, Mathf.TWO_PI);

    // Step 2: If the result is negative, add TWO_PI to bring it into [0, 2π)
    if (this.phaseShift < 0) {
      this.phaseShift += Mathf.TWO_PI;
    }

    // Step 3: Special case: if phaseShift is exactly TWO_PI (i.e., full cycle),
    // set
    // it to 0
    if (Math.abs(this.phaseShift - Mathf.TWO_PI) < 0.001) {
      this.phaseShift = 0.0f;
    }
  }

  /**
   * Retrieves the decay factor of the ripple effect. The decay factor controls how the amplitude of
   * the ripple decreases as it propagates outward from the center.
   *
   * @return the current decay factor value.
   */
  public float getDecayFactor() {
    return decayFactor;
  }

  /**
   * Sets the decay factor of the ripple effect. Higher values result in faster attenuation of the
   * ripple's amplitude over distance, while lower values allow the ripple to sustain longer.
   *
   * @param decayFactor the new decay factor value.
   */
  public void setDecayFactor(float decayFactor) {
    this.decayFactor = decayFactor;
  }

  /**
   * Retrieves the center point of the ripple effect as a copy.
   *
   * <p>The returned vector is independent of the internally stored center. Modifying it does not
   * affect the ripple modifier's configuration.
   *
   * @return a copy of the current center point
   * @see #getCenter(Vector3f)
   * @see #setCenter(Vector3f)
   */
  public Vector3f getCenter() {
    return new Vector3f(center);
  }

  /**
   * Retrieves the center point of the ripple effect and stores it in the provided vector.
   *
   * <p>This method allows the caller to reuse an existing {@link Vector3f} instance and avoid
   * creating a new object. The supplied vector is overwritten with the current center coordinates.
   *
   * @param store the vector in which to store the center point (must not be null)
   * @return the supplied store vector containing the center coordinates
   * @throws IllegalArgumentException if store is null
   * @see #getCenter()
   */
  public Vector3f getCenter(Vector3f store) {
    if (store == null) {
      throw new IllegalArgumentException("Store must not be null.");
    }

    store.set(center);
    return store;
  }

  /**
   * Sets the center point of the ripple effect. Vertices closer to this point experience a stronger
   * displacement due to the ripple.
   *
   * @param center the new center point (must not be null)
   * @throws IllegalArgumentException if the provided center is null
   */
  public void setCenter(Vector3f center) {
    if (center == null) {
      throw new IllegalArgumentException("Center must not be null.");
    }
    this.center = new Vector3f(center);
  }

  /**
   * Retrieves the direction of the ripple displacement as a copy.
   *
   * <p>The direction determines the axis along which vertices are displaced when the ripple effect
   * is applied. The returned vector is independent of the internally stored direction. Modifying it
   * does not affect the ripple modifier's configuration.
   *
   * @return a copy of the current displacement direction
   * @see #getDirection(Vector3f)
   * @see #setDirection(Vector3f)
   */
  public Vector3f getDirection() {
    return new Vector3f(direction);
  }

  /**
   * Retrieves the direction of the ripple displacement and stores it in the provided vector.
   *
   * <p>This method allows the caller to reuse an existing {@link Vector3f} instance and avoid
   * creating a new object. The supplied vector is overwritten with the current direction.
   *
   * @param store the vector in which to store the direction (must not be null)
   * @return the supplied store vector containing the direction
   * @throws IllegalArgumentException if store is null
   * @see #getDirection()
   */
  public Vector3f getDirection(Vector3f store) {
    if (store == null) {
      throw new IllegalArgumentException("Store must not be null.");
    }

    store.set(direction);
    return store;
  }

  /**
   * Sets the direction of the ripple displacement. The direction vector determines the axis along
   * which the ripple effect displaces vertices in the 3D mesh. It defines the direction of wave
   * propagation, and vertices are displaced in this direction based on the calculated ripple
   * effect. The direction vector is normalized automatically when set to ensure consistent scaling
   * of the displacement.
   *
   * <p>By default, the direction is set to (0, -1, 0), which means the ripple displaces vertices
   * along the negative Y-axis.
   *
   * @param direction the new direction vector (must not be null).
   * @throws IllegalArgumentException if the provided direction is null.
   */
  public void setDirection(Vector3f direction) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction must not be null.");
    }
    this.direction = direction.normalize();
  }
}
