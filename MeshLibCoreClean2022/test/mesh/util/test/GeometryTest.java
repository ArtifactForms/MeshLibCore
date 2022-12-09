package mesh.util.test;

import org.junit.Assert;
import org.junit.Test;

import mesh.util.Geometry;

public class GeometryTest {
	
	@Test
	public void tribonacciConstant() {
		Assert.assertEquals(1.83928675521416f, Geometry.TRIBONACCI_CONSTANT, 0);
	}

}
