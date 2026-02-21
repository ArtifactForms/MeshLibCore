package mesh.modifier.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.FaceModifier;
import mesh.modifier.IMeshModifier;

/**
 * A mesh modifier that creates random holes in the given 3D mesh by extruding and removing
 * specified faces. The size of the holes is determined randomly as a percentage of the original
 * face size within a defined range.
 *
 * <p>This modifier supports modifying all faces, a single face, or a subset of faces in a 3D mesh.
 */
public class RandomHolesModifier implements IMeshModifier, FaceModifier {

  /** The default minimum amount for the hole size as a percentage of the face area. */
  private static final float DEFAULT_MIN_AMOUNT = 0.1f;

  /** The default maximum amount for the hole size as a percentage of the face area. */
  private static final float DEFAULT_MAX_AMOUNT = 0.9f;

  /** The minimum amount for the hole size as a percentage of the face area. */
  private float minAmount;

  /** The maximum amount for the hole size as a percentage of the face area. */
  private float maxAmount;

  /** The seed for the random number generator used to determine hole sizes. */
  private long seed;

  /** A random number generator used to calculate random hole sizes. */
  private Random random;

  /** An {@link ExtrudeModifier} used to create the holes by extruding and removing faces. */
  private ExtrudeModifier modifier;

  /**
   * Creates a new RandomHolesModifier with the default minimum and maximum hole percentages.
   *
   * <p>Default values:
   *
   * <ul>
   *   <li>Minimum hole percentage: 10% (0.1)
   *   <li>Maximum hole percentage: 90% (0.9)
   * </ul>
   */
  public RandomHolesModifier() {
    this(DEFAULT_MIN_AMOUNT, DEFAULT_MAX_AMOUNT);
    validateAmountRange(DEFAULT_MIN_AMOUNT, DEFAULT_MAX_AMOUNT);
  }

  /**
   * Creates a new RandomHolesModifier with specified minimum and maximum hole percentages.
   *
   * @param minAmount the minimum size of a hole as a percentage of the original face size. Must be
   *     in the range [0, 1].
   * @param maxAmount the maximum size of a hole as a percentage of the original face size. Must be
   *     in the range [0, 1] and greater than or equal to {@code minAmount}.
   * @throws IllegalArgumentException if {@code minAmount} or {@code maxAmount} are out of range or
   *     if {@code minAmount > maxAmount}.
   */
  public RandomHolesModifier(float minAmount, float maxAmount) {
    validateAmountRange(minAmount, maxAmount);
    this.minAmount = minAmount;
    this.maxAmount = maxAmount;
    this.random = new Random();
    this.modifier = new ExtrudeModifier();
  }

  /**
   * Modifies the entire mesh by creating random holes in all its faces.
   *
   * @param mesh the mesh to modify.
   * @return the modified mesh.
   * @throws IllegalArgumentException if the mesh is {@code null}.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    validateMesh(mesh);
    return modify(mesh, new ArrayList<Face3D>(mesh.getFaces()));
  }

  /**
   * Modifies a single face in the mesh by creating a random hole.
   *
   * @param mesh the mesh containing the face.
   * @param face the face to modify.
   * @return the modified mesh.
   * @throws IllegalArgumentException if the mesh or face is {@code null}.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh, Face3D face) {
    validateMesh(mesh);
    validateFace(face);
    makeHole(mesh, face);
    return mesh;
  }

  /**
   * Modifies a collection of faces in the mesh by creating random holes.
   *
   * @param mesh the mesh containing the faces.
   * @param faces the faces to modify.
   * @return the modified mesh.
   * @throws IllegalArgumentException if the mesh or faces are {@code null}.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
    validateMesh(mesh);
    validateFaces(faces);
    Collection<Face3D> facesToModify = faces;
    if (faces == mesh.getFaces()) {
      facesToModify = new ArrayList<Face3D>(mesh.getFaceCount());
    }
    for (Face3D face : facesToModify) {
      makeHole(mesh, face);
    }
    return mesh;
  }

  /**
   * Creates a hole by extruding (inset) and removing the specified face.
   *
   * @param mesh the mesh containing the face.
   * @param face the face to modify.
   */
  private void makeHole(Mesh3D mesh, Face3D face) {
    float amount = createRandomAmount();
    modifier.setScale(amount);
    modifier.setRemoveFaces(true);
    modifier.modify(mesh, face);
  }

  /**
   * Generates a random hole percentage within the range defined by {@code minAmount} and {@code
   * maxAmount}.
   *
   * @return a random hole percentage.
   */
  private float createRandomAmount() {
    return minAmount + random.nextFloat() * (maxAmount - minAmount);
  }

  /**
   * Validates that the given mesh is not null.
   *
   * @param mesh the {@link Mesh3D} instance to validate.
   * @throws IllegalArgumentException if {@code mesh} is null.
   */
  private void validateMesh(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
  }

  /**
   * Validates that the given collection of faces is not null.
   *
   * @param faces a collection of {@link Face3D} instances to validate.
   * @throws IllegalArgumentException if {@code faces} is null.
   */
  private void validateFaces(Collection<Face3D> faces) {
    if (faces == null) {
      throw new IllegalArgumentException("Faces cannot be null.");
    }
  }

  /**
   * Validates that the given face is not null.
   *
   * @param face the {@link Face3D} instance to validate.
   * @throws IllegalArgumentException if {@code face} is null.
   */
  private void validateFace(Face3D face) {
    if (face == null) {
      throw new IllegalArgumentException("Face cannot be null.");
    }
  }

  /**
   * Gets the minimum hole percentage.
   *
   * @return the minimum hole percentage.
   */
  public float getMinAmount() {
    return minAmount;
  }

  /**
   * Gets the maximum hole percentage.
   *
   * @return the maximum hole percentage.
   */
  public float getMaxAmount() {
    return maxAmount;
  }

  /**
   * Validates that the given range of amounts is within acceptable bounds.
   *
   * <p>The method checks that the {@code minAmount} and {@code maxAmount} values satisfy the
   * following conditions:
   *
   * <ul>
   *   <li>{@code minAmount} is greater than or equal to 0.
   *   <li>{@code maxAmount} is less than or equal to 1.
   *   <li>{@code minAmount} is less than or equal to {@code maxAmount}.
   * </ul>
   *
   * @param minAmount the minimum hole percentage to validate.
   * @param maxAmount the maximum hole percentage to validate.
   * @throws IllegalArgumentException if the range does not satisfy the conditions: {@code 0 <=
   *     minAmount <= maxAmount <= 1}.
   */
  private void validateAmountRange(float minAmount, float maxAmount) {
    if (minAmount < 0 || maxAmount > 1 || minAmount > maxAmount) {
      throw new IllegalArgumentException("Amounts must satisfy: 0 <= minAmount <= maxAmount <= 1.");
    }
  }

  /**
   * Gets the current random seed.
   *
   * @return the random seed.
   */
  public long getSeed() {
    return seed;
  }

  /**
   * Sets a new random seed.
   *
   * @param seed the new seed value.
   */
  public void setSeed(long seed) {
    this.seed = seed;
    this.random = new Random(seed);
  }
}
