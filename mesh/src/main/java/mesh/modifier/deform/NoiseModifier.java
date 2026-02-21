package mesh.modifier.deform;

import java.util.List;
import java.util.Random;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.VertexNormals;

public class NoiseModifier implements IMeshModifier {

  private static final float DEFAULT_MINIMUM = 0;

  private static final float DEFAULT_MAXIMUM = 1;

  private float minimum;

  private float maximum;

  private long seed;

  private Random random;

  private Mesh3D mesh;

  private List<Vector3f> vertexNormals;

  public NoiseModifier() {
    this(DEFAULT_MINIMUM, DEFAULT_MAXIMUM);
  }

  public NoiseModifier(float minimum, float maximum) {
    this.minimum = minimum;
    this.maximum = maximum;
    this.random = new Random(seed);
  }

  @Override
  public Mesh3D modify(Mesh3D mesh) {
    validate(mesh);
    if (mesh.getVertexCount() == 0) {
      return mesh;
    }
    setMesh(mesh);
    calculateVertexNormals();
    applyNoise();
    return mesh;
  }

  private void applyNoise() {
    for (int index = 0; index < getVertexCount(); index++) {
      applyNoiseToVertex(index);
    }
  }

  private void applyNoiseToVertex(int index) {
    float length = createRandomValue();
    Vector3f vertex = getVertexAt(index);
    Vector3f normal = getVertexNormalAt(index);
    vertex.addLocal(normal.mult(length));
  }

  private Vector3f getVertexAt(int index) {
    return mesh.getVertexAt(index);
  }

  private Vector3f getVertexNormalAt(int index) {
    return vertexNormals.get(index);
  }

  private int getVertexCount() {
    return mesh.getVertexCount();
  }

  private void calculateVertexNormals() {
    vertexNormals = new VertexNormals(mesh).getVertexNormals();
  }

  private float createRandomValue() {
    return minimum + random.nextFloat() * (maximum - minimum);
  }

  private void validate(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
  }

  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  public float getMinimum() {
    return minimum;
  }

  public void setMinimum(float minimum) {
    this.minimum = minimum;
  }

  public float getMaximum() {
    return maximum;
  }

  public void setMaximum(float maximum) {
    this.maximum = maximum;
  }

  public long getSeed() {
    return seed;
  }

  public void setSeed(long seed) {
    this.seed = seed;
    random = new Random(seed);
  }
}
