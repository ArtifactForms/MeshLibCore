package mesh.modifier;

import java.util.HashSet;
import java.util.List;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;
import mesh.util.VertexNormals;

public class SolidifyModifier implements IMeshModifier {

	private float thickness;
	
	private Mesh3D mesh;

	private Mesh3D innerMesh;
	
	private List<Vector3f> vertexNormals;
	
	private HashSet<Edge3D> edges;

	public SolidifyModifier() {
		this(0.01f);
	}

	public SolidifyModifier(float thickness) {
		this.thickness = thickness;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (thickness == 0)
			return mesh;

		Mesh3D result = new Mesh3D();

		setMesh(mesh);
		createVertexNormals();
		initializeEdgeMap();
		mapEdges();
		initializeInnerMesh();
		flipDirectionOfInnerMesh();
		result.append(mesh, innerMesh);
		moveInnerMeshAlongVertexNormals();
		bridgeHoles(result, innerMesh);
		applyResult(result);

		return mesh;
	}

	private void initializeInnerMesh() {
		innerMesh = mesh.copy();
	}

	private void initializeEdgeMap() {
		edges = new HashSet<>();
	}

	private void flipDirectionOfInnerMesh() {
		Mesh3DUtil.flipDirection(innerMesh);
	}

	private void bridgeHoles(Mesh3D result, Mesh3D innerMesh) {
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D f : faces) {
			int size = f.indices.length;
			for (int i = 0; i < f.indices.length; i++) {
				Edge3D edge0 = new Edge3D(f.indices[i], f.indices[(i + 1) % size]);
				Edge3D edge1 = new Edge3D(f.indices[(i + 1) % size], f.indices[i]);
				if (!edges.contains(edge1)) {
					Vector3f v0 = innerMesh.getVertexAt(edge0.fromIndex);
					Vector3f v1 = innerMesh.getVertexAt(edge0.toIndex);
					Vector3f v2 = mesh.getVertexAt(edge0.fromIndex);
					Vector3f v3 = mesh.getVertexAt(edge0.toIndex);
					Mesh3DUtil.bridge(result, v0, v1, v2, v3);
				}
			}
		}
	}

	private void applyResult(Mesh3D result) {
		mesh.vertices.clear();
		mesh.faces.clear();
		mesh.addVertices(result.vertices);
		mesh.addFaces(result.faces);
	}

	private void mapEdges() {
		for (Face3D face : mesh.faces) {
			for (int i = 0; i < face.indices.length; i++) {
				Edge3D edge = new Edge3D(face.indices[i], face.indices[(i + 1) % face.indices.length]);
				edges.add(edge);
			}
		}
	}

	private void moveInnerMeshAlongVertexNormals() {
		for (int i = 0; i < innerMesh.vertices.size(); i++) {
			Vector3f vertex = innerMesh.getVertexAt(i);
			Vector3f normal = vertexNormals.get(i);
			vertex.set(normal.mult(-thickness).add(vertex));
		}
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
