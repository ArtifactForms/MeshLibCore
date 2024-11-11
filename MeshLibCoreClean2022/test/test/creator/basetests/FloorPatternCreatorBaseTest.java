package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.FloorPatternCreator;

// Auto-generated test class to execute base tests for mesh creators
public class FloorPatternCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new FloorPatternCreator().create();
    }

    public void implementsCreatorInterface() {
        FloorPatternCreator creator = new FloorPatternCreator();
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
        Mesh3D mesh0 = new FloorPatternCreator().create();
        Mesh3D mesh1 = new FloorPatternCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new FloorPatternCreator().create();
        Mesh3D mesh1 = new FloorPatternCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 2.1660218949938593E38f;
        FloorPatternCreator creator = new FloorPatternCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 2.0f;
        FloorPatternCreator creator = new FloorPatternCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetHeight() {
        float expected = 8.197881578120216E37f;
        FloorPatternCreator creator = new FloorPatternCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 0.2f;
        FloorPatternCreator creator = new FloorPatternCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetSubdivisions() {
        int expected = 854707412;
        FloorPatternCreator creator = new FloorPatternCreator();
        creator.setSubdivisions(expected);
        Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSubdivisionsReturnsDefaultValue() {
        int expected = 4;
        FloorPatternCreator creator = new FloorPatternCreator();
        Assert.assertEquals(expected, creator.getSubdivisions());
    }

}
