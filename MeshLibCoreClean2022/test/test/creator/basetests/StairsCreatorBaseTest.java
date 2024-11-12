package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.StairsCreator;

// Auto-generated test class to execute base tests for mesh creators
public class StairsCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
        mesh = new StairsCreator().create();
    }

    public void implementsCreatorInterface() {
        StairsCreator creator = new StairsCreator();
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
        Mesh3D mesh0 = new StairsCreator().create();
        Mesh3D mesh1 = new StairsCreator().create();
        Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
        Mesh3D mesh0 = new StairsCreator().create();
        Mesh3D mesh1 = new StairsCreator().create();
        mesh0.vertices.removeAll(mesh1.getVertices());
        Assert.assertEquals(0, mesh0.getVertices().size());
        Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetWidth() {
        float expected = 9.890702959340735E37f;
        StairsCreator creator = new StairsCreator();
        creator.setWidth(expected);
        Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getWidthReturnsDefaultValue() {
        float expected = 4.0f;
        StairsCreator creator = new StairsCreator();
        Assert.assertEquals(expected, creator.getWidth(), 0);
    }

    @Test
    public void getSetStepHeight() {
        float expected = 1.9554757770860212E37f;
        StairsCreator creator = new StairsCreator();
        creator.setStepHeight(expected);
        Assert.assertEquals(expected, creator.getStepHeight(), 0);
    }

    @Test
    public void getStepHeightReturnsDefaultValue() {
        float expected = 0.5f;
        StairsCreator creator = new StairsCreator();
        Assert.assertEquals(expected, creator.getStepHeight(), 0);
    }

    @Test
    public void getSetStepDepth() {
        float expected = 1.2334471421678304E38f;
        StairsCreator creator = new StairsCreator();
        creator.setStepDepth(expected);
        Assert.assertEquals(expected, creator.getStepDepth(), 0);
    }

    @Test
    public void getStepDepthReturnsDefaultValue() {
        float expected = 0.5f;
        StairsCreator creator = new StairsCreator();
        Assert.assertEquals(expected, creator.getStepDepth(), 0);
    }

    @Test
    public void getSetFloating() {
        boolean expected = true;
        StairsCreator creator = new StairsCreator();
        creator.setFloating(expected);
        Assert.assertEquals(expected, creator.isFloating());
    }

    @Test
    public void getFloatingReturnsDefaultValue() {
        boolean expected = false;
        StairsCreator creator = new StairsCreator();
        Assert.assertEquals(expected, creator.isFloating());
    }

    @Test
    public void getSetNumSteps() {
        int expected = 2051499246;
        StairsCreator creator = new StairsCreator();
        creator.setNumSteps(expected);
        Assert.assertEquals(expected, creator.getNumSteps());
    }

    @Test
    public void getNumStepsReturnsDefaultValue() {
        int expected = 20;
        StairsCreator creator = new StairsCreator();
        Assert.assertEquals(expected, creator.getNumSteps());
    }

}
