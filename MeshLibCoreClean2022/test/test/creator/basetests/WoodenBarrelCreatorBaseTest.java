package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.WoodenBarrelCreator;

// Auto-generated test class to execute base tests for mesh creators
public class WoodenBarrelCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new WoodenBarrelCreator().create();
    }

    public void implementsCreatorInterface() {
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
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
        Mesh3D mesh0 = new WoodenBarrelCreator().create();
        Mesh3D mesh1 = new WoodenBarrelCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new WoodenBarrelCreator().create();
        Mesh3D mesh1 = new WoodenBarrelCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 6.067371408263765E37f;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetRotationSegments() {
        int expected = 2039505349;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        creator.setRotationSegments(expected);
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getRotationSegmentsReturnsDefaultValue() {
        int expected = 16;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getSetHeight() {
        float expected = 1.2327599337948204E38f;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 2.0f;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetInset() {
        float expected = 2.21327929615497E38f;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        creator.setInset(expected);
        Assert.assertEquals(expected, creator.getInset(), 0);
    }

    @Test
    public void getInsetReturnsDefaultValue() {
        float expected = 0.05f;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        Assert.assertEquals(expected, creator.getInset(), 0);
    }

    @Test
    public void getSetHeightSegments() {
        int expected = 1247229428;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        creator.setHeightSegments(expected);
        Assert.assertEquals(expected, creator.getHeightSegments());
    }

    @Test
    public void getHeightSegmentsReturnsDefaultValue() {
        int expected = 8;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        Assert.assertEquals(expected, creator.getHeightSegments());
    }

    @Test
    public void getSetBendFactor() {
        float expected = 1.6588885438786272E38f;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        creator.setBendFactor(expected);
        Assert.assertEquals(expected, creator.getBendFactor(), 0);
    }

    @Test
    public void getBendFactorReturnsDefaultValue() {
        float expected = 0.75f;
        WoodenBarrelCreator creator = new WoodenBarrelCreator();
        Assert.assertEquals(expected, creator.getBendFactor(), 0);
    }

}
