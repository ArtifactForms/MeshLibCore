package mesh.creator.unsorted;

import java.util.ArrayList;
import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.wip.Mesh3DUtil;

public class RingCageCreator implements IMeshCreator {

	private static final float[] SEGMENT_HEIGHTS = new float[] { 0f, 0.01f, 0.51f, 0.52f };

	private int segments;
	private int vertices;
	private float outerRadius;
	private float innerRadius;
	private Mesh3D mesh;

	public RingCageCreator() {
		super();
		this.segments = 3;
		this.vertices = 16;
		this.outerRadius = 1f;
		this.innerRadius = 0.9f;
	}

	private void createCircles(List<Mesh3D> meshes) {
		for (int i = 0; i <= segments; i++) {
			Mesh3D mesh = new CircleCreator(vertices, outerRadius).create();
			mesh.translateY(SEGMENT_HEIGHTS[i]);
			meshes.add(mesh);
		}
	}

	private void append(List<Mesh3D> meshes) {
		for (Mesh3D mesh : meshes) {
			this.mesh = Mesh3DUtil.append(this.mesh, mesh);
		}
	}

	private void bridge(List<Mesh3D> meshes) {
		for (int i = 0; i < meshes.size() - 1; i++) {
			bridge(meshes.get(i), meshes.get(i + 1));
		}
	}

	private void bridge(Mesh3D m0, Mesh3D m1) {
		for (int i = 0; i < vertices; i++) {
			Mesh3DUtil.bridge(mesh, m1.getVertexAt(i), m1.getVertexAt((i + 1) % vertices), m0.getVertexAt(i),
					m0.getVertexAt((i + 1) % vertices));
		}
	}

	private void createHoles() {
		List<Face3D> faces = mesh.getFaces(vertices, vertices * 2);
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, 0.6f, 0f);
		}
		mesh.faces.removeAll(faces);
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		List<Mesh3D> meshes = new ArrayList<Mesh3D>();
		createCircles(meshes);
		append(meshes);
		bridge(meshes);
		createHoles();
		new SolidifyModifier(outerRadius - innerRadius).modify(mesh);
		mesh.translateY(-0.26f);
		new CatmullClarkModifier(2).modify(mesh);
		return mesh;
	}

}
