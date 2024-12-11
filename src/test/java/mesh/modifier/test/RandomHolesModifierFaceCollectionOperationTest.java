package mesh.modifier.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.RandomHolesModifier;

public class RandomHolesModifierFaceCollectionOperationTest {

	private RandomHolesModifier modifier;

	@BeforeEach
	public void setUp() {
		modifier = new RandomHolesModifier();
	}

	@Test
	public void returnsNonNullMesh() {
		Mesh3D mesh = new CubeCreator().create();
		assertNotNull(modifier.modify(mesh, new ArrayList<Face3D>()));
	}

	@Test
	public void nullMeshThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> {
			modifier.modify(null, new ArrayList<Face3D>());
		});
	}

	@Test
	public void nullCollcetionThrowsException() {
		ArrayList<Face3D> faces = null;
		assertThrows(IllegalArgumentException.class, () -> {
			modifier.modify(new Mesh3D(), faces);
		});
	}

	@Test
	public void testFaceCountModifyAllCubeFaces() {
		Mesh3D cube = new CubeCreator().create();
		ArrayList<Face3D> faces = new ArrayList<Face3D>(cube.getFaces());
		modifier.modify(cube, faces);
		assertEquals(24, cube.getFaceCount());
	}

	@Test
	public void testFaceCountModifyTwoCubeFaces() {
		Mesh3D cube = new CubeCreator().create();
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		faces.add(cube.getFaceAt(0));
		faces.add(cube.getFaceAt(5));
		modifier.modify(cube, faces);
		assertEquals(12, cube.getFaceCount());
	}

	@Test
	public void testVertexCountModifyAllCubeFaces() {
		Mesh3D cube = new CubeCreator().create();
		ArrayList<Face3D> faces = new ArrayList<Face3D>(cube.getFaces());
		modifier.modify(cube, faces);
		assertEquals(32, cube.getVertexCount());
	}

	@Test
	public void testVertexCountModifyTwoCubeFaces() {
		Mesh3D cube = new CubeCreator().create();
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		faces.add(cube.getFaceAt(3));
		faces.add(cube.getFaceAt(4));
		modifier.modify(cube, faces);
		assertEquals(16, cube.getVertexCount());
	}

	@ParameterizedTest
	@ValueSource(longs = { 0, 4432, 245, -14332, -13423293, 4324243,
	    Long.MAX_VALUE, Long.MIN_VALUE })
	public void testFacesWithDifferentSeeds(long seed) {
		int[][] expected = new int[][] { { 3, 0, 9, 8 }, { 0, 1, 10, 9 },
		    { 1, 2, 11, 10 }, { 2, 3, 8, 11 }, { 6, 5, 13, 12 }, { 5, 4, 14, 13 },
		    { 4, 7, 15, 14 }, { 7, 6, 12, 15 }, { 1, 0, 17, 16 }, { 0, 4, 18, 17 },
		    { 4, 5, 19, 18 }, { 5, 1, 16, 19 }, { 1, 5, 21, 20 }, { 5, 6, 22, 21 },
		    { 6, 2, 23, 22 }, { 2, 1, 20, 23 }, { 6, 7, 25, 24 }, { 7, 3, 26, 25 },
		    { 3, 2, 27, 26 }, { 2, 6, 24, 27 }, { 3, 7, 29, 28 }, { 7, 4, 30, 29 },
		    { 4, 0, 31, 30 }, { 0, 3, 28, 31 } };
		Mesh3D mesh = new CubeCreator().create();
		modifier.setSeed(seed);
		modifier.modify(mesh, mesh.getFaces());
		for (int i = 0; i < expected.length; i++) {
			int[] actual = mesh.getFaceAt(i).indices;
			assertArrayEquals(expected[i], actual);
		}
	}

	@Test
	public void testPlaneVerticesWithZeroSeeed() {
		Vector3f[] expected = { new Vector3f(1.0f, 0.0f, -1.0f),
		    new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 1.0f),
		    new Vector3f(-1.0f, 0.0f, -1.0f),
		    new Vector3f(0.6847742f, 0.0f, -0.6847742f),
		    new Vector3f(0.6847742f, 0.0f, 0.6847742f),
		    new Vector3f(-0.6847742f, 0.0f, 0.6847742f),
		    new Vector3f(-0.6847742f, 0.0f, -0.6847742f) };
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		Mesh3D plane = new PlaneCreator().create();
		faces.add(plane.getFaceAt(0));
		modifier.modify(plane, faces);
		assertArrayEquals(expected, plane.vertices.toArray());
	}

	@Test
	public void testPlaneVerticesWithPositiveSeeed() {
		Vector3f[] expected = { new Vector3f(1.0f, 0.0f, -1.0f),
		    new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 1.0f),
		    new Vector3f(-1.0f, 0.0f, -1.0f),
		    new Vector3f(0.31211585f, 0.0f, -0.31211585f),
		    new Vector3f(0.31211585f, 0.0f, 0.31211585f),
		    new Vector3f(-0.31211585f, 0.0f, 0.31211585f),
		    new Vector3f(-0.31211585f, 0.0f, -0.31211585f), };
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		Mesh3D plane = new PlaneCreator().create();
		faces.add(plane.getFaceAt(0));
		modifier.setSeed(13424);
		modifier.modify(plane, faces);
		assertArrayEquals(expected, plane.vertices.toArray());
	}

	@Test
	public void testPlaneVerticesWithNegativeSeeed() {
		Vector3f[] expected = { new Vector3f(1.0f, 0.0f, -1.0f),
		    new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 1.0f),
		    new Vector3f(-1.0f, 0.0f, -1.0f),
		    new Vector3f(0.2969781f, 0.0f, -0.2969781f),
		    new Vector3f(0.2969781f, 0.0f, 0.2969781f),
		    new Vector3f(-0.2969781f, 0.0f, 0.2969781f),
		    new Vector3f(-0.2969781f, 0.0f, -0.2969781f), };
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		Mesh3D plane = new PlaneCreator().create();
		faces.add(plane.getFaceAt(0));
		modifier.setSeed(-3244324);
		modifier.modify(plane, faces);
		assertArrayEquals(expected, plane.vertices.toArray());
	}

	@Test
	public void testPlaneVerticesWithSeedMaxLong() {
		Vector3f[] expected = { new Vector3f(1.0f, 0.0f, -1.0f),
		    new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 1.0f),
		    new Vector3f(-1.0f, 0.0f, -1.0f),
		    new Vector3f(0.31515408f, 0.0f, -0.31515408f),
		    new Vector3f(0.31515408f, 0.0f, 0.31515408f),
		    new Vector3f(-0.31515408f, 0.0f, 0.31515408f),
		    new Vector3f(-0.31515408f, 0.0f, -0.31515408f), };
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		Mesh3D plane = new PlaneCreator().create();
		faces.add(plane.getFaceAt(0));
		modifier.setSeed(Long.MAX_VALUE);
		modifier.modify(plane, faces);
		assertArrayEquals(expected, plane.vertices.toArray());
	}

	@Test
	public void testPlaneVerticesWithSeedMinLong() {
		Vector3f[] expected = { new Vector3f(1.0f, 0.0f, -1.0f),
		    new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 1.0f),
		    new Vector3f(-1.0f, 0.0f, -1.0f),
		    new Vector3f(0.6847742f, 0.0f, -0.6847742f),
		    new Vector3f(0.6847742f, 0.0f, 0.6847742f),
		    new Vector3f(-0.6847742f, 0.0f, 0.6847742f),
		    new Vector3f(-0.6847742f, 0.0f, -0.6847742f), };
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		Mesh3D plane = new PlaneCreator().create();
		faces.add(plane.getFaceAt(0));
		modifier.setSeed(Long.MIN_VALUE);
		modifier.modify(plane, faces);
		assertArrayEquals(expected, plane.vertices.toArray());
	}

	@Test
	public void tesCubeVerticesWithPositiveSeed() {
		Vector3f[] expected = new Vector3f[] { new Vector3f(1.0f, -1.0f, -1.0f),
		    new Vector3f(1.0f, -1.0f, 1.0f), new Vector3f(-1.0f, -1.0f, 1.0f),
		    new Vector3f(-1.0f, -1.0f, -1.0f), new Vector3f(1.0f, 1.0f, -1.0f),
		    new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(-1.0f, 1.0f, 1.0f),
		    new Vector3f(-1.0f, 1.0f, -1.0f),
		    new Vector3f(-1.0f, 0.2517369f, 0.2517369f),
		    new Vector3f(-1.0f, 0.2517369f, -0.2517369f),
		    new Vector3f(-1.0f, -0.2517369f, -0.2517369f),
		    new Vector3f(-1.0f, -0.2517369f, 0.2517369f), };
		Mesh3D mesh = new CubeCreator().create();
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		faces.add(mesh.getFaceAt(4));
		modifier = new RandomHolesModifier(0.1f, 0.3f);
		modifier.setSeed(234453);
		modifier.modify(mesh, faces);
		assertArrayEquals(expected, mesh.vertices.toArray());
	}

	@ParameterizedTest
	@ValueSource(floats = { 0.1f, 0.322f, 0.123f, 0.022f, 0.245f, 0.751f,
	    0.965f })
	public void testMinMaxAmountPositiveValuesAndSeed(float amount) {
		Mesh3D expected = new PlaneCreator().create();
		ExtrudeModifier extrudeModifier = new ExtrudeModifier();
		extrudeModifier.setRemoveFaces(true);
		extrudeModifier.setScale(amount);
		extrudeModifier.setAmount(0);
		expected.apply(extrudeModifier);

		Mesh3D actual = new PlaneCreator().create();
		ArrayList<Face3D> faces = new ArrayList<Face3D>();
		faces.add(actual.getFaceAt(0));
		modifier = new RandomHolesModifier(amount, amount);
		modifier.setSeed(234);
		modifier.modify(actual, faces);
		assertArrayEquals(expected.vertices.toArray(), actual.vertices.toArray());
	}

}
