package workspace.ui;

import java.util.List;

import engine.render.Material;
import engine.resources.Texture;
import engine.scene.camera.Camera;
import engine.scene.light.Light;
import math.Matrix4f;
import mesh.Mesh3D;

public interface Graphics3D extends Graphics2D {

  void translate(float x, float y, float z);

  void scale(float sx, float sy, float sz);

  void rotateX(float angle);

  void rotateY(float angle);

  void rotateZ(float angle);

  void rotate(float rx, float ry, float rz);

  void render(Light light);

  void fillFaces(Mesh3D mesh);

  void renderInstances(Mesh3D mesh, List<Matrix4f> instanceTransforms);

  void setShader(String vertexShaderName, String fragmentShaderName);

  void enableDepthTest();

  void disableDepthTest();

  void setMaterial(Material material);

  void drawLine(float x1, float y1, float z1, float x2, float y2, float z2);

  void camera();

  void lightsOff();

  void setWireframeMode(boolean wireframeMode);

  void bindTexture(Texture texture, int unit);

  void unbindTexture(int unit);

  /**
   * Sets the global ambient light color for the scene.
   *
   * @param color The color of the ambient light. Must not be null.
   */
  void setAmbientColor(math.Color color);

  /**
   * Gets the current global ambient light color.
   *
   * @return The current ambient light color.
   */
  math.Color getAmbientColor();

  void applyMatrix(Matrix4f matrix);

  void applyCamera(Camera camera);
}
