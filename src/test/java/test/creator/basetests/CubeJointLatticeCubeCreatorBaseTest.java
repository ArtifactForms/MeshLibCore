package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.CubeJointLatticeCubeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CubeJointLatticeCubeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new CubeJointLatticeCubeCreator().create();
    }

    public void implementsCreatorInterface() {
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
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
        Mesh3D mesh0 = new CubeJointLatticeCubeCreator().create();
        Mesh3D mesh1 = new CubeJointLatticeCubeCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new CubeJointLatticeCubeCreator().create();
        Mesh3D mesh1 = new CubeJointLatticeCubeCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetScaleZ() {
        float expected = 2.1473268246767526E38f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setScaleZ(expected);
        Assert.assertEquals(expected, creator.getScaleZ(), 0);
    }

    @Test
    public void getScaleZReturnsDefaultValue() {
        float expected = 0.5f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getScaleZ(), 0);
    }

    @Test
    public void getSetSubdivisionsX() {
        int expected = 1636918661;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setSubdivisionsX(expected);
        Assert.assertEquals(expected, creator.getSubdivisionsX());
    }

    @Test
    public void getSubdivisionsXReturnsDefaultValue() {
        int expected = 5;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getSubdivisionsX());
    }

    @Test
    public void getSetSubdivisionsZ() {
        int expected = 36213352;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setSubdivisionsZ(expected);
        Assert.assertEquals(expected, creator.getSubdivisionsZ());
    }

    @Test
    public void getSubdivisionsZReturnsDefaultValue() {
        int expected = 5;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getSubdivisionsZ());
    }

    @Test
    public void getSetTileSizeZ() {
        float expected = 2.6904135209522484E38f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setTileSizeZ(expected);
        Assert.assertEquals(expected, creator.getTileSizeZ(), 0);
    }

    @Test
    public void getTileSizeZReturnsDefaultValue() {
        float expected = 0.1f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getTileSizeZ(), 0);
    }

    @Test
    public void getSetTileSizeX() {
        float expected = 1.1584311837580438E37f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setTileSizeX(expected);
        Assert.assertEquals(expected, creator.getTileSizeX(), 0);
    }

    @Test
    public void getTileSizeXReturnsDefaultValue() {
        float expected = 0.1f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getTileSizeX(), 0);
    }

    @Test
    public void getSetScaleY() {
        float expected = 3.275002492097209E38f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setScaleY(expected);
        Assert.assertEquals(expected, creator.getScaleY(), 0);
    }

    @Test
    public void getScaleYReturnsDefaultValue() {
        float expected = 0.5f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getScaleY(), 0);
    }

    @Test
    public void getSetSubdivisionsY() {
        int expected = 1200244670;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setSubdivisionsY(expected);
        Assert.assertEquals(expected, creator.getSubdivisionsY());
    }

    @Test
    public void getSubdivisionsYReturnsDefaultValue() {
        int expected = 5;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getSubdivisionsY());
    }

    @Test
    public void getSetJointSize() {
        float expected = 3.2477269494900225E38f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setJointSize(expected);
        Assert.assertEquals(expected, creator.getJointSize(), 0);
    }

    @Test
    public void getJointSizeReturnsDefaultValue() {
        float expected = 0.01f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getJointSize(), 0);
    }

    @Test
    public void getSetTileSizeY() {
        float expected = 1.5077335842574076E38f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setTileSizeY(expected);
        Assert.assertEquals(expected, creator.getTileSizeY(), 0);
    }

    @Test
    public void getTileSizeYReturnsDefaultValue() {
        float expected = 0.1f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getTileSizeY(), 0);
    }

    @Test
    public void getSetScaleX() {
        float expected = 3.2905590031447613E38f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        creator.setScaleX(expected);
        Assert.assertEquals(expected, creator.getScaleX(), 0);
    }

    @Test
    public void getScaleXReturnsDefaultValue() {
        float expected = 0.5f;
        CubeJointLatticeCubeCreator creator = new CubeJointLatticeCubeCreator();
        Assert.assertEquals(expected, creator.getScaleX(), 0);
    }

}
