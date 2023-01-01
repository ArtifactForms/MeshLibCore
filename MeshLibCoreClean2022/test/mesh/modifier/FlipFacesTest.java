package mesh.modifier;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CapsuleCreator;
import mesh.creator.primitives.IcoSphereCreator;

public class FlipFacesTest {
	
	@Test
	public void keepsFaceCount() {
		Mesh3D originalMesh = new CapsuleCreator().create();
		Mesh3D flippedMesh = new CapsuleCreator().create();
		new FlipFacesModifier().modify(flippedMesh);
		int expected = originalMesh.getFaceCount();
		int actual = flippedMesh.getFaceCount();
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void keepsIndicesLength() {
		Mesh3D originalMesh = new CapsuleCreator().create();
		Mesh3D flippedMesh = new CapsuleCreator().create();
		new FlipFacesModifier().modify(flippedMesh);
		for (int i = 0; i < originalMesh.getFaceCount(); i++) {
			Face3D originalFace = originalMesh.getFaceAt(i);
			Face3D flippedFace = flippedMesh.getFaceAt(i);
			int expected = originalFace.indices.length;
			int actual = flippedFace.indices.length;
			Assert.assertEquals(expected, actual);
		}
	}
	
	@Test
	public void reversesIndices() {
		Mesh3D originalMesh = new CapsuleCreator().create();
		Mesh3D flippedMesh = new CapsuleCreator().create();
		new FlipFacesModifier().modify(flippedMesh);
		for (int i = 0; i < originalMesh.getFaceCount(); i++) {
			Face3D originalFace = originalMesh.getFaceAt(i);
			Face3D flippedFace = flippedMesh.getFaceAt(i);
			for (int j = 0; j < originalFace.indices.length; j++) {
				int indicesLength = originalFace.indices.length;
				int expected = originalFace.indices[j];
				int actual = flippedFace.indices[indicesLength - j - 1];
				Assert.assertEquals(expected, actual);
			}
		}
	}
	
	@Test
	public void keepsOriginalFaces() {
		Mesh3D originalMesh = new IcoSphereCreator().create();
		List<Face3D> faces = originalMesh.getFaces();
		new FlipFacesModifier().modify(originalMesh);
		for (int i = 0; i < faces.size(); i++) {
			Face3D expected = faces.get(i);
			Face3D actual = originalMesh.getFaceAt(i);
			Assert.assertTrue(expected == actual);
		}
	}

}
