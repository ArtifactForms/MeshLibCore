package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.TorusCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TorusCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TorusCreator().create();
    }

    public void implementsCreatorInterface() {
        TorusCreator creator = new TorusCreator();
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
        Mesh3D mesh0 = new TorusCreator().create();
        Mesh3D mesh1 = new TorusCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new TorusCreator().create();
        Mesh3D mesh1 = new TorusCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetMajorRadius() {
        float expected = 2.949975398568701E38f;
        TorusCreator creator = new TorusCreator();
        creator.setMajorRadius(expected);
        Assert.assertEquals(expected, creator.getMajorRadius(), 0);
    }

    @Test
    public void getMajorRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        TorusCreator creator = new TorusCreator();
        Assert.assertEquals(expected, creator.getMajorRadius(), 0);
    }

    @Test
    public void getSetMinorRadius() {
        float expected = 2.173017761603318E38f;
        TorusCreator creator = new TorusCreator();
        creator.setMinorRadius(expected);
        Assert.assertEquals(expected, creator.getMinorRadius(), 0);
    }

    @Test
    public void getMinorRadiusReturnsDefaultValue() {
        float expected = 0.25f;
        TorusCreator creator = new TorusCreator();
        Assert.assertEquals(expected, creator.getMinorRadius(), 0);
    }

    @Test
    public void getSetMinorSegments() {
        int expected = 2069354220;
        TorusCreator creator = new TorusCreator();
        creator.setMinorSegments(expected);
        Assert.assertEquals(expected, creator.getMinorSegments());
    }

    @Test
    public void getMinorSegmentsReturnsDefaultValue() {
        int expected = 12;
        TorusCreator creator = new TorusCreator();
        Assert.assertEquals(expected, creator.getMinorSegments());
    }

    @Test
    public void getSetMajorSegments() {
        int expected = 110690080;
        TorusCreator creator = new TorusCreator();
        creator.setMajorSegments(expected);
        Assert.assertEquals(expected, creator.getMajorSegments());
    }

    @Test
    public void getMajorSegmentsReturnsDefaultValue() {
        int expected = 48;
        TorusCreator creator = new TorusCreator();
        Assert.assertEquals(expected, creator.getMajorSegments());
    }

}
