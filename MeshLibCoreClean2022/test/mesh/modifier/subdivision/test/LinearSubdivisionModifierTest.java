package mesh.modifier.subdivision.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.subdivision.LinearSubdivisionModifier;
import mesh.util.Bounds3;
import util.MeshTest;

public class LinearSubdivisionModifierTest {

	private LinearSubdivisionModifier modifier;
	private Mesh3D cubeMesh;

	@Before
	public void setUp() {
		modifier = new LinearSubdivisionModifier();
		cubeMesh = new CubeCreator().create();
	}

	@Test
	public void modifierImplementsModifierInterface() {
		Assert.assertTrue(modifier instanceof IMeshModifier);
	}

	@Test
	public void returnsReferenceToOriginalMeshInstance() {
		Mesh3D result = modifier.modify(cubeMesh);
		Assert.assertTrue(cubeMesh == result);
	}

	@Test
	public void subdividedCubeHasTwentyFourFaces() {
		modifier.modify(cubeMesh);
		Assert.assertEquals(24, cubeMesh.getFaceCount());
	}

	@Test
	public void twiceSubdividedCubeHasNintySixFaces() {
		modifier.modify(cubeMesh);
		modifier.modify(cubeMesh);
		Assert.assertEquals(96, cubeMesh.getFaceCount());
	}

	@Test
	public void subdividedCubeHasTwentySixVertices() {
		modifier.modify(cubeMesh);
		Assert.assertEquals(26, cubeMesh.getVertexCount());
	}

	@Test
	public void twiceSubdividedCubeHasNintyEightVertices() {
		modifier.modify(cubeMesh);
		modifier.modify(cubeMesh);
		Assert.assertEquals(98, cubeMesh.getVertexCount());
	}

	@Test
	public void subdividedCubeIsManifold() {
		modifier.modify(cubeMesh);
		MeshTest.assertIsManifold(cubeMesh);
	}

	@Test
	public void subdividedCubeHasNoLooseVertices() {
		modifier.modify(cubeMesh);
		MeshTest.assertMeshHasNoLooseVertices(cubeMesh);
	}

	@Test
	public void twiceSubdividedCubeHasNoLooseVertices() {
		modifier.modify(cubeMesh);
		modifier.modify(cubeMesh);
		MeshTest.assertMeshHasNoLooseVertices(cubeMesh);
	}

	@Test
	public void subdividedCubeHasFaceNormalPointOutward() {
		modifier.modify(cubeMesh);
		MeshTest.assertNormalsPointOutwards(cubeMesh);
	}

	@Test
	public void subdividedCubeContainsVerticesAtOriginalCubePositions() {
		Mesh3D originalCube = new CubeCreator().create();
		Mesh3D subdividedCube = new CubeCreator().create();
		modifier.modify(subdividedCube);
		for (Vector3f v : originalCube.getVertices()) {
			Assert.assertTrue(subdividedCube.getVertices().contains(v));
		}
	}

	@Test
	public void subdividedCubeContainsFaceCenterVertices() {
		Mesh3D originalCube = new CubeCreator().create();
		Mesh3D subdividedCube = new CubeCreator().create();
		modifier.modify(subdividedCube);
		for (Face3D face : originalCube.getFaces()) {
			Vector3f center = originalCube.calculateFaceCenter(face);
			Assert.assertTrue(subdividedCube.getVertices().contains(center));
		}
	}

	@Test
	public void subdividedCuveHasNoDuplicatedFaces() {
		modifier.modify(cubeMesh);
		MeshTest.assertMeshHasNoDuplicatedFaces(cubeMesh);
	}

	@Test
	public void subdividedCubeConsistsOfQuadsOnly() {
		modifier.modify(cubeMesh);
		int faceCount = cubeMesh.getFaceCount();
		MeshTest.assertQuadCountEquals(cubeMesh, faceCount);
	}

	@Test
	public void subdividedIcosahedronConsistsOfTrianglesOnly() {
		Mesh3D mesh = new IcosahedronCreator().create();
		modifier.modify(mesh);
		int faceCount = mesh.getFaceCount();
		MeshTest.assertTriangleCountEquals(mesh, faceCount);
	}

	@Test
	public void subdividedIcosahedronHasEightyFaces() {
		Mesh3D mesh = new IcosahedronCreator().create();
		modifier.modify(mesh);
		Assert.assertEquals(80, mesh.getFaceCount());
	}

	@Test
	public void subdividedIcosahedronHasFourtyTwoVertices() {
		Mesh3D mesh = new IcosahedronCreator().create();
		modifier.modify(mesh);
		Assert.assertEquals(42, mesh.getVertexCount());
	}

	@Test
	public void subdividedIcosahedronIsManifold() {
		Mesh3D mesh = new IcosahedronCreator().create();
		modifier.modify(mesh);
		MeshTest.assertIsManifold(mesh);
	}

	@Test
	public void subdividedIcosahedronHasNoLooseVertices() {
		Mesh3D mesh = new IcosahedronCreator().create();
		modifier.modify(mesh);
		MeshTest.assertMeshHasNoLooseVertices(mesh);
	}

	@Test
	public void subdividedCubeIsWithinBounds() {
		modifier.modify(cubeMesh);
		Bounds3 bounds = cubeMesh.calculateBounds();
		Assert.assertEquals(2, bounds.getWidth(), 0);
		Assert.assertEquals(2, bounds.getHeight(), 0);
		Assert.assertEquals(2, bounds.getDepth(), 0);
	}

	@Test
	public void everyFaceContainsCenterVertexIndex() {
		Mesh3D plane = new PlaneCreator().create();
		Face3D face = plane.getFaceAt(0);
		Vector3f center = plane.calculateFaceCenter(face);
		modifier.modify(plane);
		for (Face3D f : plane.getFaces()) {
			boolean containsCenter = false;
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = plane.getVertexAt(f.indices[i]);
				containsCenter |= v.equals(center);
			}
			Assert.assertTrue(containsCenter);
		}
	}

	@Test
	public void removingCenterAndEdgePointsLeavesOriginalVerticesQuadCase() {
		Mesh3D originalCube = new CubeCreator().create();
		modifier.modify(cubeMesh);
		List<Vector3f> vertices = cubeMesh.getVertices();
		for (Face3D face : originalCube.getFaces()) {
			int length = face.indices.length;
			Vector3f center = originalCube.calculateFaceCenter(face);
			vertices.remove(center);
			for (int i = 0; i < length; i++) {
				Vector3f from = originalCube.getVertexAt(face.indices[i]);
				Vector3f to = originalCube.getVertexAt(face.indices[(i + 1) % length]);
				Vector3f edgePoint = from.add(to).mult(0.5f);
				vertices.remove(edgePoint);
			}
		}
		Assert.assertEquals(8, vertices.size());
		for (Vector3f v : originalCube.getVertices()) {
			Assert.assertTrue(vertices.contains(v));
		}
	}
	
}
