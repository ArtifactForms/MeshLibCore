package mesh.creator.test.manifold;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
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
import mesh.creator.primitives.QuadCapCapsule;
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

public class PrimitivesCreatorsManifoldTest {
	
	private void test(IMeshCreator creator) {
		Mesh3D mesh = creator.create();
		new ManifoldTest(mesh).assertIsManifold();
	}
	
	@Test
	public void manifoldBox() {
		test(new BoxCreator());
	}
	
	@Test
	public void manifoldCapsule() {
		test(new CapsuleCreator());
	}
	
	@Test
	public void manifoldCone() {
		test(new ConeCreator());
	}
	
	@Test
	public void manifoldCube() {
		test(new CubeCreator());
	}
	
	@Test
	public void manifoldCylinder() {
		test(new CylinderCreator());
	}
	
	@Test
	public void manifoldDoubleCone() {
		test(new DoubleConeCreator());
	}
	
	@Test
	public void manifoldFlatTopPyramidCreator() {
		test(new FlatTopPyramidCreator());
	}

	@Test
	public void manifoldHalfUVSphere() {
		test(new HalfUVSphere());
	}
	
	@Test
	public void manifoldHelix() {
		test(new HelixCreator());
	}
	
	@Test
	public void manifoldIcoSphere() {
		test(new IcoSphereCreator());
	}
	
	@Test
	public void manifoldQuadCapCapsule() {
		test(new QuadCapCapsule());
	}
	
	@Test
	public void manifoldQuadCapCylinder() {
		test(new QuadCapCylinderCreator());
	}
	
	@Test
	public void manifoldQuadSphere() {
		test(new QuadSphereCreator());
	}
	
	@Test
	public void manifoldSegmentedBox() {
		test(new SegmentedBoxCreator());
	}
	
	@Test
	public void manifoldSegmentedCylinder() {
		test(new SegmentedCylinderCreator());
	}
	
	@Test
	public void manifoldSegmentedTube() {
		test(new SegmentedTubeCreator());
	}
	
	@Test
	public void manifoldSolidArc() {
		SolidArcCreator creator = new SolidArcCreator();
		creator.setCapEnd(true);
		creator.setCapStart(true);
		test(creator);
	}
	
	@Test
	public void manifoldSquareBasedPyramid() {
		test(new SquareBasedPyramidCreator());
	}
	
	@Test
	public void manifoldTorus() {
		test(new TorusCreator());
	}
	
	@Test
	public void manifoldTruncatedCone() {
		test(new TruncatedConeCreator());
	}
	
	@Test
	public void manifoldTube() {
		test(new TubeCreator());
	}
	
	@Test
	public void manifoldUVSphere() {
		test(new UVSphereCreator());
	}
	
	@Test
	public void manifoldWedge() {
		test(new WedgeCreator());
	}
	
}
