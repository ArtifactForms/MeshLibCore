package mesh.test.catmullclark;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.io.SimpleObjectReader;
import mesh.modifier.subdivision.CatmullClarkModifier;

public class CatmullClarkTest {
	
	private static final int ITERATIONS_TO_TEST = 9;
	
	private Mesh3D loadReference(int iteration) {
		String file = "Catmull_Clark_Reference_Level_" + iteration + ".obj";
		file = CatmullClarkTest.class.getResource(file).getPath();
		Mesh3D expectedMesh = new SimpleObjectReader().read(new File(file));
		return expectedMesh;
	}
	
	@Test
	public void createdVerticesAreEqualToThoseOfReferenceFile() {
		for (int i = 0; i < ITERATIONS_TO_TEST; i++) {
			Mesh3D expectedMesh = loadReference(i);
			Mesh3D actualMesh = new CubeCreator().create();
			new CatmullClarkModifier(i).modify(actualMesh);
			for (int j = 0; j < expectedMesh.getVertexCount(); j++) {
				Vector3f expectedVertex = expectedMesh.getVertexAt(j);
				Vector3f toCompare = actualMesh.getVertexAt(j);
				Assert.assertEquals(expectedVertex, toCompare);
			}
		}
	}
	
	@Test
	public void createdFacesAreEqualToThoseOfReferenceFile() {
		for (int i = 0; i < ITERATIONS_TO_TEST; i++) {
			Mesh3D expectedMesh = loadReference(i);
			Mesh3D actualMesh = new CubeCreator().create();
			new CatmullClarkModifier(i).modify(actualMesh);
			for (int j = 0; j < expectedMesh.getFaceCount(); j++) {
				Face3D expectedFace = expectedMesh.getFaceAt(j);
				Face3D actualFace = actualMesh.getFaceAt(j);
				for (int index = 0; index < expectedFace.indices.length; index++) {
					Assert.assertEquals(expectedFace.indices[index], actualFace.indices[index]);
				}
			}
		}
	}

}
