package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.wip.TerrainCreator;

// Auto-generated test class to execute base tests for mesh creators
public class TerrainCreatorBaseTest {

    private Mesh3D mesh;

    @Before
    public void setUp() {
	mesh = new TerrainCreator().create();
    }

    public void implementsCreatorInterface() {
	TerrainCreator creator = new TerrainCreator();
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
	Mesh3D mesh0 = new TerrainCreator().create();
	Mesh3D mesh1 = new TerrainCreator().create();
	Assert.assertTrue(mesh0 != mesh1);
    }

    @Test
    public void creationOfVerticesIsConsistentIfNotChangingParameters() {
	Mesh3D mesh0 = new TerrainCreator().create();
	Mesh3D mesh1 = new TerrainCreator().create();
	mesh0.vertices.removeAll(mesh1.getVertices());
	Assert.assertEquals(0, mesh0.getVertices().size());
	Assert.assertEquals(0, mesh0.getVertexCount());
    }

    @Test
    public void getSetNoiseY() {
	float expected = 7.116276738582379E37f;
	TerrainCreator creator = new TerrainCreator();
	creator.setNoiseY(expected);
	Assert.assertEquals(expected, creator.getNoiseY(), 0);
    }

    @Test
    public void getSetHeightFactor() {
	float expected = 2.6108353930495772E38f;
	TerrainCreator creator = new TerrainCreator();
	creator.setHeightFactor(expected);
	Assert.assertEquals(expected, creator.getHeightFactor(), 0);
    }

    @Test
    public void getHeightFactorReturnsDefaultValue() {
	float expected = 1.0f;
	TerrainCreator creator = new TerrainCreator();
	Assert.assertEquals(expected, creator.getHeightFactor(), 0);
    }

    @Test
    public void getSetNoiseZ() {
	float expected = 1.8474282855944766E37f;
	TerrainCreator creator = new TerrainCreator();
	creator.setNoiseZ(expected);
	Assert.assertEquals(expected, creator.getNoiseZ(), 0);
    }

    @Test
    public void getSetSubdivisionsX() {
	int expected = 924882619;
	TerrainCreator creator = new TerrainCreator();
	creator.setSubdivisionsX(expected);
	Assert.assertEquals(expected, creator.getSubdivisionsX());
    }

    @Test
    public void getSubdivisionsXReturnsDefaultValue() {
	int expected = 10;
	TerrainCreator creator = new TerrainCreator();
	Assert.assertEquals(expected, creator.getSubdivisionsX());
    }

    @Test
    public void getSetSubdivisionsZ() {
	int expected = 1725373031;
	TerrainCreator creator = new TerrainCreator();
	creator.setSubdivisionsZ(expected);
	Assert.assertEquals(expected, creator.getSubdivisionsZ());
    }

    @Test
    public void getSubdivisionsZReturnsDefaultValue() {
	int expected = 10;
	TerrainCreator creator = new TerrainCreator();
	Assert.assertEquals(expected, creator.getSubdivisionsZ());
    }

    @Test
    public void getSetTileSizeZ() {
	float expected = 2.899662723386238E38f;
	TerrainCreator creator = new TerrainCreator();
	creator.setTileSizeZ(expected);
	Assert.assertEquals(expected, creator.getTileSizeZ(), 0);
    }

    @Test
    public void getTileSizeZReturnsDefaultValue() {
	float expected = 0.1f;
	TerrainCreator creator = new TerrainCreator();
	Assert.assertEquals(expected, creator.getTileSizeZ(), 0);
    }

    @Test
    public void getSetTileSizeX() {
	float expected = 1.2207449363505362E38f;
	TerrainCreator creator = new TerrainCreator();
	creator.setTileSizeX(expected);
	Assert.assertEquals(expected, creator.getTileSizeX(), 0);
    }

    @Test
    public void getTileSizeXReturnsDefaultValue() {
	float expected = 0.1f;
	TerrainCreator creator = new TerrainCreator();
	Assert.assertEquals(expected, creator.getTileSizeX(), 0);
    }

}
