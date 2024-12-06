package mesh.modifier;

import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;

/**
 * A modifier responsible for inverting the indices of 3D mesh faces. This is
 * typically used to reverse the winding order of faces, which can change
 * rendering order, normal direction, or surface orientation.
 *
 * <p>
 * Implements both single-face and collection-based operations to handle
 * individual or multiple faces in a given mesh.
 * </p>
 */
public class FlipFacesModifier implements IMeshModifier, FaceModifier {

	/**
	 * Inverts the indices of all faces in the provided mesh.
	 *
	 * @param mesh The 3D mesh whose faces will be inverted.
	 * @return The modified mesh with inverted face indices.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		return modify(mesh, mesh.faces);
	}

	/**
	 * Inverts the indices of a single face in the provided mesh.
	 *
	 * @param mesh The 3D mesh containing the face to modify.
	 * @param face The single face whose indices will be inverted.
	 * @return The modified mesh with the single face's indices inverted.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh, Face3D face) {
		validate(mesh, face);
		invertFaceIndices(face);
		return mesh;
	}

	/**
	 * Inverts the indices of a collection of faces in the provided mesh.
	 *
	 * @param mesh  The 3D mesh containing faces to modify.
	 * @param faces A collection of faces to process and invert.
	 * @return The modified mesh with all specified faces' indices inverted.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
		validate(mesh, faces);
		if (faces.isEmpty()) {
			return mesh;
		}
		for (Face3D face : faces) {
			invertFaceIndices(face);
		}
		return mesh;
	}

	/**
	 * Validates that the provided mesh and single face are valid and not null.
	 *
	 * @param mesh The 3D mesh to validate.
	 * @param face The single face to validate.
	 * @throws IllegalArgumentException if either the mesh or face is null.
	 */
	private void validate(Mesh3D mesh, Face3D face) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (face == null) {
			throw new IllegalArgumentException("Face cannot be null.");
		}
	}

	/**
	 * Validates that the provided mesh and a collection of faces are valid and not
	 * null.
	 *
	 * @param mesh  The 3D mesh to validate.
	 * @param faces The collection of faces to validate.
	 * @throws IllegalArgumentException if either the mesh or the faces collection
	 *                                  is null.
	 */
	private void validate(Mesh3D mesh, Collection<Face3D> faces) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (faces == null) {
			throw new IllegalArgumentException("Faces collection cannot be null.");
		}
	}

	/**
	 * Performs in-place index inversion on a single face's indices to change its
	 * winding order.
	 *
	 * @param face The face whose indices are to be inverted.
	 */
	private void invertFaceIndices(Face3D face) {
		for (int i = 0, j = face.indices.length - 1; i < j; i++, j--) {
			int temp = face.indices[i];
			face.indices[i] = face.indices[j];
			face.indices[j] = temp;
		}
	}

}