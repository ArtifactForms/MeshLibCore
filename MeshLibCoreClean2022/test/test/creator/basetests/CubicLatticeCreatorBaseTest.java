package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.creative.CubicLatticeCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CubicLatticeCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new CubicLatticeCreator().create();
    }

    public void implementsCreatorInterface() {
	CubicLatticeCreator creator = new CubicLatticeCreator();
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
	Mesh3D mesh0 = new CubicLatticeCreator().create();
	Mesh3D mesh1 = new CubicLatticeCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new CubicLatticeCreator().create();
	Mesh3D mesh1 = new CubicLatticeCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetSubdivisions() {
	int expected = 168256328;
	CubicLatticeCreator creator = new CubicLatticeCreator();
	creator.setSubdivisions(expected);
	Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSubdivisionsReturnsDefaultValue() {
	int expected = 1;
	CubicLatticeCreator creator = new CubicLatticeCreator();
	Assert.assertEquals(expected, creator.getSubdivisions());
    }

    @Test
    public void getSetSegmentsY() {
	int expected = 111189102;
	CubicLatticeCreator creator = new CubicLatticeCreator();
	creator.setSegmentsY(expected);
	Assert.assertEquals(expected, creator.getSegmentsY());
    }

    @Test
    public void getSegmentsYReturnsDefaultValue() {
	int expected = 3;
	CubicLatticeCreator creator = new CubicLatticeCreator();
	Assert.assertEquals(expected, creator.getSegmentsY());
    }

    @Test
    public void getSetSegmentsZ() {
	int expected = 309034666;
	CubicLatticeCreator creator = new CubicLatticeCreator();
	creator.setSegmentsZ(expected);
	Assert.assertEquals(expected, creator.getSegmentsZ());
    }

    @Test
    public void getSegmentsZReturnsDefaultValue() {
	int expected = 3;
	CubicLatticeCreator creator = new CubicLatticeCreator();
	Assert.assertEquals(expected, creator.getSegmentsZ());
    }

    @Test
    public void getSetSegmentsX() {
	int expected = 2014656684;
	CubicLatticeCreator creator = new CubicLatticeCreator();
	creator.setSegmentsX(expected);
	Assert.assertEquals(expected, creator.getSegmentsX());
    }

    @Test
    public void getSegmentsXReturnsDefaultValue() {
	int expected = 3;
	CubicLatticeCreator creator = new CubicLatticeCreator();
	Assert.assertEquals(expected, creator.getSegmentsX());
    }

}
