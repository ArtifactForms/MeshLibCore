package workspace.render;

import java.util.Collection;
import java.util.HashMap;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import processing.core.PApplet;
import processing.opengl.PGraphics3D;

public class SelectionRenderer {

	private int width;
	private int height;
	private PApplet p;
	private PGraphics3D g3d;
	private HashMap<Integer, Face3D> colorToFaceMap;

	public SelectionRenderer(PApplet p) {
		this.p = p;
		g3d = (PGraphics3D) p.createGraphics(p.width, p.height, PApplet.P3D);
		colorToFaceMap = new HashMap<Integer, Face3D>();
		p.registerMethod("pre", this);
	}

	public void pre() {
		if (p.width != this.width || p.height != this.height) {
			this.width = p.width;
			this.height = p.height;
			g3d = (PGraphics3D) p.createGraphics(p.width, p.height, PApplet.P3D);
		}
	}

	public Face3D getFace(int x, int y) {
		int color = getColor(x, y);
		return colorToFaceMap.get(color);
	}

	public int getColor(int x, int y) {
		int color = g3d.get(x, y);
		return color;
	}

	public void drawFaces(Mesh3D mesh) {
		g3d.beginDraw();
		g3d.setMatrix(p.getMatrix());
		g3d.noLights();
		g3d.background(0, 0, 0, 0);
		g3d.noStroke();
		drawFacesBuffer(mesh, mesh.faces);
		g3d.endDraw();
	}

	public void drawColorBuffer() {
		if (g3d == null)
			return;
		p.image(g3d, 0, 0, p.width, p.height);
	}

	private void drawFacesBuffer(Mesh3D mesh, Collection<Face3D> faces) {
		PGraphics3D context = g3d;

		context.pushMatrix();

		ColorGenerator generator = new ColorGenerator();
		this.colorToFaceMap.clear();

		for (int i = 0; i < mesh.faces.size(); i++) {
			Face3D f = mesh.getFaceAt(i);
			Vector3f v;
			int c = generator.next().getRGBA();
			context.fill(c);
			this.colorToFaceMap.put(c, f);

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
