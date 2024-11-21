package mesh.util;

import java.util.Arrays;

import math.Mathf;
import math.Matrix3f;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class Mesh3DUtil {

    public static void flipDirection(Mesh3D mesh, Face3D face) {
        int[] copy = Arrays.copyOf(face.indices, face.indices.length);
        for (int i = 0; i < face.indices.length; i++) {
            face.indices[i] = copy[face.indices.length - 1 - i];
        }
    }

    public static void flipDirection(Mesh3D mesh) {
        for (Face3D face : mesh.faces) {
            flipDirection(mesh, face);
        }
    }

    public static float perimeter(Mesh3D mesh, Face3D face) {
        float perimeter = 0;
        for (int i = 0; i < face.indices.length - 2; i++) {
            i++;
            Vector3f v0 = mesh.getVertexAt(face.indices[i]);
            Vector3f v1 = mesh.getVertexAt(face.indices[i + 1]);
            perimeter += v0.distance(v1);
        }
        return perimeter;
    }

    private static void scaleFace(Mesh3D mesh, Face3D face, float scale) {
        Vector3f center = mesh.calculateFaceCenter(face);
        for (int i = 0; i < face.indices.length; i++) {
            Vector3f v = mesh.vertices.get(face.indices[i]);
            v.subtractLocal(center).multLocal(scale).addLocal(center);
        }
    }

    public static void scaleFaceAt(Mesh3D mesh, int index, float scale) {
        Face3D f = mesh.faces.get(index);
        scaleFace(mesh, f, scale);
    }

    public static void rotateFaceX(Mesh3D mesh, Face3D face, float a) {
        Matrix3f m = new Matrix3f(
                1, 0, 0, 0, Mathf.cos(a), -Mathf.sin(a), 0, Mathf.sin(a),
                Mathf.cos(a)
        );

        for (int i = 0; i < face.indices.length; i++) {
            Vector3f v = mesh.vertices.get(face.indices[i]);
            Vector3f v0 = v.mult(m);
            v.set(v.getX(), v0.getY(), v0.getZ());
        }
    }

    public static void rotateFaceY(Mesh3D mesh, Face3D face, float a) {
        Matrix3f m = new Matrix3f(
                Mathf.cos(a), 0, Mathf.sin(a), 0, 1, 0, -Mathf.sin(a), 0,
                Mathf.cos(a)
        );

        for (int i = 0; i < face.indices.length; i++) {
            Vector3f v = mesh.vertices.get(face.indices[i]);
            Vector3f v0 = v.mult(m);
            v.set(v0.getX(), v.getY(), v0.getZ());
        }
    }

    public static void rotateFaceZ(Mesh3D mesh, Face3D face, float a) {
        Matrix3f m = new Matrix3f(
                Mathf.cos(a), -Mathf.sin(a), 0, Mathf.sin(a), Mathf.cos(a), 0,
                0, 0, 1
        );

        for (int i = 0; i < face.indices.length; i++) {
            Vector3f v = mesh.vertices.get(face.indices[i]);
            Vector3f v0 = v.mult(m);
            v.set(v0.getX(), v0.getY(), v.getZ());
        }
    }

    public static void extrudeFace(Mesh3D mesh, Face3D face, float scale,
            float amount) {
        int n = face.indices.length;
        int idx = mesh.vertices.size();
        Vector3f normal = mesh.calculateFaceNormal(face);
        Vector3f center = mesh.calculateFaceCenter(face);

        normal.multLocal(amount);

        for (int i = 0; i < n; i++) {
            Vector3f v0 = mesh.vertices.get(face.indices[i]);
            Vector3f v1 = new Vector3f(v0).subtract(center).mult(scale)
                    .add(center);

            v1.addLocal(normal);
            mesh.vertices.add(v1);
        }

        for (int i = 0; i < n; i++) {
            Face3D f0 = new Face3D(
                    face.indices[i], face.indices[(i + 1) % n],
                    idx + ((i + 1) % n), idx + i
            );
            mesh.add(f0);
        }

        for (int i = 0; i < n; i++) {
            face.indices[i] = idx + i;
        }
    }

}
