package mesh.modifier;

import mesh.Mesh3D;

/**
 * IMeshModifier defines the interface for operations that transform or modify
 * 3D meshes.
 * <p>
 * Mesh modifiers are tools that allow for geometric transformations on 3D
 * meshes. These transformations include operations such as translation,
 * rotation, scaling, bending, and other geometric manipulations. The library
 * provides a variety of pre-built modifiers that adhere to this interface,
 * ensuring consistency and extensibility.
 * </p>
 * <p>
 * If you intend to create custom mesh modifiers, you must implement this
 * interface to ensure compatibility with the existing system and other mesh
 * transformations.
 * </p>
 * <p>
 * The {@code modify} method performs an in-place modification of the provided
 * mesh and returns the same reference as the one passed in. This ensures that
 * changes are directly applied to the provided instance, avoiding unnecessary
 * object creation and improving performance.
 * </p>
 */
public interface IMeshModifier {

	/**
	 * Applies a modification to the provided 3D mesh.
	 *
	 * <p>
	 * This method modifies the given mesh directly and returns the same reference
	 * that was provided. This is a design choice to avoid creating new objects
	 * unnecessarily and to enable efficient transformations on the provided mesh
	 * data.
	 * </p>
	 *
	 * @param mesh The 3D mesh to modify. Must not be null.
	 * @return The same 3D mesh reference that was passed in, representing its
	 *         modified state.
	 * @throws IllegalArgumentException if the provided mesh is null.
	 *
	 * @see mesh.modifier.* examples for concrete implementations
	 */
	public Mesh3D modify(Mesh3D mesh);

}
