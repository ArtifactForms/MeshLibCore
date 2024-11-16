package mesh.creator.test.manifold;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.creator.primitives.CapsuleCreator;
import mesh.creator.primitives.ConeCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.CylinderCreator;
import mesh.creator.primitives.DoubleConeCreator;
import mesh.creator.primitives.FlatTopPyramidCreator;
import mesh.creator.primitives.HalfUVSphere;
import mesh.creator.primitives.HelixCreator;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.creator.primitives.QuadCapCylinderCreator;
import mesh.creator.primitives.QuadSphereCreator;
import mesh.creator.primitives.SegmentedBoxCreator;
import mesh.creator.primitives.SegmentedCylinderCreator;
import mesh.creator.primitives.SegmentedTubeCreator;
import mesh.creator.primitives.SolidArcCreator;
import mesh.creator.primitives.SquareBasedPyramidCreator;
import mesh.creator.primitives.TorusCreator;
import mesh.creator.primitives.TruncatedConeCreator;
import mesh.creator.primitives.TubeCreator;
import mesh.creator.primitives.UVSphereCreator;
import mesh.creator.primitives.WedgeCreator;
import util.MeshTest;

public class PrimitivesCreatorsManifoldTest {

    @Test
    public void manifoldBox() {
        BoxCreator creator = new BoxCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldCapsule() {
        CapsuleCreator creator = new CapsuleCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldCone() {
        ConeCreator creator = new ConeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldCube() {
        CubeCreator creator = new CubeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldCylinder() {
        CylinderCreator creator = new CylinderCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldDoubleCone() {
        DoubleConeCreator creator = new DoubleConeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldFlatTopPyramidCreator() {
        FlatTopPyramidCreator creator = new FlatTopPyramidCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldHalfUVSphere() {
        HalfUVSphere creator = new HalfUVSphere();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldHelix() {
        HelixCreator creator = new HelixCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldIcoSphere() {
        IcoSphereCreator creator = new IcoSphereCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldQuadCapCylinder() {
        QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldQuadSphere() {
        QuadSphereCreator creator = new QuadSphereCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldSegmentedBox() {
        SegmentedBoxCreator creator = new SegmentedBoxCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldSegmentedCylinder() {
        SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldSegmentedTube() {
        SegmentedTubeCreator creator = new SegmentedTubeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldSolidArc() {
        SolidArcCreator creator = new SolidArcCreator();
        creator.setCapEnd(true);
        creator.setCapStart(true);
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldSquareBasedPyramid() {
        SquareBasedPyramidCreator creator = new SquareBasedPyramidCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldTorus() {
        TorusCreator creator = new TorusCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldTruncatedCone() {
        TruncatedConeCreator creator = new TruncatedConeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldTube() {
        TubeCreator creator = new TubeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldUVSphere() {
        UVSphereCreator creator = new UVSphereCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldWedge() {
        WedgeCreator creator = new WedgeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

}
