package test;

import org.junit.Assert;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.util.Bounds3;

public class BoundsTranslateTest {

    @Test
    public void boundsWith() {
        Mesh3D cube = new CubeCreator().create();
        Bounds3 bounds = cube.calculateBounds();
        Assert.assertEquals(2, bounds.getWidth(), 0);
    }

    @Test
    public void boundsHeight() {
        Mesh3D cube = new CubeCreator().create();
        Bounds3 bounds = cube.calculateBounds();
        Assert.assertEquals(2, bounds.getHeight(), 0);
    }

    @Test
    public void boundsDepth() {
        Mesh3D cube = new CubeCreator().create();
        Bounds3 bounds = cube.calculateBounds();
        Assert.assertEquals(2, bounds.getDepth(), 0);
    }

    @Test
    public void minX() {
        Mesh3D cube = new CubeCreator().create();
        Bounds3 bounds = cube.calculateBounds();
        Assert.assertEquals(-1, bounds.getMinX(), 0);
    }

    @Test
    public void minXTranslate() {
        Mesh3D cube = new CubeCreator().create();
        cube.translateX(1);
        Bounds3 bounds = cube.calculateBounds();
        Assert.assertEquals(0, bounds.getMinX(), 0);
    }

    @Test
    public void boundsWithTranslate() {
        Mesh3D cube = new CubeCreator().create();
        cube.translateX(1);
        Bounds3 bounds = cube.calculateBounds();
        Assert.assertEquals(2, bounds.getWidth(), 0);
    }

    @Test
    public void boundsHeightTranslate() {
        Mesh3D cube = new CubeCreator().create();
        cube.translateX(1);
        Bounds3 bounds = cube.calculateBounds();
        Assert.assertEquals(2, bounds.getHeight(), 0);
    }

    @Test
    public void boundsDepthTranslate() {
        Mesh3D cube = new CubeCreator().create();
        cube.translateX(1);
        Bounds3 bounds = cube.calculateBounds();
        Assert.assertEquals(2, bounds.getDepth(), 0);
    }

}
