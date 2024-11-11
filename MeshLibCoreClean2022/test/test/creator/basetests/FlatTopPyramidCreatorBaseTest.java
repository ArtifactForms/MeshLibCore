package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.FlatTopPyramidCreator;

// Auto-generated test class to execute base tests for mesh creators
public class FlatTopPyramidCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new FlatTopPyramidCreator().create();
    }

    public void implementsCreatorInterface() {
        FlatTopPyramidCreator creator = new FlatTopPyramidCreator();
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
        Mesh3D mesh0 = new FlatTopPyramidCreator().create();
        Mesh3D mesh1 = new FlatTopPyramidCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new FlatTopPyramidCreator().create();
        Mesh3D mesh1 = new FlatTopPyramidCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetSize() {
        float expected = 1.6062180142586494E38f;
        FlatTopPyramidCreator creator = new FlatTopPyramidCreator();
        creator.setSize(expected);
        Assert.assertEquals(expected, creator.getSize(), 0);
    }

    @Test
    public void getSizeReturnsDefaultValue() {
        float expected = 1.0f;
        FlatTopPyramidCreator creator = new FlatTopPyramidCreator();
        Assert.assertEquals(expected, creator.getSize(), 0);
    }

    @Test
    public void getSetTopScale() {
        float expected = 2.9430294044814006E38f;
        FlatTopPyramidCreator creator = new FlatTopPyramidCreator();
        creator.setTopScale(expected);
        Assert.assertEquals(expected, creator.getTopScale(), 0);
    }

    @Test
    public void getTopScaleReturnsDefaultValue() {
        float expected = 0.5f;
        FlatTopPyramidCreator creator = new FlatTopPyramidCreator();
        Assert.assertEquals(expected, creator.getTopScale(), 0);
    }

}
