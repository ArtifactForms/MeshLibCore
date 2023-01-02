package mesh.modifier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;

public class TranslateModifierTest {

	private TranslateModifier modifier;
	
	@Before
	public void setUp() {
		modifier = new TranslateModifier();
	}
	
	@Test
	public void implementsModifierInterface() {
		Assert.assertTrue(modifier instanceof IMeshModifier);
	}
	
	@Test
	public void deltaXisZeroByDefault() {
		Assert.assertEquals(0, modifier.getDeltaX(), 0);
	}
	
	@Test
	public void deltaYisZeroByDefault() {
		Assert.assertEquals(0, modifier.getDeltaY(), 0);
	}
	
	@Test
	public void deltaZisZeroByDefault() {
		Assert.assertEquals(0, modifier.getDeltaZ(), 0);
	}
	
	@Test
	public void returnsReferenceToOriginal() {
		Mesh3D mesh = new CubeCreator().create();
		Mesh3D result = modifier.modify(mesh);
		Assert.assertTrue(mesh == result);
	}
	
	@Test
	public void doesNotTranslateMeshByDefault() {
		Mesh3D original = new CubeCreator().create();
		Mesh3D mesh = new CubeCreator().create();
		modifier.modify(mesh);
		for (int i = 0; i < original.getVertexCount(); i++) {
			Vector3f expected = original.getVertexAt(i);
			Vector3f actual = mesh.getVertexAt(i);
			Assert.assertEquals(expected.x, actual.x, 0);
			Assert.assertEquals(expected.y, actual.y, 0);
			Assert.assertEquals(expected.z, actual.z, 0);
		}
	}
	
	@Test
	public void constructWithGivenDeltaX() {
		float deltaX = Mathf.random(-10000f, 10000f);
		modifier = new TranslateModifier(deltaX, 0, 0);
		Assert.assertEquals(deltaX, modifier.getDeltaX(), 0);
	}
	
	@Test
	public void constructWithGivenDeltaY() {
		float deltaY = Mathf.random(-10000f, 10000f);
		modifier = new TranslateModifier(0, deltaY, 0);
		Assert.assertEquals(deltaY, modifier.getDeltaY(), 0);
	}
	
	@Test
	public void constructWithGivenDeltaZ() {
		float deltaZ = Mathf.random(-10000f, 10000f);
		modifier = new TranslateModifier(0, 0, deltaZ);
		Assert.assertEquals(deltaZ, modifier.getDeltaZ(), 0);
	}
	
	@Test
	public void randomGetSetDeltaX() {
		float deltaX = Mathf.random(-10000f, 10000f);
		modifier.setDeltaX(deltaX);
		Assert.assertEquals(deltaX, modifier.getDeltaX(), 0);
	}
	
	@Test
	public void randomGetSetDeltaY() {
		float deltaY = Mathf.random(-10000f, 10000f);
		modifier.setDeltaY(deltaY);
		Assert.assertEquals(deltaY, modifier.getDeltaY(), 0);
	}
	
	@Test
	public void randomGetSetDeltaZ() {
		float deltaZ = Mathf.random(-10000f, 10000f);
		modifier.setDeltaZ(deltaZ);
		Assert.assertEquals(deltaZ, modifier.getDeltaZ(), 0);
	}
	
	@Test
	public void randomTranslateSetDelta() {
		float deltaX = Mathf.random(-10000f, 10000f);
		float deltaY = Mathf.random(-10000f, 10000f);
		float deltaZ = Mathf.random(-10000f, 10000f);
		Mesh3D original = new CubeCreator().create();
		Mesh3D mesh = new CubeCreator().create();
		modifier.setDeltaX(deltaX);
		modifier.setDeltaY(deltaY);
		modifier.setDeltaZ(deltaZ);
		modifier.modify(mesh);
		for (int i = 0; i < original.getVertexCount(); i++) {
			Vector3f expected = original.getVertexAt(i);
			Vector3f actual = mesh.getVertexAt(i);
			Assert.assertEquals(expected.x + deltaX, actual.x, 0);
			Assert.assertEquals(expected.y + deltaY, actual.y, 0);
			Assert.assertEquals(expected.z + deltaZ, actual.z, 0);
		}
	}
	
	@Test
	public void randomTranslateWithDeltaValuesPassedViaConstructor() {
		float deltaX = Mathf.random(-10000f, 10000f);
		float deltaY = Mathf.random(-10000f, 10000f);
		float deltaZ = Mathf.random(-10000f, 10000f);
		Mesh3D original = new CubeCreator().create();
		Mesh3D mesh = new CubeCreator().create();
		modifier = new TranslateModifier(deltaX, deltaY, deltaZ);
		modifier.modify(mesh);
		for (int i = 0; i < original.getVertexCount(); i++) {
			Vector3f expected = original.getVertexAt(i);
			Vector3f actual = mesh.getVertexAt(i);
			Assert.assertEquals(expected.x + deltaX, actual.x, 0);
			Assert.assertEquals(expected.y + deltaY, actual.y, 0);
			Assert.assertEquals(expected.z + deltaZ, actual.z, 0);
		}
	}
	
	@Test
	public void randomTranslateWithDeltaVectorPassedViaConstructor() {
		float deltaX = Mathf.random(-10000f, 10000f);
		float deltaY = Mathf.random(-10000f, 10000f);
		float deltaZ = Mathf.random(-10000f, 10000f);
		Vector3f delta = new Vector3f(deltaX, deltaY, deltaZ);
		Mesh3D original = new CubeCreator().create();
		Mesh3D mesh = new CubeCreator().create();
		modifier = new TranslateModifier(delta);
		modifier.modify(mesh);
		for (int i = 0; i < original.getVertexCount(); i++) {
			Vector3f expected = original.getVertexAt(i);
			Vector3f actual = mesh.getVertexAt(i);
			Assert.assertEquals(expected.x + deltaX, actual.x, 0);
			Assert.assertEquals(expected.y + deltaY, actual.y, 0);
			Assert.assertEquals(expected.z + deltaZ, actual.z, 0);
		}
	}
	
	@Test
	public void deltaVectorIsNotStoredInternally() {
		float deltaX = Mathf.random(-10000f, 10000f);
		float deltaY = Mathf.random(-10000f, 10000f);
		float deltaZ = Mathf.random(-10000f, 10000f);
		Vector3f delta = new Vector3f(deltaX, deltaY, deltaZ);
		TranslateModifier modifier = new TranslateModifier(delta);
		Mesh3D original = new CubeCreator().create();
		Mesh3D mesh = new CubeCreator().create();
		delta.set(10, 20, 30);
		modifier.modify(mesh);
		for (int i = 0; i < original.getVertexCount(); i++) {
			Vector3f expected = original.getVertexAt(i);
			Vector3f actual = mesh.getVertexAt(i);
			Assert.assertEquals(expected.x + deltaX, actual.x, 0);
			Assert.assertEquals(expected.y + deltaY, actual.y, 0);
			Assert.assertEquals(expected.z + deltaZ, actual.z, 0);
		}
	}
	
}
