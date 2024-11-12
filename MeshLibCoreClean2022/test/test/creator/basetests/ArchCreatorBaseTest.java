package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.ArchCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ArchCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new ArchCreator().create();
    }

    public void implementsCreatorInterface() {
        ArchCreator creator = new ArchCreator();
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
        Mesh3D mesh0 = new ArchCreator().create();
        Mesh3D mesh1 = new ArchCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new ArchCreator().create();
        Mesh3D mesh1 = new ArchCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetDepth() {
        float expected = 2.9802896448516972E38f;
        ArchCreator creator = new ArchCreator();
        creator.setDepth(expected);
        Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getDepthReturnsDefaultValue() {
        float expected = 1.0f;
        ArchCreator creator = new ArchCreator();
        Assert.assertEquals(expected, creator.getDepth(), 0);
    }

    @Test
    public void getSetSegments() {
        int expected = 803739061;
        ArchCreator creator = new ArchCreator();
        creator.setSegments(expected);
        Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSegmentsReturnsDefaultValue() {
        int expected = 15;
        ArchCreator creator = new ArchCreator();
        Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSetExtendTop() {
        float expected = 2.6027520033536032E38f;
        ArchCreator creator = new ArchCreator();
        creator.setExtendTop(expected);
        Assert.assertEquals(expected, creator.getExtendTop(), 0);
    }

    @Test
    public void getExtendTopReturnsDefaultValue() {
        float expected = 0.5f;
        ArchCreator creator = new ArchCreator();
        Assert.assertEquals(expected, creator.getExtendTop(), 0);
    }

    @Test
    public void getSetExtendBottom() {
        float expected = 1.9401451244481176E38f;
        ArchCreator creator = new ArchCreator();
        creator.setExtendBottom(expected);
        Assert.assertEquals(expected, creator.getExtendBottom(), 0);
    }

    @Test
    public void getExtendBottomReturnsDefaultValue() {
        float expected = 2.0f;
        ArchCreator creator = new ArchCreator();
        Assert.assertEquals(expected, creator.getExtendBottom(), 0);
    }

    @Test
    public void getSetRadius() {
        float expected = 3.0385564103721855E37f;
        ArchCreator creator = new ArchCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        ArchCreator creator = new ArchCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetExtendRight() {
        float expected = 2.4425271855482205E38f;
        ArchCreator creator = new ArchCreator();
        creator.setExtendRight(expected);
        Assert.assertEquals(expected, creator.getExtendRight(), 0);
    }

    @Test
    public void getExtendRightReturnsDefaultValue() {
        float expected = 1.0f;
        ArchCreator creator = new ArchCreator();
        Assert.assertEquals(expected, creator.getExtendRight(), 0);
    }

    @Test
    public void getSetExtendLeft() {
        float expected = 1.6129459256377293E38f;
        ArchCreator creator = new ArchCreator();
        creator.setExtendLeft(expected);
        Assert.assertEquals(expected, creator.getExtendLeft(), 0);
    }

    @Test
    public void getExtendLeftReturnsDefaultValue() {
        float expected = 1.0f;
        ArchCreator creator = new ArchCreator();
        Assert.assertEquals(expected, creator.getExtendLeft(), 0);
    }

}
