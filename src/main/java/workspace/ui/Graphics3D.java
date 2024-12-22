package workspace.ui;

import java.util.List;

import engine.render.Material;
import engine.scene.light.Light;
import math.Matrix4f;
import mesh.Mesh3D;

public interface Graphics3D extends Graphics2D {
	
	void translate(float x, float y, float z);

	void scale(float sx, float sy, float sz);
	
	void rotateX(float angle);

	void rotateY(float angle);

	void rotateZ(float angle);

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
	
	/**
	 * Sets the current view matrix for rendering. The view matrix transforms
	 * coordinates from world space to camera (view) space.
	 *
	 * @param viewMatrix The 4x4 view matrix to be applied for rendering.
	 */
	void setViewMatrix(Matrix4f viewMatrix);

	/**
	 * Sets the current projection matrix for rendering. The projection matrix
	 * defines how 3D coordinates are projected into the 2D viewport for rendering
	 * purposes.
	 *
	 * @param projectionMatrix The 4x4 projection matrix to be applied for
	 *                         rendering.
	 */
	void setProjectionMatrix(Matrix4f projectionMatrix);

}
