package util;

import org.junit.Assert;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class MeshCompare {

    public static void assertFacesAreEqual(Mesh3D expected, Mesh3D actual) {
        for (int j = 0; j < expected.getFaceCount(); j++) {
            Face3D expectedFace = expected.getFaceAt(j);
            Face3D actualFace = actual.getFaceAt(j);
            for (int index = 0; index < expectedFace.indices.length; index++) {
                Assert.assertEquals(expectedFace.indices[index],
                        actualFace.indices[index]);
            }
        }
    }

    public static void assertVerticesAreEqual(Mesh3D expected, Mesh3D actual,
            float delta) {
        for (int j = 0; j < expected.getVertexCount(); j++) {
            Vector3f expectedVertex = expected.getVertexAt(j);
            Vector3f actualVertex = actual.getVertexAt(j);
            Assert.assertEquals(expectedVertex.getX(), actualVertex.getX(),
                    delta);
            Assert.assertEquals(expectedVertex.getY(), actualVertex.getY(),
                    delta);
            Assert.assertEquals(expectedVertex.getZ(), actualVertex.getZ(),
                    delta);
        }
    }

}
