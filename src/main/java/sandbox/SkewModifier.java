package sandbox;

import mesh.Mesh3D;
import math.Vector3f;

public class SkewModifier implements IMeshModifier {

    public enum Axis {
        X, Y, Z
    }

    private float skewAmount;
  
    private Axis skewAxis;
  
    private Axis referenceAxis;
  
    private float origin;

    public SkewModifier() {
        this(0.5f, Axis.X, Axis.Y, 0.0f);
    }

    public SkewModifier(float skewAmount, Axis skewAxis, Axis referenceAxis, float origin) {
        this.skewAmount = skewAmount;
        this.skewAxis = skewAxis;
        this.referenceAxis = referenceAxis;
        this.origin = origin;
    }

    @Override
    public void modify(Mesh3D mesh) {
        for (Vector3f vertex : mesh.getVertices()) {
            float referenceValue = getAxisValue(vertex, referenceAxis);
            float skewOffset = skewAmount * (referenceValue - origin);
            setAxisValue(vertex, skewAxis, getAxisValue(vertex, skewAxis) + skewOffset);
        }
    }

    private float getAxisValue(Vector3f vertex, Axis axis) {
        switch (axis) {
            case X: return vertex.x;
            case Y: return vertex.y;
            case Z: return vertex.z;
            default: throw new IllegalArgumentException("Invalid axis");
        }
    }

    private void setAxisValue(Vector3f vertex, Axis axis, float value) {
        switch (axis) {
            case X: vertex.x = value; break;
            case Y: vertex.y = value; break;
            case Z: vertex.z = value; break;
            default: throw new IllegalArgumentException("Invalid axis");
        }
    }

    public float getSkewAmount() {
        return skewAmount;
    }

    public void setSkewAmount(float skewAmount) {
        this.skewAmount = skewAmount;
    }

    public Axis getSkewAxis() {
        return skewAxis;
    }

    public void setSkewAxis(Axis skewAxis) {
        this.skewAxis = skewAxis;
    }

    public Axis getReferenceAxis() {
        return referenceAxis;
    }

    public void setReferenceAxis(Axis referenceAxis) {
        this.referenceAxis = referenceAxis;
    }

    public float getOrigin() {
        return origin;
    }

    public void setOrigin(float origin) {
        this.origin = origin;
    }
}
