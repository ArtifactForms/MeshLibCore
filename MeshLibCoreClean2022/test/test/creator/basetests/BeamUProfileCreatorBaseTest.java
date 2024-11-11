package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.beam.BeamUProfileCreator;

// Auto-generated test class to execute base tests for mesh creators
public class BeamUProfileCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new BeamUProfileCreator().create();
    }

    public void implementsCreatorInterface() {
        BeamUProfileCreator creator = new BeamUProfileCreator();
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
        MeshTest.assertMeshHasNoLooseVertices(mesh);
    }

    @Test
    public void createdMeshHasNoDuplicatedFaces() {
        // Running this test is very time expensive
        MeshTest.assertMeshHasNoDuplicatedFaces(mesh);
    }

    @Test
    public void eachCallOfCreateReturnsNewUniqueMeshInstance() {
        Mesh3D mesh0 = new BeamUProfileCreator().create();
        Mesh3D mesh1 = new BeamUProfileCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new BeamUProfileCreator().create();
        Mesh3D mesh1 = new BeamUProfileCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetDepth() {
        float expected = 8.154341218953896E37f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        creator.setDepth(expected);
        Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getDepthReturnsDefaultValue() {
        float expected = 2.0f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getSetHeight() {
        float expected = 2.7138767839429846E38f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 0.85f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetWidth() {
        float expected = 2.719644513047436E37f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        creator.setWidth(expected);
        Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getWidthReturnsDefaultValue() {
        float expected = 0.5f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getSetThickness() {
        float expected = 7.509751216413944E37f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        creator.setThickness(expected);
        Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getThicknessReturnsDefaultValue() {
        float expected = 0.1f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getTaperReturnsDefaultValue() {
        float expected = 0.0f;
        BeamUProfileCreator creator = new BeamUProfileCreator();
        Assert.assertEquals(expected, creator.getTaper(), 0);
    }

}
