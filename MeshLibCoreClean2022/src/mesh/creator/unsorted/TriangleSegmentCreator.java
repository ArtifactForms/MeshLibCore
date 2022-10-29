package mesh.creator.unsorted;

import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.PlanarMidEdgeCenterModifier;
import mesh.wip.Mesh3DUtil;

public class TriangleSegmentCreator implements IMeshCreator {

	private float size;
	private float height;
	private float scaleExtrude;
	private Mesh3D mesh;
	
	public TriangleSegmentCreator() {
		super();
		this.size = 1f;
		this.height = 0.125f;
		this.scaleExtrude = 0.9f;
	}
	
	private void createVertices() {
		mesh.add(new Vector3f(-0.866f, 0f, -0.5f));
		mesh.add(new Vector3f(0.866f, 0f, -0.5f));
		mesh.add(new Vector3f(0f, 0f, 1f));
	}
	
	private void createFaces() {
		mesh.add(new Face3D(0, 2, 1));
	}
	
	private void transform() {
		mesh.scale(size);
	}
	
	private void extrude() {
		new PlanarMidEdgeCenterModifier().modify(mesh);
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, scaleExtrude, 0f);
		}
		mesh.faces.removeAll(faces);
	}
	
	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createVertices();
		createFaces();
		extrude();
		transform();
		new SolidifyModifier(height).modify(mesh);
		mesh.translateY(height / 2f);
		return mesh;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}
