package workspace;

import java.util.List;

import engine.processing.LightGizmoRenderer;
import engine.processing.LightRendererImpl;
import engine.render.Material;
import engine.scene.camera.Camera;
import engine.scene.light.Light;
import engine.scene.light.LightRenderer;
import math.Matrix4f;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PShader;
import workspace.render.Mesh3DRenderer;
import workspace.ui.Color;
import workspace.ui.Graphics;

public class GraphicsPImpl implements Graphics {

  private boolean wireframeMode;

  private Color color;

  private math.Color ambientColor;

  private PGraphics g;

  private Mesh3DRenderer renderer;

  private LightRenderer lightRenderer;

  private LightGizmoRenderer lightGizmoRenderer;

  @Override
  public void setAmbientColor(math.Color ambientColor) {
    this.ambientColor = ambientColor;
  }

  @Override
  public math.Color getAmbientColor() {
    return ambientColor;
  }

  @Override
  public void setWireframeMode(boolean wireframeMode) {
    this.wireframeMode = wireframeMode;
  }

  public GraphicsPImpl(PApplet p) {
    this.g = p.g;
    renderer = new Mesh3DRenderer(p);

    lightRenderer = new LightRendererImpl(p);
    lightRenderer.setGraphics(this);

    lightGizmoRenderer = new LightGizmoRenderer(p);
    lightGizmoRenderer.setGraphics(this);

    color = Color.BLACK;
    ambientColor = math.Color.WHITE;
  }

  @Override
  public void fillFaces(Mesh3D mesh) {
    if (wireframeMode) {
      g.noFill();
      stroke();
      renderer.drawFaces(mesh);
    } else {
      g.noStroke();
      fill();
      renderer.drawFaces(mesh);
    }
  }

  @Override
  public void renderInstances(Mesh3D mesh, List<Matrix4f> instanceTransforms) {
    if (mesh.getFaces().isEmpty() || mesh.getVertices().isEmpty()) {
      return;
    }

    setColor(Color.WHITE);

    for (Matrix4f transform : instanceTransforms) {
      g.pushMatrix();
      applyTransform(transform);
      drawMeshFaces(mesh);
      g.popMatrix();
    }
  }

  private void applyTransform(Matrix4f transform) {
    float[] matrix = transform.getValues();

    g.applyMatrix(
        matrix[0],
        matrix[1],
        matrix[2],
        matrix[3],
        matrix[4],
        matrix[5],
        matrix[6],
        matrix[7],
        matrix[8],
        matrix[9],
        matrix[10],
        matrix[11],
        matrix[12],
        matrix[13],
        matrix[14],
        matrix[15]);
  }

  private void drawMeshFaces(Mesh3D mesh) {
    //    g.beginShape(PApplet.TRIANGLES);
    //    for (Face3D f : mesh.getFaces()) {
    //      for (int index : f.indices) {
    //        Vector3f v = mesh.vertices.get(index);
    //        g.vertex(v.getX(), v.getY(), v.getZ());
    //      }
    //    }
    //    g.endShape();
    for (Face3D f : mesh.getFaces()) {
      if (f.indices.length == 3) {
        g.beginShape(PApplet.TRIANGLES);
      } else if (f.indices.length == 4) {
        g.beginShape(PApplet.QUADS);
      } else {
        g.beginShape(PApplet.POLYGON);
      }
      for (int index : f.indices) {
        Vector3f v = mesh.vertices.get(index);
        g.vertex(v.getX(), v.getY(), v.getZ());
      }
      g.endShape(PApplet.CLOSE);
    }
  }

  @Override
  public int getWidth() {
    return g.width;
  }

  @Override
  public int getHeight() {
    return g.height;
  }

  private void stroke() {
    g.stroke(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  private void fill() {
    g.fill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  @Override
  public void pushMatrix() {
    g.pushMatrix();
  }

  @Override
  public void popMatrix() {
    g.popMatrix();
  }

  @Override
  public void scale(float sx, float sy, float sz) {
    g.scale(sx, sy, sz);
  }

  @Override
  public void scale(float sx, float sy) {
    g.scale(sx, sy);
  }

  @Override
  public void translate(float x, float y) {
    g.translate(x, y, 0);
  }

  @Override
  public void translate(float x, float y, float z) {
    g.translate(x, y, z);
  }

  @Override
  public void strokeWeight(float weight) {
    g.strokeWeight(weight);
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public void setColor(int red, int green, int blue) {
    color = new Color(red, green, blue);
  }

  @Override
  public void setColor(math.Color color) {
    this.color =
        new Color(color.getRedInt(), color.getGreenInt(), color.getBlueInt(), color.getAlphaInt());
  }

  @Override
  public void drawRect(float x, float y, float width, float height) {
    g.pushStyle();
    g.noFill();
    stroke();
    g.rectMode(PApplet.CORNER);
    g.rect(x, y, width, height);
    g.popStyle();
  }

  @Override
  public void fillRect(float x, float y, float width, float height) {
    g.pushStyle();
    g.noStroke();
    fill();
    g.rectMode(PApplet.CORNER);
    g.rect(x, y, width, height);
    g.popStyle();
  }

  @Override
  public void drawRoundRect(float x, float y, float width, float height, float radii) {
    g.pushStyle();
    g.noFill();
    stroke();
    g.rectMode(PApplet.CORNER);
    g.rect(x, y, width, height, radii);
    g.popStyle();
  }

  @Override
  public void fillRoundRect(float x, float y, float width, float height, float radii) {
    g.pushStyle();
    g.noStroke();
    fill();
    g.rectMode(PApplet.CORNER);
    g.rect(x, y, width, height, radii);
    g.popStyle();
  }

  @Override
  public void drawLine(float x1, float y1, float x2, float y2) {
    g.pushStyle();
    g.noFill();
    stroke();
    g.line(x1, y1, x2, y2);
    g.popStyle();
  }

  @Override
  public void drawLine(float x1, float y1, float z1, float x2, float y2, float z2) {
    g.pushStyle();
    g.noFill();
    stroke();
    g.line(x1, y1, z1, x2, y2, z2);
    g.popStyle();
  }

  @Override
  public void drawOval(float x, float y, float width, float height) {
    g.pushStyle();
    g.noFill();
    stroke();
    g.ellipseMode(PApplet.CORNER);
    g.ellipse(x, y, height, width);
    g.popStyle();
  }

  @Override
  public void fillOval(float x, float y, float width, float height) {
    g.pushStyle();
    g.noStroke();
    fill();
    g.ellipseMode(PApplet.CORNER);
    g.ellipse(x, y, height, width);
    g.popStyle();
  }

  @Override
  public void textSize(float size) {
    g.textSize(size);
  }

  @Override
  public float getTextSize() {
    return g.textSize;
  }

  @Override
  public float textWidth(String text) {
    return g.textWidth(text);
  }

  @Override
  public float textAscent() {
    return g.textAscent();
  }

  @Override
  public float textDescent() {
    return g.textDescent();
  }

  @Override
  public void text(String text, float x, float y) {
    fill();
    g.text(text, x, y);
  }

  @Override
  public void enableDepthTest() {
    g.hint(PApplet.ENABLE_DEPTH_TEST);
  }

  @Override
  public void disableDepthTest() {
    g.hint(PApplet.DISABLE_DEPTH_TEST);
  }

  @Override
  public void rotate(float angle) {
    g.rotate(angle);
  }

  @Override
  public void rotateX(float angle) {
    g.rotateX(angle);
  }

  @Override
  public void rotateY(float angle) {
    g.rotateY(angle);
  }

  @Override
  public void rotateZ(float angle) {
    g.rotate(angle);
  }

  public void camera() {
    g.camera();
    //		// Push the current transformation state
    //		pushMatrix();
    //
    //		// Set up orthographic projection for 2D UI rendering
    //		// Adjust these parameters depending on your screen size or UI layout
    //		float left = 0;
    //		float right = getWidth();
    //		float bottom = getHeight();
    //		float top = 0;
    //		float near = -1;
    //		float far = 1;
    //
    //		Matrix4f orthoProjection = new Matrix4f().setOrtho(left, right, bottom, top,
    //		    near, far);
    //		setProjectionMatrix(orthoProjection);
    //
    //		// Disable depth testing to ensure 2D UI renders on top
    //		disableDepthTest();
  }

  //	@Override
  //	public void setMaterial(Material material) {
  //		if (material == null) {
  //			System.err.println("Warning: Null material passed to setMaterial().");
  //			return;
  //		}
  //
  //		// Extract material properties
  //		math.Color color = material.getColor();
  //		float[] ambient = material.getAmbient();
  //		float[] diffuse = material.getDiffuse(); // NEW
  //		float[] specular = material.getSpecular();
  //		float shininess = material.getShininess();
  //
  //		// Ensure ambient, diffuse, and specular arrays are valid
  //		if (ambient == null || ambient.length < 3) {
  //			ambient = new float[] { 0.2f, 0.2f, 0.2f }; // Default ambient
  //			System.err
  //			    .println("Warning: Material ambient property is null or incomplete.");
  //		}
  //		if (diffuse == null || diffuse.length < 3) {
  //			diffuse = new float[] { 1.0f, 1.0f, 1.0f }; // Default diffuse
  //			System.err
  //			    .println("Warning: Material diffuse property is null or incomplete.");
  //		}
  //		if (specular == null || specular.length < 3) {
  //			specular = new float[] { 1.0f, 1.0f, 1.0f }; // Default specular
  //			System.err.println(
  //			    "Warning: Material specular property is null or incomplete.");
  //		}
  //
  //		// Apply material properties
  //		setColor(color != null ? color : math.Color.WHITE); // Default to white
  //
  //		math.Color ambientColor = new math.Color(this.ambientColor);
  //		ambientColor.multLocal(ambient[0], ambient[1], ambient[2], 1);
  //		ambientColor.clampLocal();
  //
  //		g.ambient(ambientColor.getRedInt(), ambientColor.getGreenInt(),
  //		    ambientColor.getBlueInt());
  //		// Set diffuse indirectly using fill() for now
  //		// FIXME
  //		// g.fill(diffuse[0] * 255, diffuse[1] * 255, diffuse[2] * 255);
  //		g.specular(specular[0], specular[1], specular[2]);
  //		g.shininess(shininess);
  //	}

  @Override
  public void setMaterial(Material material) {
    if (material == null) {
      System.err.println("Warning: Null material passed to setMaterial().");
      return;
    }

    // Extract material properties
    math.Color color = material.getColor();
    float[] ambient = material.getAmbient();
    float[] diffuse = material.getDiffuse();
    float[] specular = material.getSpecular();
    float shininess = material.getShininess();

    // Validate and set defaults for ambient, diffuse, and specular arrays
    if (ambient == null || ambient.length < 3) {
      ambient = new float[] {0.2f, 0.2f, 0.2f}; // Default ambient
      System.err.println(
          "Warning: Material ambient property is null or incomplete. Using default.");
    }
    if (diffuse == null || diffuse.length < 3) {
      diffuse = new float[] {1.0f, 1.0f, 1.0f}; // Default diffuse
      System.err.println(
          "Warning: Material diffuse property is null or incomplete. Using default.");
    }
    if (specular == null || specular.length < 3) {
      specular = new float[] {1.0f, 1.0f, 1.0f}; // Default specular
      System.err.println(
          "Warning: Material specular property is null or incomplete. Using default.");
    }

    // Apply material properties
    setColor(color != null ? color : math.Color.WHITE); // Default to white

    // Calculate and apply ambient color
    math.Color ambientColor = new math.Color(this.ambientColor);
    ambientColor.multLocal(ambient[0], ambient[1], ambient[2], 1.0f);
    ambientColor.clampLocal();
    g.ambient(ambientColor.getRedInt(), ambientColor.getGreenInt(), ambientColor.getBlueInt());

    // Set diffuse color
    math.Color diffuseColor = new math.Color(color != null ? color : math.Color.WHITE);
    diffuseColor.multLocal(diffuse[0], diffuse[1], diffuse[2], 1.0f);
    diffuseColor.clampLocal();
    g.fill(diffuseColor.getRedInt(), diffuseColor.getGreenInt(), diffuseColor.getBlueInt());

    // Set specular and shininess properties
    g.specular(specular[0] * 255, specular[1] * 255, specular[2] * 255);
    g.shininess(shininess);
  }

  @Override
  public void setShader(String vertexShaderName, String fragmentShaderName) {
    try {
      // Correctly load the shader using Processing's loadShader
      PShader shader = g.loadShader("shaders/" + vertexShaderName, "shaders/" + fragmentShaderName);

      if (shader == null) {
        System.err.println(
            "Failed to load shader: " + vertexShaderName + ", " + fragmentShaderName);
      } else {
        g.shader(shader); // Apply shader to PGraphics
        System.out.println("Shader applied successfully.");
      }
    } catch (Exception e) {
      System.err.println("Error while loading shader: " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void lightsOff() {
    g.noLights();
  }

  @Override
  public void render(Light light) {
    light.render(lightRenderer);
    light.render(lightGizmoRenderer);
  }

  @Override
  public void lookAt(Vector3f eye, Vector3f target, Vector3f up) {
    //		g.camera(eye.x, eye.y, eye.z, target.x, target.y, target.z, up.x, up.y,
    //		    up.z);
  }

  @Override
  public void applyMatrix(Matrix4f matrix) {
    if (matrix == null) return;
    float[] values = matrix.getValues();
    g.applyMatrix(
        values[0],
        values[1],
        values[2],
        values[3],
        values[4],
        values[5],
        values[6],
        values[7],
        values[8],
        values[9],
        values[10],
        values[11],
        values[12],
        values[13],
        values[14],
        values[15]);
  }

  @Override
  public void applyCamera(Camera camera) {
    if (camera == null) {
      throw new IllegalArgumentException("Camera instance cannot be null.");
    }
    applyMatrix(camera.getViewMatrix());

    //      // Get camera parameters
    //      Vector3f position = camera.getPosition();
    //      float pitch = camera.getPitch();
    //      float yaw = camera.getYaw();
    //      Matrix4f viewMatrix = camera.getViewMatrix();
    Matrix4f projectionMatrix = camera.getProjectionMatrix();
    //
    //      // Set the view matrix
    //      g.camera(
    //          position.x, position.y, position.z, // Camera position
    //          position.x + (float) Math.cos(yaw), // Look at position (X)
    //          position.y + (float) Math.sin(pitch), // Look at position (Y)
    //          position.z + (float) Math.sin(yaw), // Look at position (Z)
    //          0, 1, 0 // Up vector
    //      );
    //
    // Set the projection matrix (if applicable)
    if (projectionMatrix != null) {
      //          applyProjectionMatrix(projectionMatrix);
    }
  }

  private void applyProjectionMatrix(Matrix4f projectionMatrix) {
    float near = projectionMatrix.get(2, 3) / (projectionMatrix.get(2, 2) - 1);
    float far = projectionMatrix.get(3, 2) / (projectionMatrix.get(2, 2) + 1);

    float left = near * (projectionMatrix.get(2, 0) - 1) / projectionMatrix.get(0, 0);
    float right = near * (projectionMatrix.get(2, 0) + 1) / projectionMatrix.get(0, 0);
    float bottom = near * (projectionMatrix.get(2, 1) - 1) / projectionMatrix.get(1, 1);
    float top = near * (projectionMatrix.get(2, 1) + 1) / projectionMatrix.get(1, 1);

    g.frustum(left, right, bottom, top, near, far);
  }

  //	@Override
  //	public void setViewMatrix(Matrix4f viewMatrix) {
  ////		float[] viewValues = viewMatrix.getValues();
  ////		g.applyMatrix(viewValues[0], viewValues[1], viewValues[2], viewValues[3],
  ////		    viewValues[4], viewValues[5], viewValues[6], viewValues[7],
  ////		    viewValues[8], viewValues[9], viewValues[10], viewValues[11],
  ////		    viewValues[12], viewValues[13], viewValues[14], viewValues[15]);
  //////		g.camera(0, 0, 100, 0, 0, 0, 0, 1, 0);
  //	}
  //
  //	@Override
  //	public void setProjectionMatrix(Matrix4f projectionMatrix) {
  ////		float[] projectionValues = projectionMatrix.getValues();
  ////		g.applyMatrix(projectionValues[0], projectionValues[1], projectionValues[2],
  ////		    projectionValues[3], projectionValues[4], projectionValues[5],
  ////		    projectionValues[6], projectionValues[7], projectionValues[8],
  ////		    projectionValues[9], projectionValues[10], projectionValues[11],
  ////		    projectionValues[12], projectionValues[13], projectionValues[14],
  ////		    projectionValues[15]);
  //	}

}
