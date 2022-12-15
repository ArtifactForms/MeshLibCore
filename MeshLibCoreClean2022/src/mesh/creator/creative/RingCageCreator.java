package mesh.creator.creative;

import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.util.Mesh3DUtil;

public class RingCageCreator implements IMeshCreator {

	private float[] segmentsHeights = new float[4];
	
	private int subdivisions;
	
	private int segments;
	
	private int vertices;
	
	private float outerRadius;
	
	private float innerRadius;
	
	private float segmentHeight;
	
	private float controlLoopDistance;
	
	private Mesh3D mesh;
	
	private ArrayList<Mesh3D> circles = new ArrayList<Mesh3D>();

	public RingCageCreator() {
		subdivisions = 1;
		segments = 3;
		vertices = 16;
		outerRadius = 1f;
		innerRadius = 0.9f;
		controlLoopDistance = 0.001f;
	}

	@Override
	public Mesh3D create() {
		clear();
		updateInnerRadius();
		initializeMesh();
		updateSegmentHeight();
		updateSegmentHeights();
		createCircles();
		append();
		bridge();
		createHoles();
		solidify();
		translate();
		subdivide();
		return mesh;
	}

	private void updateSegmentHeight() {
		Mesh3D mesh = new CircleCreator(vertices, outerRadius).create();
		Vector3f v0 = mesh.getVertexAt(0);
		Vector3f v1 = mesh.getVertexAt(1);
		segmentHeight = v0.distance(v1);
	}

	private void updateSegmentHeights() {
		float height = segmentHeight - (controlLoopDistance * 2);
		segmentsHeights[0] = 0;
		segmentsHeights[1] = controlLoopDistance;
		segmentsHeights[2] = height + controlLoopDistance;
		segmentsHeights[3] = height + (controlLoopDistance * 2);
	}

	private void createCircles() {
		for (int i = 0; i <= segments; i++) {
			Mesh3D mesh = new CircleCreator(vertices, outerRadius).create();
			mesh.translateY(segmentsHeights[i]);
			circles.add(mesh);
		}
	}

	private void append() {
		for (Mesh3D mesh : circles) {
			this.mesh.append(mesh);
		}
	}

	private void bridge() {
		for (int i = 0; i < circles.size() - 1; i++) {
			bridge(circles.get(i), circles.get(i + 1));
		}
	}

	private void bridge(Mesh3D m0, Mesh3D m1) {
		for (int i = 0; i < vertices; i++) {
			Vector3f v0 = m1.getVertexAt(i);
			Vector3f v1 = m1.getVertexAt((i + 1) % vertices);
			Vector3f v2 = m0.getVertexAt(i);
			Vector3f v3 = m0.getVertexAt((i + 1) % vertices);
			Mesh3DUtil.bridge(mesh, v0, v1, v2, v3);
		}
	}

	private void createHoles() {
		List<Face3D> faces = mesh.getFaces(vertices, vertices * 2);
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, 0.9f, 0f);
		}
		mesh.faces.removeAll(faces);
	}

	private void updateInnerRadius() {
		innerRadius = outerRadius - 0.1f;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void clear() {
		circles.clear();
	}

	private void solidify() {
		new SolidifyModifier((outerRadius - innerRadius)).modify(mesh);
	}

	private void subdivide() {
		new CatmullClarkModifier(subdivisions).modify(mesh);
	}

	private void translate() {
		mesh.translateY(-segmentHeight / 2.0f);
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	public float getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(float outerRadius) {
		this.outerRadius = outerRadius;
	}

}
