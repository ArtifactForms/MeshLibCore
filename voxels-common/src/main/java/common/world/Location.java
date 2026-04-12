package common.world;

public class Location {

  private double x;

  private double y;

  private double z;

  private float pitch;

  private float yaw;

  public Location() {
    this(0, 0, 0, 0, 0);
  }

  public Location(double x, double y, double z, float pitch, float yaw) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.pitch = pitch;
    this.yaw = yaw;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  public float getYaw() {
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public void setPosition(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public int getChunkX() {
    return WorldMath.worldToChunk(x, ChunkData.WIDTH);
  }

  public int getChunkZ() {
    return WorldMath.worldToChunk(z, ChunkData.DEPTH);
  }

  public int getBlockX() {
    return fastFloor(x);
  }

  public int getBlockY() {
    return fastFloor(y);
  }

  public int getBlockZ() {
    return fastFloor(z);
  }

  private int fastFloor(double x) {
//    int i = (int) x;
//    return x < i ? i - 1 : i;
	  return (int) Math.floor(x);
  }
}
