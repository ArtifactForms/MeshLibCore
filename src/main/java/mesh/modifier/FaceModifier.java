package mesh.modifier;

import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;

/**
 * The {@code FaceModifier} interface defines a contract for operations that can
 * modify a 3D mesh by manipulating specific faces or collections of faces.
 * Implementations of this interface should provide logic for modifying
 * individual or groups of faces within a 3D mesh.
 * 
 * <p>
 * Key methods:
 * <ul>
 * <li>{@code modify(Mesh3D mesh, Face3D face)} - Modifies a single face in the
 * provided 3D mesh.</li>
 * <li>{@code modify(Mesh3D mesh, Collection<Face3D> faces)} - Modifies a group
 * of faces in the provided 3D mesh.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Implementations of this interface should ensure that face-specific operations
 * are isolated to avoid unintended side-effects on adjacent or connected faces
 * unless explicitly intended. For instance, operations like edge splitting or
 * other transformations on a single face could affect surrounding faces, as
 * their topology might be interconnected. These effects must be considered when
 * implementing or using a {@code FaceModifier} with mesh modifiers.
 * </p>
 * 
 * Implementations of this interface can encapsulate operations such as
 * extrusion, scaling, transformation, or other face-specific geometry changes.
 * Caution should be taken with face operations that alter the underlying mesh
 * structure to avoid breaking mesh integrity unless such changes are well
 * understood and intentionally applied.
 */
public interface FaceModifier {

	/**
	 * Modifies a single face within the provided 3D mesh.
	 * 
	 * @param mesh the {@link Mesh3D} object to modify.
	 * @param face the single {@link Face3D} to be modified.
	 * @return the modified {@link Mesh3D} object.
	 */
	Mesh3D modify(Mesh3D mesh, Face3D face);

	/**
	 * Modifies a collection of faces within the provided 3D mesh.
	 * 
	 * @param mesh  the {@link Mesh3D} object to modify.
	 * @param faces the collection of {@link Face3D} to be modified.
	 * @return the modified {@link Mesh3D} object.
	 */
	Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces);

}