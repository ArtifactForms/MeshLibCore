package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.DiscCreator;

// Auto-generated test class to execute base tests for mesh creators
public class DiscCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new DiscCreator().create();
    }

    public void implementsCreatorInterface() {
	DiscCreator creator = new DiscCreator();
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
	Mesh3D mesh0 = new DiscCreator().create();
	Mesh3D mesh1 = new DiscCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new DiscCreator().create();
	Mesh3D mesh1 = new DiscCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetRotationSegments() {
	int expected = 1016999211;
	DiscCreator creator = new DiscCreator();
	creator.setRotationSegments(expected);
	Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getRotationSegmentsReturnsDefaultValue() {
	int expected = 32;
	DiscCreator creator = new DiscCreator();
	Assert.assertEquals(expected, creator.getRotationSegments());
    }

    @Test
    public void getSetInnerRadius() {
	float expected = 1.0733576989060986E38f;
	DiscCreator creator = new DiscCreator();
	creator.setInnerRadius(expected);
	Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getInnerRadiusReturnsDefaultValue() {
	float expected = 0.5f;
	DiscCreator creator = new DiscCreator();
	Assert.assertEquals(expected, creator.getInnerRadius(), 0);
    }

    @Test
    public void getSetOuterRadius() {
	float expected = 1.4161152011724493E38f;
	DiscCreator creator = new DiscCreator();
	creator.setOuterRadius(expected);
	Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

    @Test
    public void getOuterRadiusReturnsDefaultValue() {
	float expected = 1.0f;
	DiscCreator creator = new DiscCreator();
	Assert.assertEquals(expected, creator.getOuterRadius(), 0);
    }

    @Test
    public void getSetDiscSegments() {
	int expected = 1667892525;
	DiscCreator creator = new DiscCreator();
	creator.setDiscSegments(expected);
	Assert.assertEquals(expected, creator.getDiscSegments());
    }

    @Test
    public void getDiscSegmentsReturnsDefaultValue() {
	int expected = 1;
	DiscCreator creator = new DiscCreator();
	Assert.assertEquals(expected, creator.getDiscSegments());
    }

}
