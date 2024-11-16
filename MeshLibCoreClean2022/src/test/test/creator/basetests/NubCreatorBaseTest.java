package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.NubCreator;

// Auto-generated test class to execute base tests for mesh creators
public class NubCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new NubCreator().create();
    }

    public void implementsCreatorInterface() {
        NubCreator creator = new NubCreator();
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
        Mesh3D mesh0 = new NubCreator().create();
        Mesh3D mesh1 = new NubCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new NubCreator().create();
        Mesh3D mesh1 = new NubCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 2.718180080773435E38f;
        NubCreator creator = new NubCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        NubCreator creator = new NubCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetRotationSegments() {
        int expected = 1447198058;
        NubCreator creator = new NubCreator();
        creator.setRotationSegments(expected);
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getRotationSegmentsReturnsDefaultValue() {
        int expected = 16;
        NubCreator creator = new NubCreator();
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getSetSubdivisions() {
        int expected = 2111894902;
        NubCreator creator = new NubCreator();
        creator.setSubdivisions(expected);
        Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSubdivisionsReturnsDefaultValue() {
        int expected = 1;
        NubCreator creator = new NubCreator();
        Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSetMinorRadius() {
        float expected = 2.0261534054032868E38f;
        NubCreator creator = new NubCreator();
        creator.setMinorRadius(expected);
        Assert.assertEquals(expected, creator.getMinorRadius(), 0);
    }

    @Test
    public void getMinorRadiusReturnsDefaultValue() {
        float expected = 0.7f;
        NubCreator creator = new NubCreator();
        Assert.assertEquals(expected, creator.getMinorRadius(), 0);
    }

    @Test
    public void getSetHeightSegments() {
        int expected = 240657000;
        NubCreator creator = new NubCreator();
        creator.setHeightSegments(expected);
        Assert.assertEquals(expected, creator.getHeightSegments());
    }

    @Test
    public void getHeightSegmentsReturnsDefaultValue() {
        int expected = 7;
        NubCreator creator = new NubCreator();
        Assert.assertEquals(expected, creator.getHeightSegments());
    }

    @Test
    public void getSetSegmentHeight() {
        float expected = 1.1086817666826105E38f;
        NubCreator creator = new NubCreator();
        creator.setSegmentHeight(expected);
        Assert.assertEquals(expected, creator.getSegmentHeight(), 0);
    }

    @Test
    public void getSegmentHeightReturnsDefaultValue() {
        float expected = 0.5f;
        NubCreator creator = new NubCreator();
        Assert.assertEquals(expected, creator.getSegmentHeight(), 0);
    }

}
