package mesh.conway;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.TraverseHelper;

public class ConwayAmboModifier implements IMeshModifier {

    private Mesh3D mesh;

    private ArrayList<Face3D> facesToAdd;

    private List<Vector3f> verticesToAdd;

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        setMesh(mesh);
        initializeLists();
        processFaces();
        createFacesFromAdjacentVertices();
        clearOriginalGeometry();
        addNewlyCreatedGeometry();
        return mesh;
    }

    private void processFaces() {
        for (Face3D face : mesh.faces) {
            addNewFace(createEdgePoints(face));
        }
    }

    private int[] createEdgePoints(Face3D face) {
        int nextIndex = getNextVertexIndex();
        int[] edgePointIndices = new int[face.indices.length];

        for (int i = 0; i < face.indices.length; i++) {
            Vector3f edgePoint = calculateEdgePointForEdgeAt(face, i);
            int edgePointIndex = findIndexOfNewVertex(edgePoint);

            if (edgePointIndex > -1) {
                edgePointIndices[i] = edgePointIndex;
            } else {
                addNewVertex(edgePoint);
                edgePointIndices[i] = nextIndex;
                nextIndex++;
            }
        }

        return edgePointIndices;
    }

    /**
     * Calculate the edge point (mid point) of the edge at the specified index.
     */
    private Vector3f calculateEdgePointForEdgeAt(Face3D face, int edgeIndex) {
        int faceVertexCount = face.indices.length;
        int fromIndex = face.indices[edgeIndex % faceVertexCount];
        int toIndex = face.indices[(edgeIndex + 1) % faceVertexCount];
        return calculateEdgePoint(fromIndex, toIndex);
    }

    /**
     * For each vertex of the original mesh, connect the new points that have
     * been generated for the faces that are adjacent to this vertex.
     */
    private void createFacesFromAdjacentVertices() {
        TraverseHelper helper = new TraverseHelper(mesh);
        for (int i = 0; i < mesh.getVertexCount(); i++) {
            Edge3D outgoingEdge = helper.getOutgoing(i);
            Edge3D edge = outgoingEdge;
            Vector<Integer> indices = new Vector<Integer>();
            do {
                Vector3f from = getOriginalVertexAt(edge.getFromIndex());
                Vector3f to = getOriginalVertexAt(edge.getToIndex());
                Vector3f edgePoint = calculateEdgePoint(from, to);
                int index = verticesToAdd.indexOf(edgePoint);
                indices.add(index);
                edge = helper.getPairNext(edge.getFromIndex(), edge.getToIndex());
            } while (!outgoingEdge.equals(edge));
            Face3D face = new Face3D(toReverseArray(indices));
            face.setTag("ambo");
            facesToAdd.add(face);
        }
    }

    private int[] toReverseArray(Vector<Integer> values) {
        int[] a = new int[values.size()];
        for (int j = 0; j < a.length; j++) {
            int index = a.length - j - 1;
            a[index] = values.get(j);
        }
        return a;
    }

    private void initializeLists() {
        initializeFaceList();
        initializeVertexList();
    }

    private void clearOriginalGeometry() {
        removeOriginalVerices();
        removeOriginalFaces();
    }

    private void addNewlyCreatedGeometry() {
        addNewlyVertices();
        addNewlyCreatedFaces();
    }

    private Vector3f getOriginalVertexAt(int index) {
        return mesh.getVertexAt(index);
    }

    private void removeOriginalVerices() {
        mesh.vertices.clear();
    }

    private void addNewlyVertices() {
        mesh.vertices.addAll(verticesToAdd);
    }

    private void initializeFaceList() {
        facesToAdd = new ArrayList<>();
    }

    private void initializeVertexList() {
        verticesToAdd = new ArrayList<Vector3f>();
    }

    private int getNextVertexIndex() {
        return verticesToAdd.size();
    }

    private void addNewVertex(Vector3f v) {
        verticesToAdd.add(v);
    }

    private int findIndexOfNewVertex(Vector3f v) {
        return verticesToAdd.indexOf(v);
    }

    private void addNewlyCreatedFaces() {
        mesh.faces.addAll(facesToAdd);
    }

    private void removeOriginalFaces() {
        mesh.faces.clear();
    }

    private void addNewFace(int[] indices) {
        facesToAdd.add(new Face3D(indices));
    }

    private Vector3f calculateEdgePoint(int startVertexIndex,
            int endVertexIndex) {
        return calculateEdgePoint(getOriginalVertexAt(startVertexIndex),
                getOriginalVertexAt(endVertexIndex));
    }

    private Vector3f calculateEdgePoint(Vector3f start, Vector3f end) {
        return start.add(end).mult(0.5f);
    }

    private void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

}
