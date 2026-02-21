package workspace;

import java.util.ArrayList;
import java.util.List;

import engine.backend.processing.LightRendererImpl;
import engine.backend.processing.ProcessingFontManager;
import engine.backend.processing.ProcessingTexture;
import engine.backend.processing.VBOProcessing;
import engine.render.Graphics;
import engine.render.Material;
import engine.render.MaterialResolver;
import engine.render.MaterialState;
import engine.resources.FilterMode;
import engine.resources.Font;
import engine.resources.Image;
import engine.resources.Model;
import engine.resources.Texture;
import engine.resources.TextureWrapMode;
import engine.scene.camera.Camera;
import engine.scene.light.Light;
import engine.vbo.VBO;
import math.Color;
import math.Mathf;
import math.Matrix4f;
import math.Vector2f;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.SubMesh;
import mesh.next.surface.SurfaceLayer;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShader;

public class GraphicsPImpl implements Graphics {

  private Color color;

  private PGraphics g;

  private LightRendererImpl lightRenderer;

  private ProcessingTexture texture;

  private Font font;

  private ProcessingFontManager fontManager;

  private boolean smoothShading;

  private PShader activeShader;

  public GraphicsPImpl(PApplet p) {
    this.g = p.g;

    lightRenderer = new LightRendererImpl(p);
    lightRenderer.setGraphics(this);

    fontManager = new ProcessingFontManager(p);

    color = Color.BLACK;

    setShader("toon.vert", "toon.frag");
  }

  @Override
  public int getWidth() {
    return g.width;
  }

  @Override
  public int getHeight() {
    return g.height;
  }

  @Override
  public void setShader(String vertexShaderName, String fragmentShaderName) {
    try {
      activeShader = g.loadShader("shaders/" + fragmentShaderName, "shaders/" + vertexShaderName);

      if (activeShader == null) {
        System.err.println("Failed to load shader.");
      } else {
        System.out.println("Shader loaded successfully.");
      }
    } catch (Exception e) {
      System.err.println("Error while loading shader: " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void lightsOff() {
    //    g.noLights();
    lightRenderer.off();
  }

  @Override
  public void render(Light light) {
    light.render(lightRenderer);
  }

  // -------------------------------------------------
  // MATERIAL / TEXTURE
  // -------------------------------------------------

  @Override
  public void setMaterial(Material material) {
    if (material == null) {
      System.err.println("Warning: Null material passed to setMaterial().");
      return;
    }

    MaterialState state = MaterialResolver.resolve(material);
    Color base = state.baseColor;

    this.smoothShading = state.smoothShading;

    if (state.useLighting) {
      lightRenderer.on();
    } else {
      lightRenderer.off();
    }

    float r = Mathf.clamp01(state.diffuseR * base.getRed());
    float g = Mathf.clamp01(state.diffuseG * base.getGreen());
    float b = Mathf.clamp01(state.diffuseB * base.getBlue());
    setColor(new Color(r, g, b));

    this.texture = null;

    if (state.diffuseTexture != null) {
      bindTexture(state.diffuseTexture, 0); // Bind to texture unit 0
    }

    this.g.specular(state.specularR, state.specularG, state.specularB);
    this.g.shininess(state.shininess);

    //    if (material.getShading() == Shading.TOON && activeShader != null) {
    //      this.g.shader(activeShader);
    //
    //      // ---- Toon shader uniforms ----
    //      activeShader.set("baseColor", color.getRed(), color.getGreen(), color.getBlue());
    //
    //      activeShader.set("steps", 3);
    //
    //      // Example directional light (view space)
    //      activeShader.set("lightDirection", 0f, -1f, 0f);
    //    } else {
    //      this.g.resetShader();
    //    }
  }

  @Override
  public void bindTexture(Texture texture, int unit) {
    // FIXME 1. unit not needed cause we are only using normal mode for textures
    // FIXME 2  bind texture should not be exposed by the graphics context
    //      if (unit == 1) {
    //	  g.textureMode(PApplet.NORMAL);
    //      }
    ProcessingTexture texture2 = (ProcessingTexture) texture.getBackendTexture();
    this.texture = texture2;
  }

  @Override
  public void unbindTexture(int unit) {}

  // -------------------------------------------------
  // MESH
  // -------------------------------------------------

  @Override
  public void fillFaces(Mesh3D mesh) {
    boolean texture = true;
    g.noStroke();
    fill();
    drawMeshFaces(mesh, texture);
  }

  @Override
  public void drawFaces(Mesh3D mesh) {
    boolean texture = false;
    g.noFill();
    stroke();
    drawMeshFaces(mesh, texture);
  }

  private void drawMeshFaces(Mesh3D mesh, boolean texture) {
    final boolean hasNormals = mesh.hasVertexNormals();
    final ArrayList<Vector3f> vertexNormals = hasNormals ? mesh.getVertexNormals() : null;
    SurfaceLayer surfaceLayer = mesh.getSurfaceLayer();

    for (int faceIdx = 0; faceIdx < mesh.getFaceCount(); faceIdx++) {
      Face3D f = mesh.getFaceAt(faceIdx);
      if (f.indices.length == 3) {
        g.beginShape(PApplet.TRIANGLES);
      } else if (f.indices.length == 4) {
        g.beginShape(PApplet.QUADS);
      } else {
        g.beginShape(PApplet.POLYGON);
      }

      if (texture) applyTexture();

      int[] vertexIndices = f.indices;

      for (int i = 0; i < vertexIndices.length; i++) {

        Vector3f v = mesh.getVertexAt(vertexIndices[i]);
        int[] uvIndices = surfaceLayer.getFaceUVIndices(faceIdx);

        if (uvIndices != null) {
          Vector2f uv = surfaceLayer.getUvAt(uvIndices[i]);
          g.vertex(v.getX(), v.getY(), v.getZ(), uv.getX(), 1 - uv.getY());
        } else {
          g.vertex(v.getX(), v.getY(), v.getZ());
        }

        //      int[] indices = f.indices;
        //      for (int i = 0; i < indices.length; i++) {
        //        Vector3f v = mesh.getVertexAt(f.indices[i]);
        //
        //        if (hasNormals && smoothShading) {
        //          Vector3f normal = vertexNormals.get(f.indices[i]);
        //          g.normal(normal.getX(), normal.getY(), normal.getZ());
        //        }
        //
        //        int uvIndex = f.getUvIndexAt(i);
        //        if (uvIndex != -1) {
        //          Vector2f uv = mesh.getUvAt(uvIndex);
        //          g.vertex(v.getX(), v.getY(), v.getZ(), uv.getX(), 1 - uv.getY());
        //        } else {
        //          g.vertex(v.getX(), v.getY(), v.getZ());
        //        }
        //      }

      }

      g.endShape();
    }

    g.resetShader();
  }

  @Override
  public void render(Model model) {
    //    Mesh3D mesh = model.getMesh();
    //	SurfaceLayer surfaceLayer = mesh.getSurfaceLayer();
    //    List<Vector3f> vertexNormals = mesh.getVertexNormals();
    //    for (SubMesh subMesh : model.getSubMeshes()) {
    //      List<Face3D> subFaces = subMesh.getFaces();
    //
    //      String materialName = subMesh.getMaterialName();
    //      Material subMaterial = model.getMaterial(materialName);
    //      setMaterial(subMaterial);
    //
    //      for (Face3D f : subFaces) {
    //        if (f.indices.length == 3) {
    //          g.beginShape(PApplet.TRIANGLES);
    //        } else if (f.indices.length == 4) {
    //          g.beginShape(PApplet.QUADS);
    //        } else {
    //          g.beginShape(PApplet.POLYGON);
    //        }
    //
    //        applyTexture();
    //
    //        int[] indices = f.indices;
    //        for (int i = 0; i < indices.length; i++) {
    //          Vector3f v = mesh.getVertexAt(f.indices[i]);
    //
    //          if (f.indices[i] < vertexNormals.size()) {
    //            Vector3f vertexNormal = vertexNormals.get(f.indices[i]);
    //            g.normal(vertexNormal.getX(), vertexNormal.getY(), vertexNormal.getZ());
    //          }
    //
    //          int uvIndex = f.getUvIndexAt(i);
    //
    //          int[] uvIndices = surfaceLayer.getFaceUVIndices(i);
    //
    //          if (uvIndex != -1) {
    //            Vector2f uv = mesh.getUvAt(uvIndex);
    //            g.vertex(v.getX(), v.getY(), v.getZ(), uv.getX(), 1 - uv.getY());
    //          } else {
    //            g.vertex(v.getX(), v.getY(), v.getZ());
    //          }
    //        }
    //        g.endShape();
    //      }
    //    }
    //
    //    g.resetShader();
  }

  @Override
  public void draw(VBO vbo) {
    applyTexture();
    VBOProcessing vboProcessing = (VBOProcessing) vbo;
    vboProcessing.draw(g);
  }

  // -------------------------------------------------
  // COLOR
  // -------------------------------------------------

  @Override
  public void clear(Color color) {
    g.background(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  @Override
  public void setColor(int red, int green, int blue) {
    color = Color.getColorFromInt(red, green, blue);
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
  }

  // -------------------------------------------------
  // STYLE
  // -------------------------------------------------

  private void stroke() {
    g.stroke(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  private void fill() {
    g.fill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  @Override
  public void strokeWeight(float weight) {
    g.strokeWeight(weight);
  }

  @Override
  public float getStrokeWeight() {
    return g.strokeWeight;
  }

  // -------------------------------------------------
  // TRANSFORM
  // -------------------------------------------------

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
    g.rotateZ(angle);
  }

  @Override
  public void rotate(float rx, float ry, float rz) {
    g.rotateX(rx);
    g.rotateY(ry);
    g.rotateZ(rz);
  }

  // -------------------------------------------------
  // MATRIX
  // -------------------------------------------------

  @Override
  public void pushMatrix() {
    g.pushMatrix();
  }

  @Override
  public void popMatrix() {
    g.popMatrix();
  }

  @Override
  public void resetMatrix() {
    g.resetMatrix();
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

  // -------------------------------------------------
  // CAMERA
  // -------------------------------------------------

  @Override
  public void camera() {
    g.camera();

  }
  
  @Override
  public void ortho() {
    g.ortho();
  }
  
  @Override
  public void applyCamera(Camera camera) {
    if (camera == null) {
      throw new IllegalArgumentException("Camera instance cannot be null.");
    }

    Matrix4f m = camera.getProjectionMatrix();
    ((PGraphicsOpenGL) g).projection.set(m.getValues());

    Vector3f target = camera.getTarget();
    Vector3f eye = camera.getTransform().getPosition();
    g.camera(eye.x, eye.y, eye.z, target.x, target.y, target.z, 0, 1, 0);
  }

  // -------------------------------------------------
  // LINE
  // -------------------------------------------------

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
  public void drawLine(Vector3f from, Vector3f to) {
    drawLine(from.x, from.y, from.z, to.x, to.y, to.z);
  }

  @Override
  public void drawLines(Vector3f[] vertices, Color[] colors) {

    g.beginShape(PApplet.LINES);

    Color last = null;

    for (int i = 0; i < vertices.length; i++) {

      Color c = colors[i];
      if (last == null || !c.equals(last)) {
        g.stroke(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        last = c;
      }

      Vector3f v = vertices[i];
      g.vertex(v.x, v.y, v.z);
    }

    g.endShape();
  }

  // -------------------------------------------------
  // RECT
  // -------------------------------------------------

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

  // -------------------------------------------------
  // OVAL
  // -------------------------------------------------

  @Override
  public void drawOval(float x, float y, float width, float height) {
    g.pushStyle();
    g.noFill();
    stroke();
    g.ellipseMode(PApplet.CORNER);
    g.ellipse(x, y, width, height);
    g.popStyle();
  }

  @Override
  public void fillOval(float x, float y, float width, float height) {
    g.pushStyle();
    g.noStroke();
    fill();
    g.ellipseMode(PApplet.CORNER);
    g.ellipse(x, y, width, height);
    g.popStyle();
  }

  // -------------------------------------------------
  // TEXT & FONT
  // -------------------------------------------------

  @Override
  public void setFont(Font font) {
    if (font == null) {
      this.font = new Font("SansSerif", 12, Font.PLAIN);
    } else {
      this.font = font;
    }
    g.textFont(fontManager.loadFont(this.font));
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
  public void textCentered(String text, float y) {
    float x = (getWidth() - textWidth(text)) * 0.5f;
    text(text, x, y);
  }

  @Override
  public void textCenteredBoth(String text) {
    float x = (getWidth() - textWidth(text)) * 0.5f;

    float ascent = textAscent();
    float descent = textDescent();
    float y = (getHeight() + ascent - descent) * 0.5f;

    text(text, x, y);
  }

  // -------------------------------------------------
  // IMAGE
  // -------------------------------------------------

  @Override
  public void drawImage(Image image, float x, float y) {
    if (!isPImage(image)) throw new IllegalArgumentException("Unsupported backend image.");

    g.image((PImage) image.getBackendImage(), x, y);
  }

  @Override
  public void drawImage(Image image, float x, float y, float width, float height) {
    if (!isPImage(image)) throw new IllegalArgumentException("Unsupported backend image.");

    g.image((PImage) image.getBackendImage(), x, y, width, height);
  }

  private boolean isPImage(Image image) {
    return image.getBackendImage() instanceof PImage;
  }

  // -------------------------------------------------
  // BACKFACE CULLING
  // -------------------------------------------------

  /**
   * Enables backface culling in the current graphics context.
   *
   * <p>Backface culling is a technique that skips rendering of polygons whose back sides are facing
   * the camera. This can significantly improve rendering performance by reducing the number of
   * polygons sent to the GPU for processing, especially in scenes where many faces are not visible
   * (e.g., inside voxel structures).
   *
   * <p>This method sets the winding order of front faces to counter-clockwise (CCW), enables face
   * culling, and specifies that back faces should be culled.
   *
   * <p>Preconditions: - This method assumes that the current `PGraphics` object is an instance of
   * `PGraphicsOpenGL`.
   */
  @Override
  public void enableFaceCulling() {
    PGraphicsOpenGL pgl = (PGraphicsOpenGL) g;
    pgl.pgl.frontFace(PGL.CCW); // Counter-clockwise winding order for front faces
    pgl.pgl.enable(PGL.CULL_FACE); // Enable face culling
    pgl.pgl.cullFace(PGL.BACK); // Cull back faces
  }

  /**
   * Disables backface culling in the current graphics context.
   *
   * <p>This method turns off the face culling feature, ensuring that all polygons, regardless of
   * their orientation relative to the camera, are rendered. Disabling face culling may be useful
   * for debugging or for specific cases where both sides of polygons need to be visible.
   *
   * <p>Preconditions: - This method assumes that the current `PGraphics` object is an instance of
   * `PGraphicsOpenGL`.
   */
  @Override
  public void disableFaceCulling() {
    PGraphicsOpenGL pgl = (PGraphicsOpenGL) g;
    pgl.pgl.disable(PGL.CULL_FACE); // Disable face culling
  }

  // -------------------------------------------------
  // DEPTH TEST
  // -------------------------------------------------

  @Override
  public void enableDepthTest() {
    g.hint(PApplet.ENABLE_DEPTH_TEST);
  }

  @Override
  public void disableDepthTest() {
    g.hint(PApplet.DISABLE_DEPTH_TEST);
  }

  private void setDepthTest(boolean depthTest) {
    if (depthTest) {
      enableDepthTest();
    } else {
      disableDepthTest();
    }
  }

  //  public void disableDepthMask() {
  //      g.hint(PApplet.DISABLE_DEPTH_MASK);
  //  }

  //  public void enableDepthMask() {
  //      g.hint(PApplet.ENABLE_ASYNC_SAVEFRAME);
  //  }

  // -------------------------------------------------

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
    g.textureWrap(getTextureWrapMode());
    g.textureMode(PApplet.NORMAL);
    g.texture(texture.getImage());
  }

  /**
   * Converts the internal {@link TextureWrapMode} to a corresponding Processing constant.
   *
   * <p>This method maps the engine's {@link TextureWrapMode} values to the equivalent constants
   * defined by the {@link PApplet} class for use in Processing-based rendering. The supported
   * mappings are:
   *
   * <ul>
   *   <li>{@link TextureWrapMode#CLAMP} -> {@link PApplet#CLAMP}
   *   <li>{@link TextureWrapMode#REPEAT} -> {@link PApplet#REPEAT}
   * </ul>
   *
   * <p>If an unsupported or unexpected wrap mode is encountered, a warning is printed to {@code
   * System.err}, and {@link PApplet#CLAMP} is returned as a fallback.
   *
   * @return the corresponding Processing constant for the texture wrap mode.
   */
  private int getTextureWrapMode() {
    TextureWrapMode textureWrapMode = texture.getTextureWrapMode();
    if (textureWrapMode == TextureWrapMode.CLAMP) return PApplet.CLAMP;
    if (textureWrapMode == TextureWrapMode.REPEAT) return PApplet.REPEAT;
    System.err.println("Warning: Unexpected texture wrap mode value: " + textureWrapMode);
    return PApplet.CLAMP;
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
}
