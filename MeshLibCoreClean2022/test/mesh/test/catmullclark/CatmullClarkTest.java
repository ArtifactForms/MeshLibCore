package mesh.test.catmullclark;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.platonic.DodecahedronCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.io.SimpleObjectReader;
import mesh.modifier.subdivision.CatmullClarkModifier;
import util.MeshTest;

public class CatmullClarkTest {

	private static final int ITERATIONS_TO_TEST = 8;

	private Mesh3D loadReference(int iteration) {
		String file = "Catmull_Clark_Reference_Level_" + iteration + ".obj";
		file = CatmullClarkTest.class.getResource(file).getPath();
		Mesh3D expectedMesh = new SimpleObjectReader().read(new File(file));
		return expectedMesh;
	}

	private void facesAreEqual(Mesh3D expected, Mesh3D actual) {
		for (int j = 0; j < expected.getFaceCount(); j++) {
			Face3D expectedFace = expected.getFaceAt(j);
			Face3D actualFace = actual.getFaceAt(j);
			for (int index = 0; index < expectedFace.indices.length; index++) {
				Assert.assertEquals(expectedFace.indices[index], actualFace.indices[index]);
			}
		}
	}

	private void verticesAreEqual(Mesh3D expected, Mesh3D actual) {
		for (int j = 0; j < expected.getVertexCount(); j++) {
			Vector3f expectedVertex = expected.getVertexAt(j);
			Vector3f actualVertex = actual.getVertexAt(j);
			Assert.assertEquals(expectedVertex.getX(), actualVertex.getX(), 0.000001f);
			Assert.assertEquals(expectedVertex.getY(), actualVertex.getY(), 0.000001f);
			Assert.assertEquals(expectedVertex.getZ(), actualVertex.getZ(), 0.000001f);
		}
	}

	@Test
	public void subdivideOtherShapeInAdvanceTestFaces() {
		Mesh3D advance = new DodecahedronCreator().create();
		Mesh3D expected = loadReference(4);
		Mesh3D actual = new CubeCreator().create();
		CatmullClarkModifier modifier = new CatmullClarkModifier(4);
		modifier.modify(advance);
		modifier.modify(actual);
		facesAreEqual(expected, actual);
	}

	@Test
	public void subdivideOtherShapeInAdvanceTestVertices() {
		Mesh3D advance = new DodecahedronCreator().create();
		Mesh3D expected = loadReference(4);
		Mesh3D actual = new CubeCreator().create();
		CatmullClarkModifier modifier = new CatmullClarkModifier(4);
		modifier.modify(advance);
		modifier.modify(actual);
		verticesAreEqual(expected, actual);
	}

	@Test
	public void setIterationCountToSpecifiedValueTestFaces() {
		int iterations = 4;
		Mesh3D expected = loadReference(iterations);
		Mesh3D actual = new CubeCreator().create();
		CatmullClarkModifier modifier = new CatmullClarkModifier();
		modifier.setSubdivisions(iterations);
		modifier.modify(actual);
		facesAreEqual(expected, actual);
	}

	@Test
	public void setIterationCountToSpecifiedValueTestVertices() {
		int iterations = 5;
		Mesh3D expected = loadReference(iterations);
		Mesh3D actual = new CubeCreator().create();
		CatmullClarkModifier modifier = new CatmullClarkModifier();
		modifier.setSubdivisions(iterations);
		modifier.modify(actual);
		verticesAreEqual(expected, actual);
	}

	@Test
	public void defaultSettingsOneIterationTestFaces() {
		Mesh3D expected = loadReference(1);
		Mesh3D actual = new CubeCreator().create();
		new CatmullClarkModifier().modify(actual);
		facesAreEqual(expected, actual);
	}

	@Test
	public void defaultSettingsOneIterationTestVertices() {
		Mesh3D expected = loadReference(1);
		Mesh3D actual = new CubeCreator().create();
		new CatmullClarkModifier().modify(actual);
		verticesAreEqual(expected, actual);
	}

	@Test
	public void createdVerticesAreEqualToThoseOfReferenceFile() {
		for (int i = 0; i < ITERATIONS_TO_TEST; i++) {
			Mesh3D expected = loadReference(i);
			Mesh3D actual = new CubeCreator().create();
			new CatmullClarkModifier(i).modify(actual);
			verticesAreEqual(expected, actual);
		}
	}

	@Test
	public void createdFacesAreEqualToThoseOfReferenceFile() {
		for (int i = 0; i < ITERATIONS_TO_TEST; i++) {
			Mesh3D expected = loadReference(i);
			Mesh3D actual = new CubeCreator().create();
			new CatmullClarkModifier(i).modify(actual);
			facesAreEqual(expected, actual);
		}
	}

	@Test
	public void getSetSubdivisions() {
		int iterations = (int) (Math.random() * Integer.MAX_VALUE);
		CatmullClarkModifier modifier = new CatmullClarkModifier();
		modifier.setSubdivisions(iterations);
		Assert.assertEquals(iterations, modifier.getSubdivisions());
	}

	@Test
	public void cubeStaysManifold() {
		Mesh3D cube = new CubeCreator().create();
		for (int i = 0; i < ITERATIONS_TO_TEST; i++) {
			new CatmullClarkModifier().modify(cube);
			MeshTest.assertIsManifold(cube);
		}
	}

	@Test
	public void resultConsistsOfQuadsOnly() {
		Mesh3D cube = new CubeCreator().create();
		int faceCount = cube.getFaceCount();
		MeshTest.assertQuadCountEquals(cube, faceCount);
	}

}
