package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.platonic.DodecahedronCreator;

// Auto-generated test class to execute base tests for mesh creators
public class DodecahedronCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new DodecahedronCreator().create();
    }

    public void implementsCreatorInterface() {
        DodecahedronCreator creator = new DodecahedronCreator();
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
        Mesh3D mesh0 = new DodecahedronCreator().create();
        Mesh3D mesh1 = new DodecahedronCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new DodecahedronCreator().create();
        Mesh3D mesh1 = new DodecahedronCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 2.693718520562059E38f;
        DodecahedronCreator creator = new DodecahedronCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.4012585f;
        DodecahedronCreator creator = new DodecahedronCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

}
