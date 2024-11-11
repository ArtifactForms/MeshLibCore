package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.platonic.IcosahedronCreator;

// Auto-generated test class to execute base tests for mesh creators
public class IcosahedronCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new IcosahedronCreator().create();
    }

    public void implementsCreatorInterface() {
        IcosahedronCreator creator = new IcosahedronCreator();
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
        Mesh3D mesh0 = new IcosahedronCreator().create();
        Mesh3D mesh1 = new IcosahedronCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new IcosahedronCreator().create();
        Mesh3D mesh1 = new IcosahedronCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetSize() {
        float expected = 1.0703531411038783E38f;
        IcosahedronCreator creator = new IcosahedronCreator();
        creator.setSize(expected);
        Assert.assertEquals(expected, creator.getSize(), 0);
    }

    @Test
    public void getSizeReturnsDefaultValue() {
        float expected = 1.0f;
        IcosahedronCreator creator = new IcosahedronCreator();
        Assert.assertEquals(expected, creator.getSize(), 0);
    }

}
