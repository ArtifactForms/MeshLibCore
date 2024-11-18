package mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import math.Mathf;
import math.Matrix3f;
import math.Vector3f;
import mesh.modifier.IMeshModifier;
import mesh.modifier.RemoveDoubleVerticesModifier;
import mesh.util.Bounds3;

public class Mesh3D {

    public Vector3f translation;

    public ArrayList<Vector3f> vertices;

    public ArrayList<Face3D> faces;

    public Mesh3D() {
        translation = new Vector3f();
        vertices = new ArrayList<Vector3f>();
        faces = new ArrayList<Face3D>();
    }
    
    public void apply(IMeshModifier modifier) {
        modifier.modify(this);
    }

    public void updateFaceNormals() {
        for (Face3D face : faces) {
            face.normal = calculateFaceNormal(face);
        }
    }

    public Mesh3D rotateX(float angle) {
        Matrix3f m = new Matrix3f(1, 0, 0, 0, Mathf.cos(angle),
                -Mathf.sin(angle), 0, Mathf.sin(angle), Mathf.cos(angle));

        for (Vector3f v : vertices) {
            Vector3f v0 = v.mult(m);
            v.set(v.getX(), v0.getY(), v0.getZ());
        }

        return this;
    }

    public Mesh3D rotateY(float angle) {
        Matrix3f m = new Matrix3f(Mathf.cos(angle), 0, Mathf.sin(angle), 0, 1,
                0, -Mathf.sin(angle), 0, Mathf.cos(angle));

        for (Vector3f v : vertices) {
            Vector3f v0 = v.mult(m);
            v.set(v0.getX(), v.getY(), v0.getZ());
        }

        return this;
    }

    public Mesh3D rotateZ(float angle) {
        Matrix3f m = new Matrix3f(Mathf.cos(angle), -Mathf.sin(angle), 0,
                Mathf.sin(angle), Mathf.cos(angle), 0, 0, 0, 1);

        for (Vector3f v : vertices) {
            Vector3f v0 = v.mult(m);
            v.set(v0.getX(), v0.getY(), v.getZ());
        }

        return this;
    }

    public Mesh3D translateX(float tx) {
        for (Vector3f v : vertices) {
            v.addLocal(tx, 0, 0);
        }
        return this;
    }

    public Mesh3D translateY(float ty) {
        for (Vector3f v : vertices) {
            v.addLocal(0, ty, 0);
        }
        return this;
    }

    public Mesh3D translateZ(float tz) {
        for (Vector3f v : vertices) {
            v.addLocal(0, 0, tz);
        }
        return this;
    }

    public Mesh3D translate(float tx, float ty, float tz) {
        for (Vector3f v : vertices) {
            v.addLocal(tx, ty, tz);
        }
        return this;
    }

    public Mesh3D translate(Vector3f t) {
        for (Vector3f v : vertices) {
            v.addLocal(t);
        }
        return this;
    }

    public Vector3f calculateFaceNormal(Face3D face) {
        Vector3f faceNormal = new Vector3f();
        for (int i = 0; i < face.indices.length; i++) {
            Vector3f currentVertex = vertices.get(face.indices[i]);
            Vector3f nextVertex = vertices
                    .get(face.indices[(i + 1) % face.indices.length]);
            float x = (currentVertex.getY() - nextVertex.getY())
                    * (currentVertex.getZ() + nextVertex.getZ());
            float y = (currentVertex.getZ() - nextVertex.getZ())
                    * (currentVertex.getX() + nextVertex.getX());
            float z = (currentVertex.getX() - nextVertex.getX())
                    * (currentVertex.getY() + nextVertex.getY());
            faceNormal.addLocal(x, y, z);
        }
        return faceNormal.normalize();
    }

    public Vector3f calculateFaceCenter(Face3D face) {
        Vector3f center = new Vector3f();
        for (int i = 0; i < face.indices.length; i++) {
            center.addLocal(vertices.get(face.indices[i]));
        }
        return center.divideLocal(face.indices.length);
    }

    public Bounds3 calculateBounds() {
        if (vertices.isEmpty())
            return new Bounds3();

        Vector3f min = new Vector3f(getVertexAt(0));
        Vector3f max = new Vector3f(getVertexAt(0));
        Bounds3 bounds = new Bounds3();
        for (Vector3f v : vertices) {
            float minX = v.getX() < min.getX() ? v.getX() : min.getX();
            float minY = v.getY() < min.getY() ? v.getY() : min.getY();
            float minZ = v.getZ() < min.getZ() ? v.getZ() : min.getZ();
            float maxX = v.getX() > max.getX() ? v.getX() : max.getX();
            float maxY = v.getY() > max.getY() ? v.getY() : max.getY();
            float maxZ = v.getZ() > max.getZ() ? v.getZ() : max.getZ();
            min.set(minX, minY, minZ);
            max.set(maxX, maxY, maxZ);
        }
        bounds.setMinMax(min, max);
        return bounds;
    }

    public Mesh3D copy() {
        Mesh3D copy = new Mesh3D();
        List<Vector3f> vertices = copy.vertices;
        List<Face3D> faces = copy.faces;

        for (Vector3f v : this.vertices)
            vertices.add(new Vector3f(v));

        for (Face3D f : this.faces)
            faces.add(new Face3D(f));

        return copy;
    }

    public Mesh3D scaledCopy(Vector3f scale) {
        Mesh3D copy = new Mesh3D();
        List<Vector3f> vertices = copy.vertices;
        List<Face3D> faces = copy.faces;

        for (Vector3f v : this.vertices)
            vertices.add(new Vector3f(v).multLocal(scale));

        for (Face3D f : this.faces)
            faces.add(new Face3D(f));

        return copy;
    }

    public Mesh3D append(Mesh3D... meshes) {
        Mesh3D result = new Mesh3D();

        result = appendUtil(meshes);
        result = appendUtil(this, result);

        vertices.clear();
        vertices.addAll(result.vertices);

        faces.clear();
        faces.addAll(result.faces);

        return this;
    }

    private Mesh3D appendUtil(Mesh3D... meshes) {
        // FIXME copy vertices and faces
        int n = 0;
        Mesh3D mesh = new Mesh3D();
        List<Vector3f> vertices = mesh.vertices;
        List<Face3D> faces = mesh.faces;

        for (int i = 0; i < meshes.length; i++) {
            Mesh3D m = meshes[i];
            vertices.addAll(m.vertices);
            faces.addAll(meshes[i].faces);
            for (Face3D f : meshes[i].faces) {
                for (int j = 0; j < f.indices.length; j++) {
                    f.indices[j] += n;
                }
            }
            n += m.getVertexCount();
        }

        return mesh;
    }

    public void removeDoubles(int decimalPlaces) {
        for (Vector3f v : vertices)
            v.roundLocalDecimalPlaces(decimalPlaces);
        removeDoubles();
    }

    public void removeDoubles() {
        new RemoveDoubleVerticesModifier().modify(this);
    }

    public Collection<Face3D> getSelection(String tag) {
        ArrayList<Face3D> result = new ArrayList<Face3D>();
        for (Face3D face : faces) {
            if (face.tag.equals(tag))
                result.add(face);
        }
        return result;
    }

    public void clearFaces() {
        faces.clear();
    }

    public void clearVertices() {
        vertices.clear();
    }

    public void addVertex(float x, float y, float z) {
        vertices.add(new Vector3f(x, y, z));
    }

    public void addFace(int... indices) {
        faces.add(new Face3D(indices));
    }

    public void addVertices(Collection<Vector3f> vertices) {
        this.vertices.addAll(vertices);
    }

    public void addFaces(Collection<Face3D> faces) {
        this.faces.addAll(faces);
    }

    public void removeFace(Face3D face) {
        faces.remove(face);
    }

    public void removeFaces(Collection<Face3D> faces) {
        this.faces.removeAll(faces);
    }

    public void add(Vector3f... vertices) {
        this.vertices.addAll(Arrays.asList(vertices));
    }

    public void add(Face3D... faces) {
        this.faces.addAll(Arrays.asList(faces));
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getFaceCount() {
        return faces.size();
    }

    public List<Face3D> getFaces() {
        return new ArrayList<Face3D>(faces);
    }

    public List<Face3D> getFaces(int from, int to) {
        return new ArrayList<>(faces.subList(from, to));
    }

    public List<Vector3f> getVertices(int from, int to) {
        return new ArrayList<>(vertices.subList(from, to));
    }

    public List<Vector3f> getVertices() {
        return new ArrayList<>(vertices);
    }

    public Vector3f getVertexAt(int index) {
        return vertices.get(index);
    }

    public Face3D getFaceAt(int index) {
        return faces.get(index);
    }

}
