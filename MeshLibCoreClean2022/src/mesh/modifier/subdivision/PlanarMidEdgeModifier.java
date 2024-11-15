package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class PlanarMidEdgeModifier implements IMeshModifier {

    private int nextIndex;

    private int iterations;

    private Mesh3D mesh;

    private List<Face3D> facesToAdd;

    private HashMap<Edge3D, Integer> edgeToMidPointIndex;

    public PlanarMidEdgeModifier() {
        this(1);
    }

    public PlanarMidEdgeModifier(int iterations) {
        this.iterations = iterations;
        this.facesToAdd = new ArrayList<Face3D>();
        this.edgeToMidPointIndex = new HashMap<Edge3D, Integer>();
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        setMesh(mesh);
        for (int i = 0; i < iterations; i++)
            doOneIteration();
        return mesh;
    }

    private void doOneIteration() {
        subdivideFaces();
        removeOldFaces();
        addNewFaces();
        reset();
    }

    private void subdivideFaces() {
        nextIndex = mesh.vertices.size();
        for (Face3D face : mesh.faces) {
            subdivide(face);
        }
    }

    private void subdivide(Face3D face) {
        int[] nGonIndices = new int[face.indices.length];
        for (int i = 0; i < face.indices.length; i++) {
            int nextVertex = face.indices[(i + 1) % face.indices.length];
            Integer midPointIndex = createMidPointAt(face, i);
            Integer nextMidPointIndex = createMidPointAt(face, i + 1);
            addTriangle(nextVertex, nextMidPointIndex, midPointIndex);
            nGonIndices[i] = midPointIndex;
        }
        addNGon(nGonIndices);
    }

    private int createMidPointAt(Face3D face, int index) {
        Edge3D edge = createEdgeAt(face, index);
        Vector3f midPoint = calculateMidPoint(edge);
        Integer midPointIndex = edgeToMidPointIndex.get(edge.createPair());

        if (midPointIndex == null)
            midPointIndex = edgeToMidPointIndex.get(edge);

        if (midPointIndex == null) {
            edgeToMidPointIndex.put(edge, nextIndex);
            midPointIndex = nextIndex;
            mesh.add(midPoint);
            nextIndex++;
        }

        return midPointIndex;
    }

    private Edge3D createEdgeAt(Face3D face, int index) {
        return new Edge3D(face.indices[index % face.indices.length],
                face.indices[(index + 1) % face.indices.length]);
    }

    private void addNGon(int[] indices) {
        facesToAdd.add(new Face3D(indices));
    }

    private Vector3f calculateMidPoint(Edge3D edge) {
        Vector3f from = mesh.getVertexAt(edge.fromIndex);
        Vector3f to = mesh.getVertexAt(edge.toIndex);
        Vector3f midPoint = from.subtract(to).mult(0.5f).add(to);
        return midPoint;
    }

    private void removeOldFaces() {
        mesh.faces.clear();
    }

    private void addNewFaces() {
        mesh.faces.addAll(facesToAdd);
    }

    private void addTriangle(int... indices) {
        facesToAdd.add(new Face3D(indices));
    }

    private void reset() {
        nextIndex = 0;
        edgeToMidPointIndex.clear();
        facesToAdd.clear();
    }

    private void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

}
