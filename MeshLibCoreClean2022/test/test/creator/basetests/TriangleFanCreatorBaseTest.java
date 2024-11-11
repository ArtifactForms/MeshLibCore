package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.TriangleFanCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TriangleFanCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new TriangleFanCreator().create();
    }

    public void implementsCreatorInterface() {
        TriangleFanCreator creator = new TriangleFanCreator();
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
        Mesh3D mesh0 = new TriangleFanCreator().create();
        Mesh3D mesh1 = new TriangleFanCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new TriangleFanCreator().create();
        Mesh3D mesh1 = new TriangleFanCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 1.3783043931123233E38f;
        TriangleFanCreator creator = new TriangleFanCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        TriangleFanCreator creator = new TriangleFanCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetVertices() {
        int expected = 1552371566;
        TriangleFanCreator creator = new TriangleFanCreator();
        creator.setVertices(expected);
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
        int expected = 32;
        TriangleFanCreator creator = new TriangleFanCreator();
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetCenterY() {
        float expected = 2.5741033236475438E38f;
        TriangleFanCreator creator = new TriangleFanCreator();
        creator.setCenterY(expected);
        Assert.assertEquals(expected, creator.getCenterY(), 0);
    }

    @Test
    public void getCenterYReturnsDefaultValue() {
        float expected = 0.0f;
        TriangleFanCreator creator = new TriangleFanCreator();
        Assert.assertEquals(expected, creator.getCenterY(), 0);
    }

}
