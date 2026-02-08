package mesh.creator.assets.rock;

import java.util.List;

import math.Mathf;
import math.PerlinNoise3;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.modifier.transform.SnapToGroundModifier;
import mesh.util.VertexNormals;

/**
 * Procedural mesh creator for generating boulder-shaped rock assets.
 *
 * <p>{@code BoulderCreator} produces a single, self-contained {@link Mesh3D} representing a
 * natural-looking boulder suitable for environment placement, terrain decoration, or offline asset
 * generation.
 *
 * <p>The generated mesh is:
 *
 * <ul>
 *   <li>Convex and compact in overall shape
 *   <li>Deterministic with respect to its configuration and seed
 *   <li>Optionally flattened at the base for stable ground contact
 *   <li>Aligned so its pivot is located at the bottom center
 * </ul>
 *
 * <p>This class is designed as a <b>one-shot procedural recipe</b>. Each instance should be
 * configured and used for a single {@link #create()} call. Instances are not thread-safe.
 *
 * <p>Typical use cases include:
 *
 * <ul>
 *   <li>Scattered environment props
 *   <li>Terrain decoration
 *   <li>Procedural world generation
 *   <li>Offline mesh baking pipelines
 * </ul>
 *
 * @see IMeshCreator
 * @see Mesh3D
 */
public class BoulderCreator implements IMeshCreator {

  /**
   * Relative strength applied to secondary surface detail in relation to the primary shape
   * deformation.
   */
  private static final float SECONDARY_STRENGTH_RATIO = 0.2f;

  /**
   * Seed offset used to decorrelate multiple procedural stages while remaining fully deterministic.
   */
  private static final long SEED_OFFSET = 0x9E3779B97F4A7C15L;

  /** Seed controlling all deterministic variation produced by this creator. */
  private long seed;

  /** Subdivision level used for the base geometric representation. */
  private int subdivisions;

  /** Base radius defining the overall scale of the boulder. */
  private float baseRadius;

  /** Frequency controlling large-scale shape variation. */
  private float primaryFrequency;

  /** Strength of the primary shape deformation. */
  private float primaryNoiseStrength;

  /** Strength of secondary surface detail deformation. */
  private float secondaryNoiseStrength;

  /** Factor controlling the density of small-scale surface variation. */
  private float roughness;

  /** Vertical extent used to blend the flattened base into the natural shape. */
  private float bottomFlatness;

  /** Whether the base of the boulder should be flattened. */
  private boolean flattenBottom;

  /** Internal mesh instance used during generation. */
  private Mesh3D mesh;

  /**
   * Cached vertex normals captured before surface deformation.
   *
   * <p>Used to ensure stable, directionally consistent displacement during mesh modification.
   */
  private List<Vector3f> vertexNormals;

  /**
   * Creates a {@code BoulderCreator} using a default deterministic seed.
   *
   * <p>Multiple instances created with this constructor will generate identical meshes unless their
   * configuration parameters differ.
   */
  public BoulderCreator() {
    this(0);
  }

  /**
   * Creates a {@code BoulderCreator} using the specified seed.
   *
   * <p>The seed controls all procedural variation produced by this creator. Using the same seed and
   * configuration will always result in the same mesh.
   *
   * @param seed the seed used for deterministic mesh generation
   */
  public BoulderCreator(long seed) {
    this.seed = seed;
    this.subdivisions = 3;
    this.baseRadius = 1.5f;
    this.primaryFrequency = 0.8f;
    this.primaryNoiseStrength = 1.0f;
    this.secondaryNoiseStrength = primaryNoiseStrength * SECONDARY_STRENGTH_RATIO;
    this.roughness = 3;
    this.bottomFlatness = 1;
    this.flattenBottom = true;
    validate();
  }

  /**
   * Generates and returns a new boulder mesh.
   *
   * <p>The returned {@link Mesh3D} is fully constructed, grounded, and ready for use in a scene or
   * further processing.
   *
   * <p>This method may be called only once per instance. Internal temporary state is cleared after
   * mesh creation.
   *
   * @return a newly generated boulder mesh
   * @throws IllegalArgumentException if the current configuration is invalid
   */
  @Override
  public Mesh3D create() {
    validate();

    // !Important modifier order shouldn't be changed
    createIcoSphere();
    cacheInitialVertexNormals();
    applyPrimaryNoise();
    applySecondaryNoise();
    flattenBottom();
    recalculateVertexNormals();
    pivotPointToBottomCenter();

    // CLEAR temporary data
    Mesh3D result = mesh;
    vertexNormals = null;
    mesh = null;

    return result;
  }

  /**
   * Recalculates and assigns vertex normals after all geometric modifications have been applied.
   */
  private void recalculateVertexNormals() {
    VertexNormals normals = new VertexNormals(mesh);
    mesh.setVertexNormals(normals.getVertexNormals());
  }

  /**
   * Captures the initial vertex normals of the base geometry.
   *
   * <p>These normals are used as stable deformation directions during subsequent mesh modification
   * stages.
   */
  private void cacheInitialVertexNormals() {
    vertexNormals = new VertexNormals(mesh).getVertexNormals();
  }

  /**
   * Validates the current configuration.
   *
   * @throws IllegalArgumentException if any configuration parameter is outside its valid range
   */
  private void validate() {
    if (subdivisions < 0) throw new IllegalArgumentException("Subdivisions must be >= 0");

    if (baseRadius <= 0) throw new IllegalArgumentException("Base radius must be > 0");

    if (primaryFrequency <= 0) throw new IllegalArgumentException("Primary frequency must be > 0");

    if (bottomFlatness <= 0) throw new IllegalArgumentException("Bottom flatness must be > 0");
  }

  /**
   * Creates the base geometric representation used as the foundation for all further modification.
   */
  private void createIcoSphere() {
    IcoSphereCreator creator = new IcoSphereCreator(baseRadius, subdivisions);
    mesh = creator.create();
  }

  /** Applies large-scale shape variation to the base geometry. */
  private void applyPrimaryNoise() {
    long base = seed;
    PerlinNoise3 noise3D = new PerlinNoise3(base);

    for (int i = 0; i < mesh.vertices.size(); i++) {
      Vector3f vertexNormal = vertexNormals.get(i);
      Vector3f vertex = mesh.getVertexAt(i);

      // low frequency
      Vector3f samplePos = vertexNormal.mult(baseRadius * primaryFrequency);
      float noiseValue = (float) noise3D.noise(samplePos.x, samplePos.y, samplePos.z);

      vertex.addLocal(vertexNormal.mult(noiseValue * primaryNoiseStrength));
    }
  }

  /** Applies secondary high-frequency surface detail to the geometry. */
  private void applySecondaryNoise() {
    long base = seed ^ SEED_OFFSET;
    PerlinNoise3 noise3D = new PerlinNoise3(base);

    for (int i = 0; i < mesh.vertices.size(); i++) {
      Vector3f n = vertexNormals.get(i);
      Vector3f v = mesh.getVertexAt(i);

      // high frequency
      Vector3f samplePos = n.mult(baseRadius * roughness);
      float n2 = (float) noise3D.noise(samplePos.x, samplePos.y, samplePos.z);

      v.addLocal(n.mult(n2 * secondaryNoiseStrength));
    }
  }

  /** Flattens the bottom portion of the mesh to improve grounding and placement stability. */
  private void flattenBottom() {
    if (!flattenBottom) return;

    float maxY = -Float.MAX_VALUE;

    for (Vector3f v : mesh.vertices) {
      maxY = Math.max(maxY, v.y);
    }

    float threshold = maxY - bottomFlatness;

    for (Vector3f v : mesh.vertices) {
      if (v.y > threshold) {
        float t = Mathf.clamp01((v.y - threshold) / bottomFlatness);
        v.y = Mathf.lerp(v.y, threshold, t);
      }
    }
  }

  /** Adjusts the mesh so its pivot point is located at the bottom center. */
  private void pivotPointToBottomCenter() {
    new SnapToGroundModifier().modify(mesh);
  }

  /**
   * Returns the subdivision level used for the base geometry.
   *
   * @return the subdivision count
   */
  public int getSubdivisions() {
    return subdivisions;
  }

  /**
   * Sets the subdivision level used for the base geometry.
   *
   * <p>Higher values increase geometric detail at the cost of additional vertices.
   *
   * @param subdivisions the subdivision count (must be non-negative)
   */
  public void setSubdivisions(int subdivisions) {
    this.subdivisions = subdivisions;
  }

  /**
   * Returns the base radius of the generated boulder.
   *
   * @return the base radius
   */
  public float getBaseRadius() {
    return baseRadius;
  }

  /**
   * Sets the base radius of the generated boulder.
   *
   * @param baseRadius the base radius (must be greater than zero)
   */
  public void setBaseRadius(float baseRadius) {
    this.baseRadius = baseRadius;
  }

  /**
   * Returns the surface roughness factor of the boulder.
   *
   * @return the roughness value
   */
  public float getRoughness() {
    return roughness;
  }

  /**
   * Sets the surface roughness factor of the boulder.
   *
   * <p>Higher values result in more pronounced small-scale surface detail.
   *
   * @param roughness the roughness value
   */
  public void setRoughness(float roughness) {
    this.roughness = roughness;
  }

  /**
   * Returns whether base flattening is enabled.
   *
   * @return {@code true} if the bottom of the boulder is flattened
   */
  public boolean isFlattenBottom() {
    return flattenBottom;
  }

  /**
   * Enables or disables base flattening.
   *
   * <p>When enabled, the generated boulder will have a flattened base to improve visual grounding
   * and placement stability.
   *
   * @param flattenBottom whether base flattening should be applied
   */
  public void setFlattenBottom(boolean flattenBottom) {
    this.flattenBottom = flattenBottom;
  }
}
