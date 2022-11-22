package mesh.creator.creative;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class LeonardoCubeCreator implements IMeshCreator {

	private int subdivisions;
	private float innerRadius;
	private float outerRadius;

	private Mesh3D mesh;

	public LeonardoCubeCreator() {
		subdivisions = 0;
		innerRadius = 0.9f;
		outerRadius = 1.0f;
	}
	
	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		initializeMesh();
		createConnectors();
		removeDoubles();
		subdivide();
		return mesh;
	}
	
	private void subdivide() {
		new CatmullClarkModifier(subdivisions).modify(mesh);
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void removeDoubles() {
		mesh.removeDoubles(2);
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectDoubles();
		mesh.faces.removeAll(selection.getFaces());
	}

	private void createConnectors() {
		float a = calculateConnectorRadius();

		Mesh3D top = createCross(true, false);
		top.translateY(-outerRadius + a);
		mesh.append(top);

		Mesh3D bottom = createCross(true, false);
		bottom.translateY(outerRadius - a);
		mesh.append(bottom);

		Mesh3D left = createCross(false, false);
		left.rotateZ(-Mathf.HALF_PI);
		left.translateX(-outerRadius + a);
		mesh.append(left);

		Mesh3D right = createCross(false, false);
		right.rotateZ(Mathf.HALF_PI);
		right.translateX(outerRadius - a);
		mesh.append(right);

		Mesh3D back = createCross(true, true);
		back.rotateX(-Mathf.HALF_PI);
		back.translateZ(-outerRadius + a);
		mesh.append(back);

		Mesh3D front = createCross(true, true);
		front.rotateX(-Mathf.HALF_PI);
		front.translateZ(outerRadius - a);
		mesh.append(front);
	}

	private Mesh3D createCross(boolean extrudeEnds0, boolean extrudeEnds1) {
		Mesh3D c0 = createConnector(extrudeEnds0, extrudeEnds1);
		return c0;
	}

	private Mesh3D createConnector(boolean extrudeEnds0, boolean extrudeEnds1) {
		float a = calculateConnectorRadius();
		CubeCreator creator = new CubeCreator(a);
		Mesh3D m = creator.create();

		FaceSelection selection0 = new FaceSelection(m);
		selection0.selectLeftFaces();
		selection0.selectRightFaces();

		FaceSelection selection1 = new FaceSelection(m);
		selection1.selectBackFaces();
		selection1.selectFrontFaces();

		for (Face3D f : selection0.getFaces()) {
			Mesh3DUtil.extrudeFace(m, f, 1, outerRadius - (3 * a));
			if (extrudeEnds0)
				Mesh3DUtil.extrudeFace(m, f, 1, (2 * a));
		}

		for (Face3D f : selection1.getFaces()) {
			Mesh3DUtil.extrudeFace(m, f, 1, outerRadius - (3 * a));
			if (extrudeEnds1)
				Mesh3DUtil.extrudeFace(m, f, 1, (2 * a));
		}

		return m;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

	private float calculateConnectorRadius() {
		return (outerRadius - innerRadius) * 0.5f;
	}

	public float getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(float innerRadius) {
		this.innerRadius = innerRadius;
	}

	public float getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(float outerRadius) {
		this.outerRadius = outerRadius;
	}

}
