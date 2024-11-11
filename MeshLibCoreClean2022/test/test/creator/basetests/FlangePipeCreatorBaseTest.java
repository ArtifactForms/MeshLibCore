package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.FlangePipeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class FlangePipeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new FlangePipeCreator().create();
    }

    public void implementsCreatorInterface() {
        FlangePipeCreator creator = new FlangePipeCreator();
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
        Mesh3D mesh0 = new FlangePipeCreator().create();
        Mesh3D mesh1 = new FlangePipeCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new FlangePipeCreator().create();
        Mesh3D mesh1 = new FlangePipeCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetBoltHeadPercantage() {
        float expected = 3.2533415729073378E38f;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setBoltHeadPercantage(expected);
        Assert.assertEquals(expected, creator.getBoltHeadPercantage(), 0);
    }

    @Test
    public void getBoltHeadPercantageReturnsDefaultValue() {
        float expected = 0.8f;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getBoltHeadPercantage(), 0);
    }

    @Test
    public void getSetRotationSegments() {
        int expected = 1050538160;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setRotationSegments(expected);
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getRotationSegmentsReturnsDefaultValue() {
        int expected = 16;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getSetPipeSegmentLength() {
        float expected = 1.8711448693283982E38f;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setPipeSegmentLength(expected);
        Assert.assertEquals(expected, creator.getPipeSegmentLength(), 0);
    }

    @Test
    public void getPipeSegmentLengthReturnsDefaultValue() {
        float expected = 5.0f;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getPipeSegmentLength(), 0);
    }

    @Test
    public void getSetFlangeOuterRadius() {
        float expected = 3.39750000428076E38f;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setFlangeOuterRadius(expected);
        Assert.assertEquals(expected, creator.getFlangeOuterRadius(), 0);
    }

    @Test
    public void getFlangeOuterRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getFlangeOuterRadius(), 0);
    }

    @Test
    public void getSetFlangeGrooveWidth() {
        float expected = 6.907640333718731E37f;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setFlangeGrooveWidth(expected);
        Assert.assertEquals(expected, creator.getFlangeGrooveWidth(), 0);
    }

    @Test
    public void getFlangeGrooveWidthReturnsDefaultValue() {
        float expected = 0.05f;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getFlangeGrooveWidth(), 0);
    }

    @Test
    public void getSetPipeRadius() {
        float expected = 2.214980450116516E38f;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setPipeRadius(expected);
        Assert.assertEquals(expected, creator.getPipeRadius(), 0);
    }

    @Test
    public void getPipeRadiusReturnsDefaultValue() {
        float expected = 0.7f;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getPipeRadius(), 0);
    }

    @Test
    public void getSetBoltHeadHeight() {
        float expected = 7.514543564051123E36f;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setBoltHeadHeight(expected);
        Assert.assertEquals(expected, creator.getBoltHeadHeight(), 0);
    }

    @Test
    public void getBoltHeadHeightReturnsDefaultValue() {
        float expected = 0.05f;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getBoltHeadHeight(), 0);
    }

    @Test
    public void getSetBoltCount() {
        int expected = 1839921658;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setBoltCount(expected);
        Assert.assertEquals(expected, creator.getBoltCount());
    }

    @Test
    public void getBoltCountReturnsDefaultValue() {
        int expected = 8;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getBoltCount());
    }

    @Test
    public void getSetFlangeDepth() {
        float expected = 6.042085029512387E37f;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setFlangeDepth(expected);
        Assert.assertEquals(expected, creator.getFlangeDepth(), 0);
    }

    @Test
    public void getFlangeDepthReturnsDefaultValue() {
        float expected = 0.2f;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getFlangeDepth(), 0);
    }

    @Test
    public void getSetSegmentCount() {
        int expected = 94283261;
        FlangePipeCreator creator = new FlangePipeCreator();
        creator.setSegmentCount(expected);
        Assert.assertEquals(expected, creator.getSegmentCount());
    }

    @Test
    public void getSegmentCountReturnsDefaultValue() {
        int expected = 1;
        FlangePipeCreator creator = new FlangePipeCreator();
        Assert.assertEquals(expected, creator.getSegmentCount());
    }

}
