package math;

import java.util.Arrays;

public class Matrix3f {

    public static final Matrix3f ZERO = new Matrix3f(0f, 0f, 0f, 0f, 0f, 0f, 0f,
            0f, 0f);

    public static final Matrix3f UNIT = new Matrix3f(1f, 0f, 0f, 0f, 1f, 0f, 0f,
            0f, 1f);

    public static final Matrix3f ONE = new Matrix3f(1f, 1f, 1f, 1f, 1f, 1f, 1f,
            1f, 1f);

    public static final int M00 = 0;

    public static final int M01 = 1;

    public static final int M02 = 2;

    public static final int M10 = 3;

    public static final int M11 = 4;

    public static final int M12 = 5;

    public static final int M20 = 6;

    public static final int M21 = 7;

    public static final int M22 = 8;

    public float[] values = new float[9];

    public Matrix3f() {
        super();
    }

    public Matrix3f(float[] values) {
        super();
        set(values);
    }

    public Matrix3f(float m00, float m01, float m02, float m10, float m11,
            float m12, float m20, float m21, float m22) {
        super();
        values[M00] = m00;
        values[M01] = m01;
        values[M02] = m02;
        values[M10] = m10;
        values[M11] = m11;
        values[M12] = m12;
        values[M20] = m20;
        values[M21] = m21;
        values[M22] = m22;
    }

    public Matrix3f(Matrix3f m) {
        super();
        set(m);
    }

    // TODO Test
    public Matrix3f mult(Matrix3f m) {
        Matrix3f result = new Matrix3f();
        float[] a = values;
        float[] b = m.values;
        float[] c = result.values;
        c[M00] = a[M00] * b[M00] + a[M01] * b[M10] + a[M02] * b[M20];
        c[M01] = a[M00] * b[M01] + a[M01] * b[M11] + a[M02] * b[M21];
        c[M02] = a[M00] * b[M02] + a[M01] * b[M12] + a[M02] * b[M22];
        c[M10] = a[M10] * b[M00] + a[M11] * b[M10] + a[M12] * b[M20];
        c[M11] = a[M10] * b[M01] + a[M11] * b[M11] + a[M12] * b[M21];
        c[M12] = a[M10] * b[M02] + a[M11] * b[M12] + a[M12] * b[M22];
        c[M20] = a[M20] * b[M00] + a[M21] * b[M10] + a[M22] * b[M20];
        c[M21] = a[M20] * b[M01] + a[M21] * b[M11] + a[M22] * b[M21];
        c[M22] = a[M20] * b[M02] + a[M21] * b[M12] + a[M22] * b[M22];
        return result;
    }

    public Matrix3f multLocal(Matrix3f m) {
        set(this.mult(m));
        return this;
    }

    public Matrix3f add(Matrix3f m) {
        Matrix3f result = new Matrix3f(this);
        result.addLocal(m);
        return result;
    }

    public Matrix3f addLocal(Matrix3f m) {
        values[M00] += m.values[M00];
        values[M01] += m.values[M01];
        values[M02] += m.values[M02];
        values[M10] += m.values[M10];
        values[M11] += m.values[M11];
        values[M12] += m.values[M12];
        values[M20] += m.values[M20];
        values[M21] += m.values[M21];
        values[M22] += m.values[M22];
        return this;
    }

    public Matrix3f add(float m00, float m01, float m02, float m10, float m11,
            float m12, float m20, float m21, float m22) {
        Matrix3f result = new Matrix3f(this);
        result.addLocal(m00, m01, m02, m10, m11, m12, m20, m21, m22);
        return result;
    }

    public Matrix3f addLocal(float m00, float m01, float m02, float m10,
            float m11, float m12, float m20, float m21, float m22) {
        values[M00] += m00;
        values[M01] += m01;
        values[M02] += m02;
        values[M10] += m10;
        values[M11] += m11;
        values[M12] += m12;
        values[M20] += m20;
        values[M21] += m21;
        values[M22] += m22;
        return this;
    }

    public Matrix3f mult(float scalar) {
        Matrix3f result = new Matrix3f(this);
        result.multLocal(scalar);
        return result;
    }

    public Matrix3f multLocal(float scalar) {
        values[M00] *= scalar;
        values[M01] *= scalar;
        values[M02] *= scalar;
        values[M10] *= scalar;
        values[M11] *= scalar;
        values[M12] *= scalar;
        values[M20] *= scalar;
        values[M21] *= scalar;
        values[M22] *= scalar;
        return this;
    }

    public float det() {
        return values[M00] * values[M11] * values[M22]
                + values[M01] * values[M12] * values[M20]
                + values[M02] * values[M10] * values[M21]
                - values[M00] * values[M12] * values[M21]
                - values[M01] * values[M10] * values[M22]
                - values[M02] * values[M11] * values[M20];
    }

    public Matrix3f abs() {
        Matrix3f result = new Matrix3f(this);
        result.absLocal();
        return result;
    }

    public Matrix3f absLocal() {
        values[M00] = Mathf.abs(values[M00]);
        values[M01] = Mathf.abs(values[M01]);
        values[M02] = Mathf.abs(values[M02]);
        values[M10] = Mathf.abs(values[M10]);
        values[M11] = Mathf.abs(values[M11]);
        values[M12] = Mathf.abs(values[M12]);
        values[M20] = Mathf.abs(values[M20]);
        values[M21] = Mathf.abs(values[M21]);
        values[M22] = Mathf.abs(values[M22]);
        return this;
    }

    public Matrix3f zero() {
        values[M00] = 0;
        values[M01] = 0;
        values[M02] = 0;
        values[M10] = 0;
        values[M11] = 0;
        values[M12] = 0;
        values[M20] = 0;
        values[M21] = 0;
        values[M22] = 0;
        return this;
    }

    public Matrix3f set(float m00, float m01, float m02, float m10, float m11,
            float m12, float m20, float m21, float m22) {
        values[M00] = m00;
        values[M01] = m01;
        values[M02] = m02;
        values[M10] = m10;
        values[M11] = m11;
        values[M12] = m12;
        values[M20] = m20;
        values[M21] = m21;
        values[M22] = m22;
        return this;
    }

    public Matrix3f set(Matrix3f m) {
        values[M00] = m.values[M00];
        values[M01] = m.values[M01];
        values[M02] = m.values[M02];
        values[M10] = m.values[M10];
        values[M11] = m.values[M11];
        values[M12] = m.values[M12];
        values[M20] = m.values[M20];
        values[M21] = m.values[M21];
        values[M22] = m.values[M22];
        return this;
    }

    public Matrix3f set(float[] values) {
        System.arraycopy(values, 0, this.values, 0, this.values.length);
        return this;
    }

    public Matrix3f setValueAt(float value, int index) {
        values[index] = value;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrix3f other = (Matrix3f) obj;
        if (!Arrays.equals(values, other.values))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + values[M00] + "," + values[M01] + "," + values[M02] + "]"
                + "\n[" + values[M10] + "," + values[M11] + "," + values[M12]
                + "]" + "\n[" + values[M20] + "," + values[M21] + ","
                + values[M22] + "]";
    }

}
