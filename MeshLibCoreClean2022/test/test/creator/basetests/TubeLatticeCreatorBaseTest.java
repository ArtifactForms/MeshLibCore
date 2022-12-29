package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.TubeLatticeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TubeLatticeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new TubeLatticeCreator().create();
    }

    public void implementsCreatorInterface() {
	TubeLatticeCreator creator = new TubeLatticeCreator();
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
	Mesh3D mesh0 = new TubeLatticeCreator().create();
	Mesh3D mesh1 = new TubeLatticeCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void getSetSegments() {
	int expected = 1760518728;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	creator.setSegments(expected);
	Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSegmentsReturnsDefaultValue() {
	int expected = 2;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSetVertices() {
	int expected = 730734533;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	creator.setVertices(expected);
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getVerticesReturnsDefaultValue() {
	int expected = 16;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	Assert.assertEquals(expected, creator.getVertices());
    }

    @Test
    public void getSetHeight() {
	float expected = 2.3229067130911057E38f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	creator.setHeight(expected);
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
	float expected = 2.0f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetInnerRadius() {
	float expected = 1.3752775658222655E38f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	creator.setInnerRadius(expected);
	Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getInnerRadiusReturnsDefaultValue() {
	float expected = 0.9f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getSetOuterRadius() {
	float expected = 3.2025684614770358E38f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	creator.setOuterRadius(expected);
	Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

    @Test
    public void getOuterRadiusReturnsDefaultValue() {
	float expected = 1.0f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

    @Test
    public void getSetScaleExtrude() {
	float expected = 1.3098390073119667E38f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	creator.setScaleExtrude(expected);
	Assert.assertEquals(expected, creator.getScaleExtrude(), 0);
    }

    @Test
    public void getScaleExtrudeReturnsDefaultValue() {
	float expected = 0.5f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	Assert.assertEquals(expected, creator.getScaleExtrude(), 0);
    }

    @Test
    public void getSetThickness() {
	float expected = 2.9698238903536038E38f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	creator.setThickness(expected);
	Assert.assertEquals(expected, creator.getThickness(), 0);
    }

    @Test
    public void getThicknessReturnsDefaultValue() {
	float expected = 0.1f;
	TubeLatticeCreator creator = new TubeLatticeCreator();
	Assert.assertEquals(expected, creator.getThickness(), 0);
    }

}
