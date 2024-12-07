package sandbox;

import math.Vector3f;
import mesh.Mesh3D;

/**
 * A modifier that applies a shear transformation to a 3D mesh. 
 * The shear effect distorts the mesh along a specified axis, creating 
 * a slanted or skewed appearance. 
 * 
 * The transformation is applied to each vertex of the mesh based on the 
 * specified shear axis and factor.
 */
public class ShearModifier implements IMeshModifier {

    /**
     * Represents the axis and plane along which the shear is applied.
     * For example:
     * - XY: Shear along the X-axis based on the Y-coordinate.
     * - XZ: Shear along the X-axis based on the Z-coordinate.
     */
    public enum ShearAxis {
        XY, XZ, YX, YZ, ZX, ZY
    }

    private ShearAxis axis;
    private float shearFactor;

    /**
     * Constructs a ShearModifier with the specified shear axis and factor.
     * 
     * @param axis        the axis along which the shear transformation is applied
     * @param shearFactor the factor by which the mesh is sheared
     */
    public ShearModifier(ShearAxis axis, float shearFactor) {
        this.axis = axis;
        this.shearFactor = shearFactor;
    }

    /**
     * Applies the shear transformation to the given mesh.
     * 
     * The shear is applied to all vertices of the mesh according to the 
     * specified axis and factor.
     * 
     * @param mesh the mesh to be modified
     * @return the modified mesh with the shear transformation applied
     */
    @Override
    public Mesh3D modify(Mesh3D mesh) {
        for (Vector3f vertex : mesh.getVertices()) {
            applyShear(vertex);
        }
        return mesh;
    }

    /**
     * Applies the shear transformation to a single vertex based on the 
     * specified shear axis and factor.
     * 
     * @param vertex the vertex to modify
     */
    private void applyShear(Vector3f vertex) {
        switch (axis) {
            case XY:
                vertex.x += shearFactor * vertex.y;
                break;
            case XZ:
                vertex.x += shearFactor * vertex.z;
                break;
            case YX:
                vertex.y += shearFactor * vertex.x;
                break;
            case YZ:
                vertex.y += shearFactor * vertex.z;
                break;
            case ZX:
                vertex.z += shearFactor * vertex.x;
                break;
            case ZY:
                vertex.z += shearFactor * vertex.y;
                break;
        }
    }

    /**
     * Gets the current shear axis.
     * 
     * @return the shear axis
     */
    public ShearAxis getAxis() {
        return axis;
    }

    /**
     * Sets the shear axis.
     * 
     * @param axis the shear axis to set
     */
    public void setAxis(ShearAxis axis) {
        this.axis = axis;
    }

    /**
     * Gets the current shear factor.
     * 
     * @return the shear factor
     */
    public float getShearFactor() {
        return shearFactor;
    }

    /**
     * Sets the shear factor.
     * 
     * @param shearFactor the shear factor to set
     */
    public void setShearFactor(float shearFactor) {
        this.shearFactor = shearFactor;
    }
}

