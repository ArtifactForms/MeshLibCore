package engine.rendering;

import engine.resources.Model;
import engine.scene.camera.Camera;
import engine.scene.light.Light;
import engine.vbo.VBO;
import math.Color;
import math.Matrix4f;
import math.Vector3f;
import mesh.Mesh3D;

public interface Graphics3D {

  void translate(float x, float y, float z);

  void scale(float sx, float sy, float sz);

  void rotateX(float angle);

  void rotateY(float angle);

  void rotateZ(float angle);

  void rotate(float rx, float ry, float rz);

  void render(Light light);

  void drawFaces(Mesh3D mesh);

  void fillFaces(Mesh3D mesh);

  void render(Model model);

  void draw(VBO vbo);

  void setShader(String vertexShaderName, String fragmentShaderName);

  void resetShader();

  void setUniform(String name, float value);

  void setUniform(String name, Vector3f value);

  void setUniform(String name, Color color);

  void enableDepthTest();

  void disableDepthTest();

  void setMaterial(Material material);

  void drawLine(float x1, float y1, float z1, float x2, float y2, float z2);

  void drawLine(Vector3f from, Vector3f to);

  void drawLines(Vector3f[] vertices, math.Color[] colors);

  void camera();

  void ortho();

  void lightsOff();

  void enableFaceCulling();

  void disableFaceCulling();

  void disableDepthMask();

  void enableDepthMask();

  void applyMatrix(Matrix4f matrix);

  void applyCamera(Camera camera);

  void applyCameraRelative(Camera camera);

  void resetMatrix();
}
