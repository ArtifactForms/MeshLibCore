package mesh.modifier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.util.Bounds3;

public class FitToAABBTest {

    private FitToAABBModifier modifier;

    @Before
    public void setUp() {
        modifier = new FitToAABBModifier(0, 0, 0);
    }

    @Test
    public void implementsModifierInterface() {
        Assert.assertTrue(modifier instanceof IMeshModifier);
    }

    @Test
    public void returnsReferenceToOriginalMesh() {
        Mesh3D mesh = new CubeCreator().create();
        Mesh3D result = modifier.modify(mesh);
        Assert.assertTrue(mesh == result);
    }

    @Test
    public void modifyCube() {
        Mesh3D actual = new CubeCreator(3).create();
        Mesh3D expected = new CubeCreator(0.5f).create();
        new FitToAABBModifier(1, 1, 1).modify(actual);
        for (int i = 0; i < actual.getVertexCount(); i++) {
            Vector3f actualVertex = actual.getVertexAt(i);
            Vector3f expectedVertex = expected.getVertexAt(i);
            Assert.assertEquals(expectedVertex, actualVertex);
        }
    }

    @Test
    public void doesNotChangeProportionsBoundsOne() {
        Mesh3D actual = new BoxCreator(1, 2, 4).create();
        Mesh3D expected = new BoxCreator(0.25f, 0.5f, 1f).create();
        FitToAABBModifier modifier = new FitToAABBModifier(1, 1, 1);
        modifier.modify(actual);
        for (int i = 0; i < actual.getVertexCount(); i++) {
            Vector3f actualVertex = actual.getVertexAt(i);
            Vector3f expectedVertex = expected.getVertexAt(i);
            Assert.assertEquals(expectedVertex, actualVertex);
        }
    }

    @Test
    public void doesNotChangeProportionsDepthMax() {
        Mesh3D actual = new BoxCreator(1, 2, 4).create();
        Mesh3D expected = new BoxCreator(0.125f, 0.25f, 0.5f).create();
        FitToAABBModifier modifier = new FitToAABBModifier(0.5f, 0.5f, 0.5f);
        modifier.modify(actual);
        for (int i = 0; i < actual.getVertexCount(); i++) {
            Vector3f actualVertex = actual.getVertexAt(i);
            Vector3f expectedVertex = expected.getVertexAt(i);
            Assert.assertEquals(expectedVertex, actualVertex);
        }
    }

    @Test
    public void doesNotChangeProportionsWidthMax() {
        Mesh3D actual = new BoxCreator(4, 2, 1).create();
        Mesh3D expected = new BoxCreator(0.5f, 0.25f, 0.125f).create();
        FitToAABBModifier modifier = new FitToAABBModifier(0.5f, 0.5f, 0.5f);
        modifier.modify(actual);
        for (int i = 0; i < actual.getVertexCount(); i++) {
            Vector3f actualVertex = actual.getVertexAt(i);
            Vector3f expectedVertex = expected.getVertexAt(i);
            Assert.assertEquals(expectedVertex, actualVertex);
        }
    }

    @Test
    public void doesNotChangeProportionsWidthMaxReverse() {
        Mesh3D actual = new BoxCreator(0.5f, 0.25f, 0.125f).create();
        Mesh3D expected = new BoxCreator(4, 2, 1).create();
        FitToAABBModifier modifier = new FitToAABBModifier(4, 4, 4);
        modifier.modify(actual);
        for (int i = 0; i < actual.getVertexCount(); i++) {
            Vector3f actualVertex = actual.getVertexAt(i);
            Vector3f expectedVertex = expected.getVertexAt(i);
            Assert.assertEquals(expectedVertex, actualVertex);
        }
    }

    @Test
    public void doesNotChangeProportionsHeightMax() {
        Mesh3D actual = new BoxCreator(2, 4, 1).create();
        Mesh3D expected = new BoxCreator(0.25f, 0.5f, 0.125f).create();
        FitToAABBModifier modifier = new FitToAABBModifier(0.5f, 0.5f, 0.5f);
        modifier.modify(actual);
        for (int i = 0; i < actual.getVertexCount(); i++) {
            Vector3f actualVertex = actual.getVertexAt(i);
            Vector3f expectedVertex = expected.getVertexAt(i);
            Assert.assertEquals(expectedVertex, actualVertex);
        }
    }

    @Test
    public void randomMax() {
        float dim = 2;
        float width = Mathf.random(0, 10000f);
        float height = Mathf.random(0, 10000f);
        float depth = Mathf.random(0, 10000f);
        Mesh3D actual = new BoxCreator(width, height, depth).create();
        FitToAABBModifier modifier = new FitToAABBModifier(dim, dim, dim);
        modifier.modify(actual);
        Bounds3 bounds = actual.calculateBounds();
        float max = Mathf.max(bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
        Assert.assertEquals(dim, max, Mathf.ZERO_TOLERANCE);
    }

}
