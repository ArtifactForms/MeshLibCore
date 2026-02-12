package mesh.creator.special;

import java.util.HashMap;
import java.util.Vector;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.util.TraverseHelper;

public class DualCreator implements IMeshCreator {

    private Mesh3D source;

    private Mesh3D mesh;

    private HashMap<Face3D, Integer> faceVertexMap;

    public DualCreator(Mesh3D source) {
        this.source = source;
        faceVertexMap = new HashMap<Face3D, Integer>();
    }

    private void addFace(Vector<Integer> indices) {
        int[] a = new int[indices.size()];
        for (int j = 0; j < a.length; j++) {
            a[a.length - j - 1] = indices.get(j);
        }
        mesh.add(new Face3D(a));
    }

    private void createFaces() {
        TraverseHelper helper = new TraverseHelper(source);
        for (int i = 0; i < source.getVertexCount(); i++) {
            Edge3D outgoingEdge = helper.getOutgoing(i);
            Edge3D edge = outgoingEdge;
            Vector<Integer> indices = new Vector<Integer>();
            do {
                Face3D face = helper.getFaceByEdge(edge.getFromIndex(),
                        edge.getToIndex());
                indices.add(faceVertexMap.get(face));
                edge = helper.getPairNext(edge.getFromIndex(), edge.getToIndex());
            } while (!outgoingEdge.equals(edge));
            addFace(indices);
        }
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void createVertices() {
        for (int i = 0; i < source.getFaceCount(); i++) {
            Face3D face = source.getFaceAt(i);
            Vector3f center = source.calculateFaceCenter(face);
            addVertex(center);
            mapFaceToCenterVertexIndex(face, i);
        }
    }

    private void addVertex(Vector3f v) {
        mesh.add(v);
    }

    private void mapFaceToCenterVertexIndex(Face3D face, int index) {
        faceVertexMap.put(face, index);
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
    }

}
