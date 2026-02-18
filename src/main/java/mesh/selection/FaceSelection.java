package mesh.selection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.geometry.MeshGeometryUtil;
import mesh.util.Mesh3DUtil;
import mesh.util.TraverseHelper;

public class FaceSelection {

    private Mesh3D mesh;

    private LinkedHashSet<Face3D> faceSet;

    public FaceSelection(Mesh3D mesh) {
        this.mesh = mesh;
        this.faceSet = new LinkedHashSet<Face3D>();
    }

    /**
     * Adds the specified faces to this selection.
     * 
     * @param faces the faces to select.
     */
    public void addAll(Collection<Face3D> faces) {
        faceSet.addAll(faces);
    }

    /**
     * Selects all triangles in the mesh.
     */
    public void selectTriangles() {
        selectByVertexCount(3);
    }

    /**
     * Selects all quads in the mesh.
     */
    public void selectQuads() {
        selectByVertexCount(4);
    }

    /**
     * Selects all faces with a specified number of vertices.
     * 
     * @param n the specified number of vertices
     */
    public void selectByVertexCount(int n) {
        for (Face3D face : mesh.getFaces()) {
            if (face.indices.length == n) {
                faceSet.add(face);
            }
        }
    }

    /**
     * Selects all faces that have a similar normal as the provided one. This is
     * a way to select faces that have the same orientation (angle).
     * 
     * @param normal
     * @param threshold
     */
    public void selectSimilarNormal(Vector3f normal, float threshold) {
        for (Face3D face : mesh.getFaces()) {
            Vector3f normal0 = MeshGeometryUtil.calculateFaceNormal(mesh, face);
            float delta = normal0.distance(normal);
            if (delta <= threshold) {
                faceSet.add(face);
            }
        }
    }

    /**
     * Selects all faces that have a similar perimeter as the provided one.
     * 
     * @param face
     * @param threshold
     */
    public void selectSimilarPerimeter(Face3D face, float threshold) {
        float perimeter0 = Mesh3DUtil.perimeter(mesh, face);
        for (Face3D f : mesh.getFaces()) {
            if (Mathf.abs(
                    Mesh3DUtil.perimeter(mesh, f) - perimeter0) <= threshold) {
                faceSet.add(f);
            }
        }
    }

    public void outerBoundary() {
        TraverseHelper helper = new TraverseHelper(mesh);

        HashSet<Face3D> deselected = new HashSet<Face3D>();

        for (Face3D face : faceSet) {
            for (int i = 0; i < face.indices.length; i++) {
                if (deselected.contains(face))
                    continue;
                int index0 = face.indices[i];
                int index1 = face.indices[(i + 1) % face.indices.length];
                Face3D face1 = helper.getFaceByEdge(index1, index0);
                if (!faceSet.contains(face1))
                    deselected.add(face1);
            }
        }

        faceSet.clear();
        faceSet.addAll(deselected);
    }

    public void innerBoundary() {
        TraverseHelper helper = new TraverseHelper(mesh);

        HashSet<Face3D> deselected = new HashSet<Face3D>();

        for (Face3D face : faceSet) {
            for (int i = 0; i < face.indices.length; i++) {
                if (deselected.contains(face))
                    continue;
                int index0 = face.indices[i];
                int index1 = face.indices[(i + 1) % face.indices.length];
                Face3D face1 = helper.getFaceByEdge(index1, index0);
                if (!faceSet.contains(face1))
                    deselected.add(face);
            }
        }

        faceSet.clear();
        faceSet.addAll(deselected);
    }

    public void selectSphere(Vector3f center, float radius) {
        for (Face3D face : mesh.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = mesh.getVertexAt(face.indices[i]);
                float distance = center.distance(v);
                if (distance <= radius) {
                    faceSet.add(face);
                }
            }
        }
    }

    public void checkerDeselect() {
        new CheckerDeselect(this).deselect();
    }

    public void loop() {
        new LoopSelect(this).select(0);
    }

    public void loop(int index) {
        new LoopSelect(this).select(index);
    }

    public void select(FaceSelectionRules rules) {
        for (Face3D face : mesh.getFaces()) {
            if (rules.isValid(mesh, face))
                faceSet.add(face);
        }
    }

    public void selectByVertex(float x, float y, float z) {
        selectByVertex(new Vector3f(x, y, z));
    }

    public void selectByVertex(Vector3f v) {
        for (Face3D face : mesh.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v0 = mesh.getVertexAt(face.indices[i]);
                if (v0.equals(v)) {
                    faceSet.add(face);
                    continue;
                }
            }
        }
    }

    public void selectByTag(String tag) {
        for (Face3D face : mesh.getFaces()) {
            if (face.getTag().equals(tag))
                faceSet.add(face);
        }
    }

    public void selectDoubles() {
        for (Face3D f0 : mesh.getFaces()) {
            for (Face3D f1 : mesh.getFaces()) {
                if (f0.sharesSameIndices(f1) && f0 != f1) {
                    faceSet.add(f0);
                    faceSet.add(f1);
                }
            }
        }
    }

    public void selectCenterDistanceEquals(Vector3f origin, float distance) {
        for (Face3D face : mesh.getFaces())
            if (origin.distance(MeshGeometryUtil.calculateFaceNormal(mesh, face)) == distance)
                faceSet.add(face);
    }

    public void selectCenterDistanceLessThan(Vector3f origin, float distance) {
        for (Face3D face : mesh.getFaces())
            if (origin.distance(MeshGeometryUtil.calculateFaceCenter(mesh, face)) < distance)
                faceSet.add(face);
    }

    public void selectN(int n) {
        for (int i = 0; i < mesh.getFaceCount(); i++) {
            Face3D face = mesh.getFaceAt(i);
            if (face.indices[0] % n == 0)
                faceSet.add(face);
        }
    }

    public void selectWithCenterYLessThan(float y) {
        for (Face3D face : mesh.getFaces()) {
            Vector3f center = MeshGeometryUtil.calculateFaceCenter(mesh, face);
            if (center.getY() <= y) {
                faceSet.add(face);
            }
        }
    }

    public void selectWithCenterXLessThan(float x) {
        for (Face3D face : mesh.getFaces()) {
            Vector3f center = MeshGeometryUtil.calculateFaceCenter(mesh, face);
            if (center.getX() <= x) {
                faceSet.add(face);
            }
        }
    }

    public void selectRegion(float minX, float maxX, float minY, float maxY,
            float minZ, float maxZ) {
        for (Face3D f : mesh.getFaces()) {
            int n = f.indices.length;
            boolean add = true;
            for (int i = 0; i < n; i++) {
                Vector3f v = mesh.getVertexAt(i);
                add &= (v.getX() >= minX && v.getX() <= maxX && v.getY() >= minY
                        && v.getY() <= maxY && v.getZ() >= minZ
                        && v.getZ() <= maxZ);
            }
            if (add) {
                faceSet.add(f);
            }
        }
    }

    public void removeRegion(float minX, float maxX, float minY, float maxY,
            float minZ, float maxZ) {
        for (Face3D f : mesh.getFaces()) {
            int n = f.indices.length;
            boolean remove = true;
            for (int i = 0; i < n; i++) {
                Vector3f v = mesh.getVertexAt(i);
                remove &= (v.getX() >= minX && v.getX() <= maxX
                        && v.getY() >= minY && v.getY() <= maxY
                        && v.getZ() >= minZ && v.getZ() <= maxZ);
            }
            if (remove) {
                faceSet.remove(f);
            }
        }
    }

    public void selectLeftFaces() {
        for (Face3D f : mesh.getFaces()) {
            Vector3f v = MeshGeometryUtil.calculateFaceNormal(mesh, f);
            Vector3f v0 = new Vector3f(Mathf.round(v.getX()),
                    Mathf.round(v.getY()), Mathf.round(v.getZ()));
            if (v0.getX() == -1) {
                faceSet.add(f);
            }
        }
    }

    public void selectRightFaces() {
        for (Face3D f : mesh.getFaces()) {
            Vector3f v = MeshGeometryUtil.calculateFaceNormal(mesh, f);
            Vector3f v0 = new Vector3f(Mathf.round(v.getX()),
                    Mathf.round(v.getY()), Mathf.round(v.getZ()));
            if (v0.getX() == 1) {
                faceSet.add(f);
            }
        }
    }

    public void selectTopFaces() {
        for (Face3D f : mesh.getFaces()) {
            Vector3f v = MeshGeometryUtil.calculateFaceNormal(mesh, f);
            Vector3f v0 = new Vector3f(Mathf.round(v.getX()),
                    Mathf.round(v.getY()), Mathf.round(v.getZ()));
            if (v0.getY() == -1) {
                faceSet.add(f);
            }
        }
    }

    public void selectBottomFaces() {
        for (Face3D f : mesh.getFaces()) {
            Vector3f v = MeshGeometryUtil.calculateFaceNormal(mesh, f);
            Vector3f v0 = new Vector3f(Mathf.round(v.getX()),
                    Mathf.round(v.getY()), Mathf.round(v.getZ()));
            if (v0.getY() == 1) {
                faceSet.add(f);
            }
        }
    }

    public void selectFrontFaces() {
        for (Face3D f : mesh.getFaces()) {
            Vector3f v = MeshGeometryUtil.calculateFaceNormal(mesh, f);
            Vector3f v0 = new Vector3f(Mathf.round(v.getX()),
                    Mathf.round(v.getY()), Mathf.round(v.getZ()));
            if (v0.getZ() == 1) {
                faceSet.add(f);
            }
        }
    }

    public void selectBackFaces() {
        for (Face3D f : mesh.getFaces()) {
            Vector3f v = MeshGeometryUtil.calculateFaceNormal(mesh, f);
            Vector3f v0 = new Vector3f(Mathf.round(v.getX()),
                    Mathf.round(v.getY()), Mathf.round(v.getZ()));
            if (v0.getZ() == -1) {
                faceSet.add(f);
            }
        }
    }

    public void invert() {
    	LinkedHashSet<Face3D> faceSet = new LinkedHashSet<>();
        faceSet.addAll(mesh.getFaces());
        faceSet.removeAll(this.faceSet);
        this.faceSet.clear();
        this.faceSet.addAll(faceSet);
    }

    public FaceSelection getInvertedSelection() {
        FaceSelection selection = new FaceSelection(mesh);
        LinkedHashSet<Face3D> faceSet = new LinkedHashSet<>();
        faceSet.addAll(mesh.getFaces());
        faceSet.removeAll(this.faceSet);
        selection.faceSet = faceSet;
        return selection;
    }

    public void selectOuter() {
        for (Face3D face : mesh.getFaces()) {
            Vector3f normal = MeshGeometryUtil.calculateFaceNormal(mesh, face);
            Vector3f v = mesh.getVertexAt(face.indices[0]);
            if (normal.dot(v) > 0) {
                faceSet.add(face);
            }
        }
    }

    public void selectInner() {
        for (Face3D face : mesh.getFaces()) {
            Vector3f normal = MeshGeometryUtil.calculateFaceNormal(mesh, face);
            Vector3f v = mesh.getVertexAt(face.indices[0]);
            if (normal.dot(v) < 0) {
                faceSet.add(face);
            }
        }
    }

    public void selectRandom() {
        int random;
        for (Face3D f : mesh.getFaces()) {
            random = Mathf.random(0, 2);
            if (random == 0) {
                faceSet.add(f);
            }
        }
    }

    public FaceSelection createInvert() {
        FaceSelection invert = new FaceSelection(mesh);
        invert.faceSet.addAll(this.faceSet);
        invert.invert();
        return invert;
    }

    public void selectFromTo(int from, int to) {
        for (int i = 0; i < mesh.getFaceCount(); i++) {
        	faceSet.add(mesh.getFaceAt(i));
        }
    }

    public void selectFaceAt(int index) {
        faceSet.add(mesh.getFaceAt(index));
    }

    public void selectAll() {
        faceSet.addAll(mesh.getFaces());
    }

    public void add(Face3D face) {
        if (face != null)
            faceSet.add(face);
    }

    public void addAll(FaceSelection selection) {
        faceSet.addAll(selection.faceSet);
    }

    public void remove(Face3D face) {
        if (face != null)
            faceSet.remove(face);
    }

    public void removeAll(FaceSelection selection) {
        faceSet.removeAll(selection.faceSet);
    }

    public void clear() {
        faceSet.clear();
    }

    public Iterator<Face3D> getIterator() {
        return faceSet.iterator();
    }

    public Collection<Face3D> getFaces() {
        return faceSet;
    }

    public Mesh3D getMesh() {
        return mesh;
    }

    public int size() {
        return faceSet.size();
    }

}
