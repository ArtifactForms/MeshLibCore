package mesh.creator.special.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.creator.special.GemCreator;
import mesh.selection.FaceSelection;
import util.MeshTest;

public class GemTest {

    @Test
    public void implementsCreatorInterface() {
        GemCreator creator = new GemCreator();
        assertTrue(creator instanceof IMeshCreator);
    }

    @Test
    public void createdMeshIsNotNullByDefault() {
        GemCreator creator = new GemCreator();
        assertNotNull(creator.create());
    }

    @Test
    public void getSegmentsReturnsDefaultValue() {
        int expected = 8;
        GemCreator creator = new GemCreator();
        assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1;
        GemCreator creator = new GemCreator();
        assertEquals(expected, creator.getPavillionRadius(), 0);
    }

    @Test
    public void getTableRadiusReturnsDefaultValue() {
        float expected = 0.6f;
        GemCreator creator = new GemCreator();
        assertEquals(expected, creator.getTableRadius(), 0);
    }

    @Test
    public void getTableHeightReturnsDefaultValue() {
        float expected = 0.35f;
        GemCreator creator = new GemCreator();
        assertEquals(expected, creator.getTableHeight(), 0);
    }

    @Test
    public void getPavillionHeightReturnsDefaultValue() {
        float expected = 0.8f;
        GemCreator creator = new GemCreator();
        assertEquals(expected, creator.getPavillionHeight(), 0);
    }

    @Test
    public void getSetSegments() {
        int expected = 76;
        GemCreator creator = new GemCreator();
        creator.setSegments(expected);
        assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSetRadius() {
        float expected = 0.8253f;
        GemCreator creator = new GemCreator();
        creator.setPavillionRadius(expected);
        assertEquals(expected, creator.getPavillionRadius(), 0);
    }

    @Test
    public void getSetTableRadius() {
        float expected = 542.21f;
        GemCreator creator = new GemCreator();
        creator.setTableRadius(expected);
        assertEquals(expected, creator.getTableRadius(), 0);
    }

    @Test
    public void getSetTableHeight() {
        float expected = 762.1234f;
        GemCreator creator = new GemCreator();
        creator.setTableHeight(expected);
        assertEquals(expected, creator.getTableHeight(), 0);
    }

    @Test
    public void getSetPavillionHeight() {
        float expected = 62.1209f;
        GemCreator creator = new GemCreator();
        creator.setPavillionHeight(expected);
        assertEquals(expected, creator.getPavillionHeight(), 0);
    }

    @Test
    public void createdMeshHasFourtyEightFacesByDefault() {
        int expected = 48;
        Mesh3D gem = new GemCreator().create();
        assertEquals(expected, gem.getFaceCount());
    }

    @Test
    public void createdMeshThirtyFourVerticesByDefault() {
        int expected = 34;
        Mesh3D gem = new GemCreator().create();
        assertEquals(expected, gem.getVertexCount());
    }

    @Test
    public void createdMeshHasThirtyTwoTriangularFaces() {
        Mesh3D gem = new GemCreator().create();
        FaceSelection selection = new FaceSelection(gem);
        selection.selectTriangles();
        assertEquals(32, selection.size());
    }

    @Test
    public void createdMeshHasSixteenQuadFaces() {
        Mesh3D gem = new GemCreator().create();
        FaceSelection selection = new FaceSelection(gem);
        selection.selectQuads();
        assertEquals(16, selection.size());
    }

    @Test
    public void trianglesCountDependsOnSegments() {
        int expected = 44;
        GemCreator creator = new GemCreator();
        creator.setSegments(11);
        Mesh3D gem = creator.create();
        FaceSelection selection = new FaceSelection(gem);
        selection.selectTriangles();
        assertEquals(expected, selection.size());
    }

    @Test
    public void quadCountDependsOnSegments() {
        int expected = 54;
        GemCreator creator = new GemCreator();
        creator.setSegments(27);
        Mesh3D gem = creator.create();
        FaceSelection selection = new FaceSelection(gem);
        selection.selectQuads();
        assertEquals(expected, selection.size());
    }

    @Test
    public void createdMeshContainsCenterCircleVertices() {
        CircleCreator circleCreator = new CircleCreator();
        circleCreator.setRadius(1);
        circleCreator.setVertices(8);
        Mesh3D circle = circleCreator.create();
        circle.rotateY(-Mathf.TWO_PI / 8.0f);
        Mesh3D gem = new GemCreator().create();
        for (Vector3f v : circle.vertices) {
            assertTrue(gem.vertices.contains(v));
        }
    }

    @Test
    public void createdMeshContainsTableCircleVertices() {
        CircleCreator circleCreator = new CircleCreator();
        circleCreator.setRadius(0.6f);
        circleCreator.setVertices(8);
        circleCreator.setCenterY(-0.35f);
        Mesh3D circle = circleCreator.create();
        Mesh3D gem = new GemCreator().create();
        for (Vector3f v : circle.vertices) {
            assertTrue(gem.vertices.contains(v));
        }
    }

    @Test
    public void createdMeshContainsPavillionVertex() {
        float pavillionHeight = 0.8f;
        Vector3f v = new Vector3f(0, pavillionHeight, 0);
        Mesh3D gem = new GemCreator().create();
        assertTrue(gem.vertices.contains(v));
    }

    @Test
    public void createdMeshContainsTableVertex() {
        float tableHeight = 0.35f;
        Vector3f v = new Vector3f(0, -tableHeight, 0);
        Mesh3D gem = new GemCreator().create();
        assertTrue(gem.vertices.contains(v));
    }

    @Test
    public void createdMeshContainsVerticesAtTableHeight() {
        int segments = 26;
        float tableRadius = 0.22f;
        float tableHeight = 12.2114f;

        GemCreator creator = new GemCreator();
        creator.setTableHeight(tableHeight);
        creator.setSegments(segments);
        creator.setTableRadius(tableRadius);
        Mesh3D gem = creator.create();

        CircleCreator circleCreator = new CircleCreator();
        circleCreator.setRadius(tableRadius);
        circleCreator.setCenterY(-tableHeight);
        circleCreator.setVertices(segments);

        Mesh3D circle = circleCreator.create();

        for (Vector3f v : circle.vertices) {
            assertTrue(gem.vertices.contains(v));
        }
    }

    @Test
    public void createdMeshContainsVerticesAtCenterHeight() {
        int segments = 56;
        float radius = 2.34f;
        GemCreator creator = new GemCreator();
        creator.setSegments(segments);
        creator.setPavillionRadius(radius);
        Mesh3D gem = creator.create();
        CircleCreator circleCreator = new CircleCreator();
        circleCreator.setRadius(radius);
        circleCreator.setVertices(segments);
        Mesh3D circle = circleCreator.create();
        circle.rotateY(-Mathf.TWO_PI / segments);
        for (Vector3f v : circle.vertices) {
            assertTrue(gem.vertices.contains(v));
        }
    }

    @Test
    public void createdMeshContainsTableMidVertices() {
        float gemCenterRadius = 1;
        float tableRadius = 0.6f;
        int segments = 8;
        float angleStep = Mathf.TWO_PI / segments;
        float offset = angleStep / 2.0f;
        float tableHeight = 0.35f;
        float radius = ((gemCenterRadius + tableRadius) / 2.0f)
                / Mathf.cos(offset);

        CircleCreator circleCreator = new CircleCreator();
        circleCreator.setCenterY(-tableHeight / 2.0f);
        circleCreator.setRadius(radius);
        circleCreator.setVertices(8);
        Mesh3D circle = circleCreator.create();
        circle.rotateY(-offset);

        Mesh3D gem = new GemCreator().create();

        for (Vector3f v : circle.vertices) {
            assertTrue(gem.vertices.contains(v));
        }
    }

    @Test
    public void createdMeshContainsTableMidVerticesWithParameters() {
        float gemCenterRadius = 65.45f;
        float tableRadius = 0.232f;
        int segments = 11;
        float angleStep = Mathf.TWO_PI / segments;
        float offset = -angleStep / 2.0f;
        float tableHeight = 0.2412f;
        float radius = ((gemCenterRadius + tableRadius) / 2.0f)
                / Mathf.cos(offset);

        CircleCreator circleCreator = new CircleCreator();
        circleCreator.setCenterY(-tableHeight / 2.0f);
        circleCreator.setRadius(radius);
        circleCreator.setVertices(segments);
        Mesh3D circle = circleCreator.create();
        circle.rotateY(offset);

        GemCreator creator = new GemCreator();
        creator.setPavillionRadius(gemCenterRadius);
        creator.setTableRadius(tableRadius);
        creator.setSegments(segments);
        creator.setTableHeight(tableHeight);
        Mesh3D gem = creator.create();

        for (Vector3f v : circle.vertices) {
            assertTrue(gem.vertices.contains(v));
        }
    }

    @Test
    public void hasEightTopFaces() {
        GemCreator creator = new GemCreator();
        Mesh3D gem = creator.create();
        FaceSelection selection = new FaceSelection(gem);
        selection.selectSimilarNormal(new Vector3f(0, -1, 0), 0.00001f);
        assertEquals(8, selection.size());
    }

    @Test
    public void hasEightTriangularTopFaces() {
        GemCreator creator = new GemCreator();
        Mesh3D gem = creator.create();
        FaceSelection selection = new FaceSelection(gem);
        selection.selectSimilarNormal(new Vector3f(0, -1, 0), 0.00001f);
        for (Face3D face : selection.getFaces()) {
            assertEquals(3, face.indices.length);
        }
    }

    @Test
    public void createdMeshIsManifold() {
        GemCreator creator = new GemCreator();
        Mesh3D gem = creator.create();
        assertTrue(MeshTest.isManifold(gem));
    }

    @Test
    public void createdMeshHasNoLooseVertices() {
        GemCreator creator = new GemCreator();
        Mesh3D gem = creator.create();
        assertTrue(MeshTest.meshHasNoLooseVertices(gem));
    }

    @Test
    public void createdMeshHasNoDuplicatedFaces() {
        GemCreator creator = new GemCreator();
        Mesh3D gem = creator.create();
        assertTrue(MeshTest.meshHasNoDuplicatedFaces(gem));
    }

    @Test
    public void testNormalsPointOutwards() {
        GemCreator creator = new GemCreator();
        Mesh3D gem = creator.create();
        assertTrue(MeshTest.normalsPointOutwards(gem));
    }

}
