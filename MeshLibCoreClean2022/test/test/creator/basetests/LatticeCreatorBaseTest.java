package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.LatticeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class LatticeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new LatticeCreator().create();
    }

    public void implementsCreatorInterface() {
        LatticeCreator creator = new LatticeCreator();
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
        Mesh3D mesh0 = new LatticeCreator().create();
        Mesh3D mesh1 = new LatticeCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new LatticeCreator().create();
        Mesh3D mesh1 = new LatticeCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetHeight() {
        float expected = 4.765419119498823E37f;
        LatticeCreator creator = new LatticeCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 0.2f;
        LatticeCreator creator = new LatticeCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetSubdivisionsX() {
        int expected = 577036410;
        LatticeCreator creator = new LatticeCreator();
        creator.setSubdivisionsX(expected);
        Assert.assertEquals(expected, creator.getSubdivisionsX());
    }

    @Test
    public void getSubdivisionsXReturnsDefaultValue() {
        int expected = 10;
        LatticeCreator creator = new LatticeCreator();
        Assert.assertEquals(expected, creator.getSubdivisionsX());
    }

    @Test
    public void getSetSubdivisionsZ() {
        int expected = 799660529;
        LatticeCreator creator = new LatticeCreator();
        creator.setSubdivisionsZ(expected);
        Assert.assertEquals(expected, creator.getSubdivisionsZ());
    }

    @Test
    public void getSubdivisionsZReturnsDefaultValue() {
        int expected = 10;
        LatticeCreator creator = new LatticeCreator();
        Assert.assertEquals(expected, creator.getSubdivisionsZ());
    }

    @Test
    public void getSetTileSizeZ() {
        float expected = 1.152522126607034E38f;
        LatticeCreator creator = new LatticeCreator();
        creator.setTileSizeZ(expected);
        Assert.assertEquals(expected, creator.getTileSizeZ(), 0);
    }

    @Test
    public void getTileSizeZReturnsDefaultValue() {
        float expected = 0.2f;
        LatticeCreator creator = new LatticeCreator();
        Assert.assertEquals(expected, creator.getTileSizeZ(), 0);
    }

    @Test
    public void getSetTileSizeX() {
        float expected = 1.1012280954875058E38f;
        LatticeCreator creator = new LatticeCreator();
        creator.setTileSizeX(expected);
        Assert.assertEquals(expected, creator.getTileSizeX(), 0);
    }

    @Test
    public void getTileSizeXReturnsDefaultValue() {
        float expected = 0.2f;
        LatticeCreator creator = new LatticeCreator();
        Assert.assertEquals(expected, creator.getTileSizeX(), 0);
    }

    @Test
    public void getSetOpeningPercent() {
        float expected = 1.0720267776100249E38f;
        LatticeCreator creator = new LatticeCreator();
        creator.setOpeningPercent(expected);
        Assert.assertEquals(expected, creator.getOpeningPercent(), 0);
    }

    @Test
    public void getOpeningPercentReturnsDefaultValue() {
        float expected = 0.5f;
        LatticeCreator creator = new LatticeCreator();
        Assert.assertEquals(expected, creator.getOpeningPercent(), 0);
    }

}
