package mesh.modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import mesh.Face3D;
import mesh.Mesh3D;

public class RandomHolesModifier implements IMeshModifier, FaceModifier {

	private float minAmount;

	private float maxAmount;

	private long seed;

	private Random random;

	private ExtrudeModifier modifier;

	public RandomHolesModifier() {
		this(0.1f, 0.9f);
	}

	public RandomHolesModifier(float minAmount, float maxAmount) {
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.random = new Random(seed);
		this.modifier = new ExtrudeModifier();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		return modify(mesh, new ArrayList<Face3D>(mesh.faces));
	}

	@Override
	public Mesh3D modify(Mesh3D mesh, Face3D face) {
		if (mesh == null || face == null)
			throw new IllegalArgumentException();

		makeHole(mesh, face);
		mesh.removeFace(face);
		return mesh;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
		if (mesh == null || faces == null)
			throw new IllegalArgumentException();

		for (Face3D face : faces)
			makeHole(mesh, face);

		mesh.faces.removeAll(faces);
		return mesh;
	}

	private void makeHole(Mesh3D mesh, Face3D face) {
		float amount = createRandomAmount();
		modifier.setScale(amount);
		modifier.modify(mesh, face);
	}

	private float createRandomAmount() {
		return minAmount + random.nextFloat() * (maxAmount - minAmount);
	}

	public float getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(float minAmount) {
		this.minAmount = minAmount;
	}

	public float getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(float maxAmount) {
		this.maxAmount = maxAmount;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
		random = new Random(seed);
	}

}
