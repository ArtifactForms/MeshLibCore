package mesh.util.test;

import org.junit.Assert;
import org.junit.Test;

import mesh.util.Geometry;

public class GeometryTest {
	
	@Test
	public void tribonacciConstant() {
		Assert.assertEquals(1.83928675521416f, Geometry.TRIBONACCI_CONSTANT, 0);
	}
	
	@Test
	public void goldenRatioConstant() {
		Assert.assertEquals(1.618033988749f, Geometry.GOLDEN_RATIO, 0);
	}

}
