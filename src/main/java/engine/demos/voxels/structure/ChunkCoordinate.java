package engine.demos.voxels.structure;

import java.util.List;

/**
 * Represents a coordinate for a chunk in a voxel-based world. Each chunk is identified by its x and
 * z indices in a grid. This class provides utility methods for distance calculations and neighbor
 * retrieval.
 */
public class ChunkCoordinate {

  private final int x; // The x-coordinate of the chunk
  private final int z; // The z-coordinate of the chunk

  /**
   * Creates a new ChunkCoordinate with the specified x and z values.
   *
   * @param x the x-coordinate of the chunk
   * @param z the z-coordinate of the chunk
   */
  public ChunkCoordinate(int x, int z) {
    this.x = x;
    this.z = z;
  }

  /**
   * Calculates the Manhattan distance to another ChunkCoordinate. The Manhattan distance is the sum
   * of the absolute differences of the x and z coordinates.
   *
   * @param other the other ChunkCoordinate
   * @return the Manhattan distance between this coordinate and the other coordinate
   */
  public int manhattanDistance(ChunkCoordinate other) {
    return Math.abs(this.x - other.x) + Math.abs(this.z - other.z);
  }

  /**
   * Retrieves a list of neighboring ChunkCoordinates. Neighbors are directly adjacent chunks in the
   * x and z directions.
   *
   * @return a list of neighboring ChunkCoordinates
   */
  public List<ChunkCoordinate> getNeighbors() {
    return List.of(
        new ChunkCoordinate(x + 1, z),
        new ChunkCoordinate(x - 1, z),
        new ChunkCoordinate(x, z + 1),
        new ChunkCoordinate(x, z - 1));
  }

  /**
   * Converts this ChunkCoordinate into a single long value for efficient storage or transmission.
   * The x and z coordinates are packed into the high and low 32 bits of the long, respectively.
   *
   * @return a long value representing this ChunkCoordinate
   */
  public long toLong() {
    return ((long) x << 32) | (z & 0xFFFFFFFFL);
  }

  /**
   * Reconstructs a ChunkCoordinate from a packed long value. The x coordinate is extracted from the
   * high 32 bits, and the z coordinate is extracted from the low 32 bits.
   *
   * @param packed the packed long value
   * @return a new ChunkCoordinate object reconstructed from the packed long
   */
  public static ChunkCoordinate fromLong(long packed) {
    int x = (int) (packed >> 32);
    int z = (int) packed;
    return new ChunkCoordinate(x, z);
  }

  /**
   * Gets the x-coordinate of this ChunkCoordinate.
   *
   * @return the x-coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the z-coordinate of this ChunkCoordinate.
   *
   * @return the z-coordinate
   */
  public int getZ() {
    return z;
  }

  /**
   * Checks if this ChunkCoordinate is equal to another object. Two ChunkCoordinates are equal if
   * their x and z values are the same.
   *
   * @param obj the object to compare
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    ChunkCoordinate that = (ChunkCoordinate) obj;
    return x == that.x && z == that.z;
  }

  /**
   * Computes the hash code for this ChunkCoordinate. The hash code is calculated using the x and z
   * values.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return 31 * x + z;
  }

  /**
   * Returns a string representation of this ChunkCoordinate. The string includes the x and z values
   * for debugging purposes.
   *
   * @return a string representation of this ChunkCoordinate
   */
  @Override
  public String toString() {
    return "ChunkCoordinate{" + "x=" + x + ", z=" + z + '}';
  }
}
