package mesh.modifier.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.RippleModifier;

public class RippleModifierTest {

    private RippleModifier modifier;

    @BeforeEach
    public void setUp() {
        modifier = new RippleModifier();
    }

    @Test
    public void testModifierImplementsModifierInterface() {
        assertTrue(modifier instanceof IMeshModifier);
    }

    @Test
    public void testModifiedMeshIsNotNull() {
        Mesh3D mesh = new Mesh3D();
        assertNotNull(modifier.modify(mesh));
    }

    @Test
    public void testReturnsReferenceToTheModifiedMesh() {
        Mesh3D mesh0 = new CubeCreator().create();
        Mesh3D mesh1 = modifier.modify(mesh0);
        assertSame(mesh0, mesh1);
    }

    @Test
    public void testDefaultValues() {
        assertAll("Default Values",
            () -> assertEquals(0, modifier.getTime()),
            () -> assertEquals(1.0f, modifier.getAmplitude1()),
            () -> assertEquals(0.5f, modifier.getAmplitude2()),
            () -> assertEquals(5.0f, modifier.getWaveLength()),
            () -> assertEquals(0.1f, modifier.getDecayFactor())
        );
    }

    @ParameterizedTest
    @ValueSource(floats = { 1.0f, 15.002f, 20.245f })
    public void testGetSetAmplitude1(float amplitude1) {
        modifier.setAmplitude1(amplitude1);
        assertEquals(amplitude1, modifier.getAmplitude1());
    }

    @ParameterizedTest
    @ValueSource(floats = { 1.12f, 11.0352f, 56.245f, 120.23f })
    public void testGetSetAmplitude2(float amplitude2) {
        modifier.setAmplitude2(amplitude2);
        assertEquals(amplitude2, modifier.getAmplitude2());
    }

    @ParameterizedTest
    @ValueSource(floats = { 10.0f, 51.44f, 60.245f })
    public void testGetSetWaveLength(float waveLength) {
        modifier.setWaveLength(waveLength);
        assertEquals(waveLength, modifier.getWaveLength());
    }

    @ParameterizedTest
    @ValueSource(floats = { 155.0f, 20.44f, -100.245f })
    public void testGetSetDecayFactor(float decayFactor) {
        modifier.setDecayFactor(decayFactor);
        assertEquals(decayFactor, modifier.getDecayFactor());
    }

    @Test
    public void testCenterIsNotNullByDefault() {
        assertNotNull(modifier.getCenter());
    }

    @Test
    public void testCenterIsAtOriginByDefault() {
        assertEquals(Vector3f.ZERO, modifier.getCenter());
    }

    @Test
    public void testGetSetCenter() {
        Vector3f[] centers = new Vector3f[] { 
            new Vector3f(-0.134f, 1, 10.4f), 
            new Vector3f(102.34f, 332.431f, -0.4f),
            new Vector3f(46.34f, -32.432f, 0.134f), 
            new Vector3f(0.001f, 0.32f, -2.34f)
        };
        for (int i = 0; i < centers.length; i++) {
            modifier.setCenter(centers[i]);
            assertEquals(centers[i], modifier.getCenter());
        }
    }

    @Test
    public void testSetCenterToNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> modifier.setCenter(null));
    }

    @Test
    public void testSetWaveLengthToZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> modifier.setWaveLength(0));
    }

    @ParameterizedTest
    @ValueSource(floats = { -1.0f, -15.002f, -20.245f, -100.23f })
    public void testSetWaveLengthLessThanZeroThrowsException(float waveLength) {
        assertThrows(IllegalArgumentException.class, () -> modifier.setWaveLength(waveLength));
    }

    @Test
    public void testModifyNullMeshThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> modifier.modify(null));
    }

    @Test
    public void testNegativeAmplitude1ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> modifier.setAmplitude1(-1));
    }

    @Test
    public void testNegativeAmplitude2ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> modifier.setAmplitude2(-1));
    }

    @Test
    public void testDefaultDirection() {
        Vector3f expected = new Vector3f(0, -1, 0);
        assertEquals(expected, modifier.getDirection());
    }

    @Test
    public void testSetNullDirectionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> modifier.setDirection(null));
    }

    @Test
    public void testDirectionIsNormalizedByDefault() {
        float length = modifier.getDirection().length();
        assertEquals(1, length);
    }

    @Test
    public void testSetDirectionNormalizesDirection() {
        Vector3f direction = new Vector3f(5, 43.45f, 1);
        modifier.setDirection(direction);
        float length = modifier.getDirection().length();
        assertEquals(1, length, 0.0001f);
    }

    @Test
    public void testDirectionIsNormalizedInternally() {
        Vector3f expected = new Vector3f(1, 3.556f, 2.345f);
        Vector3f direction = new Vector3f(expected);
        modifier.setDirection(direction);
        assertEquals(expected, direction);
    }

    @Test
    public void testDefaultPhaseShift() {
        assertEquals(0, modifier.getPhaseShift());
    }

    @Test
    public void testPhaseShiftMultiplesOfTwoPi() {
        float expected = Mathf.TWO_PI;
        modifier.setPhaseShift(expected * 4);
        assertEquals(0, modifier.getPhaseShift());
    }

    @ParameterizedTest
    @ValueSource(floats = { 0, Mathf.HALF_PI, Mathf.QUARTER_PI })
    public void testGetSetPhaseShift(float phaseShift) {
        modifier.setPhaseShift(phaseShift);
        assertEquals(phaseShift, modifier.getPhaseShift());
    }

    @Test
    public void testSetPhaseShiftPositive() {
        RippleModifier modifier = new RippleModifier();
        modifier.setPhaseShift(1.5f);
        assertEquals(1.5f, modifier.getPhaseShift(), 0.001);
    }

    @Test
    public void testSetPhaseShiftNegative() {
        RippleModifier modifier = new RippleModifier();
        modifier.setPhaseShift(-2.0f);
        assertEquals(Math.PI * 2 - 2.0f, modifier.getPhaseShift(), 0.001);
    }

    @Test
    public void testSetPhaseShiftZero() {
        RippleModifier modifier = new RippleModifier();
        modifier.setPhaseShift(0.0f);
        assertEquals(0.0f, modifier.getPhaseShift(), 0.001);
    }

    @Test
    public void testSetPhaseShiftLargePositive() {
        RippleModifier modifier = new RippleModifier();
        modifier.setPhaseShift(Mathf.PI * 10);
        assertEquals(0.0f, modifier.getPhaseShift(), 0.001);
    }

    @Test
    public void testSetPhaseShiftLargeNegative() {
        RippleModifier modifier = new RippleModifier();
        modifier.setPhaseShift(-Mathf.PI * 10);
        assertEquals(0.0f, modifier.getPhaseShift(), 0.001);
    }

    @Test
    public void testSetPhaseShiftExactlyTwoPi() {
        modifier.setPhaseShift(Mathf.TWO_PI);
        assertEquals(0.0f, modifier.getPhaseShift(), 0.001);
    }

    @Test
    public void testSetPhaseShiftVerySmallPositive() {
        modifier.setPhaseShift(0.0001f);
        assertEquals(0.0001f, modifier.getPhaseShift(), 0.001);
    }
    
}