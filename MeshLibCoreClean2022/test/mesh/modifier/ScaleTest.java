package mesh.modifier;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.SegmentedCubeCreator;
import mesh.creator.primitives.UVSphereCreator;

public class ScaleTest {

    @Test
    public void modifierImplementsModifierInterface() {
        Assert.assertTrue(new ScaleModifier() instanceof IMeshModifier);
    }

    @Test
    public void returnsOriginalMeshReference() {
        Mesh3D mesh = new SegmentedCubeCreator().create();
        Mesh3D scaled = new ScaleModifier().modify(mesh);
        Assert.assertTrue(scaled == mesh);
    }

    @Test
    public void modifiesOriginalVerticesNoCopy() {
        Mesh3D mesh = new SegmentedCubeCreator().create();
        List<Vector3f> vertices = mesh.getVertices();
        new ScaleModifier().modify(mesh);
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f expected = vertices.get(i);
            Vector3f actual = vertices.get(i);
            Assert.assertTrue(expected == actual);
        }
    }

    @Test
    public void scaleXisOneByDefault() {
        ScaleModifier modifier = new ScaleModifier();
        Assert.assertEquals(1, modifier.getScaleX(), 0);
    }

    @Test
    public void scaleYisOneByDefault() {
        ScaleModifier modifier = new ScaleModifier();
        Assert.assertEquals(1, modifier.getScaleY(), 0);
    }

    @Test
    public void scaleZisOneByDefault() {
        ScaleModifier modifier = new ScaleModifier();
        Assert.assertEquals(1, modifier.getScaleZ(), 0);
    }

    @Test
    public void getSetScaleRandomValueX() {
        float randomScaleX = Mathf.random(-7000f, 7000f);
        ScaleModifier modifier = new ScaleModifier();
        modifier.setScaleX(randomScaleX);
        Assert.assertEquals(randomScaleX, modifier.getScaleX(), 0);
    }

    @Test
    public void getSetScaleRandomValueY() {
        float randomScaleY = Mathf.random(-24556f, 7234f);
        ScaleModifier modifier = new ScaleModifier();
        modifier.setScaleY(randomScaleY);
        Assert.assertEquals(randomScaleY, modifier.getScaleY(), 0);
    }

    @Test
    public void getSetScaleRandomValueZ() {
        float randomScaleZ = Mathf.random(-82734, 2934f);
        ScaleModifier modifier = new ScaleModifier();
        modifier.setScaleZ(randomScaleZ);
        Assert.assertEquals(randomScaleZ, modifier.getScaleZ(), 0);
    }

    @Test
    public void keepsScaleByDefault() {
        Mesh3D original = new UVSphereCreator().create();
        Mesh3D scaled = new UVSphereCreator().create();
        new ScaleModifier().modify(scaled);
        for (int i = 0; i < original.getVertexCount(); i++) {
            Vector3f expected = original.getVertexAt(i);
            Vector3f actual = scaled.getVertexAt(i);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void scaleCubeRandomTestX() {
        float scaleX = Mathf.random(-1000f, 1000f);
        float scaleY = Mathf.random(-1000f, 1000f);
        float scaleZ = Mathf.random(-1000f, 1000f);
        Mesh3D original = new CubeCreator().create();
        Mesh3D scaled = new CubeCreator().create();
        new ScaleModifier(scaleX, scaleY, scaleZ).modify(scaled);
        for (int i = 0; i < original.getVertexCount(); i++) {
            Vector3f originalVertex = original.getVertexAt(i);
            Vector3f scaledVertex = scaled.getVertexAt(i);
            float expectedX = originalVertex.getX() * scaleX;
            float actaual = scaledVertex.getX();
            Assert.assertEquals(expectedX, actaual, 0);
        }
    }

    @Test
    public void scaleCubeRandomTestY() {
        float scaleX = Mathf.random(-1000f, 1000f);
        float scaleY = Mathf.random(-1000f, 1000f);
        float scaleZ = Mathf.random(-1000f, 1000f);
        Mesh3D original = new CubeCreator().create();
        Mesh3D scaled = new CubeCreator().create();
        new ScaleModifier(scaleX, scaleY, scaleZ).modify(scaled);
        for (int i = 0; i < original.getVertexCount(); i++) {
            Vector3f originalVertex = original.getVertexAt(i);
            Vector3f scaledVertex = scaled.getVertexAt(i);
            float expectedY = originalVertex.getY() * scaleY;
            float actaual = scaledVertex.getY();
            Assert.assertEquals(expectedY, actaual, 0);
        }
    }

    @Test
    public void scaleCubeRandomTestZ() {
        float scaleX = Mathf.random(-1000f, 1000f);
        float scaleY = Mathf.random(-1000f, 1000f);
        float scaleZ = Mathf.random(-1000f, 1000f);
        Mesh3D original = new CubeCreator().create();
        Mesh3D scaled = new CubeCreator().create();
        new ScaleModifier(scaleX, scaleY, scaleZ).modify(scaled);
        for (int i = 0; i < original.getVertexCount(); i++) {
            Vector3f originalVertex = original.getVertexAt(i);
            Vector3f scaledVertex = scaled.getVertexAt(i);
            float expectedZ = originalVertex.getZ() * scaleZ;
            float actaual = scaledVertex.getZ();
            Assert.assertEquals(expectedZ, actaual, 0);
        }
    }

    @Test
    public void scaleCubeRandomScale() {
        float scale = Mathf.random(-25417, 2949434f);
        Mesh3D original = new CubeCreator().create();
        Mesh3D scaled = new CubeCreator().create();
        new ScaleModifier(scale).modify(scaled);
        for (int i = 0; i < original.getVertexCount(); i++) {
            Vector3f originalVertex = original.getVertexAt(i);
            Vector3f scaledVertex = scaled.getVertexAt(i);
            Assert.assertEquals(originalVertex.mult(scale), scaledVertex);
        }
    }

}
