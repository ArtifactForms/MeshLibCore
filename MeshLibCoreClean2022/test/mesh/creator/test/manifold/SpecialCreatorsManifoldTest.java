package mesh.creator.test.manifold;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.special.AccordionTorusCreator;
import mesh.creator.special.AntiprismCreator;
import mesh.creator.special.DualCreator;
import mesh.creator.special.HoneyCombCreator;
import mesh.creator.special.SimpleStarCreator;
import mesh.creator.special.VariableCylinderCreator;
import util.MeshTest;

public class SpecialCreatorsManifoldTest {

    @Test
    public void manifoldAccordionTorus() {
        AccordionTorusCreator creator = new AccordionTorusCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldAntiprism() {
        AntiprismCreator creator = new AntiprismCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldDualCubeSeed() {
        DualCreator creator = new DualCreator(new CubeCreator().create());
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldHoneyComb() {
        HoneyCombCreator creator = new HoneyCombCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldSimpleStar() {
        SimpleStarCreator creator = new SimpleStarCreator();
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

    @Test
    public void manifoldVariableCylinder() {
        VariableCylinderCreator creator = new VariableCylinderCreator();
        creator.setRotationSegments(32);
        creator.add(2, 1);
        creator.add(1, 3);
        Mesh3D mesh = creator.create();
        assertTrue(MeshTest.isManifold(mesh));
    }

}
