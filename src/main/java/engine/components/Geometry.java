package engine.components;

import engine.render.Material;
import mesh.Mesh3D;
import workspace.ui.Graphics;

/**
 * Represents a renderable geometry component within the scene graph.
 * <p>
 * The Geometry class is a component that encapsulates a 3D mesh and its
 * associated material. It implements rendering behavior through the
 * {@link RenderableComponent} interface, allowing it to be drawn during the
 * rendering pass of the scene graph traversal.
 * </p>
 * <p>
 * This class is responsible for applying the provided {@link Material} during
 * rendering, ensuring proper visualization of the associated {@link Mesh3D}.
 * </p>
 * 
 * <h3>Key Features</h3>
 * <ul>
 * <li>Supports a default white material if no custom material is provided.</li>
 * <li>Handles cleanup by releasing references to the mesh and material.</li>
 * <li>Integrates with the graphics context to issue rendering commands via the
 * {@link Graphics} class.</li>
 * </ul>
 */
public class Geometry extends AbstractComponent implements RenderableComponent {

	/** The 3D mesh to be rendered by this component. */
	private Mesh3D mesh;

	/** The material associated with this geometry for rendering purposes. */
	private Material material;

	/**
	 * Constructs a Geometry component with a provided mesh and the default white
	 * material.
	 * 
	 * @param mesh The mesh to associate with this geometry.
	 */
	public Geometry(Mesh3D mesh) {
		this(mesh, Material.DEFAULT_WHITE);
	}

	/**
	 * Constructs a Geometry component with a specific mesh and material.
	 * 
	 * @param mesh     The 3D mesh to associate with this geometry. Must not be
	 *                 null.
	 * @param material The material to use for rendering. Must not be null.
	 * @throws IllegalArgumentException if either mesh or material is null.
	 */
	public Geometry(Mesh3D mesh, Material material) {
		validate(mesh, material);
		this.mesh = mesh;
		this.material = material;
	}

	/**
	 * Validates the provided mesh and material to ensure they are non-null.
	 * 
	 * @param mesh     The 3D mesh to validate.
	 * @param material The material to validate.
	 * @throws IllegalArgumentException if either mesh or material is null.
	 */
	private void validate(Mesh3D mesh, Material material) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (material == null) {
			throw new IllegalArgumentException("Material cannot be null.");
		}
	}

	/**
	 * Cleans up resources associated with this component by nullifying references
	 * to the mesh and material. This is called when the component is no longer
	 * needed or during application shutdown.
	 */
	@Override
	public void cleanup() {
		mesh = null;
		material = null;
	}

	/**
	 * Handles rendering of the mesh with its material using the provided graphics
	 * context.
	 * 
	 * <p>
	 * This method applies the material, renders the associated mesh's faces using
	 * the {@link Graphics} instance, and then releases the material's state
	 * afterward.
	 * </p>
	 * 
	 * @param g The graphics context to use for rendering the geometry.
	 */
	@Override
	public void render(Graphics g) {
		material.apply(g);
		g.fillFaces(mesh);
		material.release(g);
	}

	/**
	 * Placeholder for initialization logic if needed in future development.
	 * Currently, no specific initialization is necessary.
	 */
	@Override
	public void initialize() {
		// Initialization logic, if needed
	}

	/**
	 * Updates geometry's state over time, if necessary. Currently, this is a
	 * placeholder for potential future logic.
	 * 
	 * @param tpf Time per frame, used to synchronize updates across frames.
	 */
	@Override
	public void update(float tpf) {
		// Placeholder for potential mesh state updates
	}
	
}