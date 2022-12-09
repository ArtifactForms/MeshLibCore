package util;

import java.util.HashSet;

import org.junit.Assert;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.test.manifold.ManifoldTest;
import mesh.selection.FaceSelection;

public class MeshTest {

	private static int calculateEdgeCount(Mesh3D mesh) {
		HashSet<Edge3D> edges = new HashSet<Edge3D>();
		for (Face3D face: mesh.faces) {
			for (int i = 0; i < face.indices.length; i++) {
				int fromIndex  = face.indices[i];
				int toIndex = face.indices[(i + 1) % face.indices.length];
				Edge3D edge = new Edge3D(fromIndex, toIndex);
				Edge3D pair = edge.createPair();
				if (!edges.contains(pair))
					edges.add(edge);
			}
		}
		return edges.size();
	}
	
	public static void assertEdgeCountEquals(Mesh3D mesh, int expectedEdgeCount) {
		Assert.assertEquals(calculateEdgeCount(mesh), expectedEdgeCount);
	}
	
	public static void assertFulfillsEulerCharacteristic(Mesh3D mesh) {
		int edgeCount = MeshTest.calculateEdgeCount(mesh);
		int faceCount = mesh.getFaceCount();
		int vertexCount = mesh.getVertexCount();
		int actual = vertexCount - edgeCount + faceCount;
		Assert.assertEquals(2, actual);
	}
	
	public static void assertEveryEgdgeHasALengthOf(Mesh3D mesh, float expectedEdgeLength) {
		for (Face3D face: mesh.faces) {
			for (int i = 0; i < face.indices.length; i++) {
				Vector3f v0 = mesh.vertices.get(face.indices[i]);
				Vector3f v1 = mesh.vertices.get(face.indices[(i + 1) % face.indices.length]);
				Assert.assertEquals(expectedEdgeLength, v0.distance(v1), 0);
			}
		}
	}
	
	public static void assertTriangleCountEquals(Mesh3D mesh, int expectedTriangleCount) {
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectTriangles();
		Assert.assertEquals(expectedTriangleCount, selection.size());
	}
	
	public static void assertQuadCountEquals(Mesh3D mesh, int expectedQuadCount) {
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectQuads();
		Assert.assertEquals(expectedQuadCount, selection.size());
	}
	
	public static void assertIsManifold(Mesh3D mesh) {
		new ManifoldTest(mesh).assertIsManifold();
	}
	
}
