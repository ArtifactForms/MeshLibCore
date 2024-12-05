package mesh.modifier.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
	public void testDefaultTime() {
		assertEquals(0, modifier.getTime());
	}

	@ParameterizedTest
	@ValueSource(floats = { 0.1f, 0.002f, 60.245f, 100.23f })
	public void testGetSetTime(float time) {
		modifier.setTime(time);
		assertEquals(time, modifier.getTime());
	}

	@Test
	public void testDefaultAmplitude1() {
		assertEquals(1.0f, modifier.getAmplitude1());
	}

	@ParameterizedTest
	@ValueSource(floats = { 1.0f, 15.002f, 20.245f, 100.23f })
	public void testGetSetAmplitude1(float amplitude1) {
		modifier.setAmplitude1(amplitude1);
		assertEquals(amplitude1, modifier.getAmplitude1());
	}

	@Test
	public void testDefaultAmplitude2() {
		assertEquals(0.5f, modifier.getAmplitude2());
	}

	@ParameterizedTest
	@ValueSource(floats = { 1.12f, 11.0352f, 56.245f, 120.23f })
	public void testGetSetAmplitude2(float amplitude2) {
		modifier.setAmplitude2(amplitude2);
		assertEquals(amplitude2, modifier.getAmplitude2());
	}

	@Test
	public void testDefaultWaveLength() {
		assertEquals(5.0f, modifier.getWaveLength());
	}

	@ParameterizedTest
	@ValueSource(floats = { 10.0f, 51.44f, 60.245f })
	public void testGetSetWaveLength(float waveLength) {
		modifier.setWaveLength(waveLength);
		assertEquals(waveLength, modifier.getWaveLength());
	}

	@Test
	public void testDefaultPhaseShift() {
		assertEquals(0, modifier.getPhaseShift());
	}

	@ParameterizedTest
	@ValueSource(floats = { 10.0f, 4.44f, 100.245f })
	public void testGetSetPhase(float phaseShift) {
		modifier.setPhaseShift(phaseShift);
		assertEquals(phaseShift, modifier.getPhaseShift());
	}

	@Test
	public void testDefaultDecayFactor() {
		assertEquals(0.1f, modifier.getDecayFactor());
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
				new Vector3f(0.001f, 0.32f, -2.34f) };
		for (int i = 0; i < centers.length; i++) {
			modifier.setCenter(centers[i]);
			assertEquals(centers[i], modifier.getCenter());
		}
	}

	@Test
	public void testSetCenterToNullThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			modifier.setCenter(null);
		});
	}

	@Test
	public void testSetWaveLengthToZeroThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			modifier.setWaveLength(0);
		});
	}

	@ParameterizedTest
	@ValueSource(floats = { -1.0f, -15.002f, -20.245f, -100.23f })
	public void testSetWaveLengthLessThanZeroThrowsIllegalArgumentException(float waveLength) {
		assertThrows(IllegalArgumentException.class, () -> {
			modifier.setWaveLength(waveLength);
		});
	}
	
	@Test
	public void testModifyNullMeshThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			modifier.modify(null);
		});
	}

}
