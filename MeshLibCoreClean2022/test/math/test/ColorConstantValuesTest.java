package math.test;

import org.junit.Assert;
import org.junit.Test;

import math.Color;

public class ColorConstantValuesTest {
	
	@Test
	public void constantBlack() {
		Assert.assertEquals(0, Color.BLACK.getRed(),  0);
		Assert.assertEquals(0, Color.BLACK.getGreen(), 0);
		Assert.assertEquals(0, Color.BLACK.getBlue(), 0);
		Assert.assertEquals(1, Color.BLACK.getAlpha(), 0);
	}
	
	@Test
	public void constantBlue() {
		Assert.assertEquals(0, Color.BLUE.getRed(),  0);
		Assert.assertEquals(0, Color.BLUE.getGreen(), 0);
		Assert.assertEquals(1, Color.BLUE.getBlue(), 0);
		Assert.assertEquals(1, Color.BLUE.getAlpha(), 0);
	}
	
	@Test
	public void constantClear() {
		Assert.assertEquals(0, Color.CLEAR.getRed(),  0);
		Assert.assertEquals(0, Color.CLEAR.getGreen(), 0);
		Assert.assertEquals(0, Color.CLEAR.getBlue(), 0);
		Assert.assertEquals(0, Color.CLEAR.getAlpha(), 0);
	}
	
	@Test
	public void constantCyan() {
		Assert.assertEquals(0, Color.CYAN.getRed(),  0);
		Assert.assertEquals(1, Color.CYAN.getGreen(), 0);
		Assert.assertEquals(1, Color.CYAN.getBlue(), 0);
		Assert.assertEquals(1, Color.CYAN.getAlpha(), 0);
	}
	
	@Test
	public void constantDarkGray() {
		Assert.assertEquals(0.25f, Color.DARK_GRAY.getRed(),  0);
		Assert.assertEquals(0.25f, Color.DARK_GRAY.getGreen(), 0);
		Assert.assertEquals(0.25f, Color.DARK_GRAY.getBlue(), 0);
		Assert.assertEquals(1, Color.DARK_GRAY.getAlpha(), 0);
	}
	
	@Test
	public void constantGray() {
		Assert.assertEquals(0.5f, Color.GRAY.getRed(),  0);
		Assert.assertEquals(0.5f, Color.GRAY.getGreen(), 0);
		Assert.assertEquals(0.5f, Color.GRAY.getBlue(), 0);
		Assert.assertEquals(1, Color.GRAY.getAlpha(), 0);
	}
	
	@Test
	public void constantGreen() {
		Assert.assertEquals(0, Color.GREEN.getRed(),  0);
		Assert.assertEquals(1, Color.GREEN.getGreen(), 0);
		Assert.assertEquals(0, Color.GREEN.getBlue(), 0);
		Assert.assertEquals(1, Color.GREEN.getAlpha(), 0);
	}
	
	@Test
	public void constantLightGray() {
		Assert.assertEquals(0.75f, Color.LIGHT_GRAY.getRed(),  0);
		Assert.assertEquals(0.75f, Color.LIGHT_GRAY.getGreen(), 0);
		Assert.assertEquals(0.75f, Color.LIGHT_GRAY.getBlue(), 0);
		Assert.assertEquals(1, Color.LIGHT_GRAY.getAlpha(), 0);
	}
	
	@Test
	public void constantMagenta() {
		Assert.assertEquals(1, Color.MAGENTA.getRed(),  0);
		Assert.assertEquals(0, Color.MAGENTA.getGreen(), 0);
		Assert.assertEquals(1, Color.MAGENTA.getBlue(), 0);
		Assert.assertEquals(1, Color.MAGENTA.getAlpha(), 0);
	}
	
	@Test
	public void constantRed() {
		Assert.assertEquals(1, Color.RED.getRed(),  0);
		Assert.assertEquals(0, Color.RED.getGreen(), 0);
		Assert.assertEquals(0, Color.RED.getBlue(), 0);
		Assert.assertEquals(1, Color.RED.getAlpha(), 0);
	}
	
	@Test
	public void constantWhite() {
		Assert.assertEquals(1, Color.WHITE.getRed(),  0);
		Assert.assertEquals(1, Color.WHITE.getGreen(), 0);
		Assert.assertEquals(1, Color.WHITE.getBlue(), 0);
		Assert.assertEquals(1, Color.WHITE.getAlpha(), 0);
	}
	
	@Test
	public void constantYellow() {
		Assert.assertEquals(1, Color.YELLOW.getRed(),  0);
		Assert.assertEquals(1, Color.YELLOW.getGreen(), 0);
		Assert.assertEquals(0, Color.YELLOW.getBlue(), 0);
		Assert.assertEquals(1, Color.YELLOW.getAlpha(), 0);
	}
	
}
