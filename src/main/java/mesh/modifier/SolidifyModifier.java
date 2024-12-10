package mesh.modifier;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.FaceBridging;
import mesh.util.VertexNormals;

public class SolidifyModifier implements IMeshModifier {

	private float thickness;

	private Mesh3D mesh;

	private Mesh3D innerMesh;

	private List<Vector3f> vertexNormals;

	private HashSet<Edge3D> edges;

	private List<Face3D> originalFaces;

	public SolidifyModifier() {
		this(0.01f);
	}

	public SolidifyModifier(float thickness) {
		this.thickness = thickness;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		validateMesh();

		if (canExitEarly()) {
			return mesh;
		}

		initialize();
		mapEdges();
		createInnerMesh();
		bridgeHoles();

		return mesh;
	}

	private void createInnerMesh() {
		initializeInnerMesh();
		appendInnerMesh();
		flipDirectionOfInnerMesh();
		moveInnerMeshAlongVertexNormals();
	}

	private void appendInnerMesh() {
		mesh.append(innerMesh);
	}

	private void initialize() {
		initializeOriginalFaces();
		initializeEdgeMap();
		createVertexNormals();
	}

	private void bridgeHoles() {
		for (Face3D face : originalFaces) {
			int size = face.indices.length;
			for (int i = 0; i < size; i++) {
				Edge3D forwardEdge = createEdgeAt(face, i);
				Edge3D reverseEdge = forwardEdge.createPair();
				if (!edges.contains(reverseEdge)) {
					bridgeHole(forwardEdge);
				}
			}
		}
	}

	private void bridgeHole(Edge3D forwardEdge) {
		Vector3f v0 = innerMesh.getVertexAt(forwardEdge.fromIndex);
		Vector3f v1 = innerMesh.getVertexAt(forwardEdge.toIndex);
		Vector3f v2 = mesh.getVertexAt(forwardEdge.fromIndex);
		Vector3f v3 = mesh.getVertexAt(forwardEdge.toIndex);
		FaceBridging.bridge(mesh, v0, v1, v2, v3);
	}

	private void mapEdges() {
		for (Face3D face : mesh.faces) {
			for (int i = 0; i < face.indices.length; i++) {
				edges.add(createEdgeAt(face, i));
			}
		}
	}

	private Edge3D createEdgeAt(Face3D face, int i) {
		int fromIndex = face.indices[i];
		int toIndex = face.indices[(i + 1) % face.indices.length];
		return new Edge3D(fromIndex, toIndex);
	}

	private void moveInnerMeshAlongVertexNormals() {
		IntStream.range(0, innerMesh.vertices.size()).parallel().forEach(i -> {
			Vector3f vertex = innerMesh.getVertexAt(i);
			Vector3f normal = vertexNormals.get(i);
			vertex.set(normal.mult(-thickness).add(vertex));
		});
	}

	private void flipDirectionOfInnerMesh() {
		innerMesh.apply(new FlipFacesModifier());
	}

	private void validateMesh() {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
	}

	/**
	 * Determines if the modification process can be skipped due to zero thickness
	 * or an empty mesh (no vertices or faces).
	 */
	private boolean canExitEarly() {
		return (thickness == 0 || mesh.vertices.isEmpty() || mesh.faces.isEmpty());
	}

	private void initializeInnerMesh() {
		innerMesh = mesh.copy();
	}

	private void initializeEdgeMap() {
		edges = new HashSet<>();
	}

	private void initializeOriginalFaces() {
		originalFaces = mesh.getFaces(0, mesh.getFaceCount());
	}

	private void createVertexNormals() {
		vertexNormals = new VertexNormals(mesh).getVertexNormals();
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

}
