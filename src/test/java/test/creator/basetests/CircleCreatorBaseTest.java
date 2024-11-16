package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.CircleCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CircleCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new CircleCreator().create();
    }

    public void implementsCreatorInterface() {
        CircleCreator creator = new CircleCreator();
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
    public void createdMeshHasNoDuplicatedFaces() {
        // Running this test is very time expensive
        Assert.assertTrue(MeshTest.meshHasNoDuplicatedFaces(mesh));
    }

    @Test
    public void eachCallOfCreateReturnsNewUniqueMeshInstance() {
        Mesh3D mesh0 = new CircleCreator().create();
        Mesh3D mesh1 = new CircleCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new CircleCreator().create();
        Mesh3D mesh1 = new CircleCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 2.9700119015788042E38f;
        CircleCreator creator = new CircleCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        CircleCreator creator = new CircleCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetVertices() {
        int expected = 454680649;
        CircleCreator creator = new CircleCreator();
        creator.setVertices(expected);
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
        int expected = 32;
        CircleCreator creator = new CircleCreator();
        Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetCenterY() {
        float expected = 2.845354635450411E37f;
        CircleCreator creator = new CircleCreator();
        creator.setCenterY(expected);
        Assert.assertEquals(expected, creator.getCenterY(), 0);
    }

    @Test
    public void getCenterYReturnsDefaultValue() {
        float expected = 0.0f;
        CircleCreator creator = new CircleCreator();
        Assert.assertEquals(expected, creator.getCenterY(), 0);
    }

}
