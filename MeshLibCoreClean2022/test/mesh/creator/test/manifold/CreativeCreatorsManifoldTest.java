package mesh.creator.test.manifold;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.creative.CubicLatticeCreator;
import mesh.creator.creative.LeonardoCubeCreator;
import mesh.creator.creative.PortedCubeCreator;
import mesh.creator.creative.RingCageCreator;
import mesh.creator.creative.TessellationRingCreator;
import mesh.creator.creative.TorusCageCreator;

public class CreativeCreatorsManifoldTest {

	private void test(IMeshCreator creator) {
		Mesh3D mesh = creator.create();
		new ManifoldTest(mesh).assertIsManifold();
	}
	
	@Test
	public void manifoldCubicLattice() {
		test(new CubicLatticeCreator());
	}
	
	@Test
	public void manifoldLeonardoCube() {
		test(new LeonardoCubeCreator());
	}	
	
	@Test
	public void manifoldPortedCube() {
		test(new PortedCubeCreator());
	}
	
	@Test
	public void manifoldRingCageCreator() {
		test(new RingCageCreator());
	}
	
	@Test
	public void manifoldTessellationRing() {
		test(new TessellationRingCreator());
	}
	
	@Test
	public void manifoldTorusCage() {
		test(new TorusCageCreator());
	}
	
}
