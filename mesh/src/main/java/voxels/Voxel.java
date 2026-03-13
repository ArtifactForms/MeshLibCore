package voxels;

public record Voxel(int x, int y, int z) {

    public Voxel add(int dx, int dy, int dz) {
        return new Voxel(x + dx, y + dy, z + dz);
    }

    public Voxel add(Voxel other) {
        return new Voxel(x + other.x, y + other.y, z + other.z);
    }

    public Voxel subtract(Voxel other) {
        return new Voxel(x - other.x, y - other.y, z - other.z);
    }

    public Voxel multiply(int scalar) {
        return new Voxel(x * scalar, y * scalar, z * scalar);
    }

    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }
    
    public int getZ() {
    	return z;
    }
    
    public static Voxel of(float fx, float fy, float fz) {
        return new Voxel(Math.round(fx), Math.round(fy), Math.round(fz));
    }
}