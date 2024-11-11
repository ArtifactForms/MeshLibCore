package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.special.DiamondCreator;

// Auto-generated test class to execute base tests for mesh creators
public class DiamondCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new DiamondCreator().create();
    }

    public void implementsCreatorInterface() {
        DiamondCreator creator = new DiamondCreator();
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
        Mesh3D mesh0 = new DiamondCreator().create();
        Mesh3D mesh1 = new DiamondCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new DiamondCreator().create();
        Mesh3D mesh1 = new DiamondCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetSegments() {
        int expected = 1035301119;
        DiamondCreator creator = new DiamondCreator();
        creator.setSegments(expected);
        Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSegmentsReturnsDefaultValue() {
        int expected = 32;
        DiamondCreator creator = new DiamondCreator();
        Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSetPavillionHeight() {
        float expected = 1.2706270545458843E38f;
        DiamondCreator creator = new DiamondCreator();
        creator.setPavillionHeight(expected);
        Assert.assertEquals(expected, creator.getPavillionHeight(), 0);
    }

    @Test
    public void getPavillionHeightReturnsDefaultValue() {
        float expected = 0.8f;
        DiamondCreator creator = new DiamondCreator();
        Assert.assertEquals(expected, creator.getPavillionHeight(), 0);
    }

    @Test
    public void getSetCrownHeight() {
        float expected = 1.7065177090656106E38f;
        DiamondCreator creator = new DiamondCreator();
        creator.setCrownHeight(expected);
        Assert.assertEquals(expected, creator.getCrownHeight(), 0);
    }

    @Test
    public void getCrownHeightReturnsDefaultValue() {
        float expected = 0.35f;
        DiamondCreator creator = new DiamondCreator();
        Assert.assertEquals(expected, creator.getCrownHeight(), 0);
    }

    @Test
    public void getSetGirdleRadius() {
        float expected = 2.9322909825069823E38f;
        DiamondCreator creator = new DiamondCreator();
        creator.setGirdleRadius(expected);
        Assert.assertEquals(expected, creator.getGirdleRadius(), 0);
    }

    @Test
    public void getGirdleRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        DiamondCreator creator = new DiamondCreator();
        Assert.assertEquals(expected, creator.getGirdleRadius(), 0);
    }

    @Test
    public void getSetTableRadius() {
        float expected = 1.0634958068870788E37f;
        DiamondCreator creator = new DiamondCreator();
        creator.setTableRadius(expected);
        Assert.assertEquals(expected, creator.getTableRadius(), 0);
    }

    @Test
    public void getTableRadiusReturnsDefaultValue() {
        float expected = 0.6f;
        DiamondCreator creator = new DiamondCreator();
        Assert.assertEquals(expected, creator.getTableRadius(), 0);
    }

}
