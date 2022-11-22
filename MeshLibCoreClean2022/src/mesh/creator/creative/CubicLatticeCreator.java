package mesh.creator.creative;

import java.util.HashSet;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.util.Mesh3DUtil;

public class CubicLatticeCreator implements IMeshCreator {

	private int segmentsX = 3;
	private int segmentsY = 3;
	private int segmentsZ = 3;
	private int subdivisions = 2;
	private Mesh3D mesh;
	
	private Mesh3D createSegment() {
		Mesh3D mesh = new CubeCreator().create();
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, 1.0f, 0.5f);
		}
		mesh.faces.removeAll(faces);
		return mesh;
	}
	
	private void createSegments() {
		for (int k = 0; k < segmentsZ; k++) {
			for (int i = 0; i < segmentsY; i++) {
				for (int j = 0; j < segmentsX; j++) {
					Mesh3D mesh = createSegment();
					mesh.translate(j * 3, i * 3, k * 3);
					this.mesh.append(mesh);
				}
			}
		}
	}
	
	private void removeDoubles() {
		Mesh3D m = new Mesh3D();
		HashSet<Vector3f> vertexSet = new HashSet<Vector3f>();
		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				vertexSet.add(v);
			}
		}
		m.vertices.addAll(vertexSet);
		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				int index = m.vertices.indexOf(v);
				f.indices[i] = index;
			}
			m.add(f);
		}
		this.mesh = m;
	}
	
	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createSegments();
		removeDoubles();
		mesh.translateX(-((segmentsX -1) * 3f) / 2f);
		mesh.translateY(-((segmentsY -1) * 3f) / 2f);
		mesh.translateZ(-((segmentsZ -1) * 3f) / 2f);
		new SolidifyModifier(0.3f).modify(mesh);
		new CatmullClarkModifier(subdivisions).modify(mesh);
		return mesh;
	}

	public int getSegmentsX() {
		return segmentsX;
	}

	public void setSegmentsX(int segmentsX) {
		this.segmentsX = segmentsX;
	}

	public int getSegmentsY() {
		return segmentsY;
	}

	public void setSegmentsY(int segmentsY) {
		this.segmentsY = segmentsY;
	}

	public int getSegmentsZ() {
		return segmentsZ;
	}

	public void setSegmentsZ(int segmentsZ) {
		this.segmentsZ = segmentsZ;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}
