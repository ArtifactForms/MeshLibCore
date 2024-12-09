package mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import math.Vector3f;
import mesh.modifier.IMeshModifier;
import mesh.modifier.RemoveDoubleVerticesModifier;
import mesh.modifier.RotateYModifier;
import mesh.modifier.RotateZModifier;
import mesh.modifier.TranslateModifier;
import mesh.util.Bounds3;

public class Mesh3D {

	public ArrayList<Vector3f> vertices;

	public ArrayList<Face3D> faces;

	public Mesh3D() {
		vertices = new ArrayList<Vector3f>();
		faces = new ArrayList<Face3D>();
	}

	/**
	 * Applies the provided {@link IMeshModifier} to this mesh. This is congruent to
	 * {@link IMeshModifier#modify(Mesh3D)}.
	 * 
	 * @param modifier The modifier to apply to this mesh.
	 * @return this
	 */
	public Mesh3D apply(IMeshModifier modifier) {
		return modifier.modify(this);
	}

	/**
	 * Rotates the mesh around the Y-axis.
	 *
	 * @deprecated Use {@link RotateYModifier} instead.
	 */
	public Mesh3D rotateY(float angle) {
		return new RotateYModifier(angle).modify(this);
	}

	/**
	 * Rotates the mesh around the Z-axis.
	 *
	 * @deprecated Use {@link RotateZModifier} instead.
	 */
	public Mesh3D rotateZ(float angle) {
		return new RotateZModifier(angle).modify(this);
	}

	/**
	 * Translates the mesh along the X-axis.
	 *
	 * @deprecated Use {@link TranslateModifier} instead.
	 */
	@Deprecated
	public Mesh3D translateX(float tx) {
		return new TranslateModifier(tx, 0, 0).modify(this);
	}

	/**
	 * Translates the mesh along the Y-axis.
	 *
	 * @deprecated Use {@link TranslateModifier} instead.
	 */
	public Mesh3D translateY(float ty) {
		return new TranslateModifier(0, ty, 0).modify(this);
	}

	/**
	 * Translates the mesh along the Z-axis.
	 *
	 * @deprecated Use {@link TranslateModifier} instead.
	 */
	public Mesh3D translateZ(float tz) {
		return new TranslateModifier(0, 0, tz).modify(this);
	}

	/**
	 * Calculates the axis-aligned bounding box (AABB) for the 3D mesh based on its
	 * vertices.
	 * <p>
	 * The bounding box is defined by the minimum and maximum extents of the
	 * vertices along the X, Y, and Z axes. If there are no vertices in the mesh, an
	 * empty `Bounds3` is returned.
	 * </p>
	 *
	 * @return A {@link Bounds3} object representing the calculated bounding box of
	 *         the mesh. The bounding box extends from the minimum vertex coordinate
	 *         to the maximum vertex coordinate.
	 */
	public Bounds3 calculateBounds() {
		if (vertices.isEmpty())
			return new Bounds3();

		Vector3f min = new Vector3f(vertices.get(0));
		Vector3f max = new Vector3f(vertices.get(0));

		for (Vector3f v : vertices) {
			min.setX(Math.min(min.getX(), v.getX()));
			min.setY(Math.min(min.getY(), v.getY()));
			min.setZ(Math.min(min.getZ(), v.getZ()));

			max.setX(Math.max(max.getX(), v.getX()));
			max.setY(Math.max(max.getY(), v.getY()));
			max.setZ(Math.max(max.getZ(), v.getZ()));
		}

		return new Bounds3(min, max);
	}

	public Vector3f calculateFaceNormal(Face3D face) {
		Vector3f faceNormal = new Vector3f();
		for (int i = 0; i < face.indices.length; i++) {
			Vector3f currentVertex = vertices.get(face.indices[i]);
			Vector3f nextVertex = vertices.get(face.indices[(i + 1) % face.indices.length]);
			float x = (currentVertex.getY() - nextVertex.getY()) * (currentVertex.getZ() + nextVertex.getZ());
			float y = (currentVertex.getZ() - nextVertex.getZ()) * (currentVertex.getX() + nextVertex.getX());
			float z = (currentVertex.getX() - nextVertex.getX()) * (currentVertex.getY() + nextVertex.getY());
			faceNormal.addLocal(x, y, z);
		}
		return faceNormal.normalize();
	}
	
	public void removeDoubles(int decimalPlaces) {
		for (Vector3f v : vertices)
			v.roundLocalDecimalPlaces(decimalPlaces);
		removeDoubles();
	}

	/**
	 * Removes duplicated vertices.
	 *
	 * @deprecated Use {@link RemoveDoubleVerticesModifier} instead.
	 */
	public void removeDoubles() {
		new RemoveDoubleVerticesModifier().modify(this);
	}

	public Mesh3D copy() {
		Mesh3D copy = new Mesh3D();
		List<Vector3f> vertices = copy.vertices;
		List<Face3D> faces = copy.faces;

		for (Vector3f v : this.vertices)
			vertices.add(new Vector3f(v));

		for (Face3D f : this.faces)
			faces.add(new Face3D(f));

		return copy;
	}

	public Vector3f calculateFaceCenter(Face3D face) {
		Vector3f center = new Vector3f();
		for (int i = 0; i < face.indices.length; i++) {
			center.addLocal(vertices.get(face.indices[i]));
		}
		return center.divideLocal(face.indices.length);
	}

	public Mesh3D append(Mesh3D... meshes) {
		Mesh3D result = new Mesh3D();

		result = appendUtil(meshes);
		result = appendUtil(this, result);

		vertices.clear();
		vertices.addAll(result.vertices);

		faces.clear();
		faces.addAll(result.faces);

		return this;
	}

	private Mesh3D appendUtil(Mesh3D... meshes) {
		// FIXME copy vertices and faces
		int n = 0;
		Mesh3D mesh = new Mesh3D();
		List<Vector3f> vertices = mesh.vertices;
		List<Face3D> faces = mesh.faces;

		for (int i = 0; i < meshes.length; i++) {
			Mesh3D m = meshes[i];
			vertices.addAll(m.vertices);
			faces.addAll(meshes[i].faces);
			for (Face3D f : meshes[i].faces) {
				for (int j = 0; j < f.indices.length; j++) {
					f.indices[j] += n;
				}
			}
			n += m.getVertexCount();
		}

		return mesh;
	}

	public void clearVertices() {
		vertices.clear();
	}

	public void addVertex(float x, float y, float z) {
		vertices.add(new Vector3f(x, y, z));
	}

	public void addFace(int... indices) {
		faces.add(new Face3D(indices));
	}

	public void addVertices(Collection<Vector3f> vertices) {
		this.vertices.addAll(vertices);
	}

	public void addFaces(Collection<Face3D> faces) {
		this.faces.addAll(faces);
	}

	public void removeFace(Face3D face) {
		faces.remove(face);
	}

	public void removeFaces(Collection<Face3D> faces) {
		this.faces.removeAll(faces);
	}

	public void add(Vector3f... vertices) {
		this.vertices.addAll(Arrays.asList(vertices));
	}

	public void add(Face3D... faces) {
		this.faces.addAll(Arrays.asList(faces));
	}

	public int getVertexCount() {
		return vertices.size();
	}

	public int getFaceCount() {
		return faces.size();
	}

	public List<Face3D> getFaces() {
		return new ArrayList<Face3D>(faces);
	}

	public List<Face3D> getFaces(int from, int to) {
		return new ArrayList<>(faces.subList(from, to));
	}

	public List<Vector3f> getVertices(int from, int to) {
		return new ArrayList<>(vertices.subList(from, to));
	}

	public List<Vector3f> getVertices() {
		return new ArrayList<>(vertices);
	}

	public Vector3f getVertexAt(int index) {
		return vertices.get(index);
	}

	public Face3D getFaceAt(int index) {
		return faces.get(index);
	}

}
