package mesh.creator.primitives.test;

import java.util.HashSet;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.ArcCreator;

public class ArcCreatorTest {

    @Test
    public void createsMeshWithThirtyTwoVerticesByDefault() {
        ArcCreator creator = new ArcCreator();
        Mesh3D mesh = creator.create();
        Assert.assertEquals(32, mesh.getVertexCount());
    }

    @Test
    public void createsMeshWithZeroFacesByDefault() {
        ArcCreator creator = new ArcCreator();
        Mesh3D mesh = creator.create();
        Assert.assertEquals(0, mesh.getFaceCount());
    }

    @Test
    public void distanceFromOriginToEachCreatedVertexIsEqualToOneByDefault() {
        Vector3f origin = new Vector3f(0, 0, 0);
        ArcCreator creator = new ArcCreator();
        Mesh3D mesh = creator.create();
        for (Vector3f v : mesh.getVertices())
            Assert.assertEquals(1, origin.distance(v), 0.0000001f);
    }

    @Test
    public void distanceFromOriginToEachCreatedVertexIsEqualToRadius() {
        Random random = new Random();
        float randomRadius = random.nextFloat();
        Vector3f origin = new Vector3f(0, 0, 0);
        ArcCreator creator = new ArcCreator();
        creator.setRadius(randomRadius);
        Mesh3D mesh = creator.create();
        for (Vector3f v : mesh.getVertices())
            Assert.assertEquals(randomRadius, origin.distance(v), 0.0000001f);
    }

    @Test
    public void eachVertexIsDifferent() {
        ArcCreator creator = new ArcCreator();
        Mesh3D mesh = creator.create();
        HashSet<Vector3f> vertices = new HashSet<Vector3f>(mesh.getVertices());
        Assert.assertEquals(32, vertices.size());
    }

    @Test
    public void vertices() {
        ArcCreator creator = new ArcCreator();
        creator.setVertices(77);
        Mesh3D mesh = creator.create();
        Assert.assertEquals(77, mesh.getVertexCount());
    }

}
