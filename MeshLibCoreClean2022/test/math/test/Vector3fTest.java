package math.test;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3f;

public class Vector3fTest {
	
	@Test
	public void xIsZeroByDefault() {
		Assert.assertEquals(0, new Vector3f().getX(), 0);
	}
	
	@Test
	public void yIsZeroByDefault() {
		Assert.assertEquals(0, new Vector3f().getY(), 0);
	}
	
	@Test
	public void zIsZeroByDefault() {
		Assert.assertEquals(0, new Vector3f().getZ(), 0);
	}
	
	@Test
	public void addReturnsNotNull() {
		Vector3f a = new Vector3f();
		Assert.assertNotNull(a.add(null));
	}
	
	@Test
	public void addReturnsNewInstanceNotA() {
		Vector3f a = new Vector3f();
		Vector3f b = new Vector3f();
		Vector3f result = a.add(b);
		Assert.assertTrue(a != result);
	}
	
	@Test
	public void addReturnsNewInstanceNotB() {
		Vector3f a = new Vector3f();
		Vector3f b = new Vector3f();
		Vector3f result = a.add(b);
		Assert.assertTrue(b != result);
	}
	
	@Test
	public void addNullGetX() {
		Vector3f a = new Vector3f();
		Vector3f result = a.add(null);
		Assert.assertEquals(a.getX(), result.getX(), 0);
	}
	
	@Test
	public void addNullGetY() {
		Vector3f a = new Vector3f();
		Vector3f result = a.add(null);
		Assert.assertEquals(a.getY(), result.getY(), 0);
	}
	
	@Test
	public void addNullGetZ() {
		Vector3f a = new Vector3f();
		Vector3f result = a.add(null);
		Assert.assertEquals(a.getZ(), result.getZ(), 0);
	}
	
	@Test
	public void getSetRandomX() {
		float random = randomFloat();
		Vector3f v = new Vector3f();
		v.setX(random);
		Assert.assertEquals(random, v.getX(), 0);
	}
	
	@Test
	public void getSetRandomY() {
		float random = randomFloat();
		Vector3f v = new Vector3f();
		v.setY(random);
		Assert.assertEquals(random, v.getY(), 0);
	}
	
	@Test
	public void getSetRandomZ() {
		float random = randomFloat();
		Vector3f v = new Vector3f();
		v.setZ(random);
		Assert.assertEquals(random, v.getZ(), 0);
	}
	
	@Test
	public void addRandomX() {
		float randomX = randomFloat();
		Vector3f a = new Vector3f();
		Vector3f b = new Vector3f();
		b.setX(randomX);
		Vector3f result = a.add(b);
		Assert.assertEquals(randomX, result.getX(), 0);
	}
	
	@Test
	public void addRandomY() {
		float randomY = randomFloat();
		Vector3f a = new Vector3f();
		Vector3f b = new Vector3f();
		b.setY(randomY);
		Vector3f result = a.add(b);
		Assert.assertEquals(randomY, result.getY(), 0);
	}
	
	@Test
	public void addRandomZ() {
		float randomZ = randomFloat();
		Vector3f a = new Vector3f();
		Vector3f b = new Vector3f();
		b.setZ(randomZ);
		Vector3f result = a.add(b);
		Assert.assertEquals(randomZ, result.getZ(), 0);
	}
	
	@Test
	public void addRandomToRandomX() {
		float randomXa = randomFloat();
		float randomXb = randomFloat();
		Vector3f a = new Vector3f();
		a.setX(randomXa);
		Vector3f b = new Vector3f();
		b.setX(randomXb);
		Vector3f result = a.add(b);
		Assert.assertEquals(randomXa + randomXb, result.getX(), 0);
	}
	
	@Test
	public void addRandomToRandomY() {
		float randomYa = randomFloat();
		float randomYb = randomFloat();
		Vector3f a = new Vector3f();
		a.setY(randomYa);
		Vector3f b = new Vector3f();
		b.setY(randomYb);
		Vector3f result = a.add(b);
		Assert.assertEquals(randomYa + randomYb, result.getY(), 0);
	}
	
	@Test
	public void addRandomToRandomZ() {
		float randomZa = randomFloat();
		float randomZb = randomFloat();
		Vector3f a = new Vector3f();
		a.setZ(randomZa);
		Vector3f b = new Vector3f();
		b.setZ(randomZb);
		Vector3f result = a.add(b);
		Assert.assertEquals(randomZa + randomZb, result.getZ(), 0);
	}
	
	@Test
	public void constructWithRandomXGetX() {
		float x = randomFloat();
		Vector3f v = new Vector3f(x, 0, 0);
		Assert.assertEquals(x, v.getX(), 0);
	}
	
	@Test
	public void constructWithRandomYGetY() {
		float y = randomFloat();
		Vector3f v = new Vector3f(0, y, 0);
		Assert.assertEquals(y, v.getY(), 0);
	}
	
	@Test
	public void constructWithRandomZGetZ() {
		float z = randomFloat();
		Vector3f v = new Vector3f(0, 0, z);
		Assert.assertEquals(z, v.getZ(), 0);
	}
	
	private static float randomFloat() {
		return (float) (Math.random() * 100000);
	}

}
