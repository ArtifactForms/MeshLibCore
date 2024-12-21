package engine.render.effects;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import workspace.ui.Graphics;

/**
 * A component responsible for managing and rendering particles using a
 * specified particle emitter and renderer.
 * <p>
 * This class implements the {@link RenderableComponent} interface, allowing it to
 * integrate seamlessly with the rendering system. It uses a
 * {@link ParticleEmitter} to handle the logic of particle spawning and updates,
 * while delegating rendering operations to a {@link ParticleRenderer}.
 * </p>
 * <p>
 * The ParticleComponent ensures proper lifecycle management by handling
 * initialization, updates, rendering, and cleanup for both the emitter and
 * renderer components.
 * </p>
 * 
 * @author Simon Dietz
 */
public class ParticleComponent extends AbstractComponent
    implements RenderableComponent {

	private ParticleEmitter emitter;

	private ParticleRenderer renderer;

	/**
	 * Creates a new ParticleComponent with the given particle emitter and
	 * renderer.
	 * 
	 * @param emitter  The particle emitter responsible for spawning and managing
	 *                 particle lifecycles.
	 * @param renderer The particle renderer responsible for drawing particles on
	 *                 the provided graphics context.
	 */
	public ParticleComponent(ParticleEmitter emitter, ParticleRenderer renderer) {
		this.emitter = emitter;
		this.renderer = renderer;
	}

	/**
	 * Initializes the renderer resources necessary for drawing particles.
	 */
	@Override
	public void initialize() {
		renderer.initialize();
	}

	/**
	 * Updates the particle emitter with the time-per-frame value to spawn and
	 * manage particles over time.
	 */
	@Override
	public void update(float tpf) {
		emitter.update(tpf);
	}

	/**
	 * Delegates the rendering of particles to the renderer, passing the current
	 * particles to visualize.
	 */
	@Override
	public void render(Graphics g) {
		renderer.render(g, emitter.getParticles());
	}

	/**
	 * Cleans up any resources used by the particle renderer.
	 */
	@Override
	public void cleanup() {
		renderer.cleanup();
	}

	/**
	 * Retrieves the particle emitter associated with this component.
	 * 
	 * @return The ParticleEmitter instance used for spawning and updating
	 *         particles.
	 */
	public ParticleEmitter getEmitter() {
		return emitter;
	}

	/**
	 * Sets a new particle emitter for this component. This can be used to
	 * dynamically change the emitter's behavior or particle spawning logic at
	 * runtime.
	 * 
	 * @param emitter The new ParticleEmitter instance.
	 */
	public void setEmitter(ParticleEmitter emitter) {
		this.emitter = emitter;
	}

	/**
	 * Retrieves the particle renderer associated with this component.
	 * 
	 * @return The ParticleRenderer responsible for drawing particles.
	 */
	public ParticleRenderer getRenderer() {
		return renderer;
	}

	/**
	 * Sets a new particle renderer for this component. This allows for swapping
	 * rendering strategies or visualizations dynamically at runtime.
	 * 
	 * @param renderer The new ParticleRenderer instance.
	 */
	public void setRenderer(ParticleRenderer renderer) {
		this.renderer = renderer;
	}

}
