package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.CubeJointLatticeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CubeJointLatticeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new CubeJointLatticeCreator().create();
    }

    public void implementsCreatorInterface() {
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
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
        Mesh3D mesh0 = new CubeJointLatticeCreator().create();
        Mesh3D mesh1 = new CubeJointLatticeCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new CubeJointLatticeCreator().create();
        Mesh3D mesh1 = new CubeJointLatticeCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetSubdivisionsX() {
        int expected = 1305716536;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        creator.setSubdivisionsX(expected);
        Assert.assertEquals(expected, creator.getSubdivisionsX());
    }

    @Test
    public void getSubdivisionsXReturnsDefaultValue() {
        int expected = 10;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        Assert.assertEquals(expected, creator.getSubdivisionsX());
    }

    @Test
    public void getSetTileSizeX() {
        float expected = 1.0149269167363893E38f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        creator.setTileSizeX(expected);
        Assert.assertEquals(expected, creator.getTileSizeX(), 0);
    }

    @Test
    public void getTileSizeXReturnsDefaultValue() {
        float expected = 0.1f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        Assert.assertEquals(expected, creator.getTileSizeX(), 0);
    }

    @Test
    public void getSetScaleY() {
        float expected = 1.392238633671224E38f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        creator.setScaleY(expected);
        Assert.assertEquals(expected, creator.getScaleY(), 0);
    }

    @Test
    public void getScaleYReturnsDefaultValue() {
        float expected = 0.5f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        Assert.assertEquals(expected, creator.getScaleY(), 0);
    }

    @Test
    public void getSetSubdivisionsY() {
        int expected = 166168903;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        creator.setSubdivisionsY(expected);
        Assert.assertEquals(expected, creator.getSubdivisionsY());
    }

    @Test
    public void getSubdivisionsYReturnsDefaultValue() {
        int expected = 10;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        Assert.assertEquals(expected, creator.getSubdivisionsY());
    }

    @Test
    public void getSetJointSize() {
        float expected = 2.7856381229146155E38f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        creator.setJointSize(expected);
        Assert.assertEquals(expected, creator.getJointSize(), 0);
    }

    @Test
    public void getJointSizeReturnsDefaultValue() {
        float expected = 0.01f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        Assert.assertEquals(expected, creator.getJointSize(), 0);
    }

    @Test
    public void getSetTileSizeY() {
        float expected = 1.6462671885750933E38f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        creator.setTileSizeY(expected);
        Assert.assertEquals(expected, creator.getTileSizeY(), 0);
    }

    @Test
    public void getTileSizeYReturnsDefaultValue() {
        float expected = 0.1f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        Assert.assertEquals(expected, creator.getTileSizeY(), 0);
    }

    @Test
    public void getSetScaleX() {
        float expected = 1.3011478216505725E38f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        creator.setScaleX(expected);
        Assert.assertEquals(expected, creator.getScaleX(), 0);
    }

    @Test
    public void getScaleXReturnsDefaultValue() {
        float expected = 0.5f;
        CubeJointLatticeCreator creator = new CubeJointLatticeCreator();
        Assert.assertEquals(expected, creator.getScaleX(), 0);
    }

}
