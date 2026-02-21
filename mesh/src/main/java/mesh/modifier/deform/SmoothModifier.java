package mesh.modifier.deform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class SmoothModifier implements IMeshModifier {

  private int iterations;

  private float factor;

  private Mesh3D mesh;

  private List<Vector3f> smoothedVertices;

  private HashMap<Integer, List<Integer>> map;

  public SmoothModifier() {
    iterations = 1;
    factor = 0.5f;
    smoothedVertices = new ArrayList<>();
    map = new HashMap<Integer, List<Integer>>();
  }

  private Vector3f getSmoothedVertex(Vector3f v, List<Integer> neighbors) {
    Vector3f smoothed = new Vector3f(v);
    float totalWeight = 0.0f;

    for (Integer index : neighbors) {
      Vector3f neighbor = mesh.getVertexAt(index);
      float distance = v.distance(neighbor);
      //			float weight = 1.0f / (distance + 1e-6f); // Avoid division by zero
      float weight = 1;
      smoothed.addLocal(neighbor.mult(weight));
      totalWeight += weight;
    }

    smoothed.divideLocal(totalWeight);
    //		return smoothed.lerpLocal(v, 1.0f - factor);
    return smoothed.mult(factor).add(v.mult(1f - factor));
  }

  private void processOneIteration() {
    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v = mesh.getVertexAt(i);
      smoothedVertices.add(getSmoothedVertex(v, map.get(i)));
    }

    for (int i = 0; i < mesh.getVertexCount(); i++) {
      mesh.getVertexAt(i).set(smoothedVertices.get(i));
    }

    smoothedVertices.clear();
  }

  private void smooth() {
    for (int i = 0; i < iterations; i++) processOneIteration();
  }

  // Pre-processes the mesh to identify neighboring vertices.
  private void mapNeighboringVertices() {
    for (Face3D face : mesh.getFaces()) {
      int n = face.indices.length;
      for (int i = 0; i < face.indices.length; i++) {
        int edgeFrom = face.indices[i];
        int edgeTo = face.indices[(i + 1) % n];
        List<Integer> list = map.get(edgeFrom);
        if (list == null) {
          list = new ArrayList<Integer>();
          map.put(edgeFrom, list);
        }
        list.add(edgeTo);
      }
    }
  }

  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    if (iterations == 0) {
      return mesh;
    }
    setMesh(mesh);
    mapNeighboringVertices();
    smooth();
    clearMap();
    return mesh;
  }

  private void clearMap() {
    map.clear();
  }

  /**
   * Sets the mesh to be modified.
   *
   * @param mesh the mesh to modify.
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  /**
   * Retrieves the number of smoothing iterations to be applied to the mesh
   *
   * @return the number of smoothing iterations.
   * @see #setIterations(int)
   */
  public int getIterations() {
    return iterations;
  }

  /**
   * Sets the number of smoothing iterations to be applied to the mesh, equivalent to executing
   * modify multiple times. The number of iterations determines how many times the smoothing process
   * will be executed. A higher number of iterations results in a smoother mesh.
   *
   * @param iterations the number of smoothing iterations, must be greater or equal to 0.
   * @throws IllegalArgumentException if the specified number of iterations is less than than 0.
   */
  public void setIterations(int iterations) {
    if (iterations < 0) {
      throw new IllegalArgumentException("Iterations must be greater or equal to zero.");
    }
    this.iterations = iterations;
  }

  /**
   * Retrieves the smoothing factor.
   *
   * @return the smoothing factor.
   * @see #setFactor(float)
   */
  public float getFactor() {
    return factor;
  }

  /**
   * Sets the smoothing factor, which determines the influence of neighboring vertices during the
   * smoothing process. A value closer to 0 minimizes the smoothing effect, while a value closer to
   * 1 maximizes it.
   *
   * @param factor the smoothing factor, must be between 0 and 1 (inclusive).
   * @throws IllegalArgumentException if the factor is less than 0 or greater than 1.
   */
  public void setFactor(float factor) {
    if (factor < 0 || factor > 1) {
      throw new IllegalArgumentException("Factor must be between 0 and 1.");
    }
    this.factor = factor;
  }
}
