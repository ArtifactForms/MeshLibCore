package workspace.render;

import java.util.HashMap;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import processing.core.PApplet;
import processing.opengl.PGraphics3D;
import workspace.SceneObject;

public class ObjectSelectionRender {

	private ColorGenerator generator;
	private int width;
	private int height;
	private PApplet p;
	private PGraphics3D g3d;
	private HashMap<Integer, String> colorToObjectName;
	private HashMap<String, Integer> nameToColor;

	public ObjectSelectionRender(PApplet p) {
		colorToObjectName = new HashMap<Integer, String>();
		nameToColor = new HashMap<String, Integer>();
		generator = new ColorGenerator();
		this.p = p;
		g3d = (PGraphics3D) p.createGraphics(p.width, p.height, PApplet.P3D);
		p.registerMethod("pre", this);
	}

	public void pre() {
		if (p.width != this.width || p.height != this.height) {
			this.width = p.width;
			this.height = p.height;
			g3d = (PGraphics3D) p.createGraphics(p.width, p.height, PApplet.P3D);
		}
	}

	public String getObject(int x, int y) {
		int color = getColor(x, y);
		return colorToObjectName.get(color);
	}

	public int getColor(int x, int y) {
		int color = g3d.get(x, y);
		return color;
	}

	public void draw(List<SceneObject> sceneObjects) {
		g3d.beginDraw();
		g3d.setMatrix(p.getMatrix());
		g3d.noLights();
		g3d.background(0, 0, 0, 0);
		g3d.noStroke();
		for (SceneObject sceneObject : sceneObjects)
			drawBuffer(sceneObject);
		g3d.endDraw();
	}

	public void drawColorBuffer() {
		if (g3d == null)
			return;
		p.image(g3d, 0, 0, p.width, p.height);
	}

	private void drawBuffer(SceneObject sceneObject) {
		PGraphics3D context = g3d;
		Mesh3D mesh = sceneObject.getMesh();

		int c = -1;
		if (nameToColor.containsKey(sceneObject.getName())) {
			c = nameToColor.get(sceneObject.getName());
		} else {
			c = generator.next().getRGBA();
			nameToColor.put(sceneObject.getName(), c);
		}

		this.colorToObjectName.put(c, sceneObject.getName());

		context.fill(c);

		context.pushMatrix();

		for (int i = 0; i < mesh.faces.size(); i++) {
			Face3D f = mesh.getFaceAt(i);
			Vector3f v;

			if (f.indices.length == 3) {
				context.beginShape(PApplet.TRIANGLES);
			}

			if (f.indices.length == 4) {
				context.beginShape(PApplet.QUADS);
			}

			if (f.indices.length > 4) {
				context.beginShape();
			}

			for (int j = 0; j < f.indices.length; j++) {
				v = mesh.vertices.get(f.indices[j]);
				context.vertex(v.getX(), v.getY(), v.getZ());
			}

			if (f.indices.length > 4) {
				context.endShape(PApplet.CLOSE);
			} else {
				context.endShape();
			}
		}

		context.popMatrix();
	}

}
