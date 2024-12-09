package mesh.modifier;

import java.util.Collection;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

/**
 * The {@code ExtrudeModifier} class implements the functionality of extruding
 * faces of a 3D mesh by a given amount and scaling factor. It allows faces to
 * be extruded outward, inward, and optionally removed from the mesh after
 * extrusion. This class implements {@code IMeshModifier} and
 * {@code FaceModifier} interfaces.
 * 
 * <p>
 * Key features:
 * <ul>
 * <li>Extrusion of a specified collection of faces, or individual faces.</li>
 * <li>Adjustable scaling and extrusion amount parameters.</li>
 * <li>Optionally remove faces after extrusion using the {@code removeFaces}
 * flag.</li>
 * <li>Validation to prevent invalid or edge-case inputs.</li>
 * </ul>
 * </p>
 */
public class ExtrudeModifier implements IMeshModifier, FaceModifier {

	private static final float DEFAULT_SCALE = 1.0f;

	private static final float DEFAULT_AMOUNT = 0.0f;

	private boolean removeFaces;

	private float scale;

	private float amount;

	/**
	 * Default constructor initializing with default scale and amount values. Scale
	 * = 1.0, Amount = 0.0.
	 */
	public ExtrudeModifier() {
		this(DEFAULT_SCALE, DEFAULT_AMOUNT);
	}

	/**
	 * Constructor initializing the ExtrudeModifier with a specified scale and
	 * amount.
	 * 
	 * @param scale  the scaling factor to apply to the extruded geometry (must be
	 *               >= 0).
	 * @param amount the distance to extrude faces by. A positive value extrudes
	 *               outward; a negative value extrudes inward.
	 */
	public ExtrudeModifier(float scale, float amount) {
		validateScale(scale);
		this.scale = scale;
		this.amount = amount;
	}

	/**
	 * Modifies the entire 3D mesh by extruding all faces in the mesh by the current
	 * scale and amount.
	 * 
	 * @param mesh the 3D mesh to modify.
	 * @return the modified mesh.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		validateMesh(mesh);
		if (mesh.faces.isEmpty()) {
			return mesh;
		}
		return modify(mesh, mesh.getFaces());
	}

	/**
	 * Modifies a 3D mesh by extruding a specific set of faces.
	 * 
	 * @param mesh  the 3D mesh to modify.
	 * @param faces the collection of faces to extrude.
	 * @return the modified mesh.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
		validateMesh(mesh);
		validateFaces(faces);
		if (faces.isEmpty()) {
			return mesh;
		}
		for (Face3D face : faces)
			extrudeFace(mesh, face);
		if (removeFaces)
			mesh.faces.removeAll(faces);
		return mesh;
	}

	/**
	 * Modifies the mesh by extruding a single face.
	 * 
	 * @param mesh the 3D mesh to modify.
	 * @param face the single face to extrude.
	 * @return the modified mesh.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh, Face3D face) {
		validateMesh(mesh);
		if (face == null) {
			throw new IllegalArgumentException("Face cannot be null.");
		}
		extrudeFace(mesh, face);
		if (removeFaces)
			mesh.removeFace(face);
		return mesh;
	}

	/**
	 * Validates if the provided mesh is not null.
	 * 
	 * @param mesh the 3D mesh to validate.
	 */
	private void validateMesh(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
	}

	/**
	 * Validates if the provided collection of faces is not null.
	 * 
	 * @param faces the faces to validate.
	 */
	private void validateFaces(Collection<Face3D> faces) {
		if (faces == null) {
			throw new IllegalArgumentException("Faces cannot be null.");
		}
	}

	/**
	 * Validates that the scale is valid (>= 0).
	 * 
	 * @param scale the scaling factor to validate.
	 */
	private void validateScale(float scale) {
		if (scale < 0)
			throw new IllegalArgumentException("Scale must be greater than or equal to 0.");
	}

	/**
	 * Executes the extrusion logic on the given face.
	 * 
	 * @param mesh the 3D mesh to modify.
	 * @param face the face to extrude.
	 */
	private void extrudeFace(Mesh3D mesh, Face3D face) {
		int n = face.indices.length;
		int nextIndex = mesh.vertices.size();
		Vector3f normal = mesh.calculateFaceNormal(face);
		Vector3f center = mesh.calculateFaceCenter(face);

		normal.multLocal(amount);

		for (int i = 0; i < n; i++) {
			Vector3f v0 = mesh.vertices.get(face.indices[i]);
			Vector3f v1 = new Vector3f(v0).subtract(center).mult(scale).add(center);
			mesh.vertices.add(v1.addLocal(normal));
			mesh.addFace(face.indices[i], face.indices[(i + 1) % n], nextIndex + ((i + 1) % n), nextIndex + i);
		}

		updateFaceIndices(face, nextIndex);
	}

	/**
	 * Updates the indices of the given face after extrusion to point to the newly
	 * created vertices in the mesh's vertex list. This is necessary because new
	 * vertices are added during the extrusion process, and their indices must be
	 * correctly reflected in the face indices to maintain the mesh's structural
	 * integrity.
	 * 
	 * @param face      the {@link Face3D} object whose indices need to be updated.
	 * @param nextIndex the starting index of the newly added vertices in the mesh's
	 *                  vertex list.
	 */
	private void updateFaceIndices(Face3D face, int nextIndex) {
		for (int i = 0; i < face.indices.length; i++) {
			face.indices[i] = nextIndex + i;
		}
	}

	/**
	 * Gets the current scaling factor applied during extrusion.
	 * 
	 * @return the current scale.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Sets the scaling factor for the extrusion.
	 * 
	 * @param scale the scaling factor to set (must be >= 0).
	 */
	public void setScale(float scale) {
		validateScale(scale);
		this.scale = scale;
	}

	/**
	 * Gets the amount of extrusion currently configured.
	 * 
	 * @return the amount of extrusion.
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * Sets the amount to extrude faces by.
	 * 
	 * @param amount the distance to extrude. Positive values extrude outward,
	 *               negative values extrude inward.
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * Checks whether faces will be removed after extrusion.
	 * 
	 * @return true if faces should be removed after extrusion, false otherwise.
	 */
	public boolean isRemoveFaces() {
		return removeFaces;
	}

	/**
	 * Sets whether faces should be removed from the mesh after extrusion.
	 * 
	 * @param removeFaces if true, faces will be removed after extrusion.
	 */
	public void setRemoveFaces(boolean removeFaces) {
		this.removeFaces = removeFaces;
	}

}
