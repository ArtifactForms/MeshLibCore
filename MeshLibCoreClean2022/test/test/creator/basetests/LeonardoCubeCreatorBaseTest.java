package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.creative.LeonardoCubeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class LeonardoCubeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new LeonardoCubeCreator().create();
    }

    public void implementsCreatorInterface() {
        LeonardoCubeCreator creator = new LeonardoCubeCreator();
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
        Mesh3D mesh0 = new LeonardoCubeCreator().create();
        Mesh3D mesh1 = new LeonardoCubeCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new LeonardoCubeCreator().create();
        Mesh3D mesh1 = new LeonardoCubeCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetInnerRadius() {
        float expected = 1.9895287934048483E38f;
        LeonardoCubeCreator creator = new LeonardoCubeCreator();
        creator.setInnerRadius(expected);
        Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getInnerRadiusReturnsDefaultValue() {
        float expected = 0.9f;
        LeonardoCubeCreator creator = new LeonardoCubeCreator();
        Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getSetOuterRadius() {
        float expected = 2.6460738221805887E38f;
        LeonardoCubeCreator creator = new LeonardoCubeCreator();
        creator.setOuterRadius(expected);
        Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

    @Test
    public void getOuterRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        LeonardoCubeCreator creator = new LeonardoCubeCreator();
        Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

}
