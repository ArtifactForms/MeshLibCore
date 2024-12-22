package workspace.render;

import java.util.Collection;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.UpdateFaceNormalsModifier;
import mesh.selection.FaceSelection;
import mesh.util.VertexNormals;
import processing.core.PApplet;
import processing.opengl.PGraphics3D;

public class Mesh3DRenderer {

  private PApplet context;

  public Mesh3DRenderer(PApplet context) {
    this.context = context;
  }

  public void drawVertices(Mesh3D mesh) {
    context.pushMatrix();
    context.beginShape(PApplet.POINTS);
    for (int i = 0; i < mesh.vertices.size(); i++) {
      Vector3f v = mesh.vertices.get(i);
      context.vertex(v.getX(), v.getY(), v.getZ());
    }
    context.endShape();
    context.popMatrix();
  }

  public void drawVertices(Mesh3D mesh, float size) {
    context.pushStyle();
    context.pushMatrix();
    context.strokeWeight(size);
    context.beginShape(PApplet.POINTS);
    for (int i = 0; i < mesh.vertices.size(); i++) {
      Vector3f v = mesh.vertices.get(i);
      context.vertex(v.getX(), v.getY(), v.getZ());
    }
    context.endShape();
    context.popMatrix();
    context.popStyle();
  }

  public void drawFaceNormals(Mesh3D mesh, float length) {
    context.pushMatrix();
    context.beginShape(PApplet.LINES);
    for (Face3D f : mesh.faces) {
      Vector3f c = mesh.calculateFaceCenter(f);
      Vector3f n = mesh.calculateFaceNormal(f);
      Vector3f v = c.add(n.mult(length));
      context.vertex(c.getX(), c.getY(), c.getZ());
      context.vertex(v.getX(), v.getY(), v.getZ());
    }
    context.endShape();
    context.popMatrix();
  }

  public void drawFaceNormals(Mesh3D mesh) {
    drawFaceNormals(mesh, 0.1f);
  }

  public void drawVertexNormals(Mesh3D mesh, List<Vector3f> normals) {
    float length = 0.1f;
    context.pushMatrix();
    context.beginShape(PApplet.LINES);
    for (int i = 0; i < mesh.vertices.size(); i++) {
      Vector3f v0 = mesh.vertices.get(i);
      Vector3f v1 = v0.add(normals.get(i).mult(length));
      context.vertex(v0.getX(), v0.getY(), v0.getZ());
      context.vertex(v1.getX(), v1.getY(), v1.getZ());
    }
    context.endShape();
    context.popMatrix();
  }

  public void drawFaces(Mesh3D mesh, Collection<Face3D> faces, Shading shading) {
    switch (shading) {
      case FLAT:
        drawFacesFlat((PGraphics3D) context.g, mesh, faces);
        break;
      case SMOOTH:
        drawFacesSmooth(mesh, faces);
        break;
    }
  }

  private void drawFacesSmooth(Mesh3D mesh, Collection<Face3D> faces) {
    context.pushMatrix();

    mesh.apply(new UpdateFaceNormalsModifier());

    VertexNormals normals = new VertexNormals(mesh);

    for (Face3D f : faces) {
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

      Vector3f fn = f.normal;
      context.normal(fn.getX(), fn.getY(), fn.getZ());

      for (int i = 0; i < f.indices.length; i++) {
        Vector3f vn = normals.getVertexNormals().get(f.indices[i]);
        v = mesh.vertices.get(f.indices[i]);
        context.normal(vn.getX(), vn.getY(), vn.getZ());
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

  //	private void drawFacesFlat(PGraphics3D context, Mesh3D mesh,
  //	    Collection<Face3D> faces) {
  //		context.pushMatrix();
  //
  //		for (Face3D f : faces) {
  //			Vector3f v;
  //
  //			if (f.indices.length == 3) {
  //				context.beginShape(PApplet.TRIANGLES);
  //			}
  //
  //			if (f.indices.length == 4) {
  //				context.beginShape(PApplet.QUADS);
  //			}
  //
  //			if (f.indices.length > 4) {
  //				context.beginShape();
  //			}
  //
  //			for (int i = 0; i < f.indices.length; i++) {
  //				v = mesh.vertices.get(f.indices[i]);
  //				context.vertex(v.getX(), v.getY(), v.getZ());
  //			}
  //
  //			if (f.indices.length > 4) {
  //				context.endShape(PApplet.CLOSE);
  //			} else {
  //				context.endShape();
  //			}
  //
  //		}
  //
  //		context.popMatrix();
  //	}

  private void drawFacesFlat(PGraphics3D context, Mesh3D mesh, Collection<Face3D> faces) {
    for (Face3D f : faces) {
      Vector3f v;

      // Use specific shape types based on face vertex count
      if (f.indices.length == 3) {
        context.beginShape(PApplet.TRIANGLES);
      } else if (f.indices.length == 4) {
        context.beginShape(PApplet.QUADS);
      } else {
        context.beginShape(PApplet.POLYGON); // Handle polygons directly
      }

      // Set flat shading normal (assuming a calculateNormal method exists)
      //			Vector3f normal = f.calculateNormal(mesh);
      //			context.normal(normal.getX(), normal.getY(), normal.getZ());

      // Add vertices
      for (int index : f.indices) {
        v = mesh.vertices.get(index);
        context.vertex(v.getX(), v.getY(), v.getZ());
      }

      context.endShape(PApplet.CLOSE); // Ensure the shape is closed
    }
  }

  public void drawFacesColored(Mesh3D mesh) {
    context.pushMatrix();
    for (Face3D f : mesh.faces) {
      context.fill(f.color.getRedInt(), f.color.getGreenInt(), f.color.getBlueInt());

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

      for (int i = 0; i < f.indices.length; i++) {
        v = mesh.vertices.get(f.indices[i]);
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

  public void drawFaces(Mesh3D mesh, Collection<Face3D> faces) {
    drawFaces(mesh, faces, Shading.FLAT);
  }

  public void drawFaces(Mesh3D mesh, FaceSelection selection) {
    drawFaces(mesh, selection.getFaces(), Shading.FLAT);
  }

  public void drawFaces(Mesh3D mesh) {
    drawFaces(mesh, mesh.faces, Shading.FLAT);
  }

  public void drawEdges(Mesh3D mesh) {
    for (Face3D f : mesh.faces) {
      for (int i = 0; i <= f.indices.length; i++) {
        Vector3f v0 = mesh.vertices.get(f.indices[i % f.indices.length]);
        Vector3f v1 = mesh.vertices.get(f.indices[(i + 1) % f.indices.length]);
        context.line(v0.getX(), v0.getY(), v0.getZ(), v1.getX(), v1.getY(), v1.getZ());
      }
    }
  }

  public void drawGizmo(float length) {
    context.pushStyle();
    context.noFill();
    context.stroke(255, 0, 0);
    context.line(0f, 0f, 0f, length, 0, 0);
    context.stroke(0, 255, 0);
    context.line(0f, 0f, 0f, 0, length, 0);
    context.stroke(0, 0, 255);
    context.line(0f, 0f, 0f, 0, 0, length);
    context.popStyle();
  }

  public void drawGrid(int rows, int cols, float size) {
    context.pushStyle();
    context.noFill();

    context.pushMatrix();
    context.rotateX(PApplet.radians(-90));
    context.translate(-cols * size / 2, -rows * size / 2);

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        context.rect(j * size, i * size, size, size);
      }
    }

    context.popMatrix();
    context.popStyle();
  }

  public void drawCheckerGrid(int rows, int cols, float size) {
    context.pushStyle();
    context.noStroke();

    context.pushMatrix();
    context.rotateX(PApplet.radians(-90));
    context.translate(-cols * size / 2, -rows * size / 2);

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if ((i + j) % 2 == 0) context.fill(132, 137, 154);
        else context.fill(150, 156, 173);
        context.rect(j * size, i * size, size, size);
      }
    }

    context.popMatrix();
    context.popStyle();
  }

  public void lights(float scale) {
    float x = 4.0762f;
    float y = -5.9039f;
    float z = -1.055f;

    context.pushMatrix();
    context.pushStyle();
    context.noLights();
    context.ambient(0);

    context.stroke(0);
    context.line(x, y, z, x, 0, z);

    context.translate(x, y, z);
    context.box(0.1f * scale);

    context.pointLight(255, 255, 255, x * scale, y * scale, z * scale);

    context.popStyle();
    context.popMatrix();
  }
}
