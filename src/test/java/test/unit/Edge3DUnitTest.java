package test.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import mesh.Edge3D;

/**
 * Unit tests for {@link Edge3D}.
 *
 * <p>This test suite verifies the fundamental invariants and contracts of the {@code Edge3D} value
 * object:
 *
 * <ul>
 *   <li>Correct construction and index access
 *   <li>Immutability guarantees of {@link Edge3D#createPair()}
 *   <li>Symmetry and reversibility of edge pairing
 *   <li>{@code equals}/{@code hashCode} consistency
 *   <li>Defensive validation against invalid indices
 * </ul>
 *
 * <p>The tests intentionally focus on logical correctness and API behavior, independent of any mesh
 * or topology context.
 */
public class Edge3DUnitTest {

  /**
   * Verifies that {@link Edge3D} is properly encapsulated.
   *
   * <p>This test ensures that all declared fields of {@code Edge3D} are {@code private}. This
   * enforces the value-object contract and prevents external code from mutating internal state
   * directly.
   *
   * <p>Encapsulation is especially important for {@code Edge3D} because correctness relies on
   * invariants such as:
   *
   * <ul>
   *   <li>non-negative indices
   *   <li>fromIndex != toIndex
   * </ul>
   *
   * <p>Any non-private field would allow these invariants to be bypassed.
   */
  @Test
  public void testHasOnlyPrivateFields() {
    for (var field : Edge3D.class.getDeclaredFields()) {
      assertTrue(
          java.lang.reflect.Modifier.isPrivate(field.getModifiers()),
          "Field '" + field.getName() + "' must be private");
    }
  }

  @Test
  public void createPair_swapsFromAndToIndices() {
    int fromIndex = 2;
    int toIndex = 3;

    Edge3D edge = new Edge3D(fromIndex, toIndex);
    Edge3D pair = edge.createPair();

    assertEquals(toIndex, pair.getFromIndex());
    assertEquals(fromIndex, pair.getToIndex());
  }

  @Test
  public void constructor_setsFromAndToIndices() {
    int fromIndex = 10;
    int toIndex = 2;

    Edge3D edge = new Edge3D(fromIndex, toIndex);

    assertEquals(fromIndex, edge.getFromIndex());
    assertEquals(toIndex, edge.getToIndex());
  }

  @Test
  public void setFromIndex_updatesFromIndex() {
    int expected = 123;
    Edge3D edge = new Edge3D(1, 2);

    edge.setFromIndex(expected);

    assertEquals(expected, edge.getFromIndex());
  }

  @Test
  public void setToIndex_updatesToIndex() {
    int expected = 26;
    Edge3D edge = new Edge3D(0, 1);

    edge.setToIndex(expected);

    assertEquals(expected, edge.getToIndex());
  }

  @Test
  public void createPair_appliedTwice_returnsOriginalEdge() {
    int fromIndex = 20;
    int toIndex = 10;

    Edge3D edge = new Edge3D(fromIndex, toIndex);
    Edge3D pairPair = edge.createPair().createPair();

    assertEquals(fromIndex, pairPair.getFromIndex());
    assertEquals(toIndex, pairPair.getToIndex());
  }

  @Test
  public void createPair_doesNotModifyOriginalEdge() {
    int fromIndex = 67;
    int toIndex = 1023;

    Edge3D edge = new Edge3D(fromIndex, toIndex);
    edge.createPair();

    assertEquals(fromIndex, edge.getFromIndex());
    assertEquals(toIndex, edge.getToIndex());
  }

  @Test
  public void createPair_returnsNewInstance() {
    Edge3D edge = new Edge3D(1, 2);

    assertTrue(edge != edge.createPair());
  }

  @Test
  public void edgesWithSameIndices_areEqual() {
    int fromIndex = 120;
    int toIndex = 12;

    Edge3D edge0 = new Edge3D(fromIndex, toIndex);
    Edge3D edge1 = new Edge3D(fromIndex, toIndex);

    assertEquals(edge0, edge1);
  }

  @Test
  public void edge_isEqualToItself() {
    Edge3D edge = new Edge3D(1, 2);

    assertEquals(edge, edge);
  }

  @Test
  public void equalEdges_haveSameHashCode() {
    int fromIndex = 10;
    int toIndex = 12;

    HashSet<Edge3D> edges = new HashSet<>();

    for (int i = 0; i < 20; i++) {
      edges.add(new Edge3D(fromIndex, toIndex));
    }

    assertEquals(1, edges.size());
  }

  @Test
  public void edge_isNotEqualToNull() {
    Edge3D edge = new Edge3D(1, 2);

    assertNotEquals(edge, null);
  }

  @Test
  public void setFromIndex_rejectsNegativeIndex() {
    Edge3D edge = new Edge3D(1, 2);

    assertThrows(IllegalArgumentException.class, () -> edge.setFromIndex(-10));
  }

  @Test
  public void setToIndex_rejectsNegativeIndex() {
    Edge3D edge = new Edge3D(1, 2);

    assertThrows(IllegalArgumentException.class, () -> edge.setToIndex(-10));
  }

  @Test
  public void setFromIndex_rejectsIndexEqualToToIndex() {
    Edge3D edge = new Edge3D(2, 3);

    assertThrows(IllegalArgumentException.class, () -> edge.setFromIndex(3));
  }

  @Test
  public void setToIndex_rejectsIndexEqualToFromIndex() {
    Edge3D edge = new Edge3D(2, 3);

    assertThrows(IllegalArgumentException.class, () -> edge.setToIndex(2));
  }

  @Test
  public void constructor_rejectsEqualFromAndToIndices() {
    assertThrows(IllegalArgumentException.class, () -> new Edge3D(10, 10));
  }

  @Test
  public void constructor_rejectsNegativeFromIndex() {
    assertThrows(IllegalArgumentException.class, () -> new Edge3D(-10, 12));
  }

  @Test
  public void constructor_rejectsNegativeToIndex() {
    assertThrows(IllegalArgumentException.class, () -> new Edge3D(32, -12));
  }
}
