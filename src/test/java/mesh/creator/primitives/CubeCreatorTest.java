package mesh.creator.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class CubeCreatorTest {

	private CubeCreator creator;

	@BeforeEach
	public void setUp() {
		creator = new CubeCreator();
	}

	@Test
	public void testImplementsMeshCreatorInterface() {
		assertTrue(creator instanceof IMeshCreator);
	}

	@Test
	public void testCreatedMeshIsNotNull() {
		assertNotNull(creator.create());
	}

	@Test
	public void testDefaultRadius() {
		assertEquals(1, creator.getRadius());
	}

	@Test
	public void testGetSetRadius() {
		float expectedRadius = 0.345f;
		creator.setRadius(expectedRadius);
		assertEquals(expectedRadius, creator.getRadius());
	}

	@Test
	public void testFaceCount() {
		assertEquals(6, creator.create().getFaceCount());
	}

	@Test
	public void testVertexCount() {
		assertEquals(8, creator.create().getVertexCount());
	}

	@Test
	public void testFace0() {
		int[] expected = new int[] { 3, 0, 1, 2 };
		int[] actual = creator.create().getFaceAt(0).indices;
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testFace1() {
		int[] expected = new int[] { 6, 5, 4, 7 };
		int[] actual = creator.create().getFaceAt(1).indices;
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testFace2() {
		int[] expected = new int[] { 1, 0, 4, 5 };
		int[] actual = creator.create().getFaceAt(2).indices;
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testFace3() {
		int[] expected = new int[] { 1, 5, 6, 2 };
		int[] actual = creator.create().getFaceAt(3).indices;
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testFace4() {
		int[] expected = new int[] { 6, 7, 3, 2 };
		int[] actual = creator.create().getFaceAt(4).indices;
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testFace5() {
		int[] expected = new int[] { 3, 7, 4, 0 };
		int[] actual = creator.create().getFaceAt(5).indices;
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testDefaultVertices() {
		Vector3f[] expected = new Vector3f[] {
			new Vector3f(+1, -1, -1),
			new Vector3f(+1, -1, +1),
			new Vector3f(-1, -1, +1),
			new Vector3f(-1, -1, -1),
			new Vector3f(+1, +1, -1),
			new Vector3f(+1, +1, +1),
			new Vector3f(-1, +1, +1),
			new Vector3f(-1, +1, -1),
		};
		Mesh3D cube = creator.create();
		for (int i = 0; i < 8; i++) {
			Vector3f expectedVertex = expected[i];
			Vector3f actualVertex = cube.getVertexAt(i);
			assertEquals(actualVertex, expectedVertex);
		}
	}
	
    @ParameterizedTest
    @ValueSource(floats = {
    		1,
    		42.44f,
    		245.2444f,
    		14332.21f,
    		1342.02f,
    		43.f,
    		Float.MIN_VALUE,
    		Float.MAX_VALUE
    })
	public void testVerticesDifferentRadii(float radius) {
		Vector3f[] expected = new Vector3f[] {
			new Vector3f(+radius, -radius, -radius),
			new Vector3f(+radius, -radius, +radius),
			new Vector3f(-radius, -radius, +radius),
			new Vector3f(-radius, -radius, -radius),
			new Vector3f(+radius, +radius, -radius),
			new Vector3f(+radius, +radius, +radius),
			new Vector3f(-radius, +radius, +radius),
			new Vector3f(-radius, +radius, -radius),
		};
		creator.setRadius(radius);
		Mesh3D cube = creator.create();
		for (int i = 0; i < 8; i++) {
			Vector3f expectedVertex = expected[i];
			Vector3f actualVertex = cube.getVertexAt(i);
			assertEquals(actualVertex, expectedVertex);
		}
	}

}
