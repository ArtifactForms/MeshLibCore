package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.unsorted.TriangleSegmentCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TriangleSegmentCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new TriangleSegmentCreator().create();
    }

    public void implementsCreatorInterface() {
	TriangleSegmentCreator creator = new TriangleSegmentCreator();
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
	Mesh3D mesh0 = new TriangleSegmentCreator().create();
	Mesh3D mesh1 = new TriangleSegmentCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new TriangleSegmentCreator().create();
	Mesh3D mesh1 = new TriangleSegmentCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetHeight() {
	float expected = 1.7460424589836908E38f;
	TriangleSegmentCreator creator = new TriangleSegmentCreator();
	creator.setHeight(expected);
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getHeightReturnsDefaultValue() {
	float expected = 0.125f;
	TriangleSegmentCreator creator = new TriangleSegmentCreator();
	Assert.assertEquals(expected, creator.getHeight(), 0);
    }

    @Test
    public void getSetSize() {
	float expected = 1.7473925444666368E38f;
	TriangleSegmentCreator creator = new TriangleSegmentCreator();
	creator.setSize(expected);
	Assert.assertEquals(expected, creator.getSize(), 0);
    }

    @Test
    public void getSizeReturnsDefaultValue() {
	float expected = 1.0f;
	TriangleSegmentCreator creator = new TriangleSegmentCreator();
	Assert.assertEquals(expected, creator.getSize(), 0);
    }

}
