package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.ModularKitFloorSegmentCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ModularKitFloorSegmentCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new ModularKitFloorSegmentCreator().create();
    }

    public void implementsCreatorInterface() {
        ModularKitFloorSegmentCreator creator = new ModularKitFloorSegmentCreator();
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
        Mesh3D mesh0 = new ModularKitFloorSegmentCreator().create();
        Mesh3D mesh1 = new ModularKitFloorSegmentCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new ModularKitFloorSegmentCreator().create();
        Mesh3D mesh1 = new ModularKitFloorSegmentCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetFloorHeight() {
        float expected = 1.3805743800628602E38f;
        ModularKitFloorSegmentCreator creator = new ModularKitFloorSegmentCreator();
        creator.setFloorHeight(expected);
        Assert.assertEquals(expected, creator.getFloorHeight(), 0);
    }

    @Test
    public void getFloorHeightReturnsDefaultValue() {
        float expected = 0.0f;
        ModularKitFloorSegmentCreator creator = new ModularKitFloorSegmentCreator();
        Assert.assertEquals(expected, creator.getFloorHeight(), 0);
    }

    @Test
    public void getSetFloorWidth() {
        float expected = 2.2454117122952173E38f;
        ModularKitFloorSegmentCreator creator = new ModularKitFloorSegmentCreator();
        creator.setFloorWidth(expected);
        Assert.assertEquals(expected, creator.getFloorWidth(), 0);
    }

    @Test
    public void getFloorWidthReturnsDefaultValue() {
        float expected = 4.0f;
        ModularKitFloorSegmentCreator creator = new ModularKitFloorSegmentCreator();
        Assert.assertEquals(expected, creator.getFloorWidth(), 0);
    }

    @Test
    public void getSetFloorDepth() {
        float expected = 3.342268463039947E38f;
        ModularKitFloorSegmentCreator creator = new ModularKitFloorSegmentCreator();
        creator.setFloorDepth(expected);
        Assert.assertEquals(expected, creator.getFloorDepth(), 0);
    }

    @Test
    public void getFloorDepthReturnsDefaultValue() {
        float expected = 4.0f;
        ModularKitFloorSegmentCreator creator = new ModularKitFloorSegmentCreator();
        Assert.assertEquals(expected, creator.getFloorDepth(), 0);
    }

}
