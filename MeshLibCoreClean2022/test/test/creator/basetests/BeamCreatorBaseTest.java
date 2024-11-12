package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.beam.BeamCreator;

// Auto-generated test class to execute base tests for mesh creators
public class BeamCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new BeamCreator().create();
    }

    public void implementsCreatorInterface() {
        BeamCreator creator = new BeamCreator();
        Assert.assertTrue(creator instanceof IMeshCreator);
    }

    @Test
    public void createdMeshIsNotNullByDefault() {
        Assert.assertNotNull(mesh);
    }

    @Test
    public void vertexListIsNotEmpty() {
        Assert.assertFalse(mesh.vertices.isEmpty());
    }

    @Test
    public void getVertexCountReturnsSizeOfVertexList() {
        int vertexCount = mesh.getVertexCount();
        Assert.assertEquals(vertexCount, mesh.getVertices().size());
    }

    @Test
    public void getFaceCountReturnsSizeOfFaceList() {
        int faceCount = mesh.getFaceCount();
        Assert.assertEquals(faceCount, mesh.getFaces().size());
    }

    @Test
    public void createdMeshHasNoLooseVertices() {
        Assert.assertTrue(MeshTest.meshHasNoLooseVertices(mesh));
    }

    @Test
    public void createdMeshHasNoDuplicatedFaces() {
        // Running this test is very time expensive
        Assert.assertTrue(MeshTest.meshHasNoDuplicatedFaces(mesh));
    }
    @Test
    public void eachCallOfCreateReturnsNewUniqueMeshInstance() {
        Mesh3D mesh0 = new BeamCreator().create();
        Mesh3D mesh1 = new BeamCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new BeamCreator().create();
        Mesh3D mesh1 = new BeamCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetDepth() {
        float expected = 1.5074277702778807E38f;
        BeamCreator creator = new BeamCreator();
        creator.setDepth(expected);
        Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getDepthReturnsDefaultValue() {
        float expected = 2.0f;
        BeamCreator creator = new BeamCreator();
        Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getSetHeight() {
        float expected = 2.452743185195673E38f;
        BeamCreator creator = new BeamCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 0.85f;
        BeamCreator creator = new BeamCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetWidth() {
        float expected = 2.0517783365951497E38f;
        BeamCreator creator = new BeamCreator();
        creator.setWidth(expected);
        Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getWidthReturnsDefaultValue() {
        float expected = 0.5f;
        BeamCreator creator = new BeamCreator();
        Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getSetThickness() {
        float expected = 3.3748056240411345E38f;
        BeamCreator creator = new BeamCreator();
        creator.setThickness(expected);
        Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getThicknessReturnsDefaultValue() {
        float expected = 0.1f;
        BeamCreator creator = new BeamCreator();
        Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getSetTaper() {
        float expected = 2.0934880937570236E38f;
        BeamCreator creator = new BeamCreator();
        creator.setTaper(expected);
        Assert.assertEquals(expected, creator.getTaper(), 0);
    }

    @Test
    public void getTaperReturnsDefaultValue() {
        float expected = 0.0f;
        BeamCreator creator = new BeamCreator();
        Assert.assertEquals(expected, creator.getTaper(), 0);
    }

}
