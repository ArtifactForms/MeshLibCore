package mesh.selection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CompareTest {

	@Test
	public void testInvalidCompareTypeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			Compare.compare(null, 10, 20);
		});
	}

	@Test
	public void testInvalidCompareTypeEnumValue() {
		assertThrows(IllegalArgumentException.class, () -> {
			Compare.compare(CompareType.INVALID_TYPE, 10, 20);
		});
	}

	// Integer Tests

	@Test
	public void testEqualsInteger() {
		assertTrue(Compare.compare(CompareType.EQUALS, 10, 10));
		assertFalse(Compare.compare(CompareType.EQUALS, 10, 20));
	}

	@Test
	public void testLessInteger() {
		assertTrue(Compare.compare(CompareType.LESS, Integer.MIN_VALUE, 10));
		assertFalse(Compare.compare(CompareType.LESS, 10, Integer.MIN_VALUE));
		assertTrue(Compare.compare(CompareType.LESS, 10, 20));
		assertFalse(Compare.compare(CompareType.LESS, 20, 10));
	}

	@Test
	public void testGreaterInteger() {
		assertTrue(Compare.compare(CompareType.GREATER, Integer.MAX_VALUE, -10));
		assertFalse(Compare.compare(CompareType.GREATER, -10, Integer.MAX_VALUE));
		assertTrue(Compare.compare(CompareType.GREATER, 20, 10));
		assertFalse(Compare.compare(CompareType.GREATER, 10, 20));
	}

	@Test
	public void testLessOrEqualInteger() {
		assertTrue(Compare.compare(CompareType.LESS_OR_EQUALS, Integer.MIN_VALUE, 10));
		assertTrue(Compare.compare(CompareType.LESS_OR_EQUALS, 10, 10));
		assertFalse(Compare.compare(CompareType.LESS_OR_EQUALS, 20, Integer.MIN_VALUE));
		assertFalse(Compare.compare(CompareType.LESS_OR_EQUALS, 20, 10));
	}

	@Test
	public void testGreaterOrEqualInteger() {
		assertTrue(Compare.compare(CompareType.GREATER_OR_EQUALS, Integer.MAX_VALUE, -10));
		assertTrue(Compare.compare(CompareType.GREATER_OR_EQUALS, 10, 10));
		assertFalse(Compare.compare(CompareType.GREATER_OR_EQUALS, Integer.MIN_VALUE, 20));
		assertFalse(Compare.compare(CompareType.GREATER_OR_EQUALS, 10, 20));
	}

	@Test
	public void testNotEqualsInteger() {
		assertTrue(Compare.compare(CompareType.NOT_EQUALS, 10, 20));
		assertFalse(Compare.compare(CompareType.NOT_EQUALS, 10, 10));
	}

	// Float Tests

	@Test
	public void testEqualsFloat() {
		assertTrue(Compare.compare(CompareType.EQUALS, 10.2f, 10.2f));
		assertFalse(Compare.compare(CompareType.EQUALS, 10.2f, 20.2f));
	}

	@Test
	public void testLessFloat() {
		assertTrue(Compare.compare(CompareType.LESS, Float.MIN_VALUE, 10.0f));
		assertFalse(Compare.compare(CompareType.LESS, 10.0f, Float.MIN_VALUE));
		assertTrue(Compare.compare(CompareType.LESS, 10.0f, 20.0f));
		assertFalse(Compare.compare(CompareType.LESS, 20.0f, 10.0f));
	}

	@Test
	public void testGreaterFloat() {
		assertTrue(Compare.compare(CompareType.GREATER, Float.MAX_VALUE, -10.0f));
		assertFalse(Compare.compare(CompareType.GREATER, -10.0f, Float.MAX_VALUE));
		assertTrue(Compare.compare(CompareType.GREATER, 20.0f, 10.0f));
		assertFalse(Compare.compare(CompareType.GREATER, 10.0f, 20.0f));
	}

	@Test
	public void testLessOrEqualFloat() {
		assertTrue(Compare.compare(CompareType.LESS_OR_EQUALS, Float.MIN_VALUE, 10.0f));
		assertTrue(Compare.compare(CompareType.LESS_OR_EQUALS, 10.0f, 10.0f));
		assertFalse(Compare.compare(CompareType.LESS_OR_EQUALS, 20.0f, Float.MIN_VALUE));
		assertFalse(Compare.compare(CompareType.LESS_OR_EQUALS, 20.0f, 10.0f));
	}

	@Test
	public void testGreaterOrEqualFloat() {
		assertTrue(Compare.compare(CompareType.GREATER_OR_EQUALS, Float.MAX_VALUE, -10.0f));
		assertTrue(Compare.compare(CompareType.GREATER_OR_EQUALS, 10.0f, 10.0f));
		assertFalse(Compare.compare(CompareType.GREATER_OR_EQUALS, Float.MIN_VALUE, 20.0f));
		assertFalse(Compare.compare(CompareType.GREATER_OR_EQUALS, 10.0f, 20.0f));
	}

	@Test
	public void testNotEqualsFloat() {
		assertTrue(Compare.compare(CompareType.NOT_EQUALS, 10.0f, 20.0f));
		assertFalse(Compare.compare(CompareType.NOT_EQUALS, 10.0f, 10.0f));
	}

	// Double Tests

	@Test
	public void testEqualsDouble() {
		assertTrue(Compare.compare(CompareType.EQUALS, 10.2, 10.2));
		assertFalse(Compare.compare(CompareType.EQUALS, 10.2, 20.2));
	}

	@Test
	public void testLessDouble() {
		assertTrue(Compare.compare(CompareType.LESS, Double.MIN_VALUE, 10.0));
		assertFalse(Compare.compare(CompareType.LESS, 10.0, Double.MIN_VALUE));
		assertTrue(Compare.compare(CompareType.LESS, 10.0, 20.0));
		assertFalse(Compare.compare(CompareType.LESS, 20.0, 10.0));
	}

	@Test
	public void testGreaterDouble() {
		assertTrue(Compare.compare(CompareType.GREATER, Double.MAX_VALUE, -10.0));
		assertFalse(Compare.compare(CompareType.GREATER, -10.0, Double.MAX_VALUE));
		assertTrue(Compare.compare(CompareType.GREATER, 20.0, 10.0));
		assertFalse(Compare.compare(CompareType.GREATER, 10.0, 20.0));
	}

	@Test
	public void testLessOrEqualDouble() {
		assertTrue(Compare.compare(CompareType.LESS_OR_EQUALS, Double.MIN_VALUE, 10.0));
		assertTrue(Compare.compare(CompareType.LESS_OR_EQUALS, 10.0, 10.0));
		assertFalse(Compare.compare(CompareType.LESS_OR_EQUALS, 20.0, Double.MIN_VALUE));
		assertFalse(Compare.compare(CompareType.LESS_OR_EQUALS, 20.0, 10.0));
	}

	@Test
	public void testGreaterOrEqualDouble() {
		assertTrue(Compare.compare(CompareType.GREATER_OR_EQUALS, Double.MAX_VALUE, -10.0));
		assertTrue(Compare.compare(CompareType.GREATER_OR_EQUALS, 10.0, 10.0));
		assertFalse(Compare.compare(CompareType.GREATER_OR_EQUALS, Double.MIN_VALUE, 20.0));
		assertFalse(Compare.compare(CompareType.GREATER_OR_EQUALS, 10.0, 20.0));
	}

	@Test
	public void testNotEqualsDouble() {
		assertTrue(Compare.compare(CompareType.NOT_EQUALS, 10.0, 20.0));
		assertFalse(Compare.compare(CompareType.NOT_EQUALS, 10.0, 10.0));
	}

}
