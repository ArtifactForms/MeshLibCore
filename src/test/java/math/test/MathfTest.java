package math.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import math.Mathf;

public class MathfTest {

    @Test
    public void testToOneDimensionalIndex() {
        // Test cases with valid inputs
        assertEquals(0, Mathf.toOneDimensionalIndex(0, 0, 3));
        assertEquals(1, Mathf.toOneDimensionalIndex(0, 1, 3));
        assertEquals(2, Mathf.toOneDimensionalIndex(0, 2, 3));
        assertEquals(3, Mathf.toOneDimensionalIndex(1, 0, 3));
        assertEquals(4, Mathf.toOneDimensionalIndex(1, 1, 3));
        assertEquals(5, Mathf.toOneDimensionalIndex(1, 2, 3));

        // Test case with invalid input (negative row index)
        assertThrows(IllegalArgumentException.class, () -> {
            Mathf.toOneDimensionalIndex(-1, 0, 3);
        });

        // Test case with invalid input (negative column index)
        assertThrows(IllegalArgumentException.class, () -> {
            Mathf.toOneDimensionalIndex(0, -1, 3);
        });

        // Test case with invalid input (zero number of columns)
        assertThrows(IllegalArgumentException.class, () -> {
            Mathf.toOneDimensionalIndex(0, 0, 0);
        });
    }

    @Test
    public void testMin() {
        // Basic cases
        assertEquals(2, Mathf.min(2, 3));
        assertEquals(-3, Mathf.min(-3, -2));

        // Equal values
        assertEquals(5, Mathf.min(5, 5));

        // Integer.MIN_VALUE and Integer.MAX_VALUE
        assertEquals(Integer.MIN_VALUE,
                Mathf.min(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE,
                Mathf.min(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void testMax() {
        // Basic cases
        assertEquals(3, Mathf.max(2, 3));
        assertEquals(-2, Mathf.max(-3, -2));

        // Equal values
        assertEquals(5, Mathf.max(5, 5));

        // Integer.MIN_VALUE and Integer.MAX_VALUE
        assertEquals(Integer.MAX_VALUE,
                Mathf.max(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE,
                Mathf.max(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void testMinArray() {
        // Empty array
        assertEquals(0, Mathf.min(new int[0]));

        // Single-element array
        assertEquals(5, Mathf.min(new int[] { 5 }));

        // Multiple elements, positive values
        assertEquals(1, Mathf.min(new int[] { 2, 4, 1, 3 }));

        // Multiple elements, negative values
        assertEquals(-5, Mathf.min(new int[] { -2, -5, -1, -3 }));

        // Mixed positive and negative values
        assertEquals(-3, Mathf.min(new int[] { 2, -3, 1, 3 }));
    }

    @Test
    public void testMaxArray() {
        // Empty array
        assertEquals(0, Mathf.max(new int[0]));

        // Single-element array
        assertEquals(5, Mathf.max(new int[] { 5 }));

        // Multiple elements, positive values
        assertEquals(4, Mathf.max(new int[] { 2, 4, 1, 3 }));

        // Multiple elements, negative values
        assertEquals(-1, Mathf.max(new int[] { -2, -5, -1, -3 }));

        // Mixed positive and negative values
        assertEquals(3, Mathf.max(new int[] { 2, -3, 1, 3 }));
    }

    @Test
    public void testMaxFloat() {
        assertEquals(5.0f, Mathf.max(3.0f, 5.0f));
        assertEquals(-2.0f, Mathf.max(-5.0f, -2.0f));
        assertEquals(0.0f, Mathf.max(-0.0f, 0.0f));
        assertTrue(Float.isNaN(Mathf.max(Float.NaN, 3.0f)));
    }

    @Test
    public void testMinFloat() {
        assertEquals(3.0f, Mathf.min(3.0f, 5.0f));
        assertEquals(-5.0f, Mathf.min(-5.0f, -2.0f));
        assertEquals(-0.0f, Mathf.min(-0.0f, 0.0f));
        assertTrue(Float.isNaN(Mathf.min(Float.NaN, 3.0f)));
    }

    @Test
    public void testMaxArrayFloat() {
        // Empty array
        assertTrue(Float.isNaN(Mathf.max()));

        // Single-element array
        assertEquals(5.0f, Mathf.max(5.0f));

        // Multiple elements, positive values
        assertEquals(4.0f, Mathf.max(2.0f, 4.0f, 1.0f, 3.0f));

        // Multiple elements, negative values
        assertEquals(-1.0f, Mathf.max(-2.0f, -5.0f, -1.0f, -3.0f));

        // Mixed positive and negative values
        assertEquals(3.0f, Mathf.max(2.0f, -3.0f, 1.0f, 3.0f));
    }

    @Test
    public void testMinArrayFloat() {
        // Empty array
        assertTrue(Float.isNaN(Mathf.min()));

        // Single-element array
        assertEquals(5.0f, Mathf.min(5.0f));

        // Multiple elements, positive values
        assertEquals(1.0f, Mathf.min(2.0f, 4.0f, 1.0f, 3.0f));

        // Multiple elements, negative values
        assertEquals(-5.0f, Mathf.min(-2.0f, -5.0f, -1.0f, -3.0f));

        // Mixed positive and negative values
        assertEquals(-3.0f, Mathf.min(2.0f, -3.0f, 1.0f, 3.0f));
    }

    @Test
    public void testRoundToInt() {
        // Positive numbers
        assertEquals(2, Mathf.roundToInt(1.5f));
        assertEquals(3, Mathf.roundToInt(2.5f));

        // Negative numbers
        assertEquals(-1, Mathf.roundToInt(-1.5f));
        assertEquals(-2, Mathf.roundToInt(-2.5f));

        // Negative numbers
        assertEquals(-2, Mathf.roundToInt(-1.6f));
        assertEquals(-3, Mathf.roundToInt(-2.6f));

        // Zero
        assertEquals(0, Mathf.roundToInt(0.0f));
        assertEquals(0, Mathf.roundToInt(-0.0f));

        // Integer values
        assertEquals(5, Mathf.roundToInt(5.0f));
        assertEquals(-5, Mathf.roundToInt(-5.0f));
    }

}