package workspace.ui;

import engine.render.Material;
import engine.resources.Model;
import engine.resources.Texture;
import engine.scene.camera.Camera;
import engine.scene.light.Light;
import engine.vbo.VBO;
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

  void enableDepthTest();

  void disableDepthTest();

  void setMaterial(Material material);

  void drawLine(float x1, float y1, float z1, float x2, float y2, float z2);

  void drawLine(Vector3f from, Vector3f to);

  void drawLines(Vector3f[] vertices, math.Color[] colors);

  void camera();

  void lightsOff();

  void bindTexture(Texture texture, int unit);

  void unbindTexture(int unit);

  void enableFaceCulling();

  void disableFaceCulling();

  void applyMatrix(Matrix4f matrix);

  void applyCamera(Camera camera);

  void resetMatrix();
}
