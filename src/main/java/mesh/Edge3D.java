package mesh;

/**
 * Represents a directed edge between two vertices in a mesh.
 *
 * <p>An {@code Edge3D} is defined by two vertex indices:
 *
 * <ul>
 *   <li>{@code fromIndex} – the start vertex
 *   <li>{@code toIndex} – the end vertex
 * </ul>
 *
 * <p>This class models a <b>directed</b> edge. Two edges with swapped indices are considered
 * distinct unless explicitly paired via {@link #createPair()}.
 *
 * <h3>Invariants</h3>
 *
 * <ul>
 *   <li>Both indices must be non-negative
 *   <li>{@code fromIndex != toIndex}
 * </ul>
 *
 * <p>Violations of these invariants result in {@link IllegalArgumentException}s.
 */
public class Edge3D {

  /** Index of the start vertex (source). */
  private int fromIndex;

  /** Index of the end vertex (target). */
  private int toIndex;

  /**
   * Creates a directed edge from {@code fromIndex} to {@code toIndex}.
   *
   * @param fromIndex index of the start vertex (must be {@code >= 0})
   * @param toIndex index of the end vertex (must be {@code >= 0})
   * @throws IllegalArgumentException if indices are negative or equal
   */
  public Edge3D(int fromIndex, int toIndex) {
    validate(fromIndex, toIndex);
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  /** Validates the edge invariants. */
  private void validate(int fromIndex, int toIndex) {
    if (fromIndex < 0) {
      throw new IllegalArgumentException("fromIndex must be non-negative (was " + fromIndex + ")");
    }

    if (toIndex < 0) {
      throw new IllegalArgumentException("toIndex must be non-negative (was " + toIndex + ")");
    }

    if (fromIndex == toIndex) {
      throw new IllegalArgumentException(
          "fromIndex and toIndex must be different (both were " + fromIndex + ")");
    }
  }

  /**
   * Creates the oppositely directed edge.
   *
   * <p>The returned edge has swapped indices: {@code (from, to) -> (to, from)}.
   *
   * @return a new {@code Edge3D} with reversed direction
   */
  public Edge3D createPair() {
    return new Edge3D(toIndex, fromIndex);
  }

  /** @return the start vertex index */
  public int getFromIndex() {
    return fromIndex;
  }

  /**
   * Sets the start vertex index.
   *
   * @param fromIndex new start index (must be valid)
   * @throws IllegalArgumentException if invariants are violated
   */
  public void setFromIndex(int fromIndex) {
    validate(fromIndex, this.toIndex);
    this.fromIndex = fromIndex;
  }

  /** @return the end vertex index */
  public int getToIndex() {
    return toIndex;
  }

  /**
   * Sets the end vertex index.
   *
   * @param toIndex new end index (must be valid)
   * @throws IllegalArgumentException if invariants are violated
   */
  public void setToIndex(int toIndex) {
    validate(this.fromIndex, toIndex);
    this.toIndex = toIndex;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Edge3D other = (Edge3D) obj;
    return fromIndex == other.fromIndex && toIndex == other.toIndex;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fromIndex;
    result = prime * result + toIndex;
    return result;
  }

  @Override
  public String toString() {
    return "Edge3D[from=" + fromIndex + ", to=" + toIndex + "]";
  }
}
