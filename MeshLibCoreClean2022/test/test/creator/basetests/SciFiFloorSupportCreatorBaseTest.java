package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.SciFiFloorSupportCreator;

// Auto-generated test class to execute base tests for mesh creators
public class SciFiFloorSupportCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new SciFiFloorSupportCreator().create();
    }

    public void implementsCreatorInterface() {
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
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
        Mesh3D mesh0 = new SciFiFloorSupportCreator().create();
        Mesh3D mesh1 = new SciFiFloorSupportCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new SciFiFloorSupportCreator().create();
        Mesh3D mesh1 = new SciFiFloorSupportCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetMirrorGap() {
        float expected = 3.411652473443284E37f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setMirrorGap(expected);
        Assert.assertEquals(expected, creator.getMirrorGap(), 0);
    }

    @Test
    public void getMirrorGapReturnsDefaultValue() {
        float expected = 0.0f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getMirrorGap(), 0);
    }

    @Test
    public void getSetGap() {
        float expected = 9.949937649506526E37f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setGap(expected);
        Assert.assertEquals(expected, creator.getGap(), 0);
    }

    @Test
    public void getGapReturnsDefaultValue() {
        float expected = 0.0f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getGap(), 0);
    }

    @Test
    public void getSetSupportCount() {
        int expected = 654700334;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setSupportCount(expected);
        Assert.assertEquals(expected, creator.getSupportCount());
    }

    @Test
    public void getSupportCountReturnsDefaultValue() {
        int expected = 1;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getSupportCount());
    }

    @Test
    public void getSetCapBack() {
        boolean expected = true;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setCapBack(expected);
        Assert.assertEquals(expected, creator.isCapBack());
    }

    @Test
    public void getCapBackReturnsDefaultValue() {
        boolean expected = true;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.isCapBack());
    }

    @Test
    public void getSetExtendBack() {
        float expected = 2.4398761637168864E38f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setExtendBack(expected);
        Assert.assertEquals(expected, creator.getExtendBack(), 0);
    }

    @Test
    public void getExtendBackReturnsDefaultValue() {
        float expected = 0.1f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getExtendBack(), 0);
    }

    @Test
    public void getSetCapTop() {
        boolean expected = true;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setCapTop(expected);
        Assert.assertEquals(expected, creator.isCapTop());
    }

    @Test
    public void getCapTopReturnsDefaultValue() {
        boolean expected = true;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.isCapTop());
    }

    @Test
    public void getSetMirror() {
        boolean expected = true;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setMirror(expected);
        Assert.assertEquals(expected, creator.isMirror());
    }

    @Test
    public void getMirrorReturnsDefaultValue() {
        boolean expected = false;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.isMirror());
    }

    @Test
    public void getSetExtendFront() {
        float expected = 1.5878246043179227E38f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setExtendFront(expected);
        Assert.assertEquals(expected, creator.getExtendFront(), 0);
    }

    @Test
    public void getExtendFrontReturnsDefaultValue() {
        float expected = 0.1f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getExtendFront(), 0);
    }

    @Test
    public void getSetSegments() {
        int expected = 1910518472;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setSegments(expected);
        Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSegmentsReturnsDefaultValue() {
        int expected = 5;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSetExtendTop() {
        float expected = 2.553132630077597E38f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setExtendTop(expected);
        Assert.assertEquals(expected, creator.getExtendTop(), 0);
    }

    @Test
    public void getExtendTopReturnsDefaultValue() {
        float expected = 0.2f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getExtendTop(), 0);
    }

    @Test
    public void getSetExtendBottom() {
        float expected = 1.1598633113596616E38f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setExtendBottom(expected);
        Assert.assertEquals(expected, creator.getExtendBottom(), 0);
    }

    @Test
    public void getExtendBottomReturnsDefaultValue() {
        float expected = 0.5f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getExtendBottom(), 0);
    }

    @Test
    public void getSetRadius() {
        float expected = 2.7419347627376233E38f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 2.0f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetCapBottom() {
        boolean expected = true;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setCapBottom(expected);
        Assert.assertEquals(expected, creator.isCapBottom());
    }

    @Test
    public void getCapBottomReturnsDefaultValue() {
        boolean expected = true;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.isCapBottom());
    }

    @Test
    public void getSetWidth() {
        float expected = 2.9217566907871255E38f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        creator.setWidth(expected);
        Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getWidthReturnsDefaultValue() {
        float expected = 0.7f;
        SciFiFloorSupportCreator creator = new SciFiFloorSupportCreator();
        Assert.assertEquals(expected, creator.getWidth(), 0);
    }

}
