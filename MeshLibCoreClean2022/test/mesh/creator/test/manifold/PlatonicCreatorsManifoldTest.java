package mesh.creator.test.manifold;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.platonic.DodecahedronCreator;
import mesh.creator.platonic.HexahedronCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.creator.platonic.OctahedronCreator;
import mesh.creator.platonic.TetrahedronCreator;

public class PlatonicCreatorsManifoldTest {

	private void test(IMeshCreator creator) {
		Mesh3D mesh = creator.create();
		new ManifoldTest(mesh).assertIsManifold();
	}
	
	@Test
	public void manifoldDodecahedron() {
		test(new DodecahedronCreator());
	}
	
	@Test
	public void manifoldHexahedron() {
		test(new HexahedronCreator());
	}
	
	@Test
	public void manifoldIcosahedron() {
		test(new IcosahedronCreator());
	}
	
	@Test
	public void manifoldOctahedron() {
		test(new OctahedronCreator());
	}
	
	@Test
	public void manifoldTetrahedron() {
		test(new TetrahedronCreator());
	}
 	
}
