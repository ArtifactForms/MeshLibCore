package mesh.creator.special;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;

public class GemCreator implements IMeshCreator {

    private int segments;

    private float pavillionRadius;

    private float tableRadius;

    private float tableHeight;

    private float pavillionHeight;

    private Mesh3D mesh;

    public GemCreator() {
        segments = 8;
        pavillionRadius = 1;
        tableRadius = 0.6f;
        tableHeight = 0.35f;
        pavillionHeight = 0.8f;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createTable();
        createVertices();
        createFaces();
        return mesh;
    }

    private void createFaces() {
        createMidTableFacesFirstRow();
        createMidTableFacesSecondRow();
        createMidTableFacesThirdRow();
        createPavillionFacesFirstRow();
        createPavillionFacesSecondRow();
    }

    private void createVertices() {
        createMidTableVertices();
        createVerticesAroundOrigin();
        createVerticesAtHalfPavilionHeight();
        createPavillionCenterVertex();
    }

    private void createMidTableFacesFirstRow() {
        int tableCenterIndex = segments;
        int nextIndex = tableCenterIndex + 1;
        for (int i = 0; i < segments; i++) {
            int index0 = i;
            int index1 = nextIndex + i;
            int index2 = (i + 1) % segments;
            mesh.addFace(index0, index1, index2);
        }
    }

    private void createMidTableFacesSecondRow() {
        int tableCenterIndex = segments;
        int nextIndex = tableCenterIndex + 1;
        int bottomIndex = segments * 2 + 1;
        for (int i = 0; i < segments; i++) {
            int index0 = (i + 1) % segments;
            int index1 = nextIndex + i;
            int index2 = bottomIndex + i;
            int index3 = nextIndex + (i + 1) % segments;
            mesh.addFace(index0, index1, index2, index3);
        }
    }

    private void createMidTableFacesThirdRow() {
        int nextIndex = segments + 1;
        int bottomIndex = segments * 2 + 1;
        for (int i = 0; i < segments; i++) {
            int index0 = nextIndex + (i + 1) % segments;
            int index1 = bottomIndex + i;
            int index2 = bottomIndex + (i + 1) % segments;
            mesh.addFace(index0, index1, index2);
        }
    }

    private void createPavillionFacesFirstRow() {
        int nextIndex = segments * 3 + 1;
        int bottomIndex = segments * 2 + 1;
        for (int i = 0; i < segments; i++) {
            int index0 = bottomIndex + (i + 1) % segments;
            int index1 = bottomIndex + i;
            int index2 = nextIndex + (i + 1) % segments;
            mesh.addFace(index0, index1, index2);
        }
    }

    private void createPavillionFacesSecondRow() {
        int nextIndex = segments * 2 + 1;
        int bottomIndex = segments * 3 + 1;
        for (int i = 0; i < segments; i++) {
            int index3 = nextIndex + i % segments;
            int index2 = bottomIndex + (i + 1) % segments;
            int index0 = bottomIndex + i % segments;
            int index1 = segments * 4 + 1;
            mesh.addFace(index0, index1, index2, index3);
        }
    }

    private void createVerticesAtHalfPavilionHeight() {
        float y = pavillionHeight / 2.0f;
        float angle = (Mathf.TWO_PI / segments) / 2.0f;
        float radius = (this.pavillionRadius / 2.0f) / Mathf.cos(angle);
        CircleCreator creator = new CircleCreator();
        creator.setCenterY(y);
        creator.setRadius(radius);
        creator.setVertices(segments);
        Mesh3D circle = creator.create();
        circle.rotateY(-angle);
        mesh.addVertices(circle.getVertices());
    }

    private void createMidTableVertices() {
        float angleStep = Mathf.TWO_PI / segments;
        float offset = -angleStep / 2.0f;
        float radius = ((this.pavillionRadius + tableRadius) / 2.0f)
                / Mathf.cos(offset);
        CircleCreator creator = new CircleCreator();
        creator.setCenterY(-tableHeight / 2.0f);
        creator.setVertices(segments);
        creator.setRadius(radius);
        Mesh3D circle = creator.create();
        circle.rotateY(offset);
        mesh.addVertices(circle.getVertices());
    }

    private void createVerticesAroundOrigin() {
        CircleCreator creator = new CircleCreator();
        creator.setRadius(pavillionRadius);
        creator.setVertices(segments);
        Mesh3D circle = creator.create();
        circle.rotateY(-Mathf.TWO_PI / segments);
        mesh.addVertices(circle.getVertices());
    }

    private void createTable() {
        CircleCreator creator = new CircleCreator();
        creator.setRadius(tableRadius);
        creator.setVertices(segments);
        creator.setCenterY(-tableHeight);
        creator.setFillType(FillType.TRIANGLE_FAN);
        mesh.addVertices(creator.create().getVertices());
        mesh.addFaces(creator.create().getFaces());
    }

    private void createPavillionCenterVertex() {
        mesh.addVertex(0, 0.8f, 0);
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    public int getSegments() {
        return segments;
    }

    public void setSegments(int segments) {
        this.segments = segments;
    }

    public float getPavillionRadius() {
        return pavillionRadius;
    }

    public void setPavillionRadius(float pavillionRadius) {
        this.pavillionRadius = pavillionRadius;
    }

    public float getTableRadius() {
        return tableRadius;
    }

    public void setTableRadius(float tableRadius) {
        this.tableRadius = tableRadius;
    }

    public float getTableHeight() {
        return tableHeight;
    }

    public void setTableHeight(float tableHeight) {
        this.tableHeight = tableHeight;
    }

    public float getPavillionHeight() {
        return pavillionHeight;
    }

    public void setPavillionHeight(float pavillionHeight) {
        this.pavillionHeight = pavillionHeight;
    }

}
