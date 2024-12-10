package mesh.modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

/**
 * The {@code InsetModifier} modifies a mesh by applying an inset operation to
 * its faces. Insetting creates smaller, inset faces inside the original faces,
 * producing a beveled or framed effect. This modifier supports applying the
 * inset operation to all faces in a mesh, a specific collection of faces, or a
 * single face.
 * <p>
 * The inset factor determines how far the vertices of the new face are moved
 * inward, based on the edges' lengths of the original face.
 * </p>
 */
public class InsetModifier implements IMeshModifier, FaceModifier {

	/**
	 * The default inset factor applied if no custom value is specified. This
	 * value determines the default distance vertices are moved inward when the
	 * inset operation is performed.
	 */
	private static final float DEFAULT_INSET = 0.1f;

	/**
	 * The index for the next available vertex in the mesh. This value is used to
	 * keep track of where to insert new vertices during the inset operation.
	 */
	private int nextIndex;

	/**
	 * The inset factor that controls the distance vertices are moved inward
	 * during the inset operation. A higher value results in a deeper inset, while
	 * a smaller value results in a shallower inset.
	 */
	private float inset;

	/**
	 * The mesh being modified by the {@code InsetModifier}. This field is set
	 * during the modification process and stores the reference to the mesh that
	 * is being operated upon.
	 */
	private Mesh3D mesh;

	/**
	 * Creates an {@code InsetModifier} with the default inset factor (0.1).
	 */
	public InsetModifier() {
		this(DEFAULT_INSET);
	}

	/**
	 * Creates an {@code InsetModifier} with the specified inset factor.
	 *
	 * @param inset the inset factor, controlling the distance vertices are moved
	 *              inward.
	 */
	public InsetModifier(float inset) {
		this.inset = inset;
	}

	/**
	 * Modifies the entire mesh by applying the inset operation to all its faces.
	 *
	 * @param mesh the {@code Mesh3D} to modify.
	 * @return the modified mesh.
	 * @throws IllegalArgumentException if the mesh is null.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		validateMesh(mesh);
		if (mesh.faces.isEmpty()) {
			return mesh;
		}
		modify(mesh, mesh.getFaces());
		return mesh;
	}

	/**
	 * Modifies the specified collection of faces in the mesh by applying the
	 * inset operation.
	 *
	 * @param mesh  the {@code Mesh3D} to modify.
	 * @param faces the collection of {@code Face3D} instances to modify.
	 * @return the modified mesh.
	 * @throws IllegalArgumentException if the mesh or faces are null.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
		validateMesh(mesh);
		validateFaces(faces);
		Collection<Face3D> facesToModify = faces;
		if (faces == mesh.faces) {
			facesToModify = new ArrayList<Face3D>(mesh.faces);
		}
		setMesh(mesh);
		for (Face3D face : facesToModify) {
			insetFace(face);
		}
		return mesh;
	}

	/**
	 * Modifies a single face in the mesh by applying the inset operation.
	 *
	 * @param mesh the {@code Mesh3D} containing the face.
	 * @param face the {@code Face3D} to modify.
	 * @return the modified mesh.
	 * @throws IllegalArgumentException if the mesh or face is null.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh, Face3D face) {
		validateMesh(mesh);
		validateFace(face);
		setMesh(mesh);
		insetFace(face);
		return mesh;
	}

	/**
	 * Applies the inset operation to a single face, creating inset vertices and
	 * updating the face structure.
	 *
	 * @param face the face to modify.
	 */
	private void insetFace(Face3D face) {
		updateNextIndex();
		createInsetVertices(processFaceEdges(face));
		for (int i = 0; i < face.getVertexCount(); i++) {
			createFaceAt(face, i);
		}
		replaceOriginalFaceWithInsetFace(face);
	}

	/**
	 * Processes the edges of a face to calculate the new inset vertices.
	 *
	 * @param face the face to process.
	 * @return a list of inset vertices.
	 */
	private List<Vector3f> processFaceEdges(Face3D face) {
		List<Vector3f> vertices = new ArrayList<>();
		for (int i = 0; i < face.indices.length; i++) {
			int index0 = face.indices[i];
			int index1 = face.indices[(i + 1) % face.indices.length];

			Vector3f v0 = mesh.vertices.get(index0);
			Vector3f v1 = mesh.vertices.get(index1);

			float edgeLength = v0.distance(v1);
			float insetFactor = calculateInsetFactor(edgeLength);

			Vector3f v4 = v1.subtract(v0).mult(insetFactor).add(v0);
			Vector3f v5 = v1.add(v1.subtract(v0).mult(-insetFactor));

			vertices.add(v4);
			vertices.add(v5);
		}
		return vertices;
	}

	/**
	 * Creates the inset vertices from the processed edge vertices.
	 *
	 * @param vertices the processed edge vertices.
	 */
	private void createInsetVertices(List<Vector3f> vertices) {
		for (int i = 1; i < vertices.size(); i += 2) {
			int a = vertices.size() - 2 + i;
			Vector3f v0 = vertices.get(a % vertices.size());
			Vector3f v1 = vertices.get((a + 1) % vertices.size());
			Vector3f v = GeometryUtil.getMidpoint(v0, v1);
			mesh.add(v);
		}
	}

	/**
	 * Replaces the original face with the inset face.
	 *
	 * @param face the face to replace.
	 */
	private void replaceOriginalFaceWithInsetFace(Face3D face) {
		for (int i = 0; i < face.getVertexCount(); i++) {
			face.indices[i] = nextIndex + i;
		}
	}

	/**
	 * Creates a new face at the specified index using the original and inset
	 * vertices.
	 *
	 * @param face the original face.
	 * @param i    the index of the vertex to process.
	 */
	private void createFaceAt(Face3D face, int i) {
		int n = face.indices.length;
		int index0 = face.indices[i];
		int index1 = face.indices[(i + 1) % n];
		int index2 = nextIndex + ((i + 1) % n);
		int index3 = nextIndex + i;
		mesh.addFace(index0, index1, index2, index3);
	}

	/**
	 * Validates that the faces collection is not null.
	 *
	 * @param faces the collection of faces to validate.
	 * @throws IllegalArgumentException if the collection is null.
	 */
	private void validateFaces(Collection<Face3D> faces) {
		if (faces == null) {
			throw new IllegalArgumentException("Faces cannot be null.");
		}
	}

	/**
	 * Validates that the mesh is not null.
	 *
	 * @param mesh the mesh to validate.
	 * @throws IllegalArgumentException if the mesh is null.
	 */
	private void validateMesh(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
	}

	/**
	 * Validates that the face is not null.
	 *
	 * @param face the face to validate.
	 * @throws IllegalArgumentException if the face is null.
	 */
	private void validateFace(Face3D face) {
		if (face == null) {
			throw new IllegalArgumentException("Face cannot be null.");
		}
	}

	/**
	 * Calculates the inset factor based on the edge length.
	 *
	 * @param edgeLength the length of the edge.
	 * @return the inset factor.
	 */
	private float calculateInsetFactor(float edgeLength) {
		return edgeLength > 0 ? (1f / edgeLength) * inset : 0f;
	}

	/**
	 * Updates the next available vertex index.
	 */
	private void updateNextIndex() {
		nextIndex = mesh.vertices.size();
	}

	/**
	 * Sets the mesh to be modified.
	 *
	 * @param mesh the mesh to set.
	 */
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	/**
	 * Retrieves the inset factor.
	 *
	 * @return the inset factor.
	 */
	public float getInset() {
		return inset;
	}

	/**
	 * Sets the inset factor.
	 *
	 * @param inset the inset factor to set.
	 */
	public void setInset(float inset) {
		this.inset = inset;
	}

}
