package mesh.modifier.subdivision.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.archimedian.SnubCubeCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.subdivision.QuadsToTrianglesModifier;
import util.MeshTest;

public class QuadsToTrianglesTest {

	private QuadsToTrianglesModifier modifier;
	
	@Before
	public void setUp() {
		modifier = new QuadsToTrianglesModifier();
	}
	
	@Test
	public void implementsModifierInterface() {
		Assert.assertTrue(modifier instanceof IMeshModifier);
	}
	
	@Test
	public void returnsMeshReference() {
		Mesh3D mesh = new Mesh3D();
		Mesh3D result = modifier.modify(mesh);
		Assert.assertTrue(mesh == result);
	}
			
	@Test
	public void subdividedPlaneHasTwoFaces() {
		Mesh3D mesh = new PlaneCreator().create();
		modifier.modify(mesh);
		Assert.assertEquals(2, mesh.getFaceCount());
	}
	
	@Test
	public void subdividedPlaneHasTwoTriangularFaces() {
		Mesh3D mesh = new PlaneCreator().create();
		modifier.modify(mesh);
		MeshTest.assertTriangleCountEquals(mesh, 2);
	}
	
	@Test
	public void subdividedCubeHasTwelveTriangularFaces() {
		Mesh3D mesh = new CubeCreator().create();
		modifier.modify(mesh);
		MeshTest.assertTriangleCountEquals(mesh, 12);
	}
	
	@Test
	public void subdividedCubeHasNoLooseVertices() {
		Mesh3D mesh = new CubeCreator().create();
		modifier.modify(mesh);
		MeshTest.assertMeshHasNoLooseVertices(mesh);
	}
	
	@Test
	public void subdivideIcosahedronVertices() {
		Mesh3D expected = new IcosahedronCreator().create();
		Mesh3D actual = new IcosahedronCreator().create();
		modifier.modify(actual);
		for (int i = 0; i < expected.getVertexCount(); i++) {
			Vector3f expectedVertex = expected.getVertexAt(i);
			Vector3f actualVertex = actual.getVertexAt(i);
			Assert.assertEquals(expectedVertex, actualVertex);
		}
	}
	
	@Test
	public void subdivideIcosahedronFaces() {
		Mesh3D expected = new IcosahedronCreator().create();
		Mesh3D actual = new IcosahedronCreator().create();
		modifier.modify(actual);
		for (int i = 0; i < expected.getFaceCount(); i++) {
			int[] expectedIndices = expected.getFaceAt(i).indices;
			int[] actualIndices = actual.getFaceAt(i).indices;
			Assert.assertArrayEquals(expectedIndices, actualIndices);
		}
	}
	
	@Test
	public void subdividedSnubCubeHasNoLooseVertices() {
		Mesh3D mesh = new SnubCubeCreator().create();
		modifier.modify(mesh);
		MeshTest.assertMeshHasNoLooseVertices(mesh);
	}
	
	@Test
	public void subdividedSnubCubeCompletelyConsistsOfTriangles() {
		Mesh3D mesh = new SnubCubeCreator().create();
		int expected = 44;
		modifier.modify(mesh);
		MeshTest.assertTriangleCountEquals(mesh, expected);
	}
	
}
