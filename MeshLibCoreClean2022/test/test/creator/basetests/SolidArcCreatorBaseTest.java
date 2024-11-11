package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.SolidArcCreator;

// Auto-generated test class to execute base tests for mesh creators
public class SolidArcCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new SolidArcCreator().create();
    }

    public void implementsCreatorInterface() {
        SolidArcCreator creator = new SolidArcCreator();
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
        Mesh3D mesh0 = new SolidArcCreator().create();
        Mesh3D mesh1 = new SolidArcCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new SolidArcCreator().create();
        Mesh3D mesh1 = new SolidArcCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRotationSegments() {
        int expected = 645650612;
        SolidArcCreator creator = new SolidArcCreator();
        creator.setRotationSegments(expected);
        Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getSetHeight() {
        float expected = 1.8445831046805718E38f;
        SolidArcCreator creator = new SolidArcCreator();
        creator.setHeight(expected);
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
        float expected = 1.0f;
        SolidArcCreator creator = new SolidArcCreator();
        Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetAngle() {
        float expected = 4.330184107566115E37f;
        SolidArcCreator creator = new SolidArcCreator();
        creator.setAngle(expected);
        Assert.assertEquals(expected, creator.getAngle(), 0);
    }

    @Test
    public void getAngleReturnsDefaultValue() {
        float expected = 1.5707964f;
        SolidArcCreator creator = new SolidArcCreator();
        Assert.assertEquals(expected, creator.getAngle(), 0);
    }

    @Test
    public void getSetCapStart() {
        boolean expected = true;
        SolidArcCreator creator = new SolidArcCreator();
        creator.setCapStart(expected);
        Assert.assertEquals(expected, creator.isCapStart());
    }

    @Test
    public void getCapStartReturnsDefaultValue() {
        boolean expected = false;
        SolidArcCreator creator = new SolidArcCreator();
        Assert.assertEquals(expected, creator.isCapStart());
    }

    @Test
    public void getSetCapEnd() {
        boolean expected = true;
        SolidArcCreator creator = new SolidArcCreator();
        creator.setCapEnd(expected);
        Assert.assertEquals(expected, creator.isCapEnd());
    }

    @Test
    public void getCapEndReturnsDefaultValue() {
        boolean expected = false;
        SolidArcCreator creator = new SolidArcCreator();
        Assert.assertEquals(expected, creator.isCapEnd());
    }

    @Test
    public void getSetInnerRadius() {
        float expected = 3.4824199730426413E37f;
        SolidArcCreator creator = new SolidArcCreator();
        creator.setInnerRadius(expected);
        Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getInnerRadiusReturnsDefaultValue() {
        float expected = 2.0f;
        SolidArcCreator creator = new SolidArcCreator();
        Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getSetOuterRadius() {
        float expected = 2.2248089423545538E38f;
        SolidArcCreator creator = new SolidArcCreator();
        creator.setOuterRadius(expected);
        Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

    @Test
    public void getOuterRadiusReturnsDefaultValue() {
        float expected = 3.0f;
        SolidArcCreator creator = new SolidArcCreator();
        Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

}
