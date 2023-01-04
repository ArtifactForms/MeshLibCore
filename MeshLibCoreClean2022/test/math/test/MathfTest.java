package math.test;

import org.junit.Assert;
import org.junit.Test;

import math.Mathf;

public class MathfTest {

	@Test
	public void tribonacciConstant() {
		Assert.assertEquals(1.83928675521416f, Mathf.TRIBONACCI_CONSTANT, 0);
	}

	@Test
	public void goldenRatioConstant() {
		Assert.assertEquals(1.618033988749f, Mathf.GOLDEN_RATIO, 0);
	}
	
	@Test
	public void constantPiEqualsMathConstant() {
		Assert.assertEquals((float) Math.PI, Mathf.PI, 0);
	}

	@Test
	public void constantPiEqualsValue() {
		Assert.assertEquals(3.1415927f, Mathf.PI, 0);
	}
	
	@Test
	public void constantHalfPiEqualsToMath() {
		Assert.assertEquals((float) Math.PI * 0.5f, Mathf.HALF_PI, 0);
	}
	
	@Test
	public void constantHalfPiEqualsToValue() {
		Assert.assertEquals(1.5707964f, Mathf.HALF_PI, 0);
	}
	
	@Test
	public void constantTwoPiEqualsMath() {
		Assert.assertEquals((float) (Math.PI + Math.PI), Mathf.TWO_PI, 0);
	}
	
	@Test
	public void constantTwoPiEqualsValue() {
		Assert.assertEquals(6.2831855f, Mathf.TWO_PI, 0);
	}
	
	@Test
	public void constantOneThird() {
		Assert.assertEquals(1f / 3f, Mathf.ONE_THIRD, 0);
	}
	
	@Test
	public void contantQuarterPiEqualsToValue() {
		Assert.assertEquals(0.7853982f, Mathf.QUARTER_PI,  0);
	}
	
	@Test
	public void constantZeroToleranceEqualsToValue() {
		Assert.assertEquals(0.00001f, Mathf.ZERO_TOLERANCE, 0);
	}
	
	@Test
	public void constantQuarterEqualsToMath() {
		Assert.assertEquals((float) (Math.PI / 4.0), Mathf.QUARTER_PI, 0);
	}
	
	@Test
	public void clampBetweenZeroAndOneUnchaged() {
		float expected = 0.5f;
		float actual = Mathf.clamp(0, 1, 0.5f);
		Assert.assertEquals(expected, actual, 0);
	}
	
	@Test
	public void clampBetweenZeroAndOneGreaterOne() {
		float expected = 1.0f;
		float actual = Mathf.clamp(0, 1, 1.5f);
		Assert.assertEquals(expected, actual, 0);
	}
	
	@Test
	public void clampBetweenZeroAndMaxRandom() {
		float max = (float) (Math.random() * 1000);
		float random = max + (float) (Math.random() * 10000f);
		float expected = max;
		float actual = Mathf.clamp(0, max, random);
		Assert.assertEquals(expected, actual, 0);
	}
	
	@Test
	public void clampBetweenRandomMinAndMax() {
		float min = (float) (Math.random() * 1000);
		float random = min - (float) (Math.random() * 10000f);
		float expected = min;
		float actual = Mathf.clamp(min, 2000, random);
		Assert.assertEquals(expected, actual, 0);
	}
	
	@Test
	public void clampRandomUnchanged() {
		float expected = (float) (Math.random() * 1000);
		float actual = Mathf.clamp(expected - 1000, expected + 1000, expected);
		Assert.assertEquals(expected, actual, 0);
	}
	
	@Test
	public void randomCos() {
		float random = -5000 + (float) (Math.random() * 10000);
		float expected = (float) Math.cos(random);
		Assert.assertEquals(expected, Mathf.cos(random), 0);
	}
	
	@Test
	public void randomAtan() {
		float random = -5000 + (float) (Math.random() * 10000);
		float expected = (float) Math.atan(random);
		Assert.assertEquals(expected, Mathf.atan(random), 0);
	}
	
	@Test
	public void randomSin() {
		float random = -5000 + (float) (Math.random() * 10000);
		float expected = (float) Math.sin(random);
		Assert.assertEquals(expected, Mathf.sin(random), 0);
	}
	
	@Test
	public void randomSqrt() {
		float random = -5000 + (float) (Math.random() * 10000);
		float expected = (float) Math.sqrt(random);
		Assert.assertEquals(expected, Mathf.sqrt(random), 0);
	}
	
	@Test
	public void randomRound() {
		float random = -5000 + (float) (Math.random() * 10000);
		float expected = (float) Math.round(random);
		Assert.assertEquals(expected, Mathf.round(random), 0);
	}
	
	@Test
	public void randomAbs() {
		float random = -5000 + (float) (Math.random() * 10000);
		float expected = (float) Math.abs(random);
		Assert.assertEquals(expected, Mathf.abs(random), 0);
	}
	
}
