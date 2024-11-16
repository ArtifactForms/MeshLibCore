package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.LatticeSphereCreator;

// Auto-generated test class to execute base tests for mesh creators
public class LatticeSphereCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new LatticeSphereCreator().create();
    }

    public void implementsCreatorInterface() {
        LatticeSphereCreator creator = new LatticeSphereCreator();
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
        Mesh3D mesh0 = new LatticeSphereCreator().create();
        Mesh3D mesh1 = new LatticeSphereCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new LatticeSphereCreator().create();
        Mesh3D mesh1 = new LatticeSphereCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRadius() {
        float expected = 1.006279273648829E38f;
        LatticeSphereCreator creator = new LatticeSphereCreator();
        creator.setRadius(expected);
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
        float expected = 1.0f;
        LatticeSphereCreator creator = new LatticeSphereCreator();
        Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetScale() {
        float expected = 2.830220922518836E38f;
        LatticeSphereCreator creator = new LatticeSphereCreator();
        creator.setScale(expected);
        Assert.assertEquals(expected, creator.getScale(), 0);
    }

    @Test
    public void getScaleReturnsDefaultValue() {
        float expected = 0.9f;
        LatticeSphereCreator creator = new LatticeSphereCreator();
        Assert.assertEquals(expected, creator.getScale(), 0);
    }

    @Test
    public void getSetSubdivisions() {
        int expected = 2039513342;
        LatticeSphereCreator creator = new LatticeSphereCreator();
        creator.setSubdivisions(expected);
        Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSubdivisionsReturnsDefaultValue() {
        int expected = 2;
        LatticeSphereCreator creator = new LatticeSphereCreator();
        Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSetThickness() {
        float expected = 2.0300935905883517E38f;
        LatticeSphereCreator creator = new LatticeSphereCreator();
        creator.setThickness(expected);
        Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getThicknessReturnsDefaultValue() {
        float expected = 0.01f;
        LatticeSphereCreator creator = new LatticeSphereCreator();
        Assert.assertEquals(expected, creator.getThickness(), 0);
    }

}
