package engine.render.effects;

import java.util.Collection;

import workspace.ui.Graphics;

/**
 * Interface for rendering particles in a particle system. Implementations of
 * this interface define how particles are visually represented, such as using
 * sprites, points, or other rendering techniques.
 * 
 * @author Simon Dietz
 */
public interface ParticleRenderer {

	/**
	 * Renders a batch of particles using the provided graphics context.
	 * 
	 * @param g         The graphics context used for rendering.
	 * @param particles The collection of particles to render.
	 */
	void render(Graphics g, Collection<Particle> particles);

	/**
	 * Initializes any resources or setup required for rendering particles. This
	 * could include shaders, textures, or other rendering assets.
	 */
	void initialize();

	/**
	 * Cleans up resources used by the renderer when it is no longer needed. This
	 * ensures efficient memory and resource management.
	 */
	void cleanup();

}