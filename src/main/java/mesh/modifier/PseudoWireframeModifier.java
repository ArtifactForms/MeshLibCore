package mesh.modifier;

import mesh.Mesh3D;

/**
 * Transforms a 3D mesh into a wireframe-like structure by creating holes and
 * solidifying the remaining geometry. This is a pseudo-wireframe modifier,
 * differing from traditional wireframe modifiers.
 * <p>
 * Traditional wireframe modifiers, such as the one in Blender, replace the
 * edges of the mesh with cylindrical or rectangular struts, directly converting
 * the mesh's edges into a visible wireframe. In contrast, this pseudo-wireframe
 * approach combines a hole-creation process with solidification to approximate
 * the effect, maintaining the original surface structure with modifications.
 * </p>
 */
public class PseudoWireframeModifier implements IMeshModifier {

	private static final float DEFAULT_HOLE_PECENTAGE = 0.9f;

	private static final float DEFAULT_THICKNESS = 0.02f;

	private float holePercentage;

	private float thickness;

	private Mesh3D mesh;

	/**
	 * Constructs a new instance of the PseudoWireframeModifier with default
	 * parameters. The default hole percentage is set to 0.9, and the default
	 * solidify thickness is 0.02.
	 */
	public PseudoWireframeModifier() {
		holePercentage = DEFAULT_HOLE_PECENTAGE;
		thickness = DEFAULT_THICKNESS;
	}

	/**
	 * Modifies the provided mesh to create a pseudo-wireframe-like effect by
	 * creating holes and applying solidification.
	 *
	 * @param mesh The 3D mesh to modify.
	 * @return The modified 3D mesh with pseudo-wireframe-like transformation
	 *         applied.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		validateMesh(mesh);
		setMesh(mesh);
		createHoles();
		solidify();
		return mesh;
	}

	/**
	 * Creates holes in the mesh by applying a custom extrusion modifier
	 * configured to act as a "hole-creation" operation.
	 */
	private void createHoles() {
		mesh.apply(createExtrudeModifier());
	}

	/**
	 * Creates a configured instance of an ExtrudeModifier to perform the
	 * hole-creation effect.
	 *
	 * @return An instance of ExtrudeModifier configured with the current hole
	 *         percentage.
	 */
	private ExtrudeModifier createExtrudeModifier() {
		ExtrudeModifier extrude = new ExtrudeModifier();
		extrude.setScale(holePercentage);
		extrude.setAmount(0);
		extrude.setRemoveFaces(true);
		return extrude;
	}

	/**
	 * Solidifies the remaining geometry after the hole-creation step by applying
	 * a solidification modifier with the defined thickness.
	 */
	private void solidify() {
		mesh.apply(new SolidifyModifier(thickness));
	}

	/**
	 * Validates that the provided mesh is not null before processing.
	 *
	 * @param mesh The mesh to validate.
	 * @throws IllegalArgumentException If the provided mesh is null.
	 */
	private void validateMesh(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
	}

	/**
	 * Sets the current mesh instance for transformation operations.
	 *
	 * @param mesh The 3D mesh to set.
	 */
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	/**
	 * Retrieves the current hole percentage value used for the pseudo-wireframe
	 * effect.
	 *
	 * @return The current hole percentage value.
	 */
	public float getHolePercentage() {
		return holePercentage;
	}

	/**
	 * Sets the percentage of the "holes" to apply on the mesh. Value must be
	 * between 0 and 1.
	 *
	 * @param holePercentage The new hole percentage value.
	 * @throws IllegalArgumentException If the value is less than 0 or greater
	 *                                  than 1.
	 */
	public void setHolePercentage(float holePercentage) {
		if (holePercentage < 0 || holePercentage > 1) {
			throw new IllegalArgumentException(
			    "Hole percentage must be between 0 and 1.");
		}
		this.holePercentage = holePercentage;
	}

	/**
	 * Retrieves the current thickness value used for solidification.
	 *
	 * @return The current solidify thickness value.
	 */
	public float getThickness() {
		return thickness;
	}

	/**
	 * Sets the solidify thickness value for the mesh transformation. Thickness
	 * must be positive.
	 *
	 * @param thickness The new thickness value.
	 * @throws IllegalArgumentException If the value is less than or equal to 0.
	 */
	public void setThickness(float thickness) {
		if (thickness <= 0) {
			throw new IllegalArgumentException(
			    "Thickness must be greater than zero.");
		}
		this.thickness = thickness;
	}

}
