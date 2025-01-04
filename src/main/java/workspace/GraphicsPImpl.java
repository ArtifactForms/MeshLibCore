package workspace;

import java.util.List;

import engine.processing.LightGizmoRenderer;
import engine.processing.LightRendererImpl;
import engine.processing.ProcessingTexture;
import engine.render.Material;
import engine.resources.FilterMode;
import engine.resources.Image;
import engine.resources.Texture;
import engine.scene.camera.Camera;
import engine.scene.light.Light;
import engine.scene.light.LightRenderer;
import math.Matrix4f;
import math.Vector2f;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShader;
import workspace.render.Mesh3DRenderer;
import workspace.ui.Color;
import workspace.ui.Graphics;

public class GraphicsPImpl implements Graphics {

  private boolean wireframeMode;

  private Color color;

  private math.Color ambientColor;

  private PGraphics g;

  private PApplet p;

  private Mesh3DRenderer renderer;

  private LightRenderer lightRenderer;

  private LightGizmoRenderer lightGizmoRenderer;

  public static int faceCount = 0;

  public static int vertexCount = 0;

  private ProcessingTexture texture;

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
    this.p = p;
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
    faceCount += mesh.faces.size();
    vertexCount += mesh.vertices.size();
    if (wireframeMode) {
      g.noFill();
      stroke();
      //      renderer.drawFaces(mesh);
      drawMeshFaces(mesh);
    } else {
      g.noStroke();
      fill();
      drawMeshFaces(mesh);
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

  /**
   * Applies the specified texture to the current rendering context with the appropriate sampling
   * mode.
   *
   * <p>The filter mode of the texture is used to determine the sampling mode, which is then applied
   * to the rendering context. If the texture is not initialized, this method does nothing. If an
   * unexpected filter mode is encountered, a warning is logged, and BILINEAR mode is used as a
   * fallback.
   *
   * @see #getSamplingMode(FilterMode)
   * @see processing.opengl.Texture
   */
  private void applyTexture() {
    if (texture == null || texture.getImage() == null) {
      return; // Ensure texture is properly initialized before applying it.
    }
    ((PGraphicsOpenGL) g).textureSampling(getSamplingMode(texture.getFilterMode()));
    g.textureMode(PApplet.NORMAL);
    g.texture(texture.getImage());
  }

  /**
   * Maps the given filter mode to the corresponding texture sampling mode.
   *
   * <p>Supported mappings:
   *
   * <ul>
   *   <li>POINT -> 2
   *   <li>LINEAR -> 3
   *   <li>BILINEAR -> 4
   *   <li>TRILINEAR -> 5
   * </ul>
   *
   * <p>If an unexpected filter mode is provided, a warning is logged, and BILINEAR mode (4) is
   * returned.
   *
   * @param filterMode the filter mode to map
   * @return the corresponding sampling mode
   * @see processing.opengl.Texture
   */
  private int getSamplingMode(FilterMode filterMode) {
    switch (filterMode) {
      case POINT:
        return 2;
      case LINEAR:
        return 3;
      case BILINEAR:
        return 4;
      case TRILINEAR:
        return 5;
      default:
        System.err.println("Warning: Unexpected filter mode value: " + filterMode);
        return 4; // Default to BILINEAR
    }
  }

  private void drawMeshFaces(Mesh3D mesh) {
    for (Face3D f : mesh.getFaces()) {
      if (f.indices.length == 3) {
        g.beginShape(PApplet.TRIANGLES);
      } else if (f.indices.length == 4) {
        g.beginShape(PApplet.QUADS);
      } else {
        g.beginShape(PApplet.POLYGON);
      }

      applyTexture();

      int[] indices = f.indices;
      for (int i = 0; i < indices.length; i++) {
        Vector3f v = mesh.vertices.get(f.indices[i]);
        int uvIndex = f.getUvIndexAt(i);
        if (uvIndex != -1) {
          Vector2f uv = mesh.getUvAt(uvIndex);
          g.vertex(v.getX(), v.getY(), v.getZ(), uv.getX(), 1 - uv.getY());
        } else {
          g.vertex(v.getX(), v.getY(), v.getZ());
        }
      }
      g.endShape();
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

  @Override
  public void rotate(float rx, float ry, float rz) {
    g.rotateX(rx);
    g.rotateY(ry);
    g.rotateZ(rz);
  }

  public void camera() {
    g.camera();
  }

  @Override
  public void setMaterial(Material material) {
    if (material == null) {
      System.err.println("Warning: Null material passed to setMaterial().");
      return;
    }

    this.texture = null;

    math.Color color = material.getColor();
    // Apply material properties
    setColor(color != null ? color : math.Color.WHITE); // Default to white

    if (!material.isUseLighting()) {
      g.noLights();
      return;
    }

    // Extract material properties

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
  public void bindTexture(Texture texture, int unit) { // TODO Auto-generated method stub
    //      if (unit == 1) {
    //	  g.textureMode(PApplet.NORMAL);
    //      }
    ProcessingTexture texture2 = (ProcessingTexture) texture.getBackendTexture();
    this.texture = texture2;
  }

  @Override
  public void unbindTexture(int unit) { // TODO Auto-generated method stub
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

    //    g.resetMatrix();
    //    Matrix4f  m = camera.getViewProjectionMatrix();
    //
    //
    //  Vector3f target = camera.getTarget();
    //  Vector3f eye = camera.getTransform().getPosition();
    //    Matrix4f look = Matrix4f.lookAt(eye, target, new Vector3f(0, 1, 0));
    //
    //    m = m.multiply(look);

    //    g.getMatrix().set(m.getValues());

    float fov = camera.getFieldOfView();
    float aspect = camera.getAspectRatio();
    float near = camera.getNearPlane();
    float far = camera.getFarPlane();
    //    g.perspective(fov, aspect, near, far);

    Matrix4f m = camera.getProjectionMatrix();
    ((PGraphicsOpenGL) g).projection.set(m.getValues());

    Vector3f target = camera.getTarget();
    Vector3f eye = camera.getTransform().getPosition();
    g.camera(eye.x, eye.y, eye.z, target.x, target.y, target.z, 0, 1, 0);
  }

  @Override
  public void drawImage(Image image, float x, float y) {
    if (image.getBackendImage() instanceof PImage) {
      g.image((PImage) image.getBackendImage(), x, y);
    } else {
      throw new IllegalArgumentException("Unsupported image backend.");
    }
  }

  @Override
  public void drawImage(Image image, float x, float y, float width, float height) {
    if (image.getBackendImage() instanceof PImage) {
      g.image((PImage) image.getBackendImage(), x, y, width, height);
    } else {
      throw new IllegalArgumentException("Unsupported image backend.");
    }
  }

  @Override
  public void clear(math.Color color) {
    g.background(color.getRedInt(), color.getGreenInt(), color.getBlueInt(), color.getAlphaInt());
  }
}
