package mesh.modifier;

import org.junit.Assert;
import org.junit.Test;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CapsuleCreator;
import mesh.creator.primitives.CubeCreator;

public class RemoveDoubleVerticesTest {

    @Test
    public void modifierImplementsModifierInterface() {
        Assert.assertTrue(new RemoveDoubleVerticesModifier() instanceof IMeshModifier);
    }

    @Test
    public void removeDoublesFromCube() {
        Mesh3D mesh = new CubeCreator().create();
        mesh.addVertices(new CubeCreator().create().getVertices());
        new RemoveDoubleVerticesModifier().modify(mesh);
        Assert.assertEquals(8, mesh.getVertexCount());
    }

    @Test
    public void removeDoublesFromCubeIndices() {
        Mesh3D originalMesh = new CubeCreator().create();
        Mesh3D modifiedMesh = new CubeCreator().create();
        modifiedMesh.append(new CubeCreator().create());
        for (int i = 0; i < originalMesh.getFaceCount(); i++) {
            Face3D originalFace = originalMesh.getFaceAt(i);
            Face3D modifiedFace = modifiedMesh.getFaceAt(i);
            for (int j = 0; j < originalFace.indices.length; j++) {
                int indexA = originalFace.indices[j];
                int indexB = modifiedFace.indices[j];
                Assert.assertEquals(indexA, indexB);
            }
        }
    }

    @Test
    public void removeDoublesFromCapsuleIndices() {
        Mesh3D originalMesh = new CapsuleCreator().create();
        Mesh3D modifiedMesh = new CapsuleCreator().create();
        modifiedMesh.append(new CapsuleCreator().create());
        for (int i = 0; i < originalMesh.getFaceCount(); i++) {
            Face3D originalFace = originalMesh.getFaceAt(i);
            Face3D modifiedFace = modifiedMesh.getFaceAt(i);
            for (int j = 0; j < originalFace.indices.length; j++) {
                int indexA = originalFace.indices[j];
                int indexB = modifiedFace.indices[j];
                Assert.assertEquals(indexA, indexB);
            }
        }
    }

}
