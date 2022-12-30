package mesh.creator.test.manifold;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.special.AccordionTorusCreator;
import mesh.creator.special.AntiprismCreator;
import mesh.creator.special.DualCreator;
import mesh.creator.special.HoneyCombCreator;
import mesh.creator.special.SimpleStarCreator;
import mesh.creator.special.VariableCylinderCreator;

public class SpecialCreatorsManifoldTest {

	private void test(IMeshCreator creator) {
		Mesh3D mesh = creator.create();
		new ManifoldTest(mesh).assertIsManifold();
	}

	@Test
	public void manifoldAccordionTorus() {
		test(new AccordionTorusCreator());
	}

	@Test
	public void manifoldAntiprism() {
		test(new AntiprismCreator());
	}

	@Test
	public void manifoldDualCubeSeed() {
		DualCreator creator = new DualCreator(new CubeCreator().create());
		test(creator);
	}

	@Test
	public void manifoldHoneyComb() {
		test(new HoneyCombCreator());
	}

	@Test
	public void manifoldSimpleStar() {
		test(new SimpleStarCreator());
	}

	@Test
	public void manifoldVariableCylinder() {
		VariableCylinderCreator creator = new VariableCylinderCreator();
		creator.setRotationSegments(32);
		creator.add(2, 1);
		creator.add(1, 3);
		test(creator);
	}

}
