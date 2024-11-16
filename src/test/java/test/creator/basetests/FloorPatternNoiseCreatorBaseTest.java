package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.FloorPatternNoiseCreator;

// Auto-generated test class to execute base tests for mesh creators
public class FloorPatternNoiseCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new FloorPatternNoiseCreator().create();
    }

    public void implementsCreatorInterface() {
        FloorPatternNoiseCreator creator = new FloorPatternNoiseCreator();
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
        Mesh3D mesh0 = new FloorPatternNoiseCreator().create();
        Mesh3D mesh1 = new FloorPatternNoiseCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void getSetRadius() {
        float expected = 1.1555839703343955E38f;
        FloorPatternNoiseCreator creator = new FloorPatternNoiseCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 2.0f;
        FloorPatternNoiseCreator creator = new FloorPatternNoiseCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetHeight() {
        float expected = 1.6051114591337228E38f;
        FloorPatternNoiseCreator creator = new FloorPatternNoiseCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 0.2f;
        FloorPatternNoiseCreator creator = new FloorPatternNoiseCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetSubdivisions() {
        int expected = 1678169057;
        FloorPatternNoiseCreator creator = new FloorPatternNoiseCreator();
        creator.setSubdivisions(expected);
        Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSubdivisionsReturnsDefaultValue() {
        int expected = 4;
        FloorPatternNoiseCreator creator = new FloorPatternNoiseCreator();
        Assert.assertEquals(expected, creator.getSubdivisions());
    }

}
