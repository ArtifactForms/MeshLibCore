package engine.render.effects;

import java.util.concurrent.ConcurrentLinkedQueue;

import math.Vector3f;

/**
 * Represents a particle emitter responsible for generating and managing
 * particles based on defined parameters like velocity, acceleration, and
 * lifetime ranges. This class supports both continuous particle emission and
 * burst-based emission modes.
 * <p>
 * The emitter allows for dynamic configuration of properties such as initial
 * velocity ranges, acceleration ranges, particle lifetime ranges, and the
 * emission rate. It uses a thread-safe queue to manage particles for efficient
 * access and updates.
 * </p>
 * 
 * <p>
 * <b>Key Features:</b>
 * </p>
 * <ul>
 * <li>Supports continuous and burst particle emission modes.</li>
 * <li>Randomizes particle properties like initial velocity, acceleration, and
 * lifetime within defined ranges.</li>
 * <li>Handles particle cleanup by removing expired particles from the
 * queue.</li>
 * </ul>
 * 
 * @author Simon Dietz
 */
public class ParticleEmitter {

	/** The world-space origin of the particle emitter. */
	private Vector3f position;

	/** The range for randomizing initial particle velocities. */
	private Vector3f velocityRange;

	/** The range for randomizing initial particle accelerations. */
	private Vector3f accelerationRange;

	/** The range of possible particle lifetimes. */
	private float lifetimeRange;

	/** The rate at which particles are emitted (particles per second). */
	private int particlesPerSecond;

	/** Whether the emitter is currently configured for burst emission mode. */
	private boolean burstMode;

	/** Number of particles to emit during each burst. */
	private int burstCount;

	/**
	 * Tracks elapsed time to determine when particles should be emitted during
	 * continuous mode.
	 */
	private float timeSinceLastEmission = 0f;

	/** A thread-safe queue storing active particles. */
	private ConcurrentLinkedQueue<Particle> particles;

	/**
	 * Constructs a new ParticleEmitter with a specified position and emission
	 * rate.
	 * 
	 * @param position           The initial world-space position of the emitter.
	 * @param particlesPerSecond The rate at which particles are emitted in
	 *                           continuous mode (particles per second).
	 */
	public ParticleEmitter(Vector3f position, int particlesPerSecond) {
		this.position = position;
		this.particlesPerSecond = particlesPerSecond;
		this.velocityRange = new Vector3f(1f, 1f, 1f);
		this.accelerationRange = new Vector3f(0f, 0f, 0f);
		this.lifetimeRange = 5f; // Default particle lifetime of 5 seconds
		this.particles = new ConcurrentLinkedQueue<>();
		this.burstMode = false; // Default mode is continuous particle emission
		this.burstCount = 0;
	}

	/**
	 * Updates particles and performs emission logic based on elapsed time.
	 * Handles both continuous emission and burst emission logic. Cleans up
	 * expired particles from the particle queue.
	 * 
	 * @param deltaTime Time elapsed since the last frame, in seconds.
	 */
	public void update(float deltaTime) {
		if (burstMode) {
			emitBurst();
		} else {
			timeSinceLastEmission += deltaTime;
			float emissionInterval = 1f / particlesPerSecond;

			// Emit particles continuously based on elapsed time
			while (timeSinceLastEmission >= emissionInterval) {
				emitParticle();
				timeSinceLastEmission -= emissionInterval;
			}
		}

		// Update and clean expired particles
		for (Particle particle : particles) {
			particle.update(deltaTime);
			if (!particle.isAlive()) {
				particles.remove(particle);
			}
		}
	}

	/**
	 * Emits a single particle with randomized properties (velocity, acceleration,
	 * and lifetime) within their configured ranges.
	 */
	private void emitParticle() {
		Vector3f initialPosition = new Vector3f(position);
		Vector3f initialVelocity = randomizeVector(velocityRange);
		Vector3f initialAcceleration = randomizeVector(accelerationRange);
		float lifetime = randomizeFloat(lifetimeRange);

		Particle particle = new Particle(initialPosition, initialVelocity,
		    initialAcceleration, lifetime);
		particles.add(particle);
	}

	/**
	 * Emits a burst of particles, the number of which is defined by the
	 * `burstCount`. After completing a burst, burst mode is disabled
	 * automatically.
	 */
	private void emitBurst() {
		for (int i = 0; i < burstCount; i++) {
			emitParticle();
		}
		burstMode = false; // Disable burst mode after the burst is emitted.
	}

	/**
	 * Randomizes a vector's x, y, z components within their respective ranges.
	 * 
	 * @param range The range to randomize values within.
	 * @return A new randomized vector.
	 */
	private Vector3f randomizeVector(Vector3f range) {
		return new Vector3f((float) (Math.random() * range.x * 2 - range.x),
		    (float) (Math.random() * range.y * 2 - range.y),
		    (float) (Math.random() * range.z * 2 - range.z));
	}

	/**
	 * Randomizes a float value within a range [0, range).
	 * 
	 * @param range The range to randomize values within.
	 * @return A randomized float value.
	 */
	private float randomizeFloat(float range) {
		return (float) (Math.random() * range);
	}

	/**
	 * Configures the emitter to use burst mode with a specified number of
	 * particles to emit. After enabling, particles will only emit in bursts until
	 * reset.
	 * 
	 * @param burstCount Number of particles to emit during each burst.
	 */
	public void setBurstMode(int burstCount) {
		this.burstMode = true;
		this.burstCount = burstCount;
	}

	/**
	 * Updates the range for randomizing initial particle velocities.
	 * 
	 * @param velocityRange The new velocity range.
	 */
	public void setVelocityRange(Vector3f velocityRange) {
		this.velocityRange = velocityRange;
	}

	/**
	 * Updates the range for randomizing initial particle accelerations.
	 * 
	 * @param accelerationRange The new acceleration range.
	 */
	public void setAccelerationRange(Vector3f accelerationRange) {
		this.accelerationRange = accelerationRange;
	}

	/**
	 * Updates the range of particle lifetimes.
	 * 
	 * @param lifetimeRange The new lifetime range.
	 */
	public void setLifetimeRange(float lifetimeRange) {
		this.lifetimeRange = lifetimeRange;
	}

	/**
	 * Retrieves all currently active particles managed by this emitter.
	 * 
	 * @return A concurrent queue containing the currently active particles.
	 */
	public ConcurrentLinkedQueue<Particle> getParticles() {
		return particles;
	}

}