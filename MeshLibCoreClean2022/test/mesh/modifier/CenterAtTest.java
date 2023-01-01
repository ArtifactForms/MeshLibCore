package mesh.modifier;

import org.junit.Assert;
import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;

public class CenterAtTest {

	@Test
	public void modifierImplementsModifierInterface() {
		CenterAtModifier modifier = new CenterAtModifier();
		Assert.assertTrue(modifier instanceof IMeshModifier);
	}
	
	@Test
	public void getCenterIsNotNullByDefault() {
		Assert.assertNotNull(new CenterAtModifier().getCenter());
	}
	
	@Test
	public void centerIsOriginByDefault() {
		Assert.assertEquals(new Vector3f(), new CenterAtModifier().getCenter());
	}
	
	@Test
	public void setCenterToNewValue() {
		Vector3f expected = new Vector3f(0.1f, 0.5f, 0.3f);
		CenterAtModifier modifier = new CenterAtModifier();
		modifier.setCenter(expected);
		Assert.assertEquals(expected, modifier.getCenter());
	}
	
	@Test
	public void setRandomCenter() {
		float x = Mathf.random(-100f, 100f);
		float y = Mathf.random(-200f, 200f);
		float z = Mathf.random(-50f, 200f);
		Vector3f expected = new Vector3f(x, y, z);
		CenterAtModifier modifier = new CenterAtModifier();
		modifier.setCenter(expected);
		Assert.assertEquals(expected, modifier.getCenter());
	}
	
	@Test
	public void centerTranslatedCubeAtOrigin() {
		Mesh3D cube = new CubeCreator().create();
		Mesh3D centerCube = new CubeCreator().create();
		cube.translate(1, 2, 3);
		new CenterAtModifier().modify(cube);
		for (int i = 0; i < cube.getVertexCount(); i++) {
			Vector3f expected = centerCube.getVertexAt(i);
			Vector3f actual = cube.getVertexAt(i);
			Assert.assertEquals(expected, actual);
		}
	}
	
	@Test
	public void centerRandomlyTranslatedCubeAtOrigin() {
		float tX = Mathf.random(-100f, 100f);
		float tY = Mathf.random(-200f, 200f);
		float tZ = Mathf.random(-50f, 200f);
		Mesh3D cube = new CubeCreator().create();
		Mesh3D centerCube = new CubeCreator().create();
		cube.translate(tX, tY, tZ);
		new CenterAtModifier().modify(cube);
		for (int i = 0; i < cube.getVertexCount(); i++) {
			Vector3f expected = centerCube.getVertexAt(i);
			Vector3f actual = cube.getVertexAt(i);
			Assert.assertEquals(expected, actual);
		}
	}
	
	@Test
	public void returnsOriginalMesh() {
		Mesh3D mesh = new CubeCreator().create();
		CenterAtModifier modifier = new CenterAtModifier();
		Mesh3D result = modifier.modify(mesh);
		Assert.assertTrue(mesh == result);
	}
	
	@Test
	public void alreadyCenteredCube() {
		Mesh3D cube = new CubeCreator().create();
		Mesh3D centerCube = new CubeCreator().create();
		new CenterAtModifier().modify(cube);
		for (int i = 0; i < cube.getVertexCount(); i++) {
			Vector3f expected = centerCube.getVertexAt(i);
			Vector3f actual = cube.getVertexAt(i);
			Assert.assertEquals(expected, actual);
		}
	}
	
	@Test
	public void getReturnsCenterItIsConstrucedWith() {
		float x = Mathf.random(-100f, 100f);
		float y = Mathf.random(-200f, 200f);
		float z = Mathf.random(-50f, 200f);
		CenterAtModifier modifier = new CenterAtModifier(new Vector3f(x, y, z));
		Assert.assertEquals(new Vector3f(x, y, z), modifier.getCenter());
	}
	
	@Test
	public void constructorDoesNotCopyCenter() {
		float x = Mathf.random(-2, 100f);
		float y = Mathf.random(-567, 20f);
		float z = Mathf.random(-52f, 220f);
		Vector3f center = new Vector3f(x, y, z);
		CenterAtModifier modifier = new CenterAtModifier(center);
		Assert.assertTrue(center == modifier.getCenter());
	}
	
}
