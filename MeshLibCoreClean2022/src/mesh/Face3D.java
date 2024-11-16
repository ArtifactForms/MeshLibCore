package mesh;

import java.util.Arrays;

import math.Color;
import math.Vector3f;

public class Face3D {

    public Color color;

    public int[] indices;

    public Vector3f normal;

    public String tag;

    public Face3D() {
        this(new int[0]);
    }

    public Face3D(int... indices) {
        this.color = new Color();
        this.indices = new int[indices.length];
        this.normal = new Vector3f();
        this.tag = "";
        for (int i = 0; i < indices.length; i++)
            this.indices[i] = indices[i];
    }

    public boolean sharesSameIndices(Face3D face) {
        int[] indices0 = Arrays.copyOf(face.indices, face.indices.length);
        int[] indices1 = Arrays.copyOf(indices, indices.length);
        Arrays.sort(indices0);
        Arrays.sort(indices1);
        return Arrays.equals(indices0, indices1);
    }

    public int getIndexAt(int index) {
        return indices[index % indices.length];
    }

    public int getVertexCount() {
        return indices.length;
    }

    public Face3D(Face3D f) {
        this(f.indices);
        this.tag = String.copyValueOf(f.tag);
    }

    @Override
    public String toString() {
        return "Face3D [indices=" + Arrays.toString(indices) + "]";
    }

}
