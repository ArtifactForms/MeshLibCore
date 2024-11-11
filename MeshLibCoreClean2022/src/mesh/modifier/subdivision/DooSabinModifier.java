package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.TraverseHelper;

/**
 * An implementation of the Doo-Sabin subdivision surface. This type of
 * subdivision surface is based on a generalization of bi-quadratic uniform
 * B-splines and was developed in 1978 by Daniel Doo and Malcolm Sabin.
 * 
 * @see http://graphics.cs.ucdavis.edu/education/CAGDNotes/Doo-Sabin/Doo-Sabin.html
 * 
 * @version 0.1, 4 December 2017
 */
public class DooSabinModifier implements IMeshModifier {

    private int subdivisions;

    private Mesh3D source;

    private Mesh3D target;

    private HashSet<Edge3D> edges;

    private HashMap<Edge3D, Face3D> edgeToFaceMap;

    private HashMap<Vector3f, List<Vector3f>> oldToNewVertexMap;

    private HashMap<VertexFacePair, Integer> vertexFaceMap;

    /**
     * Constructs a new instance of this modifier.
     */
    public DooSabinModifier() {
        this(1);
    }

    /**
     * Constructs a new instance of this modifier with the specified number of
     * subdivision iterations.
     * 
     * @param subdivisions the number of iterations
     */
    public DooSabinModifier(int subdivisions) {
        this.subdivisions = subdivisions;
        oldToNewVertexMap = new HashMap<Vector3f, List<Vector3f>>();
        edgeToFaceMap = new HashMap<>();
        edges = new HashSet<>();
        vertexFaceMap = new HashMap<VertexFacePair, Integer>();
    }

    /**
     * For each original face, create new vertices and connect the new vertices that
     * have been generated for each original vertex of the face.
     */
    private void createAndConnectNewVertices() {
        List<Face3D> faces = source.getFaces(0, source.getFaceCount());
        for (Face3D face : faces) {
            Vector3f center = source.calculateFaceCenter(face);
            int[] indices = new int[face.indices.length];
            int n = indices.length;
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = source.getVertexAt(face.indices[(i + 1) % n]);
                Vector3f res = calculateNewPoint(face, center, i);
                int index = target.getVertexCount();
                target.add(res);
                mapOldToNewVertex(v, res);
                vertexFaceMap.put(new VertexFacePair(v, face), index);
                indices[i] = index;
            }
            target.add(new Face3D(indices));
        }
    }

    /**
     * For each vertex of the original mesh, connect the new points that have been
     * generated for the faces that are adjacent to this vertex.
     */
    private void createFacesFromAdjacentVertices() {
        TraverseHelper helper = new TraverseHelper(source);
        for (int i = 0; i < source.getVertexCount(); i++) {
            Edge3D outgoingEdge = helper.getOutgoing(i);
            Edge3D edge = outgoingEdge;
            Vector<Integer> indices = new Vector<Integer>();
            do {
                Face3D face = helper.getFaceByEdge(edge.fromIndex, edge.toIndex);
                int index = vertexFaceMap.get(new VertexFacePair(source.getVertexAt(i), face));
                indices.add(index);
                edge = helper.getPairNext(edge.fromIndex, edge.toIndex);
            } while (!outgoingEdge.equals(edge));
            addFace(indices);
        }
    }

    /**
     * For each edge of the original mesh, connect the new points that have been
     * generated for the faces that are adjacent to the respective edge.
     */
    private void createFacesAdjacentToEdge() {
        for (Edge3D edge : edges) {
            Face3D face0 = edgeToFaceMap.get(edge);
            Face3D face1 = edgeToFaceMap.get(edge.createPair());
            Vector3f v0 = source.getVertexAt(edge.fromIndex);
            Vector3f v1 = source.getVertexAt(edge.toIndex);
            int idx0 = vertexFaceMap.get(new VertexFacePair(v1, face0));
            int idx1 = vertexFaceMap.get(new VertexFacePair(v0, face0));
            int idx2 = vertexFaceMap.get(new VertexFacePair(v0, face1));
            int idx3 = vertexFaceMap.get(new VertexFacePair(v1, face1));
            target.addFace(idx0, idx1, idx2, idx3);
        }
    }

    /**
     * The average of four particular points taken in a polygon.
     * 
     * <ul>
     * <li>the vertex for which the new point is being defined</li>
     * <li>the two edge points (the midpoints of the edges that are adjacent to this
     * vertex in the polygon)</li>
     * <li>and the face point (average of the vertices of the polygon)</li>
     * </ul>
     * 
     * @param face   the face (polygon) the original vertex belongs to
     * @param center the face point (average of the vertices of the polygon)
     * @param i      the index of the first adjacent vertex (references to the
     *               vertex list of the mesh)
     * @return the average of the four particular points as new {@link Vector3f}
     */
    private Vector3f calculateNewPoint(Face3D face, Vector3f center, int i) {
        int n = face.indices.length;
        Vector3f v0 = source.getVertexAt(face.indices[i % n]);
        Vector3f v1 = source.getVertexAt(face.indices[(i + 1) % n]);
        Vector3f v2 = source.getVertexAt(face.indices[(i + 2) % n]);
        Vector3f e1 = v0.add(v1).mult(0.5f);
        Vector3f e2 = v1.add(v2).mult(0.5f);
        return center.add(v1).add(e1).add(e2).divide(4);
    }

    private void addFace(Vector<Integer> indices) {
        int[] a = new int[indices.size()];
        for (int j = 0; j < a.length; j++) {
            a[a.length - j - 1] = indices.get(j);
        }
        target.add(new Face3D(a));
    }

    private void mapOldToNewVertex(Vector3f oldVertex, Vector3f newVertex) {
        List<Vector3f> vertices = oldToNewVertexMap.get(oldVertex);
        if (vertices == null) {
            vertices = new ArrayList<Vector3f>();
            oldToNewVertexMap.put(oldVertex, vertices);
        }
        vertices.add(newVertex);
    }

    private void mapEdgesToFaces() {
        for (Face3D face : source.faces) {
            for (int i = 0; i < face.indices.length; i++) {
                int a = face.indices[i];
                int b = face.indices[(i + 1) % face.indices.length];
                Edge3D pair0 = new Edge3D(a, b);
                Edge3D pair1 = new Edge3D(b, a);
                if (!edges.contains(pair1)) {
                    edges.add(pair0);
                }
                edgeToFaceMap.put(pair0, face);
            }
        }
    }

    private void subdivide(Mesh3D mesh) {
        source = mesh;
        target = new Mesh3D();

        clear();

        mapEdgesToFaces();
        createAndConnectNewVertices();
        createFacesFromAdjacentVertices();
        createFacesAdjacentToEdge();

        this.source.faces.clear();
        this.source.vertices.clear();
        this.source.faces.addAll(target.faces);
        this.source.vertices.addAll(target.vertices);
    }

    private void clear() {
        edges.clear();
        edgeToFaceMap.clear();
        oldToNewVertexMap.clear();
        vertexFaceMap.clear();
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        for (int i = 0; i < subdivisions; i++) {
            subdivide(mesh);
        }
        return mesh;
    }

    private class VertexFacePair {

        private Vector3f vertex;

        private Face3D face;

        private VertexFacePair(Vector3f vertex, Face3D face) {
            this.vertex = vertex;
            this.face = face;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((face == null) ? 0 : face.hashCode());
            result = prime * result + ((vertex == null) ? 0 : vertex.hashCode());
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
            VertexFacePair other = (VertexFacePair) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (face == null) {
                if (other.face != null)
                    return false;
            } else if (!face.equals(other.face))
                return false;
            if (vertex == null) {
                if (other.vertex != null)
                    return false;
            } else if (!vertex.equals(other.vertex))
                return false;
            return true;
        }

        private DooSabinModifier getOuterType() {
            return DooSabinModifier.this;
        }

    }

}
