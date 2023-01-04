package mesh.creator.primitives.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.ConeCreator;
import mesh.selection.FaceSelection;
import util.MeshTest;

public class ConeTest {

	private ConeCreator creator;
	private Mesh3D cone;

	@Before
	public void setUp() {
		creator = new ConeCreator();
		cone = creator.create();
	}

	@Test
	public void getRotationSegmentsReturnsThirtyTwoByDefault() {
		int expected = 32;
		int actual = creator.getRotationSegments();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void getHeightSegmentsReturnsTenByDefault() {
		int expected = 10;
		int actual = creator.getHeightSegments();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void getTopRadiusReturnsZeroByDefault() {
		float expected = 0;
		float actual = creator.getTopRadius();
		Assert.assertEquals(expected, actual, 0);
	}

	@Test
	public void getBottomRadiusReturnsOneByDefault() {
		float expected = 1;
		float actual = creator.getBottomRadius();
		Assert.assertEquals(expected, actual, 0);
	}

	@Test
	public void getHeightReturnsTwoByDefault() {
		float expected = 2;
		float actual = creator.getHeight();
		Assert.assertEquals(expected, actual, 0);
	}

	@Test
	public void createdMeshHasExpectedVertexCount() {
		int heightSegments = 10;
		int rotationSegments = 32;
		int expectedVertexCount = heightSegments * rotationSegments + 1;
		MeshTest.assertVertexCountEquals(cone, expectedVertexCount);
	}

	@Test
	public void createdMeshIsManifold() {
		MeshTest.assertIsManifold(cone);
	}

	@Test
	public void createdMeshFulfillsEulerCharacteristic() {
		MeshTest.assertFulfillsEulerCharacteristic(cone);
	}

	@Test
	public void createdMeshHasNoLooseVertices() {
		MeshTest.assertMeshHasNoLooseVertices(cone);
	}

	@Test
	public void createdMeshHasExpectedTriangleCount() {
		MeshTest.assertTriangleCountEquals(cone, 32);
	}

	@Test
	public void createdMeshHasNoDuplicatedFaces() {
		MeshTest.assertMeshHasNoDuplicatedFaces(cone);
	}

	@Test
	public void createdMeshHasExpectedQuadCount() {
		int heightSegments = 10;
		int rotationSegments = 32;
		int expectedQuadCount = (heightSegments - 1) * rotationSegments;
		MeshTest.assertQuadCountEquals(cone, expectedQuadCount);
	}

	@Test
	public void createdMeshHasOneNgon() {
		FaceSelection selection = new FaceSelection(cone);
		selection.selectByVertexCount(32);
		Assert.assertEquals(1, selection.getFaces().size());
	}

	@Test
	public void bottomFaceIsNgon() {
		FaceSelection selection = new FaceSelection(cone);
		selection.selectByVertexCount(32);
		for (Face3D face : selection.getFaces()) {
			for (int i = 0; i < face.indices.length; i++) {
				Vector3f v = cone.getVertexAt(face.indices[i]);
				Assert.assertEquals(1, v.getY(), 0);
			}
		}
	}

	@Test
	public void normalsPointOutwards() {
		Vector3f center = new Vector3f();
		for (Face3D face : cone.getFaces()) {
			Vector3f faceNormal = cone.calculateFaceNormal(face);
			Vector3f faceCenter = cone.calculateFaceCenter(face);
			Vector3f a = faceCenter.subtract(center);
			float dotProduct = faceNormal.dot(a);
			Assert.assertTrue(dotProduct >= 0);
		}
	}

	@Test
	public void nGonCountIsTwoIfTopRadiusIsAlsoGreaterThanZero() {
		creator.setTopRadius(2);
		Mesh3D cone = creator.create();
		FaceSelection selection = new FaceSelection(cone);
		selection.selectByVertexCount(32);
		Assert.assertEquals(2, selection.size());
	}

	@Test
	public void oneFaceWithExpectedBottomFaceNormal() {
		float delta = Mathf.ZERO_TOLERANCE;
		FaceSelection selection = new FaceSelection(cone);
		selection.selectSimilarNormal(new Vector3f(0, 1, 0), delta);
		Assert.assertEquals(1, selection.size());
	}

	@Test
	public void onlyOneBottomFace() {
		FaceSelection selection = new FaceSelection(cone);
		selection.selectBottomFaces();
		Assert.assertEquals(1, selection.size());
	}

	@Test
	public void meshWithNoVerticesIsCreatedIfRadiiAreZero() {
		ConeCreator creator = new ConeCreator();
		creator.setTopRadius(0);
		creator.setBottomRadius(0);
		Mesh3D cone = creator.create();
		Assert.assertEquals(0, cone.getVertexCount());
	}

	@Test
	public void meshWithNoFacesIsCreatedIfRadiiAreZero() {
		ConeCreator creator = new ConeCreator();
		creator.setTopRadius(0);
		creator.setBottomRadius(0);
		Mesh3D cone = creator.create();
		Assert.assertEquals(0, cone.getFaceCount());
	}

	@Test
	public void meshWithNoVerticesIsCreatedIfHeightIsZero() {
		ConeCreator creator = new ConeCreator();
		creator.setHeight(0);
		Mesh3D cone = creator.create();
		Assert.assertEquals(0, cone.getVertexCount());
	}

	@Test
	public void meshWithNoFacesIsCreatedIfHeight() {
		ConeCreator creator = new ConeCreator();
		creator.setHeight(0);
		Mesh3D cone = creator.create();
		Assert.assertEquals(0, cone.getFaceCount());
	}

	@Test
	public void meshWithNoVerticesIsCreatedIfHeightSegmentsAreZero() {
		ConeCreator creator = new ConeCreator();
		creator.setHeightSegments(0);
		Mesh3D cone = creator.create();
		Assert.assertEquals(0, cone.getVertexCount());
	}

	@Test
	public void meshWithNoFacesIsCreatedIfHeightSegmentsAreZero() {
		ConeCreator creator = new ConeCreator();
		creator.setHeightSegments(0);
		Mesh3D cone = creator.create();
		Assert.assertEquals(0, cone.getFaceCount());
	}

	@Test
	public void meshWithNoVerticesIsCreatedIfRotationsSegmentsAreZero() {
		ConeCreator creator = new ConeCreator();
		creator.setRotationSegments(0);
		Mesh3D cone = creator.create();
		Assert.assertEquals(0, cone.getVertexCount());
	}

	@Test
	public void meshWithNoFacesIsCreatedIfRotationSegmentsAreZero() {
		ConeCreator creator = new ConeCreator();
		creator.setRotationSegments(0);
		Mesh3D cone = creator.create();
		Assert.assertEquals(0, cone.getFaceCount());
	}

}
