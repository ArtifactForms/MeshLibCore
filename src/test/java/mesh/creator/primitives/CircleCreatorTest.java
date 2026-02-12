package mesh.creator.primitives;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;

public class CircleCreatorTest {

	private CircleCreator creator;

	@BeforeEach
	public void setUp() {
		creator = new CircleCreator();
	}

	@Test
	public void testCreatorImplementsCreatorInterface() {
		assertTrue(creator instanceof IMeshCreator);
	}

	@Test
	public void testCreatedMeshIsNotNull() {
		assertNotNull(creator.create());
	}

	@Test
	public void testCreatesUniqueInstances() {
		Mesh3D mesh0 = creator.create();
		Mesh3D mesh1 = creator.create();
		assertNotSame(mesh0, mesh1);
	}

	@Test
	public void testDefaultRadius() {
		assertEquals(1, creator.getRadius());
	}

	@ParameterizedTest
	@ValueSource(floats = { 0.1f, 10.543f, 29.333f, 234.45f })
	public void testGetSetRadius(float expectedRadius) {
		creator.setRadius(expectedRadius);
		assertEquals(expectedRadius, creator.getRadius());
	}

	@Test
	public void testDefaultVertices() {
		assertEquals(32, creator.getVertices());
	}

	@ParameterizedTest
	@ValueSource(ints = { 4, 12, 34, 100 })
	public void testGetSetVertices(int expectedVertices) {
		creator.setVertices(expectedVertices);
		assertEquals(expectedVertices, creator.getVertices());
	}

	@Test
	public void testDefaultCenterY() {
		assertEquals(0, creator.getCenterY());
	}

	@ParameterizedTest
	@ValueSource(floats = { -0.1f, -29.333f, 100.543f, 1334.45f })
	public void testGetSetCenterY(float expectedCenterY) {
		creator.setCenterY(expectedCenterY);
		assertEquals(expectedCenterY, creator.getCenterY());
	}

	@Test
	public void testDefaultFillType() {
		assertEquals(FillType.NOTHING, creator.getFillType());
	}

	@Test
	public void testGetSetFillType() {
		for (FillType fillType : FillType.values()) {
			creator.setFillType(fillType);
			assertEquals(fillType, creator.getFillType());
		}
	}

	@Test
	public void testDefaultFaceCount() {
		Mesh3D circle = creator.create();
		assertEquals(0, circle.getFaceCount());
	}

	@Test
	public void testFaceCountNGon() {
		creator.setFillType(FillType.N_GON);
		Mesh3D nGon = creator.create();
		assertEquals(1, nGon.getFaceCount());
	}

	@Test
	public void testFaceCountTriangleFan() {
		creator.setFillType(FillType.TRIANGLE_FAN);
		Mesh3D triangleFan = creator.create();
		assertEquals(32, triangleFan.getFaceCount());
	}

	@Test
	public void testFaceCountNothing() {
		creator.setFillType(FillType.TRIANGLE_FAN);
		creator.setFillType(FillType.NOTHING);
		Mesh3D circle = creator.create();
		assertEquals(0, circle.getFaceCount());
	}

	@ParameterizedTest
	@ValueSource(ints = { 4, 22, 31, 102 })
	public void testFaceCountSetVerticesNGon(int vertices) {
		creator.setVertices(vertices);
		creator.setFillType(FillType.N_GON);
		Mesh3D nGon = creator.create();
		assertEquals(1, nGon.getFaceCount());
	}

	@ParameterizedTest
	@ValueSource(ints = { 3, 10, 19, 31, 122 })
	public void testFaceCountSetVerticesTriangleFan(int vertices) {
		creator.setVertices(vertices);
		creator.setFillType(FillType.TRIANGLE_FAN);
		Mesh3D triangleFan = creator.create();
		assertEquals(vertices, triangleFan.getFaceCount());
	}

	@ParameterizedTest
	@ValueSource(ints = { 4, 7, 23, 56, 200 })
	public void testFaceCountSetVerticesNothing(int vertices) {
		creator.setVertices(vertices);
		creator.setFillType(FillType.N_GON);
		creator.setFillType(FillType.NOTHING);
		Mesh3D circle = creator.create();
		assertEquals(0, circle.getFaceCount());
	}

	@ParameterizedTest
	@ValueSource(ints = { 3, 8, 25, 66, 222 })
	public void testVertexCountTriangleFan(int vertices) {
		creator.setFillType(FillType.TRIANGLE_FAN);
		creator.setVertices(vertices);
		Mesh3D triangleFan = creator.create();
		assertEquals(vertices + 1, triangleFan.getVertexCount());
	}

	@ParameterizedTest
	@ValueSource(ints = { 4, 9, 37, 87, 102 })
	public void testVertexCountNGon(int vertices) {
		creator.setFillType(FillType.N_GON);
		creator.setVertices(vertices);
		Mesh3D nGon = creator.create();
		assertEquals(vertices, nGon.getVertexCount());
	}

	@ParameterizedTest
	@ValueSource(ints = { 8, 12, 20, 90, 210 })
	public void testVertexCountNothing(int vertices) {
		creator.setFillType(FillType.N_GON);
		creator.setFillType(FillType.NOTHING);
		creator.setVertices(vertices);
		Mesh3D circle = creator.create();
		assertEquals(vertices, circle.getVertexCount());
	}

	@ParameterizedTest
	@ValueSource(ints = { 4, 11, 14, 79 })
	public void testFaceIndicesNGon(int vertices) {
		creator.setFillType(FillType.N_GON);
		creator.setVertices(vertices);
		Mesh3D nGon = creator.create();
		Face3D face = nGon.getFaceAt(0);
		assertEquals(vertices, face.indices.length);
		for (int i = 0; i < vertices; i++) {
			assertEquals(i, face.indices[i]);
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { 4, 9, 37, 87, 102 })
	public void testFaceIndicesTriangleFan(int vertices) {
		creator.setFillType(FillType.TRIANGLE_FAN);
		creator.setVertices(vertices);
		Mesh3D triangleFan = creator.create();

		for (int i = 0; i < triangleFan.getFaceCount(); i++) {
			Face3D face = triangleFan.getFaceAt(i);
			assertEquals(3, face.indices.length);
			assertEquals(i, face.indices[0]);
			assertEquals(vertices, face.indices[2]);
			assertEquals((i + 1) % vertices, face.indices[1]);
		}
	}

	@ParameterizedTest
	@ValueSource(floats = { 0, 9, -23.4f, 0.222f, -102.234f })
	public void testCircleVertices(float centerY) {
		Vector3f[] expected = { 
				new Vector3f(1, centerY, 0), 
				new Vector3f(0, centerY, 1), 
				new Vector3f(-1, centerY, 0),
				new Vector3f(0, centerY, -1) 
				};

		CircleCreator creator = new CircleCreator();
		creator.setFillType(FillType.NOTHING);
		creator.setRadius(1);
		creator.setVertices(4);
		creator.setCenterY(centerY);
		Mesh3D circle = creator.create();

		for (int i = 0; i < circle.getVertexCount(); i++) {
			Vector3f expectedVertex = expected[i];
			Vector3f actualVertex = circle.getVertexAt(i);

			float tolerance = 0.0001f;
			assertEquals(expectedVertex.x, actualVertex.x, tolerance);
			assertEquals(expectedVertex.y, actualVertex.y, tolerance);
			assertEquals(expectedVertex.z, actualVertex.z, tolerance);
		}
	}

}
