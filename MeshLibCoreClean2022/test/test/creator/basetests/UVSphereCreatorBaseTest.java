package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.UVSphereCreator;

// Auto-generated test class to execute base tests for mesh creators
public class UVSphereCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new UVSphereCreator().create();
    }

    public void implementsCreatorInterface() {
	UVSphereCreator creator = new UVSphereCreator();
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
	Mesh3D mesh0 = new UVSphereCreator().create();
	Mesh3D mesh1 = new UVSphereCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new UVSphereCreator().create();
	Mesh3D mesh1 = new UVSphereCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetSegments() {
	int expected = 74933293;
	UVSphereCreator creator = new UVSphereCreator();
	creator.setSegments(expected);
	Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSegmentsReturnsDefaultValue() {
	int expected = 32;
	UVSphereCreator creator = new UVSphereCreator();
	Assert.assertEquals(expected, creator.getSegments());
    }

    @Test
    public void getSetRadius() {
	float expected = 4.2164820163741286E37f;
	UVSphereCreator creator = new UVSphereCreator();
	creator.setRadius(expected);
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getRadiusReturnsDefaultValue() {
	float expected = 1.0f;
	UVSphereCreator creator = new UVSphereCreator();
	Assert.assertEquals(expected, creator.getRadius(), 0);
    }

    @Test
    public void getSetRings() {
	int expected = 346141570;
	UVSphereCreator creator = new UVSphereCreator();
	creator.setRings(expected);
	Assert.assertEquals(expected, creator.getRings());
    }

    @Test
    public void getRingsReturnsDefaultValue() {
	int expected = 16;
	UVSphereCreator creator = new UVSphereCreator();
	Assert.assertEquals(expected, creator.getRings());
    }

}
