package mesh.creator.test.manifold;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.platonic.DodecahedronCreator;
import mesh.creator.platonic.HexahedronCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.creator.platonic.OctahedronCreator;
import mesh.creator.platonic.TetrahedronCreator;
import util.MeshTest;

public class PlatonicCreatorsManifoldTest {

    @Test
    public void manifoldDodecahedron() {
        DodecahedronCreator creator = new DodecahedronCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldHexahedron() {
        HexahedronCreator creator = new HexahedronCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldIcosahedron() {
        IcosahedronCreator creator = new IcosahedronCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldOctahedron() {
        OctahedronCreator creator = new OctahedronCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldTetrahedron() {
        TetrahedronCreator creator = new TetrahedronCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

}
