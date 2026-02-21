package mesh.creator.platonic;

public enum PlatonicSolid {

    TETRAHEDRON(4, 4, 6, "TETRAHEDRON"),

    HEXAHEDRON(6, 8, 12, "HEXAHEDRON"),

    OCTAHEDRON(8, 6, 12, "OCTAHEDRON"),

    ICOSAHEDRON(20, 12, 30, "ICOSAHEDRON"),

    DODECAHEDRON(12, 20, 30, "DODECAHEDRON");

    private int faceCount;

    private int vertexCount;

    private int edgeCount;

    private String name;

    private PlatonicSolid(int faceCount, int vertexCount, int edgeCount,
            String name) {
        this.faceCount = faceCount;
        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;
        this.name = name;
    }

    public int getFaceCount() {
        return faceCount;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public String getName() {
        return name;
    }

}
