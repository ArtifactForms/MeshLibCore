package mesh.creator.test.manifold;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.creative.CubicLatticeCreator;
import mesh.creator.creative.LeonardoCubeCreator;
import mesh.creator.creative.PortedCubeCreator;
import mesh.creator.creative.RingCageCreator;
import mesh.creator.creative.TessellationRingCreator;
import mesh.creator.creative.TorusCageCreator;
import util.MeshTest;

public class CreativeCreatorsManifoldTest {

    @Test
    public void manifoldCubicLattice() {
        CubicLatticeCreator creator = new CubicLatticeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldLeonardoCube() {
        LeonardoCubeCreator creator = new LeonardoCubeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldPortedCube() {
        PortedCubeCreator creator = new PortedCubeCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldRingCageCreator() {
        RingCageCreator creator = new RingCageCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldTessellationRing() {
        TessellationRingCreator creator = new TessellationRingCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldTorusCage() {
        TorusCageCreator creator = new TorusCageCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

}
