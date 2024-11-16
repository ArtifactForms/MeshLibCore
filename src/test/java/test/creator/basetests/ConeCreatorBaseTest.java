package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.ConeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ConeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new ConeCreator().create();
    }

    public void implementsCreatorInterface() {
        ConeCreator creator = new ConeCreator();
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
        Mesh3D mesh0 = new ConeCreator().create();
        Mesh3D mesh1 = new ConeCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new ConeCreator().create();
        Mesh3D mesh1 = new ConeCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRotationSegments() {
        int expected = 2047649772;
        ConeCreator creator = new ConeCreator();
        creator.setRotationSegments(expected);
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getRotationSegmentsReturnsDefaultValue() {
        int expected = 32;
        ConeCreator creator = new ConeCreator();
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getSetHeight() {
        float expected = 3.1518699774777145E38f;
        ConeCreator creator = new ConeCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 2.0f;
        ConeCreator creator = new ConeCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetTopRadius() {
        float expected = 3.3524842583790497E38f;
        ConeCreator creator = new ConeCreator();
        creator.setTopRadius(expected);
        Assert.assertEquals(expected, creator.getTopRadius(), 0);
    }

    @Test
    public void getTopRadiusReturnsDefaultValue() {
        float expected = 0.0f;
        ConeCreator creator = new ConeCreator();
        Assert.assertEquals(expected, creator.getTopRadius(), 0);
    }

    @Test
    public void getSetBottomRadius() {
        float expected = 2.3220236511383696E38f;
        ConeCreator creator = new ConeCreator();
        creator.setBottomRadius(expected);
        Assert.assertEquals(expected, creator.getBottomRadius(), 0);
    }

    @Test
    public void getBottomRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        ConeCreator creator = new ConeCreator();
        Assert.assertEquals(expected, creator.getBottomRadius(), 0);
    }

    @Test
    public void getSetHeightSegments() {
        int expected = 763652580;
        ConeCreator creator = new ConeCreator();
        creator.setHeightSegments(expected);
        Assert.assertEquals(expected, creator.getHeightSegments());
    }

    @Test
    public void getHeightSegmentsReturnsDefaultValue() {
        int expected = 10;
        ConeCreator creator = new ConeCreator();
        Assert.assertEquals(expected, creator.getHeightSegments());
    }

}
