package mesh.creator.special;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class AntiprismCreator implements IMeshCreator {

	private int n;

	public AntiprismCreator() {
		this(6);
	}

	public AntiprismCreator(int n) {
		this.n = n;
	}

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();

		int n2 = n + n;
		int[] indices0 = new int[n];
		int[] indices1 = new int[n];
		float h = Mathf.sqrt(((Mathf.cos(Mathf.PI / n)) - (Mathf.cos(Mathf.TWO_PI / n))) / 2.0f);

		for (int k = 0; k < 2 * n; k++) {
			float x = Mathf.cos(k * Mathf.PI / n);
			float z = Mathf.sin(k * Mathf.PI / n);
			float y = Mathf.pow(-1, k) * h;
			mesh.addVertex(x, y, z);
		}

		for (int i = 0; i < 2 * n; i++) {
			if (i % 2 == 1) {
				mesh.addFace(i % n2, (i + 1) % n2, (i + 2) % n2);
				indices0[i / 2] = i;
			} else {
				mesh.addFace(i % n2, (i + 2) % n2, (i + 1) % n2);
				indices1[(n - 1) - (i / 2)] = i;
			}
		}

		mesh.add(new Face3D(indices0));
		mesh.add(new Face3D(indices1));

		return mesh;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

}
