package mesh.pipeline;

import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * A pipeline modifier that applies another {@link IMeshModifier} repeatedly to the same {@link
 * Mesh3D} instance.
 *
 * <p>The {@code RepeatModifier} is useful for creating iterative or cumulative effects where a
 * modifier needs to be applied multiple times in sequence. Typical use cases include smoothing,
 * subdivision, noise layering, or procedural refinement steps.
 *
 * <p>This modifier operates <strong>destructively</strong> on the provided mesh. Each repetition
 * modifies the mesh produced by the previous iteration.
 *
 * <h3>Example</h3>
 *
 * <pre>{@code
 * IMeshModifier smoothTenTimes =
 *     new RepeatModifier(10, new SmoothModifier());
 *
 * smoothTenTimes.modify(mesh);
 * }</pre>
 *
 * <p>Conceptually equivalent to:
 *
 * <pre>{@code
 * for (int i = 0; i < repetitions; i++) {
 *   modifier.modify(mesh);
 * }
 * }</pre>
 *
 * <p>The repeated modifier instance is reused for all iterations. If the wrapped modifier maintains
 * internal state, that state will persist across repetitions.
 */
public class RepeatModifier implements IMeshModifier {

  /** Number of times the wrapped modifier will be applied. */
  private final int repetitions;

  /** The modifier to apply repeatedly. */
  private final IMeshModifier modifier;

  /**
   * Creates a new {@code RepeatModifier}.
   *
   * @param repetitions number of times to apply the modifier; must be {@code >= 1}
   * @param modifier the modifier to repeat; must not be {@code null}
   * @throws IllegalArgumentException if {@code repetitions < 1} or {@code modifier} is {@code null}
   */
  public RepeatModifier(int repetitions, IMeshModifier modifier) {
    if (repetitions < 1) {
      throw new IllegalArgumentException("Repetitions must be >= 1.");
    }
    if (modifier == null) {
      throw new IllegalArgumentException("Modifier cannot be null.");
    }
    this.repetitions = repetitions;
    this.modifier = modifier;
  }

  /**
   * Applies the wrapped modifier repeatedly to the given mesh.
   *
   * @param mesh the mesh to modify
   * @return the same mesh instance after all repetitions have been applied
   * @throws IllegalArgumentException if {@code mesh} is {@code null}
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }

    for (int i = 0; i < repetitions; i++) {
      modifier.modify(mesh);
    }

    return mesh;
  }

  /**
   * Returns the number of repetitions.
   *
   * @return the repetition count
   */
  public int getRepetitions() {
    return repetitions;
  }

  /**
   * Returns the wrapped modifier that is applied repeatedly.
   *
   * @return the wrapped modifier
   */
  public IMeshModifier getModifier() {
    return modifier;
  }
}
