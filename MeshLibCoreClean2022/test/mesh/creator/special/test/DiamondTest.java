package mesh.creator.special.test;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.creator.special.DiamondCreator;
import mesh.selection.FaceSelection;
import util.MeshTest;

public class DiamondTest {
	
	@Test
	public void creatorImplementsCreatorInterface() {
		DiamondCreator creator = new DiamondCreator();
		Assert.assertTrue(creator instanceof IMeshCreator);
	}
	
	@Test
	public void getSegmentsReturnsDefaultValue() {
		int expected = 32;
		Assert.assertEquals(expected, new DiamondCreator().getSegments());
	}
	
	@Test
	public void getSetSegments() {
		int expected = 145;
		DiamondCreator creator = new DiamondCreator();
		creator.setSegments(expected);
		Assert.assertEquals(expected, creator.getSegments());
	}
	
	@Test
	public void getGirdleRadiusReturnsDefaultValue() {
		float expected = 1;
		float actual = new DiamondCreator().getGirdleRadius();
		Assert.assertEquals(expected, actual, 0);
	}
	
	@Test
	public void getSetGirdleRadius() {
		float expected = 0.9562f;
		DiamondCreator creator = new DiamondCreator();
		creator.setGirdleRadius(expected);
		Assert.assertEquals(expected, creator.getGirdleRadius(), 0);
	}
	
	@Test
	public void getTableRadiusReturnsDefaultValue() {
		float expected = 0.6f;
		DiamondCreator creator = new DiamondCreator();
		Assert.assertEquals(expected, creator.getTableRadius(), 0);
	}
	
	@Test
	public void getSetTableRadius() {
		float expected = 0.65416f;
		DiamondCreator creator = new DiamondCreator();
		creator.setTableRadius(expected);
		Assert.assertEquals(expected, creator.getTableRadius(), 0);
	}
	
	@Test
	public void getCrownHeightReturnsDefaulValue() {
		float expected = 0.35f;
		DiamondCreator creator = new DiamondCreator();
		Assert.assertEquals(expected, creator.getCrownHeight(), 0);
	}
	
	@Test
	public void getSetCrownHeight() {
		float expected = 67.45f;
		DiamondCreator creator = new DiamondCreator();
		creator.setCrownHeight(expected);
		Assert.assertEquals(expected, creator.getCrownHeight(), 0);
	}
	
	@Test
	public void getPavillionReturnsDefaultValue() {
		float expected = 0.8f;
		DiamondCreator creator = new DiamondCreator();
		Assert.assertEquals(expected, creator.getPavillionHeight(), 0);
	}
	
	@Test
	public void getSetPavillionHeight() {
		float expected = 9.2536f;
		DiamondCreator creator = new DiamondCreator();
		creator.setPavillionHeight(expected);
		Assert.assertEquals(expected, creator.getPavillionHeight(), 0);
	}
	
	@Test
	public void vertexCountIsSixtyFiveByDefault() {
		int expected = 65;
		Mesh3D mesh = new DiamondCreator().create();
		Assert.assertEquals(expected, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshContainsVertexAtCenterPavillionHeight() {
		Vector3f expected = new Vector3f(0, 0.8f, 0);
		Mesh3D mesh = new DiamondCreator().create();
		Assert.assertTrue(mesh.vertices.contains(expected));
	}
	
	@Test
	public void createdMeshContainsVertexAtCenzerOfSpecifiedPavillionHeight() {
		float pavillionHeight = 9.45f;
		Vector3f expected = new Vector3f(0, pavillionHeight, 0);
		DiamondCreator creator = new DiamondCreator();
		creator.setPavillionHeight(pavillionHeight);
		Mesh3D mesh = creator.create();
		Assert.assertTrue(mesh.vertices.contains(expected));
	}
	
	@Test
	public void createdMeshContainsVerticesOfGirdleCircle() {
		int vertices = 32;
		float radius = 1;
		CircleCreator creator = new CircleCreator();
		creator.setRadius(radius);
		creator.setVertices(vertices);
		Mesh3D circle = creator.create();
		Mesh3D mesh = new DiamondCreator().create();
		for (Vector3f v : circle.vertices) {
			Assert.assertTrue(mesh.vertices.contains(v));
		}
	}
	
	@Test
	public void createdMeshContainsVerticesOfSpecifiedGirdleCircle() {
		int vertices = 21;
		float radius = 1.9654f;
		CircleCreator creator = new CircleCreator();
		creator.setRadius(radius);
		creator.setVertices(vertices);
		Mesh3D circle = creator.create();
		DiamondCreator creator2 = new DiamondCreator();
		creator2.setGirdleRadius(radius);
		creator2.setSegments(vertices);
		Mesh3D mesh = creator2.create();
		for (Vector3f v : circle.vertices) {
			Assert.assertTrue(mesh.vertices.contains(v));
		}
	}
	
	@Test
	public void vertexCountIsTwoTimesSegmentsPlusOne() {
		DiamondCreator creator = new DiamondCreator();
		creator.setSegments(6);
		Mesh3D mesh = creator.create();
		Assert.assertEquals(13, mesh.getVertexCount());
	}
	
	@Test
	public void defaultFaceCountIsSixtyFive() {
		int expected = 65;
		Mesh3D mesh = new DiamondCreator().create();
		Assert.assertEquals(expected, mesh.getFaceCount());
	}
	
	@Test
	public void faceCountIsTwoTimesVertexCountPlusOne() {
		int expected = 23;
		DiamondCreator creator = new DiamondCreator();
		creator.setSegments(11);
		Mesh3D mesh = creator.create();
		Assert.assertEquals(expected, mesh.getFaceCount());
	}
	
	@Test
	public void meshContainsFaceWithVertexCountEqualsToSegments() {
		DiamondCreator creator = new DiamondCreator();
		Mesh3D mesh = creator.create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectByVertexCount(32);
		Assert.assertEquals(1, selection.size());
	}
	
	@Test
	public void meshContainsFaceWithVertexCountEqualsToSpecifiedSegments() {
		int segments = 19;
		DiamondCreator creator = new DiamondCreator();
		creator.setSegments(segments);
		Mesh3D mesh = creator.create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectByVertexCount(segments);
		Assert.assertEquals(1, selection.size());
	}
	
	@Test
	public void meshContainsTableCircleVertices() {
		int segments = 32;
		float radius = 0.6f;
		float crownHeight = 0.35f;
		
		CircleCreator creator = new CircleCreator();
		creator.setVertices(segments);
		creator.setRadius(radius);
		creator.setCenterY(-crownHeight);
		
		Mesh3D circle = creator.create();
		Mesh3D diamond = new DiamondCreator().create();
		
		for (Vector3f v : circle.getVertices()) {
			Assert.assertTrue(diamond.getVertices().contains(v));
		}
	}
	
	@Test
	public void meshContainsVerticesOfSpecifiedTable() {
		int segments = 16;
		float radius = 0.45f;
		float crownHeight = 0.812f;
		
		CircleCreator creator = new CircleCreator();
		creator.setVertices(segments);
		creator.setRadius(radius);
		creator.setCenterY(-crownHeight);
		Mesh3D circle = creator.create();
		
		DiamondCreator creator2 = new DiamondCreator();
		creator2.setSegments(segments);
		creator2.setTableRadius(radius);
		creator2.setCrownHeight(crownHeight);
		Mesh3D diamond = creator2.create();
		
		for (Vector3f v : circle.getVertices()) {
			Assert.assertTrue(diamond.getVertices().contains(v));
		}
	}
	
	@Test
	public void meshContainsThirtyTwoQuadsByDefault() {
		Mesh3D mesh = new DiamondCreator().create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectQuads();
		Assert.assertEquals(32, selection.size());
	}

	@Test
	public void meshContainsThirtyTwoTriangularFacesByDefault() {
		Mesh3D mesh = new DiamondCreator().create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectTriangles();
		Assert.assertEquals(32, selection.size());
	}
	
	@Test
	public void eachTriangularFaceContainsPavillionVertexIndex() {
		float pavillionHeight = 0.8f; 
		Vector3f pavillionVertex = new Vector3f(0, pavillionHeight, 0);
		Mesh3D mesh = new DiamondCreator().create();
		int pavillionVertexIndex = mesh.getVertices().indexOf(pavillionVertex);
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectTriangles();
		for (Face3D face : selection.getFaces()) {
			MeshTest.assertFaceContainsVertexIndex(face, pavillionVertexIndex);
		}
	}
	
	@Test
	public void eachTriangularFaceContainsGirdleVertexIndex() {
		CircleCreator creator = new CircleCreator();
		creator.setRadius(1);
		creator.setVertices(32);
		Mesh3D circle = creator.create();
		Mesh3D mesh = new DiamondCreator().create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectTriangles();
		for (Face3D face : selection.getFaces()) {
			for (int i = 0; i < face.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(face.indices[i]);
				circle.vertices.remove(v);
			}
		}
		Assert.assertEquals(0, circle.getVertexCount());
	}
	
	@Test
	public void nGonConsistsOfTableVertices() {
		CircleCreator creator = new CircleCreator();
		creator.setRadius(0.6f);
		creator.setCenterY(-0.35f);
		creator.setVertices(32);
		Mesh3D circle = creator.create();
		Mesh3D mesh = new DiamondCreator().create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectByVertexCount(32);
		for (Face3D face : selection.getFaces()) {
			for (int i = 0; i < face.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(face.indices[i]);
				circle.vertices.remove(v);
			}
		}
		Assert.assertEquals(0, circle.getVertexCount());
	}
	
	@Test
	public void secondVertexIndexOfEachTriangleIsPavillionIndex() {
		Mesh3D mesh = new DiamondCreator().create();
		int pavillionIndex = mesh.vertices.indexOf(new Vector3f(0, 0.8f, 0));
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectTriangles();
		for (Face3D face : selection.getFaces()) {
			int actual = face.indices[1];
			Assert.assertEquals(pavillionIndex, actual);
		}
	}
	
	@Test
	public void firstIndexOfQuadFacesReferencesGirdleVertex() {
		CircleCreator creator = new CircleCreator();
		creator.setRadius(1);
		creator.setVertices(32);
		Mesh3D circle = creator.create();
		Mesh3D mesh = new DiamondCreator().create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectQuads();
		for (Face3D face : selection.getFaces()) {
			int index = face.indices[0];
			Vector3f v = mesh.getVertexAt(index);
			circle.vertices.remove(v);
		}
		Assert.assertEquals(0, circle.vertices.size());
	}
	
	@Test
	public void thirdIndexOfEachQuadReferencesTablesVertex() {
		CircleCreator creator = new CircleCreator();
		creator.setRadius(0.6f);
		creator.setVertices(32);
		creator.setCenterY(-0.35f);
		Mesh3D circle = creator.create();
		Mesh3D mesh = new DiamondCreator().create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectQuads();
		for (Face3D face : selection.getFaces()) {
			int index = face.indices[2];
			Vector3f v = mesh.getVertexAt(index);
			circle.vertices.remove(v);
		}
		Assert.assertEquals(0, circle.vertices.size());
	}
	
	@Test
	public void meshIsManifold() {
		MeshTest.assertIsManifold(new DiamondCreator().create());
	}
	
	@Test
	public void normalsPointOutwards() {
		Mesh3D mesh = new DiamondCreator().create();
		Vector3f center = new Vector3f();
		for (Face3D face : mesh.getFaces()) {
			Vector3f faceNormal = mesh.calculateFaceNormal(face);
			Vector3f faceCenter = mesh.calculateFaceCenter(face);
			Vector3f a = faceCenter.subtract(center);
			float dotProduct = faceNormal.dot(a);
			Assert.assertTrue(dotProduct >= 0);
		}
	}
	
	@Test
	public void thirdTriangleIndexIsLessThanSegmentsCount() {
		int segments = 65;
		DiamondCreator creator = new DiamondCreator();
		creator.setSegments(segments);
		Mesh3D mesh = creator.create();
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectTriangles();
		for (Face3D face : selection.getFaces()) {
			Assert.assertTrue(face.indices[2] < segments);
		}
	}
	
	@Test
	public void meshHasNoLooseVertices() {
		MeshTest.assertMeshHasNoLooseVertices(new DiamondCreator().create());
	}
	
	@Test
	public void meshHasNoDuplicatedFaces() {
		MeshTest.assertMeshHasNoDuplicatedFaces(new DiamondCreator().create());
	}
	
}
