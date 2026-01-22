package engine.particles;

import math.Vector3f;

/**
 * Represents a single particle with physical properties and lifecycle tracking. This class models
 * particles' motion using physics principles like velocity, acceleration, damping (drag), and
 * lifetime constraints.
 *
 * <p>Designed to support effects like trails, smoke, explosions, or other particle effects by
 * integrating old and new positions for rendering purposes.
 *
 * @author Simon Dietz
 */
public class Particle {

  /** The current position of the particle in world-space coordinates. */
  private Vector3f position;

  /** The previous position of the particle; used for effects like trails or motion blur. */
  private Vector3f oldPosition;

  /** The velocity vector of the particle, representing how it moves over time. */
  private Vector3f velocity;

  /** The acceleration vector affecting the particle's motion. */
  private Vector3f acceleration;

  /** Total duration (in seconds) for which the particle will live. */
  private float lifetime;

  /** Tracks how much time has elapsed since the particle was created. */
  private float elapsedTime;

  /** The damping factor simulates air resistance or drag on the particle. */
  private float dampingFactor = 0.98f;

  /**
   * Constructs a new particle with the specified initial position, velocity, acceleration, and
   * lifetime. Initializes the old position to match the initial position.
   *
   * @param position Initial position of the particle in 3D space.
   * @param velocity Initial velocity vector of the particle.
   * @param acceleration Acceleration vector affecting the particle.
   * @param lifetime Total time (seconds) this particle will live.
   */
  public Particle(Vector3f position, Vector3f velocity, Vector3f acceleration, float lifetime) {
    this.position = new Vector3f(position);
    this.oldPosition = new Vector3f(position);
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.lifetime = lifetime;
    this.elapsedTime = 0f;
  }

  /**
   * Applies a force to the particle by adding it to the acceleration vector. Useful for simulating
   * environmental effects like wind or gravity.
   *
   * <p>Assumes that mass is constant and equals 1 for simplicity.
   *
   * @param force The force vector to apply to the particle's acceleration.
   */
  public void applyForce(Vector3f force) {
    acceleration.addLocal(force);
  }

  /**
   * Applies damping to simulate drag or resistance, slowing down the particle's motion over time.
   */
  public void applyDamping() {
    velocity.multLocal(dampingFactor);
  }

  /**
   * Updates the particle's position, velocity, and applies damping effects over a given time step.
   * Resets acceleration after each update to ensure isolated force application each frame.
   *
   * @param deltaTime The time elapsed since the last frame (in seconds).
   */
  public void update(float deltaTime) {
    elapsedTime += deltaTime;

    oldPosition.set(position);

    // Apply physics: velocity changes due to acceleration
    velocity.addLocal(acceleration.mult(deltaTime));

    // Apply environmental drag/damping
    applyDamping();

    // Update position based on the new velocity
    position.addLocal(velocity.mult(deltaTime));

    // Reset acceleration for the next simulation step
    acceleration.set(0, 0, 0);
  }

  /**
   * Checks if the particle is still alive (i.e., has not exceeded its lifetime).
   *
   * @return {@code true} if the particle's elapsed time is less than its total lifetime, otherwise
   *     {@code false}.
   */
  public boolean isAlive() {
    return elapsedTime < lifetime;
  }

  /**
   * Gets the current position of the particle in 3D space.
   *
   * @return The current position vector of the particle.
   */
  public Vector3f getPosition() {
    return position;
  }

  /**
   * Gets the previous position of the particle. Useful for rendering trails or other visual
   * effects.
   *
   * @return The old position vector of the particle.
   */
  public Vector3f getOldPosition() {
    return oldPosition;
  }

  /**
   * Gets the amount of time that has elapsed since the particle was created.
   *
   * @return The elapsed time in seconds.
   */
  public float getElapsedTime() {
    return elapsedTime;
  }

  /**
   * Gets the total lifetime of the particle.
   *
   * @return The total lifetime of the particle in seconds.
   */
  public float getLifetime() {
    return lifetime;
  }

  /**
   * Sets the particle's lifetime to a new value. This can extend or shorten how long the particle
   * will exist.
   *
   * @param lifetime New lifetime value in seconds.
   */
  public void setLifetime(float lifetime) {
    this.lifetime = lifetime;
  }
}
