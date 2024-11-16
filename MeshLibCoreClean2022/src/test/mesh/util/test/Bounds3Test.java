package mesh.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.util.Bounds3;

public class Bounds3Test {

    private Bounds3 bounds;

    @Before
    public void setUp() {
        bounds = new Bounds3();
    }

    @Test
    public void notEqualsToNull() {
        assertFalse(bounds.equals(null));
    }

    @Test
    public void equalsToItself() {
        assertTrue(bounds.equals(bounds));
    }

    @Test
    public void equalsToOtherConstructedWithSameSize() {
        Vector3f size = createRandomVector();
        Bounds3 bounds0 = new Bounds3(size);
        Bounds3 bounds1 = new Bounds3(size);
        assertTrue(bounds0.equals(bounds1));
    }

    @Test
    public void equalsToOtherConstructedWithSameCenterAndSize() {
        Vector3f center = createRandomVector();
        Vector3f size = createRandomVector();
        Bounds3 bounds0 = new Bounds3(center, size);
        Bounds3 bounds1 = new Bounds3(center, size);
        assertTrue(bounds0.equals(bounds1));
    }

    @Test
    public void centerIsNotNullByDefault() {
        assertNotNull(bounds.getCenter());
    }

    @Test
    public void centerIsAtOriginByDefault() {
        Vector3f origin = new Vector3f();
        assertEquals(origin, bounds.getCenter());
    }

    @Test
    public void returnsNewCenter() {
        Vector3f center0 = bounds.getCenter();
        Vector3f center1 = bounds.getCenter();
        assertTrue(center0 != center1);
    }

    @Test
    public void doesNotChangeCenterInternally() {
        Vector3f center0 = bounds.getCenter();
        center0.set(1, 2, 3);
        assertEquals(new Vector3f(), bounds.getCenter());
    }

    @Test
    public void getCenterXisZeroByDefault() {
        assertEquals(0, bounds.getCenterX(), 0);
    }

    @Test
    public void getCenterYisZeroByDefault() {
        assertEquals(0, bounds.getCenterY(), 0);
    }

    @Test
    public void getCenterZisZeroByDefault() {
        assertEquals(0, bounds.getCenterZ(), 0);
    }

    @Test
    public void consturctorCopiesCenter() {
        Vector3f center = new Vector3f(1, 2, 3);
        Bounds3 bounds = new Bounds3(center, new Vector3f());
        Vector3f center0 = bounds.getCenter();
        assertTrue(center != center0);
    }

    @Test
    public void getSetRandomMin() {
        Vector3f randomMin = createRandomVector();
        bounds.setMin(randomMin.getX(), randomMin.getY(), randomMin.getZ());
        assertEquals(randomMin, bounds.getMin());
    }

    @Test
    public void getSetRandomMax() {
        Vector3f randomMax = createRandomVector();
        bounds.setMax(randomMax.getX(), randomMax.getY(), randomMax.getZ());
        assertEquals(randomMax, bounds.getMax());
    }

    @Test
    public void getRandomMinX() {
        Vector3f randomMin = createRandomVector();
        bounds.setMin(randomMin.getX(), randomMin.getY(), randomMin.getZ());
        assertEquals(randomMin.getX(), bounds.getMinX(), 0);
    }

    @Test
    public void getRandomMinY() {
        Vector3f randomMin = createRandomVector();
        bounds.setMin(randomMin.getX(), randomMin.getY(), randomMin.getZ());
        assertEquals(randomMin.getY(), bounds.getMinY(), 0);
    }

    @Test
    public void getRandomMinZ() {
        Vector3f randomMin = createRandomVector();
        bounds.setMin(randomMin.getX(), randomMin.getY(), randomMin.getZ());
        assertEquals(randomMin.getZ(), bounds.getMinZ(), 0);
    }

    @Test
    public void getRandomMaxX() {
        Vector3f randomMax = createRandomVector();
        bounds.setMax(randomMax.getX(), randomMax.getY(), randomMax.getZ());
        assertEquals(randomMax.getX(), bounds.getMaxX(), 0);
    }

    @Test
    public void getRandomMaxY() {
        Vector3f randomMax = createRandomVector();
        bounds.setMax(randomMax.getX(), randomMax.getY(), randomMax.getZ());
        assertEquals(randomMax.getY(), bounds.getMaxY(), 0);
    }

    @Test
    public void getRandomMaxZ() {
        Vector3f randomMax = createRandomVector();
        bounds.setMax(randomMax.getX(), randomMax.getY(), randomMax.getZ());
        assertEquals(randomMax.getZ(), bounds.getMaxZ(), 0);
    }

    @Test
    public void volumeIsZeroByDefault() {
        assertEquals(0, bounds.volume(), 0);
    }

    @Test
    public void volumeOfBoundsConstructedWithSpecifiedSize() {
        Vector3f size = createRandomVector();
        Bounds3 bounds = new Bounds3(size);
        float expected = size.getX() * size.getY() * size.getZ();
        assertEquals(expected, bounds.volume(), Mathf.ZERO_TOLERANCE);
    }

    @Test
    public void getRandomSizeAsVector() {
        Vector3f center = new Vector3f();
        Vector3f size = createRandomPositiveVector();
        Bounds3 bounds = new Bounds3(center, size);
        assertEquals(size, bounds.getSize());
    }

    @Test
    public void getRandomSizeDoesNotReturnProvidedSize() {
        Vector3f center = new Vector3f();
        Vector3f size = createRandomPositiveVector();
        Bounds3 bounds = new Bounds3(center, size);
        assertTrue(size != bounds.getSize());
    }

    @Test
    public void getSetRandomCenter() {
        Bounds3 bounds = new Bounds3();
        Vector3f center = createRandomVector();
        bounds.setCenter(center);
        assertEquals(center, bounds.getCenter());
    }

    @Test
    public void getCenterDoesNotStoreOriginal() {
        Bounds3 bounds = new Bounds3();
        Vector3f center = createRandomVector();
        bounds.setCenter(center);
        assertTrue(bounds.getCenter() != center);
    }

    @Test
    public void randomExpandWidth() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getWidth(), expand, 0);
    }

    @Test
    public void randomExpandHeight() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getHeight(), expand, 0);
    }

    @Test
    public void randomExpandDepth() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getHeight(), expand, 0);
    }

    @Test
    public void randomExpandMinX() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getMinX(), -expand * 0.5f, 0);
    }

    @Test
    public void randomExpandMinY() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getMinY(), -expand * 0.5f, 0);
    }

    @Test
    public void randomExpandMinZ() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getMinZ(), -expand * 0.5f, 0);
    }

    @Test
    public void randomExpandMaxX() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getMaxX(), expand * 0.5f, 0);
    }

    @Test
    public void randomExpandMaxY() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getMaxY(), expand * 0.5f, 0);
    }

    @Test
    public void randomExpandMaxZ() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        assertEquals(bounds.getMaxZ(), expand * 0.5f, 0);
    }

    @Test
    public void randomExpandVolume() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        float expectedVolume = expand * expand * expand;
        assertEquals(expectedVolume, bounds.volume(), 0);
    }

    @Test
    public void randomExpandSize() {
        float expand = Mathf.random(0, 100000f);
        Bounds3 bounds = new Bounds3();
        bounds.expand(expand);
        Vector3f expected = new Vector3f(expand, expand, expand);
        assertEquals(expected, bounds.getSize());
    }

    private static Vector3f createRandomPositiveVector() {
        float min = 0;
        float max = Float.MAX_VALUE / 2f;
        float x = Mathf.random(min, max);
        float y = Mathf.random(min, max);
        float z = Mathf.random(min, max);
        return new Vector3f(x, y, z);
    }

    private static Vector3f createRandomVector() {
        float min = Float.MIN_VALUE / 2f;
        float max = Float.MAX_VALUE / 2f;
        float x = Mathf.random(min, max);
        float y = Mathf.random(min, max);
        float z = Mathf.random(min, max);
        return new Vector3f(x, y, z);
    }

}
